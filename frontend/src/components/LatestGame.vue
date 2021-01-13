<template>
  <div id="latestGame" v-if="!currentUser.username.startsWith('guest') && latestGame">
    <div class="p-text-center p-mb-1">Your latest played game</div>
    <div class="latestGame-content p-m-0 p-grid">
      <div class="p-col-2 p-p-0 p-my-auto"><img class="avatar p-my-auto" :src="'/avatar/' + currentUser.avatar + '.png'" alt="" /></div>
      <div class="p-col-9 p-p-0 p-my-auto">{{currentUser.username}}</div>
      <div class="p-col-1 p-p-0 p-my-auto">{{latestGame.myPoints}}</div>
      <div class="divider p-m-1"></div>
      <div class="p-col-2 p-p-0 p-my-auto"><img class="avatar" :src="'/avatar/' + latestGame.oppAvatar + '.png'" alt="" /></div>
      <div class="p-col-9 p-p-0 p-my-auto">{{latestGame.opponent}}</div>
      <div class="p-col-1 p-p-0 p-my-auto">{{latestGame.oppPoints}}</div>
    </div>
    <div class="p-text-center p-mt-1">{{moment(latestGame.time).fromNow(true)}} ago</div>
  </div>
</template>
<script>
import GameHandler from "@/modules/GameHandler";
import UserHandler from "@/modules/UserHandler";
import moment from 'moment';

export default {

  async setup() {

  const { getLatestGame, latestGame } = GameHandler();
  const { currentUser } = UserHandler();

  await getLatestGame();

  return {
    latestGame,
    currentUser,
    moment
  }
  }
};
</script>

<style>
#latestGame {
  color: #3b1704;
  background-color: #e2c3a6;
  padding: 1%;
  border: 2px solid #3b1704;
  border-radius: 5px;
  font-family: "Yanone Kaffeesatz", sans-serif;
}

.latestGame-content {
  background-color: #eeeeee;
  width: 15vw;
  padding: 3%;
  border-radius: 5px;
}
.divider {
  width:100%;
  border-bottom: 1px solid #bebebe;
}
.avatar {
  display: block;
  margin-top: auto;
  margin-bottom: auto;
  width: 2em;
  border: 2px solid #3b1704;
  border-radius: 50%;
  vertical-align: baseline
}
</style>
