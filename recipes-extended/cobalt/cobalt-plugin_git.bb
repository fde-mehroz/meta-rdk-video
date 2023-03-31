SUMMARY = "Cobalt plugin"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

require starboard_revision.inc

TOOLCHAIN = "gcc"

SRC_URI = "${STARBOARD_SRC_URI};protocol=${CMF_GIT_PROTOCOL};branch=master"

SRCREV = "${STARBOARD_SRCREV}"

S = "${WORKDIR}/git/plugin"

inherit cmake pkgconfig

DEPENDS = "wpeframework"

COBALT_PERSISTENTPATHPOSTFIX ?= "Cobalt-0"
COBALT_CLIENTIDENTIFIER ?= "wst-Cobalt-0"

PACKAGECONFIG ??= "focus closurepolicy"

PACKAGECONFIG[focus]  = "-DPLUGIN_COBALT_ENABLE_FOCUS_IFACE=ON,-DPLUGIN_COBALT_ENABLE_FOCUS_IFACE=OFF,"
PACKAGECONFIG[closurepolicy]  = ",-DPLUGIN_COBALT_CLOSUREPOLICY=OFF,"
PACKAGECONFIG[evegreenlite]   = "-DPLUGIN_COBALT_EVEGREEN_LITE=ON,-DPLUGIN_COBALT_EVEGREEN_LITE=OFF,libloader-app,virtual/cobalt-evergreen"
PACKAGECONFIG[evegreencompatible]  = "-DPLUGIN_COBALT_EVEGREEN_COMPATIBLE=ON,-DPLUGIN_COBALT_EVEGREEN_COMPATIBLE=OFF,libloader-app,virtual/cobalt-evergreen"
PACKAGECONFIG[libcobalt]  = "-DPLUGIN_COBALT_EVEGREEN_LITE=OFF,,libcobalt"

def get_cobalt_config(d):
    if bb.utils.contains('DISTRO_FEATURES', 'libcobalt-rdm morty', True, False, d):
       return 'libcobalt'
    if bb.utils.contains('DISTRO_FEATURES', 'cobalt-evergreen-full', True, False, d):
       return 'evegreencompatible'
    if bb.utils.contains('DISTRO_FEATURES', 'cobalt_enable_evergreen_lite', True, False, d):
       return 'evegreencompatible evegreenlite'
    return 'libcobalt'

PACKAGECONFIG_append = " ${@get_cobalt_config(d)}"

EXTRA_OECMAKE += " \
    -DCMAKE_BUILD_TYPE=Release \
    -DBUILD_SHARED_LIBS=ON \
    -DPLUGIN_COBALT_CLIENTIDENTIFIER="${COBALT_CLIENTIDENTIFIER}" \
    -DPLUGIN_COBALT_PERSISTENTPATHPOSTFIX="${COBALT_PERSISTENTPATHPOSTFIX}" \
"

FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/wpeframework/plugins/*.so"
