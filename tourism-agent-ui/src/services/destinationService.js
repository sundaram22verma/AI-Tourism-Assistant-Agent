import { destinations } from '../data/destinations';

export const destinationService = {
  /**
   * Get filtered, sorted and paginated destinations
   */
  getDestinations({ q, region, category, sortBy, page, size }) {
    let filtered = [...destinations];

    // Search query filter
    if (q) {
      const lowerQuery = q.toLowerCase();
      filtered = filtered.filter(d => 
        d.name.toLowerCase().includes(lowerQuery) || 
        d.location.toLowerCase().includes(lowerQuery)
      );
    }

    // Region filter
    if (region) {
      filtered = filtered.filter(d => d.region.toLowerCase() === region.toLowerCase());
    }

    // Category filter
    if (category) {
      filtered = filtered.filter(d => d.category.toLowerCase() === category.toLowerCase());
    }

    // Sorting
    if (sortBy === 'popularity') {
      filtered.sort((a, b) => b.reviewCount - a.reviewCount);
    } else if (sortBy === 'rating') {
      filtered.sort((a, b) => b.rating - a.rating);
    }

    const start = page * size;
    const end = Math.min(start + size, filtered.length);
    
    const items = filtered.slice(start, end);

    return {
      items,
      total: filtered.length
    };
  },

  /**
   * Get available filter options
   */
  getFilterOptions() {
    const regions = [...new Set(destinations.map(d => d.region))].sort();
    const categories = [...new Set(destinations.map(d => d.category))].sort();
    
    return {
      regions,
      categories
    };
  }
};
