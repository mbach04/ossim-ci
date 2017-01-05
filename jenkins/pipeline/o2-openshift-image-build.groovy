node("master"){
    env.WORKSPACE=pwd()
    if (USE_C2S_ACCOUNT=="true") {
        echo "Using C2S account"
        env.USE_C2S_ACCOUNT="true"
        env.DOCKER_REGISTRY_URI="docker-registry-default.cloudapps.ossimc2s.com"
        sh "oc login openshift-master.ossimc2s.com -u admin -p P@ssw()rd"
        sh "oc whoami -t > ocwhoami.txt"
        env.DOCKER_REGISTRY_PW=readFile("ocwhoami.txt").trim()
        env.OPENSHIFT_PROJECT_PATH="/oc2s"
    }
    else {
        echo "Using ModApps account"
        env.USE_C2S_ACCOUNT="false"
        env.DOCKER_REGISTRY_URI="docker-registry-default.o2.radiantbluecloud.com"
        env.DOCKER_REGISTRY_PW="2fOqq6fPnmNqXTrK-tSztJZE2d5u2MJmMRsI3bKrxPU"
        env.OPENSHIFT_PROJECT_PATH="/omar-${OSSIM_GIT_BRANCH}"
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
            dir("${env.WORKSPACE}/o2-paas/docker"){
                sh "./dockeraws-build.sh"
            }
            dir("${env.WORKSPACE}/o2-paas/spring-cloud"){
                sh "./docker-build.sh"
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
