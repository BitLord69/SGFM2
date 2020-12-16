<template>
  <div class="spinner">
    <ProgressSpinner
      style="width: 50px; height: 50px"
      strokeWidth="8"
      animationDuration="1.5s"
    />
  </div>
</template>

<script>
export default {
  name: "WaitingForPlayer",
};
</script>

<style>
.spinner {
  display: flex;
  justify-content: center;
}
</style>