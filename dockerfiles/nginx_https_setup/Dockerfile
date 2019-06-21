FROM nginx
RUN mkdir /https
COPY docker.local.crt /https
COPY docker.local.key /https
COPY default.conf /etc/nginx/conf.d

EXPOSE 80 443
CMD ["nginx", "-g", "daemon off;"]
