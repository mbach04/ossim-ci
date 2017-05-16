#---
# File: ossim-all.spec
#
# Spec file for building ossim-msp rpm with rpmbuild.
#
#
# Example usage:
# rpmbuild -ba --define 'RPM_OSSIM_VERSION 1.9.0' --define 'BUILD_RELEASE 1' ossim-el7.spec
#
# Caveats: 
# 1) Builder/user needs "groovy" in their search path.
# 2) Use "archive.sh" script in ossim/scripts/archive.sh to generate the source
#    tar ball, e.g. ossim-1.9.0.tar.gz, from appropriate git branch.
#---
Name:           ossim-msp
Version:        %{RPM_OSSIM_VERSION} 
Release:        %{BUILD_RELEASE}%{?dist}
Summary:        OSSIM-MSP library and command line application
Group:          System Environment/Libraries
#TODO: Which version?
License:        LGPLv2+
URL:            https://github.com/orgs/ossimlabs/dashboard
#Source0:        http://download.osgeo.org/ossim/source/%{name}-%{version}.tar.gz
%define is_systemd %(test -d /etc/systemd && echo 1 || echo 0)

Requires:       %{name}-libs%{?_isa} = %{version}-%{release}


%description
OSSIM-MSP enables Mensuration Support Program capability in OSSIM.

%package    ossim-msp-devel
Summary:        Development files for ossim-msp
Group:          System Environment/Libraries
Requires:       %{name}-libs%{?_isa} = %{version}-%{release}

%description ossim-msp-devel
Development files for ossim-msp.

%package    ossim-msp-libs
Summary:        Development files for ossim-msp
Group:          System Environment/Libraries

%description ossim-msp-libs
Libraries for ossim-msp.


%build
echo "********************** $OSSIM_DEV_HOME ***********************"

%install
echo "************************BUILDDIR: %{_builddir}*************** "
echo "************************BUILDROOT: %{buildroot}*************** "
pushd %{_builddir}/../..
export ROOT_DIR=$PWD
popd > /dev/null

export DESTDIR=%{buildroot}
mkdir -p %{_bindir}
mkdir -p %{_libdir}

pushd %{_builddir}/install
echo off
  for x in `find include`; do
    if [ -f $x ] ; then
      install -p -m644 -D $x %{buildroot}/usr/$x;
    fi
  done
  for x in `find share`; do
    if [ -f $x ] ; then
      install -p -m644 -D $x %{buildroot}/usr/$x;
    fi
  done

  cp -R lib64 %{buildroot}/usr/
  chmod -R 755 %{buildroot}/usr/lib64/*

  cp -R bin %{buildroot}/usr/
  chmod -R 755 %{buildroot}/usr/bin/*

#  if [ -f ./etc/profile.d/ossim.sh ] ; then
#    install -p -m644 -D ./etc/profile.d/ossim.sh %{buildroot}%{_sysconfdir}/profile.d/ossim.sh
#  fi

popd
echo on


%files
%{_bindir}/*

# In jpip-server package:
%exclude %{_bindir}/ossim-jpip-server

%files ossim-msp-devel
%{_includedir}/ossimMsp

%files ossim-msp-libs
#%doc ossim/LICENSE.txt
%{_libdir}/libossim-msp.so*
#%{_libdir}/pkgconfig/ossim.pc
#%{_sysconfdir}/profile.d/ossim.sh
#%{_sysconfdir}/profile.d/ossim.csh


%changelog
