<template>
  <div>
    <div class="p-fluid p-grid p-jc-center">
      <h2>My received friend requests</h2>
      <div class="p-col-12">
        <span class="p-input-icon-left p-fluid">
        <i class="pi pi-search" />
        <InputText type="text" v-model="userList.search" placeholder="Search" />
    </span>
      </div>
    </div>
    
    <ScrollPanel style="width: 100%; height: 13.7em" class="p-scrollpanel-bar-y" >
      <div class="userlist-item p-grid p-m-1" v-for="user in filteredUsers()" :key="user.username">
        <div class="p-col-1 p-jc-end p-py-1 p-d-flex">
          <img
                :src="'/avatar/' + user.avatar + '.png'"
                :alt="user.avatar"
                class="avatar-image"
              />
        </div>
        <span class="p-my-auto p-col-9 p-py-0">{{user.username}}</span>
        <div class="p-my-auto p-col-2 p-py-0 p-text-center">
          <i class="pi pi-check-circle pointer greenText" style="fontSize: 1.2rem" @click="acceptFriendRequest(user.username)" v-if="!userList.processed.includes(user.username)"></i>
          <i class="pi pi-times-circle pointer p-pl-3 redText" style="fontSize: 1.2rem" @click="denyFriendRequest(user.username)" v-if="!userList.processed.includes(user.username)"></i>
          <i class="pi pi-check-circle" style="fontSize: 1.2rem" v-else></i>
        </div>
      </div>
    </ScrollPanel>
  </div>
</template>

<script>
import { reactive } from "vue";
import FriendHandler from "@/modules/FriendHandler";

export default {

  async setup() {
    const { inFriendReq, getIncomingFriendRequest, createFriendRequest, deleteFriend } = FriendHandler();
    const userList = reactive({
      search: "",
      processed: ""
    });

    await getIncomingFriendRequest();

    function filteredUsers() {
      return inFriendReq.value.filter(user => user.username.toLowerCase().includes(userList.search.toLowerCase()))
    } 

    function acceptFriendRequest(username) {
      userList.processed += username;
      createFriendRequest(username);
    }

    function denyFriendRequest(username) {
      userList.processed += username;
      deleteFriend(username);
    }

    return {
      filteredUsers,
      userList,
      acceptFriendRequest,
      denyFriendRequest
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