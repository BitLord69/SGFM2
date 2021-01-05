<template>
<teleport to="body">
  <div class="sidebar" v-if="currentUser !== null">
    <img class="profile-sb-close" id="profile-img" @click="toggleNav" :src="'/avatar/' + currentUser.avatar + '.png'" alt="" />
    <div class="sidebar-hidden" id="sidebar">
      <div class="sidebar-content">
        <div class="username">
          {{currentUser.username}}
        </div>
        <div class="p-mt-5" v-if="stats !== null">
          <Statistics v-bind:stats="stats"/>
        </div>
      </div>
      <div class="sb-logout" @click="logMeOut">Logout</div>
    </div>
  </div>
</teleport>
</template>

<script>
import UserHandler from "../modules/UserHandler";
import {useRouter} from "vue-router"
import Statistics from './Statistics.vue';
import { extFetch } from '../modules/extFetch';
import { ref } from "vue";

export default {
  components: { Statistics },
  name: "Header",
  setup() {
    const router = useRouter();
    const { currentUser, isLoggedIn, logout } = UserHandler();
    let toggle = false;
    const stats = ref(null);
    async function logMeOut(){
      await logout();
      router.push("/")
    }
    
    return {
      toggleNav,
      currentUser,
      isLoggedIn,
      logMeOut,
      stats
    };

    async function toggleNav() {
      toggle = !toggle;
      if(toggle){
        try {
           stats.value = await extFetch("/api/game/", "GET")
           console.log(stats.value);
        } catch (e) {
         console.log("toggleNav fetch statistics:", e);
        }    
      }
      let sidebar = document.getElementById("sidebar");
      sidebar.classList.toggle("sidebar-hidden");
      sidebar.classList.toggle("sidebar-visible");
      let profileImg = document.getElementById('profile-img');
      profileImg.classList.toggle('profile-sb-open');
      profileImg.classList.toggle('profile-sb-close');
    }
  },
};
</script>

<style scoped lang="scss">
.sidebar {
  width: 100px;
  height: 100px;
  color:#3b1704;
  position: absolute;
  right: 12vw;
  top: 3vh;
  font-family: "Yanone Kaffeesatz", sans-serif;
  letter-spacing: 2px;
  font-weight: bold;
  font-size: 18px;
  
  img {
    cursor: pointer;
    position: relative;
    width: 100px;
    height: 100px;
    border: 2px solid #2c3e50;
    border-radius: 50px; 
    z-index: 100;
  }
}

.sidebar-visible {
  position: absolute;
  visibility: visible;
  transition: ease-in 400ms;
  z-index: 15;
  top: -1vh;
  right: -1vw;
  width: 250px;
  height: 82vh;
  background-color:#e2c3a6;
  border: 2px solid #3b1704;
  border-top-left-radius: 40px;
  .sidebar-content{
    transition: ease-in 400ms;
    display: flex;
    flex-direction: column;
    height: 95%;
    div{
      // margin: 0;
      padding: 0.2rem;
      font-size: 18px;
      text-transform: uppercase;
      text-align: center;
    }
  }
  .sb-logout{
    transition: ease-in 400ms;
    display: flex;
  }
}

.sidebar-hidden {
  position: absolute;
  visibility: hidden;
  overflow-x: hidden;
  top: -1vh;
  right: -1vw;
  width: 0;
  height: 82vh;
  transition: ease-in 400ms;
  border-top-left-radius: 40px;
}

.sb-logout{
  height: 5%;
  justify-content: center;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
  text-transform: uppercase;
  &:hover{
    cursor: pointer;
    background-color: darken($color: #e2c3a6, $amount: 30%);
  }
}

.username {
  margin-top: 120px;
}

.profile-sb-open {
  right: 3.75vw;
  transition: ease-in 400ms;
}

.profile-sb-close {
  right: 0;
  transition: ease-in 400ms;
}
</style>
