document.addEventListener('DOMContentLoaded', function() {
    const addCategoryModal = document.getElementById('addCategoryModal');
    if (addCategoryModal) {
        addCategoryModal.addEventListener('show.bs.modal', function() {
            fetch('/Admin/category-manager/next-code')
                .then(response => response.text())
                .then(data => {
                    document.getElementById('categoryCode').value = data;
                });
            document.getElementById('addCategoryForm').reset();
            document.querySelectorAll('.error').forEach(error => error.textContent = '');
        });
    }

    // Hàm validate field chung
    function validateField(inputElement, errorSelector, minLength, maxLength, regexPattern, emptyMessage, lengthMessage, formatMessage) {
        const value = inputElement.value.trim();
        const errorElement = document.querySelector(errorSelector);

        if (value === '') {
            errorElement.textContent = emptyMessage;
            return false;
        } else if (value.length < minLength || value.length > maxLength) {
            errorElement.textContent = lengthMessage;
            return false;
        } else if (!regexPattern.test(value)) {
            errorElement.textContent = formatMessage;
            return false;
        } else {
            errorElement.textContent = '';
            return true;
        }
    }

    // Validate tên danh mục khi nhập
    const categoryName = document.getElementById('categoryName');
    if (categoryName) {
        categoryName.addEventListener('input', function() {
            // Loại bỏ ký tự đặc biệt
            this.value = this.value.replace(/[^\p{L}0-9\s]/gu, '');

            validateField(
                this,
                '#categoryNameError',
                2, 100,
                /^[\p{L}0-9\s]+$/u,
                'Vui lòng không để trống tên danh mục',
                'Tên danh mục phải từ 2 đến 100 ký tự',
                'Tên danh mục không được chứa ký tự đặc biệt'
            );
        });
    }

    // Validate mô tả khi nhập
    const description = document.getElementById('description');
    if (description) {
        description.addEventListener('input', function() {
            this.value = this.value.replace(/[^\p{L}0-9\s,.]/gu, '');

            validateField(
                this,
                '#descriptionError',
                10, 1000,
                /^[\p{L}0-9\s,.]+$/u,
                'Vui lòng không để trống mô tả',
                'Mô tả phải từ 10 đến 1000 ký tự',
                'Mô tả không được chứa ký tự đặc biệt ngoại trừ dấu chấm và dấu phẩy'
            );
        });
    }

    const editCategoryName = document.getElementById('editCategoryName');
    if (editCategoryName) {
        editCategoryName.addEventListener('input', function() {
            this.value = this.value.replace(/[^\p{L}0-9\s]/gu, '');

            validateField(
                this,
                '#editCategoryNameError',
                2, 100,
                /^[\p{L}0-9\s]+$/u,
                'Vui lòng không để trống tên danh mục',
                'Tên danh mục phải từ 2 đến 100 ký tự',
                'Tên danh mục không được chứa ký tự đặc biệt'
            );
        });
    }

    // Validate form chỉnh sửa - mô tả
    const editCategoryDescription = document.getElementById('editCategoryDescription');
    if (editCategoryDescription) {
        editCategoryDescription.addEventListener('input', function() {
            this.value = this.value.replace(/[^\p{L}0-9\s,.]/gu, '');

            validateField(
                this,
                '#editDescriptionError',
                10, 1000,
                /^[\p{L}0-9\s,.]+$/u,
                'Vui lòng không để trống mô tả',
                'Mô tả phải từ 10 đến 1000 ký tự',
                'Mô tả không được chứa ký tự đặc biệt ngoại trừ dấu chấm và dấu phẩy'
            );
        });
    }

    // Xử lý submit form thêm danh mục
    const addCategoryForm = document.getElementById('addCategoryForm');
    if (addCategoryForm) {
        addCategoryForm.addEventListener('submit', function(event) {
            event.preventDefault();
            const nameValid = validateField(
                document.getElementById('categoryName'),
                '#categoryNameError',
                2, 100,
                /^[\p{L}0-9\s]+$/u,
                'Vui lòng không để trống tên danh mục',
                'Tên danh mục phải từ 2 đến 100 ký tự',
                'Tên danh mục không được chứa ký tự đặc biệt'
            );

            const descValid = validateField(
                document.getElementById('description'),
                '#descriptionError',
                10, 1000,
                /^[\p{L}0-9\s,.]+$/u,
                'Vui lòng không để trống mô tả',
                'Mô tả phải từ 10 đến 1000 ký tự',
                'Mô tả không được chứa ký tự đặc biệt ngoại trừ dấu chấm và dấu phẩy'
            );

            // Dừng nếu validation fail
            if (!nameValid || !descValid) {
                return;
            }

            // Lấy dữ liệu form
            const categoryData = {
                categoryCode: document.getElementById('categoryCode').value,
                categoryName: document.getElementById('categoryName').value.trim(),
                description: document.getElementById('description').value.trim()
            };

            // Lấy CSRF token
            const token = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
            const header = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

            const headers = {
                'Content-Type': 'application/json'
            };

            if (token && header) {
                headers[header] = token;
            }

            // Gửi request
            fetch('/Admin/category-manager/add', {
                method: 'POST',
                headers: headers,
                body: JSON.stringify(categoryData)
            })
                .then(response => {
                    if (response.ok) {
                        // Đóng modal và chuyển hướng
                        const modal = bootstrap.Modal.getInstance(document.getElementById('addCategoryModal'));
                        modal.hide();
                        window.location.href = '/Admin/category-manager?successMessage=' + encodeURIComponent('Thêm danh mục thành công!');
                    } else {
                        return response.json().then(errors => {
                            // Xóa lỗi cũ
                            document.querySelectorAll('.error').forEach(error => error.textContent = '');

                            // Hiển thị lỗi mới
                            for (let field in errors) {
                                const errorElement = document.getElementById(field + 'Error');
                                if (errorElement) {
                                    errorElement.textContent = errors[field];
                                }
                            }
                        });
                    }
                })
                .catch(error => console.error('Error:', error));
        });
    }

    // Xử lý submit form chỉnh sửa
    const editCategoryForm = document.getElementById('editCategoryForm');
    if (editCategoryForm) {
        editCategoryForm.addEventListener('submit', function(event) {
            event.preventDefault();

            // Validate tất cả trường
            const nameValid = validateField(
                document.getElementById('editCategoryName'),
                '#editCategoryNameError',
                2, 100,
                /^[\p{L}0-9\s]+$/u,
                'Vui lòng không để trống tên danh mục',
                'Tên danh mục phải từ 2 đến 100 ký tự',
                'Tên danh mục không được chứa ký tự đặc biệt'
            );

            const descValid = validateField(
                document.getElementById('editCategoryDescription'),
                '#editDescriptionError',
                10, 1000,
                /^[\p{L}0-9\s,.]+$/u,
                'Vui lòng không để trống mô tả',
                'Mô tả phải từ 10 đến 1000 ký tự',
                'Mô tả không được chứa ký tự đặc biệt ngoại trừ dấu chấm và dấu phẩy'
            );

            // Dừng nếu validation fail
            if (!nameValid || !descValid) {
                return;
            }

            // Lấy dữ liệu form
            const categoryData = {
                categoryID: document.getElementById('editCategoryId').value,
                categoryCode: document.getElementById('editCategoryCode').value,
                categoryName: document.getElementById('editCategoryName').value.trim(),
                description: document.getElementById('editCategoryDescription').value.trim()
            };

            // Lấy CSRF token
            const token = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
            const header = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

            const headers = {
                'Content-Type': 'application/json'
            };

            if (token && header) {
                headers[header] = token;
            }

            // Gửi request
            fetch('/Admin/category-manager/edit', {
                method: 'POST',
                headers: headers,
                body: JSON.stringify(categoryData)
            })
                .then(response => {
                    if (response.ok) {
                        // Đóng modal và chuyển hướng
                        const modal = bootstrap.Modal.getInstance(document.getElementById('editCategoryModal'));
                        modal.hide();
                        window.location.href = '/Admin/category-manager?successMessage=' + encodeURIComponent('Chỉnh sửa danh mục thành công.');
                    } else {
                        return response.json().then(errors => {
                            // Xóa lỗi cũ
                            document.querySelectorAll('.error').forEach(error => error.textContent = '');

                            // Hiển thị lỗi mới
                            for (let field in errors) {
                                const errorElement = document.getElementById('edit' + field.charAt(0).toUpperCase() + field.slice(1) + 'Error');
                                if (errorElement) {
                                    errorElement.textContent = errors[field];
                                }
                            }
                        });
                    }
                })
                .catch(error => console.error('Error:', error));
        });
    }

    // Kiểm tra trùng lặp tên danh mục khi thêm mới
    if (categoryName) {
        categoryName.addEventListener('blur', function() {
            const name = this.value.trim();

            if (name.length >= 2) {
                fetch(`/Admin/category-manager/check-name?categoryName=${encodeURIComponent(name)}`)
                    .then(response => response.json())
                    .then(data => {
                        if (data.exists) {
                            document.getElementById('categoryNameError').textContent = 'Tên danh mục đã tồn tại';
                        }
                    })
                    .catch(error => console.error('Error:', error));
            }
        });
    }

    // Kiểm tra trùng lặp tên danh mục khi chỉnh sửa
    if (editCategoryName) {
        editCategoryName.addEventListener('blur', function() {
            const name = this.value.trim();
            const categoryId = document.getElementById('editCategoryId').value;

            if (name.length >= 2) {
                fetch(`/Admin/category-manager/check-name?categoryName=${encodeURIComponent(name)}&id=${categoryId}`)
                    .then(response => response.json())
                    .then(data => {
                        if (data.exists) {
                            document.getElementById('editCategoryNameError').textContent = 'Tên danh mục đã tồn tại';
                        }
                    })
                    .catch(error => console.error('Error:', error));
            }
        });
    }

});