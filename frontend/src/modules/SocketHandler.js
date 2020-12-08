import { ref } from "vue";
// import io from 'socket.io-client';
import { socketClient } from '@/main'

export default function SocketHandler() {
  // let client = io();
  const error = ref(null);
  const gameState = ref(null);
  const isConnected = ref(true);

  console.log('SocketHandler.js - socketClient:', socketClient);

  // const connect = async () => {
  //   if (isConnected.value) return;

  //   try {
  //     client = io.connect("http://localhost:9092", {
  //       "reconnection delay": 2000,
  //       "force new connection": true,
  //     });       
  //   } catch (e) {
  //     error.value = e;
  //   }
  // } // connect

  socketClient.on("connect", function () {
    isConnected.value = true;
    console.log("Connected to server")
    // socketClient.emit("SEND_PLAYER_NAME", "James Bond" + new Date(), () => {});
  });

  // Keep if we're going to have a in-game chat function later in sprint 2
  socketClient.on("message", (data) => {
    console.log("data i message: ", data);
  });

  // Keep if we're going to have a in-game chat function later in sprint 2
  function sendMessage(message) {
    socketClient.emit("send", message);
  }

  socketClient.on("GAME_UPDATE", (incomingGameState) => {
    console.log("GAME_UPDATE received");
    console.log("gameState: ", JSON.parse(incomingGameState));
    gameState.value = incomingGameState
  });

  // socketClient.subscribe('message', (data) => {
  //   console.log("Message recieved:", data);
// });

  function createGame(name) {
    console.log("Trying to create a new game, with the name", name);
    // function createGame(name, pointsToWin, cardsOnHand) {
    socketClient.emit("CREATE_GAME", name);// { name, pointsToWin, cardsOnHand });
  }

  function joinGame(name, roomNo) {
    socketClient.emit("JOIN_GAME", { name, roomNo });
  }

  // send the played card as an index in a string format
  function playCard(cardId) {
    socketClient.emit("PLAYED_CARD", cardId);
  }

  socketClient.on("connect_error", (e) => {
    console.log("i connect_error:", e);
    error.value = e;
  });

  socketClient.on("disconnect", () => {
    console.log("disconnected from the server");
    isConnected.value  = false;
    // client = null;
  });

  return { playCard, sendMessage, createGame, joinGame, error, gameState, isConnected }
}