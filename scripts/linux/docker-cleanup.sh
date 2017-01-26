#!/bin/bash
DOCKER_COMMAND="sudo docker"
ITEMS_TO_REMOVE=$(docker ps -q -f status=exited)
if [ "${ITEMS_TO_REMOVE}" != "" ] ; then
   $DOCKER_COMMAND rm ${ITEMS_TO_REMOVE}
fi

ITEMS_TO_REMOVE=$(docker volume ls -qf dangling=true)
if [ "${ITEMS_TO_REMOVE}" != "" ] ; then
   $DOCKER_COMMAND volume rm ${ITEMS_TO_REMOVE}
fi

ITEMS_TO_REMOVE=$(docker images --filter "dangling=true" -q --no-trunc)
if [ "${ITEMS_TO_REMOVE}" != "" ] ; then
   $DOCKER_COMMAND rmi ${ITEMS_TO_REMOVE}
fi
 
ITEMS_TO_REMOVE=$(sudo docker images | grep "<none>" | awk '{print $3}')
if [ "${ITEMS_TO_REMOVE}" != "" ] ; then
   $DOCKER_COMMAND rmi ${ITEMS_TO_REMOVE}
fi