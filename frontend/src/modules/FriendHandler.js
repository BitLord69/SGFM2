import { ref } from "vue";
import { extFetch } from "./extFetch";

const friends = ref(null);
const friendError = ref(null);

export default function FriendHandler() {

  async function getFriends() {
    try {
      friends.value = await extFetch("/api/user/friends/", "GET");
      console.log("friends.val:", friends.value);
    } catch (e) {
      friendError.value = e;
      return;
    }
  }

  async function deleteFriend(friendToDelete) {
    try {
      await extFetch("/api/user/friends", "DELETE", {friendname: friendToDelete})
    } catch (error) {
      friendError.value = error
    }
  }

  async function createFriendRequest(friendToInvite) {
    try {
      await extFetch("/api/user/friends", "POST", {friendname: friendToInvite})
    } catch (error) {
      friendError.value = error
    }
  }

  async function acceptFriendRequest(invitationToAccept) {
    try {
      await extFetch("/api/user/friends/request", "POST", {friendname: invitationToAccept})
    } catch (error) {
      friendError.value = error
    }
  }

    return {
      friends,
      friendError,
      getFriends,
      deleteFriend,
      createFriendRequest,
      acceptFriendRequest,
    };
}
