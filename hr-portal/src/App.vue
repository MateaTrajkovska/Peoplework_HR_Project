<script setup lang="ts">
import FormModal from './components/FormModal.vue';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import { state, init, login, logout, errorMessage, load, date, today } from './store';
import Icon from './components/Icon.vue';

const route = useRoute();
const username = ref('admin');
const password = ref('');
const busy = ref(false);
const error = ref('');
const mobile = ref(false);

const nav = [
  ['/', 'view-dashboard-outline', 'Overview'],
  ['/employees', 'account-group-outline', 'People'],
  ['/departments', 'domain', 'Departments'],
  ['/contracts', 'file-document-outline', 'Contracts'],
  ['/leaves', 'beach', 'Time off'],
  ['/plans', 'chart-donut', 'Leave balances'],
  ['/calendar', 'calendar-month-outline', 'Team calendar'],
  ['/templates', 'folder-outline', 'Documents']
];

onMounted(init);

async function signIn() {
  busy.value = true;
  error.value = '';
  try {
    await login(username.value, password.value);
    password.value = '';
  } catch (e) {
    error.value = errorMessage(e);
  } finally {
    busy.value = false;
  }
}

async function signOut() {
  try {
    await logout();
  } catch (e) {
    state.error = errorMessage(e);
  }
}

async function retry() {
  try {
    await load();
  } catch {}
}
</script>

<template>
  <div v-if="!state.ready" class="boot">
    <span class="brand-mark">p<span>•</span></span>
    <p>Opening your workspace…</p>
  </div>

  <div v-else-if="!state.user" class="login-layout">
    <section class="login-story">
      <div class="brand">
        <span class="brand-mark">p<span>•</span></span>peoplework<span class="brand-dot">.</span>
      </div>
      <div>
        <span class="eyebrow">A LITTLE LESS ADMIN. A LOT MORE HUMAN.</span>
        <h1>Good work starts<br />with your people.</h1>
        <p>One considered space for your team, their time,<br />and every moment that matters.</p>

        <div class="login-art">
          <div class="orbit o1"></div>
          <div class="orbit o2"></div>
          <div class="art-card">
            <span class="tiny-label">YOUR PEOPLE, CONNECTED</span>
            <div class="avatar-stack">
              <span class="avatar purple">MT</span>
              <span class="avatar orange">AP</span>
              <span class="avatar blue">ES</span>
              <span class="avatar green">+9</span>
            </div>
            <strong>Room to do great things.</strong>
            <span>People · Culture · Possibility</span>
          </div>
          <div class="art-badge"><Icon name="check-circle" /> Everything in one place</div>
        </div>
      </div>
      <small>PEOPLEWORK / HR WORKSPACE</small>
    </section>

    <section class="login-form">
      <div>
        <span class="eyebrow">WELCOME BACK</span>
        <h2>Your workspace awaits.</h2>
        <p class="muted">Sign in to take care of the people behind the work.</p>

        <form @submit.prevent="signIn">
          <label>
            Username
            <input v-model="username" autocomplete="username" required placeholder="Your username" />
          </label>
          <label>
            Password
            <input v-model="password" type="password" autocomplete="current-password" required placeholder="Enter your password" />
          </label>

          <div v-if="error||state.error" class="alert" role="alert">{{ error || state.error }}</div>

          <button class="btn primary wide" :disabled="busy">
            {{ busy ? 'Signing in…' : 'Sign in to workspace' }}
            <Icon name="arrow-right" />
          </button>
        </form>

        <div class="login-note">
          <Icon name="shield-check-outline" />
          <span>
            Private workspace · HR administrator access<br />
            <small>Local demo credentials are in the project README.</small>
          </span>
        </div>
      </div>
      <footer>Made for the everyday moments of a better workplace.</footer>
    </section>
  </div>

  <div v-else class="app-layout">
    <div v-if="mobile" class="sidebar-backdrop" @click="mobile=false"></div>

    <aside class="sidebar" :class="{ open: mobile }">
      <RouterLink to="/" class="brand">
        <span class="brand-mark">p<span>•</span></span>peoplework<span class="brand-dot">.</span>
      </RouterLink>

      <span class="nav-label">WORKSPACE</span>
      <nav>
        <RouterLink
            v-for="[path,icon,title] in nav"
            :key="path"
            :to="path"
            :class="{ active: route.path===path || (path==='/employees' && route.path.startsWith('/employees/')) }"
            @click="mobile=false"
        >
          <Icon :name="icon" />
          <span>{{ title }}</span>
          <b v-if="path==='/leaves' && state.dashboard.pending" class="nav-count">{{ state.dashboard.pending }}</b>
        </RouterLink>
      </nav>

      <div class="sidebar-bottom">

        <RouterLink to="/settings" class="settings-link">
          <Icon name="cog-outline" /> Workspace settings
        </RouterLink>

        <div class="sidebar-user">
          <span class="avatar lime">HR</span>
          <div>
            <strong>{{ state.user.username }}</strong>
            <small>HR administrator</small>
          </div>
          <button class="icon-btn" title="Sign out" aria-label="Sign out" @click="signOut">
            <Icon name="logout" />
          </button>
        </div>
      </div>
    </aside>

    <div class="main-layout">
      <header class="topbar">
        <div>
          <button class="icon-btn mobile-menu" aria-label="Open navigation" @click="mobile=!mobile">
            <Icon name="menu" />
          </button>
          <span class="breadcrumb">Workspace <Icon name="chevron-right" /> <strong>{{ route.meta.title }}</strong></span>
        </div>

        <div class="topbar-right">
          <span class="date-chip topbar-date">
            <Icon name="calendar-blank-outline"/>
              {{ date(today()) }}
            </span>
          <RouterLink to="/leaves" class="icon-btn notification" title="Review leave requests" aria-label="Review leave requests">
            <Icon name="bell-outline" />
            <span v-if="state.dashboard.pending"></span>
          </RouterLink>
          <RouterLink to="/settings" class="avatar small purple" aria-label="Account settings">HR</RouterLink>
        </div>
      </header>

      <main id="main">
        <div v-if="state.error" class="alert" role="alert">
          {{ state.error }}
          <button class="text-btn" @click="retry">Try again</button>
        </div>
        <div v-if="state.loading" class="loading-line" role="status" aria-label="Loading workspace"></div>
        <RouterView />
      </main>

      <footer class="app-footer">
        <span>Peoplework · A better everyday.</span>
        <span>People first. Always.</span>
      </footer>
    </div>

    <FormModal />

    <div v-if="state.toast" class="toast" role="status">
      <Icon name="check-circle" />{{ state.toast }}
      <button class="icon-btn" aria-label="Dismiss notification" @click="state.toast=''">
        <Icon name="close" />
      </button>
    </div>
  </div>
</template>