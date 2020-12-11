<template>
  <h4 class="p-mt-0">Cards on hand (3 - 8): {{CreateGameState.cardsOnHand}}</h4>
  <Slider :min="3" :max="8" v-model="CreateGameState.cardsOnHand" />
  <h4>Points to win (10 - 20): {{CreateGameState.pointsToWin}}</h4>
  <Slider :min="10" :max="20" v-model="CreateGameState.pointsToWin" />
</template>

<script>
import { reactive } from "vue";

const CreateGameState = reactive({
  cardsOnHand: 5,
  pointsToWin: 15
})

export default {
  name: "CreateGame",
  setup(){

    return {
      CreateGameState,
    }
  }
}
</script>