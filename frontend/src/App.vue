<template>
  <h1>Super Galaxy Face Melter</h1>
  <div class="container">
    <div class="gameWindow" :style="{ backgroundImage: `url(${'../bg.png'})` }">
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
@import url("https://fonts.googleapis.com/css2?family=Yanone+Kaffeesatz&family=MedievalSharp&family=Press+Start+2P&display=swap");
.container {
  justify-content: center;
  display: flex;
}
.gameWindow {
  width: 80vw;
  height: 85vh;
  background-size: 100% 100%;
  position: absolute;
  display: flex;
}
h1 {
  text-align: left;
  margin-left: 1.5em;
  word-spacing: -0.5em;
  font-family: "Press Start 2P", cursive;
}

Button:hover {
  background-color: #e2c3a6 !important;
  border: 0.2em solid #3b1704 !important;
  color: #3b1704 !important;
}

.p-dialog-content,
.p-dialog-header,
.p-dialog-footer {
  background-color: #e2c3a6 !important;
}
</style>
