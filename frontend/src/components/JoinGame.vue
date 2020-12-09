<template>
<div :key="componentKey">
  <ScrollPanel style="width: 420px; height: 350px" class="p-scrollpanel-bar-y" >
    <Accordion style="width: 400px" v-if="state.gameList">
      <AccordionTab
        v-for="game in state.gameList"
        :key="game.roomNo"
        :header="'Game ' + game.roomNo + ' - ' + game.creator"
      >
        <p class="text-left">Cards on hand: {{ game.cardsOnHand }}</p>
        <p class="text-left">Points to win: {{ game.pointsToWin }}</p>
      </AccordionTab>
    </Accordion>
  </ScrollPanel>
</div>
</template>

<script>
import { reactive, watchEffect, ref } from "vue";
import SocketHandler from "@/modules/SocketHandler";
export default {
  name: "JoinGame",
  setup() {
    const { gameList, getGameList } = SocketHandler();
    const state = reactive({
      selectedGame: null,
      gameList: null
    });

    const componentKey = ref(0);

    watchEffect(
       () => {
        if(gameList.value !== null){
          componentKey.value += 1;
          state.gameList = gameList.value
        }
      }
    )
    getGameList()
    return {
      state,
      componentKey
    };
  },
};
</script>