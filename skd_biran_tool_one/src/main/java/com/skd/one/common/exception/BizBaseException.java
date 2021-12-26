package com.skd.one.common.exception;



import java.text.MessageFormat;


/**
 * 基础异常类
 *
 * @author lixu
 */
public class BizBaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 异常code
     */
    private Integer code;

    /**
     * 异常消息
     */
    private String message;

    /**
     * 消息中配置的参数
     */
    private Object[] arguments;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }


    @Override
    public String getMessage() {
        if (null != this.message && null != this.arguments && this.arguments.length > 0) {
            // 替换占位符{0}
            message = MessageFormat.format(message, this.arguments);
        }
        return message;
    }


    public BizBaseException(String message) {
        this(1, message, null, new Object[0]);
    }

    public BizBaseException(Integer code, String message) {
        this(code, message, null, new Object[0]);
    }

    public BizBaseException(Integer code, String message, Object... arguments) {
        this(code, message, null, arguments);
    }


    public BizBaseException(Integer code, String message, Throwable throwable) {
        this(code, message, throwable, new Object[0]);
    }


    public BizBaseException(Integer code, String message, Throwable throwable, Object... arguments) {
        super(message, throwable);
        this.message = message;
        this.code = code;
        this.arguments = arguments;
    }

    /**
     * 无异常的Exception
     *
     * @param exceptionCode
     */
    public BizBaseException(ExceptionResolvable exceptionCode) {
        this(exceptionCode.getCode(), exceptionCode.getMessage(), null, new Object[0]);
    }

    /**
     * 无异常的Exception,但消息中配有参数
     *
     * @param exceptionCode
     */
    public BizBaseException(ExceptionResolvable exceptionCode, Object... arguments) {
        this(exceptionCode.getCode(), exceptionCode.getMessage(), null, arguments);
    }

    /**
     * 有异常的Exception
     *
     * @param exceptionCode
     * @param throwable
     */
    public BizBaseException(ExceptionResolvable exceptionCode, Throwable throwable) {
        this(exceptionCode.getCode(), exceptionCode.getMessage(), throwable, new Object[0]);
    }


    /**
     * 有异常的Exception并且消息中配有参数
     *
     * @param exceptionCode
     * @param throwable
     */
    public BizBaseException(ExceptionResolvable exceptionCode, Throwable throwable, Object... arguments) {
        this(exceptionCode.getCode(), exceptionCode.getMessage(), throwable, arguments);
    }


    public BizBaseException(Exception e) {
        super(e);

    }


    /**
     * 将JsonResult重新转回Exception的时候使用
     *
     * @param jsonResult
     */
   /* public BizBaseException(JsonResult<?> jsonResult) {
        super(jsonResult.getThrowable());
        this.message = jsonResult.getMessage();
        this.code = jsonResult.getCode();
    }*/

    /**
     * 重写该方法防止toString得到的消息为非国际化消息
     */
    @Override
    public String getLocalizedMessage() {
        return this.getMessage();
    }

}
