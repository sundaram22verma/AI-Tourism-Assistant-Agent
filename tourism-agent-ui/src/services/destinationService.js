import { destinations } from '../data/destinations';

export const destinationService = {
  /**
   * Get filtered, sorted and paginated destinations
   */
  getDestinations({ query, region, category, sortBy, page, size }) {
    let filtered = [...destinations];

    // Search query filter
    if (query) {
      const lowerQuery = query.toLowerCase();
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
    } else {
      // Recommended - using the default order or some stable shuffle if needed
      // For now, we'll keep the default order as it was in the backend (using a seed for shuffle is overkill here)
    }

    const totalPages = Math.ceil(filtered.length / size);
    const start = page * size;
    const end = Math.min(start + size, filtered.length);
    
    const content = filtered.slice(start, end);

    return {
      content,
      totalElements: filtered.length,
      totalPages,
      size,
      number: page
    };
  },

  /**
   * Get available filter options
   */
  getFilters() {
    const regions = [...new Set(destinations.map(d => d.region))].sort();
    const categories = [...new Set(destinations.map(d => d.category))].sort();
    
    return {
      regions,
      categories
    };
  }
};
