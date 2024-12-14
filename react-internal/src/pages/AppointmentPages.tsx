import { ChangeEvent, FormEvent, useEffect, useRef, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom"
import { getAppointments, addAppointment, deleteAppointment, AppointmentResponse, getAppointmentById, updateAppointment } from "../services/AppointmentService";
import { showSnackBar } from "../fragments/snackbar";
import { URLSearchParams } from "url";
import { AppointmentModel } from "../models/AppointmentModel";
import { EAppointmentState, Permission } from "../models/emums";
import { getPatientById, getPatients, PatientResponse } from "../services/PatientService";
import { PatientModel } from "../models/PatientModel";
import { getAll, SchedulerPart } from "../services/ScheduleService";
import { getProfileById, getProfiles, ProfileResponse } from "../services/ProfileService";
import { ProfileModel } from "../models/ProfileModel";
import { getDoctorById, getDoctors } from "../services/DoctorService";
import { DoctorModel } from "../models/DoctorModel";

export function AppointmentsPage() {
    const location = useLocation();
    useEffect(() => {
        if (location.pathname === "/appointment") {
            document.title = "Đặt lịch"
        }
    }, [location.pathname]);

    const [model, setModel] = useState<AppointmentResponse[]>();
    const [reloadState, setReloadState] = useState(false);
    function promptReload() {
        const timeout = setTimeout(function () {
            setReloadState(function (state) { return !state; });
            clearTimeout(timeout);
        }, 1000);
    }

    useEffect(() => {
        getAppointments().then(function (response) {
            setModel(response || []);
        }, function (reason) {
            setModel([]);
        })
    }, [reloadState]);

    function onDeleteItem(event: React.MouseEvent<HTMLAnchorElement, MouseEvent>) {
        event.preventDefault();
        const roleIdToDelete = $(event.currentTarget).data('appointment-id');
        console.log("id: " + roleIdToDelete);
        const deleteModal = $('#deleteModal');
        deleteModal.data("idToDel", roleIdToDelete);
        (deleteModal as any).modal('show');

        $('#confirmDelete').on('click', function () {
            deleteAppointment(deleteModal.data("idToDel")).then(function (response) {
                if (response) {
                    showSnackBar({ success: 'Xóa đặt lịch thành công' });
                    promptReload();
                } else showSnackBar({ error: 'Xóa đặt lịch thất bại' });
            }, function (reason) {
                showSnackBar({ error: 'Xóa đặt lịch thất bại' });
            });
            (deleteModal as any).modal('hide');
        });
    }

    return (<>
        <link href="/plugins/table/datatable/datatables.css" rel="stylesheet" />
        <link href="/plugins/table/datatable/extensions/col-reorder/col-reorder.datatables.min.css" rel="stylesheet" />
        <link href="/plugins/table/datatable/extensions/fixed-columns/fixed-columns.datatables.min.css" rel="stylesheet" />
        <link href="/css/customs/plugins/light/table/datatable/dt-global_style.css" rel="stylesheet" />
        <link href="/css/customs/plugins/light/table/datatable/custom_dt_custom.css" rel="stylesheet" />

        <div>
            {/* BREADCRUMB */}
            <div className="page-meta">
                <nav className="breadcrumb-style-one" aria-label="breadcrumb">
                    <ol className="breadcrumb">
                        <li className="breadcrumb-item"><Link to="#">Hệ thống</Link></li>
                        <li className="breadcrumb-item active" aria-current="page">
                            Quản lý đặt lịch
                        </li>
                    </ol>
                </nav>
            </div>
            {/* /BREADCRUMB */}
            {/* DATATABLE */}
            <div className="row layout-top-spacing">
                <div className="col-xl-12 col-lg-12 col-sm-12">
                    <div className="statbox widget box box-shadow">
                        <div className="widget-header">
                            <div className="row">
                                <div className="col-xl-12 col-md-12 col-sm-12 col-12">
                                    <h4>Quản lý đặt lịch</h4>
                                </div>
                            </div>
                        </div>
                        <div className="widget-content widget-content-area">
                            <div className="layout-top-spacing ps-3 pe-3 col-12 mb-3">
                                <Link to="/appointment/create" className="btn btn-primary">Thêm mới</Link>
                            </div>
                            <table id="appointment-table" className="table style-3 dt-table-hover" style={{ width: "100%" }}>
                                <thead>
                                    <tr>
                                        <th style={{ width: "5%" }}>STT</th>
                                        <th>Mã lịch đặt</th>
                                        <th>Mã hồ sơ</th>
                                        <th>Mã bác sĩ</th>
                                        <th>Thời gian đặt</th>
                                        <th>Trạng thái</th>
                                        <th className="no-content" style={{ width: "5%" }}>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>{model && model.map(function (appointment: AppointmentResponse, index: number) {
                                    ++index;
                                    const metadata: Partial<(typeof EAppointmentState)["DISABLE"]> = (EAppointmentState as any)[(EAppointmentState as any)[appointment.state || ""]] || {};
                                    return (<tr>
                                        <td>{index}</td>
                                        <td>{appointment?.id}</td>
                                        <td>{appointment?.profile}</td>
                                        <td>{appointment?.doctor}</td>
                                        <td>{appointment?.at}</td>
                                        <td> <span className={"badge badge-light-" + (metadata?.badge || "secondary")}> {metadata?.displayName} </span> </td>

                                        <td className="table-controls d-flex justify-content-center align-items-center pt-2">
                                            <li>
                                                <Link to={"/appointment/edit?id=" + appointment.id}
                                                    className="bs-tooltip edit" data-bs-toggle="tooltip"
                                                    data-bs-placement="top">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                                                        viewBox="0 0 24 24" fill="none" stroke="currentColor"
                                                        stroke-width="2" stroke-linecap="round"
                                                        stroke-linejoin="round"
                                                        className="feather feather-edit-2 p-1 br-6 mb-1">
                                                        <path d="M17 3a2.828 2.828 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5L17 3z">
                                                        </path>
                                                    </svg>
                                                </Link>
                                            </li>
                                            <li>
                                                <Link to="#" data-appointment-id={appointment.id}
                                                    className="bs-tooltip delete" data-bs-toggle="tooltip"
                                                    data-bs-placement="top" onClick={onDeleteItem}>
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                                                        viewBox="0 0 24 24" fill="none" stroke="currentColor"
                                                        stroke-width="2" stroke-linecap="round"
                                                        stroke-linejoin="round"
                                                        className="feather feather-trash p-1 br-6 mb-1">
                                                        <polyline points="3 6 5 6 21 6"></polyline>
                                                        <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                                                    </svg>
                                                </Link>
                                            </li>
                                        </td>
                                    </tr>)
                                })}</tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div >

            {/* /DATATABLE */}
        </div >

        <script src="/plugins/table/datatable/datatables.js"></script>
        <script src="/plugins/table/datatable/extensions/col-reorder/col-reorder.datatables.min.js"></script>
        <script src="/plugins/table/datatable/extensions/fixed-columns/fixed-columns.datatables.min.js"></script>
        <script>{`$('#appointment-table').DataTable({
            "dom":
                "<'dt--top-section'" + 
                    "<'row'" +
                        "<'col-12 col-sm-6 d-flex justify-content-sm-start justify-content-center'l>" +
                        "<'col-12 col-sm-6 d-flex justify-content-sm-end justify-content-center mt-sm-0 mt-3'f>" +
                    ">" +
                ">" +
                "<'table-responsive'tr>" +
                "<'dt--bottom-section d-sm-flex justify-content-sm-between text-center'" +
                    "<'dt--pages-count  mb-sm-0 mb-3'i>" +
                    "<'dt--pagination'p>" +
                ">",
            "oLanguage": {
                "oPaginate": {
                    "sPrevious":
                        '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" className="feather feather-arrow-left">' +
                            '<line x1="19" y1="12" x2="5" y2="12"></line>' +
                            '<polyline points="12 19 5 12 12 5"></polyline>' +
                        '</svg>',
                    "sNext":
                        '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" className="feather feather-arrow-right">' +
                            '<line x1="5" y1="12" x2="19" y2="12"></line>' +
                            '<polyline points="12 5 19 12 12 19"></polyline>' +
                        '</svg>'
                },
                "sEmptyTable": "Chưa có dữ liệu",
                "sInfo": "Đang hiển thị trang _PAGE_ của  _PAGES_",
                "sSearch":
                    '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" className="feather feather-search">' +
                        '<circle cx="11" cy="11" r="8"></circle>' +
                        '<line x1="21" y1="21" x2="16.65" y2="16.65"></line>' +
                    '</svg>',
                "sSearchPlaceholder": "Tìm kiếm...",
                "sZeroRecords": "Không có bản ghi nào trùng khớp",
                "sLengthMenu": "Kết quả :  _MENU_",
            },
            "stripeClasses": [],
            "lengthMenu": [7, 10, 20, 50],
            "pageLength": 7,
            "ordering": false
        });`}</script>
    </>)
}

export function CreateAppointmentPage() {
    const location = useLocation();
    const navigate = useNavigate();
    useEffect(() => {
        if (location.pathname === "/appointment/create") {
            document.title = "Thêm mới đặt lịch"
        }
    }, [location.pathname]);

    const [profiles, setProfiles] = useState<ProfileResponse[]>([]);
    useEffect(() => {
        getProfiles().then(function (response) {
            setProfiles(response || []);
        }, function (reason) {
            setProfiles([]);
        });
    }, []);

    const [doctors, setDoctors] = useState<DoctorModel[]>([]);
    useEffect(() => {
        getDoctors().then(function (response) {
            setDoctors(response || []);
        }, function (reason) {
            setDoctors([]);
        });
    }, []);

    const [parts, setParts] = useState<SchedulerPart[]>([]);
    useEffect(() => {
        getAll().then(function (response) {
            setParts(response || []);
        }, function (reason) {
            setParts([]);
        });
    }, []);

    const [selectedProfile, setSelectedProfile] = useState<ProfileModel>();
    function onProfileChange(event: ChangeEvent<HTMLSelectElement>) {
        let selectedValue: string | number = event.currentTarget.value;
        if (selectedValue && ((selectedValue = Number(selectedValue)) || selectedValue === 0)) {
            getProfileById(selectedValue).then(function (response) {
                setSelectedProfile(response || undefined);
            }, function (reason) {
                setSelectedProfile(undefined);
            })
        } else setSelectedProfile(undefined);
    }

    const [selectedDoctor, setSelectedDoctor] = useState<DoctorModel>();
    function onDoctorChange(event: ChangeEvent<HTMLSelectElement>) {
        let selectedValue: string | number = event.currentTarget.value;
        if (selectedValue && ((selectedValue = Number(selectedValue)) || selectedValue === 0)) {
            getDoctorById(selectedValue).then(function (response) {
                setSelectedDoctor(response || undefined);
            }, function (reason) {
                setSelectedDoctor(undefined);
            })
        } else setSelectedDoctor(undefined);
    }

    const [selectedSchedulerPart, setSelectedSchedulerPart] = useState<SchedulerPart>();
    function onSchedulerPartChange(event: ChangeEvent<HTMLSelectElement>) {
        let selectedValue = event.currentTarget.value;
        setSelectedSchedulerPart(parts.reduce(function (orElse, value) {
            return (value.id?.toString() || "") === selectedValue ? value : orElse;
        }, undefined as SchedulerPart | undefined));
    }

    const formRef = useRef<HTMLFormElement>(null);
    useEffect(() => {
        formRef.current && ($(formRef.current) as any).validate({
            errorPlacement(error: any, element: any) {
                error.appendTo(element.next('.invalid-feedback'));
            },
            async submitHandler(form: HTMLFormElement, e: FormEvent) {
                e.preventDefault();
                const data = new FormData(form);
                let profile: any = data.get("profile") || undefined;
                let doctor: any = data.get("doctor") || undefined;
                let date: any = data.get("date") || undefined;
                let schedule_id: any = data.get("schedule_id") || undefined;
                let end: any = data.get("end") || undefined;

                if ("string" !== typeof profile || (!(profile = Number(profile)) && profile !== 0)) profile = undefined;
                if ("string" !== typeof doctor || (!(doctor = Number(doctor)) && doctor !== 0)) doctor = undefined;
                if ("string" !== typeof date) date = undefined;
                if ("string" !== typeof schedule_id || (!(schedule_id = Number(schedule_id)) && schedule_id !== 0)) schedule_id = undefined;
                if ("string" !== typeof end) end = undefined;

                addAppointment({
                    profile, doctor, date, begin_time: parts && parts.reduce(function (orElse, value) {
                        return value.id == schedule_id ? value.start : orElse;
                    }, undefined as string | undefined), end_time: end
                } as AppointmentModel).then(function (response) {
                    if (!response) return;
                    showSnackBar({ success: "Thêm mới đặt lịch thành công." });
                    const timeout = setTimeout(function () {
                        clearTimeout(timeout);
                        navigate("/appointment");
                    });
                }, function (reason) {
                    showSnackBar({ error: "Lỗi không thể thêm đặt lịch này." });
                });
            }
        });
    }, []);

    return (<>
        <link rel="stylesheet" type="text/css" href="/css/customs/assets/light/apps/blog-post.css" />
        <link rel="stylesheet" type="text/css" href="/css/customs/assets/light/apps/ecommerce-create.css" />
        <link rel="stylesheet" type="text/css" href="/plugins/table/datatable/datatables.css" />
        <link rel="stylesheet" type="text/css" href="/plugins/table/datatable/extensions/col-reorder/col-reorder.datatables.min.css" />
        <link rel="stylesheet" type="text/css" href="/plugins/table/datatable/extensions/fixed-columns/fixed-columns.datatables.min.css" />
        <link rel="stylesheet" type="text/css" href="/css/customs/plugins/light/table/datatable/dt-global_style.css" />
        <link rel="stylesheet" type="text/css" href="/css/customs/plugins/light/table/datatable/custom_dt_custom.css" />
        <link rel="stylesheet" type="text/css" href="/css/customs/assets/light/apps/blog-create.css" />
        <link rel="stylesheet" type="text/css" href="/css/customs/plugins/light/tomSelect/custom-tomSelect.css" />
        <link rel="stylesheet" type="text/css" href="/css/customs/plugins/dark/tomSelect/custom-tomSelect.css" />
        <link rel="stylesheet" type="text/css" href="/plugins/tomSelect/tom-select.default.min.css" />

        <style>{`
            #datatable thead th {
                font-weight: bold;
                background-color: #f8f9fa;
                color: #333;
            }

            #datatable tbody td {
                vertical-align: middle;
            }

            .table-container {
                overflow-x: auto;
            }

            .widget-content-area {
                padding: 20px !important;
            }
        `}</style>

        <div className="middle-content container-xxl p-0">
            <div className="d-flex justify-content-between mt-4 mb-2">
                <div>
                    <div className="d-flex align-items-start">
                        <h5 className="text-bold">
                            THÊM MỚI ĐẶT LỊCH
                            <span className="badge badge-dark"></span>
                        </h5>
                    </div>
                </div>
                <div>
                    <iframe id="pdfFrame" style={{ display: "none" }}></iframe>
                    <Link to="/appointment" className="btn">Trở lại</Link>
                </div>
            </div>

            <div className="row mb-4 layout-spacing">
                <div className="col-xxl-8 col-xl-12 col-lg-12 col-md-12 col-sm-12">
                    <div className="statbox widget box box-shadow">
                        <div className="widget-header">
                            <div className="row">
                                <div className="col-xl-12 col-md-12 col-sm-12 col-12">
                                    <h4>Thông tin đặt lịch</h4>
                                </div>
                            </div>
                        </div>

                        <form ref={formRef} id="general-settings">
                            <div className="widget-content widget-content-area blog-create-section">
                                <div className="row">
                                    <div className="col-sm-12">
                                        <div className="col-12">
                                            <div className="form-group mb-4">
                                                <label htmlFor="profile">Hồ sơ<strong className="text-danger">*</strong></label>
                                                <select id="profile" name="profile" className="form-control"
                                                    onChange={onProfileChange}>
                                                    {profiles && profiles.map(function (value) {
                                                        return (<option value={value.id}>{value.full_name || ""}</option>)
                                                    })}
                                                    <option value="">Chọn</option>
                                                </select>
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>

                                            <div className="form-group mb-4">
                                                <label htmlFor="doctor">Bác sĩ<strong className="text-danger">*</strong></label>
                                                <select id="doctor" name="doctor" className="form-control"
                                                    onChange={onDoctorChange}>
                                                    {doctors && doctors.map(function (value) {
                                                        return (<option value={value.id}>{value.full_name || ""}</option>)
                                                    })}
                                                    <option value="">Chọn</option>
                                                </select>
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>

                                            <div className="form-group mb-4">
                                                <label htmlFor="date">Ngày đặt<strong className="text-danger">*</strong></label>
                                                <input id="date" name="date" placeholder="Chọn ngày đặt" type="date"
                                                    className="form-control" min={new Date().toISOString().split("T")[0]} />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>

                                            <div className="form-group mb-4">
                                                <label htmlFor="start">Thời gian bắt đầu<strong className="text-danger">*</strong></label>
                                                <select id="start" name="schedule_id" className="form-control"
                                                    onChange={onSchedulerPartChange} value={selectedSchedulerPart?.id || ""}>
                                                    {parts && parts.map(function (value) {
                                                        return (<option value={value.id}>{(value.start ? new Date(value.start) : new Date()).toLocaleTimeString()}</option>)
                                                    })}
                                                    <option value="">Chọn thời gian bắt đầu</option>
                                                </select>

                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>

                                            <div className="form-group mb-4">
                                                <label htmlFor="end">Thời gian kết thúc<strong className="text-danger">*</strong></label>
                                                <input id="end" name="end" style={{ color: "cadetblue" }} className="form-control" type="time" readOnly={true} value={selectedSchedulerPart?.end} />
                                            </div>

                                            <p className="text-muted mb-4">!!! Vui lòng điền đầy đủ thông tin và ngày đặt trước khi hoàn tất.</p>

                                            <button type="submit" className="btn btn-primary">Hoàn tất</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>

                <div className="col-xxl-4 col-xl-12 col-lg-12 col-md-12 col-sm-12 mt-xxl-0 mt-4">
                    <div className="statbox widget box box-shadow">
                        <div className="widget-header">
                            <div className="row">
                                <div className="col-xl-12 col-md-12 col-sm-12 col-12">
                                    <h4>Thông tin hồ sơ:</h4>
                                </div>
                            </div>
                        </div>
                        <div className="widget-content widget-content-area blog-create-section" id="profile-info">
                            <div className="row">
                                <div className="col-xxl-12">
                                    <div className="d-flex">
                                        <p className="text-start me-1">Mã hồ sơ :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedProfile?.id || ""}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Mã bệnh nhân :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedProfile?.patient || ""}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Họ tên :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedProfile?.full_name || ""}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Ngày sinh :</p>
                                        <b>
                                            <p className="text-dark text-bold">{(selectedProfile?.date_of_birth ? new Date(selectedProfile.date_of_birth) : new Date()).toLocaleDateString()}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Giới tính :</p>
                                        <b>
                                            <p className="text-dark text-bold">{({ M: "Nam", F: "Nữ" } as any)[selectedProfile?.gender || ""] || "Khác"}</p>
                                        </b>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="statbox widget box box-shadow mt-3">
                        <div className="widget-header">
                            <div className="row">
                                <div className="col-xl-12 col-md-12 col-sm-12 col-12">
                                    <h4>Thông tin bác sĩ:</h4>
                                </div>
                            </div>
                        </div>
                        <div className="widget-content widget-content-area blog-create-section" id="doctor-info">
                            <div className="row">
                                <div className="col-xxl-12">
                                    <div className="d-flex">
                                        <p className="text-start me-1">Mã bác sĩ :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedDoctor?.id || ""}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Tên :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedDoctor?.full_name || ""}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Email :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedDoctor?.email || ""}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Vị trí :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedDoctor?.position || ""}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Chứng chỉ :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedDoctor?.certificate || ""}</p>
                                        </b>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="/js/customs/assets/apps/blog-create.js"></script>
        <script src="/plugins/table/datatable/datatables.js"></script>
        <script src="/plugins/table/datatable/extensions/col-reorder/col-reorder.datatables.min.js"></script>
        <script src="/plugins/table/datatable/extensions/fixed-columns/fixed-columns.datatables.min.js"></script>

        <script src="/plugins/tomSelect/tom-select.base.js"></script>
        <script src="/plugins/tomSelect/custom-tom-select.js"></script>
        <script type="text/javascript">{`
            //Tom select
            new TomSelect("#doctor", {
                create: true,
                sortField: {
                    field: "text",
                    direction: "asc"
                }
            });

            new TomSelect("#profile", {
                create: true,
                sortField: {
                    field: "text",
                    direction: "asc"
                }
            });

            new TomSelect("#state", {
                create: true,
                sortField: {
                    field: "text",
                    direction: "asc"
                }
            });
        `}</script>
    </>)
}

export function EditAppointmentPage() {
    const location = useLocation();
    const params = new URLSearchParams();
    const id = Number(params.get("id") || undefined);
    const navigate = useNavigate();
    useEffect(() => {
        if (location.pathname === "/appointment/edit") {
            document.title = "Chỉnh sửa đặt lịch"
        }
    }, [location.pathname]);

    const [model, setModel] = useState<AppointmentModel>();
    useEffect(() => {
        getAppointmentById(id).then(function (response) {
            setModel(response || undefined);
        }, function (reason) {
            setModel(undefined);
        })
    }, []);

    const [profiles, setProfiles] = useState<ProfileResponse[]>([]);
    useEffect(() => {
        getProfiles().then(function (response) {
            setProfiles(response || []);
        }, function (reason) {
            setProfiles([]);
        });
    }, []);

    const [doctors, setDoctors] = useState<DoctorModel[]>([]);
    useEffect(() => {
        getDoctors().then(function (response) {
            setDoctors(response || []);
        }, function (reason) {
            setDoctors([]);
        });
    }, []);

    const [selectedProfile, setSelectedProfile] = useState<ProfileModel>();
    function onProfileChange(event: ChangeEvent<HTMLSelectElement>) {
        let selectedValue: string | number = event.currentTarget.value;
        if (selectedValue && ((selectedValue = Number(selectedValue)) || selectedValue === 0)) {
            getProfileById(selectedValue).then(function (response) {
                setSelectedProfile(response || undefined);
            }, function (reason) {
                setSelectedProfile(undefined);
            })
        } else setSelectedProfile(undefined);
    }

    const [selectedDoctor, setSelectedDoctor] = useState<DoctorModel>();
    function onDoctorChange(event: ChangeEvent<HTMLSelectElement>) {
        let selectedValue: string | number = event.currentTarget.value;
        if (selectedValue && ((selectedValue = Number(selectedValue)) || selectedValue === 0)) {
            getDoctorById(selectedValue).then(function (response) {
                setSelectedDoctor(response || undefined);
            }, function (reason) {
                setSelectedDoctor(undefined);
            })
        } else setSelectedDoctor(undefined);
    }

    const formRef = useRef<HTMLFormElement>(null);
    useEffect(() => {
        formRef.current && ($(formRef.current) as any).validate({
            errorPlacement(error: any, element: any) {
                error.appendTo(element.next('.invalid-feedback'));
            },
            async submitHandler(form: HTMLFormElement, e: FormEvent) {
                e.preventDefault();
                const data = new FormData(form);
                let profile: any = data.get("profile") || undefined;

                if ("string" !== typeof profile || (!(profile = Number(profile)) && profile !== 0)) profile = undefined;

                updateAppointment(id, profile).then(function (response) {
                    if (!response) return;
                    showSnackBar({ success: "Thêm mới đặt lịch thành công." });
                    const timeout = setTimeout(function () {
                        clearTimeout(timeout);
                        navigate("/appointment");
                    });
                }, function (reason) {
                    showSnackBar({ error: "Lỗi không thể thêm đặt lịch này." });
                });
            }
        });
    }, []);

    return (<>
        <link rel="stylesheet" type="text/css" href="/css/customs/assets/light/apps/blog-post.css" />
        <link rel="stylesheet" type="text/css" href="/css/customs/assets/light/apps/ecommerce-create.css" />
        <link rel="stylesheet" type="text/css" href="/plugins/table/datatable/datatables.css" />
        <link rel="stylesheet" type="text/css" href="/plugins/table/datatable/extensions/col-reorder/col-reorder.datatables.min.css" />
        <link rel="stylesheet" type="text/css" href="/plugins/table/datatable/extensions/fixed-columns/fixed-columns.datatables.min.css" />
        <link rel="stylesheet" type="text/css" href="/css/customs/plugins/light/table/datatable/dt-global_style.css" />
        <link rel="stylesheet" type="text/css" href="/css/customs/plugins/light/table/datatable/custom_dt_custom.css" />
        <link rel="stylesheet" type="text/css" href="/css/customs/assets/light/apps/blog-create.css" />
        <link rel="stylesheet" type="text/css" href="/css/customs/plugins/light/tomSelect/custom-tomSelect.css" />
        <link rel="stylesheet" type="text/css" href="/css/customs/plugins/dark/tomSelect/custom-tomSelect.css" />
        <link rel="stylesheet" type="text/css" href="/plugins/tomSelect/tom-select.default.min.css" />

        <style>{`
            #datatable thead th {
                font-weight: bold;
                background-color: #f8f9fa;
                color: #333;
            }

            #datatable tbody td {
                vertical-align: middle;
            }

            .table-container {
                overflow-x: auto;
            }

            .widget-content-area {
                padding: 20px !important;
            }
        `}</style>


        <div className="middle-content container-xxl p-0">

            <div className="d-flex justify-content-between mt-4 mb-2">
                <div>
                    <div className="d-flex align-items-start">
                        <h5 className="text-bold">
                            CHỈNH SỬA ĐẶT LỊCH
                            <span className="badge badge-dark"></span>
                        </h5>
                    </div>

                </div>
                <div>
                    <iframe id="pdfFrame" style={{ display: "none" }}></iframe>
                    <Link to="/appointment" className="btn">Trở lại</Link>
                </div>
            </div>

            <div className="row mb-4 layout-spacing">
                <div className="col-xxl-8 col-xl-12 col-lg-12 col-md-12 col-sm-12">
                    <div className="statbox widget box box-shadow">
                        <div className="widget-header">
                            <div className="row">
                                <div className="col-xl-12 col-md-12 col-sm-12 col-12">
                                    <h4>Thông tin đặt lịch</h4>
                                </div>
                            </div>
                        </div>

                        <form ref={formRef} id="general-settings">
                            <div className="widget-content widget-content-area blog-create-section">
                                <div className="row">
                                    <div className="col-sm-12">
                                        <div className="col-12">
                                            <div className="form-group mb-4">
                                                <label htmlFor="profile">Hồ sơ<strong className="text-danger">*</strong></label>
                                                <select id="profile" name="profile" className="form-control"
                                                    onChange={onProfileChange} value={model?.profile}>
                                                    {profiles && profiles.map(function (value) {
                                                        return (<option value={value.id}>{value.full_name || ""}</option>)
                                                    })}
                                                    <option value="">Chọn</option>
                                                </select>
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>

                                            <div className="form-group mb-4">
                                                <label htmlFor="doctor">Bác sĩ<strong className="text-danger">*</strong></label>
                                                <select id="doctor" name="doctor" className="form-control"
                                                    onChange={onDoctorChange} disabled={true} value={model?.doctor}>
                                                    {doctors && doctors.map(function (value) {
                                                        return (<option value={value.id}>{value.full_name || ""}</option>)
                                                    })}
                                                    <option value="">Chọn</option>
                                                </select>
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>

                                            <div className="form-group mb-4">
                                                <label htmlFor="date">Thời gian đặt<strong className="text-danger">*</strong></label>
                                                <input id="at" name="at" placeholder="Thời gian đặt" type="datetime-local" style={{ color: "cadetblue" }}
                                                    className="form-control" min={new Date().toISOString().split("T")[0]} readOnly={false} />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>

                                            <p className="text-muted mb-4">!!! Vui lòng điền đầy đủ thông tin và ngày đặt trước khi hoàn tất.</p>

                                            <button type="submit" className="btn btn-primary">Hoàn tất</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>

                <div className="col-xxl-4 col-xl-12 col-lg-12 col-md-12 col-sm-12 mt-xxl-0 mt-4">
                    <div className="statbox widget box box-shadow">
                        <div className="widget-header">
                            <div className="row">
                                <div className="col-xl-12 col-md-12 col-sm-12 col-12">
                                    <h4>Thông tin hồ sơ:</h4>
                                </div>
                            </div>
                        </div>
                        <div className="widget-content widget-content-area blog-create-section" id="profile-info">
                            <div className="row">
                                <div className="col-xxl-12">
                                    <div className="d-flex">
                                        <p className="text-start me-1">Mã hồ sơ :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedProfile?.id || ""}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Mã bệnh nhân :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedProfile?.patient || ""}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Họ tên :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedProfile?.full_name || ""}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Ngày sinh :</p>
                                        <b>
                                            <p className="text-dark text-bold">{(selectedProfile?.date_of_birth ? new Date(selectedProfile.date_of_birth) : new Date()).toLocaleDateString()}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Giới tính :</p>
                                        <b>
                                            <p className="text-dark text-bold">{({ M: "Nam", F: "Nữ" } as any)[selectedProfile?.gender || ""] || "Khác"}</p>
                                        </b>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="statbox widget box box-shadow mt-3">
                        <div className="widget-header">
                            <div className="row">
                                <div className="col-xl-12 col-md-12 col-sm-12 col-12">
                                    <h4>Thông tin bác sĩ:</h4>
                                </div>
                            </div>
                        </div>
                        <div className="widget-content widget-content-area blog-create-section" id="doctor-info">
                            <div className="row">
                                <div className="col-xxl-12">
                                    <div className="d-flex">
                                        <p className="text-start me-1">Mã bác sĩ :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedDoctor?.id || ""}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Tên :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedDoctor?.full_name || ""}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Email :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedDoctor?.email || ""}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Vị trí :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedDoctor?.position || ""}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Chứng chỉ :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedDoctor?.certificate || ""}</p>
                                        </b>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="/js/customs/assets/apps/blog-create.js"></script>
        <script src="/plugins/table/datatable/datatables.js"></script>
        <script src="/plugins/table/datatable/extensions/col-reorder/col-reorder.datatables.min.js"></script>
        <script src="/plugins/table/datatable/extensions/fixed-columns/fixed-columns.datatables.min.js"></script>

        <script src="/plugins/tomSelect/tom-select.base.js"></script>
        <script src="/plugins/tomSelect/custom-tom-select.js"></script>
        <script type="text/javascript">{`
            //Tom select
            new TomSelect("#doctor", {
                create: true,
                sortField: {
                    field: "text",
                    direction: "asc"
                }
            });

            new TomSelect("#profile", {
                create: true,
                sortField: {
                    field: "text",
                    direction: "asc"
                }
            });

            new TomSelect("#state", {
                create: true,
                sortField: {
                    field: "text"
                }
            });
        `}</script>
    </>)
}