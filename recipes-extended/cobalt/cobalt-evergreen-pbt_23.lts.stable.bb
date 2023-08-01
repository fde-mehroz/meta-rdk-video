SUMMARY = "Evergreen Cobalt Core library."
HOMEPAGE = "https://cobalt.dev"

LICENSE = "BSD-3-Clause"
# See https://github.com/youtube/cobalt/blob/master/LICENSE for governing license.
# This license has been stored locally as COBALT_LICENSE
LIC_FILES_CHKSUM = "file://../COBALT_LICENSE;md5=0fca02217a5d49a14dfe2d11837bb34d"

FILESEXTRAPATHS_prepend := "${THISDIR}/evergreen:"
DEPENDS += "unzip-native breakpad-native"
OVERRIDES_append = ":${TARGET_FPU}"

CRX_FILE_arm_hard = "cobalt_evergreen_3.5.6_arm-hardfp_sbversion-14_release_20230620182739.crx"
DBG_FILE_arm_hard = "libcobalt_3.5.6_unstripped_arm-hardfp_sbversion-14_release_9a0d51d29db3c470.tar.gz"
CRX_FILE_SHA256SUM_arm_hard = "f1ddd415b397918e817d8376956df0fb4dceb4c4ae2281be0557e4a2ab771d63"
DBG_FILE_SHA256SUM_arm_hard = "76efefc7b1a01c6538c608d75ab5537689c1c91d5d28e4ff332afa08fc7c2e5d"

CRX_FILE_aarch64 = "cobalt_evergreen_3.5.6_arm64_sbversion-14_release_20230620182739.crx"
DBG_FILE_aarch64 = "libcobalt_3.5.6_unstripped_arm64_sbversion-14_release_1796dfabb05c50aa.tar.gz"
CRX_FILE_SHA256SUM_aarch64 = "b285d9c6ac5a1f9bea2b7ac62884b0bdbdb84d940630f3584c489f08aad98da8"
DBG_FILE_SHA256SUM_aarch64 = "7e54c42c64d023f38bb906773e194a5980b48f38a9aa6065855176713b992f21"

PV = "3.5.6"
YT_BASE_URI = "https://github.com/youtube/cobalt/releases/download/23.lts.5"
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
