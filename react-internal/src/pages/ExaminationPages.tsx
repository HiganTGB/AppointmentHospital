import { ChangeEvent, FormEvent, useEffect, useRef, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom"
import { getExaminations, addExamination, deleteExamination, ExaminationResponse, getExaminationById, updateExamination } from "../services/ExaminationService";
import { showSnackBar } from "../fragments/snackbar";
import { URLSearchParams } from "url";
import { ExaminationModel } from "../models/ExaminationModel";
import { EExaminationState, Permission } from "../models/emums";
import { getPatientById, getPatients, PatientResponse } from "../services/PatientService";
import { PatientModel } from "../models/PatientModel";
import { AppointmentResponse, getAppointmentById, getAppointmentResponseById, getAppointments } from "../services/AppointmentService";
import { AppointmentModel } from "../models/AppointmentModel";
import { DiagnosticServiceResponse, getDiagnosticServiceById, getDiagnosticServices } from "../services/DiagnosticService";
import { getDoctors } from "../services/DoctorService";
import { DoctorModel } from "../models/DoctorModel";

export function ExaminationsPage() {
    const location = useLocation();
    useEffect(() => {
        if (location.pathname === "/examination") {
            document.title = "Vai trò"
        }
    }, [location.pathname]);

    const [model, setModel] = useState<ExaminationModel[]>();
    const [reloadState, setReloadState] = useState(false);
    function promptReload() {
        const timeout = setTimeout(function () {
            setReloadState(function (state) { return !state; });
            clearTimeout(timeout);
        }, 1000);
    }

    useEffect(() => {
        getExaminations().then(function (response) {
            setModel(response || []);
        }, function (reason) {
            setModel([]);
        })
    }, [reloadState]);

    function onDeleteItem(event: React.MouseEvent<HTMLAnchorElement, MouseEvent>) {
        event.preventDefault();
        const roleIdToDelete = $(event.currentTarget).data('examination-id');
        console.log("id: " + roleIdToDelete);
        const deleteModal = $('#deleteModal');
        deleteModal.data("idToDel", roleIdToDelete);
        (deleteModal as any).modal('show');

        $('#confirmDelete').on('click', function () {
            deleteExamination(deleteModal.data("idToDel")).then(function (response) {
                if (response) {
                    showSnackBar({ success: 'Xóa khám bệnh thành công' });
                    promptReload();
                } else showSnackBar({ error: 'Xóa khám bệnh thất bại' });
            }, function (reason) {
                showSnackBar({ error: 'Xóa khám bệnh thất bại' });
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
                        <li className="breadcrumb-item"><Link to="#">Dịch vụ</Link></li>
                        <li className="breadcrumb-item active" aria-current="page">
                            Quản lý khám bệnh
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
                                    <h4>Quản lý khám bệnh</h4>
                                </div>
                            </div>
                        </div>
                        <div className="widget-content widget-content-area">
                            <div className="layout-top-spacing ps-3 pe-3 col-12 mb-3">
                                <Link to="/examination/create" className="btn btn-primary">Thêm mới</Link>
                            </div>
                            <table id="examination-table" className="table style-3 dt-table-hover" style={{ width: "100%" }}>
                                <thead>
                                    <tr>
                                        <th style={{ width: "5%" }}>STT</th>
                                        <th>Mã khám bệnh</th>
                                        <th>Mã lịch đặt</th>
                                        <th>Chuẩn đoán</th>
                                        <th>Mô tả</th>
                                        <th>Trạng thái</th>
                                        <th className="no-content" style={{ width: "5%" }}>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>{model && model.map(function (examination: ExaminationModel, index: number) {
                                    ++index;
                                    const metadata: Partial<(typeof EExaminationState)["DISABLE"]>
                                        = (EExaminationState as any)[(EExaminationState as any)[examination.state || ""]] || {};
                                    return (<tr>
                                        <td>{index}</td>
                                        <td>{examination?.id}</td>
                                        <td>{examination?.appointment}</td>
                                        <td>{examination?.diagnostic}</td>
                                        <td>{examination?.description}</td>
                                        <td>
                                            <span className={"badge badge-light-" + metadata.badge}>{metadata.displayName}</span>
                                        </td>

                                        <td className="table-controls d-flex justify-content-center align-items-center pt-2">
                                            <li>
                                                <Link to={"/examination/edit?id=" + examination.id}
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
                                                <Link to="#" data-examination-id={examination.id}
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
        <script>{`$('#examination-table').DataTable({
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

export function CreateExaminationPage() {
    const location = useLocation();
    const navigate = useNavigate();
    useEffect(() => {
        if (location.pathname === "/examination/create") {
            document.title = "Thêm mới khám bệnh"
        }
    }, [location.pathname]);

    const [appointments, setAppointments] = useState<AppointmentResponse[]>([]);
    useEffect(() => {
        getAppointments().then(function (response) {
            setAppointments(response || []);
        }, function (reason) {
            setAppointments([]);
        });
    }, []);

    const [selectedAppointment, setSelectedAppointment] = useState<AppointmentResponse>();
    function onAppointmentChange(event: ChangeEvent<HTMLSelectElement>) {
        let selectedValue: string | number = event.currentTarget.value;
        if (selectedValue && ((selectedValue = Number(selectedValue)) || selectedValue === 0)) {
            getAppointmentResponseById(selectedValue).then(function (response) {
                setSelectedAppointment(response || undefined);
            }, function (reason) {
                setSelectedAppointment(undefined);
            })
        } else setSelectedAppointment(undefined);
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
                let appointment: any = data.get("appointment") || undefined;
                if ("string" !== typeof appointment || (!(appointment = Number(appointment)) && appointment !== 0)) appointment = undefined;
                addExamination(appointment).then(function (response) {
                    if (!response) return;
                    showSnackBar({ success: "Thêm mới khám bệnh thành công." });
                    const timeout = setTimeout(function () {
                        clearTimeout(timeout);
                        navigate("/examination");
                    });
                }, function (reason) {
                    showSnackBar({ error: "Lỗi không thể thêm khám bệnh này." });
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
                            THÊM MỚI KHÁM BỆNH
                            <span className="badge badge-dark"></span>
                        </h5>
                    </div>
                </div>
                <div>
                    <iframe id="pdfFrame" style={{ display: "none" }}></iframe>
                    <Link to="/examination" className="btn">Trở lại</Link>
                </div>
            </div>

            <div className="row mb-4 layout-spacing">
                <div className="col-xxl-8 col-xl-12 col-lg-12 col-md-12 col-sm-12">
                    <div className="statbox widget box box-shadow">
                        <div className="widget-header">
                            <div className="row">
                                <div className="col-xl-12 col-md-12 col-sm-12 col-12">
                                    <h4>Thông tin khám bệnh</h4>
                                </div>
                            </div>
                        </div>

                        <form ref={formRef} id="general-settings">
                            <div className="widget-content widget-content-area blog-create-section">
                                <div className="row">
                                    <div className="col-sm-12">
                                        <div className="col-12">
                                            <div className="form-group mb-4">
                                                <label htmlFor="appointment">Đơn đặt<strong className="text-danger">*</strong></label>
                                                <select id="appointment" name="appointment" className="form-control"
                                                    onChange={onAppointmentChange} value={selectedAppointment?.id || ""} disabled={true}>
                                                    {appointments && appointments.map(function (value) {
                                                        return (<option value={value.id}>{value.id}</option>);
                                                    })}
                                                    <option value="">Chọn</option>
                                                </select>
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>

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
                                    <h4>Thông tin đơn đặt:</h4>
                                </div>
                            </div>
                        </div>
                        <div className="widget-content widget-content-area blog-create-section" id="appointment-info">
                            <div className="row">
                                <div className="col-xxl-12">
                                    <div className="d-flex">
                                        <p className="text-start me-1">Mã lịch đặt :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedAppointment?.id || ""}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Thời gian đặt :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedAppointment?.at || ""}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Số :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedAppointment?.number || ""}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Mã hồ sơ :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedAppointment?.profile || ""}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Mã bác sĩ :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedAppointment?.doctor || ""}</p>
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
            new TomSelect("#patient", {
                create: true,
                sortField: {
                    field: "text",
                    direction: "asc"
                }
            });
        `}</script>
    </>)
}

export function EditExaminationPage() {
    const location = useLocation();
    const params = new URLSearchParams();
    const id = Number(params.get("id") || undefined);
    const navigate = useNavigate();
    useEffect(() => {
        if (location.pathname === "/examination/edit") {
            document.title = "Chỉnh sửa khám bệnh"
        }
    }, [location.pathname]);

    const [appointments, setAppointments] = useState<AppointmentResponse[]>([]);
    useEffect(() => {
        getAppointments().then(function (response) {
            setAppointments(response || []);
        }, function (reason) {
            setAppointments([]);
        });
    }, []);

    const [selectedAppointment, setSelectedAppointment] = useState<AppointmentResponse>();
    function onAppointmentChange(event: ChangeEvent<HTMLSelectElement>) {
        let selectedValue: string | number = event.currentTarget.value;
        if (selectedValue && ((selectedValue = Number(selectedValue)) || selectedValue === 0)) {
            getAppointmentResponseById(selectedValue).then(function (response) {
                setSelectedAppointment(response || undefined);
            }, function (reason) {
                setSelectedAppointment(undefined);
            })
        } else setSelectedAppointment(undefined);
    }

    const [diagnosticServices, setDiagnosticServices] = useState<DiagnosticServiceResponse[]>([]);
    useEffect(() => {
        getDiagnosticServices().then(function (response) {
            setDiagnosticServices(response || []);
        }, function (reason) {
            setDiagnosticServices([]);
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

    const [model, setModel] = useState<ExaminationModel>();
    useEffect(() => {
        getExaminationById(id).then(function (response) {
            setModel(response || undefined);
        }, function (reason) {
            setModel(undefined);
        })
    }, []);

    function onDianosticChange(event: ChangeEvent<HTMLInputElement>) {
        const diag = Number(event.currentTarget.id.split("_")[1])
        if (model?.selectedDiagnostics) {
            if (event.currentTarget.checked) {
                const i = model.selectedDiagnostics.indexOf(diag);
                if (i >= 0) model.selectedDiagnostics.splice(i, 1);
            } else {
                model.selectedDiagnostics.push(diag);
            }
        }
        const doctorSelection = document.getElementById(`doctorSelection_${diag}`);
        const doctorSelect = document.getElementById(`doctor_${diag}`);
        if (!doctorSelection || !doctorSelect) return;
        if (event.currentTarget.checked) {
            doctorSelection.style.display = "block";
        } else {
            doctorSelection.style.display = "none";
            (doctorSelect as HTMLSelectElement).value = "";
        }
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
                let appointment: any = data.get("appointment") || undefined;
                let diagnostic: any = data.get("diagnostic") || undefined;
                let description: any = data.get("description") || undefined;

                if ("string" !== typeof appointment || (!(appointment = Number(appointment)) && appointment !== 0)) appointment = undefined;
                if ("string" !== typeof diagnostic) diagnostic = undefined;
                if ("string" !== typeof description) description = undefined;
                // Todo: 
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
        <link rel="stylesheet" type="text/css" href="/css/customs/plugins/light/editors/quill/quill.snow.css" />
        <link rel="stylesheet" type="text/css" href="/css/customs/assets/dark/apps/ecommerce-create.css" />
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

            .diagnostic-list {
                display: flex;
                flex-wrap: wrap;
                gap: 10px; /* Adjust the gap between items */
            }

            .diagnostic-list .form-check {
                flex: 0 0 calc(25% - 10px); /* 25% width for 4 items per row, minus gap */
                box-sizing: border-box;
            }

            .custom-textarea-left {
                padding-left: 20px;
            }
        `}</style>


        <div className="middle-content container-xxl p-0">

            <div className="d-flex justify-content-between mt-4 mb-2">
                <div>
                    <div className="d-flex align-items-start">
                        <h5 className="text-bold">
                            CHỈNH SỬA KHÁM BỆNH
                            <span className="badge badge-dark"></span>
                        </h5>
                    </div>

                </div>
                <div>
                    <iframe id="pdfFrame" style={{ display: "none" }}></iframe>
                    <Link to="/examination" className="btn">Trở lại</Link>
                </div>
            </div>

            <div className="row mb-4 layout-spacing">
                <div className="col-xxl-8 col-xl-12 col-lg-12 col-md-12 col-sm-12">
                    <div className="statbox widget box box-shadow">
                        <div className="widget-header">
                            <div className="row">
                                <div className="col-xl-12 col-md-12 col-sm-12 col-12">
                                    <h4>Thông tin khám bệnh</h4>
                                </div>
                            </div>
                        </div>

                        <form ref={formRef} id="general-settings">
                            <div className="widget-content widget-content-area blog-create-section">
                                <div className="row">
                                    <div className="col-sm-12">
                                        <div className="col-12">
                                            <div className="form-group mb-4">
                                                <label htmlFor="appointment">Đơn đặt<strong className="text-danger">*</strong></label>
                                                <select name="appointment" id="profile"
                                                    className="form-control custom-textarea-left"
                                                    style={{ color: "cadetblue" }} onChange={onAppointmentChange}
                                                    disabled={true} value={model?.appointment}>
                                                    {appointments && appointments.map(function (appointment) {
                                                        return (<option value={appointment.id}>{appointment.id}</option>)
                                                    })}
                                                    <option value="">Chọn</option>
                                                </select>
                                            </div>

                                            <div className="form-group mb-4">
                                                <label htmlFor="diagnostic">Chuẩn đoán <strong className="text-danger"></strong></label>
                                                <input name="diagnostic" type="text" id="diagnostic"
                                                    className="form-control" placeholder="Chuẩn đoán" value={model?.diagnostic} />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>

                                            <div className="form-group mb-4">
                                                <label htmlFor="description">Mô tả <strong className="text-danger"></strong></label>
                                                <input name="description" type="text" id="description"
                                                    className="form-control" placeholder="Mô tả" value={model?.description} />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>

                                            <div className="row mt-4 mb-2">
                                                <div className="col-xl-12 col-md-12 col-sm-12 col-12">
                                                    <h4>Chọn dịch vụ khám</h4>
                                                </div>
                                            </div>

                                            <div className="diagnostic-list">
                                                {diagnosticServices && diagnosticServices.map(function (diagnostic) {
                                                    return (<div className="form-check">
                                                        <input className="form-check-input" type="checkbox"
                                                            id={"diagnostic_" + diagnostic.id} value={"diagnostic_" + diagnostic.id}
                                                            onChange={onDianosticChange} />
                                                        <label className="form-check-label" htmlFor={"diagnostic_" + diagnostic.id}> {diagnostic.name} </label>
                                                        <div className="doctor-selection" id={"doctorSelection_" + diagnostic.id} style={{ display: model?.selectedDiagnostics && diagnostic.id && model.selectedDiagnostics.includes(diagnostic.id) ? "block" : "none" }}>
                                                            <label htmlFor={"doctor_" + diagnostic.id}>Tên bác sĩ<strong className="text-danger">*</strong></label>
                                                            <select id={"doctor_" + diagnostic.id} className="form-control" value={model?.id}>
                                                                <option value="">Chọn</option>
                                                                {doctors && doctors.map(function (doctor) {
                                                                    return (<option value={doctor.id}>{doctor.full_name}</option>);
                                                                })}
                                                            </select>
                                                            <span className="invalid-feedback" role="alert"></span>
                                                        </div>
                                                    </div>)
                                                })}
                                            </div>

                                            <div className="col-xxl-9 col-xl-12 col-lg-12 col-md-12 col-sm-12 mt-4 mb-3" style={{ width: "100%" }}>
                                                <div className="widget-content widget-content-area ecommerce-create-section">
                                                    <div className="col-12">
                                                        <div className="form-group mb-4">
                                                            <label htmlFor="prescription"> Đơn thuốc </label>
                                                            {/* Add right padding of 20px directly */}
                                                            <textarea name="prescription" className="form-control" id="prescription"
                                                                placeholder="Viết đơn thuốc cho bệnh nhân vào đây"
                                                                style={{ paddingRight: "20px" }}></textarea>
                                                            <span className="invalid-feedback" role="alert"></span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <p className="text-muted mb-4">!!! Vui lòng điền đầy đủ thông tin trước khi hoàn tất.</p>

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
                                    <h4>Thông tin đơn đặt:</h4>
                                </div>
                            </div>
                        </div>
                        <div className="widget-content widget-content-area blog-create-section" id="appointment-info">
                            <div className="row">
                                <div className="col-xxl-12">
                                    <div className="d-flex">
                                        <p className="text-start me-1">Mã lịch đặt :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedAppointment?.id || ""}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Thời gian đặt :</p>
                                        <b>
                                            <p className="text-dark text-bold">{(selectedAppointment?.at ? new Date(selectedAppointment.at) : new Date()).toLocaleString()}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Số :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedAppointment?.number || ""}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Mã hồ sơ :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedAppointment?.profile || ""}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Mã bác sĩ :</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedAppointment?.doctor || ""}</p>
                                        </b>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div >
        </div >

        <script src="/js/customs/assets/apps/blog-create.js"></script>
        <script src="/plugins/table/datatable/datatables.js"></script>
        <script src="/plugins/table/datatable/extensions/col-reorder/col-reorder.datatables.min.js"></script>
        <script src="/plugins/table/datatable/extensions/fixed-columns/fixed-columns.datatables.min.js"></script>

        <script src="/plugins/tomSelect/tom-select.base.js"></script>
        <script src="/plugins/tomSelect/custom-tom-select.js"></script>

        <script src="~/js/customs/assets/apps/ecommerce-create.js"></script>
        <script src="~/plugins/tomSelect/tom-select.base.js"></script>
        <script src="~/plugins/tomSelect/custom-tom-select.js"></script>
        <script src="~/plugins/editors/quill/quill.js"></script>
        <script src="~/plugins/editors/quill/custom-quill.js"></script>

        <script type="text/javascript">{`
            var quill = new Quill('#perscription', {
                modules: {
                    toolbar: [[{
                        'header': [1, 2, 3, 4, 5, 6, false]
                    }], [{
                        'font': []
                    }, {
                        'align': []
                    }], [{
                        'list': 'ordered'
                    }, {
                        'list': 'bullet'
                    }], [{
                        'indent': '-1'
                    }, {
                        'indent': '+1'
                    }], ['bold', 'italic', 'underline', {
                        'size': []
                    }], [{
                        'color': []
                    }, {
                        'background': []
                    }], ['link', 'image'],]
                },
                placeholder: 'Mô tả',
                theme: 'snow'
            });

            $('#general-settings').on('submit', function () {
                $('#hidden-description').val(quill.root.innerHTML);
            });
        `}</script>
    </>)
}