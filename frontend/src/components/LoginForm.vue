<template>
  <div class="p-fluid p-mt-3">
    <div class="input p-field p-my-4">
      <span class="p-float-label">
        <InputText id="email" type="email" v-model="value.value1" />
        <label for="email">Email</label>
      </span>
    </div>
    <div class="input p-field p-my-4">
      <span class="p-float-label">
        <InputText id="password" type="password" v-model="value.value2" />
        <label for="password">Password</label>
      </span>
    </div>
    <div class="btn-group">
    <Button class="p-ripple p-mb-4" label="Login"/>
    <Button class="p-ripple" label="Login as guest"/>
    </div>
  </div>
</template>

<script>
import { reactive } from "vue";

const value = reactive({
  value1: null,
  value2: null,
});

export default {
  name: "LoginForm",

  setup() {
    return {
      value,
    };
  },
};
</script>

<style scoped>

.btn-group  {
  display: flex;
  flex-direction: column;
}


Button {
  align-self: center;
  height: 4em;
  width: 17em;
  background-color: #e2c3a6;
  border: 0.2em solid #3b1704;
  box-shadow: 0.2em 0.2em black;
  border-radius: 1em;
  color: #3b1704;
  font-family: "Press Start 2P", cursive;
}



</style>
