<template>
  <div class="login-container p-d-flex p-my-auto p-jc-center">
    <div class="p-fluid">
       <div class="input p-field p-my-4">
        <span class="p-float-label">
          <InputText
            class="input inputBox"
            id="username"
            type="text"
            v-model="form.username"
          />
          <label class="input" for="username">Username</label>
        </span>
      </div>
      <div class="input p-field p-my-4">
        <span class="p-float-label">
          <InputText
            class="input inputBox"
            id="email"
            type="email"
            v-model="form.email"
          />
          <label class="input" for="email">Email</label>
        </span>
      </div>
      <div class="input p-field p-mt-5">
        <span class="p-float-label">
          <InputText
            class="input"
            id="password"
            type="password"
            v-model="form.password"
          />
          <label class="input" for="password">Password</label>
        </span>
      </div>
      <!-- <div class="p-text-center p-invalid" v-if="state.incorrect">
        <p>Incorrect email or password!</p>
      </div> -->
      <div class="p-text-center p-mb-4">
        <p class="p-mb-0">Already have an account??</p>
        <p class="p-mt-0">
          Login <router-link to="/">here</router-link>!
        </p>
      </div>
      <div class="btn-group">
        <Button class="p-ripple p-mb-4" @click="register" label="Register" />
      </div>
    </div>
  </div>
</template>

<script>

import { reactive } from "vue";

  const form = reactive({
  username: null,
  email: null,
  password: null,
  avatar: 1,
});

export default {
  name: "RegisterForm",

  setup() {

    async function register() {
       let result = await (
        await fetch("http://localhost:8070/api/user/register", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(form),
        })
      ).json();

      if (result.error) {
        console.log("error i register:", result.error);
      }
      else {
        console.log("successfully registered! result:", result);
      }
    }
   
    return {
      form,
      register,
    };
  },
}
</script>

<style scoped>
.btn-group {
  display: flex;
  flex-direction: column;
}

button {
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

.input {
  color: #3b1704;
}

.inputBox {
  box-shadow: inset 0 0 3px #000000;
  border: 0;
}

.login-container {
  height: 45%;
  width: 40%;
  background-image: linear-gradient(#b99778, #e2c3a6);
  color: #3b1704;
  border: 0.2em solid #3b1704;
  box-shadow: 0.2em 0.2em black;
  border-radius: 1em;
  font-size: 120%;
  font-family: "Yanone Kaffeesatz", sans-serif;
}
</style>