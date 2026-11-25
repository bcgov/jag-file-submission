# pull official base image
FROM node:18-alpine AS build

WORKDIR /app

COPY ./package.json ./package-lock.json ./.npmrc ./

ENV NODE_ENV=production

# --include=dev: NODE_ENV=production above would skip devDependencies (sass, @types/*) that next build needs
RUN npm ci --include=dev
COPY . .

RUN npm run build

#############################################################################################
###                                 PRODUCTION IMAGE                                      ###
#############################################################################################
FROM nginx:alpine

RUN apk -U upgrade
RUN apk update && apk add --upgrade --no-cache libexpat libxml2 libxslt curl

RUN rm -rf /usr/share/nginx/html/
COPY --from=build /app/out /etc/nginx/html/efilinghub

WORKDIR /etc/nginx/html/efilinghub

RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY ./env.sh .
COPY .env.template .env

# CMD ["nginx", "-g", "daemon off;"]
RUN chmod g+rwx /var/cache/nginx /var/run /var/log/nginx
RUN chmod g+rwx /etc/nginx/html/efilinghub

# Add bash
RUN apk add --no-cache bash

# Make our shell script executable
RUN chmod g+rwx env.sh

CMD ["/bin/bash", "-c", "/etc/nginx/html/efilinghub/env.sh && nginx -g \"daemon off;\""]
