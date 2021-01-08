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
    // client = SocketIO("http://98.128.140.50:9092", {
   client = SocketIO("http://localhost:9092", {
    reconnectionDelay: 500,
    reconnection: false,
    timeout: 2000,
    forceNew: true,
    query: {
      token: uuid()
    }
    });
  }

  client.on("connect", function () {
    isConnected.value = true;
  });

  // Keep if we're going to have a in-game chat function later in sprint 2
  client.on("MESSAGE", (data) => {
    console.log("data i message: ", data);
  });

  client.on("GAME_UPDATE", (incomingGameState) => {
    gameState.value = JSON.parse(incomingGameState);
  });

  client.on("OPPONENT_DISCONNECTED", () => {
    opponentDisconnected.value = true;
  });

  client.on("LIST_GAMES", (data) => {
    gameList.value = JSON.parse(data);
  })

  client.on("connect_error", (e) => {
    error.value = e;
  });

  client.on("disconnect", (reason) => {
    if (reason === 'transport close') {
      client.close()
    }
    isConnected.value = false;
  });

  // Keep if we're going to have a in-game chat function later in sprint 2
  function sendMessage(message) {
    client.emit("SEND", message);
  }

  function createGame(name, createGameState) {
    client.emit("CREATE_GAME", { name, ...createGameState });
  }

  function joinGame(name, roomNo) {
    client.emit("JOIN_GAME", { name, roomNo });
  }

  // send the played card as an index in a string format
  function playCard(cardId) {
    client.emit("PLAYED_CARD", cardId);
  }

  function getGameList(username){
    client.emit("AVAILABLE_GAMES", username);
  }

  function resetGameState() {
    gameState = ref(null);
  }

  function removeGame(){
    client.emit("REMOVE_GAME");
  }

  return { playCard, sendMessage, createGame, joinGame, getGameList, gameList, error, gameState, isConnected, opponentDisconnected, resetGameState, removeGame }
}