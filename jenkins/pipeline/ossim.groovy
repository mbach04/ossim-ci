
def notifyObj
node ("master"){
   env.WORKSPACE=pwd()
   env.MAKE_VERBOSE="${MAKE_VERBOSE}"
   try{
     stage("Checkout"){
         dir("ossim"){
            git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim.git'
         }
         dir("ossim-video"){
            git branch: "${ACCESSORY_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-video.git'
         }
         dir("ossim-planet"){
            git branch: "${ACCESSORY_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-planet.git'
         }
         dir("ossim-gui"){
            git branch: "${ACCESSORY_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-gui.git'
         }
         dir("ossim-oms"){
            git branch: "${ACCESSORY_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-oms.git'
         }
         dir("ossim-plugins"){
            git branch: "${OSSIM_PLUGINS_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-plugins.git'
         }
         dir("ossim-wms"){
            git branch: "${ACCESSORY_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-wms.git'
         }
         dir("ossim-ci"){
            git branch: "${ACCESSORY_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-ci.git'
         }
         //dir("ossim-private"){
         //   git branch: "${ACCESSORY_GIT_BRANCH}", url: 'git@ossim-private.github.com:radiantbluetechnologies/ossim-private.git'
         //}
       notifyObj = load "${env.WORKSPACE}/ossim-ci/jenkins/pipeline/notify.groovy"
     }
     echo "${env.WORKSPACE}"
     stage("Download Artifacts"){
         dir("${env.WORKSPACE}"){
             step ([$class: "CopyArtifact",
                projectName: "ossim-kakadu-${ACCESSORY_GIT_BRANCH}",
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
           sh "tar cvfz ossim-install.tgz install"
       }
       dir("${env.WORKSPACE}/artifacts"){
           sh "mv ${env.WORKSPACE}/ossim-install.tgz ."
           sh "cp ${env.WORKSPACE}/ossim-oms/lib/joms-*.jar ."
       }
       archiveArtifacts 'artifacts/*'
    }
  }
  catch(e)
  {
    println e
    currentBuild.result = "FAILED"
    notifyObj?.notifyFailed()
  }

  stage("Clean Workspace"){
    step([$class: 'WsCleanup'])
  }
}
