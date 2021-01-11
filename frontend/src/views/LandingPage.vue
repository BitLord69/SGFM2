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
    <div class="p-col-4 p-offset-7 p-d-flex p-jc-center">
      <div class="container">
        <h3>How to play</h3>
          <p class="p-mt-0 p-pr-2">
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
    <div class="p-col-6 p-offset-2 p-d-flex p-jc-center ">
      <div class="container p-mb-4">
        <h3 class="p-mt-1 p-mb-2">Card Gallery</h3>
        <div class="card">
          <Carousel :value="cardGallery" :numVisible="3" :numScroll="1" :circular="true" :autoplayInterval="3000">
            <template #item="cardGallery">
                <div class="card-item">
                    <div class="card-item-content">
                        <div class="p-mb-3">
                            <img :src="'/' + getImageName(cardGallery.data.name) + '.png'" :alt="cardGallery.data.name" class="card-image" />
                        </div>
                        <div>
                            <h3 class="p-mb-1">{{cardGallery.data.name}}</h3>
                            <h4 class="p-mt-0 p-mb-1">Power: {{cardGallery.data.power}}</h4>
                            <h4 class="p-mt-0 p-mb-1">Amount in deck: {{cardGallery.data.amount}}</h4>
                        </div>
                    </div>
                </div>
            </template>
          </Carousel>
        </div>
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
    const cardGallery = [
      {
        power: 1,
        name: "Mutated worm",
        amount: 8,
      },
      {
        power: 2,
        name: "Irate rat",
        amount: 8,
      },
      {
        power: 3,
        name: "Orange Menace",
        amount: 8,
      },
      {
        power: 4,
        name: "Sleepy Joe",
        amount: 10,
      },
      {
        power: 5,
        name: "Angry teacher",
        amount: 10,
      },
      {
        power: 6,
        name: "Screaming toddler",
        amount: 8,
      },
      {
        power: 7,
        name: "Space nerd",
        amount: 8,
      },
      {
        power: 8,
        name: "Anonymous hacker",
        amount: 4,
      },
      {
        power: 9,
        name: "Radiated Zombie",
        amount: 4,
      },
      {
        power: 10,
        name: "Super Galaxy Face Melter",
        amount: 2,
      },
    ]

    function getImageName(name) {
      return name.replaceAll(" ", "_").toLowerCase();
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
      performLoginAsGuest,
      getImageName,
      cardGallery
    };
  },
};
</script>

<style scoped lang="scss">
.btn-group {
  display: flex;
  flex-direction: column;
}

.card-item-content{
  border: 1px solid #3b1704;
  border-radius: 3px;
  margin: .3rem;
  text-align: center;
  padding: 2rem 0;
  font-family: "Yanone Kaffeesatz", sans-serif;
  letter-spacing: 1px;
}

.card-image{
  width: 60%;
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
  50%{
    top:66vh;
    -webkit-transform: rotate(360deg);
  }
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
  line-height: 1.3;
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
