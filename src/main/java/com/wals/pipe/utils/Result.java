package com.wals.pipe.utils;

/**
 * @author zcj
 * @Description TODO
 * @createTime 2020-04-07 21:18:00
 */
public class Result {

    private String msg;
    private boolean success;
    private Object data;

    private int resultCode;

    @Override
    public String toString() {
        return "Result{" +
                "msg='" + msg + '\'' +
                ", success=" + success +
                ", data=" + data +
                '}';
    }

    public Result() {
        this.success = false;
    }

    public Result(String msg, boolean success, Object data) {
        this.msg = msg;
        this.success = success;
        this.data = data;
    }

    public Result(boolean success) {
        this.success = success;
    }

    public Result(String msg, boolean success) {
        this.msg = msg;
        this.success = success;
    }

    public Result(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }


    public static Result getInstance() {
        return new Result();
    }

    public static Result getInstance(String msg, boolean success, Object data) {
        return new Result(msg, success, data);
    }

    public static Result getInstance(String msg, boolean success) {
        return new Result(msg, success);
    }

    public static Result getInstance(boolean success, Object data) {
        return new Result(success, data);
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}