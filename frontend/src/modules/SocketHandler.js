import { ref } from "vue";
import SocketIO from 'socket.io-client'
const error = ref(null);
const gameState = ref(null);
const isConnected = ref(true);
const gameList = ref(null);
const roomNo = ref(null);

export default function SocketHandler() {
  let client = SocketIO("http://localhost:9092", {
    "reconnectionDelay": 500,
    "reconnection" : true,
    "timeout" : 2000,
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
    // getGameList()
    console.log("Connected to server")
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
    console.log("CREATE_GAME innan meddelande till server");
    client.emit("CREATE_GAME", { name, pointsToWin, cardsOnHand });
    console.log("CREATE_GAME efter meddelande till server");
  }

  function joinGame(name, inRoomNo) {
    roomNo.value = inRoomNo
    console.log("inroomno i sockethandler joingame: ", inRoomNo);
    client.emit("JOIN_GAME", { name, inRoomNo });
  }

  // send the played card as an index in a string format
  function playCard(cardId) {
    console.log("playCard: ", cardId, " gs: ", gameState.value);
    client.emit("PLAYED_CARD", cardId);
  }

  function getGameList(){
    console.log("anropar getGameList");
    client.emit("AVAILABLE_GAMES");
  }

  client.on('SEND_ROOMNO' , () => {
    // console.log("inRoomNo i sendroomno", inRoomNo);
    // roomNo.value = inRoomNo;
  })

  client.on("LIST_GAMES", (data) => {
    console.log("LIST_GAMES", JSON.parse(data));
    gameList.value = JSON.parse(data);
  })

  client.on("connect_error", (e) => {
    console.log("i connect_error:", e);
    error.value = e;
  });

  client.on("error", (e) => {
    console.log("i error:", e);
    error.value = e;
  });

  client.on('reconnect', () => {
    console.log("i reconnect");
    // if (roomNo.value == null) {
    //   getGameList()
    // }
    // else {
    // client.emit('REQUEST_UPDATE', roomNo.value);
    // console.log("reconnected.., roomNo: ", roomNo.value);
    // }
  })

  client.on("disconnect", () => {
    console.log("disconnected from the server");
    isConnected.value  = false;
    // client = null;
  });

  return { playCard, sendMessage, createGame, joinGame, getGameList, gameList, error, gameState, isConnected }
}