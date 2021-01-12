import { extFetch } from "./extFetch";
import { reactive, ref } from "vue";

const gameError = ref(null);
const stats = ref(null);
const CreateGameState = reactive({
  cardsOnHand: 5,
  pointsToWin: 15,
  selectedLeague: null
});
const leaderboard = ref(null);
let inGame = ref(false);

const joinGameState = reactive({
  activeIndex: -1,
  selectedGame: null,
  gameList: null
});

export default function GameHandler() {
  async function getGames(league) {
    try {
      stats.value = await extFetch("/api/game/" + league);
    } catch (e) {
      gameError.value = e;
      return
    }  
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

  return { getGames, gameError, stats, CreateGameState, inGame, joinGameState, leaderboard, getLeaderboard }
}