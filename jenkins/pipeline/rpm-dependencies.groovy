node{
    env.WORKSPACE=pwd()
    stage("Checkout"){
       dir("ossim-ci"){
          git branch: 'dev', url: 'https://github.com/ossimlabs/ossim-ci.git'
       }
    }
    stage ("Build")
    {
        sh "ossim-ci/scripts/linux/rpmbuild-dependencies.sh"
    }
    stage("Archive"){

        archiveArtifacts 'rpms.tgz'
    }
    stage("Clean Workspace"){
        step([$class: 'WsCleanup'])
    }

}