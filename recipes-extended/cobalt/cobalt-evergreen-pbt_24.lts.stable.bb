SUMMARY = "Evergreen Cobalt Core library."
HOMEPAGE = "https://cobalt.dev"

LICENSE = "BSD-3-Clause"
# See https://github.com/youtube/cobalt/blob/master/LICENSE for governing license.
# This license has been stored locally as COBALT_LICENSE
LIC_FILES_CHKSUM = "file://../COBALT_LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

inherit features_check
REQUIRED_DISTRO_FEATURES = "cobalt-24"

FILESEXTRAPATHS_prepend := "${THISDIR}/evergreen:"
DEPENDS += "unzip-native breakpad-native"
OVERRIDES_append = ":${TARGET_FPU}"

CRX_FILE_arm_hard = "cobalt_evergreen_4.10.2_arm-hardfp_sbversion-15_release_20230907212907.crx"
DBG_FILE_arm_hard = "libcobalt_4.10.2_unstripped_arm-hardfp_sbversion-15_release_9f7f94d3914cade7.tar.gz"
CRX_FILE_SHA256SUM_arm_hard = "8e937eba7f46b7454a4434903ed2a3e23a4d5fa171628039dfa9f73b167afdf9"
DBG_FILE_SHA256SUM_arm_hard = "69d4bcde4b2112556719e590e90de6a05e997017ddf9eb02950d82ef126f0824"

CRX_FILE_aarch64 = "cobalt_evergreen_4.10.2_arm64_sbversion-15_release_20230907212907.crx"
DBG_FILE_aarch64 = "libcobalt_4.10.2_unstripped_arm64_sbversion-15_release_7f479a97e78fe19c.tar.gz"
CRX_FILE_SHA256SUM_aarch64 = "4a6f7651eb298bdf3eba3457a5409c7de218318015cb13edca48e54dc14fca14"
DBG_FILE_SHA256SUM_aarch64 = "1ed748b181623218a7e3da4dde14972a9cdc69451df6e029fde32f1c8d44f062"

PV = "4.10.2"
YT_BASE_URI = "https://github.com/youtube/cobalt/releases/download/24.lts.10"
SRC_URI  = "${YT_BASE_URI}/${CRX_FILE};name=cobalt"
SRC_URI += "${YT_BASE_URI}/${DBG_FILE};name=cobalt_debug;subdir=debug_syms"
SRC_URI += "file://COBALT_LICENSE"
SRC_URI[cobalt.sha256sum] = "${CRX_FILE_SHA256SUM}"
SRC_URI[cobalt_debug.sha256sum] = "${DBG_FILE_SHA256SUM}"

PACKAGECONFIG ?= "use_system_certs"
PACKAGECONFIG[use_system_certs] = ""

COBALT_APP_DIR = "/content/data/app/cobalt"

do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_preunpack_cleanup() {
    bbnote "cleanup debug syms"
    rm -rf ${WORKDIR}/debug_syms
}
addtask preunpack_cleanup after do_fetch before do_unpack

do_install() {
    install -d "${D}${datadir}${COBALT_APP_DIR}"

    err_code=0

    set +e
    unzip -q -o -d "${D}${datadir}${COBALT_APP_DIR}" "${WORKDIR}/${CRX_FILE}" || err_code=$?
    set -e

    case $err_code in
     0) bbnote "All good";;
     1) bbwarn "Ignore unzip warnings";;
     *) bbfatal "Unzip failed, exit code: $err_code"
    esac

    if ${@bb.utils.contains('PACKAGECONFIG', 'use_system_certs', 'true', 'false', d)}; then
      # use system provided certs
      rm -rf ${D}${datadir}${COBALT_APP_DIR}/content/ssl/certs
      mkdir -p ${D}${datadir}${COBALT_APP_DIR}/content/ssl/
      ln -s /etc/ssl/certs ${D}${datadir}${COBALT_APP_DIR}/content/ssl/certs
    fi

    # intentionally install unstripped libcobalt.so, so symbol generation step will make use of debug libcobalt.so
    install -d "${D}${datadir}${COBALT_APP_DIR}/lib/"
    install -m 0755 ${WORKDIR}/debug_syms/libcobalt.so ${D}${datadir}${COBALT_APP_DIR}/lib/
}


FILES_${PN}  = "${datadir}${COBALT_APP_DIR}/content/*"
FILES_${PN} += "${datadir}${COBALT_APP_DIR}/manifest.json"
FILES_${PN} += "${datadir}${COBALT_APP_DIR}/lib/libcobalt.so"
FILES_SOLIBSDEV = ""

INSANE_SKIP_${PN} += "dev-so "
INSANE_SKIP_${PN}-dbg += "dev-so "

PROVIDES = "virtual/cobalt-evergreen"
RPROVIDES_${PN} = "virtual/cobalt-evergreen"
