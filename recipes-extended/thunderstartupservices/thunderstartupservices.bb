SUMMARY = "Thunder from systemd Initialiser Service"
LICENSE = "CLOSED"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

DEPENDS = "systemd"

SRC_URI = "${RDK_COMPONENTS_ROOT_GIT}/generic/thunderstartupservices/generic;protocol=${RDK_GIT_PROTOCOL};branch=23Q2_sprint \
    file://0001-add-dependencies.patch;patchdir=${WORKDIR}/git \
"

SRCREV = "${AUTOREV}"
S = "${WORKDIR}/git/systemd/system"

SERVICES_COMMON = "\
    wpeframework-deviceinfo.service \
    wpeframework-deviceprovisioning.service \
    wpeframework-displayinfo.service \
    wpeframework-eastereggs.service \
    wpeframework-locationsync.service \
    wpeframework-maintenancemanager.service \
    wpeframework-monitor.service \
    wpeframework-network.service \
    wpeframework-ocdm.service \
    wpeframework-ocicontainer.service \
    wpeframework-packager.service \
    wpeframework-persistentstore.service \
    wpeframework-playerinfo.service \
    wpeframework-secmanager.service \
    wpeframework-firebolt.service \
    wpeframework-systemaudioplayer.service \
    wpeframework-texttospeech.service \
    wpeframework-wifi.service \
    "

do_install() {
    install -d ${D}${systemd_system_unitdir}

    for x in ${SERVICES_COMMON}; do
        install -m 0644 ${S}/${x} ${D}${systemd_system_unitdir}
        install -d ${D}${sysconfdir}/systemd/system/${x}.requires
        ln -sf ${systemd_system_unitdir}/wpeframework.service ${D}${sysconfdir}/systemd/system/${x}.requires/wpeframework.service

        install -d ${D}${sysconfdir}/systemd/system/sky.service.wants
        ln -sf ${systemd_system_unitdir}/${x} ${D}${sysconfdir}/systemd/system/sky.service.wants/${x}
    done
}

FILES_${PN} += "${systemd_system_unitdir} ${sysconfdir}/systemd/system"
