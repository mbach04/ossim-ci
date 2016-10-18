#!/bin/bash
if [ -z $OMAR_SCRIPT_DIR ]; then
  pushd `dirname $0` >/dev/null
  OMAR_SCRIPT_DIR=`pwd -P`
  popd >/dev/null
fi
echo "SCRIPT DIR ================ ${OMAR_SCRIPT_DIR}"

source ${OMAR_SCRIPT_DIR}/docker-common.sh

echo "Removing files..."

if [ -d "/data/s3" ] ; then
  sudo rm -rf /data/s3
fi

if [ -d "/data/jpip-cache " ] ; then
  sudo rm -rf /data/jpip-cache/*
fi

echo "Initializing directories"
mkdir -p /data/jpip-cache

if [ $? != 0 ] ; then
  echo "Unable to create directory /data/jpip-cache"
  exit 1
fi
echo "Getting login information"

eval `aws ecr get-login --region us-east-1`
if [ $? != 0 ] ; then
   echo "Unable to obtain login for containers"
   exit 1
fi

echo "Setting up containers...."
if [ -z $DOCKER_HOST_URL ] ; then
   echo "DOCKER HOST URL needs to be specified"
   exit 1
fi

echo DOCKER_HOST_URL=${DOCKER_HOST_URL}
docker-compose --file=docker-compose-no-build.yml down
for x in `docker images | grep /o2- | awk '{print \$3}'`; do docker rmi -f \$x; done
for x in `docker images | grep /tlv | awk '{print \$3}'`; do docker rmi -f \$x; done
docker-compose --file=docker-compose-no-build.yml up -d

