export type PatientModel = {
    id: number;
    full_name?: string;//[Required(ErrorMessage = "Họ tên không được bỏ trống")]
    username?: string; //[Required(ErrorMessage = "Tên đăng nhập không được bỏ trống")]
    email?: string;
    password?: string; // PaswordValidation
    phone?: string; // [Required(ErrorMessage = "Số điện thoại không được bỏ trống")]
    role?: number;
}