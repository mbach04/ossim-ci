Name:           aws-sdk-cpp
Version:    1.0.29
Release:        1%{?dist}
Summary:    aws-sdk-cpp 
Group:      Applications/Engineering
License:    Apache License Version 2.0        
URL:        https://github.com/aws/aws-sdk-cpp
Source:     https://github.com/aws/aws-sdk-cpp/%{name}-%{version}.tgz

BuildRequires: cmake3 
BuildRequires: gcc-c++
BuildRequires: openssl-devel
BuildRequires: curl-devel
BuildRequires: libuuid-devel

%description
C++ SDK for amazon web services

%prep
%setup -q

%build
mkdir -p build
pushd build
cmake3 .. -DCMAKE_INSTALL_PREFIX=/usr 
#-DBUILD_ONLY="s3;sqs;sns;logs;rds;swf;queues;dynamodb;cognito-identity"
make -j8
popd

%install
pushd build
make install DESTDIR=%{buildroot}
mv %{buildroot}/usr/lib %{buildroot}/usr/lib64

popd

%post -p /sbin/ldconfig
%postun -p /sbin/ldconfig

%files
%{_libdir}/*

%package -n aws-sdk-cpp-devel
Summary:        Devel files gpstk
Group:      System Environment/Libraries
Requires:       %{name}%{?_isa} = %{version}-%{release}
License:        Apache License Version 2.0

%description -n aws-sdk-cpp-devel
Development files for gpstk.

%files -n aws-sdk-cpp-devel
%{_includedir}/aws

%exclude %{_bindir}/*

%changelog
