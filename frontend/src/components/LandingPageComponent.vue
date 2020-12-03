<template>
  <div class="gameWindow">
    <button>Play a game</button>
  </div>
</template>

<script>
export default {
  name: 'LandingPageComponent'
}
</script>

<style scoped>
  .gameWindow{
    width: 80vw;
    height: 70vh;
    background: linear-gradient(90deg, rgba(226,195,166,1) 0%, rgba(59,23,4,1) 50%, rgba(226,195,166,1) 100%);
    position: relative;
    justify-content: center;
    display: flex;
  }
  button{
    position: absolute;
    bottom: 10em;
    height: 5em;
    width: 20em;
    background-color: #e2c3a6;
    border: 0.2em solid #3b1704;
    box-shadow: 0.2em 0.2em black;
    border-radius: 1em;
    font-size: 1rem;
    color: #3b1704;
    font-family: 'Press Start 2P', cursive;
  }
</style>
