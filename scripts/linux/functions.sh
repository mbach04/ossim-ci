#!/bin/bash


##
# Example use: getOsInfo os major_version minor_version os_arch
#
function getOsInfo {
# Determine OS platform
#        UNAME=$(uname | tr "[:upper:]" "[:lower:]")
        local DISTRO=
        local majorVersion=
        local osArch=`uname -i`
        if [ -f /etc/redhat-release ] ; then
                DISTRO=`cat /etc/redhat-release | cut -d' ' -f1`
                if [[ $test =~ .*Red Hat.* ]] ; then
                   DISTRO="RedHat"
                fi
                majorVersion=`cat /etc/redhat-release | grep  -o "[0-9]*\.[0-9]*\.*[0-9]*" | cut -d'.' -f1`
                minorVersion=`cat /etc/redhat-release | grep  -o "[0-9]*\.[0-9]*\.*[0-9]*" | cut -d'.' -f2`
        fi
        eval "$1=${DISTRO}"
        eval "$2=${majorVersion}"
        eval "$3=${minorVersion}"
        eval "$4=${osArch}"
}

##
# Example use: getTimeStamp TIMESTAMP
#
function getTimeStamp {
        eval $1=`date +%Y-%m-%d-%H%M`
}
