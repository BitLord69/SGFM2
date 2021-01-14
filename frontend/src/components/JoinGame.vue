<template>
<div :key="componentKey">
  <Dropdown
    v-model="selectedLeague"
    :options="leagueFilter"
    optionLabel="league"
    placeholder="Filter by league"
    style="width: 100%"
    showClear
    scrollHeight="200px"
  >
    <template #value="slotProps">
      <div class="league-item league-item-value" v-if="slotProps.value">
        <div class="p-d-flex p-jc-between"><span>{{slotProps.value.league}}</span>
          <span>Games: {{gameList && gameList.reduce((acc, cur) => {
            if(cur.league === slotProps.value.league){
              acc += 1;
            }
            return acc
          }, 0)}}</span>
        </div>
      </div>
      <div class="p-d-flex p-jc-between" v-else>
          <span>{{slotProps.placeholder}}</span><span>Available games: {{gameList && gameList.length}}</span>
      </div>  
    </template>
    <template #option="slotProps">
      <div class="league-item">
        <div class="p-d-flex p-jc-between"><span>{{slotProps.option.league}}</span>
          <span>Games: {{gameList && gameList.reduce((acc, cur) => {
            if(cur.league === slotProps.option.league){
              acc += 1;
            }
            return acc
          }, 0)}}</span>
        </div>
      </div>
    </template>
  </Dropdown>

  <ScrollPanel style="width: 420px; height: 350px" class="p-scrollpanel-bar-y p-mt-2" >
    <div  v-if="joinGameState && filterGames.length > 0">
      <div :class="'gamelist-item' + ' item-'+index" v-for="(game, index) in filterGames" :key="game.roomNo" @click="setActiveIndex(index)">
        <div class="p-d-flex p-jc-between">
          <h4 class="p-my-1 p-ml-1">{{ game.cgm.name }}'s game</h4>
          <h4 class="p-my-1 p-mr-1">League: {{ game?.cgm?.league || "None" }}</h4>
        </div>
        <h5 class="p-d-flex p-jc-evenly"><span>&#8226; Cards on hand: {{game.cgm.cardsOnHand}}</span> <span>&#8226; Points to win: {{game.cgm.pointsToWin}}</span></h5>
      </div>
    </div>
    <div v-else>
      No current games to join, please hang tight...
    </div>
  </ScrollPanel>
</div>
</template>

<script>
import { watchEffect, ref, computed } from "vue";
import SocketHandler from "@/modules/SocketHandler";
import UserHandler from "@/modules/UserHandler";
import LeagueHandler from "@/modules/LeagueHandler";
import GameHandler from "@/modules/GameHandler";

export default {
  name: "JoinGame",
  async setup() {
    const { currentUser } = UserHandler();
    const { gameList, getGameList } = SocketHandler();
    const { leagues, getLeagues } = LeagueHandler();
    const { joinGameState } = GameHandler();
    const componentKey = ref(0);
    const selectedLeague = ref(null);

    if(currentUser.value) {
      getGameList(currentUser.value.username)
    }

    watchEffect(
      () => {
        if(gameList && gameList.value !== null){
          componentKey.value += 1;
          joinGameState.gameList = gameList.value
        }
      }
    )


    await getLeagues();

    const leagueFilter = computed(() => {
      return leagues.value
    })    

    const filterGames = computed( () => {
      if(selectedLeague.value !== null && joinGameState.gameList !== null){
        return joinGameState.gameList.filter( games => {
          return games.league === selectedLeague.value.league
        })
      } else {
        return joinGameState.gameList;
      }
    })
    
    function setActiveIndex(index){
      let previouslySelected = document.getElementsByClassName("gamelist-selected");
      if(previouslySelected.length > 0){
        previouslySelected[0].classList.toggle("gamelist-selected")
      }
      
      let selected = document.getElementsByClassName("item-"+index)[0];
      selected.classList.toggle("gamelist-selected")

      joinGameState.activeIndex = index;
    }
    
    
    
    return {
      joinGameState,
      componentKey,
      setActiveIndex,
      leagueFilter,
      selectedLeague,
      filterGames,
      gameList
    };
  },
};
</script>

<style lang="scss" scoped>
  h5{
    margin: 0;
  }

  .gamelist-item{
    border: 1px solid #C1A489;
    margin-bottom: 2px;
  }

  .gamelist-selected, .active{
    background: #C1A489;
  }

</style>