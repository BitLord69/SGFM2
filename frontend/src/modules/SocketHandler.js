import { ref } from "vue";
import SocketIO from "socket.io-client";
const error = ref(null);
const gameState = ref(null);
const isConnected = ref(true);
const gameList = ref(null);

let client = null;
export default function SocketHandler() {
  if (client === null) {
    client = SocketIO("http://localhost:9092", {
      reconnectionDelay: 500,
      reconnection: true,
      timeout: 2000,
      // "force new connection": true,
    });
  }

  client.on("connect", function() {
    isConnected.value = true;
    console.log("Connected to server");
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
    console.log("GAME_UPDATE received", JSON.parse(incomingGameState));
    gameState.value = JSON.parse(incomingGameState);
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

  function getGameList() {
    client.emit("AVAILABLE_GAMES");
  }

  client.on("LIST_GAMES", (data) => {
    console.log("LIST_GAMES", JSON.parse(data));
    gameList.value = JSON.parse(data);
  });

  client.on("connect_error", (e) => {
    console.log("i connect_error:", e);
    error.value = e;
  });

  client.on("reconnect", (attempt) => {
    console.log("reconnected to server.... attempt:", attempt);
  });

  client.on("reconnect_error", (error) => {
    console.log("reconnect_error.... ", error);
  });

  client.on("reconnect_failed", () => {
    console.log("failed to re connect!!! ");
  });

  client.on("disconnect", (reason) => {
    console.log("disconnected from the server, reason:", reason);
    if (reason === "transport close") {
      client.close();
      console.log("");
    }
    isConnected.value = false;
    // client = null;
  });

  return {
    playCard,
    sendMessage,
    createGame,
    joinGame,
    getGameList,
    gameList,
    error,
    gameState,
    isConnected,
  };
}
