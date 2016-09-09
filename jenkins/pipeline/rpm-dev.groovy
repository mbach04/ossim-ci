node {
   env.WORKSPACE=pwd()
   stage("Checkout"){
       dir("ossim-ci"){
          git branch: 'dev', url: 'https://github.com/ossimlabs/ossim-ci.git'
       }
   }
   echo "${env.WORKSPACE}"
   stage("Download Artifacts"){
     step ([$class: 'CopyArtifact',
        projectName: 'ossim-dev',
        filter: "artifacts/install.tgz",
        flatten: true,
        target: "${env.WORKSPACE}/ossim-install"])
     step ([$class: 'CopyArtifact',
        projectName: 'o2-dev',
        filter: "artifacts/install.tgz",
        flatten: true,
        target: "${env.WORKSPACE}/o2-install"])
     step ([$class: 'CopyArtifact',
        projectName: 'oldmar-dev',
        filter: "artifacts/install.tgz",
        flatten: true,
        target: "${env.WORKSPACE}/oldmar-install"])
   }
   stage("Build"){
    sh "${env.WORKSPACE}/ossim-ci/scripts/linux/rpmbuild-binary.sh"
   }
   
   stage("Archive"){
     dir("${env.WORKSPACE}/artifacts"){
         sh "mv ${env.WORKSPACE}/rpms.tgz ."
     }
     archiveArtifacts 'artifacts/*'
  }
  stage("Clean Workspace"){
    step([$class: 'WsCleanup'])
  }
}