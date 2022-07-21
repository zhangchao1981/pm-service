package com.iscas.pm.common.core.model;

/**
 * redis key  的前缀
 * @author zzc on 2020.08.20
 */
public interface RedisKeyConstants {

    String CONNECTOR_CACHE = "connector_cache";
    String LEFT_TREE_CACHE = "leftTreeCache";
    String PHYSICAL_POINT_DATA = "physicalPointData";
    String APP_DEVICE_STATUS = "appDeviceStatus";
    String notifyLock = "notifyLock";
    String daysSentLimit = "daysSentLimit";
    String daysReceiveLimit = "daysReceiveLimit";
    String loginCountValid = "login_count_valid";
}
