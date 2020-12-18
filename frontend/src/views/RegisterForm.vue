<template>
  <div class="register-container p-d-flex p-my-auto p-jc-center">
    <div class="p-fluid p-mt-3">
       <div class="input p-field p-my-4 p-mx-6">
        <span class="p-float-label">
          <InputText
            class="input"
            id="username"
            type="text"
            v-model="form.username"
          />
          <label class="input" for="username">Username</label>
        </span>
      </div>
      <div class="input p-field p-my-4 p-mx-6">
        <span class="p-float-label">
          <InputText
            class="input"
            id="email"
            type="email"
            v-model="form.email"
          />
          <label class="input" for="email">Email</label>
        </span>
      </div>
      <div class="input p-field p-mt-5 p-mx-6">
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
      <div class="p-formgroup-inline p-d-flex p-jc-center">
        <p class="p-mt-0">Choose your avatar:</p>
        <div class="p-d-flex p-jc-center">
          <img class="p-mr-3 avatar active" src="../assets/avatar/1.png"/>
          <img class="p-mr-3 avatar" src="../assets/avatar/2.png"/>
          <img class="p-mr-3 avatar" src="../assets/avatar/3.png"/>
          <img class="p-mr-3 avatar" src="../assets/avatar/4.png"/>
          <img class="avatar" src="../assets/avatar/5.png"/>
        </div>
      </div>
      <div class="p-text-center p-mb-4">
        <p class="p-mb-0">Already have an account?</p>
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
  width: 17em !important;
  background-color: #e2c3a6;
  border: 0.2em solid #3b1704;
  box-shadow: 0.2em 0.2em black;
  border-radius: 1em;
  color: #3b1704;
  font-family: "Press Start 2P", cursive;
}

.avatar {
  width: 13%;
  border: 2px solid #3b1704;
  border-radius: 50%;
}

.active {
  border: 2px solid green !important;
}

.input {
  color: #3b1704;
}

.register-container {
  height: 65%;
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