SUMMARY = "Tvmgr plugins"
DESCRIPTION = "RDK tvmgr-plugins. These are the plugins encoding and \
decoding tvsettings elements." 
PROVIDES = "TvMgr-plugins"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

inherit cmake pkgconfig

include recipes-extended/rdkservices/include/memory-check-support.inc

DEPENDS += " wpeframework wpeframework-tools-native rfc tvsettings-hal-headers virtual/tvsettings-hal devicesettings jsoncpp boost"
RDEPENDS_${PN} += " bash devicesettings"

SRC_URI = "${CMF_RDK_COMPONENTS_ROOT_GIT}/opensource/tvsettings;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/tvmgr-plugins"
CXXFLAGS_append = " -I${STAGING_INCDIR}/WPEFramework"

EXTRA_OECMAKE += " -DPYTHON_EXECUTABLE=${STAGING_BINDIR_NATIVE}/python3-native/python3"

INCLUDE_DIRS = " \
    -I${PKG_CONFIG_SYSROOT_DIR}/usr/include/WPEFramework/ \
    -I${PKG_CONFIG_SYSROOT_DIR}/usr/include/WPEFramework/websockets/ \
    "

FILES_${PN} += "${libdir}/wpeframework/plugins/*.so*"
FILES_${PN}-dbg += "${libdir}/wpeframework/plugins/.debug/*.so"

do_configure_append() {
        # Configure leaves below files changed, thus causing sha change for subsequent builds, causing rebuilds
	cd ${S}
	git update-index --assume-unchanged ${S}/TVMgr.json
	cd -
}
