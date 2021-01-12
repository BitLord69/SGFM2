import { ref } from "vue";
import { extFetch } from "./extFetch";

const friends = ref(null);
const nonFriends = ref(null);
const inFriendReq = ref(null);
const outFriendReq = ref(null);
const friendError = ref(null);

export default function FriendHandler() {

  async function getFriends() {
    try {
      friends.value = await extFetch("/api/user/friends/", "GET");
    } catch (e) {
      friendError.value = e;
      return;
    }
  }

  async function getNonFriends() {
    try {
      nonFriends.value = await extFetch("/api/user/nonfriends/", "GET");
    } catch (e) {
      friendError.value = e;
      return;
    }
  }

  async function getIncomingFriendRequest() {
    try {
      inFriendReq.value = await extFetch("/api/user/friends/requests/", "GET");
      console.log("friends.val:", inFriendReq.value);
    } catch (e) {
      friendError.value = e;
      return;
    }
  }

  async function getOutgoingFriendRequest() {
    try {
      outFriendReq.value = await extFetch("/api/user/nonfriends/requests/", "GET");
      console.log("friends.val:", outFriendReq.value);
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

  async function createOrAcceptFriendRequest(friendToInvite) {
    try {
      await extFetch("/api/user/friends", "POST", {friendname: friendToInvite})
    } catch (error) {
      friendError.value = error
    }
  }

    return {
      friends,
      nonFriends,
      inFriendReq,
      outFriendReq,
      friendError,
      getFriends,
      getNonFriends,
      getIncomingFriendRequest,
      getOutgoingFriendRequest,
      deleteFriend,
      createOrAcceptFriendRequest
    };
}
