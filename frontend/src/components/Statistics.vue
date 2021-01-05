<template>
  <Chart type="pie" :data="chartData" />
</template>

<script>

export default {
  /* props: ["stats"],
  data() {
    return { stat: this.stats}
  }, */
  setup(context, props) {
    const statistics = props.attrs.stats;
    console.log("in statistics setup: statistics", statistics);
    let numberOfLosses = statistics.filter(losses => losses.winner !== true);
    console.log("losses:", numberOfLosses);
    let numberOfWins = statistics.filter(wins => wins.winner === true);
    console.log("wins:", numberOfWins);
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
    
    /* function numberOfWins(){
      return props.stats.filter(wins => wins.winner === true);
    }

    function totalGames(){
      return props.stats.size;
    }

    numberOfWins();
    totalGames(); */
    
    return {
      chartData
    };
  },
};
</script>

<style></style>
