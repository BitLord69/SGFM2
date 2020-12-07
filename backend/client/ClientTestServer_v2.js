window.onload = function () {
    var client = io.connect("http://localhost:9092", {
      'reconnection delay': 2000,
      'force new connection': true
    });

    client.on('connect', function () {
      console.log('connected!');
      client.emit('SEND_PLAYER_NAME',  "James Bond" + new Date(), () => {});
    });

    client.on('message', (data) => {
      console.log("data i message: ", data);
    });

    client.on('connect_error', () => {
      console.log("i connect_error");
    });

    client.on('disconnect', () => {
      console.log("disconnected from the server");
    });

    client.emit('send',  {message: "Hej från klienten!", name: "Bond, James Bond"}, () => {
      console.log("meddelande skickat");
    });
}
