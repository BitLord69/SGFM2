<template>
  <div class="friend-container p-mb-5">
    <div class="area-1">
      <Suspense>
        <template #default>
          <div class="p-p-4">
            <UserList title="All Users"/>
          </div>
        </template>
        <template #fallback>
          <div>Loading users...</div>
        </template>
      </Suspense>
    </div>
    <div class="area-2">
      <Suspense>
        <template #default>
          <div class="p-p-4">
            <UserList title="My Friends" />
          </div>
        </template>
        <template #fallback>
          <div>Loading friends...</div>
        </template>
      </Suspense>
    </div>
    <div class="area-3">
      <Suspense>
        <template #default>
          <div class="p-p-4">
            <UserList title="My Sent Friend Requests" />
          </div>
        </template>
        <template #fallback>
          <div>Loading outgoing friend requests...</div>
        </template>
      </Suspense>
    </div>
    <div class="area-4">
      <Suspense>
        <template #default>
          <div class="p-p-4">
            <UserList title="My Received Friend Requests" />
          </div>
        </template>
        <template #fallback>
          <div>Loading outgoing friend requests...</div>
        </template>
      </Suspense>
    </div>
  </div>
</template>

<script>
//import YourFriends from "../components/YourFriends";
//import OutgoingFriendRequestList from '@/components/OutgoingFriendRequestList';
//import IncomingFriendRequestList from '@/components/IncomingFriendRequestList';
import UserList from '../components/UserList';

export default {
  components: { UserList },

  setup() {
  }
};
</script>

<style>
.friend-container {
  display: grid;
  width: 85%;
  color:#3b1704;
  font-family: "Yanone Kaffeesatz", sans-serif;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
  gap: 6em 6em;
  grid-template-areas:
    "area-1 area-2"
    "area-3 area-4";
}
.area-1 {
  grid-area: area-1;
}

.area-1, .area-2, .area-3, .area-4 {
  background-color: #e2c3a6;
  border-radius: 10px;
  border: 2px solid #3b1704;
}

.greenText {
  color: #20a132;
}

.redText {
  color: #8e0707;
}

.area-2 {
  grid-area: area-2;
}

.area-3 {
  grid-area: area-3;
}

.area-4 {
  grid-area: area-4;
}
</style>
