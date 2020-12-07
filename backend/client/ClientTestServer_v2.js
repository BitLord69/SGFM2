window.onload = function () {
  var client = io.connect("http://localhost:9092", {
    "reconnection delay": 2000,
    "force new connection": true,
  });

  client.on("connect", function () {
    console.log("connected!");
    client.emit("SEND_PLAYER_NAME", "James Bond" + new Date(), () => {});
  });

  client.on("message", (data) => {
    console.log("data i message: ", data);
  });

  function sendMessage(message) {
    client.emit("send", message);
  }

  client.on("GAME_UPDATE", (gameState) => {
    console.log("GAME_UPDATE received");
    console.log("gameState: ", JSON.parse(gameState));
  });

  // send the played card as an index in a string format
  function playedCard() {
    client.emit("PLAYED_CARD", "0");
  }

  client.on("connect_error", () => {
    console.log("i connect_error");
  });

  client.on("disconnect", () => {
    console.log("disconnected from the server");
  });
};
