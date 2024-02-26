$(document).ready(function() {
    const userCanvas = $("#userBattleshipCanvas")[0];
    const pcCanvas = $("#pcBattleshipCanvas")[0];

    const userContext = userCanvas.getContext('2d');
    const pcContext = pcCanvas.getContext('2d');

    const canvasWidthHeight = 500;
    const gridSize = 10;
    const cellSize = canvasWidthHeight / gridSize;
    
    let userShips = [];
    let userHits = [];
    let userHitShips = [];

    let pcShips = [];
    let pcHits = [];
    let pcHitShips = [];

    let hitAudioPlayable = false;
    let missAudioPlayable = false;
    let pcTurn = false;
    let gamePlayable = false;

    let hitAudio = new Audio("hit.mp3");
    let missAudio = new Audio("miss.mp3");

    let shipImg = new Image();
    shipImg.src = "ship.png";

    function resetAudio() {
        if(hitAudioPlayable) {
            hitAudio.pause();
            hitAudio.currentTime = 0;
        }

        if(missAudioPlayable) {
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

        while(userShips.length < 10) {
            let shipX = Math.floor(Math.random() * 10);
            let shipY = Math.floor(Math.random() * 10);

            if(!userShips.some(ship => ship[0] === shipX && ship[1] === shipY)) {
                userShips.push([shipX, shipY]);
            }
        }
    }

    function drawPcBoard() {
        pcContext.clearRect(0, 0, canvasWidthHeight, canvasWidthHeight);

        for (let i = 0; i < gridSize; i++) {
            for (let j = 0; j < gridSize; j++) {
                pcContext.strokeRect(i * cellSize, j * cellSize, cellSize, cellSize);
            }
        }
    }

    function fillPcShips() {
        while(pcShips.length < 10) {
            let shipX = Math.floor(Math.random() * 10);
            let shipY = Math.floor(Math.random() * 10);

            if(!pcShips.some(ship => ship[0] === shipX && ship[1] === shipY)) {
                pcShips.push([shipX, shipY]);

                pcContext.drawImage(shipImg, shipX * cellSize, shipY * cellSize, 50, 50);
            }
        }
    }

    function pcBomb() {
        let bombX = Math.floor(Math.random() * 10);
        let bombY = Math.floor(Math.random() * 10);

        while(true) {
            bombX = Math.floor(Math.random() * 10);
            bombY = Math.floor(Math.random() * 10);
            
            if(pcHits.some(hit => hit[0] === bombX && hit[1] === bombY)) {
                continue;
            }

            if(pcShips.some(ship => ship[0] === bombX && ship[1] === bombY)) {
                pcContext.fillStyle = 'red';
                pcContext.fillRect(bombX * cellSize, bombY * cellSize, cellSize, cellSize);

                pcContext.drawImage(shipImg, bombX * cellSize, bombY * cellSize, 50, 50);

                resetAudio();

                if(hitAudioPlayable) {
                    hitAudio.play();
                }

                pcHitShips.push([bombX, bombY]);
                pcHits.push([bombX, bombY]);
            }
            
            else {
                pcContext.fillStyle = 'blue';
                pcContext.fillRect(bombX * cellSize, bombY * cellSize, cellSize, cellSize);

                resetAudio();

                if(missAudioPlayable) {
                    missAudio.play();
                }

                pcHits.push([bombX, bombY]);
            }

            break;
        }

        if(pcShips.length === pcHitShips.length) {
            gamePlayable = false;
            $("#message").text("The PC is the winner!");
            $("#new-game-btn").show();
        }

        pcTurn = false;
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

        if(!gamePlayable) {
            $("#message").text("The game is over!");
            return;
        }

        if(pcTurn) {
            $("#message").text("The opponent has not finished their turn");
            return;
        }

        if(userHits.some(hit => hit[0] === gridX && hit[1] === gridY)) { // Check if this box has been already hit)
            $("#message").text("You already bombed that box");
            return;
        }
                
        if(userShips.some(ship => ship[0] === gridX && ship[1] === gridY)) {
            userContext.fillStyle = 'red';
            userContext.fillRect(gridX * cellSize, gridY * cellSize, cellSize, cellSize);

            userContext.drawImage(shipImg, gridX * cellSize, gridY * cellSize, 50, 50);

            resetAudio();

            if(hitAudioPlayable) {
                hitAudio.play();
            }

            userHitShips.push([gridX, gridY]);
            userHits.push([gridX, gridY]);
        }
        
        else {
            userContext.fillStyle = 'blue';
            userContext.fillRect(gridX * cellSize, gridY * cellSize, cellSize, cellSize);

            resetAudio();

            if(missAudioPlayable) {
                missAudio.play();
            }

            userHits.push([gridX, gridY]);
        }

        if(userHitShips.length === userShips.length) {
            gamePlayable = false;
            $("#message").text("The user is the winner!");
            $("#new-game-btn").show();
        }

        else{
            pcTurn = true;
            setTimeout(pcBomb, 1200);
        }
    });

    $("#ship-img").on("dragstart", (event) => {
        clearMessage();
    });

    $("#ship-img").on("dragend", (event) => {
        const rect = pcCanvas.getBoundingClientRect();
        const x = event.clientX - rect.left;
        const y = event.clientY - rect.top;
        const gridX = Math.floor(x / cellSize); // Box on X axis
        const gridY = Math.floor(y / cellSize); // Box on Y axis

        if(pcShips.length === 10) {
            $("#message").text("Maximum ships dropped");
            return;
        }

        if((gridX < 0 || gridX > 9) || (gridY < 0 || gridY > 9)) {
            $("#message").text("Ship dropped out of bounds");
            return;
        }

        if(pcShips.some(ship => ship[0] === gridX && ship[1] === gridY)) {
            $("#message").text("Ship already dropped here");
            return;
        }

        pcShips.push([gridX, gridY]);

        pcContext.drawImage(shipImg, gridX * cellSize, gridY * cellSize, 50, 50);
    });

    function initGame() {
        userShips = [];
        userHits = [];
        userHitShips = [];
        pcShips = [];
        pcHits = [];
        pcHitShips = [];
        
        $("p").text("Drop a maximum of 10 ships on the opponent's board. If you drop less than 10 ships, the remaining will be randomly generated.");
        $("#ship-img-div").show();
        $("#userBattleshipCanvas").hide();
        $("#new-game-btn").hide();
        $("#start-game-btn").show();

        clearMessage();
        drawPcBoard();
    }

    function startGame() {
        drawUserBoard();
        fillPcShips();
        clearMessage();

        $("p").text("BOMBS AWAY!!");
        $("#ship-img-div").hide();
        $("#userBattleshipCanvas").show();
    
        gamePlayable = true;
    }

    hitAudio.addEventListener("canplaythrough", () => {
        hitAudioPlayable = true;
    });

    missAudio.addEventListener("canplaythrough", () => {
        missAudioPlayable = true;
    });

    $("#new-game-btn").click(() => {
        initGame();
        $("#new-game-btn").hide();
    });

    $("#start-game-btn").click(() => {
        startGame();
        $("#start-game-btn").hide();
    });

    initGame();
});