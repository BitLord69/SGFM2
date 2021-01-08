<template>
<div :key="componentKey">
  <ScrollPanel style="width: 420px; height: 350px" class="p-scrollpanel-bar-y" >
    <!-- <Accordion style="width: 400px" v-if="joinGameState.gameList && joinGameState.gameList.length > 0">
      <AccordionTab
        v-for="game in joinGameState.gameList"
        :key="game.roomNo"
        :header="'Game ' + game.roomNo + ' - ' + game.creator"
        :activeIndex="joinGameState.activeIndex"
      >
        <p class="text-left">Cards on hand: {{ game.cardsOnHand }}</p>
        <p class="text-left">Points to win: {{ game.pointsToWin }}</p>
      </AccordionTab>
    </Accordion> -->
    <div  v-if="joinGameState.gameList && joinGameState.gameList.length > 0">
      <div  :class="'gamelist-item' + ' item-'+index" v-for="(game, index) in joinGameState.gameList" :key="game.roomNo" @click="setActiveIndex(index)">
        <h4 class="p-my-1 p-ml-1">{{game.creator}}'s game</h4>
        <h5 class="p-d-flex p-jc-evenly"><span>&#8226; Cards on hand: {{game.cardsOnHand}}</span> <span>&#8226; Points to win: {{game.pointsToWin}}</span></h5>
      </div>
    </div>
    <div v-else>
      No current games to join, please hang tight....
    </div>
  </ScrollPanel>
</div>
</template>

<script>
import { reactive, watchEffect, ref } from "vue";
import SocketHandler from "@/modules/SocketHandler";
import UserHandler from "@/modules/UserHandler";

const joinGameState = reactive({
  activeIndex: 0,
  selectedGame: null,
  gameList: null
});

export default {
  name: "JoinGame",
  setup() {
    const { currentUser } = UserHandler();
    const { gameList, getGameList } = SocketHandler();

    const componentKey = ref(0);
    
    watchEffect(
       () => {
        if(gameList.value !== null){
          componentKey.value += 1;
          joinGameState.gameList = gameList.value
        }
      }
    )

    function setActiveIndex(index){
      let previouslySelected = document.getElementsByClassName("gamelist-selected");
      if(previouslySelected.length > 0){
        previouslySelected[0].classList.toggle("gamelist-selected")
      }
      
      let selected = document.getElementsByClassName("item-"+index)[0];
      selected.classList.toggle("gamelist-selected")

      joinGameState.activeIndex = index;
    }
    
    getGameList(currentUser.value.username)
  
    return {
      joinGameState,
      componentKey,
      setActiveIndex,
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