SUMMARY = "Host/Native tooling for the Web Platform for Embedded Framework"
LIC_FILES_CHKSUM = "file://LICENSE;md5=85bcfede74b96d9a58c6ea5d4b607e58"

require include/thunder.inc


SRC_URI = "git://github.com/rdkcentral/Thunder.git;protocol=git;branch=R4"
SRC_URI += "file://0001-startuporder-configure-config-generator.patch"

SRCREV = "R4.2"
#SRCREV = "${THUNDER_RELEASE_TAG_NAME}"

inherit cmake pkgconfig native python3native

RDEPENDS_${PN} = "python3"

DEPENDS = "\
    python3-native \
    python3-jsonref-native \
"

DEPENDS_append_daisy = " python3-enum34-native"

OECMAKE_SOURCEPATH = "${WORKDIR}/git/Tools"

FILES_${PN} += "${datadir}/*/Modules/*.cmake"

EXTRA_OECMAKE_append_daisy = " ${OECMAKE_SOURCEPATH} "
do_generate_toolchain_file_append_daisy() {
  sed -i 's/CMAKE_FIND_ROOT_PATH_MODE_PROGRAM ONLY/CMAKE_FIND_ROOT_PATH_MODE_PROGRAM BOTH/g' ${WORKDIR}/toolchain.cmake
}
