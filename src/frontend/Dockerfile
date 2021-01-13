# pull official base image
FROM node:14-alpine as build

ARG SERVICE_NAME

WORKDIR /app

COPY ./${SERVICE_NAME}/package.json ./${SERVICE_NAME}/yarn.lock ./

# add `/node_modules/.bin` to $PATH
#ENV PATH ./node_modules/.bin:$PATH

# install dependencies
# RUN yarn install --production
    # tail -f /dev/null && \
    # yarn build && \
    # apk add curl && \
    # curl -sfL https://install.goreleaser.com/github.com/tj/node-prune.sh | sh -s -- -b /usr/local/bin && \
    # /usr/local/bin/node-prune
RUN yarn install --production

COPY ./${SERVICE_NAME} .

RUN npm run build

#############################################################################################
###                                 PRODUCTION IMAGE                                      ###
#############################################################################################
FROM nginx:1.19-alpine
RUN rm -rf /usr/share/nginx/html/
COPY --from=build /app/build /usr/share/nginx/html
WORKDIR /usr/share/nginx/html

RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
