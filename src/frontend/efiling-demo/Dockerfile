FROM nginx:alpine

RUN apk -U upgrade
RUN apk update && apk add --upgrade --no-cache libexpat libxml2 libxslt curl

RUN rm -rf /usr/share/nginx/html/
# Static export produced by `npm run build` on the host before docker build
COPY out/ /usr/share/nginx/html
WORKDIR /usr/share/nginx/html

RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d

COPY ./env.sh .
COPY ./.env.template .env

RUN chmod g+rwx /var/cache/nginx /var/run /var/log/nginx
RUN chmod g+rwx /usr/share/nginx/html/

RUN apk add --no-cache bash
RUN chmod g+rwx env.sh

CMD ["/bin/bash", "-c", "/usr/share/nginx/html/env.sh && nginx -g \"daemon off;\""]
