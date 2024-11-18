package appointmenthospital.appointmentservice.model.entity;

import lombok.Getter;

@Getter
public enum AppointmentStatus {

    PENDING("Chờ thanh toán"),
    PAID("Đã thanh toán"),
    CANCELED("Đã hủy"),
    COMPLETED("Đã khám"),
    NO_SHOW("Không đến khám");
    private final String label;

    AppointmentStatus(String label) {
        this.label = label;
    }

}
