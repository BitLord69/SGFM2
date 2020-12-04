<template>
  <h3>Cards on hand (3 - 8): {{state.cardsOnHand}}</h3>
  <Slider :min="3" :max="8" v-model="state.cardsOnHand" />
  <h3>Points to win (10 - 20): {{state.pointsToWin}}</h3>
  <Slider :min="10" :max="20" v-model="state.pointsToWin" />
</template>

<script>
import { reactive } from "vue";
export default {
  name: "CreateGame",
  setup(){
    const state = reactive({
      cardsOnHand: 5,
      pointsToWin: 15
    })

    return {
      state
    }
  }
}
</script>