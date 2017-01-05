node{
    env.WORKSPACE=pwd()
    if (USE_C2S_ACCOUNT=="true") {
        echo "Using C2S account"
        env.DOCKER_REGISTRY_URI="docker-registry-default.cloudapps.ossimc2s.com"
        env.DOCKER_REGISTRY_PW="2ndtAhWidV9PX_VwqWW_PnLPfCWBAc-vWSDYEUiJhQg"
    }
    else {
        echo "Using ModApps account"
        env.DOCKER_REGISTRY_URI="docker-registry-default.o2.radiantbluecloud.com"
        env.DOCKER_REGISTRY_PW="oQkGyJA7G3daTUDPKNZKRo_dRJ1ANkkRske99zUkBGA"
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
        stage("Build")
        {
            dir("${env.WORKSPACE}/o2-paas/spring-cloud"){
                sh "./docker-build.sh"
            }
            dir("${env.WORKSPACE}/o2-paas/docker"){
                sh "./dockeraws-build.sh"
            }
        }
        stage("Clean Workspace"){
            step([$class: 'WsCleanup'])
        }
    }
    catch(e)
    {
        currentBuild.result = "FAILED"
        notifyObj?.notifyFailed()
    }
}
