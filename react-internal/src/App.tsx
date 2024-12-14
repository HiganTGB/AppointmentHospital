import React, { MouseEvent, useEffect, useState } from "react";
import { BrowserRouter, Link, Route, Routes, useLocation, useNavigate } from "react-router-dom";

import "./App.css";
import { UserLoginModel } from "./models/UserLoginModel";
import { CreateRolePage, EditRolePage, RolesPage } from "./pages/RolesPages";
import { CreateProfilePage, EditProfilePage, ProfilesPage } from "./pages/ProfilePages";
import { CreateDoctorPage, DoctorsPage, EditDoctorPage } from "./pages/DoctorPages";
import { CreatePatientPage, EditPatientPage, PatientsPage } from "./pages/PatientPages";
import { CreateDiagnosticServicePage, DiagnosticServicesPage, EditDiagnosticServicePage } from "./pages/DiagnosticServicePages";
import { AppointmentsPage, CreateAppointmentPage, EditAppointmentPage } from "./pages/AppointmentPages";
import { DashboardPage, LoginPage } from "./pages/DashboardPages";
import { setAuthToken, setSession } from "./services/HttpApiService";

function AppRoutes() {
  return (
    <Routes>
      <Route path="/appointment" element={<AppointmentsPage />} />
      <Route path="/appointment/create" element={<CreateAppointmentPage />} />
      <Route path="/appointment/edit" element={<EditAppointmentPage />} />
      <Route path="/dashboard" element={<DashboardPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/diagnosticService" element={<DiagnosticServicesPage />} />
      <Route path="/diagnosticService/create" element={<CreateDiagnosticServicePage />} />
      <Route path="/diagnosticService/edit" element={<EditDiagnosticServicePage />} />
      <Route path="/doctor" element={<DoctorsPage />} />
      <Route path="/doctor/create" element={<CreateDoctorPage />} />
      <Route path="/doctor/edit" element={<EditDoctorPage />} />

      <Route path="/patient" element={<PatientsPage />} />
      <Route path="/patient/create" element={<CreatePatientPage />} />
      <Route path="/patient/edit" element={<EditPatientPage />} />
      <Route path="/profile" element={<ProfilesPage />} />
      <Route path="/profile/create" element={<CreateProfilePage />} />
      <Route path="/profile/edit" element={<EditProfilePage />} />
      <Route path="/role" element={<RolesPage />} />
      <Route path="/role/create" element={<CreateRolePage />} />
      <Route path="/role/edit" element={<EditRolePage />} />
    </Routes>
  )
}

function AppContent() {
  const location = useLocation();
  const navigate = useNavigate();
  const [sessionUser, setSessionUser] = useState<UserLoginModel>()
  useEffect(() => {
    const userJson = window.sessionStorage.getItem("user");
    if (userJson && userJson.length) setSessionUser(JSON.parse(userJson));
  }, []);

  function onLogout(event: MouseEvent<HTMLAnchorElement, MouseEvent>) {
    setAuthToken();
    setSession("user");
  }

  return (<>
    {/* BEGIN LOADER */}
    <div id="load_screen">
      <div className="loader">
        <div className="loader-content">
          <div className="spinner-grow align-self-center"></div>
        </div>
      </div>
    </div>
    {/* END LOADER */}
    {/* BEGIN NAVBAR */}
    <div className="header-container container-xxl">
      <header className="header navbar navbar-expand-sm expand-header">
        <a href="javascript:void(0);" className="sidebarCollapse">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
            stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
            className="feather feather-menu">
            <line x1="3" y1="12" x2="21" y2="12"></line>
            <line x1="3" y1="6" x2="21" y2="6"></line>
            <line x1="3" y1="18" x2="21" y2="18"></line>
          </svg>
        </a>

        <div className="search-animated toggle-search">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
            stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
            className="feather feather-search">
            <circle cx="11" cy="11" r="8"></circle>
            <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
          </svg>
          <form className="form-inline search-full form-inline search" role="search">
            <div className="search-bar">
              <span style={{ fontSize: "20px" }}>Appointment schedule</span>
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                className="feather feather-x search-close">
                <line x1="18" y1="6" x2="6" y2="18"></line>
                <line x1="6" y1="6" x2="18" y2="18"></line>
              </svg>
            </div>
          </form>
        </div>

        <ul className="navbar-item flex-row ms-lg-auto ms-0">

          <li className="nav-item theme-toggle-item">
            <a href="javascript:void(0);" className="nav-link theme-toggle">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                className="feather feather-moon dark-mode">
                <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"></path>
              </svg>
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                className="feather feather-sun light-mode">
                <circle cx="12" cy="12" r="5"></circle>
                <line x1="12" y1="1" x2="12" y2="3"></line>
                <line x1="12" y1="21" x2="12" y2="23"></line>
                <line x1="4.22" y1="4.22" x2="5.64" y2="5.64"></line>
                <line x1="18.36" y1="18.36" x2="19.78" y2="19.78"></line>
                <line x1="1" y1="12" x2="3" y2="12"></line>
                <line x1="21" y1="12" x2="23" y2="12"></line>
                <line x1="4.22" y1="19.78" x2="5.64" y2="18.36"></line>
                <line x1="18.36" y1="5.64" x2="19.78" y2="4.22"></line>
              </svg>
            </a>
          </li>

          <li className="nav-item dropdown user-profile-dropdown  order-lg-0 order-1">
            <a href="javascript:void(0);" className="nav-link dropdown-toggle user" id="userProfileDropdown"
              data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              <div className="avatar-container">
                <div className="avatar avatar-sm avatar-indicators avatar-online">
                  <img alt="avatar" src="/images/profile-30.png" className="rounded-circle" />
                </div>
              </div>
            </a>

            <div className="dropdown-menu position-absolute" style={{ width: "max-content" }} aria-labelledby="userProfileDropdown">
              <div className="user-profile-section">
                <div className="media mx-auto">
                  <div className="emoji me-2">&#x1F44B;</div>
                  <div className="media-body" style={{ width: "fit-content" }}>
                    {sessionUser ? (<>
                      <h5>{sessionUser.full_name}</h5>
                      <p>{sessionUser.id}</p>
                    </>) : (<>
                      <h6 className="">Administration</h6>
                      <p className="">Quản trị viên</p>
                    </>)}
                  </div>
                </div>
              </div>

              <div className="dropdown-item">
                <Link to="#" onClick={onLogout}>
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                    stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                    className="feather feather-log-out">
                    <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path>
                    <polyline points="16 17 21 12 16 7"></polyline>
                    <line x1="21" y1="12" x2="9" y2="12"></line>
                  </svg>
                  <span>Đăng xuất</span>
                </Link>
              </div>
            </div>

          </li>
        </ul>
      </header>
    </div>

    {/* END NAVBAR */}
    {/* BEGIN MAIN CONTAINER */}
    <div className="main-container" id="container">
      <div className="overlay"></div>
      <div className="search-overlay"></div>

      {/* BEGIN SIDEBAR */}
      <div className="sidebar-wrapper sidebar-theme">

        <nav id="sidebar">

          <div className="navbar-nav theme-brand flex-row text-center">
            <div className="nav-logo">

              <div className="nav-item theme-text">
                <a href="javascript:void(0);" className="nav-link" title="Appointment-Schedule">APPOINTMENT</a>
              </div>
            </div>
            <div className="nav-item sidebar-toggle">
              <div className="btn-toggle sidebarCollapse">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                  viewBox="0 0 24 24" fill="none" stroke="currentColor"
                  stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                  className="feather feather-chevrons-left">
                  <polyline points="11 17 6 12 11 7"></polyline>
                  <polyline points="18 17 13 12 18 7"></polyline>
                </svg>
              </div>
            </div>
          </div>

          <div className="profile-info">
            <div className="user-info">
              <div className="profile-img">
                <img src="/images/profile-30.png" alt="avatar" />
              </div>
              <div className="profile-content">
                {sessionUser ? (<>
                  <h6 className="user-name" title={sessionUser.full_name}>{sessionUser.full_name}</h6>
                  <p className="user-role" title={sessionUser.role}>{sessionUser.role}</p>
                </>) : (<>
                  <h6 className="">Administration</h6>
                  <p className="">Quản trị viên</p>
                </>)}
              </div>
            </div>
          </div>

          <ul className="list-unstyled menu-categories" id="accordionExample">
            <li className={location.pathname.startsWith("/dashboard") ? "menu activate" : "menu"}>
              <Link to="/dashboard" className="dropdown-toggle">
                <div>
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                    viewBox="0 0 24 24" fill="none" stroke="currentColor"
                    stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                    className="feather feather-home">
                    <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path>
                    <polyline points="9 22 9 12 15 12 15 22"></polyline>
                  </svg>
                  <span>Dashboard</span>
                </div>
              </Link>
            </li>

            <li className="menu menu-heading">
              <div className="heading">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                  viewBox="0 0 24 24" fill="none" stroke="currentColor"
                  stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                  className="feather feather-minus">
                  <line x1="5" y1="12" x2="19" y2="12"></line>
                </svg>
                <span>HỆ THỐNG</span>
              </div>
            </li>

            <li className={location.pathname.startsWith("/doctor") ? "menu activate" : "menu"}>
              <Link to="/doctor" className="dropdown-toggle">
                <div>
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                    viewBox="0 0 24 24" fill="none" stroke="currentColor"
                    stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                    className="feather feather-user">
                    <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                    <circle cx="12" cy="7" r="4"></circle>
                  </svg>
                  <span>Bác sĩ</span>
                </div>
              </Link>
            </li>

            <li className={location.pathname.startsWith("/patient") ? "menu activate" : "menu"}>
              <Link to="/patient" className="dropdown-toggle">
                <div>
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                    viewBox="0 0 24 24" fill="none" stroke="currentColor"
                    stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                    className="feather feather-user">
                    <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                    <circle cx="12" cy="7" r="4"></circle>
                  </svg>
                  <span>Bệnh nhân</span>
                </div>
              </Link>
            </li>

            <li className={location.pathname.startsWith("/profile") ? "menu activate" : "menu"}>
              <Link to="/profile" className="dropdown-toggle">
                <div>
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                    viewBox="0 0 24 24" fill="none" stroke="currentColor"
                    stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                    className="feather feather-user">
                    <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                    <circle cx="12" cy="7" r="4"></circle>
                  </svg>
                  <span>Hồ sơ</span>
                </div>
              </Link>
            </li>

            <li className="menu menu-heading">
              <div className="heading">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                  viewBox="0 0 24 24" fill="none" stroke="currentColor"
                  stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                  className="feather feather-minus">
                  <line x1="5" y1="12" x2="19" y2="12"></line>
                </svg>
                <span>DỊCH VỤ</span>
              </div>
            </li>
            <li className={location.pathname.startsWith("/diagnosticService") ? "menu activate" : "menu"}>
              <Link to="/diagnosticService" className="dropdown-toggle">
                <div>
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                    stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                    className="feather feather-watch">
                    <circle cx="12" cy="12" r="7"></circle>
                    <polyline points="12 9 12 12 13.5 13.5"></polyline>
                    <path d="M16.51 17.35l-.35 3.83a2 2 0 0 1-2 1.82H9.83a2 2 0 0 1-2-1.82l-.35-3.83m.01-10.7l.35-3.83A2 2 0 0 1 9.83 1h4.35a2 2 0 0 1 2 1.82l.35 3.83"></path>

                    {/* <path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z"></path>
							<line x1="7" y1="7" x2="7.01" y2="7"></line> */}
                  </svg>
                  <span>Dịch vụ chuẩn đoán</span>
                </div>
              </Link>
            </li>

            <li className={location.pathname.startsWith("/examination") ? "menu activate" : "menu"}>
              <Link to="/examination" className="dropdown-toggle">
                <div>
                  <svg xmlns="http://www.w3.org/2000/svg" width="24"
                    height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                    stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                    className="feather feather-truck"><rect x="1" y="3" width="15" height="13"></rect>
                    <polygon points="16 8 20 8 23 11 23 16 16 16 16 8"></polygon>
                    <circle cx="5.5" cy="18.5" r="2.5"></circle><circle cx="18.5" cy="18.5" r="2.5"></circle>
                  </svg>
                  <span>Khám bệnh</span>
                </div>
              </Link>
            </li>

            <li className="menu menu-heading">
              <div className="heading">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                  viewBox="0 0 24 24" fill="none" stroke="currentColor"
                  stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                  className="feather feather-minus">
                  <line x1="5" y1="12" x2="19" y2="12"></line>
                </svg>
                <span>ĐƠN ĐẶT</span>
              </div>
            </li>

            <li className={location.pathname.startsWith("/appointment") ? "menu activate" : "menu"}>
              <Link to="/appointment" className="dropdown-toggle">
                <div>
                  <svg xmlns="http://www.w3.org/2000/svg"
                    width="24" height="24" viewBox="0 0 24 24"
                    fill="none" stroke="currentColor" stroke-width="2"
                    stroke-linecap="round" stroke-linejoin="round"
                    className="feather feather-tag">
                    <path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z"></path>
                    <line x1="7" y1="7" x2="7.01" y2="7"></line>
                  </svg>
                  <span>Đặt lịch</span>
                </div>
              </Link>
            </li>

            <li className="menu menu-heading">
              <div className="heading">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                  viewBox="0 0 24 24" fill="none" stroke="currentColor"
                  stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                  className="feather feather-minus">
                  <line x1="5" y1="12" x2="19" y2="12"></line>
                </svg>
                <span>PHÂN QUYỀN</span>
              </div>
            </li>

            <li className={location.pathname.startsWith("/role") ? "menu activate" : "menu"}>
              <Link to="/role" className="dropdown-toggle">
                <div>
                  <svg xmlns="http://www.w3.org/2000/svg"
                    width="24" height="24" viewBox="0 0 24 24"
                    fill="none" stroke="currentColor" stroke-width="2"
                    stroke-linecap="round" stroke-linejoin="round"
                    className="feather feather-tag">
                    <path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z"></path>
                    <line x1="7" y1="7" x2="7.01" y2="7"></line>
                  </svg>
                  <span>Vai trò</span>
                </div>
              </Link>
            </li>

          </ul>

        </nav>

      </div>
      {/* END SIDEBAR */}
      {/* BEGIN CONTENT AREA */}
      <div id="content" className="main-content">
        <noscript>You need to enable JavaScript to run this app.</noscript>
        <div id="root" className="layout-px-spacing">
          {/* Body Content Section */}
          <AppRoutes />
        </div>

        {/*  BEGIN FOOTER  */}
        <div className="footer-wrapper">
          <div className="footer-section f-section-1">
            <p className="">
              Copyright © <span className="dynamic-year">2022</span>
              <a target="_blank" href="https://designreset.com/cork-admin/">DesignReset</a>,
              All rights reserved.
            </p>
          </div>
          <div className="footer-section f-section-2">
            <p className="">
              Coded with
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                className="feather feather-heart">
                <path
                  d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z">
                </path>
              </svg>
            </p>
          </div>
        </div>
        {/*  END FOOTER  */}
      </div>
      {/* END CONTENT AREA */}
    </div>
    {/* END MAIN CONTAINER */}
    {/* MODAL DELETE */}
    <div className="modal fade" id="deleteModal" tabIndex={-1} role="dialog" aria-labelledby="deleteConformationLabel"
      aria-hidden="true">
      <div className="modal-dialog modal-dialog-centered" role="document">
        <div className="modal-content">
          <div className="modal-header">
            <div className="icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                className="feather feather-trash-2">
                <polyline points="3 6 5 6 21 6"></polyline>
                <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                <line x1="10" y1="11" x2="10" y2="17"></line>
                <line x1="14" y1="11" x2="14" y2="17"></line>
              </svg>
            </div>
            <h5 className="modal-title">Xác nhận xóa?</h5>
            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div className="modal-body">
            <p>Bạn có chắc chắn muốn xóa phần tử này?</p>
          </div>
          <div className="modal-footer">
            <button type="button" className="btn" data-bs-dismiss="modal">Hủy</button>
            <button type="button" className="btn btn-danger" id="confirmDelete">Xác nhận</button>
          </div>
        </div>
      </div>
    </div>
    {/* END MODAL DELETE */}
    {/* BEGIN GLOBAL MANDATORY SCRIPTS */}

    <script src="/js/customs/assets/scrollspynav.js"></script>
    <script src="/js/customs/bootstrap/bootstrap.bundle.min.js"></script>
    <script src="/plugins/global/vendors.min.js"></script>
    <script src="/plugins/perfect-scrollbar/perfect-scrollbar.min.js"></script>
    <script src="/plugins/mousetrap/mousetrap.min.js"></script>
    <link href="/plugins/waves/waves.min.css" rel="stylesheet" />
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    {/* Tải Bootstrap JS sau jQuery */}
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>

    {/* Tải các thư viện khác nếu cần */}
    <script src="https://cdn.jsdelivr.net/npm/snackbarjs/dist/snackbar.min.js"></script>

    <script src="/plugins/notification/snackbar/snackbar.min.js"></script>
    <script src="/plugins/font-icons/feather/feather.min.js"></script>
    <script src="/js/customs/layouts/modern-light-menu/app.js"></script>

    {/* END GLOBAL MANDATORY SCRIPTS */}
    {/* BEGIN PAGE LEVEL PLUGINS/CUSTOM SCRIPTS */}
    <script src="/plugins/apex/apexcharts.min.js"></script>
    <script src="/js/customs/assets/dashboard/dash_1.js"></script>
    <script src="/plugins/sweetalerts2/sweetalerts2.min.js"></script>
    <script src="/js/customs/assets/custom.js"></script>
    <script src="/js/customs/assets/elements/custom-search.js"></script>
    <script src="/js/customs/app.js"></script>
  </>)
}

export function App() {
  return (
    <BrowserRouter>
      <AppContent />
    </BrowserRouter>
  );
}

// TODO: Add controller