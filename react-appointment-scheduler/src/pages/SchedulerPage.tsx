import React from "react";

import "./Page.css";
import "./SchedulerPage.css";
import { ComboBox } from "../components/ComboBoxField";
import { SubmitButton } from "../components/Button";
import { Calendar } from "../components/CalendarField";

export function Scheduler() {
    return (
        <form className="Scheduler Page">
            <h2 className="title">Đặt lịch</h2>
            <ComboBox label="Chuyên khoa">
                <option key="none">(Chọn chuyên khoa)</option>
                <option key="rhm">Răng hàm mặt</option>
                <option key="tmh">Tai mũi họng</option>
            </ComboBox>
            <Calendar label="Ngày khám" formName="date" children={
                (function () {
                    const dates = [];
                    const today = new Date();
                    const dayOfWeek = today.getDay(); // 0 (Chủ nhật) đến 6 (Thứ bảy) // Tính số ngày để lùi về thứ Hai của tuần hiện tại
                    const firstMonday = new Date();
                    firstMonday.setDate(today.getDate() - ((dayOfWeek === 0) ? 6 : dayOfWeek - 1));
                    for (let i = 0; i < 28; i++) {
                        const newDate = new Date(firstMonday);
                        newDate.setDate(firstMonday.getDate() + i); // Kiểm tra nếu ngày mới lớn hơn ngày hiện tại
                        dates.push({ date: newDate, isPast: newDate <= today });
                    }
                    return dates;
                })().map(function (value, index) {
                    console.log(index, value);
                    return {
                        key: value.date.toISOString().split("T")[0],
                        label: `${value.date.getDate()}/${value.date.getMonth()}`,
                        disabled: value.isPast
                    };
                })
            } />
            <ComboBox label="Hồ sơ">
                <option value="hxb">Huynh Xuan Bach</option>
                <option value="new">&lt;Thêm hồ sơ&gt;</option>
            </ComboBox>
            <ComboBox label="Khung giờ">
                <option value="none">7:30 - 8:00</option>
                <option value="rhm">8:00 - 8:30</option>
                <option value="tmh">9:30 - 9:00</option>
            </ComboBox>
            <SubmitButton attributes={{
                style: {
                    borderRadius: "8px",
                    fontSize: "18px",
                    lineHeight: "24px",
                    padding: "16px"
                }
            }}>Đặt lịch</SubmitButton>
        </form>
    );
}