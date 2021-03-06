//==================================================================================================
// This is the Jenkins pipeline script for building the OMAR geoscript application.
// Environment varialbes that MUST be passed in by Jenkins:
//    OSSIM_GIT_BRANCH: The tag of the branch to be built. Typically dev or master.
//
// Environment varaibles that MUST be set in the Jenkins global environment (manage jenkins -> configure system -> environment varaibles)
//    ARTIFACTORY_USER: The user to use when pushing artifacts
//    ARTIFACTORY_PASSWORD: The password to use when pushing artifacts
//    OPENSHIFT_USERNAME: The user to use logging into the docker registry
//    OPENSHIFT_PASSWORD: The password to use logging into the docker registry 
//==================================================================================================

// TODO: get to run on any node
node("master"){
    def workspaceDir = pwd()
    def ossimMavenProxy = "https://artifactory.ossim.io/artifactory/ossim-deps"
    def appName = "omar-geoscript"
    def omarCommonProjName = "omar-common"
    
    stage("Checkout"){
        dir("ossim-ci") {
            git branch: "${OSSIM_GIT_BRANCH}", url: "https://github.com/ossimlabs/ossim-ci.git"
        }
        dir(appName) {
            git branch: "${OSSIM_GIT_BRANCH}", url: "https://github.com/ossimlabs/${appName}.git"
        }

        dir(omarCommonProjName) {
            git branch: "${OSSIM_GIT_BRANCH}", url: "https://github.com/ossimlabs/${omarCommonProjName}.git"
        }

        // Set the environment variable for the gradle build
        env.OMAR_COMMON_PROPERTIES="${workspaceDir}/${omarCommonProjName}/omar-common-properties.gradle"

        notifyObj = load "${workspaceDir}/ossim-ci/jenkins/pipeline/notify.groovy"
    }

    try {
      stage("Build"){
        sh  "${workspaceDir}/plugins/omar-geoscript/gradlew install"
        // sh  "${workspaceDir}/apps/geoscript-app/gradlew doAll"
      }
    }
    catch(e){
        echo e.toString()
        currentBuild.result = "FAILED"
        notifyObj?.notifyFailed()
    }
}
