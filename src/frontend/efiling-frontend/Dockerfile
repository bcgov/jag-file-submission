# pull official base image
FROM node:14-alpine as build

WORKDIR /app

COPY ./package.json ./yarn.lock ./

ENV NODE_ENV=production

RUN yarn install

COPY . .

RUN yarn build

#############################################################################################
###                                 PRODUCTION IMAGE                                      ###
#############################################################################################
FROM nginx:1.19-alpine

RUN rm -rf /usr/share/nginx/html/
COPY --from=build /app/build /etc/nginx/html/efilinghub

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
