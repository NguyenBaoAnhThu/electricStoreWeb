document.addEventListener('DOMContentLoaded', function() {
    const phoneInputs = document.querySelectorAll("#phone, #editPhone");

    phoneInputs.forEach(input => {
        if (input) {
            input.addEventListener("input", function(e) {
                let value = e.target.value;
                value = value.replace(/\D/g, '');
                if (value.length > 0 && value[0] !== '0') {
                    value = '0' + value.replace(/^0*/, '');
                }
                if (value.length > 10) {
                    value = value.slice(0, 10);
                }
                e.target.value = value;
            });

            // Thêm validation khi paste
            input.addEventListener("paste", function(e) {
                setTimeout(() => {
                    let value = e.target.value;
                    value = value.replace(/\D/g, '');
                    if (value.length > 0 && value[0] !== '0') {
                        value = '0' + value.replace(/^0*/, '');
                    }
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

    const nameInputs = document.querySelectorAll("#supplierName, #editSupplierName");

    nameInputs.forEach(input => {
        if (input) {
            input.addEventListener("input", function(e) {
                let value = e.target.value;
                value = value.replace(/[^A-Za-zÀ-ỹ\s]/g, '');
                if (value.length > 100) {
                    value = value.slice(0, 100);
                }
                e.target.value = value;
            });
            input.addEventListener("keypress", function(e) {
                const char = String.fromCharCode(e.which);
                if (!/[A-Za-zÀ-ỹ\s]/.test(char)) {
                    e.preventDefault();
                }
            });
        }
    });
    const addressInputs = document.querySelectorAll("#address, #editAddress");

    addressInputs.forEach(input => {
        if (input) {
            input.addEventListener("input", function(e) {
                const value = e.target.value;
                if (value.length > 200) {
                    e.target.value = value.slice(0, 200);
                }
            });
        }
    });

    const emailInputs = document.querySelectorAll("#email, #editEmail");

    emailInputs.forEach(input => {
        if (input) {
            input.addEventListener("input", function(e) {
                const value = e.target.value;
                if (value.length > 100) {
                    e.target.value = value.slice(0, 100);
                }
            });
        }
    });
});