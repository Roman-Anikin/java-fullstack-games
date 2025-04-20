const { createApp } = Vue;

const App = {
    data() {
        return {
            pagePhase: 1,
            selectedGame: null,
            games: [
                { gameId: 'snake', name: 'Snake' },
                { gameId: 'racing', name: 'Racing' }
            ],
            gamesUi: [
                { uiId: 'tui', name: 'Console' },
                { uiId: 'gui', name: 'Desktop' },
                { uiId: 'web', name: 'Browser' }
            ]
        };
    },
    methods: {
        selectGame(gameId) {
            this.selectedGame = gameId;
            this.pagePhase = 2;
            const gameName = this.games.find(o => o.gameId === gameId).name;
            document.getElementById('header').innerHTML = gameName;
        },

        startGame(uiId) {
            if (uiId === 'web') {
                window.location.href = `/game/web?gameType=${this.selectedGame}`;
                return;
            }
            const url = `/game/${this.selectedGame}?mode=${uiId}`;
            fetch(url, { method: 'POST' }).catch(() => {});
        },

        returnToGames() {
            this.pagePhase = 1;
            document.getElementById('header').innerHTML = 'Arcade Games'
        }
    }
};

createApp(App).mount('#app');