import { createWebHistory, createRouter } from "vue-router";
import LandingPage from "@/views/LandingPage"
import Lobby from "@/views/Lobby"
import Gameboard from "@/views/Gameboard"
import RegisterPage from "@/views/RegisterPage"
import LoginPage from "@/views/LoginPage"


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
    component: RegisterPage,
  },
  {
    path: "/login",
    name: "Login",
    component: LoginPage,
  },
  {
    path: "/friends",
    name: "Friends",
    component: LoginPage,
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;