<template>
    <div></div>
</template>

<script>
import UserHandler from "@/modules/UserHandler.js"

export default {
    name: "CheckLoggedIn",
    async setup() {
        const {startApp} = UserHandler();

        await startApp();
    }
}
</script>