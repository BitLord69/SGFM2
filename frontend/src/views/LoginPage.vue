<template>
  <div class="login-form-container p-d-flex p-jc-center">
    <div class="p-fluid p-mt-3" v-if="!isLoggedIn">
      <div class="input p-field p-my-4">
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
      <div class="p-text-center p-invalid p-mb-3">
        <div>{{ loginError  || "&nbsp;" }}</div>
      </div>
      <div>
        <Button class="p-ripple p-mb-2" @click="performLogin" label="Login" />
      </div>
    </div>
  </div>
</template>

<script>
import { reactive } from "vue";
import { useRouter } from "vue-router"
import UserHandler from "@/modules/UserHandler.js"

const form = reactive({
  email: null,
  password: null,
});

export default {
  setup() {
    const router = useRouter();
    const { isLoggedIn, currentUser, login, userError, loginError } = UserHandler();

    if (isLoggedIn.value) {
      router.push('/lobby')
    }

    async function performLogin() {
      await login(form.email, form.password)
      if (isLoggedIn.value) router.push('/lobby')
    }

    return {
      form,
      login,
      isLoggedIn,
      currentUser,
      performLogin,
      userError, 
      loginError
    };
  },
};
</script>

<style scoped>

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

.login-form-container {
  margin-top: 13em;
  height: 15em;
  width: 20em;
  background-image: linear-gradient(#b99778, #e2c3a6);
  color: #3b1704;
  border: 0.2em solid #3b1704;
  box-shadow: 0.2em 0.2em black;
  border-radius: 1em;
  font-size: 120%;
  font-family: "Yanone Kaffeesatz", sans-serif;
}
</style>
