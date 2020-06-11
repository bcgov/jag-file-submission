# pull official base image
FROM node:13.12.0-alpine

# set working directory
WORKDIR /efiling-frontend

# add `/node_modules/.bin` to $PATH
ENV PATH ./node_modules/.bin:$PATH

RUN apk add --update \
  python \
  python-dev \
  py-pip \
  build-base \
  git \
  openssh-client \
&& pip install virtualenv \
&& rm -rf /var/cache/apk/*

# install dependencies
COPY package.json ./
COPY yarn.lock ./
RUN yarn install
RUN yarn global add react-scripts@3.4.1

# add app
COPY . ./

# start app
CMD ["yarn", "start"]
