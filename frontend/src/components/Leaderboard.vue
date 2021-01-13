<template>
  <div id="leaderboard" class="p-d-flex p-flex-column p-pb-4 p-px-4"
    :style="!props.leagueToDisplay || (props.leagueToDisplay && !props.leagueToDisplay.length) ? 'border: 2px solid #3b1704;' : ''" >
    <div class="p-text-center" v-if="!props.leagueToDisplay || (props.leagueToDisplay && !props.leagueToDisplay.length)">Leaderboard</div>
      <div>
        <div v-if="!props.leagueToDisplay || (props.leagueToDisplay && !props.leagueToDisplay.length)" >
          <Dropdown
            v-model="selectedLeague"
            :options="leagues"
            optionLabel="league"
            placeholder="Select a league"
            style="width: 100%; min-width: 220px;"
            showClear
            scrollHeight="220px"
            @change="filterLeaderboard"
            class="p-my-2"/>
        </div>
        <div v-else class="p-pt-2 p-mb-2">{{props.leagueToDisplay}}</div>
      </div>
        <router-link 
          to="/leaderboard" 
          tag="button" 
          :style="!props.leagueToDisplay || (props.leagueToDisplay && !props.leagueToDisplay.length) ? '' : 'cursor: default;'" 
          :disabled="!props.leagueToDisplay || (props.leagueToDisplay && !props.leagueToDisplay.length)"
        >
      <div class="leaderboard-content" v-if="leaderboard && leaderboard.length">
        <div class="info-row p-mb-1 p-jc-center">
          <div style="grid-column:1/2; text-decoration:underline">#</div>
          <div style="grid-column:2/3; text-decoration:underline">Player</div>
          <div style="grid-column:3/4; text-align: right; text-decoration:underline">Score</div>
        </div>
        <div :class="'p'+ (index + 1) + ' p-jc-center'" v-for="(user, index) in leaderboard" :key="user.username">
          <div style="grid-column:1/2">{{ index + 1 }}</div>
          <div style="grid-column:2/3">{{ user.username }}</div>
          <div style="grid-column:3/4; text-align: right">{{ user.wins - user.losses }}pts</div>
        </div>
      </div>
      <div v-else>No games played</div>
        </router-link>
  </div>
</template>

<script>
import { ref } from "vue";
import GameHandler from "@/modules/GameHandler"
import LeagueHandler from "@/modules/LeagueHandler"

export default {
  name: "Leaderboard",
  props: {
    leagueToDisplay: String 
  },
  async setup(props) {
    const leaderboard = ref(null);
    const selectedLeague = ref(null);
    const { getLeaderboardPrivately } = GameHandler();
    const { leagues, getLeagues } = LeagueHandler();

    if(props && props.leagueToDisplay) {
      const league = (props.leagueToDisplay === "Total") ? undefined : props.leagueToDisplay;
      leaderboard.value = await getLeaderboardPrivately(league);
    } else {
      await getLeagues();
      leaderboard.value = await getLeaderboardPrivately();
      leagues.value.unshift( {league:"No league"});
    }

    async function filterLeaderboard(){
      leaderboard.value = await getLeaderboardPrivately(selectedLeague?.value?.league);
    }

    return {
      props,
      leagues,
      leaderboard,
      selectedLeague,
      filterLeaderboard,

    };
  },
};
</script>

<style scoped lang="scss">


$col1: 2vw;
$col2: 7vw;
$col3: 2vw;

#leaderboard {
  padding: 1%;
  color: #3b1704;
  min-width: 230px;
  border-radius: 5px;
  background-color: #e2c3a6;
  //border: 2px solid #3b1704;
  a{
    text-decoration: none;
    color: #3b1704;
  }
}

.leaderboard-content {
  padding: 3%;
  width: 100%;
  display: grid;
  min-width: 220px;
  border-radius: 5px;
  background-color: #eeeeee;
  grid-template-rows: repeat(10, 1fr);
}

.info-row{ 
  display: grid;
  grid-row: 1/2;
  grid-template-columns: $col1 $col2 $col3;
}
.p1 {
  display: grid;
  grid-row: 2/3;
  grid-template-columns: $col1 $col2 $col3;
}

.p2{
  display: grid;
  grid-row: 3/4;
  grid-template-columns: $col1 $col2 $col3;
}
.p3 {
  display: grid;
  grid-row: 4/5;
  grid-template-columns: $col1 $col2 $col3;
}

.p4{
  display: grid;
  grid-row: 5/6;
  grid-template-columns: $col1 $col2 $col3;
}
.p5 {
  display: grid;
  grid-row: 6/7;
  grid-template-columns: $col1 $col2 $col3;
}

.p6{
  display: grid;
  grid-row: 7/8;
  grid-template-columns: $col1 $col2 $col3;
}
.p7 {
  display: grid;
  grid-row: 8/9;
  grid-template-columns: $col1 $col2 $col3;
}

.p8{
  display: grid;
  grid-row: 9/10;
  grid-template-columns: $col1 $col2 $col3;
}

.p9 {
  display: grid;
  grid-row: 10/11;
  grid-template-columns: $col1 $col2 $col3;
}

.p10{
  display: grid;
  grid-row: 11/12;
  grid-template-columns: $col1 $col2 $col3;
}
</style>
