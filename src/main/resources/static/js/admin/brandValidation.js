$(document).ready(function() {
    // Khi mở modal thêm thương hiệu
    $('#addBrandModal').on('show.bs.modal', function() {
        $.ajax({
            url: '/Admin/brand-manager/generate-code',
            type: 'GET',
            success: function(response) {
                $('#brandCode').val(response);
            }
        });

        // Reset form và xóa thông báo lỗi
        $('#addBrandForm')[0].reset();
        $('.error').text('');
    });

    // Xử lý form thêm thương hiệu
    $('#addBrandForm').submit(function(event) {
        event.preventDefault();

        // Validate trước khi gửi form
        let hasError = false;

        // Kiểm tra trường tên thương hiệu
        const brandName = $('#brandName').val().trim();
        if (!brandName) {
            $('#brandNameError').text('Vui lòng không để trống tên thương hiệu');
            hasError = true;
        } else if (brandName.length < 2 || brandName.length > 100) {
            $('#brandNameError').text('Tên thương hiệu phải từ 2 đến 100 ký tự');
            hasError = true;
        } else if (!/^[\p{L}0-9\s]+$/u.test(brandName)) {
            $('#brandNameError').text('Tên thương hiệu không được chứa ký tự đặc biệt');
            hasError = true;
        }

        // Kiểm tra trường xuất xứ
        const country = $('#country').val().trim();
        if (!country) {
            $('#countryError').text('Vui lòng không để trống xuất xứ');
            hasError = true;
        } else if (country.length > 100) {
            $('#countryError').text('Tên quốc gia không được dài quá 100 ký tự');
            hasError = true;
        } else if (!/^[\p{L}0-9\s]+$/u.test(country)) {
            $('#countryError').text('Tên quốc gia không được chứa ký tự đặc biệt');
            hasError = true;
        }

        // Nếu có lỗi, dừng xử lý
        if (hasError) {
            return;
        }

        // Lấy dữ liệu từ form
        let brandData = {
            brandCode: $('#brandCode').val(),
            brandName: brandName,
            country: country
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

        // Gửi request Ajax
        $.ajax({
            url: '/Admin/brand-manager/add',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(brandData),
            headers: headers,
            success: function(response) {
                $('#addBrandModal').modal('hide');

                // Chuyển hướng với tham số thông báo thành công
                window.location.href = '/Admin/brand-manager?successMessage=' + encodeURIComponent('Thêm thương hiệu thành công!');
            },
            error: function(xhr) {
                $('.error').text('');

                let errors = xhr.responseJSON;
                for (let field in errors) {
                    $('#' + field + 'Error').text(errors[field]);
                }
            }
        });
    });

    // Tương tự cho form sửa
    $('#editBrandForm').submit(function(event) {
        event.preventDefault();

        // Validate trước khi gửi form
        let hasError = false;

        // Kiểm tra trường tên thương hiệu
        const brandName = $('#editBrandName').val().trim();
        if (!brandName) {
            $('#editBrandNameError').text('Vui lòng không để trống tên thương hiệu');
            hasError = true;
        } else if (brandName.length < 2 || brandName.length > 100) {
            $('#editBrandNameError').text('Tên thương hiệu phải từ 2 đến 100 ký tự');
            hasError = true;
        } else if (!/^[\p{L}0-9\s]+$/u.test(brandName)) {
            $('#editBrandNameError').text('Tên thương hiệu không được chứa ký tự đặc biệt');
            hasError = true;
        }

        // Kiểm tra trường xuất xứ
        const country = $('#editCountry').val().trim();
        if (!country) {
            $('#editCountryError').text('Vui lòng không để trống xuất xứ');
            hasError = true;
        } else if (country.length > 100) {
            $('#editCountryError').text('Tên quốc gia không được dài quá 100 ký tự');
            hasError = true;
        } else if (!/^[\p{L}0-9\s]+$/u.test(country)) {
            $('#editCountryError').text('Tên quốc gia không được chứa ký tự đặc biệt');
            hasError = true;
        }

        // Nếu có lỗi, dừng xử lý
        if (hasError) {
            return;
        }

        // Lấy dữ liệu từ form
        let brandData = {
            brandID: $('#editBrandId').val(),
            brandName: brandName,
            country: country
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

        // Gửi request Ajax
        $.ajax({
            url: '/Admin/brand-manager/edit',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(brandData),
            headers: headers,
            success: function(response) {
                $('#editBrandModal').modal('hide');

                // Chuyển hướng với tham số thông báo thành công
                window.location.href = '/Admin/brand-manager?successMessage=' + encodeURIComponent('Chỉnh sửa thương hiệu thành công.');
            },
            error: function(xhr) {
                $('.error').text('');

                let errors = xhr.responseJSON;
                for (let field in errors) {
                    $('#edit' + field.charAt(0).toUpperCase() + field.slice(1) + 'Error').text(errors[field]);
                }
            }
        });
    });

    // Validate khi nhập liệu
    $('#brandName, #editBrandName').on('input', function() {
        const field = $(this).attr('id');
        const errorField = field === 'brandName' ? '#brandNameError' : '#editBrandNameError';

        // Loại bỏ ký tự đặc biệt khi nhập
        this.value = this.value.replace(/[^\p{L}0-9\s]/gu, '');

        // Kiểm tra và hiển thị thông báo lỗi
        if (this.value.trim() === '') {
            $(errorField).text('Vui lòng không để trống tên thương hiệu');
        } else if (this.value.length < 2) {
            $(errorField).text('Tên thương hiệu phải từ 2 đến 100 ký tự');
        } else {
            $(errorField).text('');
        }
    });

    $('#country, #editCountry').on('input', function() {
        const field = $(this).attr('id');
        const errorField = field === 'country' ? '#countryError' : '#editCountryError';

        // Loại bỏ ký tự đặc biệt khi nhập
        this.value = this.value.replace(/[^\p{L}0-9\s]/gu, '');

        // Kiểm tra và hiển thị thông báo lỗi
        if (this.value.trim() === '') {
            $(errorField).text('Vui lòng không để trống xuất xứ');
        } else if (this.value.length > 100) {
            $(errorField).text('Tên quốc gia không được dài quá 100 ký tự');
        } else {
            $(errorField).text('');
        }
    });
});