import { extFetch } from "./extFetch";
import { reactive, ref } from "vue";

const gameError = ref(null);
const stats = ref(null);
const CreateGameState = reactive({
  cardsOnHand: 5,
  pointsToWin: 15,
  selectedLeague: null
});
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

  return { getGames, gameError, stats, CreateGameState, inGame, joinGameState }
}