import { ChangeEvent, FormEvent, useEffect, useRef, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom"
import { getRoles, deleteRole, RoleResponse, getRoleById, createRole, RoleRequest, changePermission, getPermissions, updateRole } from "../services/RoleService";
import { showSnackBar } from "../fragments/snackbar";
import { URLSearchParams } from "url";
import { RoleModel } from "../models/RoleModel";
import { Permission } from "../models/emums";

export function RolesPage() {
    const location = useLocation();
    useEffect(() => {
        if (location.pathname === "/role") {
            document.title = "Vai trò"
        }
    }, [location.pathname]);

    const [model, setModel] = useState<RoleResponse[]>();
    const [reloadState, setReloadState] = useState(false);
    function promptReload() {
        const timeout = setTimeout(function () {
            setReloadState(function (state) { return !state; });
            clearTimeout(timeout);
        }, 1000);
    }

    useEffect(() => {
        getRoles().then(function (response) {
            setModel(response || []);
        }, function (reason) {
            setModel([]);
        })
    }, [reloadState]);

    function onDeleteItem(event: React.MouseEvent<HTMLAnchorElement, MouseEvent>) {
        event.preventDefault();
        const roleIdToDelete = $(event.currentTarget).data('role-id');
        console.log("id: " + roleIdToDelete);
        const deleteModal = $('#deleteModal');
        deleteModal.data("idToDel", roleIdToDelete);
        (deleteModal as any).modal('show');

        $('#confirmDelete').on('click', function () {
            deleteRole(deleteModal.data("idToDel")).then(function (response) {
                if (response) {
                    showSnackBar({ success: 'Xóa vai trò thành công' });
                    promptReload();
                } else showSnackBar({ error: 'Xóa vai trò thất bại' });
            }, function (reason) {
                showSnackBar({ error: 'Xóa vai trò thất bại' });
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
                        <li className="breadcrumb-item"><Link to="#">Phân quyền</Link></li>
                        <li className="breadcrumb-item active" aria-current="page">
                            Quản lý vai trò
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
                                    <h4>Quản lý vai trò</h4>
                                </div>
                            </div>
                        </div>
                        <div className="widget-content widget-content-area">
                            <div className="layout-top-spacing ps-3 pe-3 col-12 mb-3">
                                <Link to="/role/create" className="btn btn-primary">Thêm mới</Link>
                            </div>
                            <table id="role-table" className="table style-3 dt-table-hover" style={{ width: "100%" }}>
                                <thead>
                                    <tr>
                                        <th style={{ width: "5%" }}>STT</th>
                                        <th>Mã vai trò</th>
                                        <th>Tên vai trò</th>
                                        <th>Mô tả</th>
                                        <th className="no-content" style={{ width: "5%" }}>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>{model && model.map(function (role: RoleResponse, index: number) {
                                    ++index;
                                    return (<tr>
                                        <td>{index}</td>
                                        <td>{role.id}</td>
                                        <td>{role.name}</td>
                                        <td>{role.description}</td>
                                        <td className="table-controls d-flex justify-content-center align-items-center pt-2">
                                            <li>
                                                <Link to={"/role/edit?id=" + role.id}
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
                                                <Link to="#" data-role-id={role.id}
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
        <script>{`$('#role-table').DataTable({
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

export function CreateRolePage() {
    const location = useLocation();
    const navigate = useNavigate();
    useEffect(() => {
        if (location.pathname === "/role/create") {
            document.title = "Thêm mới vai trò"
        }
    }, [location.pathname]);

    const formRef = useRef<HTMLFormElement>(null);
    useEffect(() => {
        formRef.current && ($(formRef.current) as any).validate({
            errorPlacement(error: any, element: any) {
                error.appendTo(element.next('.invalid-feedback'));
            },
            rules: {
                name: {
                    required: true
                }
            },
            messages: {
                name: {
                    required: "Tên không được để trống.",
                }
            },
            async submitHandler(form: HTMLFormElement, e: FormEvent) {
                e.preventDefault();
                const data = new FormData(form);
                const request = {
                    description: data.get("description") || undefined,
                    name: data.get("name") || undefined
                }
                if ("string" !== typeof request.description) request.description = undefined;
                if ("string" !== typeof request.name) request.name = undefined;
                createRole(request as RoleRequest).then(function (response) {
                    if (!response) return;
                    showSnackBar({ success: "Thêm mới vai trò thành công." });
                    const timeout = setTimeout(function () {
                        clearTimeout(timeout);
                        navigate("/role");
                    });
                }, function (reason) {
                    showSnackBar({ error: "Lỗi không thể thêm vai trò này." });
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
                            THÊM MỚI VAI TRÒ
                            <span className="badge badge-dark"></span>
                        </h5>
                    </div>
                </div>
                <div>
                    <iframe id="pdfFrame" style={{ display: "none" }}></iframe>
                    <Link to="/role" className="btn">Trở lại</Link>
                </div>
            </div>

            <div className="row mb-4 layout-spacing">
                <div className="col-xxl-8 col-xl-12 col-lg-12 col-md-12 col-sm-12">
                    <div className="statbox widget box box-shadow">
                        <div className="widget-header">
                            <div className="row">
                                <div className="col-xl-12 col-md-12 col-sm-12 col-12">
                                    <h4>Thông tin vai trò</h4>
                                </div>
                            </div>
                        </div>

                        <form ref={formRef} id="general-settings">
                            <div className="widget-content widget-content-area blog-create-section">
                                <div className="row">
                                    <div className="col-sm-12">
                                        <div className="col-12">
                                            <div className="form-group mb-4">
                                                <label htmlFor="Name">Tên vai trò<strong className="text-danger">*</strong></label>
                                                <input type="text" id="Name" name="name" className="form-control" placeholder="Tên vai trò" />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>

                                            <div className="form-group mb-4">
                                                <label htmlFor="Description">Mô tả</label>
                                                <input type="text" id="Description" name="description" className="form-control" placeholder="Mô tả" />
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
            </div>
        </div>

        <script src="/js/customs/assets/apps/blog-create.js"></script>
        <script src="/plugins/table/datatable/datatables.js"></script>
        <script src="/plugins/table/datatable/extensions/col-reorder/col-reorder.datatables.min.js"></script>
        <script src="/plugins/table/datatable/extensions/fixed-columns/fixed-columns.datatables.min.js"></script>

        <script src="/plugins/tomSelect/tom-select.base.js"></script>
        <script src="/plugins/tomSelect/custom-tom-select.js"></script>
    </>)
}

export function EditRolePage() {
    const location = useLocation();
    const params = new URLSearchParams();
    const id = Number(params.get("id") || undefined);
    const navigate = useNavigate();
    useEffect(() => {
        if (location.pathname === "/role/edit") {
            document.title = "Chỉnh sửa vai trò"
        }
    }, [location.pathname]);

    const [model, setModel] = useState<RoleModel>();
    useEffect(() => {
        getRoleById(id).then(function (response) {
            if (response) {
                const model: RoleModel = {
                    id: response.id,
                    name: response.name,
                    description: response.description,
                    permissions: {}
                };

                getPermissions().then(function (permissions) {
                    if (permissions) {
                        permissions.reduce(function (holder, key) {
                            holder[key] = false;
                            return holder;
                        }, model.permissions);

                        getPermissions(id).then(function (granteds) {
                            if (granteds) {
                                granteds.reduce(function (holder, key) {
                                    holder[key] = true;
                                    return holder;
                                }, model.permissions);
                            }
                            setModel(model);
                        }, function (reason) {
                            setModel(model);
                        })
                    } else setModel(model);
                }, function (reason) {
                    setModel(model);
                });
            } else setModel(undefined);
        }, function (reason) {
            setModel(undefined);
        })
    }, []);

    const formRef = useRef<HTMLFormElement>(null);
    useEffect(() => {
        formRef.current && ($(formRef.current) as any).validate({
            errorPlacement(error: any, element: any) {
                error.appendTo(element.next('.invalid-feedback'));
            },
            rules: {
                name: {
                    required: true
                }
            },
            messages: {
                name: {
                    required: "Tên không được để trống.",
                }
            },
            async submitHandler(form: HTMLFormElement, e: FormEvent) {
                e.preventDefault();
                const data = new FormData(form);
                const request = {
                    description: data.get("description") || undefined,
                    name: data.get("name") || undefined
                }
                if ("string" !== typeof request.description) request.description = undefined;
                if ("string" !== typeof request.name) request.name = undefined;
                updateRole(request as RoleRequest, id).then(function (response) {
                    if (!response) return;
                    showSnackBar({ success: "Cập nhật vai trò thành công." });
                    const timeout = setTimeout(function () {
                        clearTimeout(timeout);
                        navigate("/role");
                    });
                }, function (reason) {
                    showSnackBar({ error: "Lỗi không thể cập nhật vai trò này." });
                });
            }
        });
    }, []);

    function onPermissionChange(event: ChangeEvent<HTMLInputElement>) {
        console.log("onPermissionChange entered");
        const target = event.currentTarget;
        if (!(model?.id)) {
            showSnackBar({ error: "Thay đổi quyền thất bại" })
            return;
        }
        changePermission(model.id, target.value, target.checked).then(function (response) {
            if (response) showSnackBar({ error: "Thay đổi quyền thành công" })
            else showSnackBar({ error: "Thay đổi quyền thất bại" })
        }, function (reason) {
            showSnackBar({ error: "Thay đổi quyền thất bại" })
        });
    }

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
                            CHỈNH SỬA VAI TRÒ
                            <span className="badge badge-dark"></span>
                        </h5>
                    </div>

                </div>
                <div>
                    <iframe id="pdfFrame" style={{ display: "none" }}></iframe>
                    <Link to="/role" className="btn">Trở lại</Link>
                </div>
            </div>

            <div className="row mb-4 layout-spacing">
                <div className="col-xxl-8 col-xl-12 col-lg-12 col-md-12 col-sm-12">
                    <div className="statbox widget box box-shadow">
                        <div className="widget-header">
                            <div className="row">
                                <div className="col-xl-12 col-md-12 col-sm-12 col-12">
                                    <h4>Thông tin vai trò</h4>
                                </div>
                            </div>
                        </div>

                        <form ref={formRef} id="general-settings">
                            <div className="widget-content widget-content-area blog-create-section">
                                <div className="row">
                                    <div className="col-sm-12">
                                        <div className="col-12">
                                            <div className="form-group mb-4">
                                                <label htmlFor="Name">Tên vai trò<strong className="text-danger">*</strong></label>
                                                <input type="text" id="Name" name="name" className="form-control" placeholder="Tên vai trò" value={model?.name} />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>

                                            <div className="form-group mb-4">
                                                <label htmlFor="Description">Mô tả</label>
                                                <input type="text" id="Description" name="description" className="form-control" placeholder="Mô tả" value={model?.description} />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>

                                            <div className="form-group mb-4">
                                                <label>Danh sách quyền</label>
                                                <div className="form-control">
                                                    {model && Object.entries(model.permissions).map(function ([key, value]) {
                                                        return (<div className="form-check">
                                                            <input type="checkbox" id={key} value={key} onChange={onPermissionChange} checked={value} />
                                                            <label htmlFor={key}>{(Permission as any)[key]?.displayName || "(Quyền không xác định)"}</label>
                                                        </div>)
                                                    })}
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <button type="submit" className="btn btn-primary">Hoàn tất</button>
                            </div>
                        </form>
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
    </>)
}