import { ref } from "vue";
import { extFetch } from "./extFetch";

const statsError = ref(null);
const leaderboard = ref(null);

export default function StatsHandler() {
  async function getLeaderboard() {
    let result;

    try {
      result = await extFetch("/api/game/leaderboard/"); 
      if (result.error) {
        statsError.value = result.error;
        return
      }
      leaderboard.value = result;
    } catch (e) {
      statsError.value = e
      return 
    }
  }


  return { leaderboard, statsError, getLeaderboard };
}
