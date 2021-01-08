<template>
  <div class="friend-container">
    <div class="Area-1">
      AREA1
    </div>
    <div class="Side">
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
    <div class="Area-2">AREA2</div>
  </div>
</template>

<script>
import FriendList from "../components/FriendList.vue";
export default {
  components: { FriendList },
};
</script>

<style>
.friend-container {
  display: grid;
  width: 85%;
  grid-template-columns: 3fr 1fr;
  grid-template-rows: 1fr 1fr;
  gap: 0px 0px;
  grid-template-areas:
    "Area-1 Side"
    "Area-2 Side";
}
.Area-1 {
  grid-area: Area-1;
}
.Side {
  grid-area: Side;
  align-self: center;
  text-align: center;
  background-color: #e2c3a6;
  border-radius: 10px;
  border: 2px solid #3b1704;
}

.side {
}
.Area-2 {
  grid-area: Area-2;
}
</style>
