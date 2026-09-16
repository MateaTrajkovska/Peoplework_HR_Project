<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import {
  state,
  person,
  fullName,
  date,
  label,
  api,
  csv,
  load,
  contractStatus,
  today
} from '../store';
import {
  employeeForm,
  contractForm,
  leaveForm,
  departmentForm,
  planForm,
  templateForm,
  generateDocument,
  holidayForm,
  confirmAction
} from '../forms';
import Icon from '../components/Icon.vue';
import Person from '../components/Person.vue';
import type { Contract, Leave, Employee } from '../types';

const route = useRoute();
const page = computed(() => route.path.split('/')[1]);

const search = ref('');
const filter = ref('ALL');
const department = ref('ALL');
const current = ref(1);
const size = 8;

const meta: Record<string, { kicker: string; title: string; description: string; action: string; icon: string }> = {
  employees: {
    kicker: 'THE PEOPLE BEHIND THE WORK',
    title: 'Good people. Great possibilities.',
    description: 'A little closer to everyone on your team.',
    action: 'Add employee',
    icon: 'account-group-outline'
  },
  departments: {
    kicker: 'BETTER, TOGETHER',
    title: 'Every team has a part to play.',
    description: 'The people and purposes that make up your organization.',
    action: 'New department',
    icon: 'domain'
  },
  contracts: {
    kicker: 'A STRONG FOUNDATION',
    title: 'Clarity from the very beginning.',
    description: 'Employment agreements, organized and easy to find.',
    action: 'New contract',
    icon: 'file-document-outline'
  },
  leaves: {
    kicker: 'ROOM TO RECHARGE',
    title: 'Time away, thoughtfully managed.',
    description: 'Plan, review and keep everyone in the loop.',
    action: 'Request time off',
    icon: 'beach'
  },
  plans: {
    kicker: 'A HEALTHIER WORKING RHYTHM',
    title: 'Make space for a well-earned break.',
    description: 'Annual allowances, approved days and pending reservations.',
    action: 'Set allowance',
    icon: 'chart-donut'
  },
  calendar: {
    kicker: 'KNOW WHAT’S AHEAD',
    title: 'A little perspective on the month.',
    description: 'See approved time off across the team.',
    action: 'Request time off',
    icon: 'calendar-month-outline'
  },
  templates: {
    kicker: 'THE DETAILS, TAKEN CARE OF',
    title: 'A head start on the paperwork.',
    description: 'Reusable templates, personalized with employee details.',
    action: 'New template',
    icon: 'folder-outline'
  },
  settings: {
    kicker: 'MAKE IT WORK FOR YOUR PEOPLE',
    title: 'Your workspace, considered.',
    description: 'Account information and your organization’s working calendar.',
    action: 'Add holiday',
    icon: 'cog-outline'
  }
};

const heading = computed(() => meta[page.value]);

watch(
    () => [route.path, route.query.department],
    () => {
      search.value = '';
      filter.value = 'ALL';
      current.value = 1;

      department.value =
          route.path === '/employees' &&
          typeof route.query.department === 'string'
              ? route.query.department
              : 'ALL';
    },
    { immediate: true }
);

watch([search, filter, department], () => (current.value = 1));

const filteredEmployees = computed(() =>
    state.employees.filter(
        (e) =>
            (filter.value === 'ALL' || e.status === filter.value) &&
            (department.value === 'ALL' || e.departmentId === department.value) &&
            [fullName(e), e.email, e.jobTitle, String(e.employeeNumber)].some((v) =>
                v?.toLowerCase().includes(search.value.toLowerCase())
            )
    )
);

const filteredContracts = computed(() =>
    state.contracts
        .filter(
            (c) =>
                (filter.value === 'ALL' || contractStatus(c) === filter.value) &&
                (c.number + ' ' + fullName(person(c.employeeId)))
                    .toLowerCase()
                    .includes(search.value.toLowerCase())
        )
        .sort((a, b) => b.startDate.localeCompare(a.startDate))
);

const filteredLeaves = computed(() =>
    state.leaves.filter(
        (l) =>
            (filter.value === 'ALL' || l.status === filter.value) &&
            (l.number + ' ' + fullName(person(l.employeeId)) + ' ' + label(l.type))
                .toLowerCase()
                .includes(search.value.toLowerCase())
    )
);

const filteredPlans = computed(() =>
    state.plans.filter((p) =>
        fullName(person(p.employeeId)).toLowerCase().includes(search.value.toLowerCase())
    )
);

const rows = computed(() =>
    page.value === 'employees'
        ? filteredEmployees.value
        : page.value === 'contracts'
            ? filteredContracts.value
            : page.value === 'leaves'
                ? filteredLeaves.value
                : filteredPlans.value
);

const total = computed(() => rows.value.length);
const pages = computed(() => Math.max(1, Math.ceil(total.value / size)));

watch(pages, (n) => (current.value = Math.min(n, current.value)));

function slice<T>(r: T[]): T[] {
  return r.slice((current.value - 1) * size, current.value * size);
}

function add() {
  (
      {
        employees: () => employeeForm(),
        departments: () => departmentForm(),
        contracts: () => contractForm(),
        leaves: () => leaveForm(),
        plans: () => planForm(),
        calendar: () => leaveForm(),
        templates: () => templateForm(),
        settings: () => holidayForm()
      } as Record<string, () => void>
  )[page.value]?.();
}

function decision(l: Leave, status: string) {
  confirmAction(
      (status === 'APPROVED' ? 'Approve' : status === 'DENIED' ? 'Decline' : 'Cancel') +
      ' this request?',
      fullName(person(l.employeeId)) +
      ' · ' +
      date(l.fromDate) +
      ' – ' +
      date(l.toDate) +
      '. ' +
      (status === 'CANCELLED'
          ? 'Any approved vacation days will be returned to the balance.'
          : 'The vacation balance will update automatically.'),
      () => api.post('/leaves/' + l.id + '/decision', { status }),
      status === 'APPROVED' ? 'Approve request' : status === 'DENIED' ? 'Decline request' : 'Cancel request',
      status !== 'APPROVED'
  );
}

function endContract(c: Contract) {
  confirmAction(
      'Terminate ' + c.number + '?',
      'This marks the agreement as terminated and retains its history. Employee archival is managed separately on the employee profile.',
      () => api.post('/contracts/' + c.id + '/terminate'),
      'Terminate contract',
      true
  );
}

async function changeYear() {
  try {
    await load();
  } catch {}
}

function exportRows() {
  if (page.value === 'employees') {
    csv('employees.csv', [
      ['Number', 'Name', 'Email', 'Job title', 'Department', 'Status'],
      ...filteredEmployees.value.map((e) => [
        e.employeeNumber,
        fullName(e),
        e.email,
        e.jobTitle,
        e.department,
        e.status
      ])
    ]);
  } else if (page.value === 'contracts') {
    csv('contracts.csv', [
      ['Number', 'Employee', 'Type', 'Start', 'End', 'Gross salary', 'Currency', 'Status'],
      ...filteredContracts.value.map((c) => [
        c.number,
        fullName(person(c.employeeId)),
        c.type,
        c.startDate,
        c.endDate,
        c.salary,
        c.currency,
        contractStatus(c)
      ])
    ]);
  } else if (page.value === 'leaves') {
    csv('leave-requests.csv', [
      ['Number', 'Employee', 'Type', 'From', 'To', 'Working days', 'Status'],
      ...filteredLeaves.value.map((l) => [
        l.number,
        fullName(person(l.employeeId)),
        l.type,
        l.fromDate,
        l.toDate,
        l.days,
        l.status
      ])
    ]);
  } else {
    csv('leave-balances.csv', [
      ['Employee', 'Year', 'Allowance', 'Approved', 'Reserved', 'Available'],
      ...filteredPlans.value.map((p) => [
        fullName(person(p.employeeId)),
        p.year,
        p.maxDays,
        p.usedDays,
        p.reservedDays,
        p.remainingDays
      ])
    ]);
  }
}

const month = ref(new Date(new Date().getFullYear(), new Date().getMonth(), 1));

const monthTitle = computed(() =>
    month.value.toLocaleDateString('en-GB', { month: 'long', year: 'numeric' })
);

const monthDays = computed(() => {
  const start = new Date(month.value);
  const offset = (start.getDay() + 6) % 7;
  start.setDate(start.getDate() - offset);
  return Array.from({ length: 42 }, (_, i) => {
    const d = new Date(start);
    d.setDate(start.getDate() + i);
    return {
      key: `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(
          d.getDate()
      ).padStart(2, '0')}`,
      day: d.getDate(),
      outside: d.getMonth() !== month.value.getMonth(),
      weekend: d.getDay() === 0 || d.getDay() === 6
    };
  });
});

function moveMonth(n: number) {
  month.value = new Date(month.value.getFullYear(), month.value.getMonth() + n, 1);
}

const calendarLeaves = (key: string) =>
    state.leaves.filter(
        (l) =>
            l.status === 'APPROVED' &&
            l.fromDate <= key &&
            l.toDate >= key &&
            (department.value === 'ALL' || person(l.employeeId)?.departmentId === department.value)
    );

</script>

<template>
  <div class="page-heading">
    <div>
      <span class="eyebrow">{{ heading.kicker }}</span>
      <h1>{{ heading.title }}</h1>
      <p>{{ heading.description }}</p>
    </div>
    <button class="btn primary" @click="add">
      <Icon name="plus" />{{ heading.action }}
    </button>
  </div>

  <template v-if="page==='employees'||page==='contracts'||page==='leaves'||page==='plans'">

    <div class="panel table-panel">
      <div class="table-tabs">
        <div class="tabs" v-if="page!=='plans'">
          <button
              v-for="s in (page==='employees'
              ? ['ALL','ACTIVE','ARCHIVED']
              : page==='contracts'
              ? ['ALL','ACTIVE','UPCOMING','EXPIRED','TERMINATED']
              : ['ALL','PENDING','APPROVED','DENIED','CANCELLED'])"
              :key="s"
              :class="{ selected: filter===s }"
              @click="filter=s"
          >
            {{ s==='ALL' ? 'All ' + (page==='employees' ? 'people' : page==='contracts' ? 'contracts' : 'requests') : label(s) }}
            <span v-if="s==='ALL'">{{ page==='employees' ? state.employees.length : page==='contracts' ? state.contracts.length : state.leaves.length }}</span>
          </button>
        </div>
        <div v-else class="year-control">
          <label for="plan-year">Allowance year</label>
          <select id="plan-year" v-model="state.year" @change="changeYear">
            <option
                v-for="y in [new Date().getFullYear()-1, new Date().getFullYear(), new Date().getFullYear()+1]"
                :key="y"
                :value="y"
            >{{ y }}</option>
          </select>
        </div>
        <button
            v-if="page==='plans'"
            class="text-link"
            @click="confirmAction(
            'Generate '+state.year+' allowances?',
            'Create missing annual plans for active employees using the original experience and education calculation. Existing plans remain unchanged.',
            () => api.post('/plans/generate?year='+state.year),
            'Generate plans'
          )"
        >
          <Icon name="auto-fix" />Generate missing plans
        </button>
        <button class="btn small-btn" @click="exportRows">
          <Icon name="tray-arrow-down" />Export CSV
        </button>
      </div>

      <div class="table-toolbar">
        <div class="search-field">
          <Icon name="magnify" />
          <input
              v-model="search"
              :placeholder="page==='employees' ? 'Search by name, email, role or employee number…' : 'Search by employee or record…'"
              aria-label="Search records"
          />
          <button v-if="search" class="icon-btn" aria-label="Clear search" @click="search=''">
            <Icon name="close" />
          </button>
        </div>
        <select v-if="page==='employees'" v-model="department" aria-label="Filter by department">
          <option value="ALL">All departments</option>
          <option v-for="d in state.departments" :key="d.id" :value="d.id">{{ d.name }}</option>
        </select>
        <span class="results-count">{{ total }} {{ total===1 ? 'result' : 'results' }}</span>
      </div>

      <div class="table-scroll">
        <table v-if="page==='employees'">
          <thead>
          <tr>
            <th>Employee</th>
            <th>Job title / Department</th>
            <th>Work arrangement</th>
            <th>Joined</th>
            <th>Status</th>
            <th><span class="sr-only">Actions</span></th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="e in slice(filteredEmployees)" :key="e.id">
            <td>
              <RouterLink :to="'/employees/'+e.id+'/detail'">
                <Person :employee="e" />
              </RouterLink>
            </td>
            <td><strong>{{ e.jobTitle }}</strong><small>{{ e.department }}</small></td>
            <td>
                <span class="work-mode">
                  <Icon :name="e.workMode==='Remote' ? 'home-outline' : 'office-building-outline'" />{{ e.workMode || 'Office' }}
                </span>
              <small>{{ e.location }}</small>
            </td>
            <td>{{ date(e.startDate) }}</td>
            <td><span class="badge" :class="e.status.toLowerCase()">{{ label(e.status) }}</span></td>
            <td>
              <RouterLink :to="'/employees/'+e.id+'/detail'" class="icon-btn" :aria-label="'View '+fullName(e)">
                <Icon name="arrow-top-right" />
              </RouterLink>
            </td>
          </tr>
          </tbody>
        </table>

        <table v-else-if="page==='contracts'">
          <thead>
          <tr>
            <th>Agreement</th>
            <th>Employee</th>
            <th>Term</th>
            <th>Monthly gross</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="c in slice(filteredContracts)" :key="c.id">
            <td><strong>{{ c.number }}</strong><small>{{ label(c.type) }}</small></td>
            <td><Person :employee="person(c.employeeId)" :subtitle="person(c.employeeId)?.jobTitle" /></td>
            <td>
              {{ date(c.startDate) }}
              <small>{{ c.endDate ? 'Until '+date(c.endDate) : 'Open-ended agreement' }}</small>
            </td>
            <td>{{ new Intl.NumberFormat('en-GB', { style: 'currency', currency: c.currency, maximumFractionDigits: 0 }).format(c.salary) }}</td>
            <td><span class="badge" :class="contractStatus(c).toLowerCase()">{{ label(contractStatus(c)) }}</span></td>
            <td>
              <div class="row-actions">
                <button
                    class="icon-btn"
                    v-if="c.status!=='TERMINATED' && person(c.employeeId)?.status==='ACTIVE'"
                    aria-label="Edit contract"
                    title="Edit contract"
                    @click="contractForm(c)"
                >
                  <Icon name="pencil-outline" />
                </button>
                <button
                    class="icon-btn"
                    v-if="c.status!=='TERMINATED' && person(c.employeeId)?.status==='ACTIVE'"
                    aria-label="Terminate contract"
                    title="Terminate contract"
                    @click="endContract(c)"
                >
                  <Icon name="file-cancel-outline" />
                </button>
              </div>
            </td>
          </tr>
          </tbody>
        </table>

        <table v-else-if="page==='leaves'">
          <thead>
          <tr>
            <th>Employee</th>
            <th>Leave type</th>
            <th>Dates</th>
            <th>Days</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="l in slice(filteredLeaves)" :key="l.id">
            <td><Person :employee="person(l.employeeId)" :subtitle="l.number" /></td>
            <td><strong>{{ label(l.type) }}</strong><small class="truncate" :title="l.reason">{{ l.reason || 'No additional context' }}</small></td>
            <td>{{ date(l.fromDate) }}<small>to {{ date(l.toDate) }}</small></td>
            <td><b>{{ l.days }}</b><small>working days</small></td>
            <td><span class="badge" :class="l.status.toLowerCase()">{{ label(l.status) }}</span></td>
            <td>
              <div class="row-actions">
                <button v-if="l.status==='PENDING'" class="approve-icon" aria-label="Approve leave" title="Approve" @click="decision(l,'APPROVED')">
                  <Icon name="check" />
                </button>
                <button v-if="l.status==='PENDING'" class="icon-btn" aria-label="Decline leave" title="Decline" @click="decision(l,'DENIED')">
                  <Icon name="close" />
                </button>
                <button v-if="l.status==='APPROVED'" class="text-btn" @click="decision(l,'CANCELLED')">Cancel</button>
              </div>
            </td>
          </tr>
          </tbody>
        </table>

        <table v-else>
          <thead>
          <tr>
            <th>Employee</th>
            <th>Allowance</th>
            <th>Approved</th>
            <th>Reserved</th>
            <th>Available</th>
            <th>Balance</th>
            <th><span class="sr-only">Actions</span></th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="p in slice(filteredPlans)" :key="p.id">
            <td><Person :employee="person(p.employeeId)" :subtitle="person(p.employeeId)?.department" /></td>
            <td>{{ p.maxDays }} days</td>
            <td>{{ p.usedDays }}</td>
            <td>{{ p.reservedDays }}</td>
            <td><strong>{{ p.remainingDays }} days</strong></td>
            <td>
              <div class="balance-track">
                <span :style="{ width: Math.min(100, (p.usedDays+p.reservedDays)/Math.max(1,p.maxDays)*100)+'%' }"></span>
              </div>
              <small>Valid through {{ date(p.validTo) }}</small>
            </td>
            <td>
              <button v-if="person(p.employeeId)?.status==='ACTIVE'" class="icon-btn" aria-label="Edit allowance" title="Edit allowance" @click="planForm(p)">
                <Icon name="pencil-outline" />
              </button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <div v-if="!total" class="empty">
        <span class="empty-icon"><Icon :name="heading.icon" /></span>
        <h3>{{ search||filter!=='ALL' ? 'No matches just yet.' : 'A fresh start.' }}</h3>
        <p>{{ search||filter!=='ALL' ? 'Try another search or adjust your filters.' : 'Add your first record to bring this space to life.' }}</p>
        <button class="btn" @click="search||filter!=='ALL' ? (search='', filter='ALL', department='ALL') : add()">
          {{ search||filter!=='ALL' ? 'Clear filters' : heading.action }}
        </button>
      </div>

      <div class="pagination">
        <span>Showing {{ total ? ((current-1)*size+1) : 0 }}–{{ Math.min(current*size, total) }} of {{ total }} records</span>
        <div>
          <button class="icon-btn" :disabled="current===1" aria-label="Previous page" @click="current--">
            <Icon name="chevron-left" />
          </button>
          <button v-for="n in pages" :key="n" class="page-number" :class="{ selected: n===current }" @click="current=n">{{ n }}</button>
          <button class="icon-btn" :disabled="current===pages" aria-label="Next page" @click="current++">
            <Icon name="chevron-right" />
          </button>
        </div>
      </div>
    </div>

    <p v-if="page==='plans'" class="footnote">
      <Icon name="information-outline" /> Available days exclude pending reservations. Previous-year days can be used through 30 June. Weekends and your configured holidays do not count.
    </p>
  </template>

  <div v-else-if="page==='departments'" class="card-grid">
    <article v-for="(d,i) in state.departments" :key="d.id" class="panel department-card">
      <div class="card-top">
        <span class="department-symbol" :class="['green','purple','orange','blue'][i%4]">
          <Icon :name="['code-braces','flower-tulip-outline','palette-outline','cog-outline','bullhorn-outline'][i%5]" />
        </span>
        <button class="icon-btn" aria-label="Edit department" @click="departmentForm(d)">
          <Icon name="pencil-outline" />
        </button>
      </div>
      <h2>{{ d.name }}</h2>
      <p>{{ d.description || 'A team with its own unique contribution.' }}</p>
      <div class="card-members">
        <div class="avatar-stack">
          <span
              v-for="e in state.employees.filter(e=>e.departmentId===d.id && e.status==='ACTIVE').slice(0,4)"
              :key="e.id"
              class="avatar small"
              :class="e.avatarColor"
          >{{ e.firstName[0] }}{{ e.lastName[0] }}</span>
        </div>
        <span>{{ state.employees.filter(e=>e.departmentId===d.id && e.status==='ACTIVE').length }} active members</span>
      </div>
      <footer>
        <RouterLink :to="{ path: '/employees', query: { department: d.id } }" class="text-link">Explore people <Icon name="arrow-right" /></RouterLink>
        <button
            class="icon-btn"
            aria-label="Delete department"
            title="Delete empty department"
            @click="confirmAction(
            'Delete '+d.name+'?',
            'Only departments with no assigned employees can be deleted.',
            () => api.delete('/departments/'+d.id),
            'Delete department',
            true
          )"
        >
          <Icon name="trash-can-outline" />
        </button>
      </footer>
    </article>
    <button class="add-card" @click="departmentForm()">
      <Icon name="plus-circle-outline" />
      <strong>A place for a new team</strong>
      <span>Create a department</span>
    </button>
  </div>

  <template v-else-if="page==='templates'">
    <div class="info-banner">
      <Icon name="file-check-outline" />
      <div>
        <strong>A useful starting point, every time.</strong>
        <p>Download personalized, editable text documents. Review each draft before issuing or signing.</p>
      </div>
    </div>
    <div class="card-grid">
      <article v-for="(t,i) in state.templates" :key="t.id" class="panel document-card">
        <div class="document-preview" :class="['sage','lavender','sand'][i%3]">
          <div class="paper">
            <span>PEOPLEWORK</span>
            <strong>{{ t.name }}</strong>
            <i></i><i></i><i></i><i></i>
            <div>________________</div>
          </div>
          <span class="document-format">TXT</span>
        </div>
        <div class="document-content">
          <span class="eyebrow">{{ t.category }}</span>
          <h3>{{ t.name }}</h3>
          <p>Personalized with your team’s details.</p>
          <div class="row-actions">
            <button class="btn small-btn" @click="generateDocument(t)">
              <Icon name="tray-arrow-down" />Use template
            </button>
            <button class="icon-btn" aria-label="Edit template" @click="templateForm(t)">
              <Icon name="pencil-outline" />
            </button>
            <button
                class="icon-btn"
                aria-label="Delete template"
                @click="confirmAction(
                'Delete this template?',
                'Downloaded documents remain on your computer.',
                () => api.delete('/templates/'+t.id),
                'Delete template',
                true
              )"
            >
              <Icon name="trash-can-outline" />
            </button>
          </div>
        </div>
      </article>
      <button class="add-card" @click="templateForm()">
        <Icon name="file-plus-outline" />
        <strong>Start with a blank page</strong>
        <span>Create your own template</span>
      </button>
    </div>
  </template>

  <template v-else-if="page==='calendar'">
    <div class="panel calendar-panel">
      <div class="panel-heading">
        <div class="month-nav">
          <button class="icon-btn" aria-label="Previous month" @click="moveMonth(-1)">
            <Icon name="chevron-left" />
          </button>
          <h2>{{ monthTitle }}</h2>
          <button class="icon-btn" aria-label="Next month" @click="moveMonth(1)">
            <Icon name="chevron-right" />
          </button>
          <button class="btn small-btn" @click="month=new Date(new Date().getFullYear(),new Date().getMonth(),1)">Today</button>
        </div>
        <select v-model="department" aria-label="Calendar department">
          <option value="ALL">All departments</option>
          <option v-for="d in state.departments" :key="d.id" :value="d.id">{{ d.name }}</option>
        </select>
      </div>

      <div class="calendar-overflow">
        <div class="calendar-week">
          <span v-for="d in ['Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday']" :key="d">{{ d }}</span>
        </div>
        <div class="calendar-grid">
          <div v-for="d in monthDays" :key="d.key" class="calendar-day" :class="{ outside: d.outside, weekend: d.weekend }">
            <span class="day-number" :class="{ today: d.key===today() }">{{ d.day }}</span>
            <div v-for="h in state.holidays.filter(h=>h.date===d.key)" :key="h.id" class="calendar-holiday">{{ h.name }}</div>
            <RouterLink
                v-for="l in calendarLeaves(d.key).slice(0,3)"
                :key="l.id"
                to="/leaves"
                class="calendar-event"
                :class="l.type==='VACATION' ? 'vacation' : 'sick'"
                :title="fullName(person(l.employeeId))+' · '+label(l.type)"
            >
              {{ person(l.employeeId)?.firstName }} {{ person(l.employeeId)?.lastName?.[0] }}.
              <small>{{ label(l.type) }}</small>
            </RouterLink>
            <RouterLink v-if="calendarLeaves(d.key).length>3" to="/leaves" class="more-events">
              +{{ calendarLeaves(d.key).length-3 }} more
            </RouterLink>
          </div>
        </div>
      </div>

      <div class="calendar-legend">
        <span><i class="legend-dot sage"></i>Annual leave</span>
        <span><i class="legend-dot lavender"></i>Other approved absence</span>
        <span>Only approved requests appear here.</span>
      </div>
    </div>
  </template>

  <template v-else-if="page==='settings'">
    <div class="settings-grid">
      <section class="panel settings-card">
        <span class="department-symbol purple"><Icon name="shield-account-outline" /></span>
        <h2>Your account</h2>
        <p>Signed in as an HR administrator.</p>
        <dl>
          <div><dt>Username</dt><dd>{{ state.user?.username }}</dd></div>
          <div><dt>Role</dt><dd>{{ state.user?.role }}</dd></div>
          <div><dt>Session</dt><dd>Expires after 30 minutes of inactivity</dd></div>
        </dl>
        <div class="info-banner">
          <Icon name="lock-outline" />
          <p>Account credentials are managed through the backend environment settings. Contact the workspace owner for changes.</p>
        </div>
      </section>

      <section class="panel settings-card">
        <h2>Working calendar</h2>
        <p>Monday to Friday. Add organization-specific holidays below.</p>
        <div v-if="!state.holidays.length" class="empty compact">
          <Icon name="calendar-blank-outline" />
          <h3>Your holiday calendar is empty.</h3>
          <p>Only weekends are excluded until you add holidays.</p>
          <button class="btn" @click="holidayForm()">Add a holiday</button>
        </div>
        <div
            v-for="h in [...state.holidays].sort((a,b)=>a.date.localeCompare(b.date))"
            :key="h.id"
            class="holiday-row"
        >
          <div><strong>{{ h.name }}</strong><small>{{ date(h.date) }}</small></div>
          <button
              class="icon-btn"
              aria-label="Delete holiday"
              @click="confirmAction(
              'Remove this holiday?',
              'New leave requests will treat this as a working day unless it falls on a weekend. Existing requests keep their recorded day counts.',
              () => api.delete('/holidays/'+h.id),
              'Remove holiday',
              true
            )"
          >
            <Icon name="trash-can-outline" />
          </button>
        </div>
      </section>
    </div>
  </template>
</template>