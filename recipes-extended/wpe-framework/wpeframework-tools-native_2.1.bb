SUMMARY = "Host/Native tooling for the Web Platform for Embedded Framework"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f1dffbfd5c2eb52e0302eb6296cc3711"

require include/thunder.inc

SRC_URI = "git://github.com/rdkcentral/Thunder.git;protocol=git;branch=R2;name=thunder"

# Sep 29, 2022, 82d4791478934292f7260fef233b9743fa34d88e
SRCREV_thunder = "${THUNDER_RELEASE_TAG_NAME}"

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
