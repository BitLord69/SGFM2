import { createWebHistory, createRouter } from "vue-router";
import LandingPage from "../views/LandingPage.vue"
import Lobby from "../views/Lobby.vue"

const routes = [
  {
    path: "/",
    name: "Home",
    component: LandingPage,
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