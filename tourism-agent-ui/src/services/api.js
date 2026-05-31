import axios from 'axios'

// Create axios instance
const apiClient = axios.create({
  baseURL: '/api/v1', // Use relative path for proxy
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 300000, 
})

// API function to generate travel plan
export async function generateTravelPlan(destination, travelDates, preferences) {
  // Build prompt text
  const prompt = `
Hello, I need you to create a detailed travel guide for ${destination}.
Please read the following requirements carefully and incorporate them into the final guide:
1. Core Travel Parameters:
 Expected Travel Dates: ${travelDates}
 Destination Name: ${destination}
2. Travel Style and Preferences:
 Interests/Preferences: ${preferences}
`

  try {
    // Call backend API
    const response = await apiClient.get('/travel/chat', {
      params: { content: prompt },
    })
    return response.data
  } catch (error) {
    console.error('Failed to generate travel plan:', error)
    throw error
  }
}

