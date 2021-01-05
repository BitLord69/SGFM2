<template>
  <div id="leaderboard">
    <div class="leaderboard-content d-flex p-jc-between">
      <div>
        <span># Player Score</span>
      </div>
      <div>
        <span v-for="(user, index) in leaderboard" :key="user.username"
          >{{ index + 1 }}
          {{ user.username }} {{ user.wins - user.losses }}</span
        >
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Leaderboard",

  setup() {
    const leaderboard = [
      {
        username: "tomtis",
        wins: 4,
        losses: 2,
      },
      {
        username: "tomtemor",
        wins: 5,
        losses: 4,
      },
      {
        username: "tomtepojke",
        wins: 3,
        losses: 1,
      },
    ];

    return {
      leaderboard,
    };
  },
};
</script>

<style scoped lang="scss">
#leaderboard {
  background-color: #e2c3a6;
}

.leaderboard-content {
  background-color: #eeeeee;
}
</style>
