node {
   env.WORKSPACE=pwd()
   stage("Checkout"){
       dir("ossim-ci"){
          git branch: 'dev-OCS626', url: 'https://github.com/ossimlabs/ossim-ci.git'
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
   stage("Test"){
     echo "NEED TO ADD TESTS FOR THE INSTALLATION!!!!"
   }
   
   stage("Archive"){
//     dir("${env.WORKSPACE}"){
//         sh "tar cvfz install.tgz install"
//     }
//     dir("${env.WORKSPACE}/artifacts"){
//         sh "mv ${env.WORKSPACE}/install.tgz ."
//         sh "cp ${env.WORKSPACE}/ossim-oms/lib/joms-*.jar ."
//     }
//     archiveArtifacts 'artifacts/*'
  }
}