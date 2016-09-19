#!/bin/bash 
# Set GoCD-specific environment:
pushd `dirname $0` >/dev/null
export SCRIPT_DIR=`pwd -P`
popd >/dev/null

source $SCRIPT_DIR/ossim-env.sh

if [ -d $OSSIM_DEV_HOME/tlv ] ; then
  pushd tlv
  rm -rf $OSSIM_DEV_HOME/tlv/plugins/network_specific
  
  if [ -d $OSSIM_DEV_HOME/ossimlabs-tlv ] ; then
    cp -R $OSSIM_DEV_HOME/ossimlabs-tlv/plugins/network_specific ./plugins/
    cat $OSSIM_DEV_HOME/ossimlabs-tlv/config.yml >> ./time_lapse/grails-app/conf/application.yml  
    pushd $OSSIM_DEV_HOME/tlv/time_lapse
    
    # force a jar artifact 
    sed -i '/apply plugin:"war"/d' build.gradle

    ./gradlew assemble
    if [ $? -ne 0 ]; then
     echo; echo "ERROR: Unable to buld Time Lapse Viewer."
     exit 1
    fi
    popd
  fi
  popd
else
  echo; echo "ERROR: TLV repo not present and will not be build"
  exit 1
fi
