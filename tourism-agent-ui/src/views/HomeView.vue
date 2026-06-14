<template>
  <div class="home-page">
    <!-- Hero Section -->
    <section class="hero-section">
      <div class="hero-bg"></div>
      <div class="container hero-container">
        <div class="hero-content">
          <div class="badge">✨ Your AI-Powered Travel Buddy</div>
          <h1 class="hero-title">
            Explore the World with <span>Intelligent</span> Precision
          </h1>
          <p class="hero-subtitle">
            Experience travel planning like never before. Our AI assistant crafts personalized
            itineraries, finds hidden gems, and manages your journey in real-time.
          </p>
          <div class="hero-actions">
            <button class="btn-modern primary" @click="createPlan">
              Plan Your Trip <el-icon class="el-icon--right"><Calendar /></el-icon>
            </button>
            <button class="btn-secondary" @click="exploreMore">
              Explore Destinations <el-icon class="el-icon--right"><Location /></el-icon>
            </button>
          </div>
        </div>
      </div>
    </section>

    <!-- Features Section -->
    <section class="features-section py-12">
      <div class="container">
        <div class="section-header text-center mb-8">
          <h2>Why Choose Travel Buddy?</h2>
          <p class="text-muted">Smart features for a seamless travel experience</p>
        </div>
        <div class="features-grid">
          <div class="feature-card glass">
            <div class="feature-icon"><el-icon><Calendar /></el-icon></div>
            <h3>Smart Itineraries</h3>
            <p>AI-generated plans tailored to your interests and schedule.</p>
          </div>
          <div class="feature-card glass">
            <div class="feature-icon"><el-icon><Location /></el-icon></div>
            <h3>Local Insights</h3>
            <p>Discover authentic experiences beyond the tourist traps.</p>
          </div>
          <div class="feature-card glass">
            <div class="feature-icon"><el-icon><Food /></el-icon></div>
            <h3>Food & Dining</h3>
            <p>Personalized recommendations for every palate.</p>
          </div>
          <div class="feature-card glass">
            <div class="feature-icon"><el-icon><Suitcase /></el-icon></div>
            <h3>Real-time Help</h3>
            <p>24/7 AI assistance throughout your entire journey.</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Popular Destinations -->
    <section class="destinations-section py-12">
      <div class="container">
        <div class="flex justify-between items-center mb-8">
          <h2>Popular Destinations</h2>
          <button class="btn-text" @click="exploreMore">View All</button>
        </div>
        <div class="destinations-grid">
          <div
            v-for="dest in popularDestinations"
            :key="dest.id"
            class="destination-card"
            @click="exploreDestination(dest)"
          >
            <div class="dest-image">
              <img :src="dest.image" :alt="dest.name" />
              <div class="dest-overlay">
                <div class="dest-info">
                  <h3>{{ dest.name }}</h3>
                  <p>{{ dest.region }}</p>
                  <button class="btn-modern btn-glass">Explore</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA Section -->
    <section class="cta-section py-12">
      <div class="container">
        <div class="cta-card">
          <h2>Ready for your next adventure?</h2>
          <p>Let our AI build your dream itinerary in seconds.</p>
          <button class="btn-modern" @click="createPlan">Start Planning Now</button>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Calendar, Location, Food, Suitcase } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { destinations } from '../data/destinations'

const router = useRouter()

const exploreMore = () => router.push('/discover')
const createPlan = () => router.push('/plan')

const exploreDestination = (destination) => {
  router.push({ path: '/discover', query: { q: destination.name } })
}

const popularDestinations = ref(destinations.slice(0, 3))
</script>

<style scoped>
.home-page {
  overflow-x: hidden;
}

/* Hero Section */
.hero-section {
  position: relative;
  min-height: 100vh;
  display: flex;
  align-items: center;
  padding-top: 80px;
  background-color: var(--bg-main);
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

.hero-container {
  position: relative;
  z-index: 1;
}

.hero-content {
  max-width: 700px;
}

.badge {
  display: inline-block;
  padding: 0.5rem 1rem;
  background: var(--primary-light);
  color: var(--primary);
  border-radius: var(--radius-full);
  font-weight: 700;
  font-size: 0.875rem;
  margin-bottom: 1.5rem;
  animation: slideDown 0.6s ease-out;
}

.hero-title {
  font-size: clamp(2.5rem, 8vw, 4.5rem);
  line-height: 1.1;
  margin-bottom: 1.5rem;
  animation: slideUp 0.8s ease-out;
}

.hero-title span {
  background: linear-gradient(135deg, var(--primary) 0%, var(--secondary) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.hero-subtitle {
  font-size: clamp(1rem, 3vw, 1.25rem);
  color: var(--text-muted);
  margin-bottom: 2.5rem;
  max-width: 600px;
  animation: slideUp 1s ease-out;
}

.hero-actions {
  display: flex;
  gap: 1rem;
  align-items: center;
  flex-wrap: wrap;
  animation: slideUp 1.2s ease-out;
}

.btn-secondary {
  font-weight: 700;
  color: var(--text-main);
  transition: var(--transition-base);
  padding: 0.5rem 1rem;
}

.btn-secondary:hover {
  color: var(--primary);
  transform: translateX(5px);
}

/* Features Section */
.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
}

.feature-card {
  padding: 2.5rem;
  border-radius: var(--radius-xl);
  transition: var(--transition-base);
}

.feature-card:hover {
  transform: translateY(-10px);
  border-color: var(--primary);
  box-shadow: var(--shadow-xl);
}

.feature-icon {
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, var(--primary) 0%, var(--secondary) 100%);
  color: white;
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  margin-bottom: 1.5rem;
}

.feature-card h3 {
  margin-bottom: 1rem;
}

/* Destinations Section */
.destinations-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
}

.destination-card {
  cursor: pointer;
  position: relative;
  height: 350px;
  border-radius: var(--radius-xl);
  overflow: hidden;
  box-shadow: var(--shadow-lg);
}

.dest-image {
  width: 100%;
  height: 100%;
}

.dest-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s ease;
}

.destination-card:hover img {
  transform: scale(1.1);
}

.dest-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.8) 0%, transparent 60%);
  display: flex;
  align-items: flex-end;
  padding: 1.5rem;
  opacity: 0.9;
  transition: var(--transition-base);
}

.dest-info h3 {
  color: white;
  font-size: 1.5rem;
  margin-bottom: 0.5rem;
}

.dest-info p {
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 1.25rem;
  font-size: 0.875rem;
}

.btn-glass {
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: white;
  padding: 0.5rem 1.25rem;
  font-size: 0.875rem;
}

.btn-glass:hover {
  background: white;
  color: var(--primary);
}

/* CTA Section */
.cta-card {
  padding: clamp(2rem, 10vw, 5rem);
  border-radius: var(--radius-xl);
  text-align: center;
  background: linear-gradient(135deg, var(--primary) 0%, var(--secondary) 100%);
  color: white;
}

.cta-card h2 {
  color: white;
  font-size: clamp(1.75rem, 5vw, 3rem);
  margin-bottom: 1rem;
}

.cta-card p {
  font-size: clamp(1rem, 3vw, 1.25rem);
  margin-bottom: 2.5rem;
  opacity: 0.9;
}

.cta-card .btn-modern {
  background: white;
  color: var(--primary);
}

@media (max-width: 1024px) {
  .hero-bg {
    width: 70%;
  }
}

@media (max-width: 768px) {
  .hero-section {
    padding-top: 100px;
    min-height: auto;
    padding-bottom: 4rem;
  }
  .hero-bg {
    width: 100%;
    opacity: 0.3;
    height: 50%;
  }
  .hero-actions {
    justify-content: center;
  }
  .feature-card {
    padding: 1.5rem;
  }
  .destinations-grid {
    grid-template-columns: 1fr;
  }
}
</style>
