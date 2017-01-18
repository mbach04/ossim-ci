#!/bin/bash
docker rm $(docker ps -q -f status=exited)
docker volume rm $(docker volume ls -qf dangling=true)
docker rmi $(docker images --filter "dangling=true" -q --no-trunc)
