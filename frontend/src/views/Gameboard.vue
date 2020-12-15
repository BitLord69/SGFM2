<template>
  <div class="gameboard">
    <Dialog
      id="joinModal"
      :modal="true"
      :dismissableMask="true"
      :visible="
        state.connectedPlayers < 2 ||
        (gameState &&
          gameState?.currentPlayer != playerId &&
          gameState?.gameWinner === -1)
      "
      :closable="false"
    >
      <template #header>
        <h3 v-if="state.connectedPlayers < 2">
          Waiting for opponent to connect...
        </h3>
        <h3 v-else>Waiting for opponent to finish their turn...</h3>
      </template>
      <WaitingForPlayer />
      <template class="p-mx-auto" #footer> </template>
    </Dialog>

    <div class="opponentRow p-mt-2">
      <div class="profile profileOpp p-pt-1">
        <div>{{ opponent?.name || "waiting..." }}</div>
        <div class="p-mt-1"><img :src="'../avatar.jpg'" /></div>
        <div class="p-mt-1">
          Points: {{ opponent?.score }}/{{ gameState?.pointsToWin }}
        </div>
      </div>
      <div class="card p-mx-1" v-for="index in 5" :key="index">
        <img src="/card_back.png" />
      </div>
    </div>
    <div class="table">
      <div class="cardsOnTable p-py-2" v-if="gameState?.gameWinner == -1">
        <div
          class="card"
          v-for="(card, index) in gameState?.playedCards"
          :key="index"
          :style="{
            backgroundImage: `url(${'../' + getImageName(card.name) + '.png'})`,
          }"
        >
          <div class="cardPower">{{ card.currentPower }}</div>
          <div class="cardName">{{ card.name }}</div>
        </div>
      </div>
      <div class="gameOver" v-else>
        <h3>GAME OVER</h3>
        <div v-if="playerId == gameState.gameWinner">You won!</div>
        <div v-else>You lost!</div>
      </div>
    </div>
    <div class="playerRow p-mb-2">
      <div
        :class="'card' + ' p-mx-1 ' + ' card-' + index"
        v-for="(card, index) in gameState?.players[playerId]?.cardsOnHand"
        :key="getIndex(card, index)"
        :style="{
          backgroundImage: `url(${'../' + getImageName(card.name) + '.png'})`,
        }"
        @click="cardToPlay(index)"
      >
        <div class="cardPower">{{ card.currentPower }}</div>
        <div class="cardName">{{ card.name }}</div>
      </div>
      <div class="profile profileYou p-pt-1">
        <div>{{ gameState && gameState?.players[playerId]?.name }}</div>
        <div class="p-mt-1"><img :src="'../avatar.jpg'" /></div>
        <div class="p-mt-1">
          Points: {{ gameState && gameState?.players[playerId]?.score }}/{{
            gameState?.pointsToWin
          }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { reactive, watchEffect, computed } from "vue";
import SocketHandler from "@/modules/SocketHandler";
import WaitingForPlayer from "../components/WaitingForPlayer";
import { useRoute } from "vue-router";

export default {
  name: "Gameboard",
  components: { WaitingForPlayer },
  setup() {
    const route = useRoute();
    const playerId = route.params.player;

    const { gameState, playCard } = SocketHandler();
    const state = reactive({
      connectedPlayers: 1,
      isDisabled: false,
      cardsOnHandSize: 5,
    });

    const opponent = computed(() => {
      let o = playerId == 0 ? 1 : 0;
      let player =
        gameState && gameState.value?.players.length > 1
          ? gameState.value?.players[o]
          : null;
      console.log("opponent", player || "null");
      return player;
    });

    watchEffect(() => {
      if (gameState.value !== null) {
        if (gameState.value.players.length > 1) {
          state.connectedPlayers = 2;
        }

        if (
          gameState &&
          (gameState.value.currentPlayer !== playerId ||
            gameState.value.startPlayer !== playerId)
        ) {
          document.getElementsByClassName("animCard").forEach((card) => {
            card.classList.toggle("animCard");
          });
        }
      }
    });

    function getIndex(card, index) {
      card.index = index;
      return index;
    }

    function getImageName(name) {
      return name.replaceAll(" ", "_").toLowerCase();
    }

    function moveCard(card) {
      return card;
    }

    function switchIsDisabled() {
      if (
        gameState.value?.players[playerId].cardsOnHand.length <
        state.cardsOnHandSize
      ) {
        state.isDisabled = !state.isDisabled;
      }
    }

    function cardToPlay(index) {
      //let kortKlass = "card-" + index;
      document
        .getElementsByClassName("card-" + index)[0]
        .classList.toggle("animCard");

      setTimeout(() => {
        playCard(index);
      }, 1500);
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
      getIndex,
      cardToPlay,
    };
  },
};
</script>
<style lang="scss" scoped>
.gameOver {
  font-size: 40px;
  color: red;
}

.gameboard {
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.opponentRow {
  top: 0;
  left: 5%;
  position: relative;
  width: 90%;
  height: 30%;
  display: flex;
  justify-content: space-between;
}

.table {
  left: 5%;
  position: relative;
  width: 90%;
  height: 67%;
}

.playerRow {
  left: 5%;
  width: 90%;
  height: 54%;
  position: relative;
  display: flex;
  justify-content: space-between;
}

.profile {
  width: 10vw;
  height: 70%;
  position: relative;
  text-align: center;
  background-color: #e2c3a6;
  color: #3b1704;
  border: 2px solid #3b1704;
  border-radius: 2px;
  font-family: "MedievalSharp", cursive;
  font-weight: bold;
}

.profileYou {
  align-self: flex-end;
}

.profile img {
  width: 50%;
  height: 100%;
}

.cardsOnHand {
  width: 100%;
  position: relative;
}

.cardsOnTable {
  display: flex;
  width: 30%;
  height: 100%;
  position: absolute;
  background-size: 100% 100%;
  .card {
    width: 50%;
    height: 90%;
  }
}

.card {
  width: 18%;
  height: 90%;
  //background-size: 100% 100%;
  position: relative;
  color: #3b1704;
  transition: 400ms ease;
  img {
    width: 90%;
    height: 100%;
  }
  /* &:hover {
    width: 21%;
    height: 100%;
    z-index: 10;
    transition: 400ms ease;
  } */
}

.card-0 {
  //transform: rotate(-5deg) translate(70%, 0%);
  z-index: 1;
  top: 0;
  left: 0;
}
.card-1 {
  //transform: rotate(-2.5deg) translate(30%, -6%);
  z-index: 2;
  top: 0;
  left: 0;
}
.card-2 {
  //transform: rotate(0deg) translate(0%, -7.5%);
  z-index: 3;
  top: 0;
  left: 0;
}
.card-3 {
  //transform: rotate(2.5deg) translate(-40%, -5%);
  z-index: 4;
  top: 0;
  left: 0;
}
.card-4 {
  //transform: rotate(5deg) translate(-75%, 0%);
  z-index: 5;
  top: 0;
  left: 0;
}

.cardPower {
  position: absolute;
  top: 5%;
  left: 20%;
  font-family: "MedievalSharp", cursive;
}

.cardName {
  position: absolute;
  bottom: 9.5%;
  width: 100%;
  text-align: center;
  font-size: 90%;
  font-family: "Yanone Kaffeesatz", sans-serif;
}

.animCard {
  transform: none;
  transition: all 1.5s;
  position: relative;
  top: -28vh;
  left: -25vw;
}
</style>