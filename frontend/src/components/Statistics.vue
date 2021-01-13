<template>
  <div :class="'p-d-flex p-flex-column ' + (props.bottomBorder ? 'bordered' : '')">
    <div>
      <div v-if="!props.leagueToDisplay || (props.leagueToDisplay && !props.leagueToDisplay.length)" >
        <Dropdown
          v-model="selectedLeague"
          :options="leagues"
          optionLabel="league"
          placeholder="Select a league"
          style="width: 100%"
          showClear
          scrollHeight="220px"
          @change="filterStats"
          class="p-mb-2"/>
      </div>
      <div v-else class="p-pt-2">{{props.leagueToDisplay}}</div>
    </div>

    <div class="p-my-auto p-js-center">
      <div v-if="stats && stats.length">
        <Chart class="p-mb-2" type="pie" :data="chartData" />
      </div>
      <div v-else>
        No stats available!
      </div>
    </div>
  </div>
</template>

<script>
import { watchEffect, ref } from "vue"
import GameHandler from "@/modules/GameHandler"
import LeagueHandler from "@/modules/LeagueHandler"

export default {
  props: { 
    leagueToDisplay: String,
    bottomBorder: { type: Boolean, default: (false) },
  },

  async setup(props) {
    const stats = ref(null);
    const selectedLeague = ref(null);
    const { getGamesPrivately } = GameHandler();
    const { leagues, getLeagues } = LeagueHandler();

    const chartData = ref(null);

    watchEffect(() => {
      if (!stats.value) return;

      let numberOfLosses = 0;
      let numberOfWins = 0;
      let numberOfTies = 0;

      stats.value.forEach(game => {
        if (game.winner == 2) {
          numberOfTies++;
          return
        }
        if (isWinner(game)) {
          numberOfWins++;
        } else {
          numberOfLosses++;
        }
      });

      chartData.value = {
        labels: ["Wins " + numberOfWins, "Losses " + numberOfLosses, "Ties " + numberOfTies],
        datasets: [
          {
            data: [numberOfWins, numberOfLosses, numberOfTies],
            backgroundColor: ["#00917c", "#c70039", "grey"],
            hoverBackgroundColor: ["#00917c", "#c70039", "grey"],
          },
        ],
      }
    })
    
    function isWinner(game) {
      return (game.isStartPlayer && game.winner == 0) || (!game.isStartPlayer && game.winner == 1);
    }

    if(props && props.leagueToDisplay) {
      const league = (props.leagueToDisplay === "Total") ? undefined : props.leagueToDisplay;
      stats.value = await getGamesPrivately(league);
    } else {
      await getLeagues();
      stats.value = await getGamesPrivately();
      leagues.value.unshift( {league:"No league"});
    }

    async function filterStats() {
      stats.value = await getGamesPrivately(selectedLeague?.value?.league);
    }

    return {
      props,
      leagues,
      chartData,
      selectedLeague, 
      filterStats,
      stats
    };
  },
};
</script>

<style lang="scss" scoped>
  .wrapper {
    min-height: 180px;
  }

  .bordered {
    border-bottom: 1px solid darken(#e2c3a6, 5%);
  }

  .p-dropdown {
    border-color: black !important;
    background-color: #e2c3a6 !important;

    &:not(.p-disabled).p-focus {
      box-shadow: 0 0 0 0.2rem darken(#e2c3a6, 10%) !important;
    }

    .p-highlight {
      background-color: darken(#e2c3a6, 10%) !important;
    }
  }

  .p-dropdown-items-wrapper {
    background-color: #e2c3a6 !important;
  }

  .p-dropdown-panel {
    background-color: #e2c3a6 !important;
    border: 1px solid darken(#e2c3a6, 10%) !important;
  }

  .p-dropdown-item {
    &:hover {
    background-color: darken(#e2c3a6, 10%) !important;
    }
  }
</style>
