SUMMARY = "Cobalt plugin"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

require starboard_revision.inc

TOOLCHAIN = "gcc"

SRC_URI = "${STARBOARD_SRC_URI};protocol=${CMF_GIT_PROTOCOL};nobranch=1"

SRCREV = "${STARBOARD_SRCREV}"

S = "${WORKDIR}/git/plugin"

inherit cmake pkgconfig

DEPENDS = "wpeframework wpeframework-tools-native"

COBALT_PERSISTENTPATHPOSTFIX ?= "Cobalt-0"
COBALT_CLIENTIDENTIFIER ?= "wst-Cobalt-0"

PACKAGECONFIG ??= "focus closurepolicy"

PACKAGECONFIG[focus]  = "-DPLUGIN_COBALT_ENABLE_FOCUS_IFACE=ON,-DPLUGIN_COBALT_ENABLE_FOCUS_IFACE=OFF,"
PACKAGECONFIG[closurepolicy]  = ",-DPLUGIN_COBALT_CLOSUREPOLICY=OFF,"
PACKAGECONFIG[evegreenlite]   = "-DPLUGIN_COBALT_EVEGREEN_LITE=ON,-DPLUGIN_COBALT_EVEGREEN_LITE=OFF,libloader-app,virtual/cobalt-evergreen"
PACKAGECONFIG[evegreencompatible]  = "-DPLUGIN_COBALT_EVEGREEN_COMPATIBLE=ON,-DPLUGIN_COBALT_EVEGREEN_COMPATIBLE=OFF,libloader-app,virtual/cobalt-evergreen"
PACKAGECONFIG[libcobalt]  = "-DPLUGIN_COBALT_EVEGREEN_LITE=OFF,,libcobalt"

def get_cobalt_config(d):
    if bb.utils.contains_any('DISTRO_FEATURES', ['libcobalt-rdm', 'libcobalt', 'morty'], True, False, d):
       return 'libcobalt'
    if bb.utils.contains('DISTRO_FEATURES', 'cobalt-evergreen-full', True, False, d):
       return 'evegreencompatible'
    return 'evegreencompatible evegreenlite'

PACKAGECONFIG_append = " ${@get_cobalt_config(d)}"

EXTRA_OECMAKE += " \
    -DCMAKE_BUILD_TYPE=Release \
    -DBUILD_SHARED_LIBS=ON \
    -DPLUGIN_COBALT_CLIENTIDENTIFIER="${COBALT_CLIENTIDENTIFIER}" \
    -DPLUGIN_COBALT_PERSISTENTPATHPOSTFIX="${COBALT_PERSISTENTPATHPOSTFIX}" \
"

def define_cobalt_properties(d, proplist):
    result = ''
    for prop_name in proplist.split():
        prop_name = 'PLUGIN_COBALT_PROP_' + prop_name
        prop_val = d.getVar(prop_name, True)
        if prop_val and prop_val != '':
           result += ''.join(['-D',prop_name,'=',prop_val]) + ' '
    return result

EXTRA_OECMAKE += " \
    ${@define_cobalt_properties(d,'MODELNAME MODELYEAR INTEGRATORNAME BRANDNAME FIRMWAREVERSION CHIPSETMODELNUMBER DEVICETYPE')} \
"

FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/wpeframework/plugins/*.so"
