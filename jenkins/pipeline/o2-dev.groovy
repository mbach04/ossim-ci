node {
   env.WORKSPACE=pwd()
   stage("Checkout"){
       dir("omar"){
          git branch: 'dev', url: 'https://github.com/ossimlabs/omar.git'
       }
       dir("ossim-ci"){
          git branch: 'dev-OCS626', url: 'https://github.com/ossimlabs/ossim-ci.git'
       }
   }
   echo "${env.WORKSPACE}"
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
         ${env.WORKSPACE}/ossim-ci/scripts/linux/o2-build.sh
         ${env.WORKSPACE}/ossim-ci/scripts/linux/o2-install.sh
       """
   }
   stage("Test"){
     echo "NEED TO ADD TESTS FOR THE INSTALLATION!!!!"
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