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
            phone: 'Số điện thoại phải đủ 10 số.',
            email: 'Email không hợp lệ, vui lòng nhập đúng định dạng',
            name: 'Tên chỉ được chứa chữ cái và khoảng trắng',
            address: 'Địa chỉ chỉ được chứa chữ cái, số, khoảng trắng và các ký tự đặc biệt cho phép',
            birthdate: 'Không đủ điều kiện, tuổi phải lớn hơn 15.'
        },
        LENGTH: {
            name: 'Tên phải có độ dài từ 2 đến 50 ký tự',
            phone: 'Số điện thoại phải có đúng 10 số',
            address: 'Địa chỉ không được vượt quá 200 ký tự',
            email: 'Email phải có độ dài từ 5 đến 100 ký tự'
        },
        DUPLICATE: {
            phone: 'Số điện thoại đã tồn tại.',
            email: 'Email đã tồn tại.'
        }
    };

    function showError(fieldId, message) {
        const errorElement = document.getElementById(fieldId + 'Error');
        if (errorElement) {
            errorElement.textContent = message;
            errorElement.style.display = 'block';
        } else {
            const inputElement = document.getElementById(fieldId);
            if (inputElement) {
                const existingError = inputElement.parentNode.querySelector('.error');
                if (existingError) {
                    existingError.remove();
                }

                const errorDiv = document.createElement('p');
                errorDiv.className = 'error text-danger';
                errorDiv.textContent = message;
                errorDiv.style.display = 'block';
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
            const inputElement = document.getElementById(fieldId);
            if (inputElement) {
                const existingError = inputElement.parentNode.querySelector('.error');
                if (existingError) {
                    existingError.remove();
                }
            }
        }
    }

    function validateName(input) {
        const value = input.value.trim();

        if (value === '') {
            showError('customerName', ERROR_MESSAGES.REQUIRED.name);
            return false;
        }

        if (value.length < 2 || value.length > 50) {
            showError('customerName', ERROR_MESSAGES.LENGTH.name);
            return false;
        }

        if (!/^[A-Za-zÀ-ỹ\s]*$/.test(value)) {
            showError('customerName', ERROR_MESSAGES.FORMAT.name);
            return false;
        }

        clearError('customerName');
        return true;
    }

    async function validatePhone(input) {
        const value = input.value.trim();

        if (value === '') {
            showError('customerPhoneNumber', ERROR_MESSAGES.REQUIRED.phone);
            return false;
        }

        if (value.length !== 10) {
            showError('customerPhoneNumber', ERROR_MESSAGES.LENGTH.phone);
            return false;
        }

        if (!/^0[35789]\d{8}$/.test(value)) {
            showError('customerPhoneNumber', ERROR_MESSAGES.FORMAT.phone);
            return false;
        }

        try {
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

    function validateAddress(input) {
        const value = input.value.trim();
        if (value === '') {
            showError('customerAddress', ERROR_MESSAGES.REQUIRED.address);
            return false;
        }

        if (value.length > 200) {
            showError('customerAddress', ERROR_MESSAGES.LENGTH.address);
            return false;
        }

        if (!/^[A-Za-z0-9À-ỹ,\s.-]*$/.test(value)) {
            showError('customerAddress', ERROR_MESSAGES.FORMAT.address);
            return false;
        }

        clearError('customerAddress');
        return true;
    }

    async function validateEmail(input) {
        const value = input.value.trim();

        if (value === '') {
            showError('customerEmail', ERROR_MESSAGES.REQUIRED.email);
            return false;
        }

        if (value.length < 5 || value.length > 100) {
            showError('customerEmail', ERROR_MESSAGES.LENGTH.email);
            return false;
        }

        const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
        if (!emailRegex.test(value)) {
            showError('customerEmail', ERROR_MESSAGES.FORMAT.email);
            return false;
        }

        try {
            const response = await fetch(`/api/customers/check-email?email=${encodeURIComponent(value)}`);
            if (response.ok) {
                const data = await response.json();
                if (data.exists) {
                    showError('customerEmail', ERROR_MESSAGES.DUPLICATE.email);
                    return false;
                }
            } else {
                console.error('Lỗi khi kiểm tra email:', response.statusText);
            }
        } catch (error) {
            console.error('Error checking email:', error);
        }

        clearError('customerEmail');
        return true;
    }

    function validateBirthdate(input) {
        const value = input.value;
        if (!value) {
            showError('customerBirthdate', ERROR_MESSAGES.REQUIRED.birthdate);
            return false;
        }
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

    function validatePaymentMethod(input) {
        const value = input.value;

        if (value === '0') {
            showError('paymentMethod', ERROR_MESSAGES.REQUIRED.payment);
            return false;
        }

        clearError('paymentMethod');
        return true;
    }

    function validateProducts() {
        const productRows = document.querySelectorAll('#productList tr.product-row');

        if (productRows.length === 0) {
            const noProductsMessage = document.getElementById('noProductsMessage');
            if (noProductsMessage) {
                noProductsMessage.innerHTML = '<span class="text-danger">Vui lòng thêm ít nhất một sản phẩm</span>';
                noProductsMessage.style.display = 'block';
            }
            return false;
        }

        return true;
    }

    const phoneInputs = document.querySelectorAll("#customerPhoneNumber");
    phoneInputs.forEach(input => {
        if (input) {
            input.addEventListener("input", function(e) {
                const value = e.target.value;
                const cleanValue = value.replace(/[^\d]/g, '');

                if (cleanValue.length <= 10) {
                    e.target.value = cleanValue;
                } else {
                    e.target.value = cleanValue.slice(0, 10);
                }
            });

            // Xử lý khi paste số điện thoại
            input.addEventListener('paste', function(e) {
                setTimeout(() => {
                    const currentValue = this.value;
                    const filteredValue = currentValue.replace(/[^\d]/g, '');
                    this.value = filteredValue.slice(0, 10);
                }, 0);
            });

            // Ngăn chặn việc nhập ký tự khi đã đủ 10 số
            input.addEventListener("keypress", function(e) {
                const currentLength = this.value.length;
                if (e.ctrlKey || e.altKey || e.metaKey ||
                    [8, 9, 13, 27, 46].includes(e.keyCode) ||
                    (e.keyCode >= 35 && e.keyCode <= 40)) {
                    return;
                }

                if (currentLength >= 10) {
                    e.preventDefault();
                    return false;
                }

                if (!/[0-9]/.test(e.key)) {
                    e.preventDefault();
                    return false;
                }
            });

            input.addEventListener('blur', function() {
                validatePhone(this);
            });
        }
    });

    const nameInputs = document.querySelectorAll("#customerName");
    nameInputs.forEach(input => {
        if (input) {
            input.addEventListener("input", function(e) {
                const value = e.target.value;
                if (!/^[A-Za-zÀ-ỹ\s]*$/.test(value)) {
                    e.target.value = value.replace(/[^A-Za-zÀ-ỹ\s]/g, '');
                }
                if (value.length > 50) {
                    e.target.value = value.slice(0, 50);
                }
            });

            input.addEventListener('paste', function(e) {
                setTimeout(() => {
                    const MAX_LENGTH = 50;
                    const currentValue = this.value;
                    const filteredValue = currentValue.replace(/[^A-Za-zÀ-ỹ\s]/g, '');
                    if (filteredValue.length > MAX_LENGTH) {
                        this.value = filteredValue.substring(0, MAX_LENGTH);
                    } else {
                        this.value = filteredValue;
                    }
                }, 0);
            });

            input.addEventListener('blur', function() {
                validateName(this);
            });
        }
    });

    const addressInputs = document.querySelectorAll("#customerAddress");
    addressInputs.forEach(input => {
        if (input) {
            input.addEventListener("input", function(e) {
                const value = e.target.value;
                if (!/^[A-Za-z0-9À-ỹ,\s.-]*$/.test(value)) {
                    e.target.value = value.replace(/[^A-Za-z0-9À-ỹ,\s.-]/g, '');
                }
                if (value.length > 200) {
                    e.target.value = value.slice(0, 200);
                }
            });

            input.addEventListener('paste', function(e) {
                setTimeout(() => {
                    const MAX_LENGTH = 200;
                    const currentValue = this.value;
                    const filteredValue = currentValue.replace(/[^A-Za-z0-9À-ỹ,\s.-]/g, '');
                    if (filteredValue.length > MAX_LENGTH) {
                        this.value = filteredValue.substring(0, MAX_LENGTH);
                    } else {
                        this.value = filteredValue;
                    }
                }, 0);
            });

            input.addEventListener('blur', function() {
                validateAddress(this);
            });
        }
    });

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

    const birthdayInputs = document.querySelectorAll("#customerBirthdate");
    birthdayInputs.forEach(input => {
        if (input) {
            // Validate khi change
            input.addEventListener("change", function() {
                validateBirthdate(this);
            });
        }
    });

    const paymentMethodInput = document.getElementById('paymentMethod');
    if (paymentMethodInput) {
        paymentMethodInput.addEventListener('change', function() {
            validatePaymentMethod(this);
        });
    }

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
        clearAllErrors();
        let isValid = true;
        const customerId = document.getElementById('customerId').value;
        if (!customerId) {
            // Lấy các trường input
            const nameInput = document.getElementById('customerName');
            const phoneInput = document.getElementById('customerPhoneNumber');
            const addressInput = document.getElementById('customerAddress');
            const emailInput = document.getElementById('customerEmail');
            const birthdateInput = document.getElementById('customerBirthdate');
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
        const paymentMethodInput = document.getElementById('paymentMethod');
        if (paymentMethodInput && !validatePaymentMethod(paymentMethodInput)) {
            isValid = false;
        }
        if (!validateProducts()) {
            isValid = false;
        }

        return isValid;
    }

    if (orderForm) {
        const originalSubmit = orderForm.onsubmit;

        orderForm.addEventListener('submit', async function(e) {
            if (!(await validateOrderForm(this))) {
                e.preventDefault();
                e.stopPropagation();
                return false;
            }
            if (typeof originalSubmit === 'function') {
                return originalSubmit.call(this, e);
            }
        });
    }

    const addProductBtn = document.getElementById('addProductButton');
    if (addProductBtn) {
        addProductBtn.addEventListener('click', function() {
            verifyCustomerInfo();
        });
    }

    function verifyCustomerInfo() {
        clearAllErrors();
        const customerId = document.getElementById('customerId').value;
        if (customerId && customerId.trim() !== '') {
            if (typeof window.selectProduct === 'function') {
                window.selectProduct();
            }
            return;
        }

        const nameInput = document.getElementById('customerName');
        const phoneInput = document.getElementById('customerPhoneNumber');
        const addressInput = document.getElementById('customerAddress');
        const emailInput = document.getElementById('customerEmail');

        let isValid = true;
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
            showCustomerRequiredModal();
            return;
        }

        if (typeof window.selectProduct === 'function') {
            window.selectProduct();
        }
    }

    function showCustomerRequiredModal() {
        let customerRequiredModal = document.getElementById('customerRequiredModal');

        if (customerRequiredModal) {
            const bsModal = new bootstrap.Modal(customerRequiredModal);
            customerRequiredModal.addEventListener('hidden.bs.modal', function () {
                const backdrops = document.querySelectorAll('.modal-backdrop');
                backdrops.forEach(backdrop => {
                    backdrop.remove();
                });
                document.body.classList.remove('modal-open');
                document.body.style.overflow = '';
                document.body.style.paddingRight = '';
            }, { once: true });

            bsModal.show();
        }
    }

    document.addEventListener('DOMContentLoaded', function() {
        const closeButton = document.querySelector('#customerRequiredModal button[data-bs-dismiss="modal"]');

        if (closeButton) {
            closeButton.addEventListener('click', function() {
                const backdrops = document.querySelectorAll('.modal-backdrop');
                backdrops.forEach(backdrop => {
                    backdrop.remove();
                });
                document.body.classList.remove('modal-open');
                document.body.style.overflow = '';
                document.body.style.paddingRight = '';
            });
        }
    });
});

document.addEventListener('DOMContentLoaded', function() {
    const discountPercentInput = document.getElementById('discountPercent');

    if (discountPercentInput) {
        // Thay đổi step thành 1 để chỉ cho phép số nguyên
        discountPercentInput.setAttribute('step', '1');

        // Xử lý khi nhập giá trị
        discountPercentInput.addEventListener('input', function(e) {
            let value = this.value;
            value = value.replace(/[^\d]/g, '');
            let numValue = parseInt(value);
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

        discountPercentInput.addEventListener('blur', function() {
            if (this.value === '' || isNaN(parseInt(this.value))) {
                this.value = 0;
            } else {
                this.value = Math.floor(parseFloat(this.value));
                if (parseInt(this.value) > 100) {
                    this.value = 100;
                }
            }
            calculateTotal();
        });
    }
});