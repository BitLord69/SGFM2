<template>
<teleport to="body">
  <div class="sidebar" v-if="currentUser !== null">
    <img class="profile-sb-close" id="profile-img" @click="toggleNav" :src="'/avatar/' + currentUser.avatar + '.png'" alt="" />
    <div class="sidebar-hidden" id="sidebar">
      <div class="sidebar-content">
        <div class="username">
          {{currentUser.username}}
        </div>
        <div class="p-mt-2" v-if="!isLoggedInAsGuest && stats !== null">
          <Suspense>
          <template #default>
            <Statistics bottomBorder />
          </template>
          <template #fallback>
            <div>Loading stats...</div>
          </template>
        </Suspense>
        </div>
        <div class="p-mt-2" v-if="!isLoggedInAsGuest">
          <Suspense>
          <template #default>
            <FriendsComponent title="My Friends" fromSideBar />
          </template>
          <template #fallback>
            <div>Loading friends...</div>
          </template>
          </Suspense>
        </div>
      </div>
      <router-link v-if="isLoggedIn" style="text-decoration: none;" to="/stats" class="sb-logout">Statistics</router-link>
      <router-link v-if="isLoggedIn" style="text-decoration: none;" to="/friends" class="sb-logout">Manage Friends</router-link>
      <div class="sb-logout" @click="logMeOut">Logout</div>
    </div>
  </div>
</teleport>
</template>

<script>
import UserHandler from "../modules/UserHandler";
import {useRouter} from "vue-router"
import Statistics from './Statistics.vue';
import GameHandler from "../modules/GameHandler";
import FriendsComponent from './FriendsComponent.vue';

export default {
  components: { Statistics, FriendsComponent },
  name: "Sidebar",
  setup() {
    const router = useRouter();
    const { currentUser, isLoggedIn, logout, isLoggedInAsGuest } = UserHandler();
    const { stats, getGames } = GameHandler();
    let toggle = false;
    async function logMeOut(){
      toggle = !toggle;
      await logout();
      router.push("/")
    }
    

    async function toggleNav() {
      toggle = !toggle;
      if(toggle && !isLoggedInAsGuest.value){
        await getGames();
      }
      let sidebar = document.getElementById("sidebar");
      sidebar.classList.toggle("sidebar-hidden");
      sidebar.classList.toggle("sidebar-visible");
      let profileImg = document.getElementById('profile-img');
      profileImg.classList.toggle('profile-sb-open');
      profileImg.classList.toggle('profile-sb-close');
    }
    
    return {
      toggleNav,
      currentUser,
      isLoggedIn,
      logMeOut,
      stats,
      isLoggedInAsGuest
    };
  },
};
</script>

<style scoped lang="scss">

$sidebarTop: 3vh;
$sidebarWidth: 250px;
$imageSize: 100px;
$imageRight: 18px;

.sidebar {
  width: 100px;
  height: 100px;
  color:#3b1704;
  position: absolute;
  right: $imageRight;
  top: $sidebarTop;
  font-family: "Yanone Kaffeesatz", sans-serif;
  letter-spacing: 2px;
  font-weight: bold;
  font-size: 18px;
  
  img {
    cursor: pointer;
    position: relative;
    width: $imageSize;
    height: $imageSize;
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
  width: $sidebarWidth;
  height: calc(100vh - #{$sidebarTop});
  background-color:#e2c3a6;
  border: 2px solid #3b1704;
  border-top-left-radius: 40px;
  .sidebar-content{
    transition: ease-in 400ms;
    display: flex;
    flex-direction: column;
    height: 85%;
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
  color: #3b1704;
  &:hover{
    cursor: pointer;
    background-color: darken($color: #e2c3a6, $amount: 10%);
  }
}

.username {
  margin-top: 120px;
}

.profile-sb-open {
  right: calc(calc(calc(#{$sidebarWidth} - #{$imageSize}) / 2) - #{$imageRight});
  transition: ease-in 400ms;
}

.profile-sb-close {
  right: 0;
  transition: ease-in 400ms;
}
</style>
