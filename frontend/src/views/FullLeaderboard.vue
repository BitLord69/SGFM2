<template>
  <div class="page p-text-center p-d-flex p-flex-column" v-if="isLoaded">
    <div>
      <h2>Leaderboard</h2>
    </div>
    <div class="p-my-auto p-mx-0 p-px-0">
      <Suspense>
        <template #default>
          <div class="p-grid p-col-12 p-jc-center p-nogutter">
            <div class="p-col-10 p-lg-5 p-xl-3 stat-container p-my-2 p-mx-2" v-for="(l, index) in leagues" :key="index">
              <Leaderboard :leagueToDisplay="l.league" />
            </div>
          </div>
        </template>
        <template #fallback>
          <div>
            <ProgressBar mode="indeterminate" />
          </div>
        </template>
      </Suspense>
    </div>
  </div>
</template>

<script>
import Leaderboard from "@/components/Leaderboard"
import LeagueHandler from "@/modules/LeagueHandler"
import { onMounted, ref } from 'vue';

export default {
  name: "FullLeaderboard",
  components: { Leaderboard },
  setup(){
    let leagues = ref(null);
    const isLoaded = ref(false);
    const { getLeaguesPrivately } = LeagueHandler();

    onMounted(async () => {
      leagues.value = await getLeaguesPrivately();
      leagues.value.unshift( { league: "No league" });
      leagues.value.unshift( { league: "Total" });
      isLoaded.value = true;
    })

    return {
      leagues,
      isLoaded,
    }
  },
}
</script>

<style lang="scss" scoped>
.page {
  font-size: 18px;
  color:#3b1704;
  font-weight: bold;
  letter-spacing: 2px;
  font-family: "Yanone Kaffeesatz", sans-serif;

  h2 {
    margin-top: 0;
    color: #e2c3a6;
    word-spacing: -0.5em;
    text-shadow: 2px 2px black;
    font-family: "Press Start 2P", cursive;
  }
}

.stat-container {
  border-radius: 10px;
  background-color: #e2c3a6;
  border: 2px solid #3b1704;
}
</style>