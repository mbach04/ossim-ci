#!/bin/bash 
# Set GoCD-specific environment:
pushd `dirname $0` >/dev/null
export SCRIPT_DIR=`pwd -P`
pushd $SCRIPT_DIR/../../.. >/dev/null
popd > /dev/null
popd >/dev/null

source $SCRIPT_DIR/ossim-env.sh

if [ ! -f $OSSIM_INSTALL_PREFIX/share/java/joms-$OSSIM_VERSION.jar ]; then
   echo "ERROR: $OSSIM_INSTALL_PREFIX/share/java/joms-$OSSIM_VERSION.jar is not found in the install artifact and OMAR can't be built."
   exit 1
fi
# make sure the joms jar is in the local maven repo
mvn install:install-file -Dfile=$OSSIM_INSTALL_PREFIX/share/java/joms-$OSSIM_VERSION.jar -DgroupId=org.ossim -DartifactId=joms -Dversion=$OSSIM_VERSION -Dpackaging=jar

if [ $? -ne 0 ]; then
 echo; echo "ERROR: MVN command failed for joms."
 exit 1
fi

export OMAR_DEV_HOME=$OSSIM_DEV_HOME/omar

#mv $OSSIM_INSTALL_PREFIX ossim-install

#if [ $? -ne 0 ]; then
# echo; echo "ERROR: MVN install failed for joms."
# exit 1
#fi

$OMAR_DEV_HOME/build_scripts/linux/build.sh
if [ $? -ne 0 ]; then
 echo; echo "ERROR: OMAR failed to build."
 exit 1
fi



