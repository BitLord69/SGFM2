<template>
  <div class="p-grid p-jc-center main" :key="redrawCounter">
    <div class="gameTitle p-d-flex p-ai-center">
      <router-link :to="jumpTo" style="text-decoration:none; color: #e69244; cursor:pointer;">
        <h1>Super Galaxy Face Melter</h1>
      </router-link>
    </div>
    <div class="gameWindow" id="gameWindow">
      <router-view :key="$route.path" />
    </div>
  </div>
</template>

<script>
import { useRoute } from "vue-router";
import { watchEffect, ref, computed } from "vue";
import UserHandler from "@/modules/UserHandler.js"

export default {
  name: "App",
  setup() {
    const route = useRoute();
    const redrawCounter = ref(0);
    const {currentUser, isLoggedIn, isLoggedInAsGuest} = UserHandler()

    const jumpTo = computed(() => {
      return isLoggedIn || isLoggedInAsGuest ? '/lobby' : '/';
    });

    watchEffect(() => {
      // Without this "hack" the game board will still be visible when coming back to the lobby
      if (route.path === '/lobby') {
        redrawCounter.value++;
      }
    })

    return { redrawCounter, currentUser, isLoggedIn, isLoggedInAsGuest, jumpTo };
  }
}
</script>

<style lang="scss">
@import url("https://fonts.googleapis.com/css2?family=Yanone+Kaffeesatz&family=MedievalSharp&family=Press+Start+2P&display=swap");

$topMargin: 80px;

//Scrollbar styling start
* {
  scrollbar-width: thin;
  scrollbar-color:#3b1704 #b99778;
}

::-webkit-scrollbar {
  width: 0.5rem;
}

::-webkit-scrollbar-track{
  background: #3b1704;
}

::-webkit-scrollbar-thumb{
  background: #b99778;
}
// Scrollbar styling end

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
  min-width: 1024px;
  min-height: 768px;
  height: calc(100vh - #{$topMargin});
  position: relative;
  justify-content: center;
  display: flex;
  overflow-y: auto;
  overflow-x: hidden;
}

h1 {
  text-align: left;
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
.p-dialog-footer {
  background-color: #e2c3a6 !important;
}

.container {
  width: 100%;
  color: #3b1704;
  padding: 10px;
  border: 0.2em solid #3b1704;
  box-shadow: 0.2em 0.2em black;
  border-radius: 1em;
  font-size: 120%;
  font-family: "Yanone Kaffeesatz", sans-serif;
  background-image: linear-gradient(#b99778, #e2c3a6);
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

.avatar-image {
  width: 40px;
  height: 40px;
  border: 2px solid #2c3e50;
  border-radius: 50px;
  margin: 0;
}

.p-progressbar-indeterminate {
  width: 75vw !important;
  height: 1.5em !important;
}

.p-progressbar .p-progressbar-value {
  background: darken(#e2c3a6, 10%) !important;
}
</style>
