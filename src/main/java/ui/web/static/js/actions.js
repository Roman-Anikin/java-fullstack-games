const canvas = document.getElementById('gameCanvas');
const ctx = canvas.getContext('2d');
const CELL_SIZE = 30;
let lastUpdate = performance.now();
let speed = 300;
let loopId = null;
let isRunning = false;
const carImages = {
    black: new Image(),
    grey: new Image(),
    red: new Image()
};
const appleImage = new Image();

window.addEventListener('DOMContentLoaded', () => {
    loadImages();
    drawMessage(null);
});

document.addEventListener('keydown', async (event) => {
    await fetch(`/game/${this.gameType}/action`, {
        method: 'POST',
        body: JSON.stringify({ key: event.key }),
    });
    let gameData = await getGameState();
    await updateView(gameData);
    if (event.key === 'Enter' && gameData.status === 'IN_GAME' && !isRunning) {
        loopId = requestAnimationFrame(gameLoop);
        isRunning = true;
    }
});

function loadImages() {
    for (const name in carImages) {
        carImages[name].src = '/resources/images/' + name + '.png';
    }
    appleImage.src = '/resources/images/apple.png';
};

async function getGameState() {
    const response = await fetch(`/game/${this.gameType}/state`);
    return await response.json();
}

async function gameLoop(timestamp) {
    let gameData = null;
    if (timestamp - lastUpdate >= speed) {
        await fetch(`/game/${this.gameType}/move`, {
            method: 'PATCH'
        });
        gameData = await getGameState();
        lastUpdate = timestamp;
        speed = gameData.speed;
    }
    if (gameData) {
        updateView(gameData);
        if (gameData.status != 'IN_GAME') {
            cancelAnimationFrame(loopId);
            loopId = null;
            isRunning = false;
            return;
        }
    }
    loopId = requestAnimationFrame(gameLoop);
}

async function updateView(gameData) {
    if (!gameData) {
        return;
    }
    if (gameData.status != 'IN_GAME') {
        drawMessage(gameData.status);
        return;
    }
    if (gameType === 'racing') {
        drawRacing(gameData);
    } else if (gameType === 'snake') {
        drawSnake(gameData);
    }
    drawStats(gameData);
}

function clearCanvas(color) {
    ctx.fillStyle = color;
    ctx.fillRect(0, 0, canvas.width, canvas.height);
}

function drawMessage(status) {
    clearCanvas('#FFFFFF');
    let str = '';
    if (status === 'WIN') {
        str = 'You Win!'
    } else if (status === 'LOOSE') {
        str = 'Game Over'
    }
    ctx.fillStyle = '#222';
    ctx.font = '30px Fantasy';
    ctx.textAlign = 'center';
    ctx.fillText(str, canvas.width / 2, canvas.height / 2 - CELL_SIZE);
    ctx.fillText('Press ENTER to start', canvas.width / 2, canvas.height / 2);
}

function drawRacing(gameData) {
    clearCanvas('#CBCBCB');
    drawCar(gameData.playerCar, 'black');
    for (let i = 0; i < gameData.rivalCars.length; i++) {
        drawCar(gameData.rivalCars[i], Object.keys(carImages)[i + 1]);
    }
}

function drawCar(car, color) {
    ctx.drawImage(carImages[color], (car.body[0][1].x - 1) * CELL_SIZE, car.body[0][1].y * CELL_SIZE,
        3 * CELL_SIZE, 5 * CELL_SIZE);
}

function drawSnake(gameData) {
    clearCanvas('#D1E8D1');
    for (let i = 0; i < gameData.snake.length; i++) {
        ctx.fillStyle = i === 0 ? '#FFEF00' : '#252525';
        ctx.beginPath();
        ctx.roundRect(gameData.snake[i].x * CELL_SIZE, gameData.snake[i].y * CELL_SIZE,
            CELL_SIZE, CELL_SIZE, CELL_SIZE / 4);
        ctx.fill();
    }
    drawApple(gameData.apple);
}

function drawApple(apple) {
    ctx.drawImage(appleImage, apple.x * CELL_SIZE, apple.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
}

function drawStats(gameData) {
    document.getElementById('score-value').textContent = gameData.score;
    document.getElementById('highest-value').textContent = gameData.highestScore;
    document.getElementById('level-value').textContent = gameData.level;
}