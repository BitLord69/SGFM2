<template>
  <div class="gameboard">
    <!-- Particles used to animating exploding cards when there is a tie -->
    <div v-for="i in 300" :key="i" class="particle particle-hidden"></div>

    <teleport to="#gameWindow">
      <div class="messageCont">
        <div
          class="waitOppo"
          v-if="
            gameState &&
            gameState?.gameWinner != 2 &&
            gameState?.currentPlayer != playerId &&
            gameState?.gameWinner == -1
          "
        >
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
      <template #header> Communication error </template>
      Your opponent has disconnected!
      <template #footer>
        <Button
          class="p-ripple"
          @click="returnToLobbyFunc(true)"
          label="Return to Lobby"
        />
      </template>
    </Dialog>

    <Dialog
      id="rematchDenied"
      :modal="true"
      :dismissableMask="false"
      :closable="false"
      :visible="rematchState.message.length">
      <template #header>
        <div class="p-text-center">Opponent left the game!</div>
      </template>
        <div class="p-text-center">
          <h3>{{ rematchState.message }}</h3>
          <div>
            Hang on, you will be tranferred to the lobby shortly.
          </div>
        </div>
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
      </template>
      <WaitingForPlayer />
      <template class="p-mx-auto" #footer> </template>
    </Dialog>

    <div class="playerRow">
      <div class="profile p-pt-1" id="profileOpponent">
        <div>{{ opponent?.name || "waiting..." }}</div>
        <div class="p-mt-1"><img :src="opponentAvatar" /></div>
        <div class="p-mt-1">
          Points: {{ opponent?.score || 0 }}/{{ gameState?.pointsToWin }}
        </div>
      </div>
      <div class="cardsOnHand">
        <div
          class="card p-mx-1"
          v-for="index in opponent?.cardsOnHand.length || 5"
          :key="index"
          :style="{ backgroundImage: `url(${'../card_back.png'})` }"
        ></div>
      </div>
    </div>

    <div class="table">
      <div
        class="cardsOnTable p-py-2"
        v-if="gameState && gameState?.gameWinner == -1"
      >
        <div
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

      <div class="gameOver p-px-5 p-my-4" v-if="gameState.gameWinner !== -1">
        <h4 class="p-my-5">GAME OVER</h4>
        <div class="p-mb-1">
          <div v-if="playerId == gameState.gameWinner">You won!</div>
          <div v-if="playerId != gameState.gameWinner && gameState.gameWinner != 2">
            You lost!
          </div>
          <div v-if="gameState.gameWinner == 2">Game is a tie!</div>
        </div>
        <div class="p-my-5">
          <div style="font-size: 1.4rem;" class="p-mb-3">Rematch?</div>
          <Button
            class="p-ripple p-mr-6"
            @click="requestRematch"
            label="Yes"
            :disabled="rematchState.rematchRequested"
          />
          <Button
            class="p-ripple"
            @click="returnToLobbyFunc(false)"
            label="No"
          />
        </div>
      </div>
    </div>

    <div class="playerRow p-mb-2">
      <div class="cardsOnHand">
        <div
          :class="
            'card p-mx-1 card-' +
            index +
            (gameState && playerId != gameState.currentPlayer
              ? ''
              : ' cardHoverable')
          "
          v-for="(card, index) in gameState?.players[playerId]?.cardsOnHand"
          :key="index"
          :style="{
            backgroundImage: `url(${'../' + getImageName(card.name) + '.png'})`,
          }"
          @click="animateCard(index)"
        >
          <div class="cardPower">{{ card.currentPower }}</div>
          <div class="cardName">{{ card.name }}</div>
        </div>
      </div>

      <div class="profile p-pt-1" id="profileYou">
        <div>{{ gameState && gameState?.players[playerId]?.name }}</div>
        <div class="p-mt-1">
          <img :src="'/avatar/' + currentUser.avatar + '.png'" />
        </div>
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
import { useRoute, useRouter } from "vue-router";
import { reactive, watchEffect, computed } from "vue";

import SocketHandler from "@/modules/SocketHandler";
import WaitingForPlayer from "../components/WaitingForPlayer";
import GameHandler from "@/modules/GameHandler";
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
    const {
      rematch,
      playCard,
      gameState,
      removeGame,
      denyRematch,
      rematchState,
      resetGameState,
      resetMatchState,
      opponentDisconnected,
    } = SocketHandler();
    const state = reactive({
      connectedPlayers: 1,
      cardsOnHandSize: 5,
    });

    const opponent = computed(() => {
      let o = playerId == 0 ? 1 : 0;
      let player =
        gameState && gameState.value?.players.length > 1
          ? gameState.value?.players[o]
          : null;
      return player;
    });

    const opponentAvatar = computed(() => {
      return (
        "/avatar/" +
        (opponent?.value?.name ? opponent.value.avatarId : "0") +
        ".png"
      );
    });

    watchEffect(() => {
      if (rematchState.rematchDenied) {
        rematchState.message = "Sorry, your opponent doesn't like you any more and has left the game!";
        setTimeout(() => { 
          resetGameState(); 
          resetMatchState();
          router.push("/lobby");
        }, 3500);
      }
    });

    watchEffect(() => {
      if (gameState.value !== null) {
        if (gameState.value.players.length > 1) {
          state.connectedPlayers = 2;
        }

        if (gameState.value.roundWinner != -1) {
          document.getElementsByClassName("hidden").forEach((element) => {
            element.classList.remove("hidden");
          });

          document
            .getElementsByClassName("cardToAnimate")
            .forEach((element) => {
              element.classList.toggle("cardToAnimate");
            });
        } else {
          document.getElementsByClassName("particle").forEach((element) => {
            if (!element.classList.contains("particle-hidden"))
              element.classList.toggle("particle-hidden");
          });
        }

        if (gameState.value.playedCards.length == 2) {
          if (gameState.value.roundWinner != 2) {
            animateLoserCard();
          } else if (gameState.value.roundWinner == 2) {
            animateTie();
          }
        }

        if (playerId == gameState.value.gameWinner) {
          startParticleAnimation();
          setTimeout(() => {
            document.getElementsByClassName("particle").forEach((element) => {
              element.className += " particle-hidden";
            });
          }, 4000);
        }
      }
    });

    function requestRematch() {
      rematch(currentUser.value.username, currentUser.value.avatar);
    }

    function startParticleAnimation() {
      setTimeout(() => {
        document.getElementsByClassName("particle").forEach((element) => {
          element.classList.remove("particle-hidden");
        });
      }, 300);
    }

    function animateTie() {
      setTimeout(() => {
        document.getElementById("card-0").className += " tie";
        document.getElementById("card-1").className += " tie";

        startParticleAnimation();
      }, 300);
    }

    function animateLoserCard() {
      let startY;
      let startX;
      let target;
      let targetY;
      let targetX;
      let loserCard;
      let root = document.documentElement;
      let winner = gameState.value.roundWinner;

      setTimeout(() => {
        if (winner == gameState.value?.startPlayer) {
          loserCard = document.getElementById("card-1");
        } else {
          loserCard = document.getElementById("card-0");
        }

        if (!loserCard) return;

        if (winner == playerId) {
          target = document.getElementById("profileYou");
          root.style.setProperty("--start", "-20px");

          startY = loserCard.getBoundingClientRect().bottom;
          startX = loserCard.getBoundingClientRect().right;

          targetY = target.getBoundingClientRect().bottom;
          targetX = target.getBoundingClientRect().right;
        } else {
          target = document.getElementById("profileOpponent");
          root.style.setProperty("--start", "50px");

          startY = loserCard.getBoundingClientRect().top;
          startX = loserCard.getBoundingClientRect().left;

          targetY = target.getBoundingClientRect().top;
          targetX = target.getBoundingClientRect().left;
        }

        root.style.setProperty("--target-y", targetY - startY + "px");
        root.style.setProperty("--target-x", targetX - startX + "px");

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

    function returnToLobbyFunc(remove) {
      console.log("i returnToLobbyFunc");
      opponentDisconnected.value = false;
      if (remove) {
        removeGame();
      }
      denyRematch();
      resetGameState();
      inGame.value = false;
      router.push("/lobby");
    }

    return {
      state,
      getImageName,
      moveCard,
      gameState,
      animateCard,
      playerId,
      opponent,
      getIndex,
      animateLoserCard,
      opponentDisconnected,
      returnToLobbyFunc,
      isLoggedIn,
      currentUser,
      opponentAvatar,
      requestRematch,
      rematchState,
    };
  },
};
</script>
<style lang="scss" scoped>
$particle-size: 8px;

:root {
  --target-x: 0px;
  --target-y: 0px;
  --start: 0px;
}

.gameOver {
  font-size: 40px;
  color: red;
  text-align: center;
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

  button {
    background-color: darken(#e2c3a6, 10%);
    border: 0.2em solid #3b1704;
    box-shadow: 0.2em 0.2em black;
    color: #3b1704;
    font-family: "Press Start 2P", cursive;
  }
}

.gameboard {
  width: 70%;
  display: flex;
  max-width: 1200px;
  position: relative;
  flex-direction: column;
  justify-content: space-between;
}

.playerRow {
  display: flex;
  width: 100%;
  height: 32%;
}

.table {
  display: flex;
  width: 100%;
  height: 35%;
  justify-content: center;
}

.profile {
  width: 20%;
  max-width: 150px;
  display: flex;
  flex-direction: column;
  align-self: center;
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
  border: 2px solid #2c3e50;
  border-radius: 50%;
}

#profileYou img {
  border: 2px solid green;
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

@keyframes playCard {
  0% {
    top: 0;
    opacity: 1;
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

.hidden,
.particle-hidden {
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

.cardHoverable:hover {
  box-shadow: 0px 0px 15px 2px #eee;
  z-index: 10;
  transform: scale(1.05);
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
    opacity: 1;
    transform: translate3d(0, 0, 0);
    z-index: 15;
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

.tie {
  animation: fade 5s;
}

.particle {
  z-index: 99;
  position: absolute;
  width: $particle-size;
  height: $particle-size;
  animation: shoot 4s ease-out;
  animation-name: shoot, fade;

  @for $i from 0 to 300 {
    $t: (1 + 0.01 * random(100)) * 1.5s;

    &:nth-child(#{$i + 1}) {
      transform: translate(random(80) * 1vw, random(80) * 1vh);
      background: hsl(random(360), 100%, 50%);
      animation-duration: $t;
      animation-delay: -0.01 * random(100) * $t;
    }
  }
}

@keyframes shoot {
  0% {
    transform: translate(35vw, 50vh);
  }
}

@keyframes fade {
  to {
    opacity: 0;
  }
}
</style>
