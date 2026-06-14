<template>
  <footer class="app-footer">
    <div class="container">
      <div class="footer-grid">
        <!-- Brand Section -->
        <div class="footer-brand">
          <div class="logo">
            <img src="@/assets/logo-icon.svg" alt="TravelBuddy" class="logo-img" />
            <span class="logo-text">Travel<span>Buddy</span></span>
          </div>
          <p class="tagline">
            Your AI-powered companion for effortless and unforgettable journeys around the globe.
          </p>
          <div class="social-links">
            <a href="#" class="social-icon"><i class="fab fa-instagram"></i></a>
            <a href="#" class="social-icon"><i class="fab fa-twitter"></i></a>
            <a href="#" class="social-icon"><i class="fab fa-facebook"></i></a>
            <a href="#" class="social-icon"><i class="fab fa-youtube"></i></a>
          </div>
        </div>

        <!-- Quick Links -->
        <div class="footer-links">
          <h3>Explore</h3>
          <ul>
            <li><router-link to="/">Home</router-link></li>
            <li><router-link to="/discover">Discover</router-link></li>
            <li><router-link to="/plan">Plan Trip</router-link></li>
            <li><a href="#">Travel Guides</a></li>
          </ul>
        </div>

        <!-- Support -->
        <div class="footer-links">
          <h3>Support</h3>
          <ul>
            <li><a href="#">Help Center</a></li>
            <li><a href="#">Safety Tips</a></li>
            <li><a href="#">Privacy Policy</a></li>
            <li><a href="#">Terms of Service</a></li>
          </ul>
        </div>

        <!-- Contact -->
        <div class="footer-contact">
          <h3>Contact Us</h3>
          <p><el-icon><Message /></el-icon> hello@travelbuddy.ai</p>
          <p><el-icon><Location /></el-icon> Global Headquarters, SF</p>
          <div class="newsletter mt-4">
            <p class="text-sm mb-2">Subscribe to our newsletter</p>
            <div class="newsletter-input">
              <el-input placeholder="Your email" size="small">
                <template #append>Go</template>
              </el-input>
            </div>
          </div>
        </div>
      </div>

      <div class="footer-bottom">
        <p>&copy; {{ new Date().getFullYear() }} TravelBuddy AI. All rights reserved.</p>
        <div class="version">v1.0.4 Premium</div>
      </div>
    </div>
  </footer>
</template>

<script setup>
import { Message, Location } from '@element-plus/icons-vue'
</script>

<style scoped>
.app-footer {
  background-color: var(--bg-card);
  border-top: 1px solid var(--border-color);
  padding: 5rem 0 2rem;
  margin-top: 4rem;
  position: relative;
  z-index: 10;
}

.footer-grid {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1.5fr;
  gap: 4rem;
  margin-bottom: 4rem;
}

.footer-brand .logo {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1.5rem;
}

.logo-text {
  font-size: 1.5rem;
  font-weight: 800;
  color: var(--text-main);
  letter-spacing: -0.02em;
}

.logo-text span {
  color: var(--primary);
}

.logo-img {
  width: 32px;
  height: 32px;
}

.tagline {
  color: var(--text-muted);
  max-width: 300px;
  line-height: 1.6;
  margin-bottom: 2rem;
}

.social-links {
  display: flex;
  gap: 1rem;
}

.social-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: var(--bg-main);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-muted);
  transition: var(--transition-base);
  border: 1px solid var(--border-color);
}

.social-icon:hover {
  background: var(--primary);
  color: white;
  border-color: var(--primary);
  transform: translateY(-3px);
  box-shadow: var(--shadow-primary);
}

.footer-links h3, 
.footer-contact h3 {
  font-size: 1.125rem;
  font-weight: 700;
  margin-bottom: 1.5rem;
  color: var(--text-main);
}

.footer-links ul {
  list-style: none;
  padding: 0;
}

.footer-links li {
  margin-bottom: 0.75rem;
}

.footer-links a {
  color: var(--text-muted);
  text-decoration: none;
  transition: var(--transition-fast);
  font-weight: 500;
}

.footer-links a:hover {
  color: var(--primary);
  padding-left: 5px;
}

.footer-contact p {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  color: var(--text-muted);
  margin-bottom: 1rem;
}

.footer-bottom {
  padding-top: 2rem;
  border-top: 1px solid var(--border-color);
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: var(--text-light);
  font-size: 0.875rem;
}

@media (max-width: 1024px) {
  .footer-grid {
    grid-template-columns: 1fr 1fr;
    gap: 3rem;
  }
}

@media (max-width: 640px) {
  .app-footer {
    padding: 3rem 0 2rem;
  }
  .footer-grid {
    grid-template-columns: 1fr;
    gap: 2.5rem;
  }
  .footer-bottom {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }
}
</style>
