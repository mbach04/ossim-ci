//==================================================================================================
// This is the Jenkins pipeline script for building the OMAR geoscript application.
// Environment varialbes that MUST be passed in by Jenkins:
//    OSSIM_GIT_BRANCH: The tag of the branch to be built. Typically dev or master.
//==================================================================================================

node("master"){
    // env.WORKSPACE=pwd()
    // env.OSSIM_MAVEN_PROXY="https://artifacts.radiantbluecloud.com/artifactory/ossim-deps"

    def workspaceDir = pwd()
    def ossimMavenProxy = "https://artifacts.radiantbluecloud.com/artifactory/ossim-deps"
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

        // dir("o2-paas/docker/o2-docs") {
        //     step ([$class: 'CopyArtifact',
        //          projectName: "build-docs-${OSSIM_GIT_BRANCH}",
        //          filter: "install.tgz",
        //          flatten: true])
        //  }
         notifyObj = load "${workspaceDir}/ossim-ci/jenkins/pipeline/notify.groovy"
    }

    try {
      stage("Build"){
        sh  "${workspaceDir}/plugins/gradlew install"
        // exec {
        //     commandLine '${workspaceDir}/apps/gradlew', 'doAll'
        // }
      }
    }
    catch(e){
        echo e.toString()
        currentBuild.result = "FAILED"
        notifyObj?.notifyFailed()
    }
}
