package com.iscas.pm.common.web.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： 公共响应类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BaseResponse<T> {
    private int code;
    private T data;
    private String message;

    public static <T> BaseResponse success(T data){
        BaseResponse response = new BaseResponse();
        response.setCode(HttpStatus.OK.value());
        response.setMessage(HttpStatus.OK.getReasonPhrase());
        if (data!=null){
            response.setData(data);
        }
        return response;
    }


    public static BaseResponse fail(int code, String msg){
        BaseResponse response = new BaseResponse();
        response.setCode(code);
        response.setMessage(msg);
        return response;
    }

    @JsonIgnore
    public boolean isSuccess(){
        return code==HttpStatus.OK.value();
    }
}
