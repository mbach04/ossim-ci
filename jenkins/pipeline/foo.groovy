node {
   env.WORKSPACE=pwd()
   stage("Checkout"){
     checkout([$class: 'GitSCM', branches: [[name: '*/dev']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/ossimlabs/ossim-ci.git']]])
   }
   echo "${env.WORKSPACE}"
   stage("Download Artifacts"){
       dir("${env.WORKSPACE}"){
           step ([$class: 'CopyArtifact',
              projectName: 'ossim-dev',
              filter: "artifacts/install.tgz",
              flatten: true])
       }
   }
   stage("Test"){
       sh """
        tar xvfz install.tgz
        """
       sh """
         install/bin/ossim-info --version > foo.txt
       """
   }

   stage("Archive"){
     archiveArtifacts 'foo.txt'
   }
   
   stage("Clean Workspace"){
       step([$class: 'WsCleanup'])
  }
}
