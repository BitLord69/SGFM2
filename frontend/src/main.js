import { createApp } from "vue";
import App from "./App.vue";
import "./index.css";
import router from "./router";

//PrimeVue imports
import PrimeVue from 'primevue/config';

import Chart from 'primevue/chart';
import Dialog from "primevue/dialog";
import Button from "primevue/button";
import Slider from 'primevue/slider';
import Dropdown from 'primevue/dropdown';
import Carousel from 'primevue/carousel';
import Password from 'primevue/password';
import Accordion from 'primevue/accordion';
import InputText from "primevue/inputtext";
import ScrollPanel from 'primevue/scrollpanel';
import ProgressBar from 'primevue/progressbar';
import AccordionTab from 'primevue/accordiontab';
import ProgressSpinner from 'primevue/progressspinner';

import 'primeflex/primeflex.css';
import "primeicons/primeicons.css";
import "primevue/resources/primevue.min.css";
import "primevue/resources/themes/saga-blue/theme.css";

const app = createApp(App).
              use(router).
              use(PrimeVue, { ripple: true });

app.component("Chart", Chart);
app.component("Button", Button);
app.component("Dialog", Dialog);
app.component("Slider", Slider);
app.component("Carousel", Carousel);
app.component("Dropdown", Dropdown);
app.component("Password", Password);
app.component("InputText", InputText);
app.component("Accordion", Accordion);
app.component("ProgressBar", ProgressBar);
app.component("ScrollPanel", ScrollPanel);
app.component("AccordionTab", AccordionTab);
app.component("ProgressSpinner", ProgressSpinner);

app.mount("#app");
