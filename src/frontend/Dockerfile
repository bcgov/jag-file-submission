# pull official base image
FROM node:14-alpine as build

ARG SERVICE_NAME

WORKDIR /app

COPY ./${SERVICE_NAME}/package.json ./${SERVICE_NAME}/yarn.lock ./

RUN yarn install --production

COPY ./${SERVICE_NAME} .

RUN npm run build

#############################################################################################
###                                 PRODUCTION IMAGE                                      ###
#############################################################################################
FROM nginx:1.19-alpine

ARG SERVICE_NAME

RUN rm -rf /usr/share/nginx/html/
COPY --from=build /app/build /usr/share/nginx/html
WORKDIR /usr/share/nginx/html

RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d

COPY ./env.sh .
COPY ./${SERVICE_NAME}/.env.template .env

# CMD ["nginx", "-g", "daemon off;"]
RUN chmod g+rwx /var/cache/nginx /var/run /var/log/nginx
RUN chmod g+rwx /usr/share/nginx/html/

# Add bash
RUN apk add --no-cache bash

# Make our shell script executable
RUN chmod g+rwx env.sh

CMD ["/bin/bash", "-c", "/usr/share/nginx/html/env.sh && nginx -g \"daemon off;\""]
