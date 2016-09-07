node {
   env.WORKSPACE=pwd()
   env.KAKADU_VERSION="v7_5-01123C"
   env.JAVA_HOME="/usr/lib/jvm/java"
   dir("ossim-private"){
      git branch: 'dev', url: 'git@github.com:radiantbluetechnologies/ossim-private.git'
   }
   echo "${env.WORKSPACE}"
   stage("Build"){
       sh """
       mkdir -p ${env.WORKSPACE}/ossim-private/kakadu/${env.KAKADU_VERSION}/bin/Linux-x86-64-gcc
       pushd ${env.WORKSPACE}/ossim-private/kakadu/${env.KAKADU_VERSION}/coresys/make
       make -f Makefile-Linux-x86-64-gcc
       popd
       pushd ${env.WORKSPACE}/ossim-private/kakadu/${env.KAKADU_VERSION}/apps/make
       make -f Makefile-Linux-x86-64-gcc
       popd
       pushd ${env.WORKSPACE}/ossim-private/kakadu/${env.KAKADU_VERSION}/managed/make
       make -f Makefile-Linux-x86-64-gcc
       popd
       rm -rf ${env.WORKSPACE}/kakadu-${env.KAKADU_VERSION}
       pushd ${env.WORKSPACE}/ossim-private/kakadu
       cp -R ${env.KAKADU_VERSION} ${env.WORKSPACE}/kakadu-${env.KAKADU_VERSION}
       popd
       pushd ${env.WORKSPACE}
       tar cvfz kakadu.tgz kakadu-${env.KAKADU_VERSION}
       popd
       """
   }
//   stage("Install"){
//     sh "${env.WORKSPACE}/ossim/scripts/install.sh"
//   }
   stage("Test"){
     echo "NEED TO ADD TESTS FOR THE INSTALLATION!!!!"
   }
   
   stage("Archive"){
     dir("${env.WORKSPACE}"){
         archiveArtifacts "kakadu.tgz"
     }
   }
}