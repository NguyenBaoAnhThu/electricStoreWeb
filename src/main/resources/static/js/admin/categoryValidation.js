$(document).ready(function() {
    // Khi mở modal thêm danh mục
    $('#addCategoryModal').on('show.bs.modal', function() {
        $.ajax({
            url: '/Admin/category-manager/next-code',
            type: 'GET',
            success: function(response) {
                $('#categoryCode').val(response);
            }
        });

        // Reset form và xóa thông báo lỗi
        $('#addCategoryForm')[0].reset();
        $('.error').text('');
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

    // Validate tên danh mục khi nhập
    $('#categoryName').on('input', function() {
        // Loại bỏ ký tự đặc biệt khi nhập
        this.value = this.value.replace(/[^\p{L}0-9\s]/gu, '');

        validateField(
            $(this),
            '#categoryNameError',
            2, 100,
            /^[\p{L}0-9\s]+$/u,
            'Vui lòng không để trống tên danh mục',
            'Tên danh mục phải từ 2 đến 100 ký tự',
            'Tên danh mục không được chứa ký tự đặc biệt'
        );
    });

    // Validate mô tả khi nhập
    $('#description').on('input', function() {
        // Loại bỏ ký tự đặc biệt khi nhập
        this.value = this.value.replace(/[^\p{L}0-9\s,.]/gu, '');

        validateField(
            $(this),
            '#descriptionError',
            10, 1000,
            /^[\p{L}0-9\s,.]+$/u,
            'Vui lòng không để trống mô tả',
            'Mô tả phải từ 10 đến 1000 ký tự',
            'Mô tả không được chứa ký tự đặc biệt ngoại trừ dấu chấm và dấu phẩy'
        );
    });

    // Tương tự cho form chỉnh sửa
    $('#editCategoryName').on('input', function() {
        this.value = this.value.replace(/[^\p{L}0-9\s]/gu, '');

        validateField(
            $(this),
            '#editCategoryNameError',
            2, 100,
            /^[\p{L}0-9\s]+$/u,
            'Vui lòng không để trống tên danh mục',
            'Tên danh mục phải từ 2 đến 100 ký tự',
            'Tên danh mục không được chứa ký tự đặc biệt'
        );
    });

    $('#editCategoryDescription').on('input', function() {
        this.value = this.value.replace(/[^\p{L}0-9\s,.]/gu, '');

        validateField(
            $(this),
            '#editDescriptionError',
            10, 1000,
            /^[\p{L}0-9\s,.]+$/u,
            'Vui lòng không để trống mô tả',
            'Mô tả phải từ 10 đến 1000 ký tự',
            'Mô tả không được chứa ký tự đặc biệt ngoại trừ dấu chấm và dấu phẩy'
        );
    });

    // Thêm hiệu ứng kiểm tra khi người dùng submit form thêm mới
    $('#addCategoryForm').on('submit', function(event) {
        event.preventDefault();

        // Validate tất cả trường dữ liệu
        let nameValid = validateField(
            $('#categoryName'),
            '#categoryNameError',
            2, 100,
            /^[\p{L}0-9\s]+$/u,
            'Vui lòng không để trống tên danh mục',
            'Tên danh mục phải từ 2 đến 100 ký tự',
            'Tên danh mục không được chứa ký tự đặc biệt'
        );

        let descValid = validateField(
            $('#description'),
            '#descriptionError',
            10, 1000,
            /^[\p{L}0-9\s,.]+$/u,
            'Vui lòng không để trống mô tả',
            'Mô tả phải từ 10 đến 1000 ký tự',
            'Mô tả không được chứa ký tự đặc biệt ngoại trừ dấu chấm và dấu phẩy'
        );

        // Nếu dữ liệu không hợp lệ, dừng xử lý
        if (!nameValid || !descValid) {
            return;
        }

        // Lấy dữ liệu từ form
        let categoryData = {
            categoryCode: $('#categoryCode').val(),
            categoryName: $('#categoryName').val().trim(),
            description: $('#description').val().trim()
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
            url: '/Admin/category-manager/add',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(categoryData),
            headers: headers,
            success: function(response) {
                $('#addCategoryModal').modal('hide');

                // Thay thế SweetAlert2 bằng chuyển hướng có tham số thông báo
                window.location.href = '/Admin/category-manager?successMessage=' + encodeURIComponent('Thêm danh mục thành công!');
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

    // Xử lý form chỉnh sửa
    $('#editCategoryForm').on('submit', function(event) {
        event.preventDefault();

        // Validate tất cả trường dữ liệu
        let nameValid = validateField(
            $('#editCategoryName'),
            '#editCategoryNameError',
            2, 100,
            /^[\p{L}0-9\s]+$/u,
            'Vui lòng không để trống tên danh mục',
            'Tên danh mục phải từ 2 đến 100 ký tự',
            'Tên danh mục không được chứa ký tự đặc biệt'
        );

        let descValid = validateField(
            $('#editCategoryDescription'),
            '#editDescriptionError',
            10, 1000,
            /^[\p{L}0-9\s,.]+$/u,
            'Vui lòng không để trống mô tả',
            'Mô tả phải từ 10 đến 1000 ký tự',
            'Mô tả không được chứa ký tự đặc biệt ngoại trừ dấu chấm và dấu phẩy'
        );

        // Nếu dữ liệu không hợp lệ, dừng xử lý
        if (!nameValid || !descValid) {
            return;
        }

        // Lấy dữ liệu từ form
        let categoryData = {
            categoryID: $('#editCategoryId').val(),
            categoryCode: $('#editCategoryCode').val(),
            categoryName: $('#editCategoryName').val().trim(),
            description: $('#editCategoryDescription').val().trim()
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
            url: '/Admin/category-manager/edit',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(categoryData),
            headers: headers,
            success: function(response) {
                $('#editCategoryModal').modal('hide');

                // Thay thế SweetAlert2 bằng chuyển hướng có tham số thông báo
                window.location.href = '/Admin/category-manager?successMessage=' + encodeURIComponent('Chỉnh sửa danh mục thành công.');
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

    // Kiểm tra trùng lặp tên danh mục
    $('#categoryName').on('blur', function() {
        const categoryName = $(this).val().trim();

        if (categoryName.length >= 2) {
            $.ajax({
                url: '/Admin/category-manager/check-name',
                type: 'GET',
                data: { categoryName: categoryName },
                success: function(response) {
                    if (response.exists) {
                        $('#categoryNameError').text('Tên danh mục đã tồn tại');
                    }
                }
            });
        }
    });

    // Kiểm tra trùng lặp tên danh mục khi chỉnh sửa
    $('#editCategoryName').on('blur', function() {
        const categoryName = $(this).val().trim();
        const categoryId = $('#editCategoryId').val();

        if (categoryName.length >= 2) {
            $.ajax({
                url: '/Admin/category-manager/check-name',
                type: 'GET',
                data: {
                    categoryName: categoryName,
                    id: categoryId
                },
                success: function(response) {
                    if (response.exists) {
                        $('#editCategoryNameError').text('Tên danh mục đã tồn tại');
                    }
                }
            });
        }
    });
});