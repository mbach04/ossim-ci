# The name of the package.
Name: hdf5a


Version: 1.8.17
			# Version of the package contained in the RPM.


Release: %{BUILD_RELEASE}%{?dist}
			# Version of the RPM.


License: BSD-style		
			# Licensing Terms


Group: Development/Libraries	
			# Group, identifies types of software. Used by users to manage multiple RPMs.

Source0: hdf5a-1.8.17.tar.gz	

			#Source tar ball name
URL: http://www.hdfgroup.org/HDF5		

%define cmake cmake3
			# URL to find package
Buildroot: %{_tmppath}/%{name}-%{version}-%{release}-root	


			#used with non-root builds of RPM files
BuildRequires: gcc-c++
BuildRequires: gcc-gfortran
BuildRequires: szip-devel
BuildRequires: zlib-devel

Summary:  HDF5 is a unique technology suite that makes possible the management of extremely large and complex data collections.
			# One line summary of package


%description					

			# Full description. Can be multiple lines.
The HDF5 technology suite includes:

    * A versatile data model that can represent very complex data objects and a wide variety of metadata.

    * A completely portable file format with no limit on the number or size of data objects in the collection.

    * A software library that runs on a range of computational platforms, from laptops to massively parallel systems, and implements a high-level API with C, C++, Fortran 90, and Java interfaces.

    * A rich set of integrated performance features that allow for access time and storage space optimizations.

    * Tools and applications for managing, manipulating, viewing, and analyzing the data in the collection.

The HDF5 data model, file format, API, library, and tools are open and distributed without charge.

%package devel
Summary: Header files and compile scripts for developing software using the HDF5 library.
Group: Development/Libraries
Requires: %{name}%{?_isa} = %{version}-%{release}

%description devel

Header files and compile scripts for developing software using the HDF5 library.

The HDF5 technology suite includes:

    * A versatile data model that can represent very complex data objects and a wide variety of metadata.

    * A completely portable file format with no limit on the number or size of data objects in the collection.

    * A software library that runs on a range of computational platforms, from laptops to massively parallel systems, and implements a high-level API with C, C++, Fortran 90, and Java interfaces.

    * A rich set of integrated performance features that allow for access time and storage space optimizations.

    * Tools and applications for managing, manipulating, viewing, and analyzing the data in the collection.

The HDF5 data model, file format, API, library, and tools are open and distributed without charge.



%prep				
			#prep: list steps after this to unpack the package.			
#---
# Notes for debugging: 
# -D on setup = Do not delete the directory before unpacking.
# -T on setup = Disable the automatic unpacking of the archives.
#---
# %setup -q -D -T
%setup -q
			# setup is a macro used to unpack the package with default settings (i.e., gunzip, untar)

%build				
			#build: steps after this should compile the package
			#macro used to configure the package with standard ./configure command

mkdir -p build

pushd build

%cmake \
-DCMAKE_INSTALL_PREFIX=/usr \
-DBUILD_SHARED_LIBS=ON \
-DBUILD_TESTING=OFF \
-DCMAKE_BUILD_TYPE=RELEASE \
-DHDF5_BUILD_CPP_LIB=ON \
-DHDF5_BUILD_EXAMPLES=OFF \
-DHDF5_BUILD_FORTRAN=OFF \
-DHDF5_BUILD_HL_LIB=OFF \
-DHDF5_BUILD_TOOLS=OFF \
-DHDF5_ENABLE_Z_LIB_SUPPORT=ON \
-DHDF5_ENABLE_SZIP_SUPPORT=ON \
-DHDF5_INSTALL_LIB_DIR=/usr/lib64 \
..

make VERBOSE=1 %{?_smp_mflags}

popd


%install			
			#install: steps after this will install the package.

			#used with non-root builds of RPM files.
pushd build
make install DESTDIR=$RPM_BUILD_ROOT	
popd
			#performs a make install

                        # Extract szip files again for installing.  This can't 
                        # be done before the install step, which now deletes 
                        # everthing in $RPM_BUILD_ROOT before installing files there.
                        # Delete files that we won't install

#
#  Post-install-Script
#
%post
/sbin/ldconfig

%post devel

%clean				
			#performs a make clean after the install
rm -rf $RPM_BUILD_ROOT		

			#used with non-root builds of RPM files.

%postun
/sbin/ldconfig

%files				
			#files should be followed by a list of all files that get installed by the main package.
%{_libdir}/libhdf5.settings
%{_libdir}/libhdf5a.so*
%{_libdir}/libhdf5a.a*
%{_libdir}/libhdf5a_cpp.so*
%{_libdir}/libhdf5a_cpp.a*

%files devel
                        #files to be installed by the hdf5-devel package.
%defattr(0755,root,root)
%{_includedir}/*.h
                        # These three files are installed by both the main 
                        # and devel packages, to provide release, version 
                        # and copyright information.
# %doc ./COPYING
# %doc ./release_docs/RELEASE.txt
# %dir %{_datadir}/hdf5_examples
# %{_datadir}/hdf5_examples/*.sh

%exclude %{_datadir}/COPYING
%exclude %{_datadir}/RELEASE.txt
%exclude %{_datadir}/USING_HDF5_CMake.txt
%exclude %{_datadir}/cmake/FindHDF5.cmake
%exclude %{_datadir}/cmake/hdf5-config-version.cmake
%exclude %{_datadir}/cmake/hdf5-config.cmake
%exclude %{_datadir}/cmake/hdf5-targets-release.cmake
%exclude %{_datadir}/cmake/hdf5-targets.cmake
%exclude %{_datadir}/cmake/libhdf5.settings

