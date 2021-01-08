<template>
  <div class="p-grid p-jc-center main" :key="redrawCounter">
    <div class="gameTitle p-my-3">
        <h1>Super Galaxy Face Melter</h1>
    </div>
    <Suspense>
      <template #default>
        <div>
          <!-- <CheckLoggedIn /> -->
          <div class="gameWindow" id="gameWindow">
            <router-view :key="$route.path" />
          </div>
        </div>
      </template>
      <template #fallback>
        <div>Loading...</div>
      </template>
    </Suspense>
    
  </div>
</template>

<script>
import { useRoute } from "vue-router";
import { watchEffect, ref } from "vue";
//import CheckLoggedIn from "@/components/CheckLoggedIn"
import UserHandler from "@/modules/UserHandler.js"

export default {
  name: "App",
  //components: {CheckLoggedIn},
  setup() {
    const route = useRoute();
    const redrawCounter = ref(0);
    const {currentUser, isLoggedIn, isLoggedInAsGuest} = UserHandler()

    watchEffect(() => {
      // Without this "hack" the game board will still be visible when coming back to the lobby
      if (route.path === '/lobby') {
        redrawCounter.value++;
      }
    })

    return { redrawCounter, currentUser, isLoggedIn, isLoggedInAsGuest };
  }
}
</script>

<style lang="scss">
@import url("https://fonts.googleapis.com/css2?family=Yanone+Kaffeesatz&family=MedievalSharp&family=Press+Start+2P&display=swap");

$topMargin: 50px;

body{
  background-image: url("/bg.png");
  background-size: cover;
  background-position: center;
  margin: 0;
  height:100vh;
  overflow: hidden;
  
  .gameTitle{
    height: $topMargin;
  h1{
    word-wrap: none;
    text-shadow: 2px 2px black;
  }
}
}

.gameWindow {
  width: 100vw;
  height: calc(100vh - #{$topMargin});
  position: relative;
  justify-content: center;
  display: flex;
  overflow-y: auto;
  overflow-x: hidden;
}
h1 {
  text-align: left;
  margin-left: 1.5em;
  word-spacing: -0.5em;
  font-family: "Press Start 2P", cursive;
}

Button:hover{
  background-color: rgba($color: #e2c3a6, $alpha: 0.8) !important;
  border: 0.2em solid #3b1704 !important;
  color: #3b1704 !important;
}

.p-dialog-content,
.p-dialog-header,
.p-dialog-footer{
  background-color: #e2c3a6 !important;
}

.container {
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

.leftCardButtonLandingPage, .rightCardButtonLandingPage, .leftCardButtonLobby, .rightCardButtonLobby{
  color: #3b1704;
  position: absolute;
  cursor: pointer;
  width: 25%;
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

.leftCardButtonLandingPage, .leftCardButtonLobby {
  left: 17%;
  transform: rotate(-10deg);
  &:hover{
    -webkit-transform: scale(1.1) rotate(-10deg);
  }
}

.rightCardButtonLandingPage, .rightCardButtonLobby {
  right: 17%;
  transform: rotate(10deg);
    &:hover{
    -webkit-transform: scale(1.1) rotate(10deg);
  }
}

.leftCardButtonLandingPageText, .leftCardButtonLobbyText, .rightCardButtonLandingPageText, .rightCardButtonLobbyText {
  position: absolute;
  bottom: 9.6%;
  font-size: 1em;
}
</style>
