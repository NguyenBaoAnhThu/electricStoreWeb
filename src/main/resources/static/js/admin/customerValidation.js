document.addEventListener('DOMContentLoaded', function() {
    // === VALIDATE SỐ ĐIỆN THOẠI KHÁCH HÀNG ===
    const phoneInputs = document.querySelectorAll("#customerPhoneNumber, #phoneNumber");

    phoneInputs.forEach(input => {
        if (input) {
            // Xử lý khi nhập số điện thoại
            input.addEventListener("input", function(e) {
                const value = e.target.value;

                // Chỉ cho phép số và dấu +
                if (!/^[+\d]*$/.test(value)) {
                    e.target.value = value.replace(/[^+\d]/g, '');
                }

                // Giới hạn độ dài
                if (value.length > 12) {
                    e.target.value = value.slice(0, 12);
                }
            });

            // Xử lý khi paste số điện thoại
            input.addEventListener('paste', function(e) {
                setTimeout(() => {
                    const currentValue = this.value;

                    // Lọc ký tự không phải số và dấu +
                    const filteredValue = currentValue.replace(/[^+\d]/g, '');

                    // Giới hạn độ dài
                    if (filteredValue.length > 12) {
                        this.value = filteredValue.substring(0, 12);
                    } else {
                        this.value = filteredValue;
                    }
                }, 0);
            });
        }
    });

    // === VALIDATE TÊN KHÁCH HÀNG ===
    const nameInputs = document.querySelectorAll("#customerName");

    nameInputs.forEach(input => {
        if (input) {
            // Thêm id cho thông báo giới hạn ký tự
            let errorId = 'customerNameError';
            let charLimitMsg = document.getElementById(errorId);

            // Xử lý khi nhập tên khách hàng
            input.addEventListener("input", function(e) {
                const value = e.target.value;

                // Chỉ cho phép chữ cái và khoảng trắng
                if (!/^[A-Za-zÀ-ỹ\s]*$/.test(value)) {
                    e.target.value = value.replace(/[^A-Za-zÀ-ỹ\s]/g, '');
                }

                // Giới hạn độ dài
                if (value.length > 50) {
                    e.target.value = value.slice(0, 50);

                    // Hiển thị thông báo giới hạn
                    if (charLimitMsg) {
                        charLimitMsg.textContent = 'Đã đạt giới hạn 50 ký tự cho tên khách hàng';
                        charLimitMsg.style.display = 'block';

                        setTimeout(() => {
                            charLimitMsg.style.display = 'none';
                        }, 3000);
                    }
                }
            });

            // Xử lý khi paste tên khách hàng
            input.addEventListener('paste', function(e) {
                setTimeout(() => {
                    const MAX_LENGTH = 50;
                    const currentValue = this.value;

                    // Lọc ký tự không hợp lệ
                    const filteredValue = currentValue.replace(/[^A-Za-zÀ-ỹ\s]/g, '');

                    // Giới hạn độ dài
                    if (filteredValue.length > MAX_LENGTH) {
                        this.value = filteredValue.substring(0, MAX_LENGTH);

                        // Hiển thị thông báo
                        if (charLimitMsg) {
                            charLimitMsg.textContent = 'Đã đạt giới hạn 50 ký tự cho tên khách hàng';
                            charLimitMsg.style.display = 'block';

                            setTimeout(() => {
                                charLimitMsg.style.display = 'none';
                            }, 3000);
                        }
                    } else {
                        this.value = filteredValue;
                    }
                }, 0);
            });
        }
    });

    // === VALIDATE ĐỊA CHỈ KHÁCH HÀNG ===
    const addressInputs = document.querySelectorAll("#customerAddress, #address");

    addressInputs.forEach(input => {
        if (input) {
            let errorId = 'addressError';
            let charLimitMsg = document.getElementById(errorId);

            // Xử lý khi nhập địa chỉ
            input.addEventListener("input", function(e) {
                const value = e.target.value;

                // Chỉ cho phép chữ cái, số, khoảng trắng và các ký tự đặc biệt cho phép
                if (!/^[A-Za-z0-9À-ỹ,\s.-]*$/.test(value)) {
                    e.target.value = value.replace(/[^A-Za-z0-9À-ỹ,\s.-]/g, '');
                }

                // Giới hạn độ dài
                if (value.length > 200) {
                    e.target.value = value.slice(0, 200);

                    if (charLimitMsg) {
                        charLimitMsg.textContent = 'Đã đạt giới hạn 200 ký tự cho địa chỉ';
                        charLimitMsg.style.display = 'block';

                        setTimeout(() => {
                            charLimitMsg.style.display = 'none';
                        }, 3000);
                    }
                }
            });

            // Xử lý khi paste địa chỉ
            input.addEventListener('paste', function(e) {
                setTimeout(() => {
                    const MAX_LENGTH = 200;
                    const currentValue = this.value;

                    // Lọc ký tự không hợp lệ
                    const filteredValue = currentValue.replace(/[^A-Za-z0-9À-ỹ,\s.-]/g, '');

                    // Giới hạn độ dài
                    if (filteredValue.length > MAX_LENGTH) {
                        this.value = filteredValue.substring(0, MAX_LENGTH);

                        if (charLimitMsg) {
                            charLimitMsg.textContent = 'Đã đạt giới hạn 200 ký tự cho địa chỉ';
                            charLimitMsg.style.display = 'block';

                            setTimeout(() => {
                                charLimitMsg.style.display = 'none';
                            }, 3000);
                        }
                    } else {
                        this.value = filteredValue;
                    }
                }, 0);
            });
        }
    });

    // === VALIDATE EMAIL KHÁCH HÀNG ===
    const emailInputs = document.querySelectorAll("#customerEmail, #email");

    emailInputs.forEach(input => {
        if (input) {
            let errorId = 'emailError';
            let charLimitMsg = document.getElementById(errorId);

            // Xử lý khi nhập email
            input.addEventListener("input", function(e) {
                const value = e.target.value;

                // Giới hạn độ dài
                if (value.length > 100) {
                    e.target.value = value.slice(0, 100);

                    if (charLimitMsg) {
                        charLimitMsg.textContent = 'Đã đạt giới hạn 100 ký tự cho email';
                        charLimitMsg.style.display = 'block';

                        setTimeout(() => {
                            charLimitMsg.style.display = 'none';
                        }, 3000);
                    }
                }
            });

            // Kiểm tra định dạng email khi mất focus
            input.addEventListener("blur", function() {
                const value = this.value.trim();
                const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

                if (value && !emailRegex.test(value)) {
                    if (charLimitMsg) {
                        charLimitMsg.textContent = 'Email không hợp lệ, vui lòng nhập đúng định dạng';
                        charLimitMsg.style.display = 'block';
                    }
                } else if (charLimitMsg) {
                    charLimitMsg.style.display = 'none';
                }
            });
        }
    });

    // === VALIDATE NGÀY SINH KHÁCH HÀNG ===
    const birthdayInputs = document.querySelectorAll("#customerBirthdate, #birthDate");

    birthdayInputs.forEach(input => {
        if (input) {
            let errorId = 'birthDateError';
            let errorMsg = document.getElementById(errorId);

            input.addEventListener("change", function(e) {
                const birthDate = new Date(this.value);
                const today = new Date();
                const ageDate = new Date(today - birthDate);
                const age = Math.abs(ageDate.getUTCFullYear() - 1970);

                if (age < 15) {
                    if (errorMsg) {
                        errorMsg.textContent = "Không đủ điều kiện, tuổi phải lớn hơn 15!";
                        errorMsg.style.display = "block";
                    }
                } else {
                    if (errorMsg) {
                        errorMsg.textContent = "";
                        errorMsg.style.display = "none";
                    }
                }
            });
        }
    });

    // === VALIDATE FORM TRƯỚC KHI SUBMIT ===
    const addCustomerForm = document.getElementById('addCustomerForm');
    const editCustomerForm = document.getElementById('editCustomerForm');
    const orderForm = document.getElementById('orderForm');

    // Hàm kiểm tra form
    function validateCustomerForm(form) {
        let isValid = true;

        // Kiểm tra tên khách hàng
        const nameInput = form.querySelector('#customerName') || form.querySelector('[name="customerName"]');
        if (nameInput && nameInput.value.trim() === '') {
            const errorElement = document.getElementById('customerNameError') || document.querySelector('.error[for="customerName"]');
            if (errorElement) {
                errorElement.textContent = 'Tên không được để trống';
                errorElement.style.display = 'block';
            }
            isValid = false;
        }

        // Kiểm tra số điện thoại
        const phoneInput = form.querySelector('#customerPhoneNumber') || form.querySelector('#phoneNumber') || form.querySelector('[name="phoneNumber"]');
        if (phoneInput) {
            const phoneValue = phoneInput.value.trim();
            const phoneRegex = /^(\+84|0)[35789][0-9]{8}$/;

            if (phoneValue === '') {
                const errorElement = document.getElementById('phoneNumberError') || document.querySelector('.error[for="phoneNumber"]');
                if (errorElement) {
                    errorElement.textContent = 'Số điện thoại không được để trống';
                    errorElement.style.display = 'block';
                }
                isValid = false;
            } else if (!phoneRegex.test(phoneValue)) {
                const errorElement = document.getElementById('phoneNumberError') || document.querySelector('.error[for="phoneNumber"]');
                if (errorElement) {
                    errorElement.textContent = 'Số điện thoại phải đủ 10 số';
                    errorElement.style.display = 'block';
                }
                isValid = false;
            }
        }

        // Kiểm tra địa chỉ
        const addressInput = form.querySelector('#customerAddress') || form.querySelector('#address') || form.querySelector('[name="address"]');
        if (addressInput && addressInput.value.trim() === '') {
            const errorElement = document.getElementById('addressError') || document.querySelector('.error[for="address"]');
            if (errorElement) {
                errorElement.textContent = 'Địa chỉ không được để trống';
                errorElement.style.display = 'block';
            }
            isValid = false;
        }

        // Kiểm tra email
        const emailInput = form.querySelector('#customerEmail') || form.querySelector('#email') || form.querySelector('[name="email"]');
        if (emailInput) {
            const emailValue = emailInput.value.trim();
            const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

            if (emailValue === '') {
                const errorElement = document.getElementById('emailError') || document.querySelector('.error[for="email"]');
                if (errorElement) {
                    errorElement.textContent = 'Email không được để trống';
                    errorElement.style.display = 'block';
                }
                isValid = false;
            } else if (!emailRegex.test(emailValue)) {
                const errorElement = document.getElementById('emailError') || document.querySelector('.error[for="email"]');
                if (errorElement) {
                    errorElement.textContent = 'Email không hợp lệ, vui lòng nhập đúng định dạng';
                    errorElement.style.display = 'block';
                }
                isValid = false;
            }
        }

        return isValid;
    }

    // Áp dụng xác thực cho các form
    if (addCustomerForm) {
        addCustomerForm.addEventListener('submit', function(e) {
            if (!validateCustomerForm(this)) {
                e.preventDefault();
            }
        });
    }

    if (editCustomerForm) {
        editCustomerForm.addEventListener('submit', function(e) {
            if (!validateCustomerForm(this)) {
                e.preventDefault();
            }
        });
    }

    // Kiểm tra phần khách hàng trong form đơn hàng
    if (orderForm) {
        orderForm.addEventListener('submit', function(e) {
            // Chỉ kiểm tra thông tin khách hàng nếu không chọn khách hàng có sẵn
            const customerIdInput = document.getElementById('customerId');
            if (customerIdInput && !customerIdInput.value) {
                if (!validateCustomerForm(this)) {
                    e.preventDefault();
                }
            }
        });
    }
});