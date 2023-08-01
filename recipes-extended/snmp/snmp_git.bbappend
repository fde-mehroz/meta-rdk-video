inherit syslog-ng-config-gen

SYSLOG-NG_FILTER += "snmpd"
SYSLOG-NG_SERVICE_snmpd = "snmpd.service"
SYSLOG-NG_DESTINATION_snmpd = "snmpd.log"
SYSLOG-NG_LOGRATE_snmpd = "medium"
