//==================================================================================================
// This is the Jenkins pipeline script for building all O2-OMAR-related docker containers including
// the spring-cloud-related components. The script accepts two parameters:
//    OSSIM_GIT_BRANCH -- Branch tag applies to *all* repositories being accessed.
//    USE_C2S_ACCOUNT -- Boolean (actually passed as string true|false). Indicates which AWS
//       account being used to push container images.
// The container images are pushed to the corresponding OpenShift container registry.
//==================================================================================================


node("master"){
    env.WORKSPACE=pwd()
    if (USE_C2S_ACCOUNT=="true") {
        echo "Using C2S account"
        env.USE_C2S_ACCOUNT="true"
        env.DOCKER_REGISTRY_URI="docker-registry-default.cloudapps.ossimc2s.com"
        sh "oc login $C2S_OC_LOGIN --insecure-skip-tls-verify=true"
        sh "oc whoami -t > ocwhoami.txt"
        env.DOCKER_REGISTRY_PW=readFile("ocwhoami.txt").trim()
        env.OPENSHIFT_PROJECT_PATH="oc2s"
    }
    else {
        echo "Using ModApps account"
        env.USE_C2S_ACCOUNT="false"
        env.DOCKER_REGISTRY_URI="docker-registry-default.o2.radiantbluecloud.com"
        sh "oc login $MODAPPS_OC_LOGIN --insecure-skip-tls-verify=true"
        sh "oc whoami -t > ocwhoami.txt"
        env.DOCKER_REGISTRY_PW=readFile("ocwhoami.txt").trim()
        env.OPENSHIFT_PROJECT_PATH="o2"
    }

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
            dir("${env.WORKSPACE}/o2-paas/docker"){
                sh "./docker-build.sh"
            }
            dir("${env.WORKSPACE}/o2-paas/spring-cloud"){
                sh "./docker-build.sh"
            }
        }
      }
        stage("Export Docker Images")
        {
           sh """
             pushd ${env.WORKSPACE}/o2-paas/docker
             ./docker-export.sh
             popd
           """
        }
        stage("Clean Workspace")
        {
            step([$class: 'WsCleanup'])
        }
    }
    catch(e)
    {
        currentBuild.result = "FAILED"
        notifyObj?.notifyFailed()
    }
}
