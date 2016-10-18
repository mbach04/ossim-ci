node("o2.radiantbluecloud.com"){
   env.WORKSPACE=pwd()
   env.DOCKER_HOST_URL="${DOCKER_HOST_URL}"
   stage("Checkout"){
       dir("omar"){
          git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar.git'
       }
       dir("ossim-ci"){
          git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-ci.git'
       }
   }
   stage("Confiure Docker"){
       //dir("${env.WORKSPACE}/omar/build_scripts/docker"){
           sh """
           ${env.WORKSPACE}/ossim-ci/scripts/o2-docker.sh
           """
       //}
   }
   stage("Clean Workspace"){
     step([$class: 'WsCleanup'])
  }
}