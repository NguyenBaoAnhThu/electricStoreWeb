document.addEventListener('DOMContentLoaded', function() {
    // === VALIDATE SỐ ĐIỆN THOẠI ===
    const phoneInputs = document.querySelectorAll("#phone, #editPhone");

    phoneInputs.forEach(input => {
        if (input) {
            // Xử lý khi nhập số điện thoại
            input.addEventListener("input", function(e) {
                let value = e.target.value;

                // BƯỚC 1: Loại bỏ TẤT CẢ ký tự không phải số
                value = value.replace(/\D/g, '');

                // BƯỚC 2: Kiểm tra số đầu tiên phải là số 0
                if (value.length > 0 && value[0] !== '0') {
                    value = '0' + value.replace(/^0*/, ''); // Loại bỏ các số 0 đầu thừa và thêm 1 số 0
                }

                // BƯỚC 3: Giới hạn độ dài tối đa 10 số
                if (value.length > 10) {
                    value = value.slice(0, 10);
                }

                // Cập nhật giá trị đã được làm sạch
                e.target.value = value;
            });

            // Thêm validation khi paste
            input.addEventListener("paste", function(e) {
                // Delay để đợi paste hoàn tất
                setTimeout(() => {
                    let value = e.target.value;

                    // Loại bỏ tất cả ký tự không phải số
                    value = value.replace(/\D/g, '');

                    // Kiểm tra số đầu tiên phải là số 0
                    if (value.length > 0 && value[0] !== '0') {
                        value = '0' + value.replace(/^0*/, '');
                    }

                    // Giới hạn độ dài
                    if (value.length > 10) {
                        value = value.slice(0, 10);
                    }

                    e.target.value = value;
                }, 10);
            });

            // Ngăn không cho nhập ký tự không phải số bằng keypress
            input.addEventListener("keypress", function(e) {
                // Cho phép: backspace, delete, tab, escape, enter
                if ([8, 9, 27, 13, 46].indexOf(e.keyCode) !== -1 ||
                    // Cho phép Ctrl+A, Ctrl+C, Ctrl+V, Ctrl+X
                    (e.keyCode === 65 && e.ctrlKey === true) ||
                    (e.keyCode === 67 && e.ctrlKey === true) ||
                    (e.keyCode === 86 && e.ctrlKey === true) ||
                    (e.keyCode === 88 && e.ctrlKey === true)) {
                    return;
                }

                // Chỉ cho phép số (0-9)
                if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
                    e.preventDefault();
                }
            });
        }
    });

    // === VALIDATE TÊN NHÀ CUNG CẤP ===
    const nameInputs = document.querySelectorAll("#supplierName, #editSupplierName");

    nameInputs.forEach(input => {
        if (input) {
            // Xử lý khi nhập tên nhà cung cấp
            input.addEventListener("input", function(e) {
                let value = e.target.value;

                // Chỉ cho phép chữ cái, khoảng trắng và dấu tiếng Việt
                value = value.replace(/[^A-Za-zÀ-ỹ\s]/g, '');

                // Giới hạn độ dài
                if (value.length > 100) {
                    value = value.slice(0, 100);
                }

                e.target.value = value;
            });

            // Ngăn nhập ký tự không hợp lệ
            input.addEventListener("keypress", function(e) {
                const char = String.fromCharCode(e.which);
                if (!/[A-Za-zÀ-ỹ\s]/.test(char)) {
                    e.preventDefault();
                }
            });
        }
    });

    // === VALIDATE ĐỊA CHỈ ===
    const addressInputs = document.querySelectorAll("#address, #editAddress");

    addressInputs.forEach(input => {
        if (input) {
            // Xử lý khi nhập địa chỉ
            input.addEventListener("input", function(e) {
                const value = e.target.value;

                // Giới hạn độ dài
                if (value.length > 200) {
                    e.target.value = value.slice(0, 200);
                }
            });
        }
    });

    // === VALIDATE EMAIL ===
    const emailInputs = document.querySelectorAll("#email, #editEmail");

    emailInputs.forEach(input => {
        if (input) {
            // Xử lý khi nhập email
            input.addEventListener("input", function(e) {
                const value = e.target.value;

                // Giới hạn độ dài
                if (value.length > 100) {
                    e.target.value = value.slice(0, 100);
                }
            });
        }
    });

    // === VALIDATE FORM TRƯỚC KHI SUBMIT ===
    const addSupplierForm = document.getElementById('addSupplierForm');
    const editSupplierForm = document.getElementById('editSupplierForm');

    // Xử lý khi submit form - giữ lại những phần này nếu cần thiết
    if (addSupplierForm) {
        addSupplierForm.addEventListener('submit', function(e) {
            // Validation server-side
        });
    }

    if (editSupplierForm) {
        editSupplierForm.addEventListener('submit', function(e) {
            // Validation server-side
        });
    }
});