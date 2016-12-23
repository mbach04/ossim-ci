#!/bin/bash 
pushd `dirname $0` >/dev/null
OSSIMCI_SCRIPT_DIR=`pwd -P`
pushd $OSSIMCI_SCRIPT_DIR/../../.. >/dev/null
popd > /dev/null
popd >/dev/null

source $OSSIMCI_SCRIPT_DIR/ossim-env.sh

if [ "$BUILD_OMS" = "ON" ] ; then
   pushd $OSSIM_DEV_HOME/ossim-oms/joms/build_scripts/linux
   ./setup.sh
   if [ $? -ne 0 ]; then
     echo; echo "ERROR: Build SETUP for OSSIM."
     exit 1
   fi
   popd
fi

if [ -d $OSSIM_DEV_HOME/ossim ] ; then
   pushd $OSSIM_DEV_HOME/ossim/scripts
   ./build.sh
   if [ $? -ne 0 ]; then
     echo; echo "ERROR: Build failed for OSSIM."
     exit 1
   fi
   popd
else
   echo "ERROR: OSSIM module not found.  We at least need ossim module to build the baseline"
   exit 1
fi

pushd $OSSIM_DEV_HOME/ossim-oms/joms/build_scripts/linux
./build.sh
   if [ $? -ne 0 ]; then
     echo; echo "ERROR: Build failed for JOMS."
     exit 1
   fi
popd
