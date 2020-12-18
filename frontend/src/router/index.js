import { createWebHistory, createRouter } from "vue-router";
import LandingPage from "../views/LandingPage.vue"
import Lobby from "../views/Lobby.vue"
import Gameboard from "../views/Gameboard.vue"
import RegisterForm from "../views/RegisterForm.vue"


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
  {
    path: "/gameboard/:player",
    name: "Gameboard",
    component: Gameboard,
  },
  {
    path: "/register",
    name: "Register",
    component: RegisterForm,
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;