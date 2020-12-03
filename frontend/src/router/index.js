import { createWebHistory, createRouter } from "vue-router";
import LandingPageComponent from "../components/LandingPageComponent.vue"
import Lobby from "../components/Lobby.vue"


const routes = [
  {
    path: "/",
    name: "Home",
    component: LandingPageComponent,
  },
  {
    path: "/lobby",
    name: "Lobby",
    component: Lobby,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;