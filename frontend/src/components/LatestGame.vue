<template>
  <div>
    <div id="latestGame1" v-if="props.type == 'Your latest played game' && !currentUser.username.startsWith('guest') && myLatestGame">
      <div>
        <div class="p-text-center p-mb-1">{{props.type}}</div>
        <div class="latestGame-content p-m-0 p-grid">
          <div class="p-col-2 p-p-0 p-my-auto"><img class="avatar p-my-auto" :src="'/avatar/' + currentUser.avatar + '.png'" alt="" /></div>
          <div class="p-col-9 p-p-0 p-my-auto">{{currentUser.username}}</div>
          <div class="p-col-1 p-p-0 p-my-auto">{{myLatestGame.myPoints}}</div>
          <div class="divider p-m-1"></div>
          <div class="p-col-2 p-p-0 p-my-auto"><img class="avatar" :src="'/avatar/' + myLatestGame.oppAvatar + '.png'" alt="" /></div>
          <div class="p-col-9 p-p-0 p-my-auto">{{myLatestGame.opponent}}</div>
          <div class="p-col-1 p-p-0 p-my-auto">{{myLatestGame.oppPoints}}</div>
        </div>
        <div class="p-text-center p-mt-1">{{moment(myLatestGame.time).fromNow(true)}} ago</div>
      </div>
    </div>
    <div id="latestGame2" v-if="props.type == 'The latest played game' && latestGame">
      <div>
        <div class="p-text-center p-mb-1">{{props.type}}</div>
        <div class="latestGame-content p-m-0 p-grid">
          <div class="p-col-2 p-p-0 p-my-auto"><img class="avatar p-my-auto" :src="'/avatar/' + latestGame.p1Avatar + '.png'" alt="" /></div>
          <div class="p-col-9 p-p-0 p-my-auto">{{latestGame.p1Username}}</div>
          <div class="p-col-1 p-p-0 p-my-auto">{{latestGame.p1Points}}</div>
          <div class="divider p-m-1"></div>
          <div class="p-col-2 p-p-0 p-my-auto"><img class="avatar" :src="'/avatar/' + latestGame.p2Avatar + '.png'" alt="" /></div>
          <div class="p-col-9 p-p-0 p-my-auto">{{latestGame.p2Username}}</div>
          <div class="p-col-1 p-p-0 p-my-auto">{{latestGame.p2Points}}</div>
        </div>
        <div class="p-text-center p-mt-1">{{moment(latestGame.time).fromNow(true)}} ago</div>
      </div>
    </div>
  </div>
</template>
<script>
import GameHandler from "@/modules/GameHandler";
import UserHandler from "@/modules/UserHandler";
import moment from 'moment';

export default {
  props: { type: String },

  async setup(props) {

  const { getMyLatestGame, myLatestGame, getLatestGame, latestGame } = GameHandler();
  const { currentUser } = UserHandler();

  await getLatest();

  async function getLatest() {
    if(props.type == "Your latest played game") {
      await getMyLatestGame();
    } else {
      await getLatestGame();
    }
  }
  

  return {
    myLatestGame,
    latestGame,
    currentUser,
    moment,
    props
  }
  }
};
</script>

<style>
#latestGame1 ,#latestGame2 {
  display: flex;
  justify-content: center;
  color: #3b1704;
  background-color: #e2c3a6;
  padding: 1%;
  border: 2px solid #3b1704;
  width:17vw;
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
  vertical-align: baseline;
}
</style>
