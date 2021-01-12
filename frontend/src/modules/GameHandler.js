import { extFetch } from "./extFetch";
import { reactive, ref } from "vue";

const gameError = ref(null);
const stats = ref(null);
const latestGame = ref(null);

const CreateGameState = reactive({
  cardsOnHand: 5,
  pointsToWin: 15,
  selectedLeague: null,
});
let inGame = ref(false);

const joinGameState = reactive({
  activeIndex: -1,
  selectedGame: null,
  gameList: null,
});

export default function GameHandler() {
  async function getGames() {
    try {
      stats.value = await extFetch("/api/game/", "GET");
    } catch (e) {
      gameError.value = e;
      return;
    }
  }

  async function getLatestGame() {
    try {
      latestGame.value = await extFetch("/api/game/latestgame", "GET");
    } catch (e) {
      gameError.value = e;
      return;
    }
  }

  return {
    getGames,
    gameError,
    stats,
    CreateGameState,
    inGame,
    joinGameState,
    latestGame,
    getLatestGame,
  };
}
