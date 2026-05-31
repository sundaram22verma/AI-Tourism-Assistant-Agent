<template>
  <div class="home-page">
    <app-header />

    <!-- Hero Section -->
    <section class="hero-section">
      <div class="hero-bg"></div>
      <div class="container hero-container">
        <div class="hero-content">
          <div class="badge">✨ AI-Powered Travel Assistant</div>
          <h1 class="hero-title">Your Next Adventure, <br /><span>Crafted by AI</span></h1>
          <p class="hero-subtitle">
            Experience travel like never before. Our intelligent agent creates personalized
            itineraries that match your soul, your style, and your schedule.
          </p>
          <div class="hero-actions">
            <button class="btn-modern" @click="createPlan">Start Planning Now</button>
            <router-link to="/discover" class="btn-secondary">Explore Destinations</router-link>
          </div>
        </div>
      </div>
    </section>

    <!-- Features Section -->
    <section class="features-section py-20">
      <div class="container">
        <div class="section-header text-center mb-8">
          <h2 class="section-title">Why Choose Travel Buddy?</h2>
          <p class="section-subtitle">The smartest way to plan your global escapes</p>
        </div>

        <div class="features-grid">
          <div class="feature-card">
            <div class="icon-box primary">
              <el-icon><calendar /></el-icon>
            </div>
            <h3>Smart Itineraries</h3>
            <p>AI-driven routes optimized for your time, interests, and local traffic patterns.</p>
          </div>

          <div class="feature-card">
            <div class="icon-box secondary">
              <el-icon><location /></el-icon>
            </div>
            <h3>Hidden Gems</h3>
            <p>Go beyond the tourist traps with recommendations only locals and AI know.</p>
          </div>

          <div class="feature-card">
            <div class="icon-box accent">
              <el-icon><food /></el-icon>
            </div>
            <h3>Local Flavors</h3>
            <p>Personalized food guides from street food to fine dining, curated just for you.</p>
          </div>

          <div class="feature-card">
            <div class="icon-box success">
              <el-icon><suitcase /></el-icon>
            </div>
            <h3>One-Click Magic</h3>
            <p>Generate a complete, downloadable guide in seconds. Ready when you are.</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Popular Destinations -->
    <section class="popular-destinations py-20">
      <div class="container">
        <div class="flex items-center justify-between mb-8">
          <div>
            <h2 class="section-title">Trending Destinations</h2>
            <p class="text-muted">Hand-picked by our AI community</p>
          </div>
          <router-link to="/discover" class="text-primary font-bold">View All →</router-link>
        </div>

        <div class="destinations-grid">
          <div
            v-for="destination in popularDestinations"
            :key="destination.id"
            class="destination-card"
          >
            <div class="dest-image">
              <img :src="destination.image" :alt="destination.name" />
              <div class="dest-overlay">
                <div class="dest-info">
                  <h3>{{ destination.name }}</h3>
                  <p>{{ destination.description }}</p>
                  <button class="btn-glass" @click="exploreDestination(destination)">
                    Explore
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA Section -->
    <section class="cta-section py-20">
      <div class="container">
        <div class="cta-card glass">
          <h2>Ready to explore the world?</h2>
          <p>Join thousands of travelers using AI to make every trip unforgettable.</p>
          <button class="btn-modern" @click="createPlan">Get Started for Free</button>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Calendar, Location, Food, Suitcase } from '@element-plus/icons-vue'
import AppHeader from '../components/AppHeader.vue'
import { useRouter } from 'vue-router'
import { destinations } from '../data/destinations'
const router = useRouter()

const createPlan = () => router.push('/plan')

const exploreDestination = (destination) => {
  console.log('Explore destination:', destination.name)
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
.section-title {
  font-size: clamp(2rem, 5vw, 2.5rem);
  margin-bottom: 1rem;
}

.section-subtitle {
  font-size: clamp(1rem, 3vw, 1.125rem);
  color: var(--text-muted);
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
}

.feature-card {
  background: var(--bg-card);
  padding: 2rem;
  border-radius: var(--radius-lg);
  border: 1px solid var(--border-color);
  transition: var(--transition-base);
}

.feature-card:hover {
  transform: translateY(-10px);
  box-shadow: var(--shadow-xl);
  border-color: var(--primary-light);
}

.icon-box {
  width: 56px;
  height: 56px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.75rem;
  margin-bottom: 1.5rem;
}

.icon-box.primary {
  background: var(--primary-light);
  color: var(--primary);
}
.icon-box.secondary {
  background: rgba(255, 215, 0, 0.1);
  color: var(--secondary);
}
.icon-box.accent {
  background: rgba(245, 158, 11, 0.1);
  color: #f59e0b;
}
.icon-box.success {
  background: rgba(16, 185, 129, 0.1);
  color: var(--success);
}

.feature-card h3 {
  font-size: 1.25rem;
  margin-bottom: 1rem;
}

.feature-card p {
  color: var(--text-muted);
  line-height: 1.6;
  font-size: 0.9375rem;
}

/* Destinations */
.destinations-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
}

.destination-card {
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
  border: 1px solid rgba(255, 255, 255, 0.4);
  color: white;
  padding: 0.5rem 1.25rem;
  border-radius: var(--radius-full);
  font-weight: 600;
  transition: var(--transition-base);
}

.btn-glass:hover {
  background: white;
  color: var(--text-main);
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

/* Animations */
@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
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
