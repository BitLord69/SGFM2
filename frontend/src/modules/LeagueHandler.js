import { extFetch } from "./extFetch";
import { ref } from "vue";

const leagueError = ref(null);
const leagues = ref(null);

export default function LeagueHandler() {
  async function getLeagues() {
    try {
      leagues.value = await extFetch("/api/league/");
    } catch (e) {
      leagueError.value = e;
      return
    }  
  }

  return { getLeagues, leagueError, leagues }
}