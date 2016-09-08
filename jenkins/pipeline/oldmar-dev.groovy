node {
    env.WORKSPACE=pwd()
    stage("Checkout"){
        dir("omar"){
          git branch: 'dev', url: 'https://github.com/radiantbluetechnologies/omar.git'
        }
       dir("ossim-ci"){
          git branch: 'dev-OCS626', url: 'https://github.com/ossimlabs/ossim-ci.git'
       }
    }
    echo "GIT_BRANCH ============= ${GIT_BRANCH}"
   stage("Download Artifacts"){
       dir("${env.WORKSPACE}/install/share/java"){
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
}