<template>
  <div class="plan-page">
    <!-- Hero Header -->
    <header class="plan-hero">
      <div class="hero-bg"></div>
      <div class="container relative z-10 text-center">
        <h1 class="page-title animate-slide-down">Plan Your <span>Dream</span> Trip</h1>
        <p class="page-subtitle animate-slide-up">
          Tell us where you want to go, and our AI will build a perfect day-by-day itinerary for you.
        </p>
      </div>
    </header>

    <main class="container pb-20">
      <div class="plan-wrapper">
        <!-- Form Section -->
        <transition name="fade-slide">
          <div v-if="showForm" class="form-card glass">
            <div class="form-header">
              <h2>Trip Details</h2>
              <p>Help us customize your perfect journey</p>
            </div>

            <el-form :model="planForm" label-position="top" class="modern-form">
              <div class="form-row">
                <el-form-item label="Destination" class="flex-1">
                  <el-input
                    v-model="planForm.destination"
                    placeholder="e.g. Paris, France or Tokyo, Japan"
                    prefix-icon="Location"
                    class="premium-input"
                  />
                </el-form-item>

                <el-form-item label="Travel Dates" class="flex-1">
                  <el-date-picker
                    v-model="planForm.dates"
                    type="daterange"
                    range-separator="to"
                    start-placeholder="Start"
                    end-placeholder="End"
                    class="premium-date-picker w-full"
                  />
                </el-form-item>
              </div>

              <div class="form-row">
                <el-form-item label="What's your travel style?" class="w-full">
                  <el-select
                    v-model="planForm.travelStyle"
                    multiple
                    collapse-tags
                    collapse-tags-indicator
                    placeholder="Select Styles (Adventure, Beach, etc.)"
                    class="premium-select w-full"
                  >
                    <el-option label="History & Culture" value="History & Culture" />
                    <el-option label="Food Experience" value="Food Experience" />
                    <el-option label="Natural Scenery" value="Natural Scenery" />
                    <el-option label="Shopping" value="Shopping" />
                    <el-option label="Adventure" value="Adventure" />
                    <el-option label="Relaxation" value="Relaxation" />
                    <el-option label="Family Friendly" value="Family Friendly" />
                    <el-option label="Photography" value="Photography" />
                  </el-select>
                </el-form-item>
              </div>

              <div class="form-row">
                <el-form-item label="Special Requests" class="w-full">
                  <el-input
                    v-model="planForm.specialRequests"
                    type="textarea"
                    :rows="3"
                    placeholder="e.g. We prefer vegetarian food, need wheelchair access for some sites, and would love to see some hidden local spots..."
                    class="premium-textarea w-full"
                  />
                </el-form-item>
              </div>

              <div class="form-actions mt-8">
                <button
                  class="btn-modern primary full-width"
                  :disabled="loading"
                  @click.prevent="handleGeneratePlan"
                >
                  <template v-if="!loading">
                    Generate My Itinerary
                    <el-icon class="el-icon--right"><EditPen /></el-icon>
                  </template>
                  <template v-else>
                    <el-icon class="is-loading"><Loading /></el-icon>
                    Crafting your journey...
                  </template>
                </button>
              </div>
            </el-form>
          </div>
        </transition>

        <!-- Result Section -->
        <transition name="fade-slide">
          <div v-if="itinerary || loading" class="itinerary-result mt-12">
            <div class="result-card glass">
              <!-- Loading State -->
              <div v-if="loading" class="plan-loading-state py-12 text-center">
                <div class="loading-animation mb-6">🌍</div>
                <h3 class="loading-title">Designing your dream trip to {{ planForm.destination }}...</h3>
                <p class="text-muted mb-8">Our AI is searching for the best spots and optimizing your route.</p>
                
                <div class="loading-content-wrapper">
                  <el-progress 
                    :percentage="loadingProgress" 
                    :stroke-width="12" 
                    striped 
                    striped-flow 
                    class="modern-progress"
                  />
                  
                  <button class="btn-secondary mt-10" @click="cancelAndEdit">
                    <el-icon><EditPen /></el-icon> Edit Trip Details
                  </button>
                </div>
              </div>

              <!-- Final Itinerary -->
              <div v-else-if="itinerary" class="itinerary-content">
                <div class="result-header flex justify-between items-center mb-8">
                  <div>
                    <span class="badge">Your Personalized Guide</span>
                    <h2 class="mt-2">The Perfect Trip to {{ planForm.destination }}</h2>
                  </div>
                  <button class="btn-secondary" @click="editDetails">
                    <el-icon><EditPen /></el-icon> Edit Details
                  </button>
                </div>

                <TravelPlanViewer 
                :html-content="itinerary" 
                :loading="loading"
              />
              </div>
            </div>
          </div>
        </transition>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { Location, Calendar, Star, EditPen, Loading } from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import TravelPlanViewer from '../components/TravelPlanViewer.vue'
import { generateTravelPlan } from '../services/api'

const loading = ref(false)
const itinerary = ref(null)
const loadingProgress = ref(0)
const showForm = ref(true)

const planForm = reactive({
  destination: '',
  dates: [],
  travelStyle: [],
  specialRequests: ''
})

const editDetails = () => {
  itinerary.value = null
  showForm.value = true
}

const cancelAndEdit = () => {
  loading.value = false
  showForm.value = true
}

const handleGeneratePlan = async () => {
  if (!planForm.destination) {
    ElMessage.warning('Please enter a destination')
    return
  }
  if (!planForm.dates || planForm.dates.length === 0) {
    ElMessage.warning('Please select travel dates')
    return
  }
  if (planForm.travelStyle.length === 0) {
    ElMessage.warning('Please select at least one travel style')
    return
  }

  showForm.value = false
  loading.value = true
  itinerary.value = null
  loadingProgress.value = 10

  // Start progress animation
  const interval = setInterval(() => {
    if (loadingProgress.value < 90) {
      loadingProgress.value += Math.floor(Math.random() * 5)
    }
  }, 500)

  try {
    const startDate = planForm.dates[0] ? dayjs(planForm.dates[0]).format('YYYY-MM-DD') : ''
    const endDate = planForm.dates[1] ? dayjs(planForm.dates[1]).format('YYYY-MM-DD') : ''

    const preferences = planForm.travelStyle.join(', ') + 
      (planForm.specialRequests ? `. Special requests: ${planForm.specialRequests}` : '')

    const response = await generateTravelPlan(
      planForm.destination,
      `${startDate} to ${endDate}`,
      preferences
    )

    if (!loading.value) {
      clearInterval(interval)
      return
    }

    loadingProgress.value = 100
    itinerary.value = response
  } catch (error) {
    if (!loading.value) {
      clearInterval(interval)
      return
    }
    console.error('Plan generation failed:', error)
    ElMessage.error('Failed to generate plan. Please try again.')
    showForm.value = true // Restore form on error
  } finally {
    clearInterval(interval)
    loading.value = false
  }
}
</script>

<style scoped>
.plan-page {
  min-height: 100vh;
  background-color: var(--bg-main);
}

.plan-hero {
  position: relative;
  padding: 80px 0 40px;
  background-color: var(--bg-main);
  overflow: hidden;
}

.hero-bg {
  position: absolute;
  top: 0;
  right: 0;
  width: 50%;
  height: 100%;
  background: radial-gradient(circle at 70% 30%, var(--primary-light) 0%, transparent 70%);
  z-index: 0;
}

.page-title {
  font-size: clamp(2rem, 8vw, 3.5rem);
  margin-bottom: 1rem;
  color: var(--text-main);
}

.page-title span {
  background: linear-gradient(135deg, var(--primary) 0%, var(--secondary) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.page-subtitle {
  font-size: clamp(1rem, 3vw, 1.25rem);
  color: var(--text-muted);
  max-width: 600px;
  margin: 0 auto;
}

.plan-wrapper {
  max-width: 900px;
  margin: 0 auto;
  position: relative;
  z-index: 10;
}

.form-card {
  padding: clamp(1.5rem, 5vw, 3rem);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-xl);
  border: 1px solid var(--border-color);
  background: var(--bg-card);
  position: relative;
  z-index: 2;
}

.form-header {
  margin-bottom: 2.5rem;
}

.form-header h2 {
  font-size: clamp(1.5rem, 4vw, 1.75rem);
  margin-bottom: 0.5rem;
  color: var(--text-main);
}

.form-header p {
  color: var(--text-muted);
  font-size: 0.9375rem;
}

.form-row {
  display: flex;
  gap: 1.5rem;
  margin-bottom: 1rem;
}

.modern-form :deep(.el-form-item__label) {
  font-weight: 600;
  color: var(--text-main);
  margin-bottom: 0.5rem;
}

.premium-input :deep(.el-input__wrapper),
.premium-date-picker :deep(.el-input__wrapper),
.premium-date-picker.el-range-editor,
.premium-select :deep(.el-select__wrapper) {
  background-color: var(--bg-glass) !important;
  border: 1px solid var(--border-color);
  box-shadow: none !important;
  border-radius: var(--radius-full) !important;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  height: 52px;
  padding: 0 20px !important;
}

.premium-textarea :deep(.el-textarea__inner) {
  background-color: var(--bg-glass) !important;
  border: 1px solid var(--border-color);
  box-shadow: none !important;
  border-radius: var(--radius-xl);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  padding: 15px 20px;
  font-family: inherit;
}

.premium-input-lg :deep(.el-input__wrapper) {
  height: 56px;
}

.premium-input :deep(.el-input__wrapper):hover,
.premium-date-picker :deep(.el-range-editor.el-input__wrapper):hover,
.premium-select :deep(.el-select__wrapper):hover,
.premium-textarea :deep(.el-textarea__inner):hover {
  border-color: var(--primary);
  background-color: var(--bg-card);
}

.premium-input :deep(.el-input__wrapper.is-focus),
.premium-date-picker :deep(.el-range-editor.el-input__wrapper.is-active),
.premium-select :deep(.el-select__wrapper.is-focused),
.premium-textarea :deep(.el-textarea__inner:focus) {
  border-color: var(--primary);
  background-color: var(--bg-card);
  box-shadow: 0 0 0 1px var(--primary) !important;
}

.w-full {
  width: 100%;
}

.result-card {
  padding: clamp(1.5rem, 5vw, 3rem);
  border-radius: var(--radius-xl);
  min-height: 400px;
}

.loading-animation {
  font-size: 4rem;
  margin-bottom: 1.5rem;
  animation: floating 3s ease-in-out infinite;
}

.loading-title {
  font-size: 1.75rem;
  font-weight: 700;
  color: var(--text-main);
  margin-bottom: 0.5rem;
}

.loading-content-wrapper {
  max-width: 500px;
  margin: 0 auto;
}

.modern-progress {
  margin: 2rem 0;
}

.modern-progress :deep(.el-progress-bar__outer) {
  background-color: rgba(255, 255, 255, 0.1) !important;
  border-radius: var(--radius-full);
}

.modern-progress :deep(.el-progress-bar__inner) {
  background: linear-gradient(90deg, var(--primary), var(--secondary)) !important;
  box-shadow: 0 0 15px rgba(0, 206, 209, 0.4);
}

@keyframes floating {
  0% { transform: translateY(0px) rotate(0deg); }
  50% { transform: translateY(-20px) rotate(10deg); }
  100% { transform: translateY(0px) rotate(0deg); }
}

/* Transitions */
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

.fade-slide-enter-from {
  opacity: 0;
  transform: translateY(30px);
}

.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-30px);
}

.is-loading {
  animation: rotating 2s linear infinite;
}

@keyframes rotating {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

@media (max-width: 768px) {
  .plan-hero {
    padding: 100px 0 40px;
  }
  .form-row {
    flex-direction: column;
    gap: 0;
  }
}
</style>
