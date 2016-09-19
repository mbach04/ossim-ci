Name:          tlv
Version:        %{TLV_VERSION}
Release:        %{TLV_BUILD_RELEASE}%{?dist}
Summary:        New TLV Web Application Services
Group:          System Environment/Libraries
License:        MIT License
#URL:            http://github

%define is_systemd %(test -d /etc/systemd && echo 1 || echo 0)

# this is to stop it from compressing the jar files so we do not get nested zips because the
# jars are already zipped
%define __os_install_post %{nil}

%description
Time Lapse Viewer web application.

%build

%install
export APP=time_lapse
pushd %{_builddir}/install
  # Install all files with default permissions
  for x in `find share`; do
    if [ -f $x ] ; then
      install -p -m644 -D $x %{buildroot}/usr/$x;
    fi
  done
  # Loop through each app and sym link to the versioned app
  if [ -d %{buildroot}%{_datadir}/omar/${APP} ]; then
    pushd %{buildroot}%{_datadir}/omar/${APP}
      if [ -L ${APP}.jar ]; then
       unlink ${APP}.jar
      fi
      if [ ! -f ${APP}.jar ]; then
        ln -s ${APP}-*.jar ${APP}.jar
      fi
      chmod 755 *.sh
    popd
  fi  
  chmod 755 `find %{buildroot}%{_datadir}/omar -type d`

%if %{is_systemd}
  for x in `find lib/systemd/system` ; do
    if [ -f $x ] ; then
      install -p -m755 -D $x %{buildroot}/usr/$x;
    fi
  done
%else
  for x in `find etc/init.d` ; do
    if [ -f $x ] ; then
      install -p -m755 -D $x %{buildroot}/$x;
    fi
  done
%endif

popd

%pre
export USER_NAME=omar
export APP_NAME=time_lapse
if ! id -u omar > /dev/null 2>&1; then 
  adduser -r -d /usr/share/omar -s /bin/false --no-create-home --user-group ${USER_NAME}
fi


%post
export USER_NAME=omar
export APP_NAMEtime_lapse

chown -R ${USER_NAME}:${USER_NAME} %{_datadir}/omar
if [ ! -d "/var/log/${APP_NAME}" ] ; then
  mkdir /var/log/${APP_NAME}
fi
if [ ! -d "/var/run/${APP_NAME}" ] ; then
  mkdir /var/run/${APP_NAME}
fi

chown -R ${USER_NAME}:${USER_NAME}  /var/log/${APP_NAME}
chmod 755 /var/log/${APP_NAME}
chown -R ${USER_NAME}:${USER_NAME}  /var/run/${APP_NAME}
chmod 755 /var/run/${APP_NAME}

%preun
export APP_NAME=time_lapse
ps -ef | grep $APP_NAME | grep -v grep
if [ $? -eq "0" ] ; then
%if %{is_systemd}
systemctl stop $APP_NAME
%else
service $APP_NAME stop
%endif
  if [ "$?" -eq "0" ]; then
     echo "Service $APP_NAME stopped successfully"
  else
     echo "Problems stopping $APP_NAME.  Ignoring..."
  fi
else
  echo "Service ${APP_NAME} is not running and will not be stopped."
fi

%postun
export APP_NAME=time_lapse
rm -rf /var/log/${APP_NAME}
rm -rf /var/run/${APP_NAME}
rm -rf /usr/share/omar/${APP_NAME}

%files
%{_datadir}/omar/time_lapse
%if %{is_systemd}
/usr/lib/systemd/system/time_lapse.service
%else
%{_sysconfdir}/init.d/time_lapse
%endif
