# pull official base image
FROM node:13.12.0-alpine

ARG SERVICE_NAME

COPY . .

# set working directory
WORKDIR /${SERVICE_NAME}

# add `/node_modules/.bin` to $PATH
ENV PATH ./node_modules/.bin:$PATH

# install dependencies
RUN yarn install
RUN yarn global add react-scripts@3.4.1

# set working directory
WORKDIR /${SERVICE_NAME}

# start app
CMD ["yarn", "start"]
