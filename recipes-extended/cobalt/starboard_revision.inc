STARBOARD_SRC_URI = "${CMF_GIT_ROOT}/rdk/components/generic/cobalt"

# Nov 6, 2023
STARBOARD_SRCREV = "${@bb.utils.contains('DISTRO_FEATURES', 'cobalt-24', '293b1f15c324aa40d029f2f167660916be9037bd', '19e5a0df9979519ae6b75de5b400b1b48dc58ec8', d)}"
