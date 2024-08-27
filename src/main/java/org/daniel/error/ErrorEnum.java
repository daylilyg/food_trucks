package org.daniel.error;

public enum ErrorEnum {
    UNKNOWN_ERROR(-1, "Unknown error"),

    ES_ERROR(-1000, "Search service is unavailable now");

    private final Integer code;

    private final String msg;

    ErrorEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
