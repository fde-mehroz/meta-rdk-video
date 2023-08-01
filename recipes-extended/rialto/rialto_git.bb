SUMMARY = "Rialto"
LICENSE  = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=327e572d11c37963726ba0b02d30cf2c"

require rialto_revision.inc

SRC_URI = "${CMF_GITHUB_ROOT}/rialto;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GITHUB_MASTER_BRANCH}"
SRCREV = "${@bb.utils.contains('DISTRO_FEATURES', 'rialto_head', '${AUTOREV}', '${RIALTO_VERSION}', d)}"

DEPENDS = "openssl jsoncpp protobuf protobuf-native"

S = "${WORKDIR}/git"
inherit cmake coverity features_check

PACKAGES =+ "${PN}-client ${PN}-server ${PN}-servermanager-lib ${PN}-servermanager"

PACKAGECONFIG[server] = "-DENABLE_SERVER=ON,-DENABLE_SERVER=OFF,wpeframework-clientlibraries gstreamer1.0 gstreamer1.0-plugins-base glib-2.0 rdk-gstreamer-utils"
PACKAGECONFIG[servermanager] = "-DENABLE_SERVER_MANAGER=ON,-DENABLE_SERVER_MANAGER=OFF,mongoose,"

# The 'servermanager' package config has a runtime
# dependency on the 'RialtoServer' executable and as such
# requires the 'server' package config to be enabled as well.
PACKAGECONFIG ??= "server servermanager"

FILES_${PN}-client += "${libdir}/libRialtoClient.so.*"

FILES_${PN}-server += "${bindir}/RialtoServer"

FILES_${PN}-servermanager += "${bindir}/RialtoServerManagerSim"
RDEPENDS_${PN}-servermanager += "${PN}-server"

FILES_${PN}-servermanager-lib += "${libdir}/libRialtoServerManager.so.*"

RDEPENDS_rialto-server += " rdk-gstreamer-utils"

REQUIRED_DISTRO_FEATURES = "enable_rialto"

RDEPENDS_${PN} += "protobuf mongoose"

IMAGE_INSTALL_append = " rialto"
