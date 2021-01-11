<template>
  <div>
    <div class="p-fluid p-grid p-jc-center">
      <h2>My Friends</h2>
      <div class="p-col-12">
        <span class="p-input-icon-left p-fluid">
        <i class="pi pi-search" />
        <InputText type="text" v-model="friendList.search" placeholder="Search" />
    </span>
      </div>
    </div>
    
    <ScrollPanel style="width: 100%; height: 13.75em" class="p-scrollpanel-bar-y" >
      <div class="userlist-item p-grid p-m-1" v-for="user in filteredFriends()" :key="user.username">
        <div class="p-col-1 p-jc-end p-py-1 p-d-flex">
          <img
                :src="'/avatar/' + user.avatar + '.png'"
                :alt="user.avatar"
                class="avatar-image"
              />
        </div>
        <span class="p-my-auto p-col-10 p-py-0 p-text-left">{{user.username}}</span>
        <div class="p-my-auto p-col-1 p-py-0">
          <i class="pi pi-trash pointer redText" style="fontSize: 1.2rem" @click="confirmDeleteFriend(user.username)" v-if="!friendList.deleted.includes(user.username)"></i>
          <i class="pi pi-check-circle" style="fontSize: 1.2rem" v-else></i>
        </div>
      </div>
    </ScrollPanel>
    <div>You have {{ friends ? friends.length : 0 }} friend(s)</div>
  </div>
</template>

<script>
import { reactive } from "vue";
import FriendHandler from "@/modules/FriendHandler";

export default {

  async setup() {
    const { friends, getFriends, deleteFriend } = FriendHandler();
    const friendList = reactive({
      search: "",
      deleted: ""
    });

    await getFriends();

    function filteredFriends() {
      return friends.value.filter(user => user.username.toLowerCase().includes(friendList.search.toLowerCase()))
    } 

    function confirmDeleteFriend(username) {
      console.log("Request Sent!");
      friendList.deleted += username;
      deleteFriend(username);
    }

    return {
      friends,
      filteredFriends,
      friendList,
      confirmDeleteFriend
    }
  }
};
</script>

<style>
  .userlist-item{
    border: 1px solid #C1A489;
    color: #3b1704;
    font-family: "Yanone Kaffeesatz", sans-serif;
    font-size: 120%;
  }
</style>