SUMMARY = "RDK services plugins"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=16cf2209d4e903e4d5dcd75089d7dfe2"

PR = "r1"
PV = "3.0+git${SRCPV}"

S = "${WORKDIR}/git"
inherit cmake pkgconfig

SRC_URI = "git://github.com/rdkcentral/rdkservices.git;protocol=git;branch=main \
           file://0002-RDK-28534-Comcast-Security-Agent-changes.patch \
           file://index.html \
           file://osmc-devinput-remote.json \
           file://thunder_acl.json \
           file://rdkshell_post_startup.conf \
           file://0003-set-OCDM-sharepath-to-tmp-OCDM.patch \
           ${@bb.utils.contains('DISTRO_FEATURES', 'systimemgr','file://0001-Added-Support-for-querying-and-publishing-the-Secure.patch','',d)} \
           file://0001-RDK-31882-Add-GstCaps-parsing-in-OCDM-to-rdkservices.patch \
           file://0001-add_gstcaps_forcobalt_mediatype.patch \
           file://0001-Add-monitoring-of-cloned-callsigns.patch \
           file://rdkservices.ini \
           file://0001-RDKTV-11792-increase-retry-count.patch \
           file://0001-SERXIONE-600-LocationSync-Network-check-timer.patch \
           file://0001-RDKTV-20749-Revert-Merge-pull-request-3336-from-npol.patch \
           file://0001-RDK-39924-Launch-Airplay-App-from-Airplay-icon-sel.patch \
           file://0001-rdkservices_cbcs_changes.patch \
           file://0001-RDK-41681-PR4013.patch \
           file://0002-Adding-Support-For-R4.patch \
          "

# July 24 2023
SRCREV = "4b50067797574dde71e80c30198f93b5e2e6e857"

TOOLCHAIN = "gcc"
EXTRA_OECMAKE += "${@bb.utils.contains('DISTRO_FEATURES', 'wpe_r4', ' -DUSE_THUNDER_R4=ON', '', d)}"

DEPENDS += "wpeframework wpeframework-tools-native"
RDEPENDS_${PN} += "wpeframework"

TARGET_LDFLAGS += " -Wl,--no-as-needed -ltelemetry_msgsender -Wl,--as-needed "

CXXFLAGS += " -I${STAGING_DIR_TARGET}${includedir}/wdmp-c/ "
CXXFLAGS += " -I${STAGING_DIR_TARGET}${includedir}/trower-base64/ "
CXXFLAGS += "${@bb.utils.contains('DISTRO_FEATURES', 'build_rfc_enabled',"-DRFC_ENABLED ","", d)}"
CXXFLAGS += " -Wall -Werror "
SELECTED_OPTIMIZATION_append = " -Wno-deprecated-declarations"

# More complicated plugins are moved seperate includes
include include/compositor.inc
include include/ocdm.inc
include include/power.inc
include include/remotecontrol.inc
include include/snapshot.inc
include include/webkitbrowser.inc
include include/packager.inc
include include/texttospeech.inc

WPEFRAMEWORK_LOCATIONSYNC_URI ?= "http://jsonip.metrological.com/?target=wpe"

# ----------------------------------------------------------------------------

PACKAGECONFIG ?= " \
    breakpadsupport \
    telemetrysupport \
    ${WPE_SNAPSHOT} \
    activitymonitor avinput continuewatching datacapture devicediagnostics \
    displaysettings framerate hdcpprofile hdmicec hdmiinput loggingpreferences \
    screencapture securityagent stateobserver \
    systemservices timer userpreferences warehouse monitor locationsync texttospeech persistent_store airplayservice \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'bluetoothcontrol', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opencdm',              'opencdmi', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'playready_nexus_svp',  'opencdmi_prnx_svp', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'widevine_nexus_svp',   'opencdmi_wv_svp', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'clearkey',             'opencdmi_ck', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'rdkshell',             'rdkshell', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'DOBBY_CONTAINERS',     'ocicontainer', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'wifimanager network', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'enable_maintenance_manager', 'maintenancemanager', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'fireboltmediaplayer', 'fireboltmediaplayer', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'dlnasupport', ' dlna', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'flex2_rdk', ' hdmicecsource', ' ', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wpe_r4', ' messagecontrol ', ' tracecontrol messenger ', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systimemgr', 'systimemgrsupport', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'ctrlm', 'controlservice voicecontrol remotecontrol remoteactionmapping', '', d)} \
"

EXTRA_OECMAKE += "${@bb.utils.contains('DISTRO_FEATURES', 'rdkshell_disable_autostart', ' -DPLUGIN_RDKSHELL_AUTOSTART=false ', ' -DPLUGIN_RDKSHELL_AUTOSTART=true ', d)}"
EXTRA_OECMAKE += "${@bb.utils.contains_any('DISTRO_FEATURES', 'rdkshell_ra second_form_factor', ' -DPLUGIN_RDKSHELL_AUTOSTART=true ', ' ', d)}"

EXTRA_OECMAKE += "-DDISABLE_GEOGRAPHY_TIMEZONE=ON"

PACKAGECONFIG[breakpadsupport]      = ",,breakpad-wrapper,breakpad-wrapper"
PACKAGECONFIG[telemetrysupport]     = "-DBUILD_ENABLE_TELEMETRY_LOGGING=ON,,telemetry,telemetry"
PACKAGECONFIG[activitymonitor]      = "-DPLUGIN_ACTIVITYMONITOR=ON,-DPLUGIN_ACTIVITYMONITOR=OFF,"
PACKAGECONFIG[avinput]              = "-DPLUGIN_AVINPUT=ON,-DPLUGIN_AVINPUT=OFF,iarmbus iarmmgrs devicesettings,iarmbus devicesettings"
PACKAGECONFIG[bluetoothcontrol]     = "-DPLUGIN_BLUETOOTH=ON -DPLUGIN_BLUETOOTH_AUTOSTART=true,-DPLUGIN_BLUETOOTH=OFF,iarmbus iarmmgrs bluetooth-mgr,bluez5 iarmbus bluetooth-mgr"
PACKAGECONFIG[continuewatching]     = "-DPLUGIN_CONTINUEWATCHING=ON,-DPLUGIN_CONTINUEWATCHING=OFF,rfc trower-base64 openssl,rfc trower-base64 openssl"
PACKAGECONFIG[controlservice]       = "-DPLUGIN_CONTROLSERVICE=ON,-DPLUGIN_CONTROLSERVICE=OFF,iarmbus iarmmgrs ctrlm-headers,iarmbus ctrlm"
PACKAGECONFIG[voicecontrol]         = "-DPLUGIN_VOICECONTROL=ON,-DPLUGIN_VOICECONTROL=OFF,iarmbus iarmmgrs ctrlm-headers,iarmbus ctrlm"
PACKAGECONFIG[remotecontrol]        = "-DPLUGIN_REMOTECONTROL=ON,-DPLUGIN_REMOTECONTROL=OFF,iarmbus iarmmgrs ctrlm-headers,iarmbus ctrlm"
PACKAGECONFIG[datacapture]          = "-DPLUGIN_DATACAPTURE=ON, -DPLUGIN_DATACAPTURE=OFF,iarmbus iarmmgrs audiocapturemgr curl,iarmbus audiocapturemgr curl"
PACKAGECONFIG[devicediagnostics]    = "-DPLUGIN_DEVICEDIAGNOSTICS=ON,-DPLUGIN_DEVICEDIAGNOSTICS=OFF,curl,curl"
PACKAGECONFIG[deviceinfo]           = "-DPLUGIN_DEVICEINFO=ON,-DPLUGIN_DEVICEINFO=OFF,iarmbus iarmmgrs rfc devicesettings wpeframework-interfaces,iarmbus rfc devicesettings wpeframework-interfaces"
PACKAGECONFIG[deviceidentification] = "-DPLUGIN_DEVICEIDENTIFICATION=ON,-DPLUGIN_DEVICEIDENTIFICATION=OFF,wpeframework-interfaces,wpeframework-interfaces"
PACKAGECONFIG[dictionary]           = "-DPLUGIN_DICTIONARY=ON,-DPLUGIN_DICTIONARY=OFF,"
PACKAGECONFIG[displayinfo]          = "-DPLUGIN_DISPLAYINFO=ON  -DUSE_DEVICESETTINGS=1,-DPLUGIN_DISPLAYINFO=OFF,iarmbus iarmmgrs wpeframework-interfaces devicesettings,iarmbus wpeframework-interfaces devicesettings"
PACKAGECONFIG[displaysettings]      = "-DPLUGIN_DISPLAYSETTINGS=ON,-DPLUGIN_DISPLAYSETTINGS=OFF,iarmbus iarmmgrs rfc devicesettings,iarmbus rfc devicesettings"
PACKAGECONFIG[framerate]            = "-DPLUGIN_FRAMERATE=ON,-DPLUGIN_FRAMERATE=OFF,iarmbus iarmmgrs devicesettings,iarmbus devicesettings"
PACKAGECONFIG[hdcpprofile]          = "-DPLUGIN_HDCPPROFILE=ON,-DPLUGIN_HDCPPROFILE=OFF,iarmbus iarmmgrs devicesettings,iarmbus devicesettings"
PACKAGECONFIG[hdmicec]              = "-DPLUGIN_HDMICEC=ON,-DPLUGIN_HDMICEC=OFF,iarmbus iarmmgrs devicesettings hdmicec,iarmbus devicesettings hdmicec"
PACKAGECONFIG[hdmicec2]             = "-DPLUGIN_HDMICEC2=ON,-DPLUGIN_HDMICEC2=OFF,iarmbus iarmmgrs devicesettings hdmicec,iarmbus devicesettings hdmicec"
PACKAGECONFIG[hdmicecsink]          = "-DPLUGIN_HDMICECSINK=ON,,iarmbus iarmmgrs devicesettings hdmicec,iarmbus devicesettings hdmicec"
PACKAGECONFIG[hdmicecsource]        = "-DPLUGIN_HDMICECSOURCE=ON,-DPLUGIN_HDMICECSOURCE=OFF,iarmbus iarmmgrs devicesettings hdmicec,iarmbus devicesettings hdmicec"
PACKAGECONFIG[hdmiinput]            = "-DPLUGIN_HDMIINPUT=ON,-DPLUGIN_HDMIINPUT=OFF,iarmbus iarmmgrs devicesettings,iarmbus devicesettings"
PACKAGECONFIG[ledcontrol]           = "-DPLUGIN_LEDCONTROL=ON,,iarmbus iarmmgrs devicesettings,iarmbus devicesettings"
PACKAGECONFIG[compositeinput]       = "-DPLUGIN_COMPOSITEINPUT=ON,-DPLUGIN_COMPOSITEINPUT=OFF,iarmbus iarmmgrs devicesettings,iarmbus devicesettings"
PACKAGECONFIG[ioconnector]          = "-DPLUGIN_IOCONNECTOR=ON,-DPLUGIN_IOCONNECTOR=OFF,"
PACKAGECONFIG[jsonrpcplugin]        = "-DPLUGIN_JSONRPC=ON,-DPLUGIN_JSONRPC=OFF,"
PACKAGECONFIG[locationsync]         = "-DPLUGIN_LOCATIONSYNC=ON \
                                       -DPLUGIN_LOCATIONSYNC_URI=${WPEFRAMEWORK_LOCATIONSYNC_URI} \
                                      ,-DPLUGIN_LOCATIONSYNC=OFF,wpeframework-interfaces,wpeframework-interfaces"
PACKAGECONFIG[loggingpreferences]   = "-DPLUGIN_LOGGINGPREFERENCES=ON,-DPLUGIN_LOGGINGPREFERENCES=OFF,iarmbus iarmmgrs,iarmbus"
PACKAGECONFIG[messenger]            = "-DPLUGIN_MESSENGER=ON,-DPLUGIN_MESSENGER=OFF,wpeframework-interfaces,wpeframework-interfaces"
PACKAGECONFIG[monitor]              = "-DPLUGIN_MONITOR=ON ${MONITOR_PLUGIN_ARGS},-DPLUGIN_MONITOR=OFF,wpeframework-interfaces,wpeframework-interfaces"
PACKAGECONFIG[motiondetection]      = "-DPLUGIN_MOTION_DETECTION=ON,,motiondetector-hal,motiondetector-hal"
PACKAGECONFIG[network]              = "-DPLUGIN_NETWORK=ON,-DPLUGIN_NETWORK=OFF,iarmbus iarmmgrs rfc,iarmbus rfc netsrvmgr"
PACKAGECONFIG[persistent_store]     = "-DPLUGIN_PERSISTENTSTORE=ON,-DPLUGIN_PERSISTENTSTORE=OFF,sqlite3 wpeframework-interfaces,wpeframework-interfaces"
PACKAGECONFIG[playerinfo]           = "-DPLUGIN_PLAYERINFO=ON -DUSE_DEVICESETTINGS=1,-DPLUGIN_PLAYERINFO=OFF,iarmbus iarmmgrs wpeframework-interfaces devicesettings gstreamer1.0,iarmbus wpeframework-interfaces devicesettings gstreamer1.0"
PACKAGECONFIG[rdkshell]             = "-DPLUGIN_RDKSHELL=ON,-DPLUGIN_RDKSHELL=OFF,rdkshell wpeframework-interfaces,rdkshell wpeframework-interfaces"
PACKAGECONFIG[remoteactionmapping]  = "-DPLUGIN_REMOTEACTIONMAPPING=ON,-DPLUGIN_REMOTEACTIONMAPPING=OFF,iarmbus iarmmgrs ctrlm-headers,iarmbus ctrlm"
PACKAGECONFIG[rustadapter]          = "-DPLUGIN_RUSTADAPTER=ON,,,"
PACKAGECONFIG[screencapture]        = "-DPLUGIN_SCREENCAPTURE=ON,-DPLUGIN_SCREENCAPTURE=OFF,wpeframework-interfaces curl libpng,wpeframework-interfaces curl libpng"
PACKAGECONFIG[securityagent]        = "-DPLUGIN_SECURITYAGENT=ON -DPLUGIN_SECURITYAGENT_DAC_FOLDER=/opt/secure/securityagent/dac,-DPLUGIN_SECURITYAGENT=OFF,wpeframework-interfaces,wpeframework-interfaces"
PACKAGECONFIG[stateobserver]        = "-DPLUGIN_STATEOBSERVER=ON,-DPLUGIN_STATEOBSERVER=OFF,iarmbus iarmmgrs cjson,iarmbus cjson"
PACKAGECONFIG[systemaudioplayer]    = "-DPLUGIN_SYSTEMAUDIOPLAYER=ON,,wpeframework-interfaces trower-base64 boost websocketpp wpeframework-clientlibraries openssl gstreamer1.0 gstreamer1.0-plugins-base,wpeframework-interfaces trower-base64 wpeframework-clientlibraries openssl gstreamer1.0 gstreamer1.0-plugins-base"
PACKAGECONFIG[systemdconnector]     = "-DPLUGIN_SYSTEMDCONNECTOR=ON,-DPLUGIN_SYSTEMDCONNECTOR=OFF,"
PACKAGECONFIG[maintenancemanager]   = "-DPLUGIN_MAINTENANCEMANAGER=ON,-DPLUGIN_MAINTENANCEMANAGER=OFF,iarmbus iarmmgrs rfc,iarmbus rfc"
PACKAGECONFIG[systemservices]       = "-DPLUGIN_SYSTEMSERVICES=ON,-DPLUGIN_SYSTEMSERVICES=OFF,iarmbus iarmmgrs rfc devicesettings curl,tzcode iarmbus rfc devicesettings curl"
PACKAGECONFIG[systimemgrsupport]    = "-DBUILD_ENABLE_SYSTIMEMGR_SUPPORT=ON,,systimemgrinetrface,"
PACKAGECONFIG[telemetry]            = "-DPLUGIN_TELEMETRY=ON,,iarmbus iarmmgrs rfc rbus,iarmbus rfc rbus"
PACKAGECONFIG[timer]                = "-DPLUGIN_TIMER=ON,-DPLUGIN_TIMER=OFF,iarmbus iarmmgrs,iarmbus"
PACKAGECONFIG[timesync]             = "-DPLUGIN_TIMESYNC=ON,-DPLUGIN_TIMESYNC=OFF,"
PACKAGECONFIG[tracecontrol]         = "-DPLUGIN_TRACECONTROL=ON,-DPLUGIN_TRACECONTROL=OFF,wpeframework-interfaces,wpeframework-interfaces"
PACKAGECONFIG[userpreferences]      = "-DPLUGIN_USERPREFERENCES=ON,-DPLUGIN_USERPREFERENCES=OFF,glib-2.0,glib-2.0"
PACKAGECONFIG[virtualinput]         = "-DPLUGIN_COMPOSITOR_VIRTUALINPUT=ON,-DPLUGIN_COMPOSITOR_VIRTUALINPUT=OFF,"
PACKAGECONFIG[warehouse]            = "-DPLUGIN_WAREHOUSE=ON,-DPLUGIN_WAREHOUSE=OFF,iarmbus iarmmgrs rfc devicesettings,iarmbus rfc devicesettings"
PACKAGECONFIG[webproxy]             = "-DPLUGIN_WEBPROXY=ON,-DPLUGIN_WEBPROXY=OFF,"
PACKAGECONFIG[webserver]            = "-DPLUGIN_WEBSERVER=ON \
                                       -DPLUGIN_WEBSERVER_PORT="${PLUGIN_WEBSERVER_PORT}" \
                                       -DPLUGIN_WEBSERVER_PATH="${PLUGIN_WEBSERVER_PATH}" \
                                      ,-DPLUGIN_WEBSERVER=OFF,"
PACKAGECONFIG[webshell]             = "-DPLUGIN_WEBSHELL=ON,-DPLUGIN_WEBSHELL=OFF,"
PACKAGECONFIG[wifi]                 = "-DPLUGIN_WIFICONTROL=ON,-DPLUGIN_WIFICONTROL=OFF,,wpa-supplicant"
PACKAGECONFIG[wifi_rdkhal]          = "-DPLUGIN_USE_RDK_HAL_WIFI=ON,-DPLUGIN_USE_RDK_HAL_WIFI=OFF,,wifi-hal"
PACKAGECONFIG[wifimanager]          = "-DPLUGIN_WIFIMANAGER=ON,-DPLUGIN_WIFIMANAGER=OFF,netsrvmgr iarmbus iarmmgrs,iarmbus"
PACKAGECONFIG[xcast]                = "-DPLUGIN_XCAST=ON,-DPLUGIN_XCAST=OFF,iarmbus iarmmgrs rfc pxcore-libnode rtremote,iarmbus rfc pxcore-libnode rtremote"
PACKAGECONFIG[ocicontainer]         = "-DPLUGIN_OCICONTAINER=ON, -DPLUGIN_OCICONTAINER=OFF, dobby systemd, dobby systemd"
PACKAGECONFIG[usbaccess]            = "-DPLUGIN_USBACCESS=ON,-DPLUGIN_USBACCESS=OFF,iarmbus iarmmgrs udev,iarmbus udev"
PACKAGECONFIG[erm]                  = "-DBUILD_ENABLE_ERM=ON,-DBUILD_ENABLE_ERM=OFF,essos,essos"
PACKAGECONFIG[fireboltmediaplayer]  = "-DPLUGIN_FIREBOLTMEDIAPLAYER=ON,-DPLUGIN_FIREBOLTMEDIAPLAYER=OFF, aamp wpeframework-interfaces, aamp wpeframework-interfaces"
PACKAGECONFIG[dlna]                 = "-DPLUGIN_DLNA_SERVICE=ON, -DPLUGIN_DLNA_SERVICE=OFF,xupnp-rpc,xupnp-rpc"
PACKAGECONFIG[mediaenginermf]       = "-DPLUGIN_MEDIAENGINERMF=ON,-DPLUGIN_MEDIAENGINERMF=OFF,rmfgeneric, rmfgeneric"
PACKAGECONFIG[airplayservice]       = "-DPLUGIN_AIRPLAYSERVICE=ON,-DPLUGIN_PLUGIN_AIRPLAYSERVICE=OFF,"
PACKAGECONFIG[messagecontrol]       = "-DPLUGIN_MESSAGECONTROL=ON,-DPLUGIN_MESSAGECONTROL=OFF,wpeframework-interfaces,wpeframework-interfaces"

# ----------------------------------------------------------------------------

MONITOR_PLUGIN_ARGS                ?= " \
                                       -DPLUGIN_WEBKITBROWSER_MEMORYLIMIT=614400 \
                                       -DPLUGIN_YOUTUBE_MEMORYLIMIT=614400 \
                                       -DPLUGIN_NETFLIX_MEMORYLIMIT=307200 \
                                       -DPLUGIN_MONITOR_CLONED_APPS=ON -DPLUGIN_MONITOR_CLONED_APP_MEMORYLIMIT=657408 \
                                       -DPLUGIN_MONITOR_SEARCH_AND_DISCOVERY_MEMORYLIMIT=888832 \
                                       -DPLUGIN_MONITOR_NETFLIX_APP_MEMORYLIMIT=1048576 \
"

EXTRA_OECMAKE += " \
    -DBUILD_REFERENCE=${SRCREV} \
    -DBUILD_SHARED_LIBS=ON \
    -DSECAPI_LIB=sec_api \
"

do_install_append() {
    install -m 0644 ${WORKDIR}/thunder_acl.json ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/rdkshell_post_startup.conf ${D}${sysconfdir}
    if ${@bb.utils.contains_any("DISTRO_FEATURES", "rdkshell_ra second_form_factor", "true", "false", d)}
    then
      install -d ${D}${sysconfdir}/rfcdefaults
      install -m 0644 ${WORKDIR}/rdkservices.ini ${D}${sysconfdir}/rfcdefaults/
    fi
}

# ----------------------------------------------------------------------------

FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/wpeframework/plugins/*.so ${libdir}/*.so ${datadir}/WPEFramework/* ${sysconfdir}/thunder_acl.json"

INSANE_SKIP_${PN} += "libdir staticdev dev-so"
INSANE_SKIP_${PN}-dbg += "libdir"
