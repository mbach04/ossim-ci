#!/bin/bash 
# Set GoCD-specific environment:
pushd `dirname $0` >/dev/null
export SCRIPT_DIR=`pwd -P`
pushd $SCRIPT_DIR/../../.. >/dev/null
popd > /dev/null
popd >/dev/null

source $SCRIPT_DIR/ossim-env.sh

pushd $OSSIM_DEV_HOME/tlv/time_lapse
./gradlew assemble

