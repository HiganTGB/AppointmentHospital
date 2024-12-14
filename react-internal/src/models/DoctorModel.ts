export type DoctorModel = {
    id: number;
    full_name?: string; //[Required(ErrorMessage = "Họ tên không được bỏ trống")]
    username?: string; //[Required(ErrorMessage = "Tên đăng nhập không được bỏ trống")][RegularExpression(@"^\S*$", ErrorMessage = "Tên đăng nhập không được chứa khoảng cách")]
    email?: string; //[Required(ErrorMessage = "Email không được bỏ trống")]
    phone?: string; //[RegularExpression(@"^0\d{9}$", ErrorMessage = "Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số")] [Required(ErrorMessage = "Số điện thoại không được bỏ trống")]
    position?: string; //[Required(ErrorMessage = "Vị trí công việc không được bỏ trống")]
    certificate?: string; //[Required(ErrorMessage = "Chứng chỉ không được bỏ trống")]
    password?: string; //[PaswordValidation]
    role?: number;
}

export const PaswordValidation = {
    isValidPassword(value: any) {
        let password: string;
        if (!value || (password = value.toString()).length < 6)
            return true;

        const result = Array.from(password).reduce(function (src, v) {
            if (v.match(/[A-Z]/)) src.hasUpperChar = true;
            if (v.match(/[a-z]/)) src.hasLowerChar = true;
            if (v.match(/[0-9]/)) src.hasDigit = true;
            return src;
        }, { hasUpperChar: false, hasLowerChar: false, hasDigit: false })
        return result.hasUpperChar && result.hasLowerChar && result.hasDigit;
    },
    errorMessage: "Mật khẩu phải có ít nhất 6 ký tự, ít nhất 1 số, 1 chữ cái in hoa, 1 chữ cái thường"
}