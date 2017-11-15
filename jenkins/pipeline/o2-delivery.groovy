def notifyObj
node(){
   env.WORKSPACE=pwd();
   env.S3_DELIVERY_BUCKET="s3://o2-delivery/${OSSIM_GIT_BRANCH}";
   env.DOCKER_REGISTRY_URI="320588532383.dkr.ecr.us-east-1.amazonaws.com";
   echo "WORKSPACE        = ${env.WORKSPACE}";
   echo "S3_DELIVERY_BUCKET   = ${env.S3_DELIVERY_BUCKET}";
   try {
      stage("Checkout"){
         dir("ossimlabs-${OSSIM_GIT_BRANCH}"){
            dir("omar"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar.git'
            }
            dir("omar-avro"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-avro.git'
            }
            dir("omar-base"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-base.git'
            }
            dir("omar-common"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-common.git'
            }
            dir("omar-core"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-core.git'
            }
            dir("omar-disk-cleanup"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-disk-cleanup.git'
            }
            dir("omar-docs"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-docs.git'
            }
            dir("omar-download"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-download.git'
            }
            dir("omar-eureka-server"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-eureka-server.git'
            }
            dir("omar-geoscript"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-geoscript.git'
            }
            dir("omar-hibernate-spatial"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-hibernate-spatial.git'
            }
            dir("omar-ingest-metrics"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-ingest-metrics.git'
            }
            dir("omar-jpip"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-jpip.git'
            }
            dir("omar-mensa"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-mensa.git'
            }
            dir("omar-oldmar"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-oldmar.git'
            }
            dir("omar-oms"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-oms.git'
            }
            dir("omar-openlayers"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-openlayers.git'
            }
            dir("omar-opir"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-opir.git'
            }
            dir("omar-ossim-base"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-ossim-base.git'
            }
            dir("omar-ossimtools"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-ossimtools.git'
            }
            dir("omar-raster"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-raster.git'
            }
            dir("omar-scdf"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-scdf.git'
            }
            dir("omar-scdf-aggregator"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-scdf-aggregator.git'
            }
            dir("omar-scdf-downloader"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-scdf-downloader.git'
            }
            dir("omar-scdf-extractor"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-scdf-extractor.git'
            }
            dir("omar-scdf-file-parser"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-scdf-file-parser.git'
            }
            dir("omar-scdf-indexer"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-scdf-indexer.git'
            }
            dir("omar-scdf-kafka"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-scdf-kafka.git'
            }
            dir("omar-scdf-notifier-email"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-scdf-notifier-email.git'
            }
            dir("omar-scdf-s3-extractor-filter"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-scdf-s3-extractor-filter.git'
            }
            dir("omar-scdf-s3-filter"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-scdf-s3-filter.git'
            }
            dir("omar-scdf-s3-uploader"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-scdf-s3-uploader.git'
            }
            dir("omar-scdf-server"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-scdf-server.git'
            }
            dir("omar-scdf-sqs"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-scdf-sqs.git'
            }
            dir("omar-scdf-stager"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-scdf-stager.git'
            }
            dir("omar-scdf-zookeeper"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-scdf-zookeeper.git'
            }
            dir("omar-security"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-security.git'
            }
            dir("omar-service-proxy"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-service-proxy.git'
            }
            dir("omar-services"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-services.git'
            }
            dir("omar-sqs"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-sqs.git'
            }
            dir("omar-stager"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-stager.git'
            }
            dir("omar-superoverlay"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-superoverlay.git'
            }
            dir("omar-turbine-server"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-turbine-server.git'
            }
            dir("omar-ui"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-ui.git'
            }
            dir("omar-video"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-video.git'
            }
            dir("omar-wcs"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-wcs.git'
            }
            dir("omar-wfs"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-wfs.git'
            }
            dir("omar-wms"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-wms.git'
            }
            dir("omar-wmts"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-wmts.git'
            }
            dir("omar-zuul-server"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/omar-zuul-server.git'
            }
            dir("ossim"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim.git'
            }
            dir("ossim-ci"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-ci.git'
            }
            dir("ossim-csm-plugin"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-csm-plugin.git'
            }
            dir("ossim-gui"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-gui.git'
            }
            dir("ossim-planet"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-planet.git'
            }
            dir("ossim-oms"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-oms.git'
            }
            dir("ossim-plugins"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-plugins.git'
            }
            dir("ossim-video"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-video.git'
            }
            dir("ossim-wms"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/ossim-wms.git'
            }
            dir("tlv"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/ossimlabs/tlv.git'
            }
         }
         dir("radiantbluetechnologies-${OSSIM_GIT_BRANCH}"){
//            dir("cucumber-${OSSIM_GIT_BRANCH}-src"){
//               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/radiantbluetechnologies/cucumber-oc2s.git', credentialsId: "cicdGithub"
//            }
            dir("config-repo-src"){
               git branch: "master", url: 'https://github.com/radiantbluetechnologies/config-repo.git', credentialsId: "cicdGithub"
            }
            dir("o2-paas-${OSSIM_GIT_BRANCH}-src"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/radiantbluetechnologies/o2-paas.git', credentialsId: "cicdGithub"
            }
            dir("oldmar"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/radiantbluetechnologies/omar.git', credentialsId: "cicdGithub"
            }
            dir("ossim-isa"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/radiantbluetechnologies/ossim-isa.git', credentialsId: "cicdGithub"
            }
            dir("ossim-isa-plugin"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/radiantbluetechnologies/ossim-isa-plugin.git', credentialsId: "cicdGithub"
            }
            dir("ossim-private-${OSSIM_GIT_BRANCH}-src"){
               git branch: "${OSSIM_GIT_BRANCH}", url: 'https://github.com/radiantbluetechnologies/ossim-private.git', credentialsId: "cicdGithub"
            }
         }
      }

      stage("Download Artifacts"){
         dir("${env.WORKSPACE}"){
            step ([$class: 'CopyArtifact',
                   projectName: "ossim-rpm-${OSSIM_GIT_BRANCH}",
                   filter: "artifacts/*",
                   target: "ossim-rpm-${OSSIM_GIT_BRANCH}",
                   flatten: true])
            step ([$class: 'CopyArtifact',
                   projectName: "ossim-${OSSIM_GIT_BRANCH}",
                   filter: "artifacts/*",
                   target: "ossim-${OSSIM_GIT_BRANCH}/",
                   flatten: true])
         }
      }

      stage("Package Source"){
         dir("${env.WORKSPACE}"){
            // Remove the git directories:
            sh "rm -rf `find ossimlabs-${OSSIM_GIT_BRANCH} -name .git`"
            sh "rm -rf `find radiantbluetechnologies-${OSSIM_GIT_BRANCH} -name .git`"

            // Tar up the repository directories:
            sh "tar cvfz ossimlabs-${OSSIM_GIT_BRANCH}.tgz ossimlabs-${OSSIM_GIT_BRANCH}"
            sh "tar cvfz radiantbluetechnologies-${OSSIM_GIT_BRANCH}.tgz radiantbluetechnologies-${OSSIM_GIT_BRANCH}"
         }
      }

      stage("Deliver Artifacts"){  
         dir("${env.WORKSPACE}"){
            step([$class: 'S3BucketPublisher',
                  dontWaitForConcurrentBuildCompletion: false,
                  entries: [[bucket: "o2-delivery/${OSSIM_GIT_BRANCH}/src",
                             excludedFile: '',
                             flatten: false,
                             gzipFiles: false,
                             keepForever: false,
                             managedArtifacts: false,
                             noUploadOnFailure: false,
                             selectedRegion: 'us-east-1',
                             showDirectlyInBrowser: true,
                             sourceFile: "ossimlabs-${OSSIM_GIT_BRANCH}.tgz",
                             storageClass: 'STANDARD',
                             uploadFromSlave: false,
                             useServerSideEncryption: false],

                            [bucket: "o2-delivery/${OSSIM_GIT_BRANCH}/src",
                             excludedFile: '',
                             flatten: false,
                             gzipFiles: false,
                             keepForever: false,
                             managedArtifacts: false,
                             noUploadOnFailure: false,
                             selectedRegion: 'us-east-1',
                             showDirectlyInBrowser: true,
                             sourceFile: "radiantbluetechnologies-${OSSIM_GIT_BRANCH}.tgz",
                             storageClass: 'STANDARD',
                             uploadFromSlave: false,
                             useServerSideEncryption: false],

                            [bucket: "o2-delivery/${OSSIM_GIT_BRANCH}/o2-rpms",
                             excludedFile: '',
                             flatten: false,
                             gzipFiles: false,
                             keepForever: false,
                             managedArtifacts: false,
                             noUploadOnFailure: false,
                             selectedRegion: 'us-east-1',
                             showDirectlyInBrowser: true,
                             sourceFile: "ossim-rpm-${OSSIM_GIT_BRANCH}/*",
                             storageClass: 'STANDARD',
                             uploadFromSlave: false,
                             useServerSideEncryption: false],

                            [bucket: "o2-delivery/${OSSIM_GIT_BRANCH}/ossim",
                             excludedFile: '',
                             flatten: false,
                             gzipFiles: false,
                             keepForever: false,
                             managedArtifacts: false,
                             noUploadOnFailure: false,
                             selectedRegion: 'us-east-1',
                             showDirectlyInBrowser: true,
                             sourceFile: "ossim-${OSSIM_GIT_BRANCH}/*",
                             storageClass: 'STANDARD',
                             uploadFromSlave: false,
                             useServerSideEncryption: false]], 
                  profileName: 'o2-cicd',
                  userMetadata: []])
         }
      }
   }

   catch(e)
   {
      currentBuild.result = "FAILED";
      notifyObj?.notifyFailed();
   }

   stage("Clean Workspace"){
      step([$class: 'WsCleanup'])
   }
}
