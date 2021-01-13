<template>
 <Suspense>
      <template #default>
        <Leaderboard class="leaderboard"/>
      </template>
      <template #fallback>
        <div>Loading stats...</div>
      </template>
  </Suspense>
  <Suspense>
      <template #default>
        <LatestGame class="latestGame"/>
      </template>
      <template #fallback>
        <div>Loading your latest game...</div>
      </template>
  </Suspense>
    <div class="p-grid" style="width: 100%">
      <div class="first-container p-col-6 p-offset-3 p-d-flex p-jc-center">
        <div class="container p-d-flex p-jc-center">
            <div class="leftCardButtonLobby p-shadow-24" @click="setVisibleCreate()">
              <div class="leftCardButtonLobbyText">Create Game</div>
            </div>
          <div class="rightCardButtonLobby p-shadow-24" @click="setVisibleJoin()">
            <div class="rightCardButtonLobbyText">Join Game</div>
          </div>
        </div>
      </div>
    </div>
    <div class="modals">
      <Dialog
        id="createModal"
        :modal="true"
        :dismissableMask="true"
        :closable="false"
        :visible="state.displayCreate"
      >
        <template #header>
          <div class="p-grid" style="width: 100%">
            <h3 class="p-m-0 p-col-11 p-pb-0">Create game</h3>
            <i class="pi pi-times-circle p-col-1 p-mt-2" @click="setVisibleCreate" style="cursor: pointer"></i>
          </div>
        </template>
        <Suspense>
          <template #default>
            <CreateGame />
          </template>
          <template #fallback>
            <div>Loading leagues...</div>
          </template>
        </Suspense>
        <template class="p-mx-auto" #footer>
          <router-link style="text-decoration: none" to="/gameboard/0">
            <Button
              label="Create"
              class="p-d-block p-mx-auto p-button-raised btn-dialog"
              @click="createNewGame"
            />
          </router-link>
        </template>
      </Dialog>

      <Dialog
        id="joinModal"
        :modal="true"
        :dismissableMask="true"
        :closable="false"
        :visible="state.displayJoin"
      >
        <template #header>
          <div class="p-grid p-d-flex p-ai-between" style="width: 100%">
            <h3 class="p-m-0 p-col-11 p-pb-0">Join game</h3>
            <i class="pi pi-times-circle p-col-1 p-mt-2 p-text-right p-mr-0" @click="setVisibleJoin" style="cursor: pointer"></i>
          </div>
        </template>
        <Suspense>
          <template #default>
            <JoinGame />
          </template>
          <template #fallback>
            Loading available games...
          </template>
        </Suspense>
          
        <template class="p-mx-auto" #footer>
          <router-link style="text-decoration: none" to="/gameboard/1">
            <Button
              label="Join"
              class="p-d-block p-mx-auto p-button-raised btn-dialog"
              :disabled="joinGameState.activeIndex == -1"
              @click="joinExistingGame"
            />
          </router-link>
        </template>
      </Dialog>
    </div>
    <Sidebar />
</template>

<script>
import { reactive } from "vue";
import CreateGame from "@/components/CreateGame";
import JoinGame from "@/components/JoinGame";
import Sidebar from "@/components/Sidebar";
import Leaderboard from '@/components/Leaderboard';
import LatestGame from '../components/LatestGame.vue';
import UserHandler from "@/modules/UserHandler";
import SocketHandler from "@/modules/SocketHandler";
import GameHandler from "@/modules/GameHandler";
import { useRouter } from "vue-router";

export default {
  name: "Lobby",
  components: { CreateGame, JoinGame, Sidebar, Leaderboard, LatestGame },
  setup() {
    const { createGame, joinGame, gameList, error } = SocketHandler();
    const { CreateGameState, inGame, joinGameState } = GameHandler();
    const { logout, currentUser } = UserHandler();
    const router = useRouter();

    const state = reactive({
      displayCreate: false,
      displayJoin: false,
      cardsOnHand: 5,
      pointsToWin: 15,
    });

    function setVisibleCreate() {
      state.displayCreate = !state.displayCreate;
    }

    function setVisibleJoin() {
      state.displayJoin = !state.displayJoin;
    }

    function createNewGame() {
      let selectedLeague = CreateGameState;
      if (CreateGameState.selectedLeague) {
        selectedLeague = { ...CreateGameState.selectedLeague };
      }
      createGame(currentUser.value.username, currentUser.value.avatar, selectedLeague);
      inGame.value = true;
    }

    function joinExistingGame() {
      joinGame(
        currentUser.value.username,
        currentUser.value.avatar, 
        gameList.value[joinGameState.activeIndex].roomNo
      );
      inGame.value = true;
    }

    async function performlogout() {
      await logout();
      router.push("/");
    }

    return {
      state,
      setVisibleCreate,
      setVisibleJoin,
      createNewGame,
      error,
      performlogout,
      joinExistingGame,
      joinGameState
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

.already-connected {
  color: red;
  text-shadow: 2px 2px black;
  font-family: "Press Start 2P", cursive;
}

.btn-dialog {
  background-color: #e2c3a6;
  border: 0.2em solid #3b1704;
  box-shadow: 0.2em 0.2em black;
  color: #3b1704;
  font-family: "Press Start 2P", cursive;
}

.leftCardButtonLobby{
  background-image: url("/space_nerd.png");
}

.leftCardButtonLobbyText{
  left: 36%;
}

.rightCardButtonLobby{
  background-image: url("/anonymous_hacker.png");
}

.rightCardButtonLobbyText{
  left: 38%;
}

.leaderboard {
  position: absolute;
  top: 2vh;
  left: 1vw;
}

.latestGame {
  position: absolute;
  top: 40vh;
  left: 1vw;
}
</style>
