node {
    env.WORKSPACE=pwd()
    stage("Checkout"){
      dir("ossim-dev-src"){
        dir("ossim"){
          git branch: 'dev', url: 'https://github.com/ossimlabs/ossim.git'
        }
        dir("ossim-video"){
          git branch: 'dev', url: 'https://github.com/ossimlabs/ossim-video.git'
        }
        dir("ossim-planet"){
          git branch: 'dev', url: 'https://github.com/ossimlabs/ossim-planet.git'
        }
        dir("ossim-gui"){
          git branch: 'dev', url: 'https://github.com/ossimlabs/ossim-gui.git'
        }
        dir("ossim-oms"){
          git branch: 'dev', url: 'https://github.com/ossimlabs/ossim-oms.git'
        }
        dir("ossim-plugins"){
          git branch: 'dev', url: 'https://github.com/ossimlabs/ossim-plugins.git'
        }
        dir("ossim-wms"){
          git branch: 'dev', url: 'https://github.com/ossimlabs/ossim-wms.git'
        }
        dir("ossim-ci"){
          git branch: 'dev', url: 'https://github.com/ossimlabs/ossim-ci.git'
        }
        dir("ossim-private"){
           git branch: 'dev', url: 'git@ossim-private.github.com:radiantbluetechnologies/ossim-private.git'
        }
        dir("omar"){
          git branch: 'dev', url: 'https://github.com/ossimlabs/omar.git'
        }
        dir("oldomar"){
          git branch: 'dev', url: 'https://github.com/radiantbluetechnologies/omar.git'
        }
        dir("ossim-ci"){
          git branch: 'dev', url: 'https://github.com/ossimlabs/ossim-ci.git'
        }
        dir("ossimlabs-tlv"){
          git branch: 'dev', url: 'https://github.com/ossimlabs/tlv.git'
        }
        dir("tlv"){
          git branch: 'master', url: 'https://github.com/time-lapse-viewer/tlv.git'
        }
      }
    }
   stage("Archive"){
     dir("${env.WORKSPACE}"){
         dir("ossim-dev-src"){
          sh "rm -rf `find . -name .git`"
         }
         sh "tar cvfz ossim-dev-src.tgz ossim-dev-src"
     }
     dir("${env.WORKSPACE}/artifacts"){
         sh "mv ${env.WORKSPACE}/ossim-dev-src.tgz ."
     }
     archiveArtifacts 'artifacts/*'
  }
  stage("Clean Workspace"){
    step([$class: 'WsCleanup'])
  }
}