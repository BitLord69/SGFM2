import { ref } from "vue";
import { extFetch } from "./extFetch";

const currentUser = ref(null);
const isLoggedIn = ref(false);
const isLoggedInAsGuest = ref(false);
const userError = ref(null);
const loginError = ref(null);

export default function UserHandler() {

  async function logout() {
    try {
      await extFetch("/api/auth/logout/", "POST");
    } catch (e) {
      userError.value = e
    }    
    currentUser.value = null;
    isLoggedIn.value = false;
    isLoggedInAsGuest.value = false;
  }

  const login = async (email, password) => {
    let result;

    try {
      result = await extFetch("/api/auth/login/", "POST", {"email" : email, "password" : password});
      if (result.error) {
        loginError.value = result.error;
        isLoggedIn.value = false;
        return 
      }

      isLoggedIn.value = true;
      currentUser.value = result;
    } catch (e) {
      loginError.value = e
      isLoggedIn.value = false;
      return 
    }
  };

  function loginAsGuest() {
    isLoggedInAsGuest.value = true;
    currentUser.value = { username: "guest" + Date.now(), avatar: 0 };
  }

  async function createUser(form) {
    try {
      await extFetch("/api/user/", "POST", form);
    } catch (e) {
      userError.value = e;
      return 
    }
    await login(form.email, form.password)
  }

  async function startApp() {
    let result;

    try {
      result = await extFetch("/api/auth/whoami/"); 
      if (result.error) {
        isLoggedIn.value = false;
        userError.value = result.error;
        return
      }
      isLoggedIn.value = true;
      currentUser.value = result;
    } catch (e) {
      userError.value = e
      isLoggedIn.value = false;
      return;
    }
  }


  return { currentUser, isLoggedIn, isLoggedInAsGuest, userError, loginError, logout, login, loginAsGuest, createUser, startApp };
}
