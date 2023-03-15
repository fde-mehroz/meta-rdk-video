SRC_URI += "file://cobalt-test-plugin_package.json"

# Generate cobalt plugin as a rdm package for test purpose when rdm-test-plugin distro is enabled.
inherit ${@bb.utils.contains('DISTRO_FEATURES', 'rdm-test-plugin', 'comcast-package-deploy', '', d)}
DOWNLOAD_APPS="${@bb.utils.contains('DISTRO_FEATURES','rdm-test-plugin','cobalt-test-plugin', '', d)}"
RDM_PACKAGE_TYPE="${@bb.utils.contains('DISTRO_FEATURES','rdm-test-plugin','plugin', '', d)}"

do_install_append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'rdm-test-plugin', 'true', 'false', d)}; then
         install -d ${D}${sysconfdir}/apps/
         install -m 0644  ${WORKDIR}/cobalt-test-plugin_package.json ${D}${sysconfdir}/apps/
    fi
}

FILES_${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'rdm-test-plugin', '${sysconfdir}/apps/cobalt-test-plugin_package.json', '', d)}"
