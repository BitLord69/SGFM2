<template>
  <div class="gameboard">
    <div class="playerRow">
      <div class="profile">PROFIL</div>
      <draggable class="cardsOnHand" :list="state.cardsOnHand">
        <div class="card" v-for="(card, index) in state.cardsOnHand" :key="index">
          <img :src="'../' + getImageName(card.name) + '.png'">
        </div>
      </draggable>
    </div>
    <div class="playerRow">
      <draggable class="cardsOnHand" :list="state.playedCards">
        <div class="card" v-for="(card, index) in state.playedCards" :key="index">
          <img :src="'../' + getImageName(card.name) + '.png'">
        </div>
      </draggable>
    </div>
    <div class="playerRow">
      <draggable class="cardsOnHand" :list="state.cardsOnHand" :move="state.playedCards">
        <div class="card" v-for="(card, index) in state.cardsOnHand" :key="index">
          <img :src="'../' + getImageName(card.name) + '.png'">
        </div>
      </draggable>
      <div class="profile">PROFIL</div>
    </div>
  </div>
</template>
<script>
import { reactive } from 'vue';
 import { VueDraggableNext } from 'vue-draggable-next'
export default {
  name: "Gameboard",
  components: {draggable: VueDraggableNext},
  setup() {
    const state = reactive({
      cardsOnHand: [
        {name: "Mutated Worm", power: 1},
        {name: "Orange Menace", power: 3},
        {name: "Sleepy Joe", power: 4},
        {name: "Anonymous Hacker", power: 8},
        {name: "Super Galaxy Face Melter", power: 10},
      ],
      playedCards: [{name: "Super Galaxy Face Melter", power: 10}]
    })


    function getImageName(name) {
      return name.replaceAll(" ", "_").toLowerCase()
    }

    function moveCard(card) {
      return card
    }
    return {state, getImageName, moveCard}
  }
}
</script>
<style scoped>
.gameboard {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.playerRow{
  display: flex;
  width: 100%;
  height:33%;
  justify-content: space-between;
}
.profile {
  width: 20%;
}
.cardsOnHand {
  display: flex;
  width: 80%;
  justify-content: space-evenly;
}
.card img{
  width: 90%;
  height: 90%;
}
</style>