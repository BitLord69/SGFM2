<template>
  <div class="gameboard">
    <Dialog
        id="joinModal"
        :modal="true"
        :dismissableMask="true"
        :visible="state.connectedPlayers < 2 || (gameState && gameState?.currentPlayer != playerId)"
        :closable="false"
      >
        <template #header>
          <h3 v-if="state.connectedPlayers < 2">Waiting for opponent to connect...</h3>
          <h3 v-else>Waiting for opponent to finish their turn...</h3>
        </template>
      <WaitingForPlayer />
        <template class="p-mx-auto" #footer>
        </template>
    </Dialog>

    <div class="playerRow p-mt-2">
      <div class="profile profileOpp p-pt-1">
        <div>{{ opponent?.name || 'waiting...'}}</div>
        <div class="p-mt-1"><img :src="'../avatar.jpg'" /></div>
        <div class="p-mt-1">Points: {{ opponent?.score }}/{{gameState?.pointsToWin}} </div>
      </div>
      <div class="cardsOnHand">
        <div
          class="card p-mx-1"
          v-for="index in 5"
          :key="index"
          :style="{ backgroundImage: `url(${'../card_back.png'})` }"
        >
        </div>
      </div>
    </div>
    <div class="table">
      <draggable
        class="cardsOnTable p-py-2"
        :list="gameState?.playedCards"
        group="cards"
        item-key="index">
        <div
          class="card"
          v-for="(card, index) in gameState?.playedCards"
          :key="index"
          :style="{ backgroundImage: `url(${'../' + getImageName(card.name) + '.png'})` }"
        >
          <div class="cardPower">{{card.currentPower}}</div>
          <div class="cardName">{{card.name}}</div>
        </div>
      </draggable>
    </div>
    <div class="playerRow p-mb-2">
      <draggable
        class="cardsOnHand"
        :list="gameState?.players[playerId]?.cardsOnHand"
        :group="{ name: 'cards', pull: true}"
        :clone="moveCard"
        @change="switchIsDisabled"
        item-key="index"
        :disabled="state.isDisabled"
      >
        <div
          class="card p-mx-1"
          v-for="(card, index) in gameState?.players[playerId]?.cardsOnHand"
          :key="getIndex(card, index)"
          :style="{ backgroundImage: `url(${'../' + getImageName(card.name) + '.png'})` }"
          @click="playCard(index)"
        >
          <div class="cardPower">{{card.currentPower}}</div>
          <div class="cardName">{{card.name}}</div>
        </div>
      </draggable>
      <div class="profile profileYou p-pt-1">
        <div>{{ gameState && gameState?.players[playerId]?.name }}</div>
        <!-- <div>Ditt namn...</div> -->
        <div class="p-mt-1"><img :src="'../avatar.jpg'" /></div>
        <div class="p-mt-1">Points: {{ gameState && gameState?.players[playerId]?.score }}/{{gameState?.pointsToWin}}</div>
      </div>
    </div>
  </div>
</template>

<script>


import { reactive, watchEffect, computed } from "vue";
import { VueDraggableNext } from "vue-draggable-next";
import SocketHandler from '@/modules/SocketHandler';
import WaitingForPlayer from '../components/WaitingForPlayer';
import { useRoute } from 'vue-router'


export default {
  name: "Gameboard",
  components: { draggable: VueDraggableNext, WaitingForPlayer },
  setup() {
    const route = useRoute();
    const playerId = route.params.player;

    const { gameState, playCard } = SocketHandler();
    const state = reactive({
      connectedPlayers: 1,
      isDisabled: false,
      cardsOnHandSize: 5
    });

    const opponent = computed(() => {
      let o = playerId == 0 ? 1 : 0;
      let player = (gameState && gameState.value?.players.length > 1) ? gameState.value?.players[o] : null;
    console.log("opponent", player || 'null');
      return player;
    });

    watchEffect(
      () => {
        if (gameState.value !== null) {
          if (gameState.value.players.length > 1) {
            state.connectedPlayers = 2
          }
        }
      }
    )

    function getIndex(card, index){
      card.index = index;
      return index
    }

    function getImageName(name) {
      return name.replaceAll(" ", "_").toLowerCase();
    }

    function moveCard(card) {
      return card;
    }

    function switchIsDisabled() {
      if(gameState.value?.players[playerId].cardsOnHand.length < state.cardsOnHandSize) {
        state.isDisabled = !state.isDisabled;
      }
    }

    return {
      state,
      getImageName,
      moveCard,
      switchIsDisabled,
      gameState,
      playCard,
      playerId,
      opponent,
      getIndex
    };
  },
}
</script>
<style scoped>
.gameboard {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.playerRow {
  display: flex;
  width: 100%;
  height: 32%;
  justify-content: space-between;
}

.table {
  display: flex;
  width: 100%;
  height: 32%;
  justify-content: center;
}

.profile {
  width: 20%;
  display: flex;
  flex-direction: column;
  text-align: center;
  background-color:  #e2c3a6;
  color: #3b1704;
  border: 2px solid #3b1704;
  border-radius: 2px;
  font-family: 'MedievalSharp', cursive;
  font-weight: bold;
}

.profile img {
  width: 50%;
  height: 100%;
}

.profileOpp {
  margin-bottom: 8.5%;
}

.profileYou {
  margin-top: 8.5%;
}

.cardsOnHand {
  display: flex;
  width: 80%;
  justify-content: space-evenly;
}

.cardsOnTable {
  display: flex;
  width: 80%;
  justify-content: center;
  background-size: 100% 100%;
}

.card {
  width: 20%;
  background-size: 100% 100%;
  position: relative;
  color: #3b1704;
}

.cardPower {
  position: absolute;
  top: 5%;
  left: 20%;
  font-family: 'MedievalSharp', cursive;
}

.cardName {
  position: absolute;
  bottom: 9.5%;
  width: 100%;
  text-align: center;
  font-size: 90%;
  font-family: 'Yanone Kaffeesatz', sans-serif;
}
</style>