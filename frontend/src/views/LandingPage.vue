<template>
    <div class="login-container p-grid p-my-auto p-jc-around">
      <div class="gameTitle">
        <h1>Super Galaxy Face Melter</h1>
      </div>
      <Suspense>
        <template #default>
          <Leaderboard class="leaderboard"/>
        </template>
        <template #fallback>
          <div>Loading stats...</div>
        </template>
      </Suspense>
      
      <router-link class="login-link" to="/login">
        <div class="loginCard p-shadow-24">
          <div class="loginCardText">Login</div>
        </div>
      </router-link>

      <div class="guestCard p-shadow-24" @click="performLoginAsGuest">
        <div class="guestCardText">Login as Guest</div>
      </div>

      <div class="p-text-center p-mb-4 p-col-12 register">
          <p class="p-mb-0">Don't have an account?</p>
          <p class="p-mt-0">
            Register <router-link to="/register">here</router-link>!
          </p>
        </div>
    </div>
</template>

<script>
import { reactive } from "vue";
import { useRouter } from "vue-router";
import UserHandler from "@/modules/UserHandler.js"
import Leaderboard from '../components/Leaderboard.vue';

const form = reactive({
  email: null,
  password: null,
});
const state = reactive({
  incorrect: false,
});

export default {
  components: { Leaderboard },

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

<style scoped lang="scss">
.btn-group {
  display: flex;
  flex-direction: column;
}
.gameTitle{
  position: absolute;
  left: 10vw;
  top: 5vh;
  h1{
    word-wrap: none;
    text-shadow: 2px 2px black;
  }
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

.leaderboard {
  position: absolute;
  top: 2vh;
  right: 1vw;
}

.login-container {
  height: 53%;
  width: 40%;
  background-image: linear-gradient(#b99778, #e2c3a6);
  /* color: #3b1704; */
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

.register{
  position: absolute;
  bottom: 17vh;
  color: #3b1704;
}

.login-link{
  color: inherit;
}

.loginCard, .guestCard{
  color: #3b1704;
  position: absolute;
  cursor: pointer;
  width: 15%;
  height: 40%;
  top: 25%;
  background-size: 100% 100%;
  transition: ease-in 200ms;
  border-radius: 5px;
  &:hover{
    scale: 110%;
    box-shadow: 0px 0px 15px 2px #eee;
    z-index: 10;
  }
}
.loginCard{
  left: 30%;
  transform: rotate(-10deg);
  background-image: url("/radiated_zombie.png");
  
}

.loginCardText{
  position: absolute;
  bottom: 9%;
  left: 45%;
  font-size: 1em;
}

.guestCard{
  left: 55%;
  transform: rotate(10deg);
  background-image: url("/screaming_toddler.png");
}

.guestCardText{
  position: absolute;
  bottom: 9%;
  left: 32%;
  font-size: 1em;
}
</style>
