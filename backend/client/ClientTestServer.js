// let client = io.connect('http://localhost:9092', {
//              'reconnection delay' : 2000,
//              'force new connection' : true
//            });

import "./node_modules/socket.io-client/dist/socket.io.js";

class Client {
  
  constructor() {
    this.client = io.connect("http://localhost:9092", {
      'reconnection delay': 2000,
      'force new connection': true
    });
    //    client.off('message')
    console.log("hellluuuuuuu i konstruktorn");
    setTimeout( () => console.log("väntar litta..."), 1000 )
  }

  setup() {
    console.log("i setup");

    this.client.on('connect', function () {
      console.log("connected to server");
    });

    this.client.on('message', (data) => {
      console.log("data i message: ", data);
    });

    this.client.on('connect_error', () => {
      console.log("i connect_error");
    });

    this.client.on('disconnect', () => {
      console.log("disconnected from the server");
    });
    //console.log(client);
    this.client.emit('message',  {message: "Hej från klienten!", name: "Bond, James Bond"}, () => {
      console.log("meddelande skickat");
    });
  }
}
export default new Client();
