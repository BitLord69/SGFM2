import { createApp } from "vue";
import App from "./App.vue";
import "./index.css";
import router from "./router";

//PrimeVue imports
import InputText from "primevue/inputtext";
import Dialog from "primevue/dialog";
import Button from "primevue/button";
import Ripple from "primevue/ripple";
import Slider from 'primevue/slider';
import Accordion from 'primevue/accordion';
import AccordionTab from 'primevue/accordiontab';
import ScrollPanel from 'primevue/scrollpanel';
import ProgressSpinner from 'primevue/progressspinner';
import Password from 'primevue/password';

import "primevue/resources/themes/saga-blue/theme.css";
import "primevue/resources/primevue.min.css";
import "primeicons/primeicons.css";
import 'primeflex/primeflex.css';

const app = createApp(App).use(router);

app.config.globalProperties.$primevue = { ripple: true };
app.component("InputText", InputText);
app.component("Dialog", Dialog);
app.component("Button", Button);
app.component("Slider", Slider);
app.component("Accordion", Accordion);
app.component("AccordionTab", AccordionTab);
app.component("ScrollPanel", ScrollPanel);
app.component("ProgressSpinner", ProgressSpinner);
app.directive("ripple", Ripple);
app.component("Password", Password);

app.mount("#app");
