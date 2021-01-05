<template>
  <Chart type="pie" :data="chartData" />
</template>

<script>

export default {
  setup(context, props) {
    const statistics = props.attrs.stats;
    let numberOfLosses = statistics.filter(losses => losses.winner !== true);
    let numberOfWins = statistics.filter(wins => wins.winner === true);

    const chartData = {
      labels: ["Wins", "Losses"],
      datasets: [
        {
          data: [numberOfWins.length, numberOfLosses.length],
          backgroundColor: ["#00917c", "#c70039"],
          hoverBackgroundColor: ["#00917c", "#c70039"],
        },
      ],
    }
    
    return {
      chartData
    };
  },
};
</script>

<style></style>
