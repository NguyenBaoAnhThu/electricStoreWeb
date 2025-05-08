document.addEventListener('DOMContentLoaded', function() {
    // === VALIDATE SỐ ĐIỆN THOẠI ===
    const phoneInputs = document.querySelectorAll("#phone, #editPhone");

    phoneInputs.forEach(input => {
        if (input) {
            // Xử lý khi nhập số điện thoại
            input.addEventListener("input", function(e) {
                const value = e.target.value;

                // Chỉ cho phép số
                if (!/^\d*$/.test(value)) {
                    e.target.value = value.replace(/\D/g, '');
                }

                // Kiểm tra số đầu tiên phải là số 0
                if (value.length > 0 && value[0] !== '0') {
                    e.target.value = '0' + value.slice(1);
                }

                // Giới hạn độ dài
                if (value.length > 10) {
                    e.target.value = value.slice(0, 10);
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
                const value = e.target.value;

                // Chỉ cho phép chữ cái và khoảng trắng (không có số)
                if (!/^[A-Za-zÀ-ỹ\s]*$/.test(value)) {
                    e.target.value = value.replace(/[^A-Za-zÀ-ỹ\s]/g, '');
                }

                // Giới hạn độ dài
                if (value.length > 100) {
                    e.target.value = value.slice(0, 100);
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