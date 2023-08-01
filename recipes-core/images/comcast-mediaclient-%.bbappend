# Turn off autostart if launching from systemd
ROOTFS_POSTPROCESS_COMMAND += "${@bb.utils.contains('DISTRO_FEATURES', 'thunder_startup_services', 'thunder_services_autostart_patch; ','',d)}"

thunder_services_autostart_patch() {
   find  ${IMAGE_ROOTFS}/etc/WPEFramework/plugins/ -type f | xargs sed -i -r 's/"autostart"[[:space:]]*:[[:space:]]*true/"autostart":false/g'
}
