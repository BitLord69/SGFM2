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

import "primevue/resources/themes/saga-blue/theme.css";
import "primevue/resources/primevue.min.css";
import "primeicons/primeicons.css";

const app = createApp(App).use(router);
app.config.globalProperties.$primevue = { ripple: true };
app.component("InputText", InputText);
app.component("Dialog", Dialog);
app.component("Button", Button);
app.component("Slider", Slider);
app.directive("ripple", Ripple);

app.mount("#app");
