<template>
  <div class="profile-img" @click="toggleNav" v-if="user !== null">
    <img :src="'/avatar/' + user.avatar + '.png'" alt="" />
    <div class="sidebar-hidden" id="sidebar">
      <p>Logout</p>
    </div>
  </div>
</template>

<script>
import { user } from "../views/LandingPage";

export default {
  name: "Header",
  setup() {
    return {
      user,
      toggleNav,
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
.profile-img {
  width: 100px;
  height: 100px;
  color: red;
  position: absolute;
  right: -2.5vw;
  top: 0.5vh;
  border-radius: 50px;
  img {
    width: 100px;
    height: 100px;
    border: 2px solid #2c3e50;
    border-radius: 50px;
    z-index: 100;
  }
}

.sidebar-visible {
  position: relative;
  visibility: visible;
  transition: 500ms ease;
  width: 50px;
  height: 150px;
  background-color: royalblue;
}

.sidebar-hidden {
  position: relative;
  visibility: hidden;
  transition: 500ms ease;
}
</style>
