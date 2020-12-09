<template>
  <div class="gameboard">
    <Dialog
        id="joinModal"
        :modal="true"
        :dismissableMask="true"
        :visible="state.connectedPlayers < 2"
        :closable="false"
      >
        <template #header>
          <h3 v-if="state.connectedPlayers < 2">Waiting for opponent to connect...</h3>
        </template>
    <WaitingForPlayer />
        <template class="p-mx-auto" #footer>
        </template>
      </Dialog>
    <div class="playerRow p-mt-2">
      <div class="profile profileOpp p-pt-1">
        <div>Name </div>
        <div class="p-mt-1"><img :src="'../avatar.jpg'" /></div>
        <div class="p-mt-1">Points </div>
      </div>
      <draggable class="cardsOnHand" :list="state.cardsOnHandOpponent">
        <div
          class="card p-mx-1"
          v-for="index in 5"
          :key="index"
          :style="{ backgroundImage: `url(${'../card_back.png'})` }"
        >
        </div>
      </draggable>
    </div>
    <div class="table">
      <draggable
        class="cardsOnTable p-py-2"
        :list="state.playedCards"
        group="cards"
        item-key="index"
         
      >
        <div
          class="card"
          v-for="(card, index) in state.playedCards"
          :key="index"
          :style="{ backgroundImage: `url(${'../' + getImageName(card.name) + '.png'})` }"
        >
          <div class="cardPower">{{card.power}}</div>
          <div class="cardName">{{card.name}}</div>
        </div>
      </draggable>
    </div>
    <div class="playerRow p-mb-2">
      <draggable
        class="cardsOnHand"
        :list="state.cardsOnHand"
        :group="{ name: 'cards', pull: true}"
        :clone="moveCard"
        @change="switchIsDisabled"
        item-key="index"
        :disabled="state.isDisabled"
      >
        <div
          class="card p-mx-1"
          v-for="(card, index) in state.cardsOnHand"
          :key="index"
          :style="{ backgroundImage: `url(${'../' + getImageName(card.name) + '.png'})` }"
          @click="playCard(index)"
        >
          <div class="cardPower">{{card.power}}</div>
          <div class="cardName">{{card.name}}</div>
        </div>
      </draggable>
      <div class="profile profileYou p-pt-1">
        <div>Name </div>
        <div class="p-mt-1"><img :src="'../avatar.jpg'" /></div>
        <div class="p-mt-1">Points </div>
      </div>
    </div>
  </div>
</template>
<script>
import { reactive, watchEffect } from "vue";
import { VueDraggableNext } from "vue-draggable-next";
import SocketHandler from '@/modules/SocketHandler';
import WaitingForPlayer from '../components/WaitingForPlayer';

export default {
  name: "Gameboard",
  components: { draggable: VueDraggableNext, WaitingForPlayer },
  setup() {
    const { gameState, playCard } = SocketHandler();
    const state = reactive({
      cardsOnHand: [
        { name: "Mutated Worm", power: 1 },
        { name: "Orange Menace", power: 3 },
        { name: "Sleepy Joe", power: 4 },
        { name: "Anonymous Hacker", power: 8 },
        { name: "Super Galaxy Face Melter", power: 10 },
      ],
      playedCards: [],
      connectedPlayers: 1,
      isDisabled: false,
      cardsOnHandSize: 5,
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

    function getImageName(name) {
      return name.replaceAll(" ", "_").toLowerCase();
    }

    function moveCard(card) {
      return card;
    }

    function switchIsDisabled() {
      if(state.cardsOnHand.length < state.cardsOnHandSize) {
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