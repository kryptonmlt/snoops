# snoops
Manage containers in your machine through the browser. You can access logs and delete containers

## Instructions
- Windows:
  - docker run -rm  -p8082:8082 -v //var/run/docker.sock:/var/run/docker.sock
- Linux:
  - docker run -rm  -p8082:8082 -v /var/run/docker.sock:/var/run/docker.sock
- Env variables
  - --env CONTAINER_URL=".test.xyz"