import { ref, reactive } from "vue";
import SocketIO from 'socket.io-client'
import { v4 as uuid } from 'uuid';

const error = ref(null);
let gameState = ref(null);
const isConnected = ref(true);
const gameList = ref(null);
const opponentDisconnected = ref(false);
const rematchState = reactive({
  message: "",
  rematchDenied: false,
  rematchRequested: false,
});

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

  client.on("REMATCH_DENIED", () => {
    rematchState.rematchDenied = true;
  });

   client.on("REMATCH_STARTED", () => {
     resetMatchState();
  });

  // Keep if we're going to have a in-game chat function later in sprint 2
  function sendMessage(message) {
    client.emit("SEND", message);
  }

  function createGame(name, avatarId, createGameState) {
    resetMatchState();
    client.emit("CREATE_GAME", { name, avatarId, ...createGameState });
  }

  function joinGame(name, avatarId, roomNo) {
    resetMatchState();
    client.emit("JOIN_GAME", { name, avatarId, roomNo });
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

  function resetMatchState() {
    rematchState.message = '';
    rematchState.rematchDenied = false;
    rematchState.rematchRequested = false;
  }

  function removeGame(){
    client.emit("REMOVE_GAME");
  }

  function rematch(name, avatarId) {
    rematchState.rematchRequested = true;
    client.emit("REQUEST_REMATCH", { name, avatarId });
  }

  function denyRematch() {
    client.emit("DENY_REMATCH");
  }

  return {
    error,
    gameList,
    gameState,
    isConnected,
    rematchState,
    opponentDisconnected,
    rematch,
    playCard,
    joinGame,
    createGame,
    removeGame,
    denyRematch,
    sendMessage,
    getGameList,
    resetGameState,
    resetMatchState,
  }
}