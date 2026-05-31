<template>
  <header :class="['app-header', { 'is-scrolled': isScrolled, 'mobile-menu-active': mobileMenuOpen }]">
    <div class="container header-container">
      <div class="logo">
        <router-link to="/" class="flex items-center gap-2">
          <img src="../assets/logo-icon.svg" alt="Travel Buddy Logo" class="logo-img" />
          <span class="logo-text">Travel<span>Buddy</span></span>
        </router-link>
      </div>

      <nav class="main-nav hidden-mobile">
        <router-link to="/" class="nav-item">Home</router-link>
        <router-link to="/discover" class="nav-item">Discover</router-link>
        <router-link to="/plan" class="nav-item">Plan Trip</router-link>
      </nav>

      <div class="user-actions flex items-center gap-4">
        <button
          class="theme-toggle"
          @click="toggleTheme"
          :title="isDark ? 'Switch to Light Mode' : 'Switch to Dark Mode'"
        >
          <el-icon v-if="isDark"><Sunny /></el-icon>
          <el-icon v-else><Moon /></el-icon>
        </button>

        <button
          class="mobile-menu-toggle hidden-desktop"
          @click="toggleMobileMenu"
          :aria-expanded="mobileMenuOpen"
        >
          <el-icon v-if="!mobileMenuOpen"><Menu /></el-icon>
          <el-icon v-else><Close /></el-icon>
        </button>
      </div>
    </div>

    <!-- Mobile Navigation Drawer -->
    <transition name="drawer-slide">
      <div v-if="mobileMenuOpen" class="mobile-drawer glass">
        <nav class="mobile-nav">
          <router-link to="/" class="mobile-nav-item" @click="closeMobileMenu">
            <span class="nav-label">Home</span>
            <el-icon><ArrowRight /></el-icon>
          </router-link>
          <router-link to="/discover" class="mobile-nav-item" @click="closeMobileMenu">
            <span class="nav-label">Discover</span>
            <el-icon><ArrowRight /></el-icon>
          </router-link>
          <router-link to="/plan" class="mobile-nav-item" @click="closeMobileMenu">
            <span class="nav-label">Plan Trip</span>
            <el-icon><ArrowRight /></el-icon>
          </router-link>
        </nav>
      </div>
    </transition>
  </header>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { Moon, Sunny, Menu, Close, ArrowRight } from '@element-plus/icons-vue'

const isDark = ref(false)
const isScrolled = ref(false)
const mobileMenuOpen = ref(false)
const route = useRoute()

const handleScroll = () => {
  isScrolled.value = window.scrollY > 10
}

const toggleTheme = () => {
  isDark.value = !isDark.value
  const theme = isDark.value ? 'dark' : 'light'
  document.documentElement.setAttribute('data-theme', theme)
  if (isDark.value) {
    document.documentElement.classList.add('dark')
  } else {
    document.documentElement.classList.remove('dark')
  }
  localStorage.setItem('theme', theme)
}

const toggleMobileMenu = () => {
  mobileMenuOpen.value = !mobileMenuOpen.value
  if (mobileMenuOpen.value) {
    document.body.style.overflow = 'hidden'
  } else {
    document.body.style.overflow = ''
  }
}

const closeMobileMenu = () => {
  mobileMenuOpen.value = false
  document.body.style.overflow = ''
}

watch(
  () => route.path,
  () => {
    closeMobileMenu()
  },
)

onMounted(() => {
  const savedTheme = localStorage.getItem('theme') || 'light'
  isDark.value = savedTheme === 'dark'
  document.documentElement.setAttribute('data-theme', savedTheme)
  if (isDark.value) {
    document.documentElement.classList.add('dark')
  }
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.app-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 80px;
  z-index: 1000;
  display: flex;
  align-items: center;
  transition: all 0.3s ease;
  background: transparent;
  border-bottom: 1px solid transparent;
}

.header-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.app-header.is-scrolled,
.app-header.mobile-menu-active {
  height: 70px;
  background: var(--bg-card);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-bottom: 1px solid var(--border-color);
  box-shadow: var(--shadow-sm);
}

[data-theme='dark'] .app-header.is-scrolled,
[data-theme='dark'] .app-header.mobile-menu-active {
  background: rgba(15, 23, 42, 0.9);
}

.theme-toggle,
.mobile-menu-toggle {
  background: none;
  border: none;
  font-size: 1.5rem;
  color: var(--text-muted);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 44px;
  height: 44px;
  border-radius: var(--radius-md);
  transition: var(--transition-base);
}

.theme-toggle:hover,
.mobile-menu-toggle:hover {
  background: var(--primary-light);
  color: var(--primary);
}

.logo-text {
  font-size: 1.5rem;
  font-weight: 800;
  color: var(--text-main);
  letter-spacing: -0.02em;
  display: flex;
  align-items: center;
}

.logo-text span {
  color: var(--primary);
}

.logo-img {
  width: 40px;
  height: 40px;
  object-fit: contain;
}

.main-nav {
  display: flex;
  gap: 2rem;
  align-items: center;
}

.nav-item {
  color: var(--text-muted);
  font-weight: 600;
  font-size: 1rem;
  padding: 0.5rem 0;
  position: relative;
  transition: var(--transition-fast);
}

.nav-item:hover {
  color: var(--primary);
}

.nav-item.router-link-active {
  color: var(--text-main);
}

.nav-item.router-link-active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 3px;
  background: var(--primary);
  border-radius: var(--radius-full);
}

/* Mobile Drawer - Fixed Overlay */
.mobile-drawer {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  width: 100vw;
  height: 100vh;
  z-index: -1; /* Behind the header container but fixed */
  background: var(--bg-card);
  padding: 100px 1.5rem 2rem;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
}

.mobile-nav {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.mobile-nav-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1.25rem 1rem;
  background: var(--bg-main);
  border-radius: var(--radius-lg);
  color: var(--text-main);
  font-size: 1.25rem;
  font-weight: 700;
  transition: all 0.3s ease;
  border: 1px solid var(--border-color);
}

.mobile-nav-item:hover {
  background: var(--primary-light);
  color: var(--primary);
  transform: translateX(5px);
}

.mobile-nav-item.router-link-active {
  background: var(--primary);
  color: white;
  border-color: var(--primary);
}

/* Transitions */
.drawer-slide-enter-active,
.drawer-slide-leave-active {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.drawer-slide-enter-from,
.drawer-slide-leave-to {
  transform: translateY(-100%);
  opacity: 0;
}

@media (max-width: 768px) {
  .app-header {
    height: 70px;
  }
  .app-header.is-scrolled,
  .app-header.mobile-menu-active {
    height: 65px;
  }
  .mobile-drawer {
    padding-top: 85px;
  }
}
</style>
