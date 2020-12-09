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

// Socket imports
// import VueSocketIO from 'wsj.vue-socket.io'
// import SocketIO from 'socket.io-client'

import "primevue/resources/themes/saga-blue/theme.css";
import "primevue/resources/primevue.min.css";
import "primeicons/primeicons.css";
import 'primeflex/primeflex.css';
import { VueDraggableNext } from "vue-draggable-next";

// export const socketClient = SocketIO('http://localhost:9092');
// export const socketClient = SocketIO('http://localhost:9092'), {
//   "reconnection delay": 2000,
//   "force new connection": true,
// });

const app = createApp(App)
  .use(router)
  // .use(new VueSocketIO({
  //   debug: true,
  //   connection: 'http://localhost:9092'
    // connection: socketClient//'http://localhost:9092'
  // }))
  ;

app.config.globalProperties.$primevue = { ripple: true };
app.component("InputText", InputText);
app.component("Dialog", Dialog);
app.component("Button", Button);
app.component("Slider", Slider);
app.component("Accordion", Accordion);
app.component("AccordionTab", AccordionTab);
app.component("ScrollPanel", ScrollPanel);
app.directive("ripple", Ripple);

app.mount("#app");
