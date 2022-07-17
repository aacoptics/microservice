FROM harbor.aacoptics.com/bcproject/ngnix:1.23.0-alpine AS base

FROM harbor.aacoptics.com/bcproject/node:14.20.0 AS build
WORKDIR /src
COPY . .

WORKDIR "/src/microservice-vue"
RUN ls -al
RUN npm config set registry https://nexus.aacoptics.com/repository/npm-all/
RUN npm install
RUN npm run build

FROM base AS final
COPY --from=build /src/microservice-vue/dist /usr/share/nginx/html/