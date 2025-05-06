$(document).ready(function() {
    // Khi mở modal thêm nhân viên
    $('#addEmployeeModal').on('show.bs.modal', function() {
        // Lấy mã nhân viên mới từ server
        $.ajax({
            url: '/Admin/employee-manager/generate-code',
            type: 'GET',
            success: function(response) {
                $('#employeeCode').val(response);
            }
        });

        // Reset form và xóa thông báo lỗi
        $('#addEmployeeForm')[0].reset();
        $('.invalid-feedback').text('');
    });

    // Validate form khi nhập
    function validateField(inputElement, errorElementSelector, minLength, maxLength, regexPattern, emptyMessage, lengthMessage, formatMessage) {
        let value = inputElement.val().trim();
        let errorElement = $(errorElementSelector);

        if (value === '') {
            errorElement.text(emptyMessage);
            return false;
        } else if (value.length < minLength || value.length > maxLength) {
            errorElement.text(lengthMessage);
            return false;
        } else if (!regexPattern.test(value)) {
            errorElement.text(formatMessage);
            return false;
        } else {
            errorElement.text('');
            return true;
        }
    }

    // Validate tên nhân viên khi nhập
    $('#employeeName').on('input', function() {
        // Loại bỏ ký tự đặc biệt khi nhập
        this.value = this.value.replace(/[^\p{L}\s]/gu, '');

        validateField(
            $(this),
            '#employeeNameError',
            2, 50,
            /^[\p{L}\s]+$/u,
            'Vui lòng không để trống tên nhân viên',
            'Tên nhân viên phải từ 2 đến 50 ký tự',
            'Tên nhân viên chỉ được chứa chữ cái và khoảng trắng'
        );
    });

    // Validate số điện thoại khi nhập
    $('#employeePhone').on('input', function() {
        // Loại bỏ ký tự không phải số
        this.value = this.value.replace(/[^\d]/g, '');

        validateField(
            $(this),
            '#employeePhoneError',
            10, 11,
            /^(0)[35789][0-9]{8}$/,
            'Vui lòng không để trống số điện thoại',
            'Số điện thoại phải có 10 số',
            'Số điện thoại phải bắt đầu bằng 0, theo sau là 3,5,7,8,9 và 8 chữ số'
        );
    });

    // Validate địa chỉ khi nhập
    $('#employeeAddress').on('input', function() {
        validateField(
            $(this),
            '#employeeAddressError',
            5, 200,
            /^[A-Za-z0-9À-ỹ,\s.-]+$/,
            'Vui lòng không để trống địa chỉ',
            'Địa chỉ phải có độ dài từ 5 đến 200 ký tự',
            'Địa chỉ chỉ được chứa chữ cái, số, khoảng trắng, dấu chấm, dấu phẩy và dấu gạch ngang'
        );
    });

    // Validate email khi nhập
    $('#email').on('input', function() {
        validateField(
            $(this),
            '#emailError',
            5, 100,
            /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/,
            'Vui lòng không để trống email',
            'Email phải theo định dạng chuẩn (ví dụ: example@domain.com)'
        );
    });

    // Validate username khi nhập
    $('#username').on('input', function() {
        // Loại bỏ ký tự đặc biệt
        this.value = this.value.replace(/[^a-zA-Z0-9_]/g, '');

        validateField(
            $(this),
            '#usernameError',
            4, 20,
            /^[a-zA-Z0-9_]+$/,
            'Vui lòng không để trống tên đăng nhập',
            'Tên đăng nhập phải từ 4 đến 20 ký tự',
            'Tên đăng nhập chỉ được chứa chữ cái, số và dấu gạch dưới'
        );
    });

    // Đã xóa validate cho password ở đây

    // Tương tự validate cho các trường trong form chỉnh sửa
    $('#editEmployeeName').on('input', function() {
        // Loại bỏ ký tự đặc biệt khi nhập
        this.value = this.value.replace(/[^\p{L}\s]/gu, '');

        validateField(
            $(this),
            '#editEmployeeNameError',
            2, 50,
            /^[\p{L}\s]+$/u,
            'Vui lòng không để trống tên nhân viên',
            'Tên nhân viên phải từ 2 đến 50 ký tự',
            'Tên nhân viên chỉ được chứa chữ cái và khoảng trắng'
        );
    });

    $('#editEmployeePhone').on('input', function() {
        // Loại bỏ ký tự không phải số
        this.value = this.value.replace(/[^\d]/g, '');

        validateField(
            $(this),
            '#editEmployeePhoneError',
            10, 11,
            /^(0)[35789][0-9]{8}$/,
            'Vui lòng không để trống số điện thoại',
            'Số điện thoại phải có 10 số',
            'Số điện thoại phải bắt đầu bằng 0, theo sau là 3,5,7,8,9 và 8 chữ số'
        );
    });

    $('#editEmployeeAddress').on('input', function() {
        validateField(
            $(this),
            '#editEmployeeAddressError',
            5, 200,
            /^[A-Za-z0-9À-ỹ,\s.-]+$/,
            'Vui lòng không để trống địa chỉ',
            'Địa chỉ phải có độ dài từ 5 đến 200 ký tự',
            'Địa chỉ chỉ được chứa chữ cái, số, khoảng trắng, dấu chấm, dấu phẩy và dấu gạch ngang'
        );
    });

    $('#editEmail').on('input', function() {
        validateField(
            $(this),
            '#editEmailError',
            5, 100,
            /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/,
            'Vui lòng không để trống email',
            'Email phải theo định dạng chuẩn (ví dụ: example@domain.com)'
        );
    });

    // Không thêm validate cho mật khẩu trong form chỉnh sửa

    // Kiểm tra trùng lặp email
    $('#email').on('blur', function() {
        const email = $(this).val().trim();

        if (email.length >= 5 && /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/.test(email)) {
            $.ajax({
                url: '/Admin/employee-manager/check-email',
                type: 'GET',
                data: { email: email },
                success: function(response) {
                    if (response.exists) {
                        $('#emailError').text('Email đã tồn tại');
                    }
                }
            });
        }
    });

    // Kiểm tra trùng lặp email khi chỉnh sửa
    $('#editEmail').on('blur', function() {
        const email = $(this).val().trim();
        const userId = $('#editUserId').val();

        if (email.length >= 5 && /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/.test(email)) {
            $.ajax({
                url: '/Admin/employee-manager/check-email',
                type: 'GET',
                data: { email: email, userId: userId },
                success: function(response) {
                    if (response.exists) {
                        $('#editEmailError').text('Email đã tồn tại');
                    }
                }
            });
        }
    });

    // Kiểm tra trùng lặp username
    $('#username').on('blur', function() {
        const username = $(this).val().trim();

        if (username.length >= 4) {
            $.ajax({
                url: '/Admin/employee-manager/check-username',
                type: 'GET',
                data: { username: username },
                success: function(response) {
                    if (response.exists) {
                        $('#usernameError').text('Tên đăng nhập đã tồn tại');
                    }
                }
            });
        }
    });

    // Validate ngày sinh
    function validateDateOfBirth(dateInput, errorElement) {
        const selectedDate = new Date(dateInput.val());
        const currentDate = new Date();
        const minAge = 16; // Tuổi tối thiểu của nhân viên

        // Tính ngày tối đa cho phép (hiện tại - 16 năm)
        const maxDate = new Date(
            currentDate.getFullYear() - minAge,
            currentDate.getMonth(),
            currentDate.getDate()
        );

        if (isNaN(selectedDate.getTime())) {
            errorElement.text('Vui lòng chọn ngày sinh');
            return false;
        } else if (selectedDate > currentDate) {
            errorElement.text('Ngày sinh không thể là ngày trong tương lai');
            return false;
        } else if (selectedDate > maxDate) {
            errorElement.text('Nhân viên phải đủ ' + minAge + ' tuổi');
            return false;
        } else {
            errorElement.text('');
            return true;
        }
    }

    // Validate ngày sinh khi nhập
    $('#employeeBirthday').on('change', function() {
        validateDateOfBirth($(this), $('#employeeBirthdayError'));
    });

    // Validate ngày sinh khi chỉnh sửa
    $('#editEmployeeBirthday').on('change', function() {
        validateDateOfBirth($(this), $('#editEmployeeBirthdayError'));
    });

    // Xử lý submit form thêm nhân viên
    $('#addEmployeeForm').submit(function(e) {
        e.preventDefault();
        $('.invalid-feedback').text('');

        // Validate tất cả các trường
        // Thực hiện validation cho từng trường ở đây...

        let employeeData = {
            employeeCode: $('#employeeCode').val(),
            employeeName: $('#employeeName').val().trim(),
            employeeBirthday: $('#employeeBirthday').val(),
            email: $('#email').val().trim(),
            employeePhone: $('#employeePhone').val().trim(),
            employeeAddress: $('#employeeAddress').val().trim(),
            username: $('#username').val().trim(),
            password: $('#password').val(), // Vẫn lấy giá trị nhưng không validate
            role: $('#role').val()
        };

        // Lấy CSRF token nếu có
        const token = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
        const header = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

        // Cấu hình header
        const headers = {
            'Content-Type': 'application/json'
        };

        if (token && header) {
            headers[header] = token;
        }

        $.ajax({
            url: '/Admin/employee-manager/create',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(employeeData),
            headers: headers,
            success: function(response) {
                $('#addEmployeeModal').modal('hide');
                window.location.href = '/Admin/employee-manager?successMessage=' + encodeURIComponent('Thêm nhân viên thành công!');
            },
            error: function(xhr) {
                let errors = xhr.responseJSON;
                for (let field in errors) {
                    $('#' + field + 'Error').text(errors[field]).show();
                }
            }
        });
    });

    // Xử lý form sửa nhân viên
    $('#editEmployeeForm').submit(function(event) {
        event.preventDefault();

        // Xóa thông báo lỗi cũ
        $('.error').text('');

        // Lấy dữ liệu từ form
        let employeeData = {
            userId: $('#editUserId').val(),
            employeeCode: $('#editEmployeeCode').val(),
            employeeName: $('#editEmployeeName').val(),
            employeeBirthday: $('#editEmployeeBirthday').val(),
            email: $('#editEmail').val(),
            employeeAddress: $('#editEmployeeAddress').val(),
            employeePhone: $('#editEmployeePhone').val(),
            username: $('#editUsername').val(),
            password: $('#editPassword').val(), // Lấy giá trị password mà không validate
            role: $('#editRole').val()
        };

        // Lấy CSRF token nếu có
        const token = $('meta[name="_csrf"]').attr('content');
        const header = $('meta[name="_csrf_header"]').attr('content');

        // Cấu hình header cho request
        const headers = {
            'Content-Type': 'application/json'
        };

        // Thêm CSRF token vào header nếu có
        if (token && header) {
            headers[header] = token;
        }

        // Gửi request Ajax
        $.ajax({
            url: '/Admin/employee-manager/edit',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(employeeData),
            headers: headers,
            success: function(response) {
                // Đóng modal
                $('#editEmployeeModal').modal('hide');

                // Chuyển hướng với thông báo thành công
                window.location.href = '/Admin/employee-manager?successMessage=' + encodeURIComponent('Cập nhật nhân viên thành công!');
            },
            error: function(xhr) {
                // Xóa thông báo lỗi cũ
                $('.error').text('');

                // Hiển thị lỗi
                let errors = xhr.responseJSON;
                for (let field in errors) {
                    if (field !== 'password') { // Bỏ qua lỗi về mật khẩu
                        $('#edit' + field.charAt(0).toUpperCase() + field.slice(1) + 'Error').text(errors[field]);
                    }
                }
            }
        });
    });
});