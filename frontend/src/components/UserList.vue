<template>
  <div>
    <div v-for="user in filteredUsers" :key="user.username">
    <span>{{user.username}}</span>
  </div>
  </div>
</template>

<script>
import UserHandler from "../modules/UserHandler";

export default {

  async setup() {
    const { currentUser, users, getAllUsers } = UserHandler();
    await getAllUsers();

    let filteredUsers = users.value.filter(user => user.username !== currentUser.value.username);


    return {
      filteredUsers,
    }
  }
};
</script>

<style>

</style>