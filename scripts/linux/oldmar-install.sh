#!/bin/bash
pushd `dirname $0` >/dev/null
export SCRIPT_DIR=`pwd -P`
pushd $SCRIPT_DIR/../../.. >/dev/null
popd > /dev/null
popd >/dev/null

source $SCRIPT_DIR/ossim-env.sh

$OSSIM_DEV_HOME/omar/build_scripts/linux/install.sh

if [ $? -ne 0 ]; then
  echo; echo "ERROR: Failed installation for oldmar binaries."
  exit 1
fi
