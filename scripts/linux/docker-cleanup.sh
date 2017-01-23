#!/bin/bash

ITEMS_TO_REMOVE=$(docker ps -q -f status=exited)
if [ "${ITEMS_TO_REMOVE}" != "" ] ; then
   docker rm ${ITEMS_TO_REMOVE}
fi

ITEMS_TO_REMOVE=$(docker volume ls -qf dangling=true)
if [ "${ITEMS_TO_REMOVE}" != "" ] ; then
   docker volume rm ${ITEMS_TO_REMOVE}
fi

ITEMS_TO_REMOVE=$(docker images --filter "dangling=true" -q --no-trunc)
if [ "${ITEMS_TO_REMOVE}" != "" ] ; then
   docker rmi ${ITEMS_TO_REMOVE}
fi
 
