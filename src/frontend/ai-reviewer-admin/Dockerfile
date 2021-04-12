# pull official base image
FROM node:14-alpine as build

ARG SERVICE_NAME

WORKDIR /app

COPY ./package.json ./yarn.lock ./

RUN yarn install --production=true

COPY . .

RUN yarn build --production=true

#############################################################################################
###                                 PRODUCTION IMAGE                                      ###
#############################################################################################
FROM nginx:1.19-alpine

RUN rm -rf /usr/share/nginx/html/
COPY --from=build /app/build /usr/share/nginx/html
WORKDIR /usr/share/nginx/html

RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d

COPY ./env.sh .
COPY ./.env.template .env

# CMD ["nginx", "-g", "daemon off;"]
RUN chmod g+rwx /var/cache/nginx /var/run /var/log/nginx
RUN chmod g+rwx /usr/share/nginx/html/

# Add bash
RUN apk add --no-cache bash

# Make our shell script executable
RUN chmod g+rwx env.sh

CMD ["/bin/bash", "-c", "/usr/share/nginx/html/env.sh && nginx -g \"daemon off;\""]
