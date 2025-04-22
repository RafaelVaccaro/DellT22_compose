FROM node:20 as build
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build -- --configuration=production

FROM nginx:alpine
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html
COPY nginx/default.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
