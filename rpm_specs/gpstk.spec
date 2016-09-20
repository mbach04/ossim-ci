Name:           gpstk
Version:	2.3
Release:        1%{?dist}
Summary:	gpstk 
Group: 		Applications/Engineering
License:	LGPL        
URL:            http://trac.osgeo.org/ossim/wiki
Source: 	http://hivelocity.dl.sourceforge.net/project/%{name}/%{name}/%{version}/%{name}-%{version}.src.tar.gz

BuildRequires: 	cmake 
BuildRequires: gcc-c++
BuildRequires: ncurses-devel
BuildRequires: doxygen

%description
Algorithms and frameworks supporting the development of processing
and analysis applications in navigation and global positioning.


%prep
%setup -q

%build
mkdir -p build
pushd build
%cmake ..
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
%{_libdir}/libgpstk*

%package -n gpstk-devel
Summary:        Devel files gpstk
Group:		System Environment/Libraries
Requires:       %{name}%{?_isa} = %{version}-%{release}
License:        LGPL

%description -n gpstk-devel
Development files for gpstk.

%files -n gpstk-devel
%{_includedir}/gpstk

%exclude %{_bindir}/*

%changelog
