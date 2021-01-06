import { extFetch } from "./extFetch";
import { reactive, ref } from "vue";

const error = ref(null);
const stats = ref(null);
const CreateGameState = reactive({
  cardsOnHand: 5,
  pointsToWin: 15,
  selectedLeague: null
});

export default function GameHandler() {
  async function getGames() {
    try {
      stats.value = await extFetch("/api/game/", "GET");
    } catch (e) {
      error.value = e;
      return
    }  
  }

  return { getGames, error, stats, CreateGameState }
}