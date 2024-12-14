export type ProfileModel = {
    id?: number;
    patient?: number; // [Required(ErrorMessage = "Chọn bệnh nhân")]
    full_name?: string; // [Required(ErrorMessage = "Họ tên không được để trống")]
    date_of_birth?: string; // dateonly [Required(ErrorMessage = "Ngày sinh không được bỏ trống")]
    gender?: 'M' | 'F'
}