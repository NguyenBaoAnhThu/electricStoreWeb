document.addEventListener('DOMContentLoaded', function() {
    const ERROR_MESSAGES = {
        REQUIRED: {
            name: 'Tên không được để trống',
            phone: 'Số điện thoại không được để trống',
            address: 'Địa chỉ không được để trống',
            email: 'Email không được để trống',
            payment: 'Vui lòng chọn phương thức thanh toán',
            birthdate: 'Ngày sinh không được để trống'
        },
        FORMAT: {
            phone: 'Số điện thoại phải bắt đầu bằng +84 hoặc 0, theo sau là 3,5,7,8,9 và 8 chữ số',
            email: 'Email không hợp lệ, vui lòng nhập đúng định dạng',
            name: 'Tên chỉ được chứa chữ cái và khoảng trắng',
            address: 'Địa chỉ chỉ được chứa chữ cái, số, khoảng trắng và các ký tự đặc biệt cho phép',
            birthdate: 'Không đủ điều kiện, tuổi phải lớn hơn 15.'
        },
        LENGTH: {
            name: 'Tên phải có độ dài từ 2 đến 50 ký tự',
            phone: 'Số điện thoại phải có độ dài từ 10 đến 12 ký tự',
            address: 'Địa chỉ không được vượt quá 200 ký tự',
            email: 'Email phải có độ dài từ 5 đến 100 ký tự'
        },
        DUPLICATE: {
            phone: 'Số điện thoại đã tồn tại.',
            email: 'Email đã tồn tại.'
        }
    };

    // === FUNCTION VALIDATION ===
    function showError(fieldId, message) {
        const errorElement = document.getElementById(fieldId + 'Error');
        if (errorElement) {
            errorElement.textContent = message;
            errorElement.style.display = 'block';
        } else {
            // Nếu không tìm thấy element có sẵn, tạo một element mới
            const inputElement = document.getElementById(fieldId);
            if (inputElement) {
                // Xóa thông báo lỗi cũ nếu có
                const existingError = inputElement.parentNode.querySelector('.error');
                if (existingError) {
                    existingError.remove();
                }

                // Tạo thông báo lỗi mới
                const errorDiv = document.createElement('p');
                errorDiv.className = 'error text-danger';
                errorDiv.textContent = message;
                errorDiv.style.display = 'block';

                // Thêm vào sau input
                inputElement.parentNode.appendChild(errorDiv);
            }
        }
    }

    function clearError(fieldId) {
        const errorElement = document.getElementById(fieldId + 'Error');
        if (errorElement) {
            errorElement.textContent = '';
            errorElement.style.display = 'none';
        } else {
            // Xóa thông báo lỗi nếu không tìm thấy phần tử lỗi chuyên dụng
            const inputElement = document.getElementById(fieldId);
            if (inputElement) {
                const existingError = inputElement.parentNode.querySelector('.error');
                if (existingError) {
                    existingError.remove();
                }
            }
        }
    }

    // === VALIDATE TÊN KHÁCH HÀNG ===
    function validateName(input) {
        const value = input.value.trim();

        // Kiểm tra trống
        if (value === '') {
            showError('customerName', ERROR_MESSAGES.REQUIRED.name);
            return false;
        }

        // Kiểm tra độ dài
        if (value.length < 2 || value.length > 50) {
            showError('customerName', ERROR_MESSAGES.LENGTH.name);
            return false;
        }

        // Kiểm tra định dạng
        if (!/^[A-Za-zÀ-ỹ\s]*$/.test(value)) {
            showError('customerName', ERROR_MESSAGES.FORMAT.name);
            return false;
        }

        clearError('customerName');
        return true;
    }

    // === VALIDATE SỐ ĐIỆN THOẠI ===
    async function validatePhone(input) {
        const value = input.value.trim();

        // Kiểm tra trống
        if (value === '') {
            showError('customerPhoneNumber', ERROR_MESSAGES.REQUIRED.phone);
            return false;
        }

        // Kiểm tra độ dài
        if (value.length < 10 || value.length > 12) {
            showError('customerPhoneNumber', ERROR_MESSAGES.LENGTH.phone);
            return false;
        }

        // Kiểm tra định dạng
        const phoneRegex = /^(\+84|0)[35789][0-9]{8}$/;
        if (!phoneRegex.test(value)) {
            showError('customerPhoneNumber', ERROR_MESSAGES.FORMAT.phone);
            return false;
        }

        try {
            // Kiểm tra trùng lặp
            const response = await fetch(`/api/customers/check-phone?phone=${encodeURIComponent(value)}`);
            const data = await response.json();
            if (data.exists) {
                showError('customerPhoneNumber', ERROR_MESSAGES.DUPLICATE.phone);
                return false;
            }
        } catch (error) {
            console.error('Error checking phone:', error);
        }

        clearError('customerPhoneNumber');
        return true;
    }

    // === VALIDATE ĐỊA CHỈ ===
    function validateAddress(input) {
        const value = input.value.trim();

        // Kiểm tra trống
        if (value === '') {
            showError('customerAddress', ERROR_MESSAGES.REQUIRED.address);
            return false;
        }

        // Kiểm tra độ dài
        if (value.length > 200) {
            showError('customerAddress', ERROR_MESSAGES.LENGTH.address);
            return false;
        }

        // Kiểm tra định dạng
        if (!/^[A-Za-z0-9À-ỹ,\s.-]*$/.test(value)) {
            showError('customerAddress', ERROR_MESSAGES.FORMAT.address);
            return false;
        }

        clearError('customerAddress');
        return true;
    }

    // === VALIDATE EMAIL ===
    async function validateEmail(input) {
        const value = input.value.trim();

        // Kiểm tra trống
        if (value === '') {
            showError('customerEmail', ERROR_MESSAGES.REQUIRED.email);
            return false;
        }

        // Kiểm tra độ dài
        if (value.length < 5 || value.length > 100) {
            showError('customerEmail', ERROR_MESSAGES.LENGTH.email);
            return false;
        }

        // Kiểm tra định dạng
        const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
        if (!emailRegex.test(value)) {
            showError('customerEmail', ERROR_MESSAGES.FORMAT.email);
            return false;
        }

        try {
            // Kiểm tra trùng lặp
            const response = await fetch(`/api/customers/check-email?email=${encodeURIComponent(value)}`);
            if (response.ok) {
                const data = await response.json();
                if (data.exists) {
                    showError('customerEmail', ERROR_MESSAGES.DUPLICATE.email);
                    return false;
                }
            } else {
                // Nếu lỗi server, chỉ log ra console
                console.error('Lỗi khi kiểm tra email:', response.statusText);
            }
        } catch (error) {
            console.error('Error checking email:', error);
        }

        clearError('customerEmail');
        return true;
    }

    // === VALIDATE NGÀY SINH ===
    function validateBirthdate(input) {
        const value = input.value;

        // Kiểm tra trống
        if (!value) {
            showError('customerBirthdate', ERROR_MESSAGES.REQUIRED.birthdate);
            return false;
        }

        // Kiểm tra tuổi
        const birthDate = new Date(value);
        const today = new Date();
        const ageDate = new Date(today - birthDate);
        const age = Math.abs(ageDate.getUTCFullYear() - 1970);

        if (age < 15) {
            showError('customerBirthdate', ERROR_MESSAGES.FORMAT.birthdate);
            return false;
        }

        clearError('customerBirthdate');
        return true;
    }

    // === VALIDATE PHƯƠNG THỨC THANH TOÁN ===
    function validatePaymentMethod(input) {
        const value = input.value;

        if (value === '0') {
            showError('paymentMethod', ERROR_MESSAGES.REQUIRED.payment);
            return false;
        }

        clearError('paymentMethod');
        return true;
    }

    // === VALIDATE SẢN PHẨM TRONG ĐƠN HÀNG ===
    function validateProducts() {
        const productRows = document.querySelectorAll('#productList tr.product-row');

        if (productRows.length === 0) {
            // Hiển thị thông báo lỗi nếu không có sản phẩm
            const noProductsMessage = document.getElementById('noProductsMessage');
            if (noProductsMessage) {
                noProductsMessage.innerHTML = '<span class="text-danger">Vui lòng thêm ít nhất một sản phẩm</span>';
                noProductsMessage.style.display = 'block';
            }
            return false;
        }

        return true;
    }

    // === XỬ LÝ INPUT SỐ ĐIỆN THOẠI ===
    const phoneInputs = document.querySelectorAll("#customerPhoneNumber");
    phoneInputs.forEach(input => {
        if (input) {
            // Xử lý khi nhập số điện thoại
            input.addEventListener("input", function(e) {
                const value = e.target.value;
                // Chỉ cho phép số và dấu +
                if (!/^[+\d]*$/.test(value)) {
                    e.target.value = value.replace(/[^+\d]/g, '');
                }
                // Giới hạn độ dài
                if (value.length > 12) {
                    e.target.value = value.slice(0, 12);
                }
            });

            // Xử lý khi paste số điện thoại
            input.addEventListener('paste', function(e) {
                setTimeout(() => {
                    const currentValue = this.value;
                    // Lọc ký tự không phải số và dấu +
                    const filteredValue = currentValue.replace(/[^+\d]/g, '');
                    // Giới hạn độ dài
                    if (filteredValue.length > 12) {
                        this.value = filteredValue.substring(0, 12);
                    } else {
                        this.value = filteredValue;
                    }
                }, 0);
            });
        }
    });

    // === XỬ LÝ INPUT TÊN KHÁCH HÀNG ===
    const nameInputs = document.querySelectorAll("#customerName");
    nameInputs.forEach(input => {
        if (input) {
            // Xử lý khi nhập tên khách hàng
            input.addEventListener("input", function(e) {
                const value = e.target.value;
                // Chỉ cho phép chữ cái và khoảng trắng
                if (!/^[A-Za-zÀ-ỹ\s]*$/.test(value)) {
                    e.target.value = value.replace(/[^A-Za-zÀ-ỹ\s]/g, '');
                }
                // Giới hạn độ dài
                if (value.length > 50) {
                    e.target.value = value.slice(0, 50);
                }
            });

            // Xử lý khi paste tên khách hàng
            input.addEventListener('paste', function(e) {
                setTimeout(() => {
                    const MAX_LENGTH = 50;
                    const currentValue = this.value;
                    // Lọc ký tự không hợp lệ
                    const filteredValue = currentValue.replace(/[^A-Za-zÀ-ỹ\s]/g, '');
                    // Giới hạn độ dài
                    if (filteredValue.length > MAX_LENGTH) {
                        this.value = filteredValue.substring(0, MAX_LENGTH);
                    } else {
                        this.value = filteredValue;
                    }
                }, 0);
            });

            // Validate khi blur
            input.addEventListener('blur', function() {
                validateName(this);
            });
        }
    });

    // === XỬ LÝ INPUT ĐỊA CHỈ KHÁCH HÀNG ===
    const addressInputs = document.querySelectorAll("#customerAddress");
    addressInputs.forEach(input => {
        if (input) {
            // Xử lý khi nhập địa chỉ
            input.addEventListener("input", function(e) {
                const value = e.target.value;
                // Chỉ cho phép chữ cái, số, khoảng trắng và các ký tự đặc biệt cho phép
                if (!/^[A-Za-z0-9À-ỹ,\s.-]*$/.test(value)) {
                    e.target.value = value.replace(/[^A-Za-z0-9À-ỹ,\s.-]/g, '');
                }
                // Giới hạn độ dài
                if (value.length > 200) {
                    e.target.value = value.slice(0, 200);
                }
            });

            // Xử lý khi paste địa chỉ
            input.addEventListener('paste', function(e) {
                setTimeout(() => {
                    const MAX_LENGTH = 200;
                    const currentValue = this.value;
                    // Lọc ký tự không hợp lệ
                    const filteredValue = currentValue.replace(/[^A-Za-z0-9À-ỹ,\s.-]/g, '');
                    // Giới hạn độ dài
                    if (filteredValue.length > MAX_LENGTH) {
                        this.value = filteredValue.substring(0, MAX_LENGTH);
                    } else {
                        this.value = filteredValue;
                    }
                }, 0);
            });

            // Validate khi blur
            input.addEventListener('blur', function() {
                validateAddress(this);
            });
        }
    });

    // === XỬ LÝ INPUT EMAIL KHÁCH HÀNG ===
    const emailInputs = document.querySelectorAll("#customerEmail");
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

    // === XỬ LÝ INPUT NGÀY SINH KHÁCH HÀNG ===
    const birthdayInputs = document.querySelectorAll("#customerBirthdate");
    birthdayInputs.forEach(input => {
        if (input) {
            // Validate khi change
            input.addEventListener("change", function() {
                validateBirthdate(this);
            });
        }
    });

    // === XỬ LÝ PHƯƠNG THỨC THANH TOÁN ===
    const paymentMethodInput = document.getElementById('paymentMethod');
    if (paymentMethodInput) {
        paymentMethodInput.addEventListener('change', function() {
            validatePaymentMethod(this);
        });
    }

    // === VALIDATE FORM TRƯỚC KHI SUBMIT ===
    const orderForm = document.getElementById('orderForm');

    // Xoá tất cả lỗi khi mở form
    function clearAllErrors() {
        document.querySelectorAll('.error').forEach(error => {
            error.textContent = '';
            error.style.display = 'none';
        });
    }

    // Validate form khi submit
    async function validateOrderForm(form) {
        // Xoá tất cả lỗi trước khi validate
        clearAllErrors();

        // Khởi tạo biến kiểm tra valid
        let isValid = true;

        // Kiểm tra thông tin khách hàng chỉ khi không chọn khách hàng từ danh sách
        const customerId = document.getElementById('customerId').value;
        if (!customerId) {
            // Lấy các trường input
            const nameInput = document.getElementById('customerName');
            const phoneInput = document.getElementById('customerPhoneNumber');
            const addressInput = document.getElementById('customerAddress');
            const emailInput = document.getElementById('customerEmail');
            const birthdateInput = document.getElementById('customerBirthdate');

            // Validate theo thứ tự ưu tiên: Tên -> SĐT -> Địa chỉ -> Email -> Ngày sinh
            if (nameInput && !validateName(nameInput)) {
                isValid = false;
            }

            if (phoneInput && !(await validatePhone(phoneInput))) {
                isValid = false;
            }

            if (addressInput && !validateAddress(addressInput)) {
                isValid = false;
            }

            if (emailInput && !(await validateEmail(emailInput))) {
                isValid = false;
            }

            if (birthdateInput && !validateBirthdate(birthdateInput)) {
                isValid = false;
            }
        }

        // Kiểm tra phương thức thanh toán
        const paymentMethodInput = document.getElementById('paymentMethod');
        if (paymentMethodInput && !validatePaymentMethod(paymentMethodInput)) {
            isValid = false;
        }

        // Kiểm tra sản phẩm
        if (!validateProducts()) {
            isValid = false;
        }

        return isValid;
    }

    // Áp dụng xác thực khi submit form
    if (orderForm) {
        // Thêm xử lý submit form
        const originalSubmit = orderForm.onsubmit;

        orderForm.addEventListener('submit', async function(e) {
            if (!(await validateOrderForm(this))) {
                e.preventDefault();
                e.stopPropagation();
                return false;
            }

            // Gọi xử lý submit gốc nếu có
            if (typeof originalSubmit === 'function') {
                return originalSubmit.call(this, e);
            }
        });
    }

    // === XỬ LÝ NÚT THÊM SẢN PHẨM ===
    const addProductBtn = document.getElementById('addProductButton');
    if (addProductBtn) {
        addProductBtn.addEventListener('click', function() {
            // Kiểm tra thông tin khách hàng trước khi thêm sản phẩm
            verifyCustomerInfo();
        });
    }

    // Hàm kiểm tra thông tin khách hàng trước khi thêm sản phẩm
    function verifyCustomerInfo() {
        // Xoá các thông báo lỗi hiện tại
        clearAllErrors();

        // Kiểm tra nếu đã có customerId thì cho phép thêm sản phẩm
        const customerId = document.getElementById('customerId').value;
        if (customerId && customerId.trim() !== '') {
            // Đã chọn khách hàng có sẵn, gọi hàm selectProduct
            if (typeof window.selectProduct === 'function') {
                window.selectProduct();
            }
            return;
        }

        // Kiểm tra thông tin khách hàng nếu nhập mới
        const nameInput = document.getElementById('customerName');
        const phoneInput = document.getElementById('customerPhoneNumber');
        const addressInput = document.getElementById('customerAddress');
        const emailInput = document.getElementById('customerEmail');

        let isValid = true;

        // Kiểm tra từng trường theo thứ tự ưu tiên
        if (nameInput && !validateName(nameInput)) {
            isValid = false;
        }

        if (phoneInput && !validatePhone(phoneInput)) {
            isValid = false;
        }

        if (addressInput && !validateAddress(addressInput)) {
            isValid = false;
        }

        if (emailInput && !validateEmail(emailInput)) {
            isValid = false;
        }

        if (!isValid) {
            // Hiển thị modal thông báo
            showCustomerRequiredModal();
            return;
        }

        // Thông tin khách hàng hợp lệ, gọi hàm selectProduct
        if (typeof window.selectProduct === 'function') {
            window.selectProduct();
        }
    }

    // Hàm hiển thị modal yêu cầu chọn khách hàng
    function showCustomerRequiredModal() {
        // Kiểm tra xem modal đã tồn tại trong DOM chưa
        let customerRequiredModal = document.getElementById('customerRequiredModal');

        if (customerRequiredModal) {
            // Hiển thị modal
            const bsModal = new bootstrap.Modal(customerRequiredModal);

            // Thêm sự kiện 'hidden.bs.modal' để đảm bảo xóa backdrop
            customerRequiredModal.addEventListener('hidden.bs.modal', function () {
                // Xóa tất cả modal backdrops còn sót lại
                const backdrops = document.querySelectorAll('.modal-backdrop');
                backdrops.forEach(backdrop => {
                    backdrop.remove();
                });

                // Xóa class 'modal-open' khỏi body nếu còn
                document.body.classList.remove('modal-open');
                document.body.style.overflow = '';
                document.body.style.paddingRight = '';
            }, { once: true });

            bsModal.show();
        }
    }

// Ngoài ra, cần cập nhật event listener cho nút "Đóng" trong modal
    document.addEventListener('DOMContentLoaded', function() {
        // Tìm nút đóng trong modal
        const closeButton = document.querySelector('#customerRequiredModal button[data-bs-dismiss="modal"]');

        if (closeButton) {
            closeButton.addEventListener('click', function() {
                // Xóa tất cả modal backdrops còn sót lại
                const backdrops = document.querySelectorAll('.modal-backdrop');
                backdrops.forEach(backdrop => {
                    backdrop.remove();
                });

                // Xóa class 'modal-open' khỏi body
                document.body.classList.remove('modal-open');
                document.body.style.overflow = '';
                document.body.style.paddingRight = '';
            });
        }
    });
});

// === XỬ LÝ PHẦN TRĂM GIẢM GIÁ ===
document.addEventListener('DOMContentLoaded', function() {
    const discountPercentInput = document.getElementById('discountPercent');

    if (discountPercentInput) {
        // Thay đổi step thành 1 để chỉ cho phép số nguyên
        discountPercentInput.setAttribute('step', '1');

        // Xử lý khi nhập giá trị
        discountPercentInput.addEventListener('input', function(e) {
            let value = this.value;

            // Loại bỏ các ký tự không phải số
            value = value.replace(/[^\d]/g, '');

            // Chuyển thành số nguyên
            let numValue = parseInt(value);

            // Kiểm tra giới hạn
            if (!isNaN(numValue)) {
                if (numValue > 100) {
                    numValue = 100;
                }
                this.value = numValue;
            } else if (value === '') {
                this.value = '';
            } else {
                this.value = 0;
            }
        });

        // Xử lý khi blur (rời khỏi trường nhập liệu)
        discountPercentInput.addEventListener('blur', function() {
            // Nếu trường rỗng hoặc giá trị không hợp lệ, đặt về 0
            if (this.value === '' || isNaN(parseInt(this.value))) {
                this.value = 0;
            } else {
                // Đảm bảo giá trị là số nguyên
                this.value = Math.floor(parseFloat(this.value));

                // Đảm bảo giá trị không vượt quá 100
                if (parseInt(this.value) > 100) {
                    this.value = 100;
                }
            }

            // Tính toán lại tổng sau khi điều chỉnh giá trị
            calculateTotal();
        });
    }
});