node{
  env.WORKSPACE=pwd()
  env.S3_DELIVERY_BUCKET="s3://o2-delivery/${OSSIM_GIT_BRANCH}"
  env.DOCKER_REGISTRY_URI="320588532383.dkr.ecr.us-east-1.amazonaws.com"
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

  stage("Download RPMS"){
     dir("${env.WORKSPACE}/rpms"){
         step ([$class: 'CopyArtifact',
            projectName: "rpm-${OSSIM_GIT_BRANCH}",
            filter: "artifacts/*.tgz",
            flatten: true])
     }
  }

  stage("Deliver RPMS"){
    dir("${env.WORKSPACE}"){
        step([$class: 'S3BucketPublisher',
              dontWaitForConcurrentBuildCompletion: false, 
              entries: [[bucket: "o2-delivery/${OSSIM_GIT_BRANCH}/o2-rpms", 
                         excludedFile: '', 
                         flatten: false, 
                         gzipFiles: false, 
                         keepForever: false, 
                         managedArtifacts: false, 
                         noUploadOnFailure: false, 
                         selectedRegion: 'us-east-1', 
                         showDirectlyInBrowser: true, 
                         sourceFile: 'rpms/*.tgz', 
                         storageClass: 'STANDARD', 
                         uploadFromSlave: false, 
                         useServerSideEncryption: false]], 
              profileName: 'o2-cicd', 
              userMetadata: []])
    }
        
  }

  stage("Download DOCS"){
     dir("${env.WORKSPACE}/install-guide"){
         step ([$class: 'CopyArtifact',
            projectName: "build-docs-${OSSIM_GIT_BRANCH}",
            filter: "install.tgz",
            flatten: true])
     }
  }
  
 stage("Deliver O2 Docs"){
    dir("${env.WORKSPACE}"){
        step([$class: 'S3BucketPublisher',
              dontWaitForConcurrentBuildCompletion: false, 
              entries: [[bucket: "o2-delivery/${OSSIM_GIT_BRANCH}/o2-install-guide", 
                         excludedFile: '', 
                         flatten: false, 
                         gzipFiles: false, 
                         keepForever: false, 
                         managedArtifacts: false, 
                         noUploadOnFailure: false, 
                         selectedRegion: 'us-east-1', 
                         showDirectlyInBrowser: true, 
                         sourceFile: 'install-guide/*.tgz', 
                         storageClass: 'STANDARD', 
                         uploadFromSlave: false, 
                         useServerSideEncryption: false]], 
              profileName: 'o2-cicd', 
              userMetadata: []])
    }
        
   }

  stage("Clean Workspace"){
     step([$class: 'WsCleanup'])
  }
}