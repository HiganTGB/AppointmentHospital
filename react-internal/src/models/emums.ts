export const EAppointmentState = {
    DISABLE: {
        value: 0,
        displayName: "Không hoạt động",
        badge: "danger"
    },
    EXPIRED: {
        value: 1,
        displayName: "Hết hạn",
        badge: "dark"
    },
    ENABLE: {
        value: 2,
        displayName: "Đang hoạt động",
        badge: "secondary"
    },
    COMPLETED: {
        value: 3,
        displayName: "Hoàn thành",
        badge: "success"
    },
    0: "DISABLE",
    1: "EXPIRED",
    2: "ENABLE",
    3: "COMPLETED"
};

export const EExaminationState = {
    DISABLE: {
        value: 0,
        displayName: "Không hoạt động",
        badge: "danger"
    },
    ENABLE: {
        value: 1,
        displayName: "Đang hoạt động",
        badge: "secondary"
    },
    COMPLETED: {
        value: 2,
        displayName: "Hoàn thành",
        badge: "success"
    },
    0: "DISABLE",
    1: "ENABLE",
    2: "COMPLETED"
};

export const Permission = {
    SystemPrivilege: {
        value: 0,
        displayName: "Quản trị hệ thống"
    },
    ReadRole: {
        value: 1,
        displayName: "Đọc vai trò"
    },
    CreateRole: {
        value: 2,
        displayName: "Tạo vai trò"
    },
    UpdateRole: {
        value: 3,
        displayName: "Sửa vai trò"
    },
    DeleteRole: {
        value: 4,
        displayName: "Xoá vai trò"
    },
    ReadUser: {
        value: 5,
        displayName: "Đọc người dùng"
    },
    CreateUser: {
        value: 6,
        displayName: "Tạo người dùng"
    },
    UpdateUser: {
        value: 7,
        displayName: "Sửa người dùng"
    },
    DeleteUser: {
        value: 8,
        displayName: "Xoá người dùng"
    },
    ReadProfile: {
        value: 9,
        displayName: "Đọc hồ sơ"
    },
    CreateProfile: {
        value: 10,
        displayName: "Tạo hồ sơ"
    },
    UpdateProfile: {
        value: 11,
        displayName: "Sửa hồ sơ"
    },
    DeleteProfile: {
        value: 12,
        displayName: "Xoá hồ sơ"
    },
    ReadDiagnosticService: {
        value: 13,
        displayName: "Đọc dịch vụ chuẩn đoán"
    },
    CreateDiagnosticService: {
        value: 14,
        displayName: "Tạo dịch vụ chuẩn đoán"
    },
    UpdateDiagnosticService: {
        value: 15,
        displayName: "Sửa dịch vụ chuẩn đoán"
    },
    DeleteDiagnosticService: {
        value: 16,
        displayName: "Xoá dịch vụ chuẩn đoán"
    },
    CreateExaminationDiagnostic: {
        value: 17,
        displayName: "Tạo chuẩn đoán"
    },
    SealExaminationDiagnostic: {
        value: 18,
        displayName: "Khoá chuẩn đoán"
    },
    DeleteExaminationDiagnostic: {
        value: 19,
        displayName: "Xoá chuẩn đoán"
    },
    ReadAppointment: {
        value: 20,
        displayName: "Đọc lịch hẹn"
    },
    CreateAppointment: {
        value: 21,
        displayName: "Tạo lịch hẹn"
    },
    DeleteAppointment: {
        value: 22,
        displayName: "Xoá lịch hẹn"
    },
    ReadExamination: {
        value: 23,
        displayName: "Đọc phiếu khám"
    },
    CreateExamination: {
        value: 24,
        displayName: "Tạo phiếu khám"
    },
    UpdateExamination: {
        value: 25,
        displayName: "Sửa phiếu khám"
    },
    DeleteExamination: {
        value: 26,
        displayName: "Xoá phiếu khám"
    },
    ReadPrescription: {
        value: 27,
        displayName: "Đọc đơn thuốc"
    },
    CreatePrescription: {
        value: 28,
        displayName: "Tạo đơn thuốc"
    },
    SealPrescription: {
        value: 29,
        displayName: "Khoá đơn thuốc"
    },
    DeletePrescription: {
        value: 30,
        displayName: "Xoá đơn thuốc"
    },
    0: "SystemPrivilege",
    1: "ReadRole",
    2: "CreateRole",
    3: "UpdateRole",
    4: "DeleteRole",
    5: "ReadUser",
    6: "CreateUser",
    7: "UpdateUser",
    8: "DeleteUser",
    9: "ReadProfile",
    10: "CreateProfile",
    11: "UpdateProfile",
    12: "DeleteProfile",
    13: "ReadDiagnosticService",
    14: "CreateDiagnosticService",
    15: "UpdateDiagnosticService",
    16: "DeleteDiagnosticService",
    17: "CreateExaminationDiagnostic",
    18: "SealExaminationDiagnostic",
    19: "DeleteExaminationDiagnostic",
    20: "ReadAppointment",
    21: "CreateAppointment",
    22: "DeleteAppointment",
    23: "ReadExamination",
    24: "CreateExamination",
    25: "UpdateExamination",
    26: "DeleteExamination",
    27: "ReadPrescription",
    28: "CreatePrescription",
    29: "SealPrescription",
    30: "DeletePrescription",
};