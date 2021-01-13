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
        <div class="p-text-center p-invalid" v-if="!check.username">
          <p class="p-mx-1 p-my-1 p-text-left">
            Username already exists!
          </p>
        </div>
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
        <div class="p-text-center p-invalid" v-if="!check.email">
          <p class="p-mx-1 p-my-1 p-text-left">
            Email is not valid or already exists!
          </p>
        </div>
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
      <div class="p-text-center p-invalid" v-if="!check.password">
        <p class="p-mx-6 p-text-left p-my-0">
          Password must have atleast 8 characters, consist of uppercase,
          lowercase and numeric character!
        </p>
      </div>
      <div class="input p-field p-mt-5 p-mx-6">
        <span class="p-float-label">
          <InputText
            class="input"
            id="rePassword"
            type="password"
            v-model="state.rePassword"
          />
          <label class="input" for="password">Password (again)</label>
        </span>
      </div>
      <div class="p-text-center p-invalid" v-if="!check.passwordMatch">
        <p class="p-mx-6 p-text-left p-mt-0">Password doesn't match!</p>
      </div>
      <div class="p-formgroup-inline p-d-flex p-jc-center">
        <p class="p-mt-0">Choose your avatar:</p>
        <div class="p-d-flex p-jc-center">
          <img
            class="p-mr-3 avatar"
            v-bind:class="{ active: form.avatar === 1 }"
            @click="setAvatar(1)"
            src="../assets/avatar/1.png"
          />
          <img
            class="p-mr-3 avatar"
            v-bind:class="{ active: form.avatar === 2 }"
            @click="setAvatar(2)"
            src="../assets/avatar/2.png"
          />
          <img
            class="p-mr-3 avatar"
            v-bind:class="{ active: form.avatar === 3 }"
            @click="setAvatar(3)"
            src="../assets/avatar/3.png"
          />
          <img
            class="p-mr-3 avatar"
            v-bind:class="{ active: form.avatar === 4 }"
            @click="setAvatar(4)"
            src="../assets/avatar/4.png"
          />
          <img
            class="avatar"
            v-bind:class="{ active: form.avatar === 5 }"
            @click="setAvatar(5)"
            src="../assets/avatar/5.png"
          />
        </div>
      </div>
      <div class="p-text-center p-mb-4">
        <p class="p-mb-0">Already have an account?</p>
        <p class="p-mt-0">Login <router-link to="/">here</router-link>!</p>
      </div>
      <div class="btn-group">
        <Button class="p-ripple p-mb-4" @click="register" label="Register" />
      </div>
    </div>
  </div>
</template>

<script>
import { reactive } from "vue";
import { useRouter } from "vue-router";
import UserHandler from "@/modules/UserHandler";

const form = reactive({
  username: null,
  email: null,
  password: null,
  avatar: 1,
});

const state = reactive({
  rePassword: null,
  users: [],
});

const check = reactive({
  username: true,
  email: true,
  password: true,
  passwordMatch: true
});

export default {
  name: "RegisterPage",

  setup() {
    const {
      getUsername,
      usernameToCheck,
      getEmail,
      emailToCheck,
      createUser,
      isLoggedIn,
    } = UserHandler();
    const router = useRouter();

    function setAvatar(choice) {
      form.avatar = choice;
    }

    function passwordMatch() {
      if(form.password !== state.rePassword) {
        check.passwordMatch = false;
        return false;
      }

      check.passwordMatch = true;
      return true;
    }

    function checkPassword() {
      let regex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$/;
      if(!regex.exec(form.password)) {
        check.password = false;
        return false;
      }

      check.password = true;
      return true;
    }

    async function checkUsername() {
      usernameToCheck.value = null;
      console.log(usernameToCheck.value);
      await getUsername(form.username);
      if (usernameToCheck.value) {
        check.username = false;
        return false;
      }

      check.username = true;
      return true;
    }

    async function checkEmail() {
      let regex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      emailToCheck.value = null;
      await getEmail(form.email);
      console.log("checkEmail: ", regex.test(form.email));
      if (emailToCheck.value || !regex.test(form.email)) {
        check.email = false;
        return false;
      }

      check.email = true;
      return true;
    }

    async function register() {
      check.username = true;
      check.email = true;
      check.password = true;
      check.passwordMatch = true;

      if (await checkUsername() && await checkEmail() && checkPassword() && passwordMatch()) {
        await createUser(form);
        if (isLoggedIn.value) {
          router.push("/lobby");
        }
      }
    }

    return {
      form,
      state,
      register,
      setAvatar,
      passwordMatch,
      checkPassword,
      checkUsername,
      check,
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
  cursor: pointer;
}

.active {
  border: 2px solid green !important;
}

.input {
  color: #3b1704;
}

.register-container {
  height: 70%;
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
