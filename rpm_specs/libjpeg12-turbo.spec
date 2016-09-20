Name:           libjpeg12-turbo
Version:        1.4.2
Release:        1%{?dist}
Summary:        A 12 bit library for manipulating JPEG image files
License:        IJG
URL:            http://sourceforge.net/projects/libjpeg-turbo

Source0:        http://downloads.sourceforge.net/%{name}/%{name}-%{version}.tar.gz
Patch0:         libjpeg12-turbo14-noinst.patch
Patch1:         libjpeg12-turbo-header-files.patch

BuildRequires:  autoconf
BuildRequires:  automake
BuildRequires:  libtool
BuildRequires:  nasm

%description
The libjpeg-turbo package contains a library of functions for manipulating JPEG
images. 12 bit build.

%package devel
Summary:        Headers for the libjpeg-turbo library

%description devel
This package contains header files necessary for developing programs which will
manipulate JPEG files using the libjpeg-turbo library.

%prep
%setup -q

%build
autoreconf -fiv
%configure --disable-static --with-12bit --with-jpeg8
make %{?_smp_mflags}

%install
make install install DESTDIR=%{buildroot}
find %{buildroot} -name "*.la" -delete

# hack - move the header files to a subdirectory.
mkdir -p %{buildroot}/usr/include/jpeg12
mv %{buildroot}/usr/include/jconfig.h %{buildroot}/usr/include/jpeg12/
mv %{buildroot}/usr/include/jerror.h %{buildroot}//usr/include/jpeg12/
mv %{buildroot}/usr/include/jmorecfg.h %{buildroot}/usr/include/jpeg12/
mv %{buildroot}/usr/include/jpeglib.h %{buildroot}/usr/include/jpeg12/

%check
# make test %{?_smp_mflags}

%post -p /sbin/ldconfig
%postun -p /sbin/ldconfig

# %post -n turbojpeg -p /sbin/ldconfig
# %postun -n turbojpeg -p /sbin/ldconfig

%files
# %doc README README-turbo.txt ChangeLog.txt
%{_libdir}/libjpeg12.so*
%exclude %{_libdir}/pkgconfig/libjpeg.pc
%exclude %{_libdir}/pkgconfig/libturbojpeg.pc

%{_bindir}/cjpeg12
%{_bindir}/djpeg12
%{_bindir}/jpegtran12
%{_bindir}/rdjpgcom12
%{_bindir}/wrjpgcom12

%exclude %{_bindir}/wrjpgcom
%exclude %{_mandir}/man1/cjpeg.1*
%exclude %{_mandir}/man1/djpeg.1*
%exclude %{_mandir}/man1/jpegtran.1*
%exclude %{_mandir}/man1/rdjpgcom.1*
%exclude %{_mandir}/man1/wrjpgcom.1*
%exclude %{_datadir}/doc/libjpeg-turbo/LICENSE.md
%exclude %{_datadir}/doc/libjpeg-turbo/README.ijg
%exclude %{_datadir}/doc/libjpeg-turbo/README.md
%exclude %{_datadir}/doc/libjpeg-turbo/example.c
%exclude %{_datadir}/doc/libjpeg-turbo/libjpeg.txt
%exclude %{_datadir}/doc/libjpeg-turbo/structure.txt
%exclude %{_datadir}/doc/libjpeg-turbo/usage.txt
%exclude %{_datadir}/doc/libjpeg-turbo/wizard.txt




%files devel
# %doc coderules.txt jconfig.txt libjpeg.txt structure.txt example.c
%{_includedir}/jpeg12/jconfig.h
%{_includedir}/jpeg12/jerror.h
%{_includedir}/jpeg12/jmorecfg.h
%{_includedir}/jpeg12/jpeglib.h
%{_libdir}/libjpeg12.so

%changelog
* Thu Oct 08 2015 Petr Hracek <phracek@redhat.com> - 1.4.1-2
- Fix multilib issue like jconfig.h (#1264675)

* Tue Jun 16 2015 Peter Robinson <pbrobinson@fedoraproject.org> 1.4.1-1
- new upstream version 1.4.1
- nasm available on all arches
- run tests with SMP

* Tue Jan 20 2015 Petr Hracek <phracek@redhat.com> - 1.4.0-1
- new upstream version 1.4.0 (#1180442)

* Wed Nov 26 2014 Petr Hracek <phracek@redhat.com> - 1.3.90-3
- libjpeg-turbo no longer defined macros like JPP (#1164815)

* Wed Nov 19 2014 Petr Hracek <phracek@redhat.com> - 1.3.90-2
- Resolves #1161585 Add suport for secondary arches

* Mon Oct 27 2014 Petr Hracek <phracek@redhat.com> - 1.3.90-1
- new upstream version 1.3.90
Resolves #1135375

* Sun Aug 17 2014 Fedora Release Engineering <rel-eng@lists.fedoraproject.org> - 1.3.1-4
- Rebuilt for https://fedoraproject.org/wiki/Fedora_21_22_Mass_Rebuild

* Sat Jun 07 2014 Fedora Release Engineering <rel-eng@lists.fedoraproject.org> - 1.3.1-3
- Rebuilt for https://fedoraproject.org/wiki/Fedora_21_Mass_Rebuild

* Thu Apr 17 2014 Simone Caronni <negativo17@gmail.com> - 1.3.1-2
- Re-add libjpeg-devel requirements for broken packages since Fedora 13.

* Wed Apr 16 2014 Petr Hracek <phracek@redhat.com> - 1.3.1-1
- New upstream version
- Remove upstreamed patches, add missing jpegint.h
- Clean up SPEC file
- Disable --static subpackage
- Remove libjpeg obsolency, removed in Fedora 13

* Thu Dec 19 2013 Petr Hracek <phracek@redhat.com> - 1.3.0-2
- Apply fixes CVE-2013-6629, CVE-2013-6630 (#20131737)

* Thu Jul 25 2013 Petr Hracek <phracek@redhat.com> - 1.3.0-1
- new upstream version
- no soname bump change

* Tue Mar 26 2013 Adam Tkac <atkac redhat com> - 1.2.90-2
- rebuild for ARM64 support

* Fri Feb 08 2013 Adam Tkac <atkac redhat com> 1.2.90-1
- update to 1.2.90

* Mon Feb 04 2013 Adam Tkac <atkac redhat com> 1.2.90-0.1.20130204svn922
- update to 1.2.80 snapshot (#854695)
- run `make test` during build

* Fri Jan 18 2013 Adam Tkac <atkac redhat com> 1.2.1-6
- build with jpeg6 API/ABI (jpeg8-ABI feature was dropped)

* Tue Dec 04 2012 Adam Tkac <atkac redhat com> 1.2.1-5
- change license to IJG (#877517)

* Wed Oct 24 2012 Adam Tkac <atkac redhat com> 1.2.1-4
- build with jpeg8 API/ABI (#854695)

* Thu Oct 18 2012 Adam Tkac <atkac redhat com> 1.2.1-3
- minor provides tuning (#863231)

* Thu Jul 19 2012 Fedora Release Engineering <rel-eng@lists.fedoraproject.org> - 1.2.1-2
- Rebuilt for https://fedoraproject.org/wiki/Fedora_18_Mass_Rebuild

* Mon Jul 16 2012 Adam Tkac <atkac redhat com> 1.2.1-1
- update to 1.2.1

* Thu Mar 08 2012 Adam Tkac <atkac redhat com> 1.2.0-1
- update to 1.2.0

* Fri Jan 13 2012 Fedora Release Engineering <rel-eng@lists.fedoraproject.org> - 1.1.1-4
- Rebuilt for https://fedoraproject.org/wiki/Fedora_17_Mass_Rebuild

* Mon Nov 21 2011 Orion Poplawski <orion cora nwra com> 1.1.1-3
- Make turobojpeg-devel depend on turbojpeg

* Fri Oct 7 2011 Orion Poplawski <orion cora nwra com> 1.1.1-2
- Ship the turbojpeg library (#744258)

* Mon Jul 11 2011 Adam Tkac <atkac redhat com> 1.1.1-1
- update to 1.1.1
  - ljt11-rh688712.patch merged

* Tue Mar 22 2011 Adam Tkac <atkac redhat com> 1.1.0-2
- handle broken JPEGs better (#688712)

* Tue Mar 01 2011 Adam Tkac <atkac redhat com> 1.1.0-1
- update to 1.1.0

* Tue Feb 08 2011 Fedora Release Engineering <rel-eng@lists.fedoraproject.org> - 1.0.90-2
- Rebuilt for https://fedoraproject.org/wiki/Fedora_15_Mass_Rebuild

* Mon Jan 17 2011 Adam Tkac <atkac redhat com> 1.0.90-1
- update to 1.0.90
- libjpeg-turbo10-rh639672.patch merged

* Fri Oct 29 2010 Adam Tkac <atkac redhat com> 1.0.1-3
- add support for arithmetic coded files into decoder (#639672)

* Wed Sep 29 2010 jkeating - 1.0.1-2
- Rebuilt for gcc bug 634757

* Mon Sep 13 2010 Adam Tkac <atkac redhat com> 1.0.1-1
- update to 1.0.1
  - libjpeg-turbo10-rh617469.patch merged
- add -static subpkg (#632859)

* Wed Aug 04 2010 Adam Tkac <atkac redhat com> 1.0.0-3
- fix huffman decoder to handle broken JPEGs well (#617469)

* Fri Jul 02 2010 Adam Tkac <atkac redhat com> 1.0.0-2
- add libjpeg-devel%%{_isa} provides to -devel subpkg to satisfy imlib-devel
  deps

* Fri Jul 02 2010 Adam Tkac <atkac redhat com> 1.0.0-1
- update to 1.0.0
- patches merged
  - libjpeg-turbo-programs.patch
  - libjpeg-turbo-nosimd.patch
- add libjpeg provides to the main package to workaround problems with broken
  java-1.6.0-openjdk package

* Fri Jul 02 2010 Adam Tkac <atkac redhat com> 0.0.93-13
- remove libjpeg provides from -utils subpkg

* Wed Jun 30 2010 Rex Dieter <rdieter@fedoraproject.org> 0.0.93-12
- move Obsoletes: libjpeg to main pkg

* Wed Jun 30 2010 Rex Dieter <rdieter@fedoraproject.org> 0.0.93-11
- -utils: Requires: %%name ...

* Wed Jun 30 2010 Adam Tkac <atkac redhat com> 0.0.93-10
- add Provides = libjpeg to -utils subpackage

* Mon Jun 28 2010 Adam Tkac <atkac redhat com> 0.0.93-9
- merge review related fixes (#600243)

* Wed Jun 16 2010 Adam Tkac <atkac redhat com> 0.0.93-8
- merge review related fixes (#600243)

* Mon Jun 14 2010 Adam Tkac <atkac redhat com> 0.0.93-7
- obsolete -static libjpeg subpackage (#600243)

* Mon Jun 14 2010 Adam Tkac <atkac redhat com> 0.0.93-6
- improve package description a little (#600243)
- include example.c as %%doc in the -devel subpackage

* Fri Jun 11 2010 Adam Tkac <atkac redhat com> 0.0.93-5
- don't use "fc12" disttag in obsoletes/provides (#600243)

* Thu Jun 10 2010 Adam Tkac <atkac redhat com> 0.0.93-4
- fix compilation on platforms without MMX/SSE (#600243)

* Thu Jun 10 2010 Adam Tkac <atkac redhat com> 0.0.93-3
- package review related fixes (#600243)

* Wed Jun 09 2010 Adam Tkac <atkac redhat com> 0.0.93-2
- package review related fixes (#600243)

* Fri Jun 04 2010 Adam Tkac <atkac redhat com> 0.0.93-1
- initial package
