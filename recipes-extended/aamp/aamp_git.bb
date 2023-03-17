SUMMARY = "RDK AAMP component"
SECTION = "console/utils"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=97dd37dbf35103376811825b038fc32b"

PV = "${RDK_RELEASE}"
SRCREV_aamp = "${AUTOREV}"
SRCREV_FORMAT = "aamp"

DEPENDS += "curl libdash libxml2 cjson iarmmgrs aampabr aampmetrics wpeframework"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'gstreamer1', 'gstreamer1.0  gstreamer1.0-plugins-base', 'gstreamer gst-plugins-base', d)}"
RDEPENDS_${PN} += "gst-plugins-rdk-aamp devicesettings"

NO_RECOMMENDATIONS = "1"

AAMP_RELEASE_TAG_NAME ?= "5.6.1.0"

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/aamp;protocol=${CMF_GIT_PROTOCOL};branch=${AAMP_RELEASE_TAG_NAME};nobranch=1;name=aamp"
SRC_URI += "${@bb.utils.contains('AAMP_RELEASE_TAG_NAME', 'dev_sprint_22_1', 'file://0001-Updated-Find-JSC-include-path.patch', 'file://0001-Find-JSC-include-path.patch', d)}"
SRC_URI += "file://aamp-opencdm-cbcs.patch"

S = "${WORKDIR}/git/"

DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'webkit', 'qtwebkit', '', d)}"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'rdkbrowser2', '${WPEWEBKIT}', '', d)}"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'webkitbrowser-plugin', '${WPEWEBKIT}', '', d)}"
DEPENDS_remove_rpi = "${@bb.utils.contains('DISTRO_FEATURES', 'webkit', 'qtwebkit', '', d)}"


require aamp-common.inc

PACKAGECONFIG_append = " playready widevine clearkey"

EXTRA_OECMAKE_remove_rpi = "${@bb.utils.contains('DISTRO_FEATURES', 'webkit', ' -DCMAKE_QT5WEBKIT_JSBINDINGS=1', '', d)}"
PACKAGES = "${PN} ${PN}-dev ${PN}-dbg"

FILES_${PN} += "${libdir}/lib*.so"
FILES_${PN} += "${libdir}/aamp-cli"
FILES_${PN} += "${libdir}/aamp/lib*.so"
FILES_${PN} +="${libdir}/gstreamer-1.0/lib*.so"
FILES_${PN}-dbg +="${libdir}/gstreamer-1.0/.debug/*"

INSANE_SKIP_${PN} = "dev-so"

CXXFLAGS += " -DAAMP_BUILD_INFO=${AAMP_RELEASE_TAG_NAME}" 

do_install_append() {
    echo "Installing aamp-cli..."
    install -m755 ${B}/aamp-cli ${D}${libdir}
}
