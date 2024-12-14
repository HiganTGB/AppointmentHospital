export type ExaminationModel = {
    id: number;
    appointment?: number; // [Required(ErrorMessage = "Chuẩn đoán không được bỏ trống")]
    diagnostic?: string;
    description?: string;
    state?: number; // [Required(ErrorMessage = "Chọn trạng thái")]
    selectedDiagnostics: number[];
    selectedDoctors: { [diagnostics: number]: number; }
    prescription: string;
}