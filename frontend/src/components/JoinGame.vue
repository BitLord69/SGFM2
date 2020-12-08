<template>
  <ScrollPanel style="width: 420px; height: 350px" class="p-scrollpanel-bar-y">
    <Accordion style="width: 400px">
      <AccordionTab v-for="game in state.games" :key="game.title" :header="game.title">
        <p class="text-left">Cards on hand: {{game.content.cardsOnHand}}</p>
        <p class="text-left">Points to win: {{game.content.pointsToWin}}</p>
      </AccordionTab>
    </Accordion>
  </ScrollPanel>
</template>

<script>
import { reactive } from "vue";
export default {
  name: "JoinGame",
  setup(){
    const state = reactive({
      selectedGame: null,
      games: [
                {
                    title: 'Game 1', 
                    content: {
                      cardsOnHand: 5,
                      pointsToWin: 15
                    }
                },
                {
                    title: 'Game 2', 
                    content: {
                      cardsOnHand: 5,
                      pointsToWin: 15
                    }
                },{
                    title: 'Game 3', 
                    content: {
                      cardsOnHand: 5,
                      pointsToWin: 15
                    }
                },{
                    title: 'Game 4', 
                    content: {
                      cardsOnHand: 5,
                      pointsToWin: 15
                    }
                },{
                    title: 'Game 5', 
                    content: {
                      cardsOnHand: 5,
                      pointsToWin: 15
                    }
                },{
                    title: 'Game 6', 
                    content: {
                      cardsOnHand: 5,
                      pointsToWin: 15
                    }
                }
            ]
    })

    async function getGames() {}

    return {
      state, getGames
    }
  }
}
</script>