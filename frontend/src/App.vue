<template>
  <!-- <h1>Super Galaxy Face Melter</h1> -->
  <div class="container" :key="redrawCounter">
    <Suspense>
      <template #default>
        <CheckLoggedIn />
      </template>
      <template #fallback>
        <div>Loading...</div>
      </template>
    </Suspense>
    <div class="gameWindow" id="gameWindow" :style="{ backgroundImage: `url(${'../bg.png'})` }">
      <router-view :key="$route.path" />
    </div>
  </div>
</template>

<script>
import { useRoute } from "vue-router";
import { watchEffect, ref } from "vue";
import CheckLoggedIn from "@/components/CheckLoggedIn"
//import UserHandler from "@/modules/UserHandler.js"

export default {
  name: "App",
  components: {CheckLoggedIn},
  setup() {
    const route = useRoute();
    const redrawCounter = ref(0);
    //const {startApp} = UserHandler()

    watchEffect(() => {
      // Without this "hack" the game board will still be visible when coming back to the lobby
      if (route.path === '/lobby') {
        redrawCounter.value++;
      }
    })

    //await startApp()

    return { redrawCounter };
  }
}
</script>

<style lang="scss">
@import url("https://fonts.googleapis.com/css2?family=Yanone+Kaffeesatz&family=MedievalSharp&family=Press+Start+2P&display=swap");
.container {
  justify-content: center;
  display: flex;
}
.gameWindow {
  width: 80vw;
  height: 85vh;
  background-size: 100% 100%;
  position: relative;
  justify-content: center;
  display: flex;
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
