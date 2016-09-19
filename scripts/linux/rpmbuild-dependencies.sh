#!/bin/bash

pushd `dirname $0` >/dev/null
SCRIPT_DIR=$PWD
popd > /dev/null

source $SCRIPT_DIR/ossim-env.sh
source $SCRIPT_DIR/functions.sh

mkdir -p $OSSIM_DEV_HOME/rpmbuild/{BUILD,RPMS,SOURCES,SPECS,SRPMS}
if [ $? -ne 0 ]; then
  echo; echo "ERROR: Unable to create rpmbuild directories."
  exit 1
fi
cp $OSSIM_DEV_HOME/ossim-ci/rpm_specs/*.spec $OSSIM_DEV_HOME/rpmbuild/SPECS/
if [ $? -ne 0 ]; then
  echo; echo "ERROR: Unable to copy spec files from $OSSIM_DEV_HOME/ossim-ci/rpm_specs/*.spec to location $OSSIM_DEV_HOME/rpmbuild/SPECS."
  exit 1
fi

rpmbuild -ba --define "_topdir ${OSSIM_DEV_HOME}/rpmbuild" --define "BUILD_RELEASE 1" ${OSSIM_DEV_HOME}/rpmbuild/SPECS/hdf5a.spec
rpmbuild -ba --define "_topdir ${OSSIM_DEV_HOME}/rpmbuild" --define "BUILD_RELEASE 1" ${OSSIM_DEV_HOME}/rpmbuild/SPECS/libjpeg12-turbo.spec
rpmbuild -ba --define "_topdir ${OSSIM_DEV_HOME}/rpmbuild" --define "BUILD_RELEASE 1" ${OSSIM_DEV_HOME}/rpmbuild/SPECS/gpstk.spec
rpmbuild -ba --define "_topdir ${OSSIM_DEV_HOME}/rpmbuild" --define "BUILD_RELEASE 3" ${OSSIM_DEV_HOME}/rpmbuild/SPECS/szip.spec
rpmbuild -ba --define "_topdir ${OSSIM_DEV_HOME}/rpmbuild" --define "BUILD_RELEASE 1" ${OSSIM_DEV_HOME}/rpmbuild/SPECS/OpenSceneGraph.spec

getOsInfo os major_version minor_version os_arch

rpmdir=${OSSIM_DEV_HOME}/rpms
if [ -d "$rpmdir" ] ; then
  rm -rf $rpmdir/*
fi
mkdir -p $rpmdir

pushd ${OSSIM_DEV_HOME}/rpmbuild/RPMS >/dev/null
  mv `find ./${os_arch} -name "*.rpm"` $rpmdir/
popd > /dev/null

pushd ${OSSIM_DEV_HOME} >/dev/null
tar cvfz rpms.tgz rpms
popd > /dev/null
