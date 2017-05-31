
def notifyObj
node ("master"){
   env.WORKSPACE=pwd()
   env.MAKE_VERBOSE="${MAKE_VERBOSE}"
   try{
     stage("Checkout"){
         dir("ossim-ci"){
            git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/radiantbluetechnologies/ossim-ci.git'
         }
         dir("ossim-msp"){
            git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/radiantbluetechnologies/ossim-msp.git', credentialsId: 'cicdGithub'
         }
       notifyObj = load "${env.WORKSPACE}/ossim-ci/jenkins/pipeline/notify.groovy"
     }
     echo "${env.WORKSPACE}"
     stage("Download Artifacts"){
         dir("${env.WORKSPACE}"){
             step ([$class: "CopyArtifact",
                projectName: "ossim-${OSSIM_GIT_BRANCH}",
                filter: "artifacts/ossim-install.tgz",
                flatten: true])
             step ([$class: "CopyArtifact",
                projectName: "ossim-${OSSIM_GIT_BRANCH}",
                filter: "artifacts/msp-install.tgz",
                flatten: true])
         }
     }
     stage("Build"){
         sh """
          tar xvfz ossim-install.tgz
          tar xvfz msp-install.tgz
          
          """
         sh """
           rm -rf ${env.WORKSPACE}/build/CMakeCache.txt
           ${env.WORKSPACE}/ossim-msp/scripts/build.sh
         """
     }

     stage("Archive"){
       dir("${env.WORKSPACE}"){
           sh "tar cvfz ossim-msp-install.tgz install"
       }
       dir("${env.WORKSPACE}/artifacts"){
           sh "mv ${env.WORKSPACE}/ossim-msp-install.tgz ."
           sh "cp ${env.WORKSPACE}/ossim-msp/web/ossim-msp-service/build/libs/*.jar ."
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
