import { FormEvent, useEffect, useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom"
import { saveTokenToService } from "../services/AuthService";
import { getCurrentDoctor } from "../services/DoctorService";
import { getRoleById } from "../services/RoleService";
import { setSession } from "../services/HttpApiService";

export function DashboardPage() {
    const location = useLocation();
    useEffect(() => {
        if (location.pathname === "/dashboard") {
            document.title = "Trang chủ"
        }
    }, [location.pathname]);

    return (<>
        <style>{`
            body {
                background: url('/images/doctor-background.jpg') no-repeat center center fixed;
                background-size: cover;
                font-family: Arial, sans-serif;
                color: #fff;
            }

            .middle-content {
                position: relative;
                background: rgba(0, 0, 0, 0.5);
                padding: 50px 0;
                border-radius: 10px;
                text-align: center;
            }

            .widget-card-four {
                position: relative;
                background: rgba(255, 255, 255, 0.8);
                padding: 40px;
                border-radius: 8px;
                z-index: 5; /* Make sure text is on top */
            }

            .widget-card-four h1 {
                font-size: 40px;
                font-weight: bold;
                margin-bottom: 20px;
            }

            .widget-card-four p {
                font-size: 18px;
                line-height: 1.6;
                color: #555;
            }

            /* Style for the layered images */
            .image-stack {
                position: relative;
                display: inline-block;
                width: 100%;
                height: 400px; /* Set a fixed height for the image stack */
                max-width: 600px; /* Set a max-width if needed */
                margin: 0 auto; /* Center the image stack */
            }

            .image-stack img {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                object-fit: cover;
                border-radius: 10px;
            }

            .image-stack img:nth-child(1) {
                opacity: 0.6;
            }

            .image-stack img:nth-child(2) {
                opacity: 0.7;
            }

            .image-stack img:nth-child(3) {
                opacity: 0.8;
            }

            /* Optional: Add a nice border or shadow to the images */
            .image-stack img {
                box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.3);
            }
        `}</style>

        <div className="middle-content container-xxl p-0 mt-5">
            <div className="row layout-top-spacing justify-content-center">
                <div className="col-xl-8 col-lg-10 col-md-12 col-sm-12 col-12 layout-spacing" style={{ width: "100%" }}>
                    <div className="widget widget-card-four">
                        <h1>CHÀO MỪNG BẠN ĐẾN VỚI TRANG WEB ĐẶT LỊCH PHÒNG KHÁM</h1>
                        <p>Chúng tôi cung cấp dịch vụ đặt lịch khám bệnh trực tuyến, giúp bạn dễ dàng và nhanh chóng sắp xếp thời gian cho các cuộc hẹn.</p>

                        {/* Layered images */}
                        <div className="image-stack">
                            <img src="https://barbadostoday.bb/wp-content/uploads/2021/12/appointment.png" alt="Doctor Image 1" />
                            <img src="https://barbadostoday.bb/wp-content/uploads/2021/12/appointment.png" alt="Doctor Image 2" />
                            <img src="https://barbadostoday.bb/wp-content/uploads/2021/12/appointment.png" alt="Doctor Image 3" />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </>)
}

export function LoginPage() {
    const location = useLocation();
    const navigate = useNavigate();
    useEffect(() => {
        if (location.pathname === "/dashboard") {
            document.title = "Trang chủ"
        }
    }, [location.pathname]);

    const formRef = useRef<HTMLFormElement>(null);
    useEffect(() => {
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
                let username: any = data.get("username") || undefined;
                let password: any = data.get("password") || undefined;
                if ("string" !== typeof username) username = undefined;
                if ("string" !== typeof password) password = undefined;

                saveTokenToService({ username, password }).then(function (response) {
                    if (!response) return;
                    getCurrentDoctor().then(function(response) {
                        if (!response) return;
                        const id = response.id;
                        const full_name = response.full_name;
                        if (response.role) getRoleById(response.role).then(function(response) {
                            const role = response?.name;
                            setSession("user", JSON.stringify({ id, full_name, role }));
                            navigate("/dashboard");
                        });
                    });
                });
            }
        });
    }, []);

    return (<>
        <style>{`
            /* General container styling */
            .container {
                max-width: 500px;
                margin: 0 auto;
                padding: 30px;
                background-color: #f4f7fa;
                border-radius: 8px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            }
        
            /* Title styling */
            h1 {
                color: #d9534f;
                font-family: 'Arial', sans-serif;
                font-weight: 700;
                font-size: 36px;
                margin-bottom: 20px;
            }
        
            /* Input fields styling */
            .form-group {
                margin-bottom: 20px;
            }
        
            /* Input group */
            .input-group {
                margin-bottom: 15px;
            }
        
            /* Styling the text inputs */
            .form-control {
                border-radius: 5px;
                padding: 10px;
                font-size: 16px;
                border: 1px solid #ccc;
                box-shadow: none;
            }
        
            /* Styling for the icon in the input fields */
            .input-group-text {
                background-color: #f1f1f1;
                border-right: 1px solid #ddd;
                border-radius: 5px;
                color: #6c757d;
            }
        
            /* Styling for the eye icon toggle */
            .input-group-append .bi-eye {
                cursor: pointer;
                color: #6c757d;
            }
        
            /* Button styles */
            button.btn {
                width: 100%;
                padding: 12px;
                background-color: #007bff;
                border: none;
                border-radius: 5px;
                color: white;
                font-size: 16px;
                cursor: pointer;
                transition: background-color 0.3s ease;
            }
        
            button.btn:hover {
                background-color: #0056b3;
            }
        
            /* "Remember me" checkbox and forgot password link styling */
            .form-check-label {
                font-size: 14px;
                color: #6c757d;
            }
        
            .forgot-pasword {
                font-size: 14px;
                color: #007bff;
                text-decoration: none;
            }
        
            .forgot-pasword:hover {
                text-decoration: underline;
            }
        
            /* Error message styling */
            .text-danger {
                font-size: 14px;
                font-weight: 600;
            }
        
            /* Link to regulatory policy */
            .mt-4 a {
                color: #007bff;
            }
        
            .mt-4 a:hover {
                text-decoration: underline;
            }
        
            /* Sign-up button styling */
            .btn-secondary {
                width: 100%;
                padding: 12px;
                background-color: #6c757d;
                border: none;
                border-radius: 5px;
                color: white;
                font-size: 16px;
                cursor: pointer;
                transition: background-color 0.3s ease;
            }
        
            .btn-secondary:hover {
                background-color: #5a6268;
            }
        `}</style>


        <div className="container" id="container">
            <div className="login-container">
                <div className="text-center mb-4">
                    <div style={{ width: "fit-content", margin: "auto" }}>
                        <h1 style={{ color: "red" }}>ĐĂNG NHẬP</h1>
                    </div>
                </div>

                <form ref={formRef} id="login-form">
                    <div className="container-form-group">
                        <div className="form-group" style={{ marginBottom: "20px", width: "100%" }}>
                            <div className="input-group">
                                <div className="input-group-prepend">
                                    <span className="input-group-text"><i className="bi bi-envelope"
                                        style={{ marginBottom: "5px" }}></i></span>
                                </div>
                                <input id="email" style={{ width: "100%" }} asp-for="Username" className="form-control"
                                    placeholder="Username or email" />
                            </div>
                            <span className="text-danger" style={{ marginLeft: "40px;" }}></span>
                        </div>
                        <div className="form-group">
                            <div className="input-group" style={{ marginBottom: "5px" }}>
                                <div className="input-group-prepend">
                                    <span className="input-group-text"><i className="bi bi-lock"></i></span>
                                </div>
                                <input id="password" style={{ width: "100%" }} asp-for="Password" className="form-control" type="password"
                                    placeholder="Password" />
                                <div className="input-group-append">
                                    <span className="input-group-text">
                                        <i className="bi bi-eye" id="togglePassword"></i>
                                    </span>
                                </div>
                            </div>
                            <span className="text-danger" style={{ marginLeft: "40px;" }}></span>
                        </div>
                    </div>

                    <p className="text-center text-danger" id="error-message"></p>

                    <div className="form-group button-action">
                        <button type="submit" className="btn btn-primary">Login</button>
                    </div>

                </form>
            </div>
        </div>
    </>)
}