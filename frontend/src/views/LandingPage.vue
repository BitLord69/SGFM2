<template>
  <div class="login-container p-grid p-my-auto p-jc-around">
    <div class="p-col-12 p-text-center">
      <router-link to="/login"><Button class="p-ripple">Login</Button></router-link>
    </div>
    <div class="p-col-12 p-text-center">
      <Button class="p-ripple" @click="performLoginAsGuest">Login as guest</Button>
    </div>
    <div class="p-text-center p-mb-4 p-col-12">
        <p class="p-mb-0">Don't have an account?</p>
        <p class="p-mt-0">
          Register <router-link to="/register">here</router-link>!
        </p>
      </div>
  </div>
</template>

<script>
import { reactive } from "vue";
import { useRouter } from "vue-router"
import UserHandler from "@/modules/UserHandler.js"

const form = reactive({
  email: null,
  password: null,
});
const state = reactive({
  incorrect: false,
})

export default {
  setup() {
    const router = useRouter();
    const { isLoggedIn, currentUser, login, loginAsGuest} = UserHandler();
console.log("isLoggedIn: ", isLoggedIn.value);
    if (isLoggedIn.value) {
      router.push('/lobby')
    }

    async function performLogin() {
      await login(form.email, form.password)

      if (isLoggedIn.value) {
        router.push('/lobby')
      } else {
        state.incorrect = true
      }
    }

    function performLoginAsGuest() {
      loginAsGuest();
      router.push('/lobby')
    }

    return {
      form,
      state,
      login,
      isLoggedIn,
      currentUser,
      performLogin,
      performLoginAsGuest
    };
  },
};
</script>

<style scoped>
.btn-group {
  display: flex;
  flex-direction: column;
}

 button {
  height: 4em;
  width: 17em;
  background-color: #e2c3a6;
  border: 0.2em solid #3b1704;
  box-shadow: 0.2em 0.2em black;
  border-radius: 1em;
  color: #3b1704;
  font-family: "Press Start 2P", cursive;
} 

.input {
  color: #3b1704;
}

.login-container {
  height: 53%;
  width: 40%;
  background-image: linear-gradient(#b99778, #e2c3a6);
  color: #3b1704;
  border: 0.2em solid #3b1704;
  box-shadow: 0.2em 0.2em black;
  border-radius: 1em;
  font-size: 120%;
  font-family: "Yanone Kaffeesatz", sans-serif;
}

/* a:link,
a:visited,
a:active {
  bottom: 10em;
  position: absolute;
  justify-content: center;
  display: flex;
  color: inherit;
  text-decoration: none;
} */
</style>
