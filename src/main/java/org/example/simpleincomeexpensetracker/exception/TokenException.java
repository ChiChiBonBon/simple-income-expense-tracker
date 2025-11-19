package org.example.simpleincomeexpensetracker.exception;

public class TokenException extends RuntimeException {

    private static final long serialVersionUID = 1L;  // Old School 标配

    private String code;      // 错误码
    private int status;       // HTTP 状态码
    private long timestamp;   // 时间戳
    private String details;   // 详情信息

    // 5 个构造函数
    public TokenException(String message) { }
    public TokenException(String message, int status) { }
    public TokenException(String message, String code, int status) { }
    public TokenException(String message, String code, int status, String details) { }
    public TokenException(String message, String code, int status, Throwable cause) { }
}
