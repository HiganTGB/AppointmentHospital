import { FormEvent, useEffect, useRef, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom"
import { getDoctors, addDoctor, deleteDoctor, getDoctorById, updateDoctor } from "../services/DoctorService";
import { showSnackBar } from "../fragments/snackbar";
import { URLSearchParams } from "url";
import { DoctorModel } from "../models/DoctorModel";
import { getRoles, RoleResponse } from "../services/RoleService";

export function DoctorsPage() {
    const location = useLocation();
    useEffect(() => {
        if (location.pathname === "/doctor") {
            document.title = "Vai trò"
        }
    }, [location.pathname]);

    const [model, setModel] = useState<DoctorModel[]>();
    
    const [reloadState, setReloadState] = useState(false);
    function promptReload() {
        const timeout = setTimeout(function () {
            setReloadState(function (state) { return !state; });
            clearTimeout(timeout);
        }, 1000);
    }

    useEffect(() => {
        getDoctors().then(function (response) {
            setModel(response || []);
        }, function (reason) {
            setModel([]);
        })
    }, [reloadState]);

    function onDeleteItem(event: React.MouseEvent<HTMLAnchorElement, MouseEvent>) {
        event.preventDefault();
        const roleIdToDelete = $(event.currentTarget).data('doctor-id');
        console.log("id: " + roleIdToDelete);
        const deleteModal = $('#deleteModal');
        deleteModal.data("idToDel", roleIdToDelete);
        (deleteModal as any).modal('show');

        $('#confirmDelete').on('click', function () {
            deleteDoctor(deleteModal.data("idToDel")).then(function (response) {
                if (response) {
                    showSnackBar({ success: 'Xóa bác sĩ thành công' });
                    promptReload();
                } else showSnackBar({ error: 'Xóa bác sĩ thất bại' });
            }, function (reason) {
                showSnackBar({ error: 'Xóa bác sĩ thất bại' });
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
                            Quản lý bác sĩ
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
                                    <h4>Quản lý bác sĩ</h4>
                                </div>
                            </div>
                        </div>
                        <div className="widget-content widget-content-area">
                            <div className="layout-top-spacing ps-3 pe-3 col-12 mb-3">
                                <Link to="/doctor/create" className="btn btn-primary">Thêm mới</Link>
                            </div>
                            <table id="doctor-table" className="table style-3 dt-table-hover" style={{ width: "100%" }}>
                                <thead>
                                    <tr>
                                        <th style={{ width: "5%" }}>STT</th>
                                        <th>Mã bác sĩ</th>
                                        <th>Họ tên</th>
                                        <th>Email</th>
                                        <th>Số điện thoại</th>
                                        <th>Vị trí</th>
                                        <th className="no-content" style={{ width: "5%" }}>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>{model && model.map(function (doctor: DoctorModel, index: number) {
                                    ++index;
                                    return (<tr>
                                        <td>{index}</td>
                                        <td>{doctor?.id}</td>
                                        <td>{doctor?.full_name}</td>
                                        <td>{doctor?.email}</td>
                                        <td>{doctor?.phone}</td>
                                        <td>{doctor?.position}</td>

                                        <td className="table-controls d-flex justify-content-center align-items-center pt-2">
                                            <li>
                                                <Link to={"/doctor/edit?id=" + doctor.id}
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
                                                <Link to="#" data-doctor-id={doctor.id}
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
        <script>{`$('#doctor-table').DataTable({
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

export function CreateDoctorPage() {
    const location = useLocation();
    const navigate = useNavigate();
    useEffect(() => {
        if (location.pathname === "/doctor/create") {
            document.title = "Thêm mới bác sĩ"
        }
    }, [location.pathname]);

    const formRef = useRef<HTMLFormElement>(null);
    useEffect(() => {
        ($ as any).validator.addMethod("emailValidation", function (this: any, value: string, element: any) {
            const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            const methods = ($ as any).validator.methods;
            if (!methods.required.call(this, value, element)) return true;

            if (!methods.minlength.call(this, value, element)
                && !methods.maxlength.call(this, value, element)
            ) return true;

            return emailPattern.test(value);
        }, "Email không hợp lệ.");

        ($ as any).validator.addMethod("usernameValidation", function (this: any, value: string, element: any) {
            const usernamePattern = /^[a-zA-Z][0-9a-zA-Z_-]{5,49}$/;
            const phonePattern = /^[0-9]{10,15}$/;

            const methods = ($ as any).validator.methods;
            if (!methods.required.call(this, value, element)
                && !methods.minlength.call(this, value, element)
                && !methods.maxlength.call(this, value, element)
            ) return true;

            return usernamePattern.test(value) || phonePattern.test(value);
        }, "Tên đăng nhập hoặc số điện thoại không hợp lệ.");

        ($ as any).validator.addMethod("passwordValidation", function (this: any, value: string, element: any) {
            const methods = ($ as any).validator.methods;
            if (!methods.required.call(this, value, element)
                && !methods.minlength.call(this, value, element)
                && !methods.maxlength.call(this, value, element)
            ) return true;

            let l = false, u = false, d = false;
            console.error(this, value, element);
            for (const c of value) {
                const v = c.toString();
                if (/^[0-9]$/.test(v)) {
                    console.error(v, "Digit");
                    if (l && u) return true;
                    d = true;
                } else if (v === v.toLowerCase()) {
                    console.error(v, "LowerCase");
                    if (u && d) return true;
                    l = true;
                } else if (v === v.toUpperCase()) {
                    console.error(v, "UpperCase");
                    if (l && d) return true;
                    u = true;
                }
            }
            return false;
        }, "Mật khẩu phải có ít nhất một ký tự hoa, thường, số.");

        formRef.current && ($(formRef.current) as any).validate({
            errorPlacement(error: any, element: any) {
                error.appendTo(element.next('.invalid-feedback'));
            },
            rules: {
                full_name: {
                    required: true
                },
                email: {
                    emailValidation: true
                },
                username: {
                    required: true,
                    usernameValidation: true
                },
                password: {
                    required: true,
                    passwordValidation: true
                }
            },
            messages: {
                full_name: {
                    required: "Họ tên không được để trống."
                },
                username: {
                    required: "Username không được để trống."
                },
                password: {
                    required: "Mật khẩu không được để trống."
                }
            },
            async submitHandler(form: HTMLFormElement, e: FormEvent) {
                e.preventDefault();
                const data = new FormData(form);
                let full_name: any = data.get("full_name") || undefined;
                let email: any = data.get("email") || undefined;
                let username: any = data.get("username") || undefined;
                let password: any = data.get("password") || undefined;
                let position: any = data.get("position") || undefined;
                let certificate: any = data.get("certificate") || undefined;
                let phone: any = data.get("phone") || undefined;

                if ("string" !== typeof full_name) full_name = undefined;
                if ("string" !== typeof email) email = undefined;
                if ("string" !== typeof username) username = undefined;
                if ("string" !== typeof password) password = undefined;
                if ("string" !== typeof position) position = undefined;
                if ("string" !== typeof certificate) certificate = undefined;
                if ("string" !== typeof phone) phone = undefined;
                addDoctor({ full_name, email, username, password, position, certificate, phone } as DoctorModel).then(function (response) {
                    if (!response) return;
                    showSnackBar({ success: "Thêm mới bác sĩ thành công." });
                    const timeout = setTimeout(function () {
                        clearTimeout(timeout);
                        navigate("/doctor");
                    });
                }, function (reason) {
                    showSnackBar({ error: "Lỗi không thể thêm bác sĩ này." });
                });
            }
        });
    }, []);

    return (
        <div>
            {/* BREADCRUMB */}
            <div className="page-meta">
                <nav className="breadcrumb-style-one" aria-label="breadcrumb">
                    <ol className="breadcrumb">
                        <li className="breadcrumb-item"><Link to="#">Bác sĩ</Link></li>
                        <li className="breadcrumb-item" aria-current="page"><Link to="/doctor">Quản lý bác sĩ</Link></li>
                        <li className="breadcrumb-item active" aria-current="page">Thêm mới bác sĩ</li>
                    </ol>
                </nav>
            </div>
            {/* /BREADCRUMB */}

            <div className="layout-top-spacing col-12">
                <Link to="/doctor" className="btn">Trở lại</Link>
            </div>

            <div className="row layout-top-spacing">
                <div id="user-management" className="col-lg-12 layout-spacing">
                    <div className="statbox widget box box-shadow">
                        <div className="widget-header">
                            <div className="row">
                                <div className="col-xl-12 col-md-12 col-sm-12 col-12">
                                    <h4>Thêm mới</h4>
                                </div>
                            </div>
                        </div>
                        <div className="widget-content widget-content-area">
                            <div className="col-12">
                                <form asp-action="Create" method="POST" id="general-settings">

                                    <div className="row">
                                        <div className="col-md-6">
                                            <div className="form-group mb-4">
                                                <label htmlFor="FullName">Tên bác sĩ <strong className="text-danger">*</strong></label>
                                                <input type="text" id="FullName" name="full_name" className="form-control" placeholder="Tên bác sĩ" />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>
                                        </div>
                                        <div className="col-md-6">
                                            <div className="form-group mb-4">
                                                <label htmlFor="email">Email <strong className="text-danger">*</strong></label>
                                                <input type="text" id="email" name="email" className="form-control" placeholder="Email" />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>
                                        </div>
                                    </div>

                                    <div className="row">
                                        <div className="col-md-6">
                                            <div className="form-group mb-4">
                                                <label htmlFor="UserName">Username <strong className="text-danger">*</strong></label>
                                                <input type="text" id="UserName" name="username" className="form-control" placeholder="Username" />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>
                                        </div>
                                        <div className="col-md-6">
                                            <div className="form-group mb-4">
                                                <label htmlFor="Password">Mật khẩu <strong className="text-danger">*</strong></label>
                                                <input type="password" id="Password" name="password" className="form-control" placeholder="Mật khẩu" />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <div className="row">
                                        <div className="col-md-6">
                                            <div className="form-group mb-4">
                                                <label htmlFor="position">Vị trí <strong className="text-danger">*</strong></label>
                                                <input type="text" id="position" name="position" className="form-control" placeholder="Vị trí công việc" />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>
                                        </div>
                                        <div className="col-md-6">
                                            <div className="form-group mb-4">
                                                <label htmlFor="certficate">Chứng chỉ <strong className="text-danger">*</strong></label>
                                                <input type="text" id="certificate" name="certificate" className="form-control" placeholder="Chứng chỉ công việc" />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>
                                        </div>
                                    </div>

                                    <div className="form-group mb-4">
                                        <label htmlFor="phone">Số điện thoại <strong className="text-danger">*</strong></label>
                                        <input type="text" id="phone" name="phone" className="form-control" placeholder="Số điện thoại" />
                                        <span className="invalid-feedback" role="alert"></span>
                                    </div>

                                    <button type="submit" className="btn btn-primary">Hoàn tất</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export function EditDoctorPage() {
    const location = useLocation();
    const params = new URLSearchParams();
    const id = Number(params.get("id") || undefined);
    const navigate = useNavigate();
    useEffect(() => {
        if (location.pathname === "/doctor/edit") {
            document.title = "Chỉnh sửa bác sĩ"
        }
    }, [location.pathname]);

    const [roles, setRoles] = useState<RoleResponse[]>([]);
    useEffect(() => {
        getRoles().then(function (response) {
            setRoles(response || []);
        }, function (reason) {
            setRoles([]);
        });
    }, []);

    const [model, setModel] = useState<DoctorModel>();
    useEffect(() => {
        getDoctorById(id).then(function (response) {
            setModel(response || undefined);
        }, function (reason) {
            setModel(undefined);
        })
    }, []);

    const formRef = useRef<HTMLFormElement>(null);
    useEffect(() => {
        ($ as any).validator.addMethod("emailValidation", function (this: any, value: string, element: any) {
            const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            const methods = ($ as any).validator.methods;
            if (!methods.required.call(this, value, element)) return true;

            if (!methods.minlength.call(this, value, element)
                && !methods.maxlength.call(this, value, element)
            ) return true;

            return emailPattern.test(value);
        }, "Email không hợp lệ.");

        ($ as any).validator.addMethod("usernameValidation", function (this: any, value: string, element: any) {
            const usernamePattern = /^[a-zA-Z][0-9a-zA-Z_-]{5,49}$/;
            const phonePattern = /^[0-9]{10,15}$/;

            const methods = ($ as any).validator.methods;
            if (!methods.required.call(this, value, element)
                && !methods.minlength.call(this, value, element)
                && !methods.maxlength.call(this, value, element)
            ) return true;

            return usernamePattern.test(value) || phonePattern.test(value);
        }, "Tên đăng nhập hoặc số điện thoại không hợp lệ.");

        ($ as any).validator.addMethod("passwordValidation", function (this: any, value: string, element: any) {
            const methods = ($ as any).validator.methods;
            if (!methods.required.call(this, value, element)
                && !methods.minlength.call(this, value, element)
                && !methods.maxlength.call(this, value, element)
            ) return true;

            let l = false, u = false, d = false;
            console.error(this, value, element);
            for (const c of value) {
                const v = c.toString();
                if (/^[0-9]$/.test(v)) {
                    console.error(v, "Digit");
                    if (l && u) return true;
                    d = true;
                } else if (v === v.toLowerCase()) {
                    console.error(v, "LowerCase");
                    if (u && d) return true;
                    l = true;
                } else if (v === v.toUpperCase()) {
                    console.error(v, "UpperCase");
                    if (l && d) return true;
                    u = true;
                }
            }
            return false;
        }, "Mật khẩu phải có ít nhất một ký tự hoa, thường, số.");

        formRef.current && ($(formRef.current) as any).validate({
            errorPlacement(error: any, element: any) {
                error.appendTo(element.next('.invalid-feedback'));
            },
            rules: {
                full_name: {
                    required: true
                },
                email: {
                    emailValidation: true
                },
                username: {
                    required: true,
                    usernameValidation: true
                },
                password: {
                    passwordValidation: true
                }
            },
            messages: {
                full_name: {
                    required: "Họ tên không được để trống."
                },
                username: {
                    required: "Username không được để trống."
                }
            },
            async submitHandler(form: HTMLFormElement, e: FormEvent) {
                e.preventDefault();
                const data = new FormData(form);
                let full_name: any = data.get("full_name") || undefined;
                let email: any = data.get("email") || undefined;
                let username: any = data.get("username") || undefined;
                let password: any = data.get("password") || undefined;
                let position: any = data.get("position") || undefined;
                let certificate: any = data.get("certificate") || undefined;
                let phone: any = data.get("phone") || undefined;

                if ("string" !== typeof full_name) full_name = undefined;
                if ("string" !== typeof email) email = undefined;
                if ("string" !== typeof username) username = undefined;
                if ("string" !== typeof password) password = undefined;
                if ("string" !== typeof position) position = undefined;
                if ("string" !== typeof certificate) certificate = undefined;
                if ("string" !== typeof phone) phone = undefined;
                updateDoctor({ id, full_name, email, username, password, position, certificate, phone } as DoctorModel).then(function (response) {
                    if (!response) return;
                    showSnackBar({ success: "Thêm mới bác sĩ thành công." });
                    const timeout = setTimeout(function () {
                        clearTimeout(timeout);
                        navigate("/doctor");
                    });
                }, function (reason) {
                    showSnackBar({ error: "Lỗi không thể thêm bác sĩ này." });
                });
            }
        });
    }, []);

    return (
        <div>
            {/* BREADCRUMB */}
            <div className="page-meta">
                <nav className="breadcrumb-style-one" aria-label="breadcrumb">
                    <ol className="breadcrumb">
                        <li className="breadcrumb-item"><Link to="#">Người dùng</Link></li>
                        <li className="breadcrumb-item" aria-current="page"><Link to="/doctor">Quản lý bác sĩ</Link></li>
                        <li className="breadcrumb-item active" aria-current="page">Chỉnh sửa bác sĩ</li>
                    </ol>
                </nav>
            </div>
            {/* /BREADCRUMB */}
        
            <div className="layout-top-spacing col-12">
                <Link to="/doctor" className="btn">
                    Trở lại
                </Link>
            </div>
        
            <div className="row layout-top-spacing">
                <div id="user-management" className="col-lg-12 layout-spacing">
                    <div className="statbox widget box box-shadow">
                        <div className="widget-header">
                            <div className="row">
                                <div className="col-xl-12 col-md-12 col-sm-12 col-12">
                                    <h4>Chỉnh sửa</h4>
                                </div>
                            </div>
                        </div>
                        <div className="widget-content widget-content-area">
                            <div className="col-12">
                                <form ref={formRef} id="general-settings">
                                    <div className="row">
                                        <div className="col-md-6">
                                            <div className="form-group mb-4">
                                                <label htmlFor="FullName">Tên bác sĩ <strong className="text-danger">*</strong></label>
                                                <input type="text" id="FullName" name="full_name" className="form-control" placeholder="Tên bác sĩ" value={model?.full_name || ""} />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>
                                        </div>
                                        <div className="col-md-6">
                                            <div className="form-group mb-4">
                                                <label htmlFor="email">Email <strong className="text-danger">*</strong></label>
                                                <input type="text" id="email" name="email" className="form-control" placeholder="Email" value={model?.email || ""} />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>
                                        </div>
                                    </div>
        
                                    <div className="row">
        
                                        <div className="col-md-6">
                                            <div className="form-group mb-4">
                                                <label htmlFor="UserName">Username <strong className="text-danger">*</strong></label>
                                                <input type="text" id="UserName" name="userName" className="form-control" placeholder="Username" value={model?.username || ""} />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>
                                        </div>
                                        <div className="col-md-6">
                                            <div className="form-group mb-4">
                                                <label htmlFor="Password">Mật khẩu <strong className="text-danger">*</strong></label>
                                                <input type="password" id="Password" name="password" className="form-control" placeholder="Mật khẩu" value={model?.password || ""} />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>
                                        </div>
                                    </div>
        
                                    <div className="row">
                                        <div className="col-md-6">
                                            <div className="form-group mb-4">
                                                <label htmlFor="position">Vị trí <strong className="text-danger">*</strong></label>
                                                <input type="text" id="position" name="position" className="form-control" placeholder="Vị trí công việc" value={model?.position || ""} />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>
                                        </div>
                                        <div className="col-md-6">
                                            <div className="form-group mb-4">
                                                <label htmlFor="certficate">Chứng chỉ <strong className="text-danger">*</strong></label>
                                                <input type="text" id="certificate" name="certificate" className="form-control" placeholder="Chứng chỉ công việc" value={model?.certificate || ""} />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>
                                        </div>
                                    </div>
        
                                    <div className="row">
                                        <div className="col-md-6">
                                            <div className="form-group mb-4">
                                                <label htmlFor="phone">Số điện thoại <strong className="text-danger">*</strong></label>
                                                <input type="text" id="phone" name="phone" className="form-control" placeholder="Số điện thoại" value={model?.phone || ""} />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>
                                        </div>
                                        <div className="col-md-6">
                                            <div className="form-group mb-4">
                                                <label htmlFor="RoleId">Vai trò <strong className="text-danger">*</strong></label>
                                                <select className="form-control" name="role" value={model?.role || ""}>
                                                    {roles && roles.map(function(value) {
                                                        return (<option value={value.id || ""}>{value.name || ""}</option>)
                                                    })}
                                                </select>
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>
                                        </div>
                                    </div>

                                    <button type="submit" className="btn btn-primary">Hoàn tất</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}