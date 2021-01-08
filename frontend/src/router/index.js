import { createWebHistory, createRouter } from "vue-router";
import LandingPage from "@/views/LandingPage";
import Lobby from "@/views/Lobby";
import Gameboard from "@/views/Gameboard";
import RegisterPage from "@/views/RegisterPage";
import LoginPage from "@/views/LoginPage";
import Friends from "@/views/Friends";
import UserHandler from "@/modules/UserHandler";
import GameHandler from "@/modules/GameHandler";

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
    component: Friends,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach(async (to, from, next) => {
  const { startApp, isLoggedIn, isLoggedInAsGuest } = UserHandler();
  await startApp();
  
  if (!isLoggedIn.value && !isLoggedInAsGuest.value) {
    if (to.name === "Login" || to.name === "Home" || to.name === "Register") next();
    else next({ name: 'Home' })
  }
  else {
    const { inGame } = GameHandler();
    if (to.name == "Gameboard") {
      if(!inGame.value){
        next({name: "Lobby"})
      } else next();
    } else next();
  }
})

export default router;
