<template>
  <div>
    <div class="p-fluid p-grid p-jc-center">
      <h2 class="p-my-2">{{title}}</h2>
      <div v-if="!fromSideBar" class="p-col-12">
        <span class="p-input-icon-left p-fluid">
          <i class="pi pi-search" />
          <InputText type="text"  v-model="userList.search" placeholder="Search" />
        </span>
      </div>
    </div>
    
    <ScrollPanel style="width: 100%; height: 13.75em" class="p-scrollpanel-bar-y" >
      <div class="userlist-item p-grid p-m-1" v-for="user in filteredUsers()" :key="user.username">
        <div class="p-col-3 p-jc-end p-py-1 p-d-flex" v-if="fromSideBar">
          <img
                :src="'/avatar/' + user.avatar + '.png'"
                :alt="user.avatar"
                class="avatar-image"
              />
        </div>
        <div class="p-col-2 p-xl-1 p-jc-end p-py-1 p-d-flex" v-if="!fromSideBar">
          <img
                :src="'/avatar/' + user.avatar + '.png'"
                :alt="user.avatar"
                class="avatar-image"
              />
        </div>
        <span class="p-my-auto p-col-9 p-py-0" v-if="fromSideBar" :class="{ sidebarFriends: fromSideBar }">{{user.username}}</span>
        <span class="p-my-auto p-col-8 p-xl-10 p-py-0" v-if="!fromSideBar">{{user.username}}</span>
        <div class="p-my-auto p-col-2 p-xl-1 p-py-0">
          <i class="pi pi-user-plus pointer greenText" style="fontSize: 1.2rem" @click="sendOrAcceptFriendRequest(user.username)" v-if="!userList.processed.includes(user.username) && title == 'All Users'"></i>
          <i class="pi pi-trash pointer redText" style="fontSize: 1.2rem" @click="deleteFriendOrFriendRequest(user.username)" v-if="!userList.processed.includes(user.username) && title == 'My Friends' && !fromSideBar"></i>
          <i class="pi pi-times-circle pointer redText" style="fontSize: 1.2rem" @click="deleteFriendOrFriendRequest(user.username)" v-if="!userList.processed.includes(user.username) && title == 'My Sent Friend Requests'"></i>
          <i class="pi pi-check-circle pointer greenText" style="fontSize: 1.2rem" @click="sendOrAcceptFriendRequest(user.username)" v-if="!userList.processed.includes(user.username) && title == 'My Received Friend Requests'"></i>
          <i class="pi pi-times-circle pointer p-pl-3 redText" style="fontSize: 1.2rem" @click="deleteFriendOrFriendRequest(user.username)" v-if="!userList.processed.includes(user.username) && title == 'My Received Friend Requests'"></i>
          <i class="pi pi-check-circle" style="fontSize: 1.2rem" v-if="userList.processed.includes(user.username)"></i>
        </div>
      </div>
    </ScrollPanel>
    <div v-if="title == 'My Friends'" class="p-text-center">You have {{ friends ? friends.length : 0 }} friend(s)</div>
  </div>
</template>

<script>
import { reactive } from "vue";
import FriendHandler from "@/modules/FriendHandler";

export default {
  props: { title: String, fromSideBar: Boolean },

  async setup(props) {
    const { 
      nonFriends, 
      getNonFriends, 
      createOrAcceptFriendRequest, 
      friends, 
      getFriends, 
      deleteFriend, 
      outFriendReq, 
      getOutgoingFriendRequest, 
      inFriendReq, 
      getIncomingFriendRequest
    } = FriendHandler();

    const userList = reactive({
      search: "",
      processed: ""
    });

    await getInformation();
    
    async function getInformation() {
      switch(props.title) {
        case "All Users":
          await getNonFriends();
          break;
        case "My Friends":
          await getFriends();
          break;
        case "My Sent Friend Requests":
          await getOutgoingFriendRequest();
          break;
        case "My Received Friend Requests":
          await getIncomingFriendRequest();
          break;
      }
    } 

    function filteredUsers() {
      switch(props.title) {
        case "All Users":
          return nonFriends.value.filter(user => user.username.toLowerCase().includes(userList.search.toLowerCase()))
        case "My Friends":
          return friends.value.filter(user => user.username.toLowerCase().includes(userList.search.toLowerCase()))
        case "My Sent Friend Requests":
          return outFriendReq.value.filter(user => user.username.toLowerCase().includes(userList.search.toLowerCase()))
        case "My Received Friend Requests":
          return inFriendReq.value.filter(user => user.username.toLowerCase().includes(userList.search.toLowerCase()))
      }
    } 

    function sendOrAcceptFriendRequest(username) {
      userList.processed += username;
      createOrAcceptFriendRequest(username);
    }

    function deleteFriendOrFriendRequest(username) {
      userList.processed += username;
      deleteFriend(username);
    }

    return {
      friends,
      deleteFriendOrFriendRequest,
      nonFriends,
      filteredUsers,
      userList,
      sendOrAcceptFriendRequest
    }
  }
};
</script>

<style lang="scss">
.sidebarFriends {
  text-align: left;
}

.userlist-item {
  border: 1px solid #C1A489;
  color: #3b1704;
  font-family: "Yanone Kaffeesatz", sans-serif;
  font-size: 120%;
}

.p-scrollpanel-wrapper {
  border-right: 7px solid #a28971;
}

.p-scrollpanel-bar {
    background: #3b1704 !important;
    opacity: 1;
    transition: background-color .3s;
    width: 7px;
}

.p-scrollpanel-bar:hover {
    background: lighten($color: #3b1704, $amount: 10%) !important;
}
</style>