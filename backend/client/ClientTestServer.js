// let client = io.connect('http://localhost:9092', {
//              'reconnection delay' : 2000,
//              'force new connection' : true
//            });

import "./node_modules/socket.io-client/dist/socket.io.js";

class Client {
  
  constructor() {
    this.client = io.connect("http://localhost:9092");
    //    client.off('message')
    console.log("hellluuuuuuu");
  }

  setup() {
    console.log("i setup");

    this.client.on("connect", function () {
      console.log("connected to server with id");
    });
    this.client.on("weekendGreeting", (data) => {
      console.log("data: ", data);
    });
    this.client.on("connect_error", () => {
      console.log("i connect_error");
    });
    this.client.on("disconnect", () => {
      console.log("disconnected from the server");
    });
    //console.log(client);
    this.client.emit("greeting", "hejsan", () => {
      console.log("meddelande skickat");
    });
  }
}
export default new Client();
