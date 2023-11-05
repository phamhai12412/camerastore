package ra.security.model.domain;

public enum OrderStatus {
    PENDING,        // Đang chờ xác nhận
    CONFIRMED,      // Đã xác nhận
    SHIPPED,        // Đã giao hàng
    DELIVERED,      // Đã nhận hàng
    CANCELED ,       // Đã hủy
    FAILURE ,     // Thất bại
    CART
}
