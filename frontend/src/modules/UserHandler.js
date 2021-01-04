import { ref } from "vue";
import { extFetch } from "./extFetch";

const currentUser = ref(null);
const isLoggedIn = ref(false);
const error = ref(null);

export default function UserHandler() {
  async function logout() {
    try {
      await extFetch("/api/auth/logout/");
    } catch (e) {
      error.value = e
    }    
    currentUser.value = null;
    isLoggedIn.value = false;
  }

  const login = async (email, password) => {
    let result;

    try {
      result = await extFetch("/api/auth/login/", "POST", {"email" : email, "password" : password});
      
      isLoggedIn.value = true;
      currentUser.value = result;
    } catch (e) {
      error.value = e
      return 
    }
  }

  async function createUser(email, userName, password) {
    try {
      const result = await extFetch("/api/users/", "POST", {"email" : email, "userName" : userName, "password" : password});
      
      console.log(result);
    } catch (e) {
      error.value = e;
      return 
    }
  
    await login(userName, password)
  }
  
  async function startApp() {
    let result;

    try {
      result = await extFetch("/api/auth/whoami/"); 
      console.log("starttApp: result = ", result);
      if (result.error) {
        isLoggedIn.value = false;
        error.value = result.error;
        return
      }
      isLoggedIn.value = true;
      currentUser.value = result;
    } catch (e) {
      error.value = e
      isLoggedIn.value = false;
      return 
    }
  }


  return { currentUser, isLoggedIn, error, logout, login, createUser, startApp };
}
