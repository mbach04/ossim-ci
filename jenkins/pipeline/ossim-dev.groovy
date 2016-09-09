node {
   env.WORKSPACE=pwd()
   stage("Checkout"){
       dir("ossim"){
          git branch: 'dev', url: 'https://github.com/ossimlabs/ossim.git'
       }
       dir("ossim-video"){
          git branch: 'dev', url: 'https://github.com/ossimlabs/ossim-video.git'
       }
       dir("ossim-planet"){
          git branch: 'dev', url: 'https://github.com/ossimlabs/ossim-planet.git'
       }
       dir("ossim-gui"){
          git branch: 'dev', url: 'https://github.com/ossimlabs/ossim-gui.git'
       }
       dir("ossim-oms"){
          git branch: 'dev', url: 'https://github.com/ossimlabs/ossim-oms.git'
       }
       dir("ossim-plugins"){
          git branch: 'dev', url: 'https://github.com/ossimlabs/ossim-plugins.git'
       }
       dir("ossim-wms"){
          git branch: 'dev', url: 'https://github.com/ossimlabs/ossim-wms.git'
       }
       dir("ossim-ci"){
          git branch: 'dev', url: 'https://github.com/ossimlabs/ossim-ci.git'
       }
       dir("ossim-private"){
          git branch: 'dev', url: 'git@github.com:radiantbluetechnologies/ossim-private.git'
       }
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
   stage("Test"){
     echo "NEED TO ADD TESTS FOR THE INSTALLATION!!!!"
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