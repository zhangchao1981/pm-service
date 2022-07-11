package com.energy.common.databaseseparate.model;

import lombok.Data;

@Data
public class Event {

    private String id;
    private String type;
    private String source;
    private String userID;
    private String userName;
    private long timestamp;
    private String objectId;
    private String ObjectName;
    private String objectType;
    private String operateType;
}
