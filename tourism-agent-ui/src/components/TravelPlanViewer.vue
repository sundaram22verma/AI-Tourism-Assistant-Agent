<template>
  <div class="travel-plan-viewer">
    <!-- Loading State -->
    <div v-if="loading" class="loading-state">
      <div class="animation-container">
        <div class="plane-wrapper">
          <el-icon class="plane-icon"><Promotion /></el-icon>
        </div>
        <div class="loading-bar-wrapper">
          <div class="loading-bar"></div>
        </div>
      </div>

      <div class="loading-content text-center">
        <h3 class="loading-msg">{{ currentLoadingMessage }}</h3>
        <p class="loading-sub">Our AI is orchestrating your perfect journey...</p>

        <div class="progress-steps mt-8">
          <div
            v-for="(step, index) in loadingSteps"
            :key="index"
            :class="[
              'step',
              { active: currentStepIndex === index, completed: currentStepIndex > index },
            ]"
          >
            <div class="step-circle">
              <el-icon v-if="currentStepIndex > index"><Check /></el-icon>
            </div>
            <span>{{ step }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Error State -->
    <div v-else-if="error" class="error-state">
      <div class="error-card">
        <el-icon class="error-icon"><Warning /></el-icon>
        <h2>Oops! Something went wrong</h2>
        <p>{{ error }}</p>
        <button class="btn-modern mt-4" @click="$emit('retry')">Try Again</button>
      </div>
    </div>

    <!-- Content State -->
    <div v-else-if="htmlContent" class="plan-ready animate-fade-in">
      <div class="plan-header-actions mb-8">
        <div class="flex gap-4">
          <button class="btn-action primary" @click="printPlan">
            <el-icon><Printer /></el-icon> Print Guide
          </button>
          <button class="btn-action secondary-action" @click="openInNewTab">
            <el-icon><Position /></el-icon> Full Screen
          </button>
        </div>
      </div>

      <div class="iframe-container">
        <iframe v-if="iframeUrl" :src="iframeUrl" class="plan-iframe" ref="htmlIframe"></iframe>
      </div>
    </div>

    <!-- Empty State -->
    <div v-else class="empty-state">
      <el-empty description="Start planning to see your itinerary here" />
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onBeforeUnmount } from 'vue'
import { Promotion, Printer, Position, Check, Warning } from '@element-plus/icons-vue'

const props = defineProps({
  loading: Boolean,
  htmlContent: String,
  error: String,
})

defineEmits(['retry', 'back'])

const htmlIframe = ref(null)
const iframeUrl = ref('')
const currentStepIndex = ref(0)
const currentLoadingMessage = ref('')

const loadingMessages = [
  'Scouting the best local spots...',
  'Syncing with travel trends...',
  'Optimizing your route...',
  'Curating food experiences...',
  'Almost ready for takeoff!',
]

const loadingSteps = ['Gathering', 'Analyzing', 'Planning', 'Finalizing']

let msgInterval, stepInterval

const startLoading = () => {
  stopLoading()
  let idx = 0
  currentLoadingMessage.value = loadingMessages[0]
  msgInterval = setInterval(() => {
    idx = (idx + 1) % loadingMessages.length
    currentLoadingMessage.value = loadingMessages[idx]
  }, 3000)

  currentStepIndex.value = 0
  stepInterval = setInterval(() => {
    if (currentStepIndex.value < 3) currentStepIndex.value++
  }, 10000)
}

const stopLoading = () => {
  clearInterval(msgInterval)
  clearInterval(stepInterval)
}

watch(
  () => props.loading,
  (val) => (val ? startLoading() : stopLoading()),
  { immediate: true },
)

watch(
  () => props.htmlContent,
  (content) => {
    if (content) {
      const blob = new Blob([content], { type: 'text/html' })
      if (iframeUrl.value.startsWith('blob:')) URL.revokeObjectURL(iframeUrl.value)
      iframeUrl.value = URL.createObjectURL(blob)
    }
  },
  { immediate: true },
)

onBeforeUnmount(() => {
  stopLoading()
  if (iframeUrl.value.startsWith('blob:')) URL.revokeObjectURL(iframeUrl.value)
})

const printPlan = () => htmlIframe.value?.contentWindow.print()
const openInNewTab = () => iframeUrl.value && window.open(iframeUrl.value, '_blank')
</script>

<style scoped>
.travel-plan-viewer {
  width: 100%;
}

/* Loading State */
.loading-state {
  padding: 4rem 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.animation-container {
  width: 100%;
  max-width: 400px;
  margin-bottom: 3rem;
}

.plane-wrapper {
  font-size: 3rem;
  color: var(--primary);
  margin-bottom: 1rem;
  animation: planeFly 2s infinite ease-in-out;
}

@keyframes planeFly {
  0%,
  100% {
    transform: translateY(0) rotate(0);
  }
  50% {
    transform: translateY(-20px) rotate(5deg);
  }
}

.loading-bar-wrapper {
  height: 6px;
  background: var(--primary-light);
  border-radius: var(--radius-full);
  overflow: hidden;
}

.loading-bar {
  height: 100%;
  width: 40%;
  background: linear-gradient(90deg, var(--primary), var(--secondary));
  border-radius: var(--radius-full);
  animation: loadingMove 1.5s infinite ease-in-out;
}

@keyframes loadingMove {
  0% {
    transform: translateX(-100%);
  }
  100% {
    transform: translateX(250%);
  }
}

.loading-msg {
  font-size: 1.5rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
}

.loading-sub {
  color: var(--text-muted);
}

.progress-steps {
  display: flex;
  justify-content: space-between;
  width: 100%;
  max-width: 500px;
  gap: 1rem;
}

.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  flex: 1;
  position: relative;
}

.step:not(:last-child)::after {
  content: '';
  position: absolute;
  top: 15px;
  left: 60%;
  width: 80%;
  height: 2px;
  background: var(--border-color);
}

.step-circle {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: var(--bg-main);
  border: 2px solid var(--border-color);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1;
  transition: var(--transition-base);
}

.step.active .step-circle {
  border-color: var(--primary);
  background: var(--primary-light);
  color: var(--primary);
  transform: scale(1.1);
}

.step.completed .step-circle {
  background: var(--success);
  border-color: var(--success);
  color: white;
}

.step span {
  font-size: 0.75rem;
  font-weight: 600;
  color: var(--text-muted);
}

/* Error State */
.error-card {
  text-align: center;
  padding: 3rem;
}

.error-icon {
  font-size: 4rem;
  color: var(--danger);
  margin-bottom: 1rem;
}

/* Plan State */
.plan-header-actions .flex {
  flex-wrap: wrap;
}

.btn-action {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  border-radius: var(--radius-md);
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  color: var(--text-main);
  font-weight: 600;
  transition: var(--transition-fast);
  font-size: 0.875rem;
  cursor: pointer;
}

.btn-action:hover {
  background: var(--border-color);
  border-color: var(--primary);
}

.btn-action.primary {
  background: var(--primary);
  color: white;
  border-color: var(--primary);
}

.btn-action.primary:hover {
  background: var(--primary-hover);
}

.btn-action.secondary-action {
  background: var(--bg-glass);
  backdrop-filter: blur(8px);
}

.iframe-container {
  margin-top: 1rem;
  border-radius: var(--radius-lg);
  overflow: hidden;
  box-shadow: var(--shadow-lg);
  border: 1px solid var(--border-color);
}

.plan-iframe {
  width: 100%;
  height: calc(100vh - 220px);
  min-height: 600px;
  border: none;
  background: white;
}

@media (max-width: 640px) {
  .progress-steps {
    flex-wrap: wrap;
    justify-content: center;
    gap: 1.5rem;
  }
  .step:not(:last-child)::after {
    display: none;
  }
  .step {
    flex: none;
    width: 80px;
  }
  .btn-action {
    flex: 1;
    justify-content: center;
  }
}
</style>
