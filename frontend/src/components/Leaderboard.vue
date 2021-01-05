<template>
  <div id="leaderboard">
      <div class="p-text-center">Leaderboard</div>
    <div class="leaderboard-content d-flex p-jc-between">
      <div class="info-row p-mb-1">
        <div style="grid-column:1/2; text-decoration:underline">#</div>
        <div style="grid-column:2/3; text-decoration:underline">Player</div>
        <div style="grid-column:3/4; text-align: right; text-decoration:underline">Score</div>
      </div>
      <div :class="'p'+ (index + 1)" v-for="(user, index) in leaderboard" :key="user.username">
        <div style="grid-column:1/2">{{ index + 1 }}</div>
        <div style="grid-column:2/3">{{ user.username }}</div>
        <div style="grid-column:3/4; text-align: right">{{ user.wins - user.losses }}pts</div>
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
      {
        username: "tomtepojke",
        wins: 1000,
        losses: 1,
      }
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
  padding: 1%;
  border: 2px solid #3b1704;
}

.leaderboard-content {
  background-color: #eeeeee;
  display: grid;
  grid-template-rows: repeat(10, 1fr);
  width: 15vw;
  padding: 3%;
  border-radius: 5px;
}

.info-row{ 
  display: grid;
  grid-template-columns: 2vw 9vw 3vw;
  grid-row: 1/2;
}
.p1 {
  display: grid;
  grid-template-columns: 20px 1fr 50px;
  grid-row: 2/3;
}

.p2{
  display: grid;
  grid-template-columns: 20px 1fr 50px;
  grid-row: 3/4;
}
.p3 {
  display: grid;
  grid-template-columns: 20px 1fr 50px;
  grid-row:4/5;
}

.p4{
  display: grid;
  grid-template-columns: 20px 1fr 50px;
  grid-row: 5/6;
}
.p5 {
  display: grid;
  grid-template-columns: 20px 1fr 50px;
  grid-row: 6/7;
}

.p6{
  display: grid;
  grid-template-columns: 20px 1fr 50px;
  grid-row: 7/8;
}
.p7 {
  display: grid;
  grid-template-columns: 20px 1fr 50px;
  grid-row: 8/9;
}

.p8{
  display: grid;
  grid-template-columns: 20px 1fr 50px;
  grid-row: 9/10;
}

.p9 {
  display: grid;
  grid-template-columns: 20px 1fr 50px;
  grid-row: 10/11;
}

.p10{
  display: grid;
  grid-template-columns: 20px 1fr 50px;
  grid-row: 11/12;
}
</style>
