<template>
  <div class="gameboard">
    <teleport to="#gameWindow">
      <div class="messageCont">
        <div
          class="waitOppo"
          v-if="
            gameState &&
            gameState?.currentPlayer != playerId &&
            gameState?.gameWinner === -1">
          Waiting for opponent to finish their turn...
          <WaitingForPlayer />
        </div>
      </div>
    </teleport>

    <Dialog
      id="opponentDisconnected"
      :modal="true"
      :dismissableMask="false"
      :closable="false"
      :visible="opponentDisconnected"
    >
      <template #header>
        Communication error
      </template>
      Your opponent has disconnected!
      <template #footer>
        <Button class="p-ripple" @click="returnToLobbyFunc(true)" label="Return to Lobby" />
      </template>
    </Dialog>

    <Dialog
      id="waitingForConnectionModal"
      :modal="true"
      :dismissableMask="true"
      :visible="state.connectedPlayers < 2"
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

    <div class="playerRow p-mt-2">
      <div class="profile profileOpp p-pt-1" :style="{}">
        <div>{{ opponent?.name || "waiting..." }}</div>
        <div class="p-mt-1"><img :src="'../avatar.jpg'" /></div>
        <div class="p-mt-1">
          Points: {{ opponent?.score }}/{{ gameState?.pointsToWin }}
        </div>
      </div>
      <div class="cardsOnHand">
        <div
          class="card p-mx-1"
          v-for="index in 5"
          :key="index"
          :style="{ backgroundImage: `url(${'../card_back.png'})` }"
        ></div>
      </div>
    </div>

    <div class="table" data-dis-container>
      <div class="cardsOnTable p-py-2" v-if="gameState?.gameWinner == -1">
        <div
          data-dis-type="self-contained"
          data-dis-particle-type="ExplodingParticle"
          class="card"
          v-for="(card, index) in gameState?.playedCards"
          :id="'card-' + index"
          :key="index"
          :style="{
            backgroundImage: `url(${'../' + getImageName(card.name) + '.png'})`,
          }"
        >
          <div class="cardPower">{{ card.currentPower }}</div>
          <div class="cardName">{{ card.name }}</div>
        </div>
      </div>

      <div class="gameOver p-px-5 p-my-3" v-else>
        <h4>GAME OVER</h4>
        <div v-if="playerId == gameState.gameWinner">You won!</div>
        <div v-else>You lost!</div>
        <div class="p-my-5">
          <Button class="p-ripple" @click="returnToLobbyFunc(false)" label="Return to Lobby" />
        </div>
      </div>
    </div>

    <div class="playerRow p-mb-2">
      <div class="cardsOnHand">
        <div
          :class="'card' + ' p-mx-1 ' + ' card-' + index"
          v-for="(card, index) in gameState?.players[playerId]?.cardsOnHand"
          :key="getIndex(card, index)"
          :style="{
            backgroundImage: `url(${'../' + getImageName(card.name) + '.png'})`,
          }"
          @click="animateCard(index)"
        >
          <div class="cardPower">{{ card.currentPower }}</div>
          <div class="cardName">{{ card.name }}</div>
        </div>
      </div>

      <div class="profile profileYou p-pt-1">
        <div>{{ gameState && gameState?.players[playerId]?.name }}</div>
        <div class="p-mt-1"><img :src="'/avatar/' + (isLoggedIn ? currentUser.avatar : 'avatar') + '.png'" /></div>
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
import { useRoute, useRouter } from "vue-router";
import GameHandler from "@/modules/GameHandler"
import UserHandler from "@/modules/UserHandler";

export default {
  name: "Gameboard",
  components: { WaitingForPlayer },
  setup() {
    const route = useRoute();
    const router = useRouter();
    const playerId = route.params.player;
    const { inGame } = GameHandler();
    const { currentUser, isLoggedIn } = UserHandler();

    const { gameState, playCard, opponentDisconnected, resetGameState, removeGame } = SocketHandler();
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
          gameState.value.roundWinner != -1
        ) {
          document.getElementsByClassName("hidden").forEach((element) => {
            element.classList.remove("hidden");
          });

          document
            .getElementsByClassName("cardToAnimate")
            .forEach((element) => {
              element.classList.toggle("cardToAnimate");
            });
        }

        if (gameState.value.playedCards.length == 2 && gameState.value.roundWinner != 2) {
          animateLoserCard();
        }

        if(gameState.value.gameWinner == playerId){
          console.log("in watchEffect - gameWinner === playerId");
        }
      }
    });

    function animateLoserCard() {
      let target;
      let winner = gameState.value.roundWinner;
      let loserCard;
      let root = document.documentElement;
      let targetY;
      let targetX;
      let startY;
      let startX;

      setTimeout(() =>{
        if ( winner == gameState.value?.startPlayer ) {
          loserCard = document.getElementById("card-1");
        } else {
          loserCard = document.getElementById("card-0");
        }

        if (winner == playerId) {
          target = document.getElementsByClassName("profileYou")[0];
          root.style.setProperty('--start',  "-20px");
          
          startY = loserCard.getBoundingClientRect().bottom;
          startX = loserCard.getBoundingClientRect().right;

          targetY = target.getBoundingClientRect().bottom;
          targetX = target.getBoundingClientRect().right;
        } else {
          target = document.getElementsByClassName("profileOpp")[0];
          root.style.setProperty('--start',  "50px");
         
          startY = loserCard.getBoundingClientRect().top;
          startX = loserCard.getBoundingClientRect().left;

          targetY = target.getBoundingClientRect().top;
          targetX = target.getBoundingClientRect().left;
        }
        
        root.style.setProperty('--target-y',  (targetY - startY) + "px");
        root.style.setProperty('--target-x', (targetX - startX) + "px");
        
        loserCard.classList.add("CSS-animation");
      }, 300);
    }

    function animateCard(index) {
      if (gameState.value?.currentPlayer != playerId) {
        return;
      }

      let cardToAnimate = document.getElementsByClassName("card-" + index)[0];
      cardToAnimate.classList.toggle("cardToAnimate");
      setTimeout(() => {
        playCard(index);
      }, 1000);
    }

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

    function returnToLobbyFunc(remove){
      opponentDisconnected.value = false;
      if(remove){
        removeGame();
      }
      resetGameState();
      inGame.value = false;
      router.push("/lobby");
    }

    return {
      state,
      getImageName,
      moveCard,
      switchIsDisabled,
      gameState,
      animateCard,
      playerId,
      opponent,
      getIndex,
      animateLoserCard,
      opponentDisconnected,
      returnToLobbyFunc,
      isLoggedIn,
      currentUser
    };
  },
};
</script>
<style lang="scss" scoped>

:root{
  --target-x: 0px;
  --target-y: 0px;
  --start: 0px;
}

.gameOver {
  font-size: 40px;
  color: red;
  text-align: center;
  // padding:0 20px;
  // margin:10px 0;
  background-color: rgba($color: #e2c3a6, $alpha: 0.8);
  border: 2px solid #3b1704;
  border-radius: 10px;

  h4 {
    text-shadow: 2px 2px black;
    font-family: "Press Start 2P", cursive;
  }

  div {
    font-size: 30px;
    font-family: "Press Start 2P", cursive;
  }

  Button {
    background-color: darken(#e2c3a6, 10%);
    border: 0.2em solid #3b1704;
    box-shadow: 0.2em 0.2em black;
    color: #3b1704;
    font-family: "Press Start 2P", cursive;
  }
}

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
  height: 35%;
  justify-content: center;
}

.profile {
  width: 20%;
  display: flex;
  flex-direction: column;
  text-align: center;
  background-color: #e2c3a6;
  color: #3b1704;
  border: 2px solid #3b1704;
  border-radius: 2px;
  font-family: "MedievalSharp", cursive;
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
  top: 0;
  left: 0;
}

.cardToAnimate {
  transition: all 1s;
  animation: playCard 1s ease 1;
}

@keyframes playCard{
  0% {
    top: 0;
    opacity: 1
  }
  50% {
     top: -14vh; 
  }
  99% {
    top: -28vh;
    opacity: 1;
  }
  to {
    top: -28vh;
    opacity: 0;
  }
}

.hidden {
  display: none;
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

.messageCont {
  position: absolute;
  width: 100%;
  bottom: 1vh;
  z-index: 11;
  margin: 0 auto;
}

.waitOppo,
.spinner {
  position: relative;
  background-color: #e2c3a6;
  opacity: 80%;
  color: #3b1704;
  bottom: 0;
  left: 0;
  right: 0;
  width: 350px;
  margin: 0 auto;
  text-align: center;
  padding: 1% 0;
}

.CSS-animation {
  animation: bounceOutDown 1.5s forwards;
}
@keyframes bounceOutDown {
  50% {
    opacity: 1; transform: translate3d(0, 0, 0); z-index: 15;
  }

  60% {
    transform: translate3d(0, var(--start), 0);
  }

  90% {
    opacity: 1;
  }

  to {
    opacity: 0;
    transform: translate3d(var(--target-x), var(--target-y), 0);
  }
}
</style>
