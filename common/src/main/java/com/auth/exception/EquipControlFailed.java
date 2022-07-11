package com.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Equip
 *
 * @author Junsoo
 * 2020/10/20 16:41
 **/
@ResponseStatus(value= HttpStatus.FAILED_DEPENDENCY)
public class EquipControlFailed extends BaseException {
    private static final int CODE = 200;

    public EquipControlFailed(){
        this("设备控制失败");
    }
    public EquipControlFailed(String message) {
        super(message);
    }

    public int getCode() {
        return CODE;
    }
}
