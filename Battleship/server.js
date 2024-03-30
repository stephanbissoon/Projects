const express = require('express');
const http = require('http');
const WebSocket = require('ws');
const uuid = require("uuid");
const app = express();
const bin = http.createServer(app);
const wss = new WebSocket.Server({ server: bin });

const clients = [];

wss.on('connection', function connection(ws) {
    console.log('A new client has connected');

    if (wss.clients.size === 3) {
        ws.send(JSON.stringify({ server_message: "MAX_PLAYERS" }));
        ws.terminate();
        console.log("Max players reached. A client was disconnected");
    }

    else {
        ws.info = {
            id: uuid.v4(),
            ready: false,
            ships: [],
            hitShips: []
        };

        console.log("%s was added to the clients list", ws.info.id);
    }

    ws.on('close', function close() {
        let clients = Array.from(wss.clients);

        // when the other player is disconnected, connected player wins
        if (clients.length === 1) {
            clients[0].send(JSON.stringify({
                server_message: "OTHER_PLAYER_DISCONNECTED"
            }));
        }
    });

    ws.on('message', (message) => {
        let parsedMessage = JSON.parse(message);

        if (parsedMessage.client_message === "SET_OPPONENT_SHIPS") {
            addShipsToOpponent(ws, parsedMessage.data);
        }

        if (parsedMessage.client_message === "DROP") {
            checkBombDrop(ws, parsedMessage.data);
        }

        if (parsedMessage.client_message === "SWITCH_TURN") {
            let otherPlayer = getOtherPlayer(ws);
            
            otherPlayer.send(JSON.stringify({
                server_message: "YOUR_TURN"
            }));
        }

        if (parsedMessage.client_message === "NEW_GAME") {
            resetClientInfo();
        }
    });
});

app.use(express.static('public'));

const port = 3000;
bin.listen(port, () => {
    console.log(`Server is running on http://localhost:${port}`);
});

function getOtherPlayer(socket) {
    let serverClients = Array.from(wss.clients);

    return serverClients.find(client => client.info.id !== socket.info.id);
}

function addShipsToOpponent(socket, ships) {
    // wait for other player
    const interval = setInterval(() => {
        socket.info.ready = true;

        if (wss.clients.size === 1) {
            console.log("Player %s is waiting for opponent to join ...", socket.info.id);
            return;
        }

        let otherPlayer = getOtherPlayer(socket);

        otherPlayer.info.ships = ships;

        let clients = Array.from(wss.clients);

        if (clients.every(client => client.info.ready === true)) {
            for (client of clients) client.send(JSON.stringify({ server_message: "READY" }));

            let turn = Math.round(Math.random());

            console.log("%s's turn", clients[turn].info.id);

            clients[turn].send(JSON.stringify({ server_message: "YOUR_TURN" }));
        }

        clearInterval(interval);

    }, 2500);
}

function checkBombDrop(socket, coordinate) {
    let otherPlayer = getOtherPlayer(socket);

    let response = {
        server_message: "",
        data: coordinate
    };

    if (socket.info.ships.some(ship => ship[0] === coordinate[0] && ship[1] === coordinate[1])) {
        response.server_message = "HIT";
        socket.info.hitShips.push(coordinate);
    }

    else {
        response.server_message = "MISS";
    }

    response = JSON.stringify(response);

    socket.send(response);
    otherPlayer.send(response);

    if (socket.info.hitShips.length === otherPlayer.info.ships.length) {
        socket.send(JSON.stringify({
            server_message: "WINNER"
        }));

        otherPlayer.send(JSON.stringify({
            server_message: "LOSER"
        }));
    }
}

function resetClientInfo() {
    for (client of wss.clients) {
        client.info.ready = false;
        client.info.hitShips = []
    }

    console.log("All clients reset");
}