import { reactive } from 'vue';
import { api, state, load, notify, today, fullName, download } from './store';
import type { Employee, Contract, Department, Template, Plan } from './types';

export interface Field {
    key: string;
    label: string;
    type?: string;
    required?: boolean;
    options?: { value: string; label: string }[];
    wide?: boolean;
    min?: string | number;
    max?: string | number;
    help?: string;
    readonly?: boolean;
}

export const modal = reactive({
    open: false,
    title: '',
    description: '',
    fields: [] as Field[],
    values: {} as Record<string, any>,
    submitText: 'Save changes',
    danger: false,
    submit: null as null | (() => Promise<void>),
    error: '',
    busy: false,
});

const opts = (values: string[]) =>
    values.map((value) => ({
        value,
        label: value
            .replaceAll('_', ' ')
            .toLowerCase()
            .replace(/^./, (s) => s.toUpperCase()),
    }));

const employees = () =>
    state.employees
        .filter((e) => e.status === 'ACTIVE')
        .map((e) => ({
            value: e.id,
            label: fullName(e) + ' · ' + e.employeeNumber,
        }));

function show(
    title: string,
    description: string,
    fields: Field[],
    values: Record<string, any>,
    submit: () => Promise<void>,
    submitText = 'Save changes',
    danger = false
) {
    Object.assign(modal, {
        open: true,
        title,
        description,
        fields,
        values,
        submit,
        submitText,
        danger,
        error: '',
        busy: false,
    });
}

async function save(path: string, id?: string) {
    await api[id ? 'put' : 'post'](path + (id ? '/' + id : ''), modal.values);
    await load();
    notify('Changes saved successfully.');
}

export function employeeForm(e?: Employee) {
    show(
        e ? 'Edit employee' : 'Add a new teammate',
        'The start of a great working relationship. Keep their details in one place.',
        [
            { key: 'firstName', label: 'First name', required: true },
            { key: 'lastName', label: 'Last name', required: true },
            { key: 'email', label: 'Work email', type: 'email', required: true },
            { key: 'phone', label: 'Phone number', required: true },
            { key: 'jobTitle', label: 'Job title', required: true },
            {
                key: 'departmentId',
                label: 'Department',
                type: 'select',
                required: true,
                options: state.departments.map((d) => ({ value: d.id, label: d.name })),
            },
            {
                key: 'employeeType',
                label: 'Employment type',
                type: 'select',
                options: opts(['FULL_TIME', 'PART_TIME', 'EXTERNAL', 'INTERN']),
            },
            { key: 'startDate', label: 'Start date', type: 'date', required: true },
            {
                key: 'dateOfBirth',
                label: 'Date of birth',
                type: 'date',
                required: true,
                max: today(),
            },
            {
                key: 'gender',
                label: 'Gender',
                type: 'select',
                options: opts(['FEMALE', 'MALE', 'OTHER']),
            },
            { key: 'nationality', label: 'Nationality', required: true },
            { key: 'personalId', label: 'Personal identification number' },
            {
                key: 'degree',
                label: 'Education',
                type: 'select',
                options: opts(['NONE', 'BACHELORS', 'MASTERS', 'PHD']),
            },
            {
                key: 'workMode',
                label: 'Work arrangement',
                type: 'select',
                options: opts(['Office', 'Hybrid', 'Remote']),
            },
            { key: 'location', label: 'Work location' },
            { key: 'municipality', label: 'Municipality', required: true },
            { key: 'street', label: 'Street address' },
            { key: 'postalCode', label: 'Postal code' },
            { key: 'bankAccount', label: 'Bank account', help: 'Digits only, up to 18.' },
            { key: 'notes', label: 'Internal HR notes', type: 'textarea', wide: true },
        ],
        e
            ? { ...e }
            : {
                firstName: '',
                lastName: '',
                email: '',
                phone: '',
                jobTitle: '',
                departmentId: state.departments[0]?.id || '',
                employeeType: 'FULL_TIME',
                startDate: today(),
                dateOfBirth: '',
                gender: 'OTHER',
                nationality: 'Macedonian',
                personalId: '',
                degree: 'NONE',
                workMode: 'Hybrid',
                location: 'Skopje, North Macedonia',
                municipality: 'Skopje',
                street: '',
                postalCode: '1000',
                bankAccount: '',
                notes: '',
            },
        () => save('/employees', e?.id),
        'Save employee'
    );
}

export function contractForm(c?: Contract, employeeId?: string) {
    show(
        c ? 'Edit contract' : 'New employment contract',
        'Create an employment record. Dates cannot overlap another active contract.',
        [
            {
                key: 'employeeId',
                label: 'Employee',
                type: 'select',
                options: employees(),
                required: true,
                wide: true,
                readonly: !!c,
            },
            { key: 'number', label: 'Contract number', required: true },
            {
                key: 'type',
                label: 'Contract type',
                type: 'select',
                options: opts(['PERMANENT', 'FIXED_TERM', 'INTERNSHIP', 'PART_TIME']),
                required: true,
            },
            { key: 'startDate', label: 'Start date', type: 'date', required: true },
            {
                key: 'endDate',
                label: 'End date',
                type: 'date',
                help: 'Required for fixed-term contracts.',
            },
            { key: 'salary', label: 'Monthly gross salary', type: 'number', min: 0, required: true },
            {
                key: 'currency',
                label: 'Currency',
                type: 'select',
                options: opts(['MKD', 'EUR', 'USD']),
                required: true,
            },
            { key: 'notes', label: 'Contract notes', type: 'textarea', wide: true },
        ],
        c
            ? { ...c }
            : {
                employeeId: employeeId || '',
                number:
                    'EMP-' +
                    new Date().getFullYear() +
                    '-' +
                    String(state.contracts.length + 1).padStart(3, '0'),
                type: 'PERMANENT',
                startDate: today(),
                endDate: '',
                salary: 0,
                currency: 'MKD',
                notes: '',
            },
        async () => {
            modal.values.endDate ||= null;
            await save('/contracts', c?.id);
        },
        'Save contract'
    );
}

export function leaveForm(employeeId?: string, type = 'VACATION') {
    show(
        'Request time off',
        'Working days exclude weekends and configured holidays. Vacation days are reserved until a decision is made.',
        [
            {
                key: 'employeeId',
                label: 'Employee',
                type: 'select',
                required: true,
                options: employees(),
                wide: true,
            },
            {
                key: 'type',
                label: 'Leave type',
                type: 'select',
                required: true,
                options: opts([
                    'VACATION',
                    'SICK_LEAVE',
                    'PAID_LEAVE',
                    'MATERNITY_LEAVE',
                    'PATERNITY_LEAVE',
                    'MEDICAL_LEAVE',
                ]),
                wide: true,
            },
            { key: 'fromDate', label: 'From', type: 'date', required: true },
            { key: 'toDate', label: 'Through', type: 'date', required: true },
            { key: 'reason', label: 'Reason / additional context', type: 'textarea', wide: true },
        ],
        {
            employeeId: employeeId || '',
            type,
            fromDate: today(),
            toDate: today(),
            reason: '',
        },
        () => save('/leaves'),
        'Submit request'
    );
}

export function departmentForm(d?: Department) {
    show(
        d ? 'Edit department' : 'Create a department',
        'Give your team a home within the organization.',
        [
            { key: 'name', label: 'Department name', required: true, wide: true },
            { key: 'description', label: 'Description', type: 'textarea', wide: true },
        ],
        d ? { ...d } : { name: '', description: '' },
        () => save('/departments', d?.id)
    );
}

export function templateForm(t?: Template) {
    show(
        t ? 'Edit document template' : 'Create a document template',
        'Use {{fullName}}, {{employeeNumber}}, {{jobTitle}}, {{department}}, {{startDate}} and {{today}} to personalize documents.',
        [
            { key: 'name', label: 'Template name', required: true },
            {
                key: 'category',
                label: 'Category',
                required: true,
                type: 'select',
                options: opts(['Employment', 'Leave', 'Onboarding', 'Other']),
            },
            { key: 'body', label: 'Document content', type: 'textarea', required: true, wide: true },
        ],
        t ? { ...t } : { name: '', category: 'Employment', body: '' },
        () => save('/templates', t?.id)
    );
}

export function generateDocument(t: Template) {
    show(
        'Prepare ' + t.name,
        'Generate an editable text document using the selected employee’s current details. Review it before issuing.',
        [
            {
                key: 'employeeId',
                label: 'Employee',
                required: true,
                type: 'select',
                wide: true,
                options: state.employees.map((e) => ({ value: e.id, label: fullName(e) })),
            },
        ],
        { employeeId: '' },
        async () => {
            const e = state.employees.find((e) => e.id === modal.values.employeeId)!;
            let text = t.body;
            const variables = { ...e, fullName: fullName(e), today: today() };

            for (const [key, value] of Object.entries(variables)) {
                text = text.replaceAll('{{' + key + '}}', String(value ?? ''));
            }

            download(
                t.name.replace(/[^a-z0-9 -]/gi, '') + ' - ' + fullName(e) + '.txt',
                text
            );
            notify('Document downloaded.');
        },
        'Download document'
    );
}

export function planForm(p?: Plan) {
    show(
        'Set vacation allowance',
        'Approved and reserved days are protected. Carryover expires on 30 June of the following year.',
        [
            {
                key: 'employeeId',
                label: 'Employee',
                type: 'select',
                required: true,
                options: employees(),
                wide: true,
                readonly: !!p,
            },
            { key: 'year', label: 'Year', type: 'number', min: 2020, max: 2100, required: true },
            {
                key: 'maxDays',
                label: 'Annual allowance (days)',
                type: 'number',
                min: 0,
                max: 100,
                required: true,
            },
        ],
        p
            ? { employeeId: p.employeeId, year: p.year, maxDays: p.maxDays }
            : { employeeId: '', year: state.year, maxDays: 24 },
        () => save('/plans'),
        'Save allowance'
    );
}

export function holidayForm() {
    show(
        'Add a non-working holiday',
        'This date will be excluded from new leave requests. Existing requests keep their recorded working-day count.',
        [
            { key: 'date', label: 'Date', type: 'date', required: true },
            { key: 'name', label: 'Holiday name', required: true },
        ],
        { date: today(), name: '' },
        () => save('/holidays'),
        'Add holiday'
    );
}

export function terminateForm(e: Employee) {
    show(
        'Archive ' + fullName(e),
        'This ends their employment, terminates contracts and cancels pending or future leave. Historical records remain available.',
        [
            { key: 'date', label: 'Termination date', type: 'date', max: today(), required: true },
            { key: 'reason', label: 'Reason', type: 'textarea', required: true, wide: true },
        ],
        { date: today(), reason: '' },
        async () => {
            await api.post('/employees/' + e.id + '/terminate', modal.values);
            await load();
            notify('Employee archived.');
        },
        'Archive employee',
        true
    );
}

export function confirmAction(
    title: string,
    description: string,
    action: () => Promise<unknown>,
    button = 'Confirm',
    danger = false
) {
    show(
        title,
        description,
        [],
        {},
        async () => {
            await action();
            await load();
            notify('Changes saved successfully.');
        },
        button,
        danger
    );
}