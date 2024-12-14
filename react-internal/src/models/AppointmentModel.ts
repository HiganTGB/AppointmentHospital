export type AppointmentModel = {
    id: number;
    profile?: number; //[Required(ErrorMessage = "Chọn hồ sơ")]
    doctor?: number;
    state?: number; //[Required(ErrorMessage = "Chọn trạng thái")]
    date?: string; // dateonly [Required(ErrorMessage = "Chọn ngày đặt")]
    begin_time?: string; // time
    end_time?: string; // time
    schedule_id?: number; // [Required(ErrorMessage = "Chọn thời gian đặt lịch")]
}