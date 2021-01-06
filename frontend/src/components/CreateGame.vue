<template>
  <h4 class="p-mt-0">Select league</h4>
  <Dropdown
    v-model="CreateGameState.selectedLeague"
    :options="leagues"
    optionLabel="league"
    placeholder="Select a league"
    style="width: 100%"
  />
  <h3 class="p-text-center"><em>Or</em></h3>
  <h4 class="p-mt-0">
    Cards on hand (3 - 8): {{ CreateGameState.cardsOnHand }}
  </h4>
  <Slider :disabled="CreateGameState.selectedLeague !== null" :min="3" :max="8" v-model="CreateGameState.cardsOnHand" />
  <h4>Points to win (10 - 20): {{ CreateGameState.pointsToWin }}</h4>
  <Slider :disabled="CreateGameState.selectedLeague !== null" :min="10" :max="20" v-model="CreateGameState.pointsToWin" />
</template>

<script>
// import { reactive } from "vue";
import LeagueHandler from "@/modules/LeagueHandler";
import GameHandler from "@/modules/GameHandler";


export default {
  name: "CreateGame",

  async setup() {
    const { getLeagues, leagueError, leagues } = LeagueHandler();
    const { CreateGameState } = GameHandler();

    await getLeagues();

    return {
      CreateGameState,
      leagueError,
      leagues,
    };
  },
};
</script>
