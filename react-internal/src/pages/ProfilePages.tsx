import { ChangeEvent, FormEvent, useEffect, useRef, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom"
import { getProfiles, addProfile, deleteProfile, ProfileResponse, getProfileById, updateProfile } from "../services/ProfileService";
import { showSnackBar } from "../fragments/snackbar";
import { URLSearchParams } from "url";
import { ProfileModel } from "../models/ProfileModel";
import { Permission } from "../models/emums";
import { getPatientById, getPatients, PatientResponse } from "../services/PatientService";
import { PatientModel } from "../models/PatientModel";

export function ProfilesPage() {
    const location = useLocation();
    useEffect(() => {
        if (location.pathname === "/profile") {
            document.title = "Vai trò"
        }
    }, [location.pathname]);

    const [model, setModel] = useState<ProfileModel[]>();
    const [reloadState, setReloadState] = useState(false);
    function promptReload() {
        const timeout = setTimeout(function () {
            setReloadState(function (state) { return !state; });
            clearTimeout(timeout);
        }, 1000);
    }

    useEffect(() => {
        getProfiles().then(function (response) {
            setModel(response || []);
        }, function (reason) {
            setModel([]);
        })
    }, [reloadState]);

    function onDeleteItem(event: React.MouseEvent<HTMLAnchorElement, MouseEvent>) {
        event.preventDefault();
        const roleIdToDelete = $(event.currentTarget).data('profile-id');
        console.log("id: " + roleIdToDelete);
        const deleteModal = $('#deleteModal');
        deleteModal.data("idToDel", roleIdToDelete);
        (deleteModal as any).modal('show');

        $('#confirmDelete').on('click', function () {
            deleteProfile(deleteModal.data("idToDel")).then(function (response) {
                if (response) {
                    showSnackBar({ success: 'Xóa hồ sơ thành công' });
                    promptReload();
                } else showSnackBar({ error: 'Xóa hồ sơ thất bại' });
            }, function (reason) {
                showSnackBar({ error: 'Xóa hồ sơ thất bại' });
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
                            Quản lý hồ sơ
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
                                    <h4>Quản lý hồ sơ</h4>
                                </div>
                            </div>
                        </div>
                        <div className="widget-content widget-content-area">
                            <div className="layout-top-spacing ps-3 pe-3 col-12 mb-3">
                                <Link to="/profile/create" className="btn btn-primary">Thêm mới</Link>
                            </div>
                            <table id="profile-table" className="table style-3 dt-table-hover" style={{ width: "100%" }}>
                                <thead>
                                    <tr>
                                        <th style={{ width: "5%" }}>STT</th>
                                        <th>Mã hồ sơ</th>
                                        <th>Mã bệnh nhân</th>
                                        <th>Người khám</th>
                                        <th>Ngày sinh</th>
                                        <th>Giới tính</th>
                                        <th className="no-content" style={{ width: "5%" }}>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>{model && model.map(function (profile: ProfileModel, index: number) {
                                    ++index;
                                    return (<tr>
                                        <td>{index}</td>
                                        <td>{profile?.id}</td>
                                        <td>{profile?.patient}</td>
                                        <td>{profile?.full_name}</td>
                                        <td>{profile?.date_of_birth}</td>
                                        <td>{{ "M": "Nam", "F": "Nữ", "": undefined }[profile?.gender || ""] || "Khác"}</td>

                                        <td className="table-controls d-flex justify-content-center align-items-center pt-2">
                                            <li>
                                                <Link to={"/profile/edit?id=" + profile.id}
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
                                                <Link to="#" data-profile-id={profile.id}
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
        <script>{`$('#profile-table').DataTable({
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

export function CreateProfilePage() {
    const location = useLocation();
    const navigate = useNavigate();
    useEffect(() => {
        if (location.pathname === "/profile/create") {
            document.title = "Thêm mới hồ sơ"
        }
    }, [location.pathname]);

    const [patients, setPatients] = useState<PatientResponse[]>([]);
    useEffect(() => {
        getPatients().then(function (response) {
            setPatients(response || []);
        }, function (reason) {
            setPatients([]);
        });
    }, []);

    const [selectedPatient, setSelectedPatient] = useState<PatientModel>();
    function onPatientChange(event: ChangeEvent<HTMLSelectElement>) {
        let selectedValue: string | number = event.currentTarget.value;
        if (selectedValue && ((selectedValue = Number(selectedValue)) || selectedValue === 0)) {
            getPatientById(selectedValue).then(function (response) {
                setSelectedPatient(response || undefined);
            }, function (reason) {
                setSelectedPatient(undefined);
            })
        } else setSelectedPatient(undefined);
    }

    const formRef = useRef<HTMLFormElement>(null);
    useEffect(() => {
        ($ as any).validator.addMethod("date_of_birthValidation", function (this: any, value: string, element: any) {
            const methods = ($ as any).validator.methods;
            if (!methods.required.call(this, value, element)) return true;
            return new Date(value) < new Date();
        }, "Ngày sinh phải là ngày trong quá khứ.");
        formRef.current && ($(formRef.current) as any).validate({
            errorPlacement(error: any, element: any) {
                error.appendTo(element.next('.invalid-feedback'));
            },
            rules: {
                full_name: {
                    required: true
                },
                patient: {
                    required: true
                },
                date_of_birth: {
                    required: true,
                    date_of_birthValidation: true
                }
            },
            messages: {
                full_name: {
                    required: "Họ tên không được để trống."
                },
                patient: {
                    required: "Bệnh nhân chưa được chọn."
                },
                date_of_birth: {
                    required: "Ngày sinh không được bỏ trống"
                }
            },
            async submitHandler(form: HTMLFormElement, e: FormEvent) {
                e.preventDefault();
                const data = new FormData(form);
                let patient: any = data.get("patient") || undefined;
                let full_name: any = data.get("full_name") || undefined;
                let date_of_birth: any = data.get("date_of_birth") || undefined;
                let gender: any = data.get("gender") || undefined;
                if ("string" !== typeof patient || (!(patient = Number(patient)) && patient !== 0)) patient = undefined;
                if ("string" !== typeof full_name) full_name = undefined;
                if ("string" !== typeof date_of_birth) date_of_birth = undefined;
                if ("string" !== typeof gender || (gender !== "M" && gender !== "F")) gender = "D";
                addProfile({ patient, full_name, date_of_birth, gender }).then(function (response) {
                    if (!response) return;
                    showSnackBar({ success: "Thêm mới hồ sơ thành công." });
                    const timeout = setTimeout(function () {
                        clearTimeout(timeout);
                        navigate("/profile");
                    });
                }, function (reason) {
                    showSnackBar({ error: "Lỗi không thể thêm hồ sơ này." });
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
                            THÊM MỚI HỒ SƠ
                            <span className="badge badge-dark"></span>
                        </h5>
                    </div>
                </div>
                <div>
                    <iframe id="pdfFrame" style={{ display: "none" }}></iframe>
                    <Link to="/profile" className="btn">Trở lại</Link>
                </div>
            </div>

            <div className="row mb-4 layout-spacing">
                <div className="col-xxl-8 col-xl-12 col-lg-12 col-md-12 col-sm-12">
                    <div className="statbox widget box box-shadow">
                        <div className="widget-header">
                            <div className="row">
                                <div className="col-xl-12 col-md-12 col-sm-12 col-12">
                                    <h4>Thông tin hồ sơ</h4>
                                </div>
                            </div>
                        </div>

                        <form ref={formRef} id="general-settings">
                            <div className="widget-content widget-content-area blog-create-section">
                                <div className="row">
                                    <div className="col-sm-12">
                                        <div className="col-12">
                                            <div className="form-group mb-4">
                                                <label htmlFor="FullName">Tên người khám<strong className="text-danger">*</strong></label>
                                                <input type="text" id="FullName" name="full_name" className="form-control" placeholder="Tên người khám" />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>

                                            <div className="form-group mb-4">
                                                <label htmlFor="Patient">Bệnh nhân<strong className="text-danger">*</strong></label>
                                                <select id="Patient" name="patient" className="form-control" value={selectedPatient?.id || ""} onChange={onPatientChange}>
                                                    {patients && patients.map(function (patient) {
                                                        return (<option value={patient.id}>{patient.full_name}</option>);
                                                    })}
                                                    <option value="">Chọn</option>
                                                </select>
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>

                                            <div className="form-group mb-4">
                                                <label htmlFor="date_of_birth">Ngày sinh<strong className="text-danger">*</strong></label>
                                                <input type="date" id="date_of_birth" name="date_of_birth" className="form-control" placeholder="Ngày sinh" max={new Date().toISOString().split("T")[0]} />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>

                                            <div className="form-group mb-4">
                                                <label htmlFor="Gender">Giới tính</label>
                                                <select id="Gender" name="gender" className="form-control" value="D">
                                                    <option value='M'>Nam</option>
                                                    <option value='F'>Nữ</option>
                                                    <option value='D'>Khác</option>
                                                </select>
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <button type="submit" className="btn btn-primary">Hoàn tất</button>
                            </div>
                        </form>
                    </div>
                </div>

                <div className="col-xxl-4 col-xl-12 col-lg-12 col-md-12 col-sm-12 mt-xxl-0 mt-4">
                    <div className="statbox widget box box-shadow">
                        <div className="widget-header">
                            <div className="row">
                                <div className="col-xl-12 col-md-12 col-sm-12 col-12">
                                    <h4>Thông tin bệnh nhân:</h4>
                                </div>
                            </div>
                        </div>
                        <div className="widget-content widget-content-area blog-create-section" id="patient-info">
                            <div className="row">
                                <div className="col-xxl-12">
                                    <div className="d-flex">
                                        <p className="text-start me-1">Mã bệnh nhân:</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedPatient?.id || ""}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Họ tên:</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedPatient?.full_name || ""}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Email:</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedPatient?.email || ""}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Số điện thoại:</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedPatient?.phone || ""}</p>
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

export function EditProfilePage() {
    const location = useLocation();
    const params = new URLSearchParams();
    const id = Number(params.get("id") || undefined);
    const navigate = useNavigate();
    useEffect(() => {
        if (location.pathname === "/profile/edit") {
            document.title = "Chỉnh sửa hồ sơ"
        }
    }, [location.pathname]);

    const [patients, setPatients] = useState<PatientResponse[]>([]);
    useEffect(() => {
        getPatients().then(function (response) {
            setPatients(response || []);
        }, function (reason) {
            setPatients([]);
        });
    }, []);

    const [model, setModel] = useState<ProfileModel>();
    const [selectedPatient, setSelectedPatient] = useState<PatientModel>();
    useEffect(() => {
        getProfileById(id).then(function (response) {
            if (response) {
                setModel(response || undefined);
                if (response.id) {
                    getPatientById(response.id).then(function (response) {
                        setSelectedPatient(response || undefined);
                    }, function (reason) {
                        setSelectedPatient(undefined);
                    })
                }
            } else setModel(undefined);
        }, function (reason) {
            setModel(undefined);
        })
    }, []);

    function onPatientChange(event: ChangeEvent<HTMLSelectElement>) {
        let selectedValue: string | number = event.currentTarget.value;
        if (selectedValue && ((selectedValue = Number(selectedValue)) || selectedValue === 0)) {
            getPatientById(selectedValue).then(function (response) {
                setSelectedPatient(response || undefined);
            }, function (reason) {
                setSelectedPatient(undefined);
            })
        } else setSelectedPatient(undefined);
    }

    const formRef = useRef<HTMLFormElement>(null);
    useEffect(() => {
        formRef.current && ($(formRef.current) as any).validate({
            errorPlacement(error: any, element: any) {
                error.appendTo(element.next('.invalid-feedback'));
            },
            rules: {
                full_name: {
                    required: true
                },
                patient: {
                    required: true
                },
                date_of_birth: {
                    required: true,
                    date_of_birthValidation: true
                }
            },
            messages: {
                full_name: {
                    required: "Họ tên không được để trống."
                },
                patient: {
                    required: "Bệnh nhân chưa được chọn."
                },
                date_of_birth: {
                    required: "Ngày sinh không được bỏ trống"
                }
            },
            async submitHandler(form: HTMLFormElement, e: FormEvent) {
                e.preventDefault();
                const data = new FormData(form);
                let patient: any = data.get("patient") || undefined;
                let full_name: any = data.get("full_name") || undefined;
                let date_of_birth: any = data.get("date_of_birth") || undefined;
                let gender: any = data.get("gender") || undefined;
                if ("string" !== typeof patient || (!(patient = Number(patient)) && patient !== 0)) patient = undefined;
                if ("string" !== typeof full_name) full_name = undefined;
                if ("string" !== typeof date_of_birth) date_of_birth = undefined;
                if ("string" !== typeof gender || (gender !== "M" && gender !== "F")) gender = "D";
                updateProfile({ id, patient, full_name, date_of_birth, gender }).then(function (response) {
                    if (!response) return;
                    showSnackBar({ success: "Cập nhật hồ sơ thành công." });
                    const timeout = setTimeout(function () {
                        clearTimeout(timeout);
                        navigate("/profile");
                    });
                }, function (reason) {
                    showSnackBar({ error: "Lỗi không thể cập nhật hồ sơ này." });
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
                            CHỈNH SỬA HỒ SƠ
                            <span className="badge badge-dark"></span>
                        </h5>
                    </div>

                </div>
                <div>
                    <iframe id="pdfFrame" style={{ display: "none" }}></iframe>
                    <Link to="/profile" className="btn">Trở lại</Link>
                </div>
            </div>

            <div className="row mb-4 layout-spacing">
                <div className="col-xxl-8 col-xl-12 col-lg-12 col-md-12 col-sm-12">
                    <div className="statbox widget box box-shadow">
                        <div className="widget-header">
                            <div className="row">
                                <div className="col-xl-12 col-md-12 col-sm-12 col-12">
                                    <h4>Thông tin hồ sơ</h4>
                                </div>
                            </div>
                        </div>

                        <form ref={formRef} id="general-settings">
                            <div className="widget-content widget-content-area blog-create-section">
                                <div className="row">
                                    <div className="col-sm-12">
                                        <div className="col-12">
                                            <div className="form-group mb-4">
                                                <label htmlFor="FullName">Tên người khám<strong className="text-danger">*</strong></label>
                                                <input type="text" id="FullName" name="full_name" className="form-control" placeholder="Tên người khám" value={model?.full_name || ""} />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>

                                            <div className="form-group mb-4">
                                                <label htmlFor="Patient">Bệnh nhân<strong className="text-danger">*</strong></label>
                                                <select id="Patient" name="patient" className="form-control" value={selectedPatient?.id || ""} onChange={onPatientChange}>
                                                    {patients && patients.map(function (patient) {
                                                        return (<option value={patient.id}>{patient.full_name}</option>);
                                                    })}
                                                    <option value="">Chọn</option>
                                                </select>
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>

                                            <div className="form-group mb-4">
                                                <label htmlFor="date_of_birth">Ngày sinh<strong className="text-danger">*</strong></label>
                                                <input type="date" id="date_of_birth" name="date_of_birth" className="form-control" placeholder="Ngày sinh" max={new Date().toISOString().split("T")[0]} value={model?.date_of_birth || ""} />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>

                                            <div className="form-group mb-4">
                                                <label htmlFor="Gender">Giới tính</label>
                                                <select id="Gender" name="gender" className="form-control" value={model?.gender || "D"}>
                                                    <option value='M'>Nam</option>
                                                    <option value='F'>Nữ</option>
                                                    <option value='D'>Khác</option>
                                                </select>
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <button type="submit" className="btn btn-primary">Hoàn tất</button>
                            </div>
                        </form>
                    </div>
                </div>

                <div className="col-xxl-4 col-xl-12 col-lg-12 col-md-12 col-sm-12 mt-xxl-0 mt-4">
                    <div className="statbox widget box box-shadow">
                        <div className="widget-header">
                            <div className="row">
                                <div className="col-xl-12 col-md-12 col-sm-12 col-12">
                                    <h4>Thông tin bệnh nhân:</h4>
                                </div>
                            </div>
                        </div>
                        <div className="widget-content widget-content-area blog-create-section" id="patient-info">
                            <div className="row">
                                <div className="col-xxl-12">
                                    <div className="d-flex">
                                        <p className="text-start me-1">Mã bệnh nhân:</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedPatient?.id}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Họ tên:</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedPatient?.full_name}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Email:</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedPatient?.email}</p>
                                        </b>
                                    </div>
                                    <div className="d-flex">
                                        <p className="text-start me-1">Số điện thoại:</p>
                                        <b>
                                            <p className="text-dark text-bold">{selectedPatient?.phone}</p>
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