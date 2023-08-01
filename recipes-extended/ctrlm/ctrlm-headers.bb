DESCRIPTION = "Control Manager Headers"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"


SECTION = "base"
DEPENDS = ""

include ctrlm.inc

SRCREV_ctrlm-headers = "${AUTOREV}"
SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/control;protocol=${CMF_GIT_PROTOCOL};branch=${CMF_GIT_BRANCH};name=ctrlm-headers"

S = "${WORKDIR}/git"

FILES_${PN} += "${includedir}/ctrlm_ipc.h \
                ${includedir}/ctrlm_ipc_rcu.h \
                ${includedir}/ctrlm_ipc_voice.h \
                ${includedir}/ctrlm_ipc_key_codes.h \
                ${includedir}/ctrlm_ipc_device_update.h \
                ${includedir}/ctrlm_ipc_ble.h \
                ${includedir}/ctrlm_hal.h \
                ${includedir}/ctrlm_hal_ip.h \
                ${includedir}/ctrlm_hal_ble.h \
                ${includedir}/ctrlm_hal_rf4ce.h \
               "

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${includedir}
    install -m 644 ${S}/include/ctrlm_ipc.h ${D}${includedir}
    install -m 644 ${S}/include/ctrlm_ipc_rcu.h ${D}${includedir}
    install -m 644 ${S}/include/ctrlm_ipc_voice.h ${D}${includedir}
    install -m 644 ${S}/include/ctrlm_ipc_key_codes.h ${D}${includedir}
    install -m 644 ${S}/include/ctrlm_ipc_device_update.h ${D}${includedir}
    install -m 644 ${S}/include/ctrlm_ipc_ble.h ${D}${includedir}
    install -m 644 ${S}/include/ctrlm_hal.h ${D}${includedir}
    install -m 644 ${S}/include/ctrlm_hal_ip.h ${D}${includedir}
    install -m 644 ${S}/include/ctrlm_hal_ble.h ${D}${includedir}
    install -m 644 ${S}/include/ctrlm_hal_rf4ce.h ${D}${includedir}

    install -d ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/ctrlm_vendor_network_factory.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/ctrlm_voice_packet_analysis.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/ctrlm_log.h ${D}${includedir}/ctrlm_private

    # Authorization Support
    install -m 644 ${S}/src/auth/ctrlm_auth.h ${D}${includedir}/ctrlm_private
    # Advanced Secure Binding
    install -m 644 ${S}/src/asb/ctrlm_asb.h ${D}${includedir}/ctrlm_private
    # IRDB Support
    install -m 644 ${S}/src/irdb/ctrlm_irdb_factory.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/irdb/ctrlm_irdb.h ${D}${includedir}
    install -m 644 ${S}/src/irdb/ctrlm_irdb_stub.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/ipc/ctrlm_ipc_iarm.h ${D}${includedir}
    install -m 644 ${S}/src/irdb/ipc/ctrlm_irdb_ipc_iarm_thunder.h ${D}${includedir}/ctrlm_private

    # Network Support
    install -m 644 ${S}/src/ctrlm.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/ctrlm_rcu.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/ctrlm_recovery.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/ctrlm_utils.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/ctrlm_validation.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/ctrlm_network.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/ctrlm_controller.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/attributes/ctrlm_attr.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/attributes/ctrlm_attr_general.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/attributes/ctrlm_attr_voice.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/database/ctrlm_database.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/database/ctrlm_db_attr.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/rfc/ctrlm_rfc.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/rfc/ctrlm_rfc_attr.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/config/ctrlm_config_attr.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/voice/ctrlm_voice_obj.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/voice/ctrlm_voice_types.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/voice/ipc/ctrlm_voice_ipc.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/voice/telemetry/ctrlm_voice_telemetry_events.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/json_config.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/ctrlm_tr181.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/telemetry/ctrlm_telemetry.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/telemetry/ctrlm_telemetry_event.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/telemetry/ctrlm_telemetry_markers.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/thunder/ctrlm_thunder_plugin.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/thunder/ctrlm_thunder_controller.h ${D}${includedir}/ctrlm_private
    install -m 644 ${S}/src/ipc/ctrlm_rcp_ipc_iarm_thunder.h ${D}${includedir}/ctrlm_private
}

ALLOW_EMPTY_${PN} = "1"
