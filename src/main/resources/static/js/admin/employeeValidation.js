document.addEventListener('DOMContentLoaded', function() {
    const phoneInputs = document.querySelectorAll("#employeePhone, #editEmployeePhone");

    phoneInputs.forEach(input => {
        if (input) {
            input.addEventListener("input", function(e) {
                const value = e.target.value;

                // Chỉ cho phép số
                if (!/^\d*$/.test(value)) {
                    e.target.value = value.replace(/\D/g, '');
                }
                if (value.length > 10) {
                    e.target.value = value.slice(0, 10);
                }
            });

            // Xử lý khi paste số điện thoại
            input.addEventListener('paste', function(e) {
                setTimeout(() => {
                    const currentValue = this.value;
                    const filteredValue = currentValue.replace(/\D/g, '');
                    if (filteredValue.length > 10) {
                        this.value = filteredValue.substring(0, 10);
                    } else {
                        this.value = filteredValue;
                    }
                }, 0);
            });
        }
    });

    const nameInputs = document.querySelectorAll("#employeeName, #editEmployeeName");

    nameInputs.forEach(input => {
        if (input) {
            let errorId = input.id === 'employeeName' ? 'employeeNameError' : 'editEmployeeNameError';
            let charLimitMsg = document.getElementById(errorId);

            input.addEventListener("input", function(e) {
                const value = e.target.value;

                if (!/^[A-Za-z0-9À-ỹ\s]*$/.test(value)) {
                    e.target.value = value.replace(/[^A-Za-z0-9À-ỹ\s]/g, '');
                }
                if (value.length > 50) {
                    e.target.value = value.slice(0, 50);
                    if (charLimitMsg) {
                        charLimitMsg.textContent = 'Đã đạt giới hạn ký tự tên nhân viên';
                        charLimitMsg.style.display = 'block';
                        setTimeout(() => {
                            charLimitMsg.style.display = 'none';
                        }, 3000);
                    }
                }
            });

            // Xử lý khi paste tên nhân viên
            input.addEventListener('paste', function(e) {
                setTimeout(() => {
                    const MAX_LENGTH = 50;
                    const currentValue = this.value;
                    const filteredValue = currentValue.replace(/[^A-Za-z0-9À-ỹ\s]/g, '');

                    if (filteredValue.length > MAX_LENGTH) {
                        this.value = filteredValue.substring(0, MAX_LENGTH);
                        if (charLimitMsg) {
                            charLimitMsg.textContent = 'Đã đạt giới hạn 50 ký tự cho tên nhân viên';
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

    const addressInputs = document.querySelectorAll("#employeeAddress, #editEmployeeAddress");

    addressInputs.forEach(input => {
        if (input) {
            let errorId = input.id === 'employeeAddress' ? 'employeeAddressError' : 'editEmployeeAddressError';
            let charLimitMsg = document.getElementById(errorId);

            // Xử lý khi nhập địa chỉ
            input.addEventListener("input", function(e) {
                const value = e.target.value;
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
                    const filteredValue = currentValue.replace(/[^A-Za-z0-9À-ỹ,\s.-]/g, '');
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

    const emailInputs = document.querySelectorAll("#email, #editEmail");

    emailInputs.forEach(input => {
        if (input) {
            let errorId = input.id === 'email' ? 'emailError' : 'editEmailError';
            let charLimitMsg = document.getElementById(errorId);

            input.addEventListener("input", function(e) {
                const value = e.target.value;
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
        }
    });

    const birthdayInputs = document.querySelectorAll("#employeeBirthday, #editEmployeeBirthday");

    birthdayInputs.forEach(input => {
        if (input) {
            let errorId = input.id === 'employeeBirthday' ? 'employeeBirthdayError' : 'editEmployeeBirthdayError';
            let errorMsg = document.getElementById(errorId);

            input.addEventListener("change", function(e) {
                const birthDate = new Date(this.value);
                const today = new Date();
                const ageDate = new Date(today - birthDate);
                const age = Math.abs(ageDate.getUTCFullYear() - 1970);

                if (age <= 15) {
                    if (errorMsg) {
                        errorMsg.textContent = "Tuổi phải lớn hơn 15";
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
});