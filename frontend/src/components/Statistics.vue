<template>
  <Dropdown
    v-model="selectedLeague"
    :options="leagues"
    optionLabel="league"
    placeholder="Select a league"
    style="width: 100%"
    showClear
    scrollHeight="220px"
    @change="filterStats"
    v-if="!props.leagueToDisplay || (props.leagueToDisplay && !props.leagueToDisplay.length)" 
    class="p-mb-2"/>

  <Chart type="pie" :data="chartData" />
</template>

<script>
import { watchEffect, ref } from "vue"
import GameHandler from "@/modules/GameHandler"
import LeagueHandler from "@/modules/LeagueHandler"

export default {
  props: { leagueToDisplay: String },

  async setup(props) {
    const selectedLeague = ref(null);
    const { stats, getGames } = GameHandler();
    const { leagues, getLeagues } = LeagueHandler();

    let numberOfLosses;
    let numberOfWins;
    const chartData = ref(null);

    watchEffect(() => {
      numberOfLosses = stats.value.filter(losses => losses.winner !== true);
      numberOfWins = stats.value.filter(wins => wins.winner === true);
      chartData.value = {
      labels: ["Wins", "Losses"],
      datasets: [
        {
          data: [numberOfWins.length, numberOfLosses.length],
          backgroundColor: ["#00917c", "#c70039"],
          hoverBackgroundColor: ["#00917c", "#c70039"],
        },
      ],
      }
    })
    
    await getLeagues();
    leagues.value.unshift( {league:"No league"});

    async function filterStats() {
      await getGames(selectedLeague?.value?.league);
    }

    return {
      props,
      leagues,
      chartData,
      selectedLeague, 
      filterStats
    };
  },
};
</script>

<style lang="scss">
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
    border: 1px solid darken(#e2c3a6, 10%) !important;
  }

  .p-dropdown-item {
    &:hover {
    background-color: darken(#e2c3a6, 10%) !important;
    }
  }
</style>
