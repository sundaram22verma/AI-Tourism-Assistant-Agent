<template>
  <div class="discover-page">
    <!-- Hero Section -->
    <header class="discover-hero">
      <div class="hero-bg"></div>
      <div class="container relative z-10">
        <h1 class="text-center animate-slide-up">Find Your Next <span>Adventure</span></h1>
        <p class="text-center text-muted mb-8 animate-slide-up" style="animation-delay: 0.1s">
          Browse through our curated collection of extraordinary destinations
        </p>

        <!-- Search Bar -->
        <div class="search-box-wrapper animate-slide-up" style="animation-delay: 0.2s">
          <div class="search-box glass">
            <el-icon class="search-icon"><Search /></el-icon>
            <input
              v-model="searchQuery"
              type="text"
              placeholder="Search by city, country, or landmark..."
              @keyup.enter="handleSearch"
            />
            <button class="btn-modern primary" @click="handleSearch">Search</button>
          </div>
        </div>
      </div>
    </header>

    <!-- Filters & Content -->
    <main class="discover-content py-12">
      <section class="filters-section">
        <div class="container">
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

      <!-- Destinations Grid -->
      <section class="results-section py-8">
        <div class="container">
          <div v-if="loading" class="loading-state py-12 text-center">
            <el-skeleton :rows="5" animated />
          </div>

          <div v-else-if="filteredDestinations.length === 0" class="empty-state py-12 text-center">
            <div class="empty-icon">🏝️</div>
            <h3>No destinations found</h3>
            <p class="text-muted">Try adjusting your filters or search query</p>
            <button class="btn-text mt-4" @click="resetFilters">Clear all filters</button>
          </div>

          <div v-else class="destinations-grid">
            <div
              v-for="dest in filteredDestinations"
              :key="dest.id"
              class="dest-card glass animate-fade-in"
            >
              <div class="card-image">
                <img :src="dest.image" :alt="dest.name" loading="lazy" />
                <div class="card-badges">
                  <span class="category-badge">{{ dest.category }}</span>
                  <span v-if="dest.rating" class="rating-badge">
                    <el-icon><Star /></el-icon> {{ dest.rating }}
                  </span>
                </div>
              </div>
              <div class="card-body">
                <h3>{{ dest.name }}</h3>
                <div class="location-text">
                  <el-icon><Location /></el-icon> {{ dest.location }}
                </div>
                <p class="description">{{ dest.description }}</p>
                <div class="dest-card-footer">
                  <div class="footer-info">
                    <span class="reviews">{{ dest.reviews }} reviews</span>
                    <span class="price">From <span>{{ dest.price }}</span></span>
                  </div>
                  <button class="btn-text" @click="exploreDetail(dest)">Details</button>
                </div>
              </div>
            </div>
          </div>

          <!-- Pagination -->
          <div v-if="totalDestinations > pageSize" class="pagination-wrapper mt-12 flex justify-center">
            <el-pagination
              v-model:current-page="currentPage"
              :page-size="pageSize"
              layout="prev, pager, next"
              :total="totalDestinations"
              background
              class="modern-pagination"
            />
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { Search, Location, Star } from '@element-plus/icons-vue'
import { destinationService } from '../services/destinationService'

const route = useRoute()

// State
const searchQuery = ref('')
const loading = ref(false)
const sortBy = ref('recommended')
const currentPage = ref(1)
const pageSize = ref(6)
const totalDestinations = ref(0)
const filteredDestinations = ref([])

const filters = reactive({
  region: '',
  category: ''
})

const availableFilters = reactive({
  regions: [],
  categories: []
})

// Actions
const fetchFilters = () => {
  const data = destinationService.getFilterOptions()
  availableFilters.regions = data.regions
  availableFilters.categories = data.categories
}

const handleSearch = () => {
  currentPage.value = 1
  fetchDestinations()
}

const resetFilters = () => {
  searchQuery.value = ''
  filters.region = ''
  filters.category = ''
  sortBy.value = 'recommended'
  handleSearch()
}

const fetchDestinations = async () => {
  loading.value = true
  try {
    const params = {
      q: searchQuery.value,
      region: filters.region,
      category: filters.category,
      sortBy: sortBy.value,
      page: currentPage.value - 1,
      size: pageSize.value
    }
    // Simulate network delay
    await new Promise(resolve => setTimeout(resolve, 300))
    
    const response = destinationService.getDestinations(params)
    filteredDestinations.value = response.items
    totalDestinations.value = response.total
  } catch (error) {
    console.error('Failed to fetch destinations:', error)
  } finally {
    loading.value = false
  }
}

const exploreDetail = (dest) => {
  console.log('Exploring:', dest.name)
}

// Watchers
watch([filters, sortBy, currentPage], () => {
  fetchDestinations()
})

onMounted(() => {
  fetchFilters()
  if (route.query.q) {
    searchQuery.value = route.query.q
  }
  fetchDestinations()
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
  right: -100px;
  width: 600px;
  height: 600px;
  background: radial-gradient(circle, var(--primary-light) 0%, transparent 70%);
  opacity: 0.6;
  z-index: 0;
}

.discover-hero h1 {
  font-size: clamp(2.5rem, 8vw, 4rem);
  margin-bottom: 1rem;
}

.discover-hero h1 span {
  background: linear-gradient(135deg, var(--primary) 0%, var(--secondary) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.search-box-wrapper {
  max-width: 800px;
  margin: 0 auto;
}

.search-box {
  display: flex;
  align-items: center;
  padding: 0.5rem 0.5rem 0.5rem 1.5rem;
  border-radius: var(--radius-full);
  box-shadow: var(--shadow-xl);
}

.search-icon {
  font-size: 1.25rem;
  color: var(--text-light);
  margin-right: 1rem;
}

.search-box input {
  flex: 1;
  border: none;
  background: none;
  font-size: 1.125rem;
  color: var(--text-main);
  outline: none;
  padding: 0.75rem 0;
}

/* Filters */
.filters-bar-wrapper {
  margin-top: -30px;
  position: relative;
  z-index: 20;
}

.filters-bar {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
  padding: 2rem;
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-lg);
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.filter-group label {
  font-size: 0.875rem;
  font-weight: 700;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

/* Results */
.destinations-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 2.5rem;
}

.dest-card {
  border-radius: var(--radius-xl);
  overflow: hidden;
  transition: var(--transition-base);
}

.dest-card:hover {
  transform: translateY(-10px);
  box-shadow: var(--shadow-xl);
  border-color: var(--primary);
}

.card-image {
  position: relative;
  height: 240px;
  overflow: hidden;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s ease;
}

.dest-card:hover .card-image img {
  transform: scale(1.1);
}

.card-badges {
  position: absolute;
  top: 1rem;
  left: 1rem;
  right: 1rem;
  display: flex;
  justify-content: space-between;
}

.category-badge {
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(4px);
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: var(--radius-full);
  font-size: 0.75rem;
  font-weight: 700;
}

.rating-badge {
  background: var(--secondary);
  color: var(--text-main);
  padding: 0.25rem 0.75rem;
  border-radius: var(--radius-full);
  font-size: 0.75rem;
  font-weight: 800;
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

.card-body {
  padding: 1.5rem;
}

.card-body h3 {
  margin-bottom: 0.5rem;
  font-size: 1.5rem;
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
}

.btn-text:hover {
  text-decoration: underline;
}

.modern-pagination {
  --el-pagination-button-width: 36px;
  --el-pagination-button-height: 36px;
}

@media (max-width: 768px) {
  .discover-hero {
    padding: 100px 0 60px;
  }
  .search-box {
    padding: 0.25rem 0.25rem 0.25rem 1rem;
  }
  .filters-bar {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  .destinations-grid {
    grid-template-columns: 1fr;
  }
}
</style>
