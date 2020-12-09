<template>
  <div class="lobby">
    <h1>Super Galaxy Face Melter</h1>

    <div v-if="error">{{error}}</div>
    <div v-else>{{ gameState || 'Inget gameState' }}
      <div style="{{color: 'white'}}">{{gameState && gameState.players}}</div>
    </div>

    <span class="p-float-label p-mt-4">
      <InputText class="inputPlayerName" id="playername" type="text" v-model="state.playername" />
      <label class="inputPlayerNameLabel" for="playername">Playername</label>
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
    </div>
    <div class="modals">
      <Dialog
        id="createModal"
        :modal="true"
        :dismissableMask="true"
        :visible="state.displayCreate"
      >
        <template #header><h3>Create game</h3></template>
        <CreateGame />
        <template class="p-mx-auto" #footer>
          <router-link to="/gameboard">
            <Button label="Create" class="p-d-block p-mx-auto p-button-raised btn-dialog" @click="createNewGame"/>
          </router-link>
        </template>
      </Dialog>
      <Dialog
        id="joinModal"
        :modal="true"
        :dismissableMask="true"
        :visible="state.displayJoin"
      >
        <template #header><h3>Join game</h3></template>
        <JoinGame />
        <template class="p-mx-auto" #footer>
          <Button label="Join" class="p-d-block p-mx-auto p-button-raised btn-dialog" @click="joinGame(state.playername, 1)" to="/gameboard"/>
        </template>
      </Dialog>
    </div>
  </div>
</template>

<script>
import { reactive } from "vue";
import CreateGame from "@/components/CreateGame";
import JoinGame from "@/components/JoinGame";

import SocketHandler from '@/modules/SocketHandler';

export default {
  name: "Lobby",
  components: { CreateGame, JoinGame },
  setup() {
    const { playCard, sendMessage, createGame, joinGame, error, gameState, isConnected } = SocketHandler();

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
      return state.playername === null || state.playername.trim() === ""
    }

    function createNewGame() {
      createGame(state.playername);
    }

    return {
      state,
      setVisibleCreate,
      setVisibleJoin,
      isDisabled,
      createNewGame,
      playCard, joinGame, sendMessage, createGame,
      error, gameState, isConnected
    };
  },
};
</script>

<style scoped>
.inputPlayerName {
  background-color:  #e2c3a6;
  color: #3b1704;
}

.inputPlayerNameLabel {
  color: #3b1704;
}

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

.btn-dialog{
  background-color: #e2c3a6;
  border: 0.2em solid #3b1704;
  box-shadow: 0.2em 0.2em black;
  color: #3b1704;
  font-family: "Press Start 2P", cursive;
}
</style>