# pull official base image
FROM node:13.12.0-alpine

ARG SERVICE_NAME

RUN npm install -g linklocal

COPY . .

WORKDIR /shared-components

RUN yarn install
RUN npm link ../efiling-frontend/node_modules/react

# set working directory
WORKDIR /${SERVICE_NAME}

# add `/node_modules/.bin` to $PATH
ENV PATH ./node_modules/.bin:$PATH

ENV REACT_APP_API_BASE_URL=http://localhost:8080

# install dependencies
RUN yarn install
RUN yarn global add react-scripts@3.4.1

RUN linklocal

# start app
CMD ["yarn", "start"]
