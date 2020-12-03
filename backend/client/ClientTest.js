const
    io = require("socket.io-client"),
    ioClient = io.connect("http://localhost:9092");

ioClient.on("HELLO", (msg) => console.info(msg));