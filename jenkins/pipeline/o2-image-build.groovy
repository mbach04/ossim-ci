//==================================================================================================
// This is the Jenkins pipeline script for building all O2-OMAR-related docker containers including
// the spring-cloud-related components. The script accepts two parameters:
//    OSSIM_GIT_BRANCH -- Branch tag applies to *all* repositories being accessed.
// The container images are pushed to the corresponding OpenShift container registry.
//==================================================================================================


node("master"){
    env.WORKSPACE=pwd()

    stage("Checkout"){
        dir("o2-paas"){
          git branch: "${OSSIM_GIT_BRANCH}", url: 'git@o2-paas.github.com:radiantbluetechnologies/o2-paas.git'
        }
        dir("ossim-ci") {
            git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-ci.git'
        }
         notifyObj = load "${env.WORKSPACE}/ossim-ci/jenkins/pipeline/notify.groovy"
    }
    try{
      if (SKIP_BUILD_STAGE=="false")
      {
        stage("Build")
        {
          // The base images are pulled from the modapps registry for building.
          // NOTE: The builds necessarily need to pull base images (o2-base, o2-ossim)
          // from a common registry for both oc2s and modapps destinations. Therefore,
          // all builds (oc2s and modapps) will always push images to the modapps
          // registry. In any case, the jenkins builds will be defaulting to "all".
          setupModappsRegistry()
          buildImages()
          pushImages()

          switch(EXPORT_REGISTRY) {
            case "modapps":
              // Nothing to do here since already done above.
            break
            case "oc2s":
              setupC2SRegistry()
              pushImages()
            break
            default:
              // default = all
              setupC2SRegistry()
              pushImages()
            break
        }
      }
      if (SKIP_EXPORT_STAGE=="false")
      {
        stage("Export Docker Images to S3")
        {
          sh """
            pushd ${env.WORKSPACE}/o2-paas/docker
            ./docker-export.sh -a
            popd
          """
          }
        }
      }
        stage("Clean Workspace")
        {
            dir("${env.WORKSPACE}/ossim-ci/scripts/linux"){
                sh "./docker-cleanup.sh"
            }
            step([$class: 'WsCleanup'])
        }
    }
    catch(e)
    {
        currentBuild.result = "FAILED"
        notifyObj?.notifyFailed()
    }
}

def setupC2SRegistry() {
  echo "Logging into C2S docker registry"
  env.DOCKER_REGISTRY_URI="docker-registry-default.cloudapps.ossimc2s.com"
  sh "oc login $C2S_OC_LOGIN --insecure-skip-tls-verify=true"
  sh "oc whoami -t > ocwhoami.txt"
  env.DOCKER_REGISTRY_PW=readFile("ocwhoami.txt").trim()
  env.OPENSHIFT_PROJECT_PATH="oc2s"
}

def setupModappsRegistry() {
  echo "Logging into ModApps docker registry"
  env.DOCKER_REGISTRY_URI="docker-registry-default.o2.radiantbluecloud.com"
  sh "oc login $MODAPPS_OC_LOGIN --insecure-skip-tls-verify=true"
  sh "oc whoami -t > ocwhoami.txt"
  env.DOCKER_REGISTRY_PW=readFile("ocwhoami.txt").trim()
  env.OPENSHIFT_PROJECT_PATH="o2"
}

def buildImages() {
  dir("${env.WORKSPACE}/o2-paas/docker"){
      sh "./docker-build.sh"
  }
  dir("${env.WORKSPACE}/o2-paas/spring-cloud"){
      sh "./docker-build.sh"
  }
}
def pushImages() {
  dir("${env.WORKSPACE}/o2-paas/docker"){
      sh "./docker-push.sh"
  }
  dir("${env.WORKSPACE}/o2-paas/spring-cloud"){
      sh "./docker-push.sh"
  }
}
