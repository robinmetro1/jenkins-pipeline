# Use an official Node.js runtime as a parent image
FROM node:9 as builder

# Set the working directory in the container
WORKDIR /app

# Copy package.json and package-lock.json to the container
COPY package*.json ./

# Install project dependencies
RUN npm install
RUN npm audit fix

# Copy the entire Angular app source code to the container
COPY . .

# Build the Angular app for production
RUN npm run build --prod

# Use a lightweight Nginx image as the final image for serving the Angular app
FROM nginx:alpine

# Copy the built Angular app files from the builder stage to the Nginx container
COPY --from=builder /app/dist /usr/share/nginx/html

# Expose port 80 for serving the Angular app
EXPOSE 80

# Start Nginx to serve the Angular app
CMD ["nginx", "-g", "daemon off;"]
