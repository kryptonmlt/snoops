# snoops
Manage containers in your machine through the browser. You can access logs and delete containers

## Instructions
- Windows:
  - docker run --rm  -p8082:8082 -v //var/run/docker.sock:/var/run/docker.sock kryptonmlt/snoops:latest
- Linux:
  - docker run --rm  -p8082:8082 -v /var/run/docker.sock:/var/run/docker.sock kryptonmlt/snoops:latest
- Env variables
  - --env CONTAINER_URL=".test.xyz"
  - This variable puts a url to make the containers clickable in case there is a service running on port 80
- More info can be found here: https://hub.docker.com/repository/docker/kryptonmlt/snoops/general
