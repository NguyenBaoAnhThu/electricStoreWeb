$(document).ready(function() {
    // Khi mở modal thêm nhà cung cấp
    $('#addSupplierModal').on('show.bs.modal', function() {
        // Lấy mã nhà cung cấp mới từ server
        $.ajax({
            url: '/Admin/suppliers-manager/generate-code',
            type: 'GET',
            success: function(response) {
                $('#supplierCode').val(response);
            }
        });

        // Reset form và xóa thông báo lỗi
        $('#addSupplierForm')[0].reset();
        $('.error').text('');
    });

    // Xử lý form thêm nhà cung cấp
    $('#addSupplierForm').submit(function(event) {
        event.preventDefault();

        // Validate trước khi gửi form
        let hasError = false;

        // Kiểm tra trường tên nhà cung cấp
        const supplierName = $('#supplierName').val().trim();
        if (!supplierName) {
            $('#supplierNameError').text('Vui lòng không để trống tên nhà cung cấp');
            hasError = true;
        } else if (supplierName.length < 2) {
            $('#supplierNameError').text('Tên nhà cung cấp phải có ít nhất 2 ký tự');
            hasError = true;
        } else if (!/^[A-Za-zÀ-ỹ\s]+$/.test(supplierName)) {
            $('#supplierNameError').text('Tên nhà cung cấp chỉ được chứa chữ cái và khoảng trắng');
            hasError = true;
        }

        // Kiểm tra trường địa chỉ
        const address = $('#address').val().trim();
        if (!address) {
            $('#addressError').text('Vui lòng không để trống địa chỉ');
            hasError = true;
        } else if (address.length < 5) {
            $('#addressError').text('Địa chỉ phải có ít nhất 5 ký tự');
            hasError = true;
        }

        // Kiểm tra số điện thoại
        const phone = $('#phone').val().trim();
        if (!phone) {
            $('#phoneError').text('Vui lòng không để trống số điện thoại');
            hasError = true;
        } else if (!/^0[0-9]{9}$/.test(phone)) {
            $('#phoneError').text('Số điện thoại phải có 10 số và bắt đầu bằng số 0');
            hasError = true;
        }

        // Kiểm tra email
        const email = $('#email').val().trim();
        const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
        if (!email) {
            $('#emailError').text('Vui lòng không để trống email');
            hasError = true;
        } else if (!emailRegex.test(email)) {
            $('#emailError').text('Email không hợp lệ, vui lòng nhập đúng định dạng');
            hasError = true;
        }

        // Nếu có lỗi, dừng xử lý
        if (hasError) {
            return;
        }

        // Lấy dữ liệu từ form
        let supplierData = {
            supplierCode: $('#supplierCode').val(),
            supplierName: supplierName,
            address: address,
            phone: phone,
            email: email
        };

        // Lấy CSRF token nếu có
        const token = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
        const header = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

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
            url: '/Admin/suppliers-manager/add',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(supplierData),
            headers: headers,
            success: function(response) {
                // Đóng modal và hiển thị thông báo thành công
                $('#addSupplierModal').modal('hide');

                Swal.fire({
                    icon: 'success',
                    title: 'Thành công',
                    text: 'Thêm nhà cung cấp thành công!',
                    showConfirmButton: false,
                    timer: 1500
                }).then(() => {
                    location.reload();
                });
            },
            error: function(xhr) {
                // Xóa thông báo lỗi cũ
                $('.error').text('');

                // Hiển thị lỗi
                let errors = xhr.responseJSON;
                for (let field in errors) {
                    $('#' + field + 'Error').text(errors[field]);
                }
            }
        });
    });

    // Xử lý form sửa nhà cung cấp
    $('#editSupplierForm').submit(function(event) {
        event.preventDefault();

        // Validate trước khi gửi form
        let hasError = false;

        // Kiểm tra trường tên nhà cung cấp
        const supplierName = $('#editSupplierName').val().trim();
        if (!supplierName) {
            $('#editSupplierNameError').text('Vui lòng không để trống tên nhà cung cấp');
            hasError = true;
        } else if (supplierName.length < 2) {
            $('#editSupplierNameError').text('Tên nhà cung cấp phải có ít nhất 2 ký tự');
            hasError = true;
        } else if (!/^[A-Za-zÀ-ỹ\s]+$/.test(supplierName)) {
            $('#editSupplierNameError').text('Tên nhà cung cấp chỉ được chứa chữ cái và khoảng trắng');
            hasError = true;
        }

        // Kiểm tra trường địa chỉ
        const address = $('#editSupplierAddress').val().trim();
        if (!address) {
            $('#editAddressError').text('Vui lòng không để trống địa chỉ');
            hasError = true;
        } else if (address.length < 5) {
            $('#editAddressError').text('Địa chỉ phải có ít nhất 5 ký tự');
            hasError = true;
        }

        // Kiểm tra số điện thoại
        const phone = $('#editSupplierPhone').val().trim();
        if (!phone) {
            $('#editPhoneError').text('Vui lòng không để trống số điện thoại');
            hasError = true;
        } else if (!/^0[0-9]{9}$/.test(phone)) {
            $('#editPhoneError').text('Số điện thoại phải có 10 số và bắt đầu bằng số 0');
            hasError = true;
        }

        // Kiểm tra email
        const email = $('#editSupplierEmail').val().trim();
        const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
        if (!email) {
            $('#editEmailError').text('Vui lòng không để trống email');
            hasError = true;
        } else if (!emailRegex.test(email)) {
            $('#editEmailError').text('Email không hợp lệ, vui lòng nhập đúng định dạng');
            hasError = true;
        }

        // Nếu có lỗi, dừng xử lý
        if (hasError) {
            return;
        }

        // Lấy dữ liệu từ form
        let supplierData = {
            id: $('#editSupplierId').val(),
            supplierCode: $('#editSupplierCode').val(),
            supplierName: supplierName,
            address: address,
            phone: phone,
            email: email
        };

        // Lấy CSRF token nếu có
        const token = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
        const header = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

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
            url: '/Admin/suppliers-manager/edit',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(supplierData),
            headers: headers,
            success: function(response) {
                // Đóng modal và hiển thị thông báo thành công
                $('#editSupplierModal').modal('hide');

                Swal.fire({
                    icon: 'success',
                    title: 'Thành công',
                    text: 'Cập nhật nhà cung cấp thành công!',
                    showConfirmButton: false,
                    timer: 1500
                }).then(() => {
                    location.reload();
                });
            },
            error: function(xhr) {
                // Xóa thông báo lỗi cũ
                $('.error').text('');

                // Hiển thị lỗi
                let errors = xhr.responseJSON;
                for (let field in errors) {
                    $('#edit' + field.charAt(0).toUpperCase() + field.slice(1) + 'Error').text(errors[field]);
                }
            }
        });
    });

    // Validate input khi nhập liệu
    $('#supplierName, #editSupplierName').on('input', function() {
        const field = $(this).attr('id');
        const errorField = field === 'supplierName' ? '#supplierNameError' : '#editSupplierNameError';

        // Loại bỏ ký tự không hợp lệ
        this.value = this.value.replace(/[^A-Za-zÀ-ỹ\s]/g, '');

        // Kiểm tra và hiển thị thông báo lỗi
        if (this.value.trim() === '') {
            $(errorField).text('Vui lòng không để trống tên nhà cung cấp');
        } else if (this.value.length < 2) {
            $(errorField).text('Tên nhà cung cấp phải có ít nhất 2 ký tự');
        } else {
            $(errorField).text('');
        }
    });

    $('#address, #editSupplierAddress').on('input', function() {
        const field = $(this).attr('id');
        const errorField = field === 'address' ? '#addressError' : '#editAddressError';

        // Kiểm tra và hiển thị thông báo lỗi
        if (this.value.trim() === '') {
            $(errorField).text('Vui lòng không để trống địa chỉ');
        } else if (this.value.length < 5) {
            $(errorField).text('Địa chỉ phải có ít nhất 5 ký tự');
        } else {
            $(errorField).text('');
        }
    });

    $('#phone, #editSupplierPhone').on('input', function() {
        const field = $(this).attr('id');
        const errorField = field === 'phone' ? '#phoneError' : '#editPhoneError';

        // Loại bỏ tất cả các ký tự không phải số
        this.value = this.value.replace(/[^0-9]/g, '');

        // Đảm bảo bắt đầu bằng số 0
        if (this.value.length > 0 && this.value[0] !== '0') {
            this.value = '0' + this.value.substring(0, 9);
        }

        // Giới hạn 10 ký tự
        if (this.value.length > 10) {
            this.value = this.value.substring(0, 10);
        }

        // Kiểm tra và hiển thị thông báo lỗi
        if (this.value.trim() === '') {
            $(errorField).text('Vui lòng không để trống số điện thoại');
        } else if (this.value.length < 10) {
            $(errorField).text('Số điện thoại phải có 10 số và bắt đầu bằng số 0');
        } else {
            $(errorField).text('');
        }
    });

    $('#email, #editSupplierEmail').on('input', function() {
        const field = $(this).attr('id');
        const errorField = field === 'email' ? '#emailError' : '#editEmailError';
        const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

        // Kiểm tra và hiển thị thông báo lỗi
        if (this.value.trim() === '') {
            $(errorField).text('Vui lòng không để trống email');
        } else if (!emailRegex.test(this.value)) {
            $(errorField).text('Email không hợp lệ, vui lòng nhập đúng định dạng');
        } else {
            $(errorField).text('');
        }
    });
});