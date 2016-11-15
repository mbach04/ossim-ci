env.WORKSPACE=pwd()
env.S3_DELIVERY_BUCKET="s3://o2-delivery/${OSSIM_GIT_BRANCH}"
env.DOCKER_REGISTRY_URI="320588532383.dkr.ecr.us-east-1.amazonaws.com"

node("docker_exporter"){

   echo "WORKSPACE        = ${env.WORKSPACE}"
   echo "S3_DELIVERY_BUCKET   = ${env.S3_DATA_BUCKET}"
   stage("Checkout") {
       dir("omar") {
          git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar.git'
       }
       dir("ossim-ci") {
          git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-ci.git'
       }
   }

   stage("Deliver Docker Images") {
//      sh """
//        pushd ${env.WORKSPACE}/omar/build_scripts/docker
//        ./docker-export.sh
//        popd
//      """
   }
   stage("Download RPMS"){
       dir("${env.WORKSPACE}"){
           step ([$class: 'CopyArtifact',
              projectName: "rpm-${OSSIM_GIT_BRANCH}",
              filter: "artifacts/*.tgz",
              flatten: true])
       }
   }

   stage("Deliver RPMS") {
      sh """
      """
   }
 
  stage("Clean Workspace"){
     step([$class: 'WsCleanup'])
  }
}
