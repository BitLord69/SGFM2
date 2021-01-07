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
    <img class="aniCard" src="/sleepy_joe.png" />
    <div class="first-container p-col-6 p-offset-3 p-d-flex p-jc-center">
      <div class="container p-d-flex p-jc-center">
        <router-link class="login-link" to="/login">
          <div class="leftCardButtonLandingPage p-shadow-24">
            <div class="leftCardButtonLandingPageText">Login</div>
          </div>
        </router-link>

        <div class="rightCardButtonLandingPage p-shadow-24" @click="performLoginAsGuest">
          <div class="rightCardButtonLandingPageText">Login as Guest</div>
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
      <div class="container">
        <h3>Game Rules</h3>
          <p class="p-mt-0">
            <ul>
              <li>Both players draw from one deck of cards. Each card has a name and power. Power determines both attack and health points.</li>
              <li>One turn consists of each player playing one card. Starting player changes each turn.</li>
              <li>
                The winner of the turn is decided by the card with the highest current power. The winning card's power decreases
                by the losing card's current power and returns to the player's hand. The losing card is discarded and the player gets a new card.
              </li>
              <li>In the case of a tie, both cards are discarded and both players draw new cards.</li>
              <li>Points are awarded from the current power of the losing card.</li>
              <li>The game is over when one player reach the agreed upon amount of points to win.</li>
            </ul>
          </p>
      </div>
    </div>
    <div class="p-col-6 p-offset-2 p-d-flex p-jc-center">
      <div class="container">
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

.aniCard {
  position: absolute;
  height: 250px;
  top: 1vh;
  left: 8vw;
  animation: cardAnimation 6s infinite;
  animation-direction: alternate-reverse;
}
@keyframes cardAnimation{
  0%{
    top: 0vh;
    -webkit-transform: rotate(0deg);
  }
  //45%{rotate: 320deg;}
  50%{
    top:66vh;
    -webkit-transform: rotate(360deg);
  }
  //55%{rotate: 420deg;}
  100%{
    top:0vh;
    -webkit-transform: rotate(720deg);
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
.leaderboard {
  position: absolute;
  top: 2vh;
  right: 1vw;
}

ul {
  line-height: 1.1;
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

.leftCardButtonLandingPage{
  background-image: url("/radiated_zombie.png");
}

.leftCardButtonLandingPageText{
  left: 45%;
}

.rightCardButtonLandingPage{
  background-image: url("/screaming_toddler.png");
}

.rightCardButtonLandingPageText{
  left: 32%;
}
</style>
