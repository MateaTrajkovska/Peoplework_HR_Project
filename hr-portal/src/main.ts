import '@fontsource-variable/dm-sans';
import '@fontsource-variable/manrope';
import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import '@mdi/font/css/materialdesignicons.css';
import './style.css';
createApp(App).use(router).mount('#app');
