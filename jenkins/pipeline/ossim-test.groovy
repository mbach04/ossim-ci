node{
   env.WORKSPACE=pwd()
   env.LD_LIBRARY_PATH="${env.WORKSPACE}/install/lib64:${env.LD_LIBRARY_PATH}"
   env.PATH="${env.WORKSPACE}/install/bin:${env.PATH}"
   
   echo "WORKSPACE       = ${env.WORKSPACE}"
   echo "LD_LIBRARY_PATH = ${env.LD_LIBRARY_PATH}"   
   echo "PATH            = ${env.PATH}"
   echo "ACCEPT_TESTS    = ${ACCEPT_TESTS}"

   stage("Checkout") {
       dir("ossim-ci") {
          git branch: "dev", url: 'https://github.com/ossimlabs/ossim-ci.git'
       }
   }

   stage("Download Artifacts") {
      step ([$class: 'CopyArtifact',
            projectName: 'ossim-dev',
            filter: "artifacts/install.tgz",
            flatten: true,
            target: "${env.WORKSPACE}"])
      sh """
        pushd ${env.WORKSPACE}
        tar xvfz install.tgz
        popd
      """
   }

   if (${ACCEPT_TESTS}) {
     stage("Accept Results")
     {
        sh """
        pushd ${env.WORKSPACE}/ossim-ci/scripts/linux
        ./ossim-test.sh accept
        popd
        """
    }
  }
  else {
     stage("Run Tests")
     {
        sh """
        pushd ${env.WORKSPACE}/ossim-ci/scripts/linux
        ./ossim-test.sh
        popd
        """
     }
  }
 
    
    stage("Publish Badges"){
//      ?
    }
    
    stage("Clean Workspace"){
      step([$class: 'WsCleanup'])
    }

}