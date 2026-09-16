<script setup lang="ts">
import { computed } from 'vue';
import { state, person, fullName, date, today, label, api, contractStatus } from '../store';
import { employeeForm, leaveForm, confirmAction } from '../forms';
import Icon from '../components/Icon.vue';
import Person from '../components/Person.vue';

const pending = computed(() =>
    state.leaves.filter((l) => l.status === 'PENDING').slice(0, 3)
);

const departments = computed(() =>
    state.departments
        .map((d, i) => ({
          ...d,
          count: state.employees.filter((e) => e.departmentId === d.id && e.status === 'ACTIVE').length,
          color: ['#64837a', '#c5a9e9', '#d4df96', '#f1bc8c', '#92b8d7'][i % 5]
        }))
        .sort((a, b) => b.count - a.count)
);

const upcoming = computed(() =>
    state.contracts
        .filter((c) => contractStatus(c) === 'ACTIVE' && c.endDate && c.endDate >= today())
        .sort((a, b) => a.endDate.localeCompare(b.endDate))
        .slice(0, 3)
);

const thisMonth = computed(() =>
    state.employees.filter((e) => e.startDate?.startsWith(today().slice(0, 7))).length
);

const month = new Intl.DateTimeFormat('en-GB', { month: 'long', year: 'numeric' }).format(new Date());

function approve(id: string) {
  confirmAction(
      'Approve this leave request?',
      'The employee’s vacation balance will be updated when approved.',
      () => api.post('/leaves/' + id + '/decision', { status: 'APPROVED' }),
      'Approve request'
  );
}
</script>

<template>
  <div class="page-heading">
    <div>
      <span class="eyebrow">YOUR PEOPLE, AT A GLANCE</span>
      <h1>A good day starts here<span class="heading-dot">.</span></h1>
      <p>Here’s what’s happening across your workplace.</p>
    </div>
    <div class="heading-actions">
      <button class="btn primary" @click="employeeForm()">
        <Icon name="plus" />Add employee
      </button>
    </div>
  </div>

  <section class="overview-banner">
    <div>
      <span class="banner-kicker"><span></span> PEOPLE FIRST. ALWAYS.</span>
      <h2>Less paperwork.<br />More room for your people.</h2>
      <p>A connected view of your team, their time, and what comes next.</p>
      <RouterLink to="/employees">Meet your team <Icon name="arrow-top-right" /></RouterLink>
    </div>
    <div class="banner-visual" aria-hidden="true">
      <div class="sunburst">✳</div>
      <div class="visual-window">
        <span class="window-top"><i></i><i></i><i></i></span>
        <div class="visual-people">
          <span class="avatar orange">AP</span>
          <span class="avatar purple">ES</span>
          <span class="avatar green">MT</span>
        </div>
        <strong>Great things. Together.</strong>
        <div class="window-lines"><i></i><i></i></div>
      </div>
      <span class="floating-star">✦</span>
      <span class="floating-label"><Icon name="heart-outline" />Built around people</span>
    </div>
  </section>

  <section class="stats-grid">
    <RouterLink to="/employees" class="stat-card">
      <span class="stat-top">Active employees <Icon name="account-group-outline" /></span>
      <strong>{{ state.dashboard.employees }}<span class="stat-mini green">Team</span></strong>
      <small>{{ thisMonth }} joined this month</small>
    </RouterLink>
    <RouterLink to="/contracts" class="stat-card">
      <span class="stat-top">Active contracts <Icon name="file-document-outline" /></span>
      <strong>{{ state.dashboard.contracts }}</strong>
      <small>Current employment agreements</small>
    </RouterLink>
    <RouterLink to="/leaves" class="stat-card">
      <span class="stat-top">Pending requests <Icon name="clock-outline" /></span>
      <strong>
        {{ state.dashboard.pending }}
        <span v-if="state.dashboard.pending" class="stat-mini orange">Needs review</span>
      </strong>
      <small>A little attention goes a long way</small>
    </RouterLink>
    <RouterLink to="/calendar" class="stat-card">
      <span class="stat-top">Away today <Icon name="weather-sunny" /></span>
      <strong>{{ state.dashboard.away }}</strong>
      <small>Approved time off, today</small>
    </RouterLink>
  </section>

  <section class="dashboard-columns">
    <div class="panel pending-panel">
      <div class="panel-heading">
        <div>
          <h3>Time off, waiting on you <span class="count">{{ state.dashboard.pending }}</span></h3>
          <p>Help your team plan their next pause.</p>
        </div>
        <RouterLink to="/leaves" class="text-link">View all <Icon name="arrow-right" /></RouterLink>
      </div>

      <div v-if="!pending.length" class="empty compact">
        <Icon name="check-circle-outline" />
        <h3>You’re all caught up.</h3>
        <p>No requests are waiting for your review.</p>
      </div>

      <div v-for="l in pending" :key="l.id" class="request-row">
        <Person :employee="person(l.employeeId)" :subtitle="person(l.employeeId)?.jobTitle" />
        <div class="request-dates">
          <strong>{{ date(l.fromDate) }} – {{ date(l.toDate) }}</strong>
          <span>{{ label(l.type) }} <b>·</b> {{ l.days }} working days</span>
        </div>
        <button
            class="approve-icon"
            :aria-label="'Approve request for '+fullName(person(l.employeeId))"
            title="Approve request"
            @click="approve(l.id)"
        >
          <Icon name="check" />
        </button>
      </div>
    </div>

    <div class="panel team-panel">
      <div class="panel-heading">
        <div>
          <h3>A team with many talents</h3>
          <p>Active people by department</p>
        </div>
        <Icon name="chart-box-outline" />
      </div>

      <div class="team-total">
        <strong>{{ state.dashboard.employees }}</strong>
        <span>people making<br />things happen</span>
      </div>

      <div class="team-distribution">
        <span
            v-for="d in departments"
            :key="d.id"
            :style="{ background: d.color, flex: d.count || 0.05 }"
            :title="d.name+': '+d.count"
        ></span>
      </div>

      <div class="department-legend">
        <RouterLink v-for="d in departments" :key="d.id" to="/departments">
          <span><i :style="{ background: d.color }"></i>{{ d.name }}</span>
          <strong>{{ d.count }}</strong>
        </RouterLink>
      </div>
    </div>
  </section>

  <section class="dashboard-columns bottom-columns">
    <div class="panel">
      <div class="panel-heading">
        <div>
          <h3>The latest around here</h3>
          <p>Small updates. A connected workplace.</p>
        </div>
        <span class="quiet-tag">Live activity</span>
      </div>

      <div v-if="!state.dashboard.activities.length" class="empty compact">
        <p>Workspace activity will appear here.</p>
      </div>

      <div v-for="(a,i) in state.dashboard.activities.slice(0,5)" :key="a.id" class="activity-row">
        <span class="activity-symbol" :class="['green','purple','orange'][i%3]">
          <Icon :name="a.kind==='leave' ? 'beach' : a.kind==='contract' ? 'file-document-outline' : 'account-outline'" />
        </span>
        <div>
          <strong>{{ a.title }}</strong>
          <p>{{ a.detail }}</p>
        </div>
        <small>{{ date(a.occurredAt) }}</small>
      </div>
    </div>

    <div class="panel">
      <div class="panel-heading">
        <div>
          <h3>On the horizon</h3>
          <p>Upcoming contract end dates</p>
        </div>
        <span class="quiet-tag">{{ month }}</span>
      </div>

      <div v-if="!upcoming.length" class="empty compact">
        <Icon name="calendar-check-outline" />
        <p>No upcoming contract end dates.</p>
      </div>

      <div v-for="c in upcoming" :key="c.id" class="milestone-row">
        <div class="date-square">
          <b>{{ new Date(c.endDate+'T12:00:00').getDate() }}</b>
          <span>{{ new Date(c.endDate+'T12:00:00').toLocaleDateString('en',{month:'short'}) }}</span>
        </div>
        <div>
          <strong>{{ fullName(person(c.employeeId)) }}</strong>
          <p>{{ c.number }} · Contract ending</p>
        </div>
        <RouterLink to="/contracts" class="icon-btn" aria-label="View contracts">
          <Icon name="arrow-top-right" />
        </RouterLink>
      </div>

      <div class="quick-note">
        <Icon name="lightbulb-on-outline" />
        <p>A timely conversation makes all the difference.<br /><strong>Plan ahead, with people in mind.</strong></p>
      </div>
    </div>
  </section>

  <section class="quick-actions">
    <span>MAKE A LITTLE PROGRESS</span>
    <button @click="employeeForm()">
      <Icon name="account-plus-outline" />Add a teammate<Icon name="arrow-right" />
    </button>
    <button @click="leaveForm()">
      <Icon name="calendar-plus" />Request time off<Icon name="arrow-right" />
    </button>
    <RouterLink to="/templates">
      <Icon name="file-edit-outline" />Prepare a document<Icon name="arrow-right" />
    </RouterLink>
  </section>
</template>