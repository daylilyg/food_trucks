package org.daniel.error;

import lombok.Data;

@Data
public class ErrorResult {
    public static ErrorResult of(ErrorEnum err){
        return new ErrorResult(err.getMsg(), err.getCode());
    }

    public ErrorResult(String msg, Integer code) {
        this.msg = msg;
        this.code = code;
    }

    private String msg;

    private Integer code;
}
