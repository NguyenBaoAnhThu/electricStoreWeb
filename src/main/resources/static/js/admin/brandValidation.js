document.addEventListener('DOMContentLoaded', function() {

    // Xử lý khi mở modal thêm thương hiệu
    const addBrandModal = document.getElementById('addBrandModal');
    if (addBrandModal) {
        addBrandModal.addEventListener('show.bs.modal', function() {
            // Lấy mã thương hiệu
            fetch('/Admin/brand-manager/generate-code')
                .then(response => response.text())
                .then(data => {
                    document.getElementById('brandCode').value = data;
                });

            // Reset form và xóa lỗi
            document.getElementById('addBrandForm').reset();
            document.querySelectorAll('.error').forEach(error => error.textContent = '');
        });
    }

    // Xử lý form thêm thương hiệu
    const addBrandForm = document.getElementById('addBrandForm');
    if (addBrandForm) {
        addBrandForm.addEventListener('submit', function(event) {
            event.preventDefault();

            let hasError = false;

            // Validate tên thương hiệu
            const brandName = document.getElementById('brandName').value.trim();
            if (!brandName) {
                document.getElementById('brandNameError').textContent = 'Vui lòng không để trống tên thương hiệu';
                hasError = true;
            } else if (brandName.length < 2 || brandName.length > 100) {
                document.getElementById('brandNameError').textContent = 'Tên thương hiệu phải từ 2 đến 100 ký tự';
                hasError = true;
            } else if (!/^[\p{L}0-9\s]+$/u.test(brandName)) {
                document.getElementById('brandNameError').textContent = 'Tên thương hiệu không được chứa ký tự đặc biệt';
                hasError = true;
            }

            // Validate xuất xứ
            const country = document.getElementById('country').value.trim();
            if (!country) {
                document.getElementById('countryError').textContent = 'Vui lòng không để trống xuất xứ';
                hasError = true;
            } else if (country.length > 100) {
                document.getElementById('countryError').textContent = 'Tên quốc gia không được dài quá 100 ký tự';
                hasError = true;
            } else if (!/^[\p{L}0-9\s]+$/u.test(country)) {
                document.getElementById('countryError').textContent = 'Tên quốc gia không được chứa ký tự đặc biệt';
                hasError = true;
            }

            if (hasError) {
                return;
            }

            // Chuẩn bị dữ liệu
            const brandData = {
                brandCode: document.getElementById('brandCode').value,
                brandName: brandName,
                country: country
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
            fetch('/Admin/brand-manager/add', {
                method: 'POST',
                headers: headers,
                body: JSON.stringify(brandData)
            })
                .then(response => {
                    if (response.ok) {
                        // Đóng modal và chuyển hướng
                        const modal = bootstrap.Modal.getInstance(document.getElementById('addBrandModal'));
                        modal.hide();
                        window.location.href = '/Admin/brand-manager?successMessage=' + encodeURIComponent('Thêm thương hiệu thành công!');
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

    // Xử lý form chỉnh sửa thương hiệu
    const editBrandForm = document.getElementById('editBrandForm');
    if (editBrandForm) {
        editBrandForm.addEventListener('submit', function(event) {
            event.preventDefault();

            let hasError = false;

            // Validate tên thương hiệu
            const brandName = document.getElementById('editBrandName').value.trim();
            if (!brandName) {
                document.getElementById('editBrandNameError').textContent = 'Vui lòng không để trống tên thương hiệu';
                hasError = true;
            } else if (brandName.length < 2 || brandName.length > 100) {
                document.getElementById('editBrandNameError').textContent = 'Tên thương hiệu phải từ 2 đến 100 ký tự';
                hasError = true;
            } else if (!/^[\p{L}0-9\s]+$/u.test(brandName)) {
                document.getElementById('editBrandNameError').textContent = 'Tên thương hiệu không được chứa ký tự đặc biệt';
                hasError = true;
            }

            // Validate xuất xứ
            const country = document.getElementById('editCountry').value.trim();
            if (!country) {
                document.getElementById('editCountryError').textContent = 'Vui lòng không để trống xuất xứ';
                hasError = true;
            } else if (country.length > 100) {
                document.getElementById('editCountryError').textContent = 'Tên quốc gia không được dài quá 100 ký tự';
                hasError = true;
            } else if (!/^[\p{L}0-9\s]+$/u.test(country)) {
                document.getElementById('editCountryError').textContent = 'Tên quốc gia không được chứa ký tự đặc biệt';
                hasError = true;
            }

            if (hasError) {
                return;
            }

            // Chuẩn bị dữ liệu
            const brandData = {
                brandID: document.getElementById('editBrandId').value,
                brandName: brandName,
                country: country
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
            fetch('/Admin/brand-manager/edit', {
                method: 'POST',
                headers: headers,
                body: JSON.stringify(brandData)
            })
                .then(response => {
                    if (response.ok) {
                        // Đóng modal và chuyển hướng
                        const modal = bootstrap.Modal.getInstance(document.getElementById('editBrandModal'));
                        modal.hide();
                        window.location.href = '/Admin/brand-manager?successMessage=' + encodeURIComponent('Chỉnh sửa thương hiệu thành công.');
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

    // Validate tên thương hiệu khi nhập
    function handleBrandNameInput(inputElement, errorSelector) {
        inputElement.addEventListener('input', function() {
            // Loại bỏ ký tự đặc biệt
            this.value = this.value.replace(/[^\p{L}0-9\s]/gu, '');

            const errorElement = document.querySelector(errorSelector);
            if (this.value.trim() === '') {
                errorElement.textContent = 'Vui lòng không để trống tên thương hiệu';
            } else if (this.value.length < 2) {
                errorElement.textContent = 'Tên thương hiệu phải từ 2 đến 100 ký tự';
            } else {
                errorElement.textContent = '';
            }
        });
    }

    // Validate xuất xứ khi nhập
    function handleCountryInput(inputElement, errorSelector) {
        inputElement.addEventListener('input', function() {
            // Loại bỏ ký tự đặc biệt
            this.value = this.value.replace(/[^\p{L}0-9\s]/gu, '');

            const errorElement = document.querySelector(errorSelector);
            if (this.value.trim() === '') {
                errorElement.textContent = 'Vui lòng không để trống xuất xứ';
            } else if (this.value.length > 100) {
                errorElement.textContent = 'Tên quốc gia không được dài quá 100 ký tự';
            } else {
                errorElement.textContent = '';
            }
        });
    }

    // Áp dụng validation cho các field
    const brandName = document.getElementById('brandName');
    const editBrandName = document.getElementById('editBrandName');
    const country = document.getElementById('country');
    const editCountry = document.getElementById('editCountry');

    if (brandName) {
        handleBrandNameInput(brandName, '#brandNameError');
    }

    if (editBrandName) {
        handleBrandNameInput(editBrandName, '#editBrandNameError');
    }

    if (country) {
        handleCountryInput(country, '#countryError');
    }

    if (editCountry) {
        handleCountryInput(editCountry, '#editCountryError');
    }

});