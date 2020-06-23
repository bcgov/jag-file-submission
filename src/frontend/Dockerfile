# pull official base image
FROM node:13.12.0-alpine

ARG SERVICE_NAME

COPY . .

WORKDIR /shared-components

RUN yarn install

# set working directory
WORKDIR /${SERVICE_NAME}

# add `/node_modules/.bin` to $PATH
ENV PATH ./node_modules/.bin:$PATH

# install dependencies
RUN yarn install
RUN yarn global add react-scripts@3.4.1

# start app
CMD ["yarn", "start"]
