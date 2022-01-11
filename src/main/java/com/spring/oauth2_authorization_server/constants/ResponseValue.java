package com.spring.oauth2_authorization_server.constants;

import org.springframework.http.HttpStatus;

public enum ResponseValue {
    // 200x
    SUCCESS(HttpStatus.OK, "thành công"),

    // 400x
    INVALID_FIELDS(HttpStatus.BAD_REQUEST, 4001, "trường không hợp lệ"),
    INVALID_OR_MISSING_REQUEST_BODY(HttpStatus.BAD_REQUEST, 4002, "request body không hợp lệ"),
    INVALID_OR_MISSING_REQUEST_PARAMETERS(HttpStatus.BAD_REQUEST, 4003, "request param thiếu hoặc không hợp lệ"),

    // 401x
    AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, 4011, "truy cập yêu cầu xác thực người dùng"),
    WRONG_CLIENT_ID_OR_SECRET(HttpStatus.UNAUTHORIZED, 4012, "client id hoặc secret không đúng"),
    WRONG_USERNAME_OR_PASSWORD(HttpStatus.UNAUTHORIZED, 4013, "sai tên đăng nhập hoặc mật khẩu"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, 4014, "token không hợp lệ"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, 4015, "token đã hết hạn"),
    USER_BANNED(HttpStatus.UNAUTHORIZED, 4016, "tài khoản đã bị vô hiệu hóa"),

    // 404x
    NO_RESOURCE_ID_FOUND(HttpStatus.NOT_FOUND, 4041, "không tìm thấy resource serevr"),
    CLIENT_ID_NOT_FOUND(HttpStatus.CONFLICT, 4042, "không tìm thấy client id"),

    // 409x
    CLIENT_ID_EXISTS(HttpStatus.CONFLICT, 4091, "client id đã tồn tại"),

    // 500x
    UNEXPECTED_ERROR_OCCURRED(HttpStatus.INTERNAL_SERVER_ERROR, "lỗi hệ thống");

    private HttpStatus httpStatus;
    private int specialCode;
    private String message;

    ResponseValue(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.specialCode = httpStatus.value();
        this.message = message;
    }

    ResponseValue(HttpStatus httpStatus, int specialCode, String message) {
        this.httpStatus = httpStatus;
        this.specialCode = specialCode;
        this.message = message;
    }

    public HttpStatus httpStatus() {
        return httpStatus;
    }

    public int specialCode() {
        return specialCode;
    }

    public String message() {
        return message;
    }
}
