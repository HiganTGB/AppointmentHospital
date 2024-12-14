export type DiagnosticServiceModel = {
    id?: number;
    name?: string; //[Required(ErrorMessage = "Tên dịch vụ chuẩn đoán không được bỏ trống")]
    price?: number; //[Required(ErrorMessage = "Giá tiền không được bỏ trống")]
}