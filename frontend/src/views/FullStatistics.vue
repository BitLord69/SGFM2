<template>
  <Suspense>
    <template #default>
      <Statistics />
    </template>
    <template #fallback>
      <div>Loading stats...</div>
    </template>
  </Suspense>
</template>

<script>

import Statistics from "@/components/Statistics"
import LeagueHandler from "@/modules/LeagueHandler"
import { onMounted } from 'vue';

export default {
  name: "FullStatistics",
  components: { Statistics },
  setup(){
    const { leagues, getLeagues } = LeagueHandler();
    
    onMounted( async () =>{
      await getLeagues();
    })

    return {
      leagues
    }
  },
  
}
</script>

<style>

</style>