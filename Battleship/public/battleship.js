$(document).ready(function () {
    const userCanvas = $("#userBattleshipCanvas")[0];
    const opponentCanvas = $("#opponentBattleshipCanvas")[0];

    const userContext = userCanvas.getContext('2d');
    const opponentContext = opponentCanvas.getContext('2d');

    const canvasWidthHeight = 500;
    const gridSize = 10;
    const cellSize = canvasWidthHeight / gridSize;

    let opponentShips = [];
    let hits = [];

    let hitAudioPlayable = false;
    let missAudioPlayable = false;

    let playersReady = false;
    let myTurn = false;
    let gamePlayable = false;

    let hitAudio = new Audio("hit.mp3");
    let missAudio = new Audio("miss.mp3");

    let shipImg = new Image();
    shipImg.src = "ship.png";

    const socket = new WebSocket('ws://localhost:3000');
    socket.onmessage = (event) => {
        let message = JSON.parse(event.data);

        console.log("Server message: " + event.data);

        if (message.server_message === "READY") {
            playersReady = true;
        }

        if (message.server_message === "YOUR_TURN") {
            myTurn = true;

            if (gamePlayable) {
                $("#message").text("It's your turn");
            }
        }

        if (message.server_message === "HIT") {
            let context = null;
            let gridX = message.data[0];
            let gridY = message.data[1];

            if (myTurn) {
                context = userContext;
                myTurn = false;

                socket.send(JSON.stringify({
                    client_message: "SWITCH_TURN"
                }));
            }

            else {
                context = opponentContext;
            }

            context.fillStyle = 'red';
            context.fillRect(gridX * cellSize, gridY * cellSize, cellSize, cellSize);

            context.drawImage(shipImg, gridX * cellSize, gridY * cellSize, 50, 50);

            resetAudio();

            if (hitAudioPlayable) {
                hitAudio.play();
            }
        }

        if (message.server_message === "MISS") {
            let context = null;
            let gridX = message.data[0];
            let gridY = message.data[1];

            if (myTurn) {
                context = userContext;
                myTurn = false;

                socket.send(JSON.stringify({
                    client_message: "SWITCH_TURN"
                }));
            }

            else {
                context = opponentContext;
            }

            context.fillStyle = 'blue';
            context.fillRect(gridX * cellSize, gridY * cellSize, cellSize, cellSize);

            resetAudio();

            if (missAudioPlayable) {
                missAudio.play();
            }
        }

        if (message.server_message === "WINNER") {
            gamePlayable = false;
            $("#message").text("You are the winner!");
            $("#new-game-btn").show();
        }

        if (message.server_message === "LOSER") {
            gamePlayable = false;
            $("#message").text("Your opponent is the winner. Better luck next time!");
            $("#new-game-btn").show();
        }

        if (message.server_message === "OTHER_PLAYER_DISCONNECTED") {
            gamePlayable = false;
            $("#message").text("The other player has disconnected");
            $("#drop-ships-btn").hide();
            $("#start-game-btn").hide();
            $("#new-game-btn").show();
        }

        if (message.server_message === "MAX_PLAYERS") {
            $("p").text("");
            $("#canvas-container").hide();
            $("#drop-ships-btn").hide();
            $("#message").text("The maximum amount of players has been reached for this game. Please try again later.");
        }
    }

    function resetAudio() {
        if (hitAudioPlayable) {
            hitAudio.pause();
            hitAudio.currentTime = 0;
        }

        if (missAudioPlayable) {
            missAudio.pause();
            missAudio.currentTime = 0;
        }
    }

    function drawUserBoard() {
        userContext.clearRect(0, 0, canvasWidthHeight, canvasWidthHeight);

        for (let i = 0; i < gridSize; i++) {
            for (let j = 0; j < gridSize; j++) {
                userContext.strokeRect(i * cellSize, j * cellSize, cellSize, cellSize);
            }
        }
    }

    function drawOpponentBoard() {
        opponentContext.clearRect(0, 0, canvasWidthHeight, canvasWidthHeight);

        for (let i = 0; i < gridSize; i++) {
            for (let j = 0; j < gridSize; j++) {
                opponentContext.strokeRect(i * cellSize, j * cellSize, cellSize, cellSize);
            }
        }
    }

    function fillOpponentShips() {
        while (opponentShips.length < 10) {
            let shipX = Math.floor(Math.random() * 10);
            let shipY = Math.floor(Math.random() * 10);

            if (!opponentShips.some(ship => ship[0] === shipX && ship[1] === shipY)) {
                opponentShips.push([shipX, shipY]);

                opponentContext.drawImage(shipImg, shipX * cellSize, shipY * cellSize, 50, 50);
            }
        }
    }

    function clearMessage() {
        $("#message").text("");
    }

    $("#userBattleshipCanvas").click((event) => {
        const rect = userCanvas.getBoundingClientRect();
        const x = event.clientX - rect.left;
        const y = event.clientY - rect.top;
        const gridX = Math.floor(x / cellSize); // Box on X axis
        const gridY = Math.floor(y / cellSize); // Box on Y axis

        clearMessage();

        if (!gamePlayable) {
            $("#message").text("The game is over!");
            return;
        }

        if (!myTurn) {
            $("#message").text("The opponent has not finished their turn");
            return;
        }

        if (hits.some(hit => hit[0] === gridX && hit[1] === gridY)) {
            $("#message").text("You already bombed that box");
            return;
        }

        hits.push([gridX, gridY]);

        socket.send(JSON.stringify({
            client_message: "DROP",
            data: [gridX, gridY]
        }));
    });

    $("#ship-img").on("dragstart", (event) => {
        clearMessage();
    });

    $("#ship-img").on("dragend", (event) => {
        const rect = opponentCanvas.getBoundingClientRect();
        const x = event.clientX - rect.left;
        const y = event.clientY - rect.top;
        const gridX = Math.floor(x / cellSize); // Box on X axis
        const gridY = Math.floor(y / cellSize); // Box on Y axis

        if (opponentShips.length === 10) {
            $("#message").text("Maximum ships dropped");
            return;
        }

        if ((gridX < 0 || gridX > 9) || (gridY < 0 || gridY > 9)) {
            $("#message").text("Ship dropped out of bounds");
            return;
        }

        if (opponentShips.some(ship => ship[0] === gridX && ship[1] === gridY)) {
            $("#message").text("Ship already dropped here");
            return;
        }

        opponentShips.push([gridX, gridY]);

        opponentContext.drawImage(shipImg, gridX * cellSize, gridY * cellSize, 50, 50);
    });

    function initGame() {
        opponentShips = [];
        hits = [];

        playersReady = false;
        myTurn = false;

        $("p").text("Drop a maximum of 10 ships on the opponent's board. If you drop less than 10 ships, the remaining will be randomly generated.");
        $("#ship-img-div").show();
        $("#userBattleshipCanvas").hide();
        $("#new-game-btn").hide();
        $("#drop-ships-btn").show();

        clearMessage();
        drawOpponentBoard();
    }

    function waitForOpponent() {
        $("#drop-ships-btn").prop("disabled", true);
        $("#message").text("Waiting for opponent ...");

        const interval = setInterval(() => {
            if (!playersReady) {
                $("#message").text("Waiting for opponent ...");
                return;
            }

            $("#drop-ships-btn").hide();
            $("#drop-ships-btn").prop("disabled", false);
            $("#start-game-btn").show();

            $("#message").text("Let's play!");

            clearInterval(interval);
        }, 1500);
    }

    function startGame() {
        clearMessage();
        drawUserBoard();

        $("p").text("BOMBS AWAY!!");
        $("#ship-img-div").hide();
        $("#userBattleshipCanvas").show();

        if (myTurn) {
            $("#message").text("It's your turn");
        }

        gamePlayable = true;
    }

    hitAudio.addEventListener("canplaythrough", () => {
        hitAudioPlayable = true;
    });

    missAudio.addEventListener("canplaythrough", () => {
        missAudioPlayable = true;
    });

    $("#drop-ships-btn").click(() => {
        fillOpponentShips();

        socket.send(JSON.stringify({
            client_message: "SET_OPPONENT_SHIPS",
            data: opponentShips
        }));

        waitForOpponent();
    });

    $("#new-game-btn").click(() => {
        socket.send(JSON.stringify({
            client_message: "NEW_GAME"
        }));

        initGame();
        $("#new-game-btn").hide();
    });

    $("#start-game-btn").click(() => {
        startGame();
        $("#start-game-btn").hide();
    });

    initGame();
});