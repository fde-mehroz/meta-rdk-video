DESCRIPTION = "Control Manager Factory Headers"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"


SECTION = "base"
DEPENDS = ""

SRCREV_ctrlm-factory-headers = "${AUTOREV}"
SRC_URI = "${RDK_GENERIC_ROOT_GIT}/control-factory/generic;protocol=${RDK_GIT_PROTOCOL};branch=${RDK_GIT_BRANCH};name=ctrlm-factory-headers"

S = "${WORKDIR}/git"

FILES_${PN} += "${includedir}/ctrlm_fta_lib.h \
                ${includedir}/ctrlm_fta_caa.h \
               "

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${includedir}
    install -m 644 ${S}/src/ctrlm_fta_lib.h ${D}${includedir}
    install -m 644 ${S}/src/ctrlm_fta_caa.h ${D}${includedir}
}

ALLOW_EMPTY_${PN} = "1"
