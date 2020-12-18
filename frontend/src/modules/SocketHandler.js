import { ref } from "vue";
import SocketIO from 'socket.io-client'
import { v4 as uuid } from 'uuid';

const error = ref(null);
let gameState = ref(null);
const isConnected = ref(true);
const gameList = ref(null);
const opponentDisconnected = ref(false);

let client = null;
export default function SocketHandler() {
  if (client === null) {
    // client = SocketIO("http://192.168.0.177:9092", {
   client = SocketIO("http://localhost:9092", {
    reconnectionDelay: 500,
    reconnection: false,
    timeout: 2000,
    forceNew: true,
    query: {
      token: uuid()
  }
      // "force new connection": true,
    });
  }

  client.on("connect", function () {
    isConnected.value = true;
    console.log("Connected to server")
  });

  // Keep if we're going to have a in-game chat function later in sprint 2
  client.on("MESSAGE", (data) => {
    console.log("data i message: ", data);
  });

  // Keep if we're going to have a in-game chat function later in sprint 2
  function sendMessage(message) {
    client.emit("SEND", message);
  }

  client.on("GAME_UPDATE", (incomingGameState) => {
    console.log("GAME_UPDATE received", JSON.parse(incomingGameState));
    gameState.value = JSON.parse(incomingGameState);
  });

  client.on("OPPONENT_DISCONNECTED", () => {
    opponentDisconnected.value = true;
    console.log("OPPONENT_DISCONNECTED received!!!!!!!!!!!!!!!!");
  });

  client.on("LIST_GAMES", (data) => {
    console.log("LIST_GAMES", JSON.parse(data));
    gameList.value = JSON.parse(data);
  })

  client.on("connect_error", (e) => {
    console.log("i connect_error:", e);
    error.value = e;
  });

  client.on("disconnect", (reason) => {
    console.log("disconnected from the server, reason:", reason);
    if (reason === 'transport close') {
      client.close()
      console.log("");
    }
    isConnected.value = false;
    // client = null;
  });

  function createGame(name, pointsToWin, cardsOnHand) {
    client.emit("CREATE_GAME", { name, pointsToWin, cardsOnHand });
  }

  function joinGame(name, roomNo) {
    client.emit("JOIN_GAME", { name, roomNo });
  }

  // send the played card as an index in a string format
  function playCard(cardId) {
    console.log("playCard: ", cardId, " gs: ", gameState.value);
    client.emit("PLAYED_CARD", cardId);
  }

  function getGameList(){
    client.emit("AVAILABLE_GAMES");
  }

  function resetGameState() {
    gameState = ref(null);
  }

  return { playCard, sendMessage, createGame, joinGame, getGameList, gameList, error, gameState, isConnected, opponentDisconnected, resetGameState }
}