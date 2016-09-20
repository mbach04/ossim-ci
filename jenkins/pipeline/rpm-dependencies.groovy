node{
    env.WORKSPACE=pwd()
    stage("Checkout"){
       dir("ossim-ci"){
          git branch: "${GIT_BRANCH}", url: "https://github.com/ossimlabs/ossim-ci.git"
       }
    }
    stage ("Build")
    {
        sh "${env.WORKSPACE}/ossim-ci/scripts/linux/rpmbuild-dependencies.sh"
    }
    stage("Archive"){

        archiveArtifacts 'dependency-rpms.tgz'
    }
    stage("Clean Workspace"){
        step([$class: 'WsCleanup'])
    }

}