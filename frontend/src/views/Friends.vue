<template>
  <div class="friend-container">
    <div class="area-1 p-mb-2">
      <Suspense>
        <template #default>
          <div class="p-p-4">
            <UserList />
          </div>
        </template>
        <template #fallback>
          <div>Loading users...</div>
        </template>
      </Suspense>
    </div>
    <div class="area-2 p-mb-2">
      <Suspense>
        <template #default>
          <div class="p-p-4">
            <FriendList showDeleteIcon />
          </div>
        </template>
        <template #fallback>
          <div>Loading friends...</div>
        </template>
      </Suspense>
    </div>
    <div class="area-3 p-mb-5">
      <Suspense>
        <template #default>
          <div class="p-p-4">
            <OutgoingFriendRequestList />
          </div>
        </template>
        <template #fallback>
          <div>Loading outgoing friend requests...</div>
        </template>
      </Suspense>
    </div>
    <div class="area-4 p-mb-5">
      <Suspense>
        <template #default>
          <div class="p-p-4">
            <IncomingFriendRequestList />
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
import FriendList from "../components/FriendList";
import UserList from '../components/UserList';
import OutgoingFriendRequestList from '@/components/OutgoingFriendRequestList';
import IncomingFriendRequestList from '@/components/IncomingFriendRequestList';

export default {
  components: { FriendList, UserList, OutgoingFriendRequestList, IncomingFriendRequestList },

  setup() {
  }
};
</script>

<style>
.friend-container {
  display: grid;
  width: 85%;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
  gap: 0px 6px;
  grid-template-areas:
    "area-1 area-2"
    "area-3 area-4";
}
.area-1 {
  grid-area: area-1;
}

.area-1, .area-3, .area-4 {
  background-color: #e2c3a6;
  border-radius: 10px;
  border: 2px solid #3b1704;
}

h2 {
  color:#3b1704;
  font-family: "Yanone Kaffeesatz", sans-serif;
}

.area-2 {
  grid-area: area-2;
  align-self: center;
  text-align: center;
  background-color: #e2c3a6;
  border-radius: 10px;
  border: 2px solid #3b1704;
}

.area-3 {
  grid-area: area-3;
}

.area-4 {
  grid-area: area-4;
}
</style>
