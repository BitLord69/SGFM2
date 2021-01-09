import { ref } from "vue";
import { extFetch } from "./extFetch";

const currentUser = ref(null);
const isLoggedIn = ref(false);
const isLoggedInAsGuest = ref(false);
const error = ref(null);

export default function UserHandler() {

  async function logout() {
    try {
      await extFetch("/api/auth/logout/", "POST");
    } catch (e) {
      error.value = e;
    }
    console.log("logout Userhandler", error.value);
    currentUser.value = null;
    isLoggedIn.value = false;
    isLoggedInAsGuest.value = false;
  }

  const login = async (email, password) => {
    let result;

    try {
      result = await extFetch("/api/auth/login/", "POST", {
        email: email,
        password: password,
      });

      isLoggedIn.value = true;
      currentUser.value = result;
    } catch (e) {
      error.value = e;
      return;
    }
  };

  function loginAsGuest() {
    isLoggedInAsGuest.value = true;
    currentUser.value = { username: "guest" + Date.now() };
  }

  async function createUser(form) {
    try {
      const result = await extFetch("/api/user/", "POST", form);

      console.log(result);
    } catch (e) {
      error.value = e;
      return;
    }

    await login(form.email, form.password);
  }

  async function startApp() {
    let result;

    try {
      result = await extFetch("/api/auth/whoami/");
      console.log("who am i: ", result);
      if (result.error) {
        isLoggedIn.value = false;
        error.value = result.error;
        return;
      }
      isLoggedIn.value = true;
      currentUser.value = result;
    } catch (e) {
      error.value = e;
      isLoggedIn.value = false;
      return;
    }
  }

  return {
    currentUser,
    isLoggedIn,
    isLoggedInAsGuest,
    error,
    logout,
    login,
    loginAsGuest,
    createUser,
    startApp,
  };
}
