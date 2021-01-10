<template>
  <div>
    <div class="p-fluid p-grid p-jc-center">
      <h2>My sent friend requests</h2>
      <div class="p-col-12">
        <span class="p-input-icon-left p-fluid">
        <i class="pi pi-search" />
        <InputText type="text" v-model="userList.search" placeholder="Search" />
    </span>
      </div>
    </div>
    
    <ScrollPanel style="width: 100%; height: 100%" class="p-scrollpanel-bar-y" >
      <div class="userlist-item p-grid p-m-1" v-for="user in filteredUsers()" :key="user.username">
        <div class="p-col-1 p-jc-end p-py-1 p-d-flex">
          <img
                :src="'/avatar/' + user.avatar + '.png'"
                :alt="user.avatar"
                class="avatar-image"
              />
        </div>
        <span class="p-my-auto p-col-10 p-py-0">{{user.username}}</span>
        <div class="p-my-auto p-col-1 p-py-0">
          <i class="pi pi-user-plus pointer" style="fontSize: 1.2rem" @click="sendFriendRequest(user.username)" v-if="!userList.requestSent.includes(user.username)"></i>
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
    const { outFriendReq, getOutgoingFriendRequest, createFriendRequest } = FriendHandler();
    const userList = reactive({
      search: "",
      requestSent: ""
    });

    await getOutgoingFriendRequest();

    function filteredUsers() {
      return outFriendReq.value.filter(user => user.username.toLowerCase().includes(userList.search.toLowerCase()))
    } 

    function sendFriendRequest(username) {
      console.log("Request Sent!");
      userList.requestSent += username;
      createFriendRequest(username);
    }

    return {
      filteredUsers,
      userList,
      sendFriendRequest
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