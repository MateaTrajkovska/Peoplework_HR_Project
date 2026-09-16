<script setup lang="ts">
import { computed, ref } from 'vue';
import { useRoute } from 'vue-router';
import { state, fullName, initials, date, label, contractStatus } from '../store';
import { employeeForm, contractForm, leaveForm, terminateForm } from '../forms';
import Icon from '../components/Icon.vue';

const route = useRoute();
const e = computed(() => state.employees.find((e) => e.id === route.params.id));
const tab = ref('Overview');
const contracts = computed(() => state.contracts.filter((c) => c.employeeId === e.value?.id));
const leaves = computed(() => state.leaves.filter((l) => l.employeeId === e.value?.id));
const plan = computed(() => state.plans.find((p) => p.employeeId === e.value?.id));
</script>

<template>
  <template v-if="e">
    <RouterLink to="/employees" class="back-link">
      <Icon name="arrow-left" />Back to people
    </RouterLink>

    <section class="panel employee-profile">
      <div class="profile-cover">
        <span>GOOD PEOPLE. GREAT POSSIBILITIES.</span>
        <div class="profile-cover-art">✳ <span>✦</span></div>
      </div>

      <div class="profile-identity">
        <span class="avatar profile-avatar" :class="e.avatarColor">{{ initials(e) }}</span>
        <div class="profile-name">
          <h1>
            {{ fullName(e) }}
            <span class="badge" :class="e.status.toLowerCase()">{{ label(e.status) }}</span>
          </h1>
          <p>{{ e.jobTitle }} <span>·</span> {{ e.department }}</p>
          <div class="profile-meta">
            <span><Icon name="map-marker-outline" />{{ e.location || e.municipality }}</span>
            <span><Icon name="identifier" />{{ e.employeeNumber }}</span>
            <span><Icon name="calendar-blank-outline" />Joined {{ date(e.startDate) }}</span>
          </div>
        </div>
        <button v-if="e.status==='ACTIVE'" class="btn" @click="employeeForm(e)">
          <Icon name="pencil-outline" />Edit profile
        </button>
      </div>

      <nav class="profile-tabs tabs">
        <button
            v-for="t in ['Overview','Contracts','Time off']"
            :key="t"
            :class="{ selected: tab===t }"
            @click="tab=t"
        >
          {{ t }}
          <span v-if="t==='Contracts'">{{ contracts.length }}</span>
          <span v-if="t==='Time off'">{{ leaves.length }}</span>
        </button>
      </nav>
    </section>

    <div v-if="tab==='Overview'" class="profile-columns">
      <div>
        <section class="panel detail-panel">
          <div class="panel-heading">
            <h3>Personal & contact details</h3>
            <Icon name="account-outline" />
          </div>
          <dl class="detail-grid">
            <div><dt>Work email</dt><dd><a :href="'mailto:'+e.email">{{ e.email }}</a></dd></div>
            <div><dt>Phone</dt><dd>{{ e.phone || '—' }}</dd></div>
            <div><dt>Date of birth</dt><dd>{{ date(e.dateOfBirth) }}</dd></div>
            <div><dt>Nationality</dt><dd>{{ e.nationality }}</dd></div>
            <div><dt>Gender</dt><dd>{{ label(e.gender) }}</dd></div>
            <div><dt>Personal ID</dt><dd>{{ e.personalId || 'Not provided' }}</dd></div>
            <div><dt>Address</dt><dd>{{ [e.street, e.municipality, e.postalCode].filter(Boolean).join(', ') || 'Not provided' }}</dd></div>
            <div><dt>Education</dt><dd>{{ label(e.degree) }}</dd></div>
          </dl>
        </section>

        <section class="panel detail-panel">
          <div class="panel-heading">
            <h3>Employment information</h3>
            <Icon name="briefcase-outline" />
          </div>
          <dl class="detail-grid">
            <div><dt>Department</dt><dd>{{ e.department }}</dd></div>
            <div><dt>Job title</dt><dd>{{ e.jobTitle }}</dd></div>
            <div><dt>Employment type</dt><dd>{{ label(e.employeeType) }}</dd></div>
            <div><dt>Work arrangement</dt><dd>{{ e.workMode }}</dd></div>
            <div><dt>Start date</dt><dd>{{ date(e.startDate) }}</dd></div>
            <div><dt>End date</dt><dd>{{ date(e.endDate) }}</dd></div>
            <div><dt>Bank account</dt><dd>{{ e.bankAccount==='0' ? 'Not provided' : e.bankAccount }}</dd></div>
          </dl>
        </section>

        <section class="panel detail-panel">
          <div class="panel-heading">
            <h3>Internal HR notes</h3>
            <span class="quiet-tag"><Icon name="lock-outline" />Private to HR</span>
          </div>
          <p class="notes-text">{{ e.notes || 'No notes have been added yet.' }}</p>
        </section>
      </div>

      <aside>
        <section class="panel balance-card">
          <span class="eyebrow">A LITTLE TIME TO RECHARGE</span>
          <Icon name="weather-sunny" class="balance-sun" />
          <h3>Vacation balance</h3>
          <div class="big-balance">
            {{ plan?.remainingDays ?? '—' }}
            <span>days available</span>
          </div>
          <p>{{ state.year }} annual allowance</p>

          <div v-if="plan" class="balance-breakdown">
            <div><span>Annual allowance</span><b>{{ plan.maxDays }} days</b></div>
            <div><span>Approved</span><b>{{ plan.usedDays }} days</b></div>
            <div><span>Reserved</span><b>{{ plan.reservedDays }} days</b></div>
          </div>
          <p v-else class="muted">No allowance has been set for this year.</p>

          <button v-if="e.status==='ACTIVE'" class="btn primary wide" @click="leaveForm(e.id)">
            <Icon name="plus" />Request time off
          </button>
        </section>

        <section class="panel detail-panel">
          <h3>Keep things moving</h3>
          <button v-if="e.status==='ACTIVE'" class="quick-profile-action" @click="contractForm(undefined,e.id)">
            <Icon name="file-document-outline" />Create contract<Icon name="arrow-right" />
          </button>
          <RouterLink to="/templates" class="quick-profile-action">
            <Icon name="file-edit-outline" />Prepare document<Icon name="arrow-right" />
          </RouterLink>
          <RouterLink to="/calendar" class="quick-profile-action">
            <Icon name="calendar-month-outline" />View team calendar<Icon name="arrow-right" />
          </RouterLink>
        </section>

        <button v-if="e.status==='ACTIVE'" class="archive-action" @click="terminateForm(e)">
          <Icon name="archive-outline" />Archive employee
        </button>
      </aside>
    </div>

    <section v-else-if="tab==='Contracts'" class="panel table-panel">
      <div class="panel-heading">
        <h3>Employment contracts</h3>
        <button v-if="e.status==='ACTIVE'" class="btn small-btn" @click="contractForm(undefined,e.id)">
          <Icon name="plus" />New contract
        </button>
      </div>
      <div class="table-scroll">
        <table>
          <thead>
          <tr>
            <th>Number</th>
            <th>Type</th>
            <th>Start</th>
            <th>End</th>
            <th>Monthly gross</th>
            <th>Status</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="c in contracts" :key="c.id">
            <td>{{ c.number }}</td>
            <td>{{ label(c.type) }}</td>
            <td>{{ date(c.startDate) }}</td>
            <td>{{ date(c.endDate) }}</td>
            <td>{{ c.salary.toLocaleString() }} {{ c.currency }}</td>
            <td><span class="badge" :class="contractStatus(c).toLowerCase()">{{ label(contractStatus(c)) }}</span></td>
          </tr>
          </tbody>
        </table>
      </div>
      <div v-if="!contracts.length" class="empty">
        <Icon name="file-document-outline" />
        <h3>No contracts yet.</h3>
      </div>
    </section>

    <section v-else class="panel table-panel">
      <div class="panel-heading">
        <h3>Time off history</h3>
        <button v-if="e.status==='ACTIVE'" class="btn small-btn" @click="leaveForm(e.id)">
          <Icon name="plus" />Request time off
        </button>
      </div>
      <div class="table-scroll">
        <table>
          <thead>
          <tr>
            <th>Reference</th>
            <th>Type</th>
            <th>From</th>
            <th>Through</th>
            <th>Days</th>
            <th>Status</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="l in leaves" :key="l.id">
            <td>{{ l.number }}</td>
            <td>{{ label(l.type) }}</td>
            <td>{{ date(l.fromDate) }}</td>
            <td>{{ date(l.toDate) }}</td>
            <td>{{ l.days }}</td>
            <td><span class="badge" :class="l.status.toLowerCase()">{{ label(l.status) }}</span></td>
          </tr>
          </tbody>
        </table>
      </div>
      <div v-if="!leaves.length" class="empty">
        <Icon name="beach" />
        <h3>No time off recorded yet.</h3>
      </div>
    </section>
  </template>

  <div v-else class="empty">
    <Icon name="account-search-outline" />
    <h2>Employee not found.</h2>
    <RouterLink to="/employees" class="btn">Back to people</RouterLink>
  </div>
</template>