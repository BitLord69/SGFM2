<template>
  <router-link to="/lobby">
    <button>Play a game</button>
  </router-link>
</template>

<script>
export default {
  name: "LandingPageComponent",
};
</script>

<style scoped>
button {
  height: 5em;
  width: 20em;
  background-color: #e2c3a6;
  border: 0.2em solid #3b1704;
  box-shadow: 0.2em 0.2em black;
  border-radius: 1em;
  font-size: 1rem;
  color: #3b1704;
  font-family: "Press Start 2P", cursive;
}

a:link,
a:visited,
a:active {
  bottom: 10em;
  position: absolute;
  justify-content: center;
  display: flex;
  color: inherit;
  text-decoration: none;
}
</style>
