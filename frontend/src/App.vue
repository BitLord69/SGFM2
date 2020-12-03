<template>
  <h1>Super Galaxy Face Melter</h1>
  <div class="container">
    <div class="gameWindow">
      <router-view />
    </div>
  </div>
</template>

<script>
export default {
  name: "App",
};
</script>

<style>
@import url("https://fonts.googleapis.com/css2?family=Press+Start+2P&display=swap");
.container {
  justify-content: center;
  display: flex;
}
.gameWindow {
  width: 80vw;
  height: 70vh;
  background: linear-gradient(
    90deg,
    rgba(226, 195, 166, 1) 0%,
    rgba(59, 23, 4, 1) 50%,
    rgba(226, 195, 166, 1) 100%
  );
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
</style>
