#!/bin/bash 
# Set GoCD-specific environment:
pushd `dirname $0` >/dev/null
export SCRIPT_DIR=`pwd -P`
pushd $SCRIPT_DIR/../../.. >/dev/null
popd > /dev/null
popd >/dev/null

source $SCRIPT_DIR/ossim-env.sh

if [ "$BUILD_OMS" = "ON" ] ; then
   pushd $OSSIM_DEV_HOME/ossim-oms/joms/build_scripts/linux
   ./setup.sh
   popd
fi

if [ -d $OSSIM_DEV_HOME/ossim ] ; then
   pushd $OSSIM_DEV_HOME/ossim/scripts
   ./build.sh
   popd
else
   echo "ERROR: OSSIM module not found.  We at least need ossim module to build the baseline"
   exit 1
fi

pushd $OSSIM_DEV_HOME/ossim-oms/joms/build_scripts/linux
./build.sh
popd

if [ -d $OSSIM_DEV_HOME/oldomar ]; then
   pushd $OSSIM_DEV_HOME/oldomar/build_scripts/linux
   ./build.sh
   popd
fi

