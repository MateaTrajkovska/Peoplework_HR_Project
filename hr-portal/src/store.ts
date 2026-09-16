import { reactive } from 'vue';
import axios from 'axios';
import type {
    Employee,
    Department,
    Contract,
    Leave,
    Plan,
    Template,
    Holiday,
    DashboardData,
} from './types';

export const api = axios.create({
    baseURL: '/api',
    timeout: 20000,
    withCredentials: true,
});

export const state = reactive({
    user: null as null | { username: string; role: string },
    ready: false,
    loading: false,
    error: '',
    toast: '',
    employees: [] as Employee[],
    departments: [] as Department[],
    contracts: [] as Contract[],
    leaves: [] as Leave[],
    plans: [] as Plan[],
    templates: [] as Template[],
    holidays: [] as Holiday[],
    year: new Date().getFullYear(),
    dashboard: {
        employees: 0,
        contracts: 0,
        pending: 0,
        away: 0,
        activities: [],
    } as DashboardData,
});

let timer: ReturnType<typeof setTimeout>;

export function notify(message: string) {
    state.toast = message;
    clearTimeout(timer);
    timer = setTimeout(() => (state.toast = ''), 4500);
}

export function errorMessage(e: unknown) {
    return axios.isAxiosError(e)
        ? e.response?.data?.message ||
        (e.response?.status === 401
            ? 'Your session has ended. Please sign in again.'
            : e.response?.status === 403
                ? 'Your security token expired. Refresh the page and try again.'
                : 'Cannot reach the server. Check that the backend is running.')
        : e instanceof Error
            ? e.message
            : 'Something went wrong.';
}

api.interceptors.response.use(
    (r) => r,
    (e) => {
        if (e.response?.status === 401) state.user = null;
        return Promise.reject(e);
    }
);

export async function csrf() {
    const { data } = await api.get('/auth/csrf');
    api.defaults.headers.common[data.headerName] = data.token;
}

export async function init() {
    try {
        await csrf();
        state.user = (await api.get('/auth/me')).data;
        if (state.user) await load();
    } catch (e) {
        if (!axios.isAxiosError(e) || e.response?.status !== 401) {
            state.error = errorMessage(e);
        }
    } finally {
        state.ready = true;
    }
}

export async function login(username: string, password: string) {
    await csrf();
    await api.post(
        '/auth/login',
        new URLSearchParams({ username, password }),
        { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }
    );
    await csrf();
    state.user = (await api.get('/auth/me')).data;
    await load();
}

export async function logout() {
    await api.post('/auth/logout');
    state.user = null;
    state.employees = [];
    state.contracts = [];
    state.leaves = [];
    state.plans = [];
    await csrf();
}

export async function load() {
    state.loading = true;
    state.error = '';
    try {
        const [
            employees,
            departments,
            contracts,
            leaves,
            plans,
            templates,
            holidays,
            dashboard,
        ] = await Promise.all(
            [
                '/employees',
                '/departments',
                '/contracts',
                '/leaves',
                '/plans?year=' + state.year,
                '/templates',
                '/holidays',
                '/dashboard',
            ].map((p) => api.get(p))
        );
        Object.assign(state, {
            employees: employees.data,
            departments: departments.data,
            contracts: contracts.data,
            leaves: leaves.data,
            plans: plans.data,
            templates: templates.data,
            holidays: holidays.data,
            dashboard: dashboard.data,
        });
    } catch (e) {
        state.error = errorMessage(e);
        throw e;
    } finally {
        state.loading = false;
    }
}

export const person = (id: string) =>
    state.employees.find((e) => e.id === id);

export const fullName = (e?: Employee) =>
    e ? e.firstName + ' ' + e.lastName : 'Unknown employee';

export const initials = (e?: Employee) =>
    e ? (e.firstName[0] + e.lastName[0]).toUpperCase() : '?';

export const date = (value?: string) =>
    value
        ? new Intl.DateTimeFormat('en-GB', {
            day: 'numeric',
            month: 'short',
            year: 'numeric',
        }).format(new Date(value.substring(0, 10) + 'T12:00:00'))
        : '—';

export const today = () => {
    const d = new Date();
    return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(
        2,
        '0'
    )}-${String(d.getDate()).padStart(2, '0')}`;
};

export const label = (v?: string) =>
    v
        ? v
            .toLowerCase()
            .replaceAll('_', ' ')
            .replace(/^./, (c) => c.toUpperCase())
        : '—';

export function contractStatus(c: Contract) {
    return c.status === 'TERMINATED'
        ? 'TERMINATED'
        : c.startDate > today()
            ? 'UPCOMING'
            : c.endDate && c.endDate < today()
                ? 'EXPIRED'
                : 'ACTIVE';
}

export function download(
    name: string,
    text: string,
    type = 'text/plain;charset=utf-8'
) {
    const url = URL.createObjectURL(new Blob([text], { type }));
    const a = document.createElement('a');
    a.href = url;
    a.download = name;
    a.click();
    setTimeout(() => URL.revokeObjectURL(url), 1000);
}

export function csv(name: string, rows: (string | number | undefined)[][]) {
    download(
        name,
        '\uFEFF' +
        rows
            .map((r) =>
                r
                    .map(
                        (v) =>
                            '"' +
                            String(v ?? '')
                                .replace(/^[=+@-]/, "'" + '$&')
                                .replaceAll('"', '""') +
                            '"'
                    )
                    .join(',')
            )
            .join('\r\n'),
        'text/csv;charset=utf-8'
    );
}