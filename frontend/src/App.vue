<template>
  <h1>Super Galaxy Face Melter</h1>
  <div class="container">
    <LandingPageComponent/>
  </div>
</template>

<script>
import LandingPageComponent from './components/LandingPageComponent.vue'

export default {
  name: 'App',
  components: {
    LandingPageComponent
  }
}
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Press+Start+2P&display=swap');
.container {
  display: flex;
  justify-content: center;
}
h1 {
  text-align: left;
  margin-left: 1.5em;
  word-spacing: -0.5em;
  font-family: 'Press Start 2P', cursive;
}
</style>
