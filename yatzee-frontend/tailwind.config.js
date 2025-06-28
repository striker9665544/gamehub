// tailwind.config.js

/** @type {import('tailwindcss').Config} */
export default {
  // Add this safelist property
  safelist: [
    'bg-red-500',
    'bg-green-500',
    'bg-blue-500',
    'bg-yellow-500',
  ],
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      gridTemplateColumns: {
        '15': 'repeat(15, minmax(0, 1fr))',
      },
      gridTemplateRows: {
        '15': 'repeat(15, minmax(0, 1fr))',
      }
    },
  },
  plugins: [],
}