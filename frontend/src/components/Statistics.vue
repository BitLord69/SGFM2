<template>
<Chart type="pie" :data="chartData" />
 </template>

<script>
export default {
  setup() {
    return {
      chartData: {
        labels: ["Wins", "Losses"],
        datasets: [
          {
            data: [300, 50],
            backgroundColor: ["#00917c", "#c70039"],
            hoverBackgroundColor: ["#00917c", "#c70039"],
          },
        ],
      },
    };
  },
};
</script>

<style></style>
