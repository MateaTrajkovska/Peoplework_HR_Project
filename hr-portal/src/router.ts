import { createRouter, createWebHistory } from 'vue-router';
import Dashboard from './pages/Dashboard.vue';
import Workspace from './pages/Workspace.vue';
import EmployeeDetail from './pages/EmployeeDetail.vue';

export default createRouter({
 history: createWebHistory(),
 routes: [
  {
   path: '/',
   component: Dashboard,
   meta: { title: 'Overview' },
  },
  {
   path: '/employees/:id/detail',
   component: EmployeeDetail,
   meta: { title: 'Employee profile' },
  },
  ...[
   'employees',
   'departments',
   'contracts',
   'leaves',
   'plans',
   'calendar',
   'templates',
   'settings',
  ].map((path) => ({
   path: '/' + path,
   component: Workspace,
   meta: {
    title: (
        {
         employees: 'People',
         departments: 'Departments',
         contracts: 'Contracts',
         leaves: 'Time off',
         plans: 'Leave balances',
         calendar: 'Team calendar',
         templates: 'Documents',
         settings: 'Workspace settings',
        } as Record<string, string>
    )[path],
   },
  })),
  { path: '/absences', redirect: '/leaves' },
  { path: '/vacation/plans', redirect: '/plans' },
  { path: '/settings/templates', redirect: '/templates' },
  { path: '/:pathMatch(.*)*', redirect: '/' },
 ],
 scrollBehavior: () => ({ top: 0 }),
});