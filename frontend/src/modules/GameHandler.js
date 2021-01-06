import { extFetch } from "./extFetch";
import { ref } from "vue";

const error = ref(null);
const stats = ref(null);

export default function GameHandler() {
  async function getGames() {
    try {
      stats.value = await extFetch("/api/game/", "GET");
    } catch (e) {
      error.value = e;
      return
    }  
  }

  return { getGames, error, stats }
}