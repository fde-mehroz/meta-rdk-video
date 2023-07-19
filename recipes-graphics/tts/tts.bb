SUMMARY = "TTS Engine is a generic solution for text to speech"
DESCRIPTION = "This component receives text inputs from various apps and \
voices it out through gstreamer"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/../meta-rdk/licenses/Apache-2.0;md5=3b83ef96387f14655fc854ddc3c6bd57"

DEPENDS += "gstreamer1.0-plugins-base pxcore-libnode rdk-logger wpeframework wpeframework-clientlibraries libsoup-2.4"

require recipes-graphics/pxcore-libnode/pxcore-libnode-env.inc

inherit cmake

SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/ttsengine;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH}"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

CXXFLAGS += " -I${STAGING_INCDIR}/pxcore"

EXTRA_OECMAKE += "${@bb.utils.contains('DISTRO_FEATURES', 'wpe_r4', ' -DUSE_THUNDER_R4=ON', '', d)}"

INSANE_SKIP_${PN} = "dev-so"
INSANE_SKIP_${PN} += "ldflags"
FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/*.so"
FILES_${PN} += "/lib/rdk/*"
