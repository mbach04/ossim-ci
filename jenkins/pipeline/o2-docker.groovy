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
       dir("${env.WORKSPACE}/omar/build_scripts/docker"){
           sh """
            echo "Removing files..."
            sudo rm -rf /data/s3
            sudo rm -rf /data/jpip-cache
            mkdir -p /data/jpip-cache
            eval `aws ecr get-login --region us-east-1`
            echo "Setting up containers...."
            . ${env.WORKSPACE}/omar/build_scripts/docker/docker-common.sh
            echo DOCKER_HOST_URL=${DOCKER_HOST_URL}
            docker-compose --file=docker-compose-no-build.yml down
            for x in `docker images | grep /o2- | awk '{print \$3}'`; do docker rmi -f \$x; done
            for x in `docker images | grep /tlv | awk '{print \$3}'`; do docker rmi -f \$x; done
            docker-compose --file=docker-compose-no-build.yml up -d
           """
       }
   }
   stage("Clean Workspace"){
     step([$class: 'WsCleanup'])
  }
}