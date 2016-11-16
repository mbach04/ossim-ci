Name:           aws-sdk-cpp
Version:    1.0.29
Release:        1%{?dist}
Summary:    aws-sdk-cpp 
Group:      Applications/Engineering
License:    Apache License Version 2.0        
URL:        https://github.com/aws/aws-sdk-cpp
Source:     https://github.com/aws/aws-sdk-cpp/%{name}-%{version}.src.tar.gz

BuildRequires:  cmake 
BuildRequires: gcc-c++

%description
C++ SDK for amazon web services

%prep
%setup -q

%build
mkdir -p build
pushd build
%cmake .. -DBUILD_ONLY="s3"
make -j
popd

%install
pushd build
make install DESTDIR=%{buildroot}
mv %{buildroot}/usr/lib %{buildroot}/usr/lib64

popd

%post -p /sbin/ldconfig
%postun -p /sbin/ldconfig

%files
%{_libdir}/libgpstk*

%package -n gpstk-devel
Summary:        Devel files gpstk
Group:      System Environment/Libraries
Requires:       %{name}%{?_isa} = %{version}-%{release}
License:        LGPL

%description -n gpstk-devel
Development files for gpstk.

%files -n gpstk-devel
%{_includedir}/gpstk

%exclude %{_bindir}/*

%changelog
