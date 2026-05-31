<template>
  <div id="app">
    <router-view v-slot="{ Component }">
      <transition name="page-slide" mode="out-in">
        <component :is="Component" />
      </transition>
    </router-view>
  </div>
</template>

<style>
/* Global styles */
#app {
  width: 100%;
  min-height: 100vh;
}

/* Modern Page Transition */
.page-slide-enter-active,
.page-slide-leave-active {
  transition:
    opacity 0.4s cubic-bezier(0.4, 0, 0.2, 1),
    transform 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.page-slide-enter-from {
  opacity: 0;
  transform: translateY(20px);
}

.page-slide-leave-to {
  opacity: 0;
  transform: translateY(-20px);
}
</style>
