node {
    env.WORKSPACE=pwd()
    stage("Checkout"){
        dir("omar"){
          git branch: 'dev', url: 'https://github.com/radiantbluetechnologies/omar.git'
        }
       dir("ossim-ci"){
          git branch: 'dev', url: 'https://github.com/ossimlabs/ossim-ci.git'
       }
    }
   stage("Download Artifacts"){
       dir("${env.WORKSPACE}"){
           step ([$class: 'CopyArtifact',
              projectName: 'ossim-dev',
              filter: "artifacts/*.jar",
              flatten: true])
       }
   }
   stage("Build"){
       sh """
         ${env.WORKSPACE}/ossim-ci/scripts/linux/oldmar-build.sh
       """
   }
   stage("Test"){
     echo "NEED TO ADD TESTS FOR THE INSTALLATION!!!!"
   }
   stage("Install"){
         sh "${env.WORKSPACE}/ossim-ci/scripts/linux/oldmar-install.sh"
   }
   stage("Archive"){
     dir("${env.WORKSPACE}"){
         sh "tar cvfz install.tgz install"
     }
     dir("${env.WORKSPACE}/artifacts"){
         sh "mv ${env.WORKSPACE}/install.tgz ."
     }
     archiveArtifacts 'artifacts/*'
  }
  stage("Clean Workspace"){
    step([$class: 'WsCleanup'])
  }
}