<template>
  <Chart type="pie" :data="chartData" />
</template>

<script>
import { watchEffect, ref } from "vue"
import GameHandler from "@/modules/GameHandler"

export default {
  setup() {
    const {stats} = GameHandler();
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
    
    return {
      chartData
    };
  },
};
</script>

<style></style>
