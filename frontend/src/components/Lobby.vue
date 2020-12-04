<template>
  <div class="lobby">
    <h1>Super Galaxy Face Melter</h1>

    <span class="p-float-label">
      <InputText type="text" v-model="state.playername" />
      <label for="username">Playername</label>
    </span>
    <div class="buttons">
      <Button
        id="btn-create"
        label="Create game"
        :disabled="isDisabled()"
        class="p-button-raised p-ripple"
        @click="setVisibleCreate()"
      />

      <Button
        id="btn-join"
        label="Join game"
        :disabled="isDisabled()"
        class="p-button-raised p-ripple"
        @click="setVisibleJoin()"
      />
      <div>
        <span>{{state.playername}}</span>
      </div>
    </div>
    <div class="modals">
      <Dialog
        id="createModal"
        
        v-model:visible="state.displayCreate"
      >
        <template #header :style="{backgroundColor: '#e2c3a6'}"><h3>Create game</h3></template>
        <CreateGame />
        <template #footer :style="{backgroundColor: '#e2c3a6'}">
          <Button label="Create" class="p-button-raised btn-dialog-create-game" />
        </template>
      </Dialog>

    </div>
  </div>
</template>

<script>
import { reactive } from "vue";
import CreateGame from "./CreateGame";
export default {
  name: "Lobby",
  components: {CreateGame},
  setup() {
    const state = reactive({
      displayCreate: false,
      displayJoin: false,
      playername: null,
    });

    function setVisibleCreate() {
      state.displayCreate = !state.displayCreate;
    }

    function setVisibleJoin() {
      state.displayJoin = !state.displayJoin;
    }

    function isDisabled() {
      return state.playername === null
    }

    return {
      state,
      setVisibleCreate,
      setVisibleJoin,
      isDisabled
    };
  },
};
</script>

<style scoped>
.lobby {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
}
h1 {
  word-spacing: -0.5em;
  filter: brightness(130%);
  text-shadow: 2px 2px black;
  font-family: "Press Start 2P", cursive;
}

.buttons {
  display: flex;
  padding: 5em;
  justify-content: space-around;
}
#btn-create,
#btn-join {
  margin-left: 7em;
  margin-right: 7em;
  height: 250px !important;
  width: 250px !important;
  background-color: #e2c3a6;
  border: 0.2em solid #3b1704;
  box-shadow: 0.2em 0.2em black;
  border-radius: 1em;
  font-size: 1rem;
  color: #3b1704;
  font-family: "Press Start 2P", cursive;
}

.btn-dialog-create-game{
  background-color: #e2c3a6;
  border: 0.2em solid #3b1704;
  box-shadow: 0.2em 0.2em black;
  color: #3b1704;
  font-family: "Press Start 2P", cursive;
}
</style>