<template>
  <div class="p-grid p-jc-center main" :key="redrawCounter">
    <div class="gameTitle p-my-3">
        <h1>Super Galaxy Face Melter</h1>
    </div>
    <Suspense>
      <template #default>
        <CheckLoggedIn />
      </template>
      <template #fallback>
        <div>Loading...</div>
      </template>
    </Suspense>
    <div class="gameWindow" id="gameWindow">
      <router-view :key="$route.path" />
    </div>
  </div>
</template>

<script>
import { useRoute } from "vue-router";
import { watchEffect, ref } from "vue";
import CheckLoggedIn from "@/components/CheckLoggedIn"
import UserHandler from "@/modules/UserHandler.js"

export default {
  name: "App",
  components: {CheckLoggedIn},
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

    //await startApp()

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
</style>
