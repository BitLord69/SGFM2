<template>
<teleport to="body">
  <div class="sidebar" v-if="currentUser !== null">
    <img @click="toggleNav" :src="'/avatar/' + currentUser.avatar + '.png'" alt="" />
    <div class="sidebar-hidden" id="sidebar">
      <div class="sidebar-content">
      </div>
      <div class="sb-logout" @click="logMeOut">Logout</div>
    </div>
  </div>
</teleport>
</template>

<script>
import UserHandler from "../modules/UserHandler";
import {useRouter} from "vue-router"

export default {
  name: "Header",
  setup() {
    const router = useRouter();
    const { currentUser, isLoggedIn, logout } = UserHandler();

    async function logMeOut(){
      await logout();
      router.push("/")
    }
    
    return {
      toggleNav,
      currentUser,
      isLoggedIn,
      logMeOut
    };

    function toggleNav() {
      let sidebar = document.getElementById("sidebar");
      sidebar.classList.toggle("sidebar-hidden");
      sidebar.classList.toggle("sidebar-visible");
      console.log("profile-bar klickad");
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
  border-radius: 50px;
  img {
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
  transition: ease-in 500ms;
  z-index: 15;
  top: -1vh;
  right: -1vw;
  width: 250px;
  height: 82vh;
  background-color:#e2c3a6;
  border: 2px solid #3b1704;
  border-radius: 2px;
  .sidebar-content{
    transition: ease-in 500ms;
    display: flex;
    flex-direction: column;
    height: 95%;
    div{
      margin: 0;
      padding: 0.2rem;
      font-size: 18px;
      text-transform: uppercase;
      text-align: center;
    }
  }
  .sb-logout{
    transition: ease-in 500ms;
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
  transition: ease-in 500ms;
  
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
</style>
