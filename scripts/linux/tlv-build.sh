#!/bin/bash 
# Set GoCD-specific environment:
pushd `dirname $0` >/dev/null
export SCRIPT_DIR=`pwd -P`
popd >/dev/null

source $SCRIPT_DIR/ossim-env.sh

if [ -d $OSSIM_DEV_HOME/tlv ] ; then
  pushd $OSSIM_DEV_HOME/tlv
  rm -rf $OSSIM_DEV_HOME/tlv/plugins/network_specific
  
  if [ -d $OSSIM_DEV_HOME/ossimlabs-tlv ] ; then
    cp -R $OSSIM_DEV_HOME/ossimlabs-tlv/plugins/network_specific ./plugins/ 
   
 
    # add documentation
    mv $OSSIM_DEV_HOME/tlv/docs/tlv.html $OSSIM_DEV_HOME/tlv/time_lapse/grails-app/conf/


    # force a jar artifact 
    sed -i '/apply plugin:"war"/d' build.gradle


    # take into account the web proxy path
    sed -i -e 's/\/assets/\/tlv\/assets/g' $OSSIM_DEV_HOME/tlv/time_lapse/grails-app/conf/tlv.html
    sed -i -e 's/${request.contextPath}/\/tlv/g' $OSSIM_DEV_HOME/tlv/time_lapse/grails-app/views/index.gsp


    pushd $OSSIM_DEV_HOME/tlv/time_lapse
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
