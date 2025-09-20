# Build
FROM node:20 as build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# Serve
FROM nginx:1.27-alpine
COPY --from=build /app/build /usr/share/nginx/html
# Nginx mặc định phục vụ index.html; thêm history fallback nếu dùng react-router
RUN sed -i 's|try_files \$uri \$uri/ =404;|try_files \$uri /index.html;|g' /etc/nginx/conf.d/default.conf
EXPOSE 80
