# pull official base image
FROM node:18-alpine as build

WORKDIR /app

COPY ./package.json ./yarn.lock ./

RUN yarn install --production=true \
    && yarn add --dev react-app-rewired # this line makes sure that react-app-rewired scripts will be in the PATH. It can  
                                        # be removed when the react-app-rewired module is no longer required to the project.

COPY . .

RUN yarn build --production=true

#############################################################################################
###                                 PRODUCTION IMAGE                                      ###
#############################################################################################
FROM nginx:1.28.0-alpine

RUN apk update && apk add --upgrade --no-cache libexpat libxml2 libxslt

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
