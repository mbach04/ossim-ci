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
if [ $? -ne 0 ]; then
 echo; echo "ERROR: Unable to buld Time Lapse Viewer."
 exit 1
fi

popd
