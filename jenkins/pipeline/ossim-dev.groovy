node {
   env.WORKSPACE=pwd()
   stage("Checkout"){
       dir("ossim"){
          git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim.git'
       }
       dir("ossim-video"){
          git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-video.git'
       }
       dir("ossim-planet"){
          git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-planet.git'
       }
       dir("ossim-gui"){
          git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-gui.git'
       }
       dir("ossim-oms"){
          git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-oms.git'
       }
       dir("ossim-plugins"){
          git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-plugins.git'
       }
       dir("ossim-wms"){
          git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-wms.git'
       }
       dir("ossim-ci"){
          git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-ci.git'
       }
       dir("ossim-private"){
          git branch: "${OSSIM_GIT_BRANCH}", url: 'git@ossim-private.github.com:radiantbluetechnologies/ossim-private.git'
       }
//       dir("cucumber-oc2s"){
//          git branch: 'dev', url: 'git@cucumber-oc2s.github.com:radiantbluetechnologies/cucumber-oc2s.git'
//       }
   }
   echo "${env.WORKSPACE}"
   stage("Download Artifacts"){
       dir("${env.WORKSPACE}"){
           step ([$class: 'CopyArtifact',
              projectName: 'ossim-kakadu-dev',
              filter: "artifacts/kakadu.tgz",
              flatten: true])
       }
   }
   stage("Build"){
       sh """
        tar xvfz kakadu.tgz
        """
       sh """
         rm -rf ${env.WORKSPACE}/build/CMakeCache.txt
         ${env.WORKSPACE}/ossim-ci/scripts/linux/ossim-build.sh
         ${env.WORKSPACE}/ossim-ci/scripts/linux/ossim-install.sh
       """
   }

   stage("Archive"){
     dir("${env.WORKSPACE}"){
         sh "tar cvfz install.tgz install"
     }
     dir("${env.WORKSPACE}/artifacts"){
         sh "mv ${env.WORKSPACE}/install.tgz ."
         sh "cp ${env.WORKSPACE}/ossim-oms/lib/joms-*.jar ."
     }
     archiveArtifacts 'artifacts/*'
  }

  stage("Clean Workspace"){
    step([$class: 'WsCleanup'])
  }
}
