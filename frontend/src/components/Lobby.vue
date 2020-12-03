<template>
  <div class="lobby">
    <h1>Super Galaxy Face Melter</h1>
    <span class="p-float-label">
      <InputText type="text" v-model="value" />
      <label for="username">Username</label>
    </span>
  </div>
</template>

<script>
export default {
  name: "Lobby",
  data() {
    return {
      value: null,
    };
  },
};
</script>

<style scoped>
.lobby {
  display: flex;
  flex-direction: column;
}
h1 {
  word-spacing: -0.5em;
  filter: brightness(130%);
  text-shadow: 2px 2px black;
  font-family: "Press Start 2P", cursive;
}
</style>