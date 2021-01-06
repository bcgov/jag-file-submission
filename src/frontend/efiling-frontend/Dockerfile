FROM node:14 AS build

WORKDIR /opt/web

# Install dependencies
COPY package.json package-lock.json ./
RUN npm install

ENV PATH="./node_modules/.bin:$PATH"

COPY package*.json ./
RUN npm ci

COPY public ./public
COPY src ./src

RUN npm run build

FROM nginx:1.19-alpine
COPY --from=build /opt/web/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf.template
