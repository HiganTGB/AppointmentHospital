package appointmenthospital.authservice.model.entity;

import lombok.Getter;

@Getter
public enum Permission{
    ReadRole(1,"READ ROLE"),
    CreateRole(2,"READ ROLE"),
    UpdateRole(3,"READ ROLE"),
    DeleteRole(4,"READ ROLE"),
    ReadUser(5,"READ ROLE"),
    CreateUser(6,"READ ROLE"),
    UpdateUser(7,"READ ROLE"),
    DeleteUser(8,"READ ROLE"),
    ReadProfile(9,"READ ROLE"),
    CreateProfile(10,"READ ROLE"),
    UpdateProfile(11,"READ ROLE"),
    DeleteProfile(12,"READ ROLE"),
    ReadDiagnosticService(13,"READ ROLE"),
    CreateDiagnosticService(14,"READ ROLE"),
    UpdateDiagnosticService(15,"READ ROLE"),
    DeleteDiagnosticService(16,"READ ROLE"),
    CreateExaminationDiagnostic(17,"READ ROLE"),
    SealExaminationDiagnostic(18,"READ ROLE"),
    DeleteExaminationDiagnostic(19,"READ ROLE"),
    ReadAppointment(20,"READ ROLE"),
    CreateAppointment(21,"READ ROLE"),
    DeleteAppointment(22,"READ ROLE"),
    ReadExamination(23,"READ ROLE"),
    CreateExamination(24,"READ ROLE"),
    UpdateExamination(25,"READ ROLE"),
    DeleteExamination(26,"READ ROLE"),
    ReadPrescription(27,"READ ROLE"),
    CreatePrescription(28,"READ ROLE"),
    SealPrescription(29,"READ ROLE"),
    DeletePrescription(30,"READ ROLE");
    private final int code;

    Permission(int code, String description) {
        this.code = code;
    }

    public static String getByIdentity(int code) {
        for (Permission permission : Permission.values()) {
            if (permission.code == code) {
                return permission.name();
            }
        }
        return null;
    }

    public static int getByCode (String identity) {
        for (Permission permission : Permission.values()) {
            if (permission.name().equals(identity)) {
                return permission.code;
            }
        }
        return 0;
    }

}