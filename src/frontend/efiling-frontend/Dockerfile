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
# There's apparently a bug in the nginx container image where the "return 301" seems to default the root of the html files into the /etc/nginx/html/ folder.
# bug here: https://github.com/nginx-proxy/nginx-proxy/issues/1246
# To get around this issue I've placed the code to be served in this /etc/nginx/html/ folder.
COPY --from=build /app/build /etc/nginx/html/efilinghub
WORKDIR /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
CMD ["nginx", "-g", "daemon off;"]
