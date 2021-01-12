<template>
  <div id="latestGame">
    <div class="p-text-center">Your latest played game</div>
    <div class="leaderboard-content d-flex p-jc-between">
      <div class="p-text-center" v-if="latestGame[0].winner == 0">You won against</div>
      <div class="p-text-center" v-if="latestGame[0].winner == 1">You lost against</div>
      <div class="p-text-center" v-if="latestGame[0].winner == 2">You tied against</div>
      <div class="p-text-center">{{latestGame[0].opponent}}</div>
    </div>
  </div>
</template>
<script>
import GameHandler from "../modules/GameHandler";

export default {

  async setup() {

  const { getLatestGame, latestGame } = GameHandler();
  await getLatestGame();
  console.log("latestGame:", );

  return {
    latestGame,
  }
  }
};
</script>

<style>
#latestGame {
  color: #3b1704;
  background-color: #e2c3a6;
  padding: 1%;
  border: 2px solid #3b1704;
}

.leaderboard-content {
  background-color: #eeeeee;
  display: grid;
  grid-template-rows: repeat(1, 1fr);
  width: 15vw;
  padding: 3%;
  border-radius: 5px;
}
</style>
