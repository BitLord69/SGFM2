<template>
<div :key="componentKey">
  <ScrollPanel style="width: 420px; height: 350px" class="p-scrollpanel-bar-y" >
    <Accordion style="width: 400px" v-if="joinGameState.gameList && joinGameState.gameList.length > 0">
      <AccordionTab
        v-for="game in joinGameState.gameList"
        :key="game.roomNo"
        :header="'Game ' + game.roomNo + ' - ' + game.creator"
        :activeIndex="joinGameState.activeIndex"
      >
        <p class="text-left">Cards on hand: {{ game.cardsOnHand }}</p>
        <p class="text-left">Points to win: {{ game.pointsToWin }}</p>
      </AccordionTab>
    </Accordion>
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

    getGameList(currentUser.value.username)
    return {
      joinGameState,
      componentKey,
    };
  },
};
</script>