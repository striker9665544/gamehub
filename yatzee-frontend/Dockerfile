# Stage 1: Build the React app
FROM node:18-alpine AS builder

# Set working directory
WORKDIR /app

# Copy dependency definitions
COPY package*.json ./

# Install dependencies
RUN npm ci

# Copy source code
COPY . .

# Build the app
RUN npm run build

# --- ADD THIS LINE TO SEE THE BUILD OUTPUT ---
RUN echo "Listing the contents of the build output directory (/app/dist):" && ls -lR /app/dist

# Stage 2: Serve the app with Nginx
FROM nginx:alpine

# Remove default nginx static assets
RUN rm -rf /usr/share/nginx/html/*

# Copy built assets from builder
COPY --from=builder /app/dist /usr/share/nginx/html

# Copy custom nginx config (optional)
# COPY nginx.conf /etc/nginx/nginx.conf

# Expose default port
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]

