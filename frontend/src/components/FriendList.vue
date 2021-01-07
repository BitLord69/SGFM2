<template>
  <div class="friendsContainer">
    <DataTable class="p-datatable-sm" :value="friends">
      <template #header>
        <div class="table-header initialSize">
          Your friends
        </div>
      </template>
      <Column class="avatar-col">
        <template #body="slotProps">
          <img
            :src="'/avatar/' + slotProps.data.avatar + '.png'"
            :alt="slotProps.data.avatar"
            class="avatar-image"
          />
        </template>
      </Column>
      <Column>
        <template #body="slotProps">
          <span class="initialSize">
            {{ slotProps.data.username }}
          </span>
        </template>
      </Column>
      <Column>
        <template #body="slotProps">
          <Button
            icon="pi pi-trash"
            v-if="props.showDeleteIcon"
            class="p-button-rounded p-button-warning"
            @click="confirmDeleteFriend(slotProps.data)"
          />
        </template>
      </Column>
      <template class="initialSize" #footer>
        <div class="initialSize">
          You have {{ friends ? friends.length : 0 }} friend(s)
        </div>
      </template>
    </DataTable>
    <div class="manage-btn p-mt-3">
      <router-link style="text-decoration: none" to="/friends">
        <Button
          label="Handle friends"
          class="p-d-block p-mx-auto p-button-raised friends-btn"
          v-if="!props.showDeleteIcon"
        />
      </router-link>
    </div>
  </div>
</template>

<script>
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import FriendHandler from "../modules/FriendHandler";

export default {
  name: "FriendList",
  components: { DataTable, Column },
  props: { showDeleteIcon: Boolean },

  async setup(props) {
    const { getFriends, friendError, friends } = FriendHandler();
    await getFriends();

    return { getFriends, friendError, friends, props };
  },
};
</script>

<style lang="scss">
/* .p-datatable .p-datatable-tbody > tr > td :first-child{
  width: 25%;
} */

//  p-datatable-body >>> .p-datatable {

//    .p-datatable-tbody > tr > td {
//     text-align: left;
//     display: block;
//     border: 0 none !important;
//     width: 100% !important;
//     float: left;
//     clear: left;
//   }
//     .p-column-title {
//       padding: 0.4rem;
//       min-width: 30%;
//       display: inline-block;
//       margin: -0.4em 1em -0.4em -0.4rem;
//       font-weight: bold;
//     }
//   }
// }

// ::v-deep(.p-datatable) {

//     .p-column-title {
//       padding: 0.4rem;
//       min-width: 30%;
//       display: inline-block;
//       margin: -0.4em 1em -0.4em -0.4rem;
//       font-weight: bold;
//     }
//   }
// }

//  .p-datatable-tbody > tr > td :first-child{
//     width: 75% !important;
//  }

//  .p-datatable-tbody > tr > td :nth-child(2){
//     width: 25% !important;
//  }

.friends-btn {
  background-color: #e2c3a6 !important;
  border: 0.2em solid #3b1704 !important;
  box-shadow: 0.2em 0.2em black !important;
  color: #3b1704 !important;
  font-family: "Press Start 2P", cursive !important;
  font-size: 0.7em !important;
  text-decoration: none !important;
}

.p-datatable-thead {
  display: none !important;
}

.initialSize {
  text-transform: initial;
}

.avatar-image {
  width: 40px;
  height: 40px;
  border: 2px solid #2c3e50;
  border-radius: 50px;
  margin: 0;
  /* z-index: 100; */
}
</style>
