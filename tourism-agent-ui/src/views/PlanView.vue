<template>
  <div class="plan-page">
    <app-header />

    <!-- Hero Header -->
    <header class="plan-hero">
      <div class="hero-bg"></div>
      <div class="container relative z-1">
        <div class="hero-content text-center">
          <h1 class="page-title">Plan Your <span>Perfect Trip</span></h1>
          <p class="page-subtitle">
            Let our AI agent handle the details while you focus on the memories.
          </p>
        </div>
      </div>
    </header>

    <div class="container mt-4 pb-20">
      <div class="plan-wrapper">
        <!-- Form Side -->
        <div v-if="!showPlanResult" class="form-container">
          <div class="form-card glass">
            <div class="form-header">
              <h2>Trip Details</h2>
              <p>Tell us where you want to go and what you love.</p>
            </div>

            <el-form :model="tripForm" label-position="top" class="modern-form">
              <div class="form-row">
                <el-form-item label="Where to?" required class="flex-1">
                  <el-input
                    v-model="tripForm.destination"
                    placeholder="e.g. Kyoto, Japan or Amalfi Coast, Italy"
                  >
                    <template #prefix
                      ><el-icon><Location /></el-icon
                    ></template>
                  </el-input>
                </el-form-item>
              </div>

              <div class="form-row">
                <el-form-item label="When are you going?" required class="flex-1">
                  <el-date-picker
                    v-model="tripForm.dateRange"
                    type="daterange"
                    range-separator="→"
                    start-placeholder="Start"
                    end-placeholder="End"
                    class="full-width"
                    :disabled-date="disablePastDates"
                  />
                </el-form-item>
              </div>

              <el-form-item label="What's your style?" required>
                <el-select
                  v-model="tripForm.travelStyle"
                  multiple
                  placeholder="Select all that apply"
                  class="full-width"
                  collapse-tags
                  collapse-tags-tooltip
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

              <el-form-item label="Any special requests?">
                <el-input
                  v-model="tripForm.specialRequests"
                  type="textarea"
                  placeholder="e.g. Child-friendly, wheelchair accessible, gluten-free options..."
                  :rows="3"
                />
              </el-form-item>

              <div class="form-actions mt-8">
                <button
                  class="btn-modern w-full"
                  @click.prevent="generatePlan"
                  :disabled="isGenerating"
                >
                  <span v-if="!isGenerating">Create My Custom Guide</span>
                  <span v-else>Crafting Your Adventure...</span>
                </button>
              </div>
            </el-form>
          </div>
        </div>

        <!-- Result Side -->
        <div v-else class="result-container animate-fade-in">
          <div class="result-header flex items-center justify-between mb-8">
            <div>
              <h2 class="text-3xl font-bold">Your Itinerary</h2>
              <p class="text-muted">{{ tripForm.destination }} • {{ formatDateRange }}</p>
            </div>
            <button class="btn-secondary" @click="showPlanResult = false">← Edit Details</button>
          </div>

          <div class="result-card glass">
            <travel-plan-viewer
              :loading="isGenerating"
              :html-content="planHtmlContent"
              :error="planError"
              @retry="generatePlan"
              @back="showPlanResult = false"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Location } from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import AppHeader from '../components/AppHeader.vue'
import TravelPlanViewer from '../components/TravelPlanViewer.vue'
import { generateTravelPlan } from '../services/api'

const tripForm = ref({
  destination: '',
  dateRange: [dayjs().add(1, 'day').toDate(), dayjs().add(5, 'day').toDate()],
  travelStyle: [],
  specialRequests: '',
})

const disablePastDates = (date) => date < dayjs().startOf('day')
const isGenerating = ref(false)
const showPlanResult = ref(false)
const planHtmlContent = ref('')
const planError = ref('')

const formatDateRange = computed(() => {
  if (!tripForm.value.dateRange?.[0] || !tripForm.value.dateRange?.[1]) return ''
  const start = dayjs(tripForm.value.dateRange[0]).format('MMM D')
  const end = dayjs(tripForm.value.dateRange[1]).format('MMM D, YYYY')
  return `${start} - ${end}`
})

const generatePlan = async () => {
  if (!tripForm.value.destination) return ElMessage.error('Please enter a destination')
  if (!tripForm.value.dateRange?.[0]) return ElMessage.error('Please select travel dates')
  if (tripForm.value.travelStyle.length === 0)
    return ElMessage.error('Please select at least one travel style')

  isGenerating.value = true
  showPlanResult.value = true
  planHtmlContent.value = ''
  planError.value = ''

  try {
    const html = await generateTravelPlan(
      tripForm.value.destination,
      `${dayjs(tripForm.value.dateRange[0]).format('YYYY-MM-DD')} to ${dayjs(tripForm.value.dateRange[1]).format('YYYY-MM-DD')}`,
      tripForm.value.travelStyle.join(', ') +
        (tripForm.value.specialRequests ? ', ' + tripForm.value.specialRequests : ''),
    )

    if (html?.trim()) {
      planHtmlContent.value = html
    } else {
      throw new Error('Received empty HTML from server')
    }
  } catch (error) {
    console.error(error)
    planError.value =
      error.response?.data?.details || error.message || 'An unexpected error occurred'
    ElMessage.error(planError.value)
  } finally {
    isGenerating.value = false
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
  padding: 120px 0 60px;
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

.text-muted {
  color: var(--text-muted);
}

.plan-wrapper {
  max-width: 900px;
  margin: 0 auto;
  position: relative;
  z-index: 10;
}

.container.pb-20 {
  padding-top: 40px;
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

.flex-1 {
  flex: 1;
}
.w-full {
  width: 100%;
}
.full-width {
  width: 100%;
}

.modern-form :deep(.el-form-item__label) {
  font-weight: 600;
  color: var(--text-main);
  margin-bottom: 0.5rem;
}

.result-header {
  flex-wrap: wrap;
  gap: 1.5rem;
}

.result-card {
  padding: clamp(1rem, 4vw, 2rem);
  border-radius: var(--radius-xl);
  min-height: 600px;
}

.animate-fade-in {
  animation: fadeIn 0.5s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 768px) {
  .plan-hero {
    padding: 100px 0 40px;
  }
  .form-row {
    flex-direction: column;
    gap: 0;
  }
  .form-header {
    margin-bottom: 1.5rem;
  }
  .result-header {
    flex-direction: column;
    align-items: flex-start;
  }
  .result-header .btn-secondary {
    width: 100%;
    justify-content: center;
    order: -1;
  }
}
</style>
