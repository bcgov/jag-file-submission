# pull official base image
FROM node:13.12.0-alpine as build

ARG SERVICE_NAME

WORKDIR /app

COPY ./${SERVICE_NAME}/package.json ./${SERVICE_NAME}/yarn.lock ./

# add `/node_modules/.bin` to $PATH
ENV PATH ./node_modules/.bin:$PATH

# install dependencies
RUN yarn install

COPY ./${SERVICE_NAME} .

RUN ls

# start app
CMD ["yarn", "start"]
