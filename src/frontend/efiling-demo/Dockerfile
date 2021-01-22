# pull official base image
FROM node:14-alpine as build

WORKDIR /app

COPY ./package.json ./yarn.lock ./

RUN yarn install --production

COPY . .

RUN npm run build

# This contains some environment variables needed for the environment to work.
# TODO: This copies the env into 2 different files because the code looks at for the full name of the env.  This is not optimal.  Can we change the route to just be env.js instead of labeling it?
COPY ./env.js ./build/demo-env.js

#############################################################################################
###                                 PRODUCTION IMAGE                                      ###
#############################################################################################
FROM nginx:1.19-alpine
RUN rm -rf /usr/share/nginx/html/
COPY --from=build /app/build /etc/nginx/html/efilinghub
WORKDIR /etc/nginx/html/efilinghub
COPY nginx.conf /etc/nginx/conf.d/default.conf
CMD ["nginx", "-g", "daemon off;"]