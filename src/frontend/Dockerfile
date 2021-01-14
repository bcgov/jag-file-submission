# pull official base image
FROM node:14-alpine as build

ARG SERVICE_NAME

WORKDIR /app

COPY ./${SERVICE_NAME}/package.json ./${SERVICE_NAME}/yarn.lock ./

# add `/node_modules/.bin` to $PATH
#ENV PATH ./node_modules/.bin:$PATH

RUN yarn install --production

COPY ./${SERVICE_NAME} .

RUN npm run build

# This contains some environment variables needed for the environment to work.
# TODO: This copies the env into 2 different files because the code looks at for the full name of the env.  This is not optimal.  Can we change the route to just be env.js instead of labeling it?
COPY ./${SERVICE_NAME}/env.js ./build/demo-env.js
COPY ./${SERVICE_NAME}/env.js ./build/frontend-env.js

#############################################################################################
###                                 PRODUCTION IMAGE                                      ###
#############################################################################################
FROM nginx:1.19-alpine
RUN rm -rf /usr/share/nginx/html/
COPY --from=build /app/build /usr/share/nginx/html
WORKDIR /usr/share/nginx/html

RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d
CMD ["nginx", "-g", "daemon off;"]
