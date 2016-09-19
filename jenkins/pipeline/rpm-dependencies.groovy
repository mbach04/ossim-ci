node{
    env.WORKSPACE=pwd()
    stage("Checkout"){
       dir("ossim-ci"){
          git branch: 'dev', url: 'https://github.com/ossimlabs/ossim-ci.git'
       }
    }
    stage("Download"){
        sh """
        mkdir -p rpmbuild/{BUILD,RPMS,SOURCES,SPECS,SRPMS}
        pushd rpmbuild/SOURCES
        wget https://s3.amazonaws.com/ossimlabs/dependencies/source/OpenSceneGraph-3.2.1.zip -O OpenSceneGraph-3.2.1.zip
        wget https://s3.amazonaws.com/ossimlabs/dependencies/source/0001-Cmake-fixes.patch -O 0001-Cmake-fixes.patch
        wget https://s3.amazonaws.com/ossimlabs/dependencies/source/0001-Update-Aether-to-0.9.0.M3.patch -O 0001-Update-Aether-to-0.9.0.M3.patch
        wget https://s3.amazonaws.com/ossimlabs/dependencies/source/0002-Fix-invalid-char.patch -O 0002-Fix-invalid-char.patch
        wget https://s3.amazonaws.com/ossimlabs/dependencies/source/0003-Activate-osgviewerWX.patch -O 0003-Activate-osgviewerWX.patch
        wget https://s3.amazonaws.com/ossimlabs/dependencies/source/0005-Use-generics-in-modello-generated-code.patch -O 0005-Use-generics-in-modello-generated-code.patch
        wget https://s3.amazonaws.com/ossimlabs/dependencies/source/szip-2.1.tar.gz -O szip-2.1.tar.gz
        wget https://s3.amazonaws.com/ossimlabs/dependencies/source/szip-linking.patch -O szip-linking.patch
        wget https://s3.amazonaws.com/ossimlabs/dependencies/source/szip-opt.patch -O szip-opt.patch
        wget https://s3.amazonaws.com/ossimlabs/dependencies/source/libjpeg12-turbo-1.4.2.tar.gz -O libjpeg12-turbo-1.4.2.tar.gz
        wget https://s3.amazonaws.com/ossimlabs/dependencies/source/libjpeg12-turbo14-noinst.patch -O libjpeg12-turbo14-noinst.patch
        wget https://s3.amazonaws.com/ossimlabs/dependencies/source/libjpeg12-turbo-header-files.patch -O libjpeg12-turbo-header-files.patch
        wget https://s3.amazonaws.com/ossimlabs/dependencies/source/gpstk-2.3.src.tar.gz -O gpstk-2.3.src.tar.gz
        wget https://s3.amazonaws.com/ossimlabs/dependencies/source/hdf5a-1.8.17.tar.gz -O hdf5a-1.8.17.tar.gz
        popd
        rpmbuild -ba --define "_topdir ${env.WORKSPACE}/rpmbuild" --define "BUILD_RELEASE 1" ${env.WORKSPACE}/rpmbuild/SPECS/hdf5a.spec
        rpmbuild -ba --define "_topdir ${env.WORKSPACE}/rpmbuild" --define "BUILD_RELEASE 1" ${env.WORKSPACE}/rpmbuild/SPECS/libjpeg12-turbo.spec
        rpmbuild -ba --define "_topdir ${env.WORKSPACE}/rpmbuild" --define "BUILD_RELEASE 1" ${env.WORKSPACE}/rpmbuild/SPECS/gpstk.spec
        rpmbuild -ba --define "_topdir ${env.WORKSPACE}/rpmbuild" --define "BUILD_RELEASE 3" ${env.WORKSPACE}/rpmbuild/SPECS/szip.spec
        rpmbuild -ba --define "_topdir ${env.WORKSPACE}/rpmbuild" ${env.WORKSPACE}/rpmbuild/SPECS/OpenSceneGraph.spec

        mkdir -p ${env.WORKSPACE}/rpms/
        cp `find ${env.WORKSPACE}/rpmbuild/RPMS/ -name "*.rpm"` ${env.WORKSPACE}/rpms/
        pushd ${env.WORKSPACE}
            tar cvfz rpms.tgz rpms
        popd > /dev/null
"""
    }
   stage("Archive"){
     
     archiveArtifacts 'rpms.tgz'
   }
    stage("Clean Workspace"){
        step([$class: 'WsCleanup'])
    }

}