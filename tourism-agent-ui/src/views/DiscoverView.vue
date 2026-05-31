<template>
  <div class="discover-page">
    <app-header />

    <!-- Hero Section -->
    <header class="discover-hero">
      <div class="hero-bg"></div>
      <div class="container relative z-1">
        <div class="hero-content text-center">
          <h1 class="page-title">Discover Your <span>Next Story</span></h1>
          <p class="page-subtitle">
            Explore hand-picked destinations and unique experiences worldwide.
          </p>

          <div class="search-box-wrapper glass">
            <el-input
              v-model="searchQuery"
              placeholder="Where do you want to go?"
              class="modern-search"
              clearable
              @keyup.enter="handleSearch"
            >
              <template #prefix
                ><el-icon><Search /></el-icon
              ></template>
            </el-input>
            <button class="btn-modern" @click="handleSearch">Search</button>
          </div>
        </div>
      </div>
    </header>

    <!-- Filters -->
    <section class="filters-section py-4">
      <div class="container mt-4">
        <div class="filters-bar-wrapper">
          <div class="filters-bar glass">
          <div class="filter-group">
            <label>Region</label>
            <el-select v-model="filters.region" placeholder="All Regions" clearable>
              <el-option label="All Regions" value="" />
              <el-option 
                v-for="region in availableFilters.regions" 
                :key="region" 
                :label="region" 
                :value="region" 
              />
            </el-select>
          </div>
          <div class="filter-group">
            <label>Category</label>
            <el-select v-model="filters.category" placeholder="All Categories" clearable>
              <el-option label="All Categories" value="" />
              <el-option 
                v-for="cat in availableFilters.categories" 
                :key="cat" 
                :label="cat" 
                :value="cat" 
              />
            </el-select>
          </div>
          <div class="filter-group">
            <label>Sort By</label>
            <el-select v-model="sortBy" placeholder="Recommended">
              <el-option label="Recommended" value="recommended" />
              <el-option label="Popularity" value="popularity" />
              <el-option label="Rating" value="rating" />
            </el-select>
          </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Content -->
    <main class="container py-12" v-loading="loading">
      <div class="section-header mb-8 flex justify-between items-end">
        <div>
          <h2 class="text-3xl font-bold">Explore Destinations</h2>
          <p class="text-muted">Showing {{ destinations.length }} of {{ totalItems }} results</p>
        </div>
      </div>

      <div v-if="destinations.length > 0" class="destinations-grid mb-20">
        <div v-for="dest in destinations" :key="dest.id" class="dest-card">
          <div class="dest-card-image">
            <img :src="dest.image" :alt="dest.name" />
            <div class="dest-badge">{{ dest.rating }} ★</div>
          </div>
          <div class="dest-card-content">
            <div class="flex justify-between items-start mb-2">
               <span class="attr-tag">{{ dest.category }}</span>
               <span class="region-tag">{{ dest.region }}</span>
            </div>
            <h3>{{ dest.name }}</h3>
            <p class="location-text">
              <el-icon><Location /></el-icon> {{ dest.location }}
            </p>
            <p class="description">{{ dest.description }}</p>
            <div class="dest-card-footer">
              <div class="footer-info">
                <span class="reviews">{{ dest.reviewCount.toLocaleString() }} reviews</span>
                <div class="price" v-if="dest.price">From <span>₹{{ dest.price.toLocaleString() }}</span></div>
              </div>
              <button class="btn-text">Explore →</button>
            </div>
          </div>
        </div>
      </div>

      <div v-else-if="!loading" class="no-results py-20 text-center">
        <el-empty description="No destinations found matching your criteria" />
      </div>

      <div class="flex justify-center mt-20" v-if="totalItems > pageSize">
        <el-pagination
          background
          layout="prev, pager, next"
          :total="totalItems"
          :page-size="pageSize"
          v-model:current-page="currentPage"
          @current-change="handlePageChange"
          class="modern-pagination"
        />
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import AppHeader from '../components/AppHeader.vue'
import { Search, Location } from '@element-plus/icons-vue'
import { destinationService } from '../services/destinationService'

const route = useRoute()
const searchQuery = ref(route.query.q || '')
const filters = reactive({ region: '', category: '' })
const sortBy = ref('recommended')
const currentPage = ref(1)
const pageSize = ref(6)
const totalItems = ref(0)
const loading = ref(false)

const destinations = ref([])
const availableFilters = reactive({ regions: [], categories: [] })

const loadDestinations = async () => {
  loading.value = true
  try {
    const params = {
      query: searchQuery.value,
      region: filters.region,
      category: filters.category,
      sortBy: sortBy.value,
      page: currentPage.value - 1,
      size: pageSize.value
    }
    // Simulate network delay for better UX (optional, but keeps loading state visible briefly)
    await new Promise(resolve => setTimeout(resolve, 300))
    
    const response = destinationService.getDestinations(params)
    destinations.value = response.content
    totalItems.value = response.totalElements
  } catch (error) {
    console.error('Error loading destinations:', error)
  } finally {
    loading.value = false
  }
}

const loadFilters = async () => {
  try {
    const response = destinationService.getFilters()
    availableFilters.regions = response.regions
    availableFilters.categories = response.categories
  } catch (error) {
    console.error('Error loading filters:', error)
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadDestinations()
}

const handlePageChange = (page) => {
  currentPage.value = page
  loadDestinations()
  window.scrollTo({ top: 300, behavior: 'smooth' })
}

watch([() => filters.region, () => filters.category, sortBy], () => {
  currentPage.value = 1
  loadDestinations()
})

onMounted(() => {
  loadFilters()
  loadDestinations()
})
</script>

<style scoped>
.discover-page {
  background-color: var(--bg-main);
}

/* Hero */
.discover-hero {
  position: relative;
  padding: 140px 0 100px;
  background: var(--bg-main);
  overflow: hidden;
}

.hero-bg {
  position: absolute;
  top: -100px;
  right: -10%;
  width: 60%;
  height: 500px;
  background: radial-gradient(circle at center, var(--primary-light) 0%, transparent 70%);
  opacity: 0.5;
}

.search-box-wrapper {
  max-width: 700px;
  margin: 3rem auto 0;
  padding: 0.75rem;
  display: flex;
  gap: 1rem;
  border-radius: var(--radius-full);
  box-shadow: var(--shadow-xl);
}

.modern-search {
  flex: 1;
}

.modern-search :deep(.el-input__wrapper) {
  background: transparent !important;
  box-shadow: none !important;
  border: none !important;
}

/* Filters Bar */
.filters-bar {
  padding: 2rem;
  border-radius: var(--radius-xl);
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.5rem;
  align-items: flex-end;
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.filter-group label {
  font-size: 0.75rem;
  font-weight: 800;
  text-transform: uppercase;
  color: var(--text-muted);
  letter-spacing: 0.1em;
  margin-left: 0.25rem;
}

.filter-group :deep(.el-select) {
  width: 100%;
}

.filter-group :deep(.el-input__wrapper) {
  height: 48px;
  padding: 0 1.25rem;
  border-radius: var(--radius-lg);
  background-color: var(--bg-glass);
  box-shadow: 0 0 0 1px var(--border-color) inset !important;
  transition: var(--transition-base);
}

.filter-group :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px var(--primary) inset !important;
  background-color: var(--bg-card);
}

.filter-group :deep(.el-input__inner) {
  font-weight: 500;
  color: var(--text-main);
}

.filter-group :deep(.el-input__suffix) {
  display: flex;
  align-items: center;
}

/* Dest Cards */
.destinations-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
}

.dest-card {
  background: var(--bg-card);
  border-radius: var(--radius-xl);
  overflow: hidden;
  box-shadow: var(--shadow-md);
  transition: var(--transition-base);
}

.dest-card:hover {
  transform: translateY(-8px);
  box-shadow: var(--shadow-xl);
}

.dest-card-image {
  position: relative;
  height: 220px;
}

.dest-card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.dest-badge {
  position: absolute;
  top: 1rem;
  right: 1rem;
  background: var(--bg-card);
  color: var(--text-main);
  padding: 0.4rem 0.8rem;
  border-radius: var(--radius-full);
  font-weight: 700;
  font-size: 0.875rem;
  box-shadow: var(--shadow-sm);
}

.dest-card-content {
  padding: 1.5rem;
}

.attr-tag {
  font-size: 0.7rem;
  font-weight: 800;
  color: var(--primary);
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.region-tag {
  font-size: 0.7rem;
  font-weight: 600;
  color: var(--text-muted);
  background: var(--primary-light);
  padding: 0.1rem 0.5rem;
  border-radius: var(--radius-sm);
}

.dest-card-content h3 {
  font-size: 1.25rem;
  margin-bottom: 0.5rem;
}

.location-text {
  font-size: 0.875rem;
  color: var(--text-muted);
  display: flex;
  align-items: center;
  gap: 0.25rem;
  margin-bottom: 1rem;
}

.description {
  color: var(--text-muted);
  font-size: 0.875rem;
  margin-bottom: 1.25rem;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.dest-card-footer {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  border-top: 1px solid var(--border-color);
  padding-top: 1rem;
}

.footer-info {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.reviews {
  font-size: 0.75rem;
  color: var(--text-light);
}

.price {
  font-size: 0.75rem;
  color: var(--text-muted);
}

.price span {
  font-size: 1rem;
  font-weight: 800;
  color: var(--text-main);
}

.btn-text {
  color: var(--primary);
  font-weight: 700;
  font-size: 0.875rem;
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
}

.btn-text:hover {
  text-decoration: underline;
}

.modern-pagination {
  --el-pagination-button-width: 36px;
  --el-pagination-button-height: 36px;
}

@media (max-width: 480px) {
  .modern-pagination :deep(.el-pagination__jump),
  .modern-pagination :deep(.el-pagination__sizes),
  .modern-pagination :deep(.el-pagination__total) {
    display: none !important;
  }
  .modern-pagination :deep(.btn-prev),
  .modern-pagination :deep(.btn-next) {
    padding: 0 4px;
  }
  .modern-pagination :deep(.el-pager li) {
    min-width: 28px;
    margin: 0 2px;
  }
}

@media (max-width: 768px) {
  .discover-hero {
    padding: 100px 0 60px;
  }
  .search-box-wrapper {
    flex-direction: column;
    border-radius: var(--radius-lg);
    background: var(--bg-card);
  }
  .search-box-wrapper .btn-modern {
    width: 100%;
  }
  .filters-bar {
    padding: 1.5rem;
    grid-template-columns: 1fr;
    gap: 1.25rem;
  }
  .filter-group {
    max-width: none;
  }
  .destinations-grid {
    grid-template-columns: 1fr;
  }
  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
}
</style>
