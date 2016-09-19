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
pushd $OSSIM_DEV_HOME/rpmbuild/SOURCES >/dev/null
wget https://s3.amazonaws.com/ossimlabs/dependencies/source/OpenSceneGraph-3.2.1.zip -O OpenSceneGraph-3.2.1.zip
wget https://s3.amazonaws.com/ossimlabs/dependencies/source/0001-Cmake-fixes.patch -O 0001-Cmake-fixes.patch
wget https://s3.amazonaws.com/ossimlabs/dependencies/source/0001-Update-Aether-to-0.9.0.M3.patch -O 0001-Update-Aether-to-0.9.0.M3.patch
wget https://s3.amazonaws.com/ossimlabs/dependencies/source/0002-Fix-invalid-char.patch -O 0002-Fix-invalid-char.patch
wget https://s3.amazonaws.com/ossimlabs/dependencies/source/0003-Activate-osgviewerWX.patch -O 0003-Activate-osgviewerWX.patch
wget https://s3.amazonaws.com/ossimlabs/dependencies/source/0005-Use-generics-in-modello-generated-code.patch -O 0005-Use-generics-in-modello-generated-code.patch
wget https://s3.amazonaws.com/ossimlabs/dependencies/source/szip-2.1.tar.gz -O szip-2.1.tar.gz
wget https://s3.amazonaws.com/ossimlabs/dependencies/source/szip-linking.patch -O szip-linking.patch
wget https://s3.amazonaws.com/ossimlabs/dependencies/source/szip-opt.patch -O szip-opt.patch
wget https://s3.amazonaws.com/ossimlabs/dependencies/source/libjpeg12-turbo-1.4.2.tar.gz -O libjpeg12-turbo-1.4.2.tar.gz
wget https://s3.amazonaws.com/ossimlabs/dependencies/source/libjpeg12-turbo14-noinst.patch -O libjpeg12-turbo14-noinst.patch
wget https://s3.amazonaws.com/ossimlabs/dependencies/source/libjpeg12-turbo-header-files.patch -O libjpeg12-turbo-header-files.patch
wget https://s3.amazonaws.com/ossimlabs/dependencies/source/gpstk-2.3.src.tar.gz -O gpstk-2.3.src.tar.gz
wget https://s3.amazonaws.com/ossimlabs/dependencies/source/hdf5a-1.8.17.tar.gz -O hdf5a-1.8.17.tar.gz
popd >/dev/null

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

rpmdir=${OSSIM_DEV_HOME}/dependency-rpms
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
