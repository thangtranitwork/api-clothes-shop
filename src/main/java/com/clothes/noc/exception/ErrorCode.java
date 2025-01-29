package com.clothes.noc.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // User-related errors (100-199)
    USER_ALREADY_EXISTS(100, "User already exists", HttpStatus.CONFLICT),
    USER_NOT_EXISTS(101, "User does not exist", HttpStatus.NOT_FOUND),
    LOGIN_FAILED(104, "Login failed, please check your login details", HttpStatus.UNAUTHORIZED),
    OAUTH2_LOGIN_HAS_NO_EMAIL(112, "OAuth2 login does not provide an email", HttpStatus.BAD_REQUEST),
    USER_HAS_NOT_VERIFIED_EMAIL(115, "This user has not verified their email", HttpStatus.FORBIDDEN),
    OAUTH2_USER_CAN_NOT_CHANGE_LOGIN_INFO(118, "OAuth2 users cannot change login information", HttpStatus.FORBIDDEN),
    VERIFY_CODE_DOES_NOT_EXIST(120, "Verification code does not exist", HttpStatus.NOT_FOUND),
    VERIFY_CODE_TIMEOUT(121, "Verification code has expired", HttpStatus.BAD_REQUEST),
    VERIFY_CODE_INVALID(122, "Invalid verification code", HttpStatus.BAD_REQUEST),
    THIS_USER_HAS_BEEN_LOCKED(124, "This user has been locked due to multiple incorrect password attempts", HttpStatus.LOCKED),
    PASSWORD_IS_INVALID(103, "Password is invalid", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(125, "Invalid email address", HttpStatus.BAD_REQUEST),
    // Token-related errors (300-399)
    TOKEN_IS_EXPIRED_OR_INVALID(300, "Token is expired or invalid", HttpStatus.UNAUTHORIZED),
    // Access-related errors (400-499)
    ACCESS_DENIED(403, "Access denied to view or modify this resource", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    NO_RESOURCE_FOUND(404, "Resource not found", HttpStatus.NOT_FOUND),

    //Product
    PRODUCT_NOT_EXIST(501, "Product does not exist", HttpStatus.NOT_FOUND),
    PRODUCT_VARIANT_NOT_EXIST(502, "Product variant does not exist", HttpStatus.NOT_FOUND),

    INVALID_QUANTITY(601, "Quantity must between 1 and 10", HttpStatus.BAD_REQUEST),
    QUANTITY_OF_PRODUCT_NOT_MEETING_REQUEST(602, "Quantity of product does not meet request", HttpStatus.BAD_REQUEST),
    CART_ITEM_NOT_EXIST(603, "Cart item does not exist", HttpStatus.NOT_FOUND),
    CART_IS_EMPTY(604, "Cart is empty", HttpStatus.BAD_REQUEST),
    SOME_ITEMS_HAVE_INVALID_QUANTITY(605, "Some items have invalid quantity", HttpStatus.BAD_REQUEST),
    CAN_NOT_CANCEL_THIS_ORDER(606, "Can not cancel this order", HttpStatus.BAD_REQUEST),
    ORDER_NOT_EXIST(607, "Order does not exist", HttpStatus.NOT_FOUND),
    CAN_NOT_PAY_THIS_ORDER(608, "Can not pay this order", HttpStatus.BAD_REQUEST),
    // Uncategorized errors
    UNCATEGORIZED_ERROR(1000, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),


    //Admin Product errors (2000 - 2099)

    PRODUCT_VARIANT_NOT_FOUND(2002, "Product variant not found", HttpStatus.NOT_FOUND ),
    DUPLICATE_PRODUCT_VARIANT(2003, "Duplicate Product variant", HttpStatus.BAD_REQUEST),
    PRODUCT_TYPE_NOT_FOUND(2004, "Product type not found", HttpStatus.NOT_FOUND),
    PRODUCT_SUBTYPE_TYPE_HAS_ALREADY_EXISTS(2005, "Product type has already exists", HttpStatus.BAD_REQUEST),
    SIZE_NOT_FOUND(2006, "Size not found", HttpStatus.NOT_FOUND),
    DUPLICATE_SIZE(2007, "Duplicate size", HttpStatus.BAD_REQUEST),
    COLOR_NOT_FOUND(2008, "Color not found", HttpStatus.NOT_FOUND),
    DUPLICATE_COLOR(2009, "Duplicate color", HttpStatus.BAD_REQUEST),
    PRODUCT_TYPE_DELETE_FAILED(2012, "Cannot delete ProductType because it is referenced by other entities.", HttpStatus.BAD_REQUEST),
    SIZE_DELETE_FAILED(2013,  "Cannot delete Size because it is referenced by other entities.", HttpStatus.BAD_REQUEST),
    COLOR_DELETE_FAILED(2014,  "Cannot delete Color because it is referenced by other entities.", HttpStatus.BAD_REQUEST),
    PRODUCT_DELETE_FAILED(2014,  "Cannot delete the product because it is referenced by 1 or many variants.", HttpStatus.BAD_REQUEST),
    INVALID_VARIANT_QUANTITY(2015, "Variant quantity must greater than or equal 0", HttpStatus.BAD_REQUEST),
    INVALID_PRICE(2016, "Price must greater than 0", HttpStatus.BAD_REQUEST),
    //Admin Product errors (2100 - 2199)
    ORDER_NOT_FOUND(2100, "Order not found", HttpStatus.NOT_FOUND),
    UPDATE_STATUS_FAILED(2101, "Update status failed", HttpStatus.BAD_REQUEST),
    ONLY_IMAGE_FILES_ARE_ALLOWED(2102, "Only image files are allowed", HttpStatus.BAD_REQUEST),
    UPLOAD_IMAGE_FAILED(2103, "Upload image failed", HttpStatus.BAD_REQUEST),
    ;

    private final int code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
