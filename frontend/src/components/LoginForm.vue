<template>
  <div class="login-container p-d-flex p-my-auto p-jc-center">
    <div class="p-fluid p-mt-3">
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
      <div class="p-text-center p-invalid" v-if="state.incorrect">
        <p>Incorrect email or password!</p>
      </div>
      <div class="p-text-center p-mb-4">
        <p class="p-mb-0">Don't have an account?</p>
        <p class="p-mt-0">
          Register <router-link to="/register">here</router-link>!
        </p>
      </div>
      <div class="btn-group">
        <Button class="p-ripple p-mb-4" @click="login" label="Login" />
        <Button class="p-ripple" label="Login as guest" />
      </div>
    </div>
  </div>
</template>

<script>
import { reactive } from "vue";

const form = reactive({
  email: null,
  password: null,
});
const state = reactive({
  incorrect: false,
})

export default {
  name: "LoginForm",

  setup() {

    async function login() {
      let result = await (
        await fetch("http://localhost:8070/api/auth/login", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(form),
        })
      ).json();

      if (result.error) {
        state.incorrect = true;
      }
      else {
        // window.location.href = '/lobby';
      }
    }

    return {
      form,
      state,
      login,
    };
  },
};
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
  height: 53%;
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
