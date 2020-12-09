import { ref } from "vue";
// import io from 'socket.io-client';
import SocketIO from 'socket.io-client'
// import { socketClient } from '@/main'
const error = ref(null);
const gameState = ref(null);
const isConnected = ref(true);

export default function SocketHandler() {
  let client = SocketIO("http://localhost:9092", {
    "reconnection delay": 2000,
    "force new connection": true,
  });
  

  /* const connect = async () => {
    if (isConnected.value) return;

    try {
      client = SocketIO("http://localhost:9092", {
        "reconnection delay": 2000,
        "force new connection": true,
      });       
    } catch (e) {
      error.value = e;
    }
  } // connect */

  client.on("connect", function () {
    isConnected.value = true;
    console.log("Connected to server")
    // client.emit("SEND_PLAYER_NAME", "James Bond" + new Date(), () => {});
  });

  // Keep if we're going to have a in-game chat function later in sprint 2
  client.on("message", (data) => {
    console.log("data i message: ", data);
  });

  // Keep if we're going to have a in-game chat function later in sprint 2
  function sendMessage(message) {
    client.emit("send", message);
  }

  client.on("GAME_UPDATE", (incomingGameState) => {
    console.log("GAME_UPDATE received");
    // console.log("gameState: ", JSON.parse(incomingGameState));
    gameState.value = incomingGameState
  });


  function createGame(name) {
    console.log("Trying to create a new game, with the name", name);
    // function createGame(name, pointsToWin, cardsOnHand) {
    client.emit("CREATE_GAME", name);// { name, pointsToWin, cardsOnHand });
  }

  function joinGame(name, roomNo) {
    client.emit("JOIN_GAME", { name, roomNo });
  }

  // send the played card as an index in a string format
  function playCard(cardId) {
    client.emit("PLAYED_CARD", cardId);
  }

  client.on("connect_error", (e) => {
    console.log("i connect_error:", e);
    error.value = e;
  });

  client.on("leave", () => {
    console.log("Leaving room!");
  })

  client.on("disconnect", () => {
    console.log("disconnected from the server");
    isConnected.value  = false;
    // client = null;
  });

  return { playCard, sendMessage, createGame, joinGame, error, gameState, isConnected }
}