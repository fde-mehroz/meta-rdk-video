SUMMARY = "Evergreen Cobalt Core library."
HOMEPAGE = "https://cobalt.googlesource.com/"

LICENSE = "BSD-3-Clause"
# See https://github.com/youtube/cobalt/blob/master/LICENSE for governing license.
# This license has been stored locally as COBALT_LICENSE
LIC_FILES_CHKSUM = "file://../COBALT_LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := "${THISDIR}/evergreen:"
DEPENDS += "unzip-native breakpad-native"
OVERRIDES_append = ":${TARGET_FPU}"

CRX_FILE_arm_hard = "cobalt_evergreen_3.4.6_arm-hardfp_sbversion-14_release_20230331190226.crx"
DBG_FILE_arm_hard = "libcobalt_3.4.6_unstripped_arm-hardfp_sbversion-14_release_781362e856d72930.tar.gz"
CRX_FILE_SHA256SUM_arm_hard = "9269ef359333ed5151749fd8d3949697f40c1c06b91cf1c7d881701966a89a1f"
DBG_FILE_SHA256SUM_arm_hard = "64f43b94cdc109824c2114d12a83e45b3e6f5228d5fb6a00f9f62ac3fa28c3f7"

CRX_FILE_aarch64 = "cobalt_evergreen_3.4.6_arm64_sbversion-14_release_20230331190226.crx"
DBG_FILE_aarch64 = "libcobalt_3.4.6_unstripped_arm64_sbversion-14_release_c2d5647959769efb.tar.gz"
CRX_FILE_SHA256SUM_aarch64 = "37afea57fd1e727c877aa3448f83a21a93c5e079cd5b70d684ed7c8296b8039f"
DBG_FILE_SHA256SUM_aarch64 = "f1cdf74926def995a823d7d17560a3a8ea09488d790eced812b0669da1d66add"

PV = "3.4.6"
YT_BASE_URI = "https://github.com/youtube/cobalt/releases/download/23.lts.4"
SRC_URI  = "${YT_BASE_URI}/${CRX_FILE};name=cobalt"
SRC_URI += "${YT_BASE_URI}/${DBG_FILE};name=cobalt_debug;subdir=debug_syms"
SRC_URI += "file://COBALT_LICENSE"
SRC_URI[cobalt.sha256sum] = "${CRX_FILE_SHA256SUM}"
SRC_URI[cobalt_debug.sha256sum] = "${DBG_FILE_SHA256SUM}"

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

    # use system provided certs
    rm -rf ${D}${datadir}${COBALT_APP_DIR}/content/ssl/certs
    mkdir -p ${D}${datadir}${COBALT_APP_DIR}/content/ssl/
    ln -s /etc/ssl/certs ${D}${datadir}${COBALT_APP_DIR}/content/ssl/certs

    #intensionally installing unstripped libcobalt.so, So symbol generation step will make use of debug libcobalt.so
    install -d "${D}${datadir}${COBALT_APP_DIR}/lib/"
    install -m 0755 ${WORKDIR}/debug_syms/tmp/cobalt-evergreen-snapshot/*/symbols/libcobalt.so ${D}${datadir}${COBALT_APP_DIR}/lib/
}


FILES_${PN}  = "${datadir}${COBALT_APP_DIR}/content/*"
FILES_${PN} += "${datadir}${COBALT_APP_DIR}/manifest.json"
FILES_${PN} += "${datadir}${COBALT_APP_DIR}/lib/libcobalt.so"
FILES_SOLIBSDEV = ""

INSANE_SKIP_${PN} += "dev-so "
INSANE_SKIP_${PN}-dbg += "dev-so "

PROVIDES = "virtual/cobalt-evergreen"
RPROVIDES_${PN} = "virtual/cobalt-evergreen"
