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
const leaderboard = ref(null);

const joinGameState = reactive({
  activeIndex: -1,
  selectedGame: null,
  gameList: null,
});

export default function GameHandler() {
  async function getGames(league) {
    try {
      stats.value = await extFetch("/api/game/" + league);
    } catch (e) {
      gameError.value = e;
      return;
    }
  }

  async function getLatestGame() {
    try {
      latestGame.value = await extFetch("/api/game/latestgame/");
    } catch (e) {
      gameError.value = e;
    }
  }
 
  async function getGamesPrivately(league) {
    let res;
    try {
      res = await extFetch("/api/game/" + league);
    } catch (e) {
      gameError.value = e;
      return
    }

    return res;
  }

  async function getLeaderboard(league) {
    let result;

    try {
      result = await extFetch("/api/game/leaderboard/" + league);
      if (result.error) {
        gameError.value = result.error;
        return
      }
      leaderboard.value = result;
    } catch (e) {
      gameError.value = e
      return 
    }
  }

  async function getLeaderboardPrivately(league) {
    let result;

    try {
      result = await extFetch("/api/game/leaderboard/" + league);
      if (result.error) {
        gameError.value = result.error;
        return;
      }
      leaderboard.value = result;
    } catch (e) {
      gameError.value = e;
      return;
    }
    return result;
  }

  return {
    stats,
    inGame,
    gameError,
    latestGame,
    joinGameState,
    CreateGameState,
    getGames,
    getLeaderboard,
    getGamesPrivately,
    getLatestGame,
    getLeaderboardPrivately,
  };
}
