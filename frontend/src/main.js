import { createApp } from 'vue';
import App from './App.vue';
import './index.css';
import router from './router';
import InputText from 'primevue/inputtext';

import 'primevue/resources/themes/saga-blue/theme.css';
import 'primevue/resources/primevue.min.css';
import 'primeicons/primeicons.css';

const app = createApp(App).use(router);
app.component('InputText', InputText);

app.mount('#app');