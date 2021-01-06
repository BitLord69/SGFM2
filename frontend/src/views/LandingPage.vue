<template>
  <Suspense>
      <template #default>
        <Leaderboard class="leaderboard"/>
      </template>
      <template #fallback>
        <div>Loading stats...</div>
      </template>
  </Suspense>
  <div class="p-grid">
    <div class="first-container p-col-6 p-offset-3 p-d-flex p-jc-center">
      <div class="start-container p-d-flex p-jc-center">
        <router-link class="login-link" to="/login">
          <div class="loginCard p-shadow-24">
            <div class="loginCardText">Login</div>
          </div>
        </router-link>

        <div class="guestCard p-shadow-24" @click="performLoginAsGuest">
          <div class="guestCardText">Login as Guest</div>
        </div>

        <div class="p-text-center p-as-end">
          <p class="p-mb-0">Don't have an account?</p>
          <p class="p-mt-0">
            Register <router-link to="/register">here</router-link>!
          </p>
        </div>
      </div>
    </div>
    <div class="p-col-6 p-offset-5 p-d-flex p-jc-center">
      <div class="start-container">
        <h4>Rules</h4>
          <p class="p-mt-0">
            Lorem ipsum dolor sit amet consectetur adipisicing elit. Sint laudantium, alias ipsum eveniet molestias cupiditate quisquam consequuntur ab delectus, temporibus voluptatem iste eaque debitis. Similique labore suscipit asperiores saepe. Saepe?
          </p>
      </div>
    </div>
    <div class="p-col-6 p-offset-2 p-d-flex p-jc-center">
      <div class="start-container">
          <h4>How to play</h4>
          <p class="p-mt-0">
            Lorem ipsum dolor sit amet consectetur adipisicing elit. Sint laudantium, alias ipsum eveniet molestias cupiditate quisquam consequuntur ab delectus, temporibus voluptatem iste eaque debitis. Similique labore suscipit asperiores saepe. Saepe?
          </p>
      </div>
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
.leaderboard {
  position: absolute;
  top: 2vh;
  right: 1vw;
}

.start-container {
  width: 100%;
  background-image: linear-gradient(#b99778, #e2c3a6);
  color: #3b1704;
  padding: 10px;
  border: 0.2em solid #3b1704;
  box-shadow: 0.2em 0.2em black;
  border-radius: 1em;
  font-size: 120%;
  font-family: "Yanone Kaffeesatz", sans-serif;
}

.first-container {
  position: relative;
  margin-top: 25%;
  height:25%;
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
  width: 35%;
  height: 160%;
  top: -230px;
  background-size: 100% 100%;
  transition: ease-in 200ms;
  border-radius: 5px;
  &:hover{
    box-shadow: 0px 0px 15px 2px #eee;
    z-index: 10;
  }
}
.loginCard{
  left: 5%;
  transform: rotate(-10deg);
  background-image: url("/radiated_zombie.png");
  &:hover{
    -webkit-transform: scale(1.1) rotate(-10deg);
  }
}

.loginCardText{
  position: absolute;
  bottom: 9%;
  left: 45%;
  font-size: 1em;
}

.guestCard{
  right: 5%;
  transform: rotate(10deg);
  background-image: url("/screaming_toddler.png");
    &:hover{
    -webkit-transform: scale(1.1) rotate(10deg);
  }
}

.guestCardText{
  position: absolute;
  bottom: 9%;
  left: 32%;
  font-size: 1em;
}
</style>
