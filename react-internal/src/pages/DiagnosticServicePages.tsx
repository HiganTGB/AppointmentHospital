import { FormEvent, useEffect, useRef, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom"
import { getDiagnosticServices, addDiagnosticService, deleteDiagnosticService, DiagnosticServiceResponse, getDiagnosticServiceById, updateDiagnosticService } from "../services/DiagnosticService";
import { showSnackBar } from "../fragments/snackbar";
import { URLSearchParams } from "url";
import { DiagnosticServiceModel } from "../models/DiagnosticModel";

export function DiagnosticServicesPage() {
    const location = useLocation();
    useEffect(() => {
        if (location.pathname === "/diagnosticService") {
            document.title = "Vai trò"
        }
    }, [location.pathname]);

    const [model, setModel] = useState<DiagnosticServiceResponse[]>();
    const [reloadState, setReloadState] = useState(false);
    function promptReload() {
        const timeout = setTimeout(function () {
            setReloadState(function (state) { return !state; });
            clearTimeout(timeout);
        }, 1000);
    }

    useEffect(() => {
        getDiagnosticServices().then(function (response) {
            setModel(response || []);
        }, function (reason) {
            setModel([]);
        })
    }, [reloadState]);

    function onDeleteItem(event: React.MouseEvent<HTMLAnchorElement, MouseEvent>) {
        event.preventDefault();
        const roleIdToDelete = $(event.currentTarget).data('diagnosticservice-id');
        console.log("id: " + roleIdToDelete);
        const deleteModal = $('#deleteModal');
        deleteModal.data("idToDel", roleIdToDelete);
        (deleteModal as any).modal('show');

        $('#confirmDelete').on('click', function () {
            deleteDiagnosticService(deleteModal.data("idToDel")).then(function (response) {
                if (response) {
                    showSnackBar({ success: 'Xóa dịch vụ chuẩn đoán thành công' });
                    promptReload();
                } else showSnackBar({ error: 'Xóa dịch vụ chuẩn đoán thất bại' });
            }, function (reason) {
                showSnackBar({ error: 'Xóa dịch vụ chuẩn đoán thất bại' });
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
                            Quản lý dịch vụ chuẩn đoán
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
                                    <h4>Quản lý dịch vụ chuẩn đoán</h4>
                                </div>
                            </div>
                        </div>
                        <div className="widget-content widget-content-area">
                            <div className="layout-top-spacing ps-3 pe-3 col-12 mb-3">
                                <Link to="/diagnosticService/create" className="btn btn-primary">Thêm mới</Link>
                            </div>
                            <table id="diagnosticservice-table" className="table style-3 dt-table-hover" style={{ width: "100%" }}>
                                <thead>
                                    <tr>
                                        <th style={{ width: "5%" }}>STT</th>
                                        <th>Mã dịch vụ</th>
                                        <th>Tên</th>
                                        <th>Giá dịch vụ</th>
                                        <th className="no-content" style={{ width: "5%" }}>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>{model && model.map(function (diagnosticservice: DiagnosticServiceResponse, index: number) {
                                    ++index;
                                    return (<tr>
                                        <td>{index}</td>
                                        <td>{diagnosticservice?.id}</td>
                                        <td>{diagnosticservice?.name}</td>
                                        <td>{diagnosticservice?.price}</td>

                                        <td className="table-controls d-flex justify-content-center align-items-center pt-2">
                                            <li>
                                                <Link to={"/diagnosticService/edit?id=" + diagnosticservice.id}
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
                                                <Link to="#" data-diagnosticservice-id={diagnosticservice.id}
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
        <script>{`$('#diagnosticservice-table').DataTable({
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

export function CreateDiagnosticServicePage() {
    const location = useLocation();
    const navigate = useNavigate();
    useEffect(() => {
        if (location.pathname === "/diagnosticService/create") {
            document.title = "Thêm mới dịch vụ chuẩn đoán"
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
                },
                price: {
                    required: true
                }
            },
            messages: {
                name: {
                    required: "Tên không được trống."
                },
                price: {
                    required: "Giá không được trống."
                }
            },
            async submitHandler(form: HTMLFormElement, e: FormEvent) {
                e.preventDefault();
                const data = new FormData(form);
                let name: any = data.get("name") || undefined;
                let price: any = data.get("price") || undefined;
                if ("string" !== typeof name) name = undefined;
                if ("string" !== typeof price || (!(price = Number(price)) && price !== 0)) price = undefined;
                addDiagnosticService({ name, price }).then(function (response) {
                    if (!response) return;
                    showSnackBar({ success: "Thêm mới dịch vụ chuẩn đoán thành công." });
                    const timeout = setTimeout(function () {
                        clearTimeout(timeout);
                        navigate("/diagnosticService");
                    });
                }, function (reason) {
                    showSnackBar({ error: "Lỗi không thể thêm dịch vụ chuẩn đoán này." });
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
                            THÊM MỚI DỊCH VỤ CHUẨN ĐOÁN
                            <span className="badge badge-dark"></span>
                        </h5>
                    </div>
                </div>
                <div>
                    <iframe id="pdfFrame" style={{ display: "none" }}></iframe>
                    <Link to="/diagnosticService" className="btn">Trở lại</Link>
                </div>
            </div>

            <div className="row mb-4 layout-spacing">
                <div className="col-xxl-8 col-xl-12 col-lg-12 col-md-12 col-sm-12">
                    <div className="statbox widget box box-shadow">
                        <div className="widget-header">
                            <div className="row">
                                <div className="col-xl-12 col-md-12 col-sm-12 col-12">
                                    <h4>Thông tin dịch vụ chuẩn đoán</h4>
                                </div>
                            </div>
                        </div>

                        <form ref={formRef} id="general-settings">
                            <div className="widget-content widget-content-area blog-create-section">
                                <div className="row">
                                    <div className="col-sm-12">
                                        <div className="col-12">
                                            <div className="form-group mb-4">
                                                <label htmlFor="name">Tên dịch vụ chuẩn đoán<strong className="text-danger">*</strong></label>
                                                <input type="text" id="name" name="name" className="form-control" placeholder="Tên dịch vụ chuẩn đoán" />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>
                                            <div className="form-group mb-4">
                                                <label htmlFor="price">Giá<strong className="text-danger">*</strong></label>
                                                <input type="number" id="price" name="price" className="form-control" placeholder="Giá dịch vụ chuẩn đoán" step="0.1" min="0" />
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

export function EditDiagnosticServicePage() {
    const location = useLocation();
    const params = new URLSearchParams();
    const id = Number(params.get("id") || undefined);
    const navigate = useNavigate();
    useEffect(() => {
        if (location.pathname === "/diagnosticService/edit") {
            document.title = "Chỉnh sửa dịch vụ chuẩn đoán"
        }
    }, [location.pathname]);

    const [model, setModel] = useState<DiagnosticServiceModel>();
    useEffect(() => {
        getDiagnosticServiceById(id).then(function (response) {
            setModel(response || undefined);
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
                },
                price: {
                    required: true
                }
            },
            messages: {
                name: {
                    required: "Tên không được trống."
                },
                price: {
                    required: "Giá không được trống."
                }
            },
            async submitHandler(form: HTMLFormElement, e: FormEvent) {
                e.preventDefault();
                const data = new FormData(form);
                let name: any = data.get("name") || undefined;
                let price: any = data.get("price") || undefined;
                if ("string" !== typeof name) name = undefined;
                if ("string" !== typeof price || (!(price = Number(price)) && price !== 0)) price = undefined;
                updateDiagnosticService({ id, name, price }).then(function (response) {
                    if (!response) return;
                    showSnackBar({ success: "Thêm mới dịch vụ chuẩn đoán thành công." });
                    const timeout = setTimeout(function () {
                        clearTimeout(timeout);
                        navigate("/diagnosticService");
                    });
                }, function (reason) {
                    showSnackBar({ error: "Lỗi không thể thêm dịch vụ chuẩn đoán này." });
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
                            CHỈNH SỬA DỊCH VỤ CHUẨN ĐOÁN
                            <span className="badge badge-dark"></span>
                        </h5>
                    </div>

                </div>
                <div>
                    <iframe id="pdfFrame" style={{ display: "none" }}></iframe>
                    <Link to="/diagnosticService" className="btn">Trở lại</Link>
                </div>
            </div>

            <div className="row mb-4 layout-spacing">
                <div className="col-xxl-8 col-xl-12 col-lg-12 col-md-12 col-sm-12">
                    <div className="statbox widget box box-shadow">
                        <div className="widget-header">
                            <div className="row">
                                <div className="col-xl-12 col-md-12 col-sm-12 col-12">
                                    <h4>Thông tin dịch vụ chuẩn đoán</h4>
                                </div>
                            </div>
                        </div>

                        <form ref={formRef} id="general-settings">
                            <div className="widget-content widget-content-area blog-create-section">
                                <div className="row">
                                    <div className="col-sm-12">
                                        <div className="col-12">
                                            <div className="form-group mb-4">
                                                <label htmlFor="name">Tên dịch vụ chuẩn đoán<strong className="text-danger">*</strong></label>
                                                <input type="text" id="name" name="name" className="form-control" placeholder="Tên dịch vụ chuẩn đoán" value={model?.name || ""} />
                                                <span className="invalid-feedback" role="alert"></span>
                                            </div>
                                            <div className="form-group mb-4">
                                                <label htmlFor="price">Giá<strong className="text-danger">*</strong></label>
                                                <input type="number" id="price" name="price" className="form-control" placeholder="Giá dịch vụ chuẩn đoán" step="0.1" min="0" value={model?.price || 0} />
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