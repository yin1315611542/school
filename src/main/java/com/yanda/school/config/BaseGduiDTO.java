package com.yanda.school.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
//封装数据给前端
public class BaseGduiDTO<T> {
    public static final int RESULT_CODE_SUCCESS = 0;
    public static final int RESULT_CODE_FAIL = 1;
    private int resultCode = 0;
    private String resultMsg;
    private String resultKey;
    private T data;

    public static <T> BaseGduiDTO<T> ok() {
        return new BaseGduiDTO();
    }

    public static <T> BaseGduiDTO<T> ok(T data) {
        return (new BaseGduiDTO()).setData(data);
    }

    public static <T> BaseGduiDTO<T> error() {
        return (new BaseGduiDTO()).setResultCode(1);
    }

    public static <T> BaseGduiDTO<T> error(String resultMsg) {
        return (new BaseGduiDTO()).setResultCode(1).setResultMsg(resultMsg);
    }

    @JsonIgnore
    public boolean isOk() {
        return 0 == this.resultCode;
    }

    public BaseGduiDTO() {
    }

    public int getResultCode() {
        return this.resultCode;
    }

    public String getResultMsg() {
        return this.resultMsg;
    }

    public String getResultKey() {
        return this.resultKey;
    }

    public T getData() {
        return this.data;
    }

    public BaseGduiDTO<T> setResultCode(int resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public BaseGduiDTO<T> setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
        return this;
    }

    public BaseGduiDTO<T> setResultKey(String resultKey) {
        this.resultKey = resultKey;
        return this;
    }

    public BaseGduiDTO<T> setData(T data) {
        this.data = data;
        return this;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof BaseGduiDTO)) {
            return false;
        } else {
            BaseGduiDTO<?> other = (BaseGduiDTO)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getResultCode() != other.getResultCode()) {
                return false;
            } else {
                label49: {
                    Object this$resultMsg = this.getResultMsg();
                    Object other$resultMsg = other.getResultMsg();
                    if (this$resultMsg == null) {
                        if (other$resultMsg == null) {
                            break label49;
                        }
                    } else if (this$resultMsg.equals(other$resultMsg)) {
                        break label49;
                    }

                    return false;
                }

                Object this$resultKey = this.getResultKey();
                Object other$resultKey = other.getResultKey();
                if (this$resultKey == null) {
                    if (other$resultKey != null) {
                        return false;
                    }
                } else if (!this$resultKey.equals(other$resultKey)) {
                    return false;
                }

                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data != null) {
                        return false;
                    }
                } else if (!this$data.equals(other$data)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof BaseGduiDTO;
    }


    public String toString() {
        return "BaseGduiDTO(resultCode=" + this.getResultCode() + ", resultMsg=" + this.getResultMsg() + ", resultKey=" + this.getResultKey() + ", data=" + this.getData() + ")";
    }
}
