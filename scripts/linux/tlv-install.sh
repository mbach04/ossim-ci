#!/bin/bash
pushd `dirname $0` >/dev/null
export SCRIPT_DIR=`pwd -P`
popd > /dev/null
popd >/dev/null

source $SCRIPT_DIR/ossim-env.sh

if [ -d $OSSIM_DEV_HOME/tlv ] ; then
   pushd $OSSIM_DEV_HOME/tlv/time_lapse/build/libs
   app=time_lapse
   for artifact in  `ls *.war` ; do
      install -p -m644 -D $artifact ${OSSIM_INSTALL_PREFIX}/share/omar/$app/$artifact
      if [ $? -ne 0 ];then
          echo "INSTALL ERROR: $app failed to install..."
          exit 1
      fi   
   done
fi

if [ $? -ne 0 ]; then
  echo; echo "ERROR: Failed installation for omar binaries."
  exit 1
fi
