// Validate số điện thoại chỉ cho phép nhập số và giới hạn 10 số
document.getElementById("employeePhone").addEventListener("input", function(e) {
    const value = e.target.value;

    // Chỉ cho phép số
    if (!/^\d*$/.test(value)) {
        e.target.value = value.replace(/\D/g, '');
    }

    // Giới hạn độ dài
    if (value.length > 10) {
        e.target.value = value.slice(0, 10);
    }
});

// Xử lý khi paste số điện thoại
document.getElementById("employeePhone").addEventListener('paste', function(e) {
    setTimeout(() => {
        const currentValue = this.value;

        // Lọc ký tự không phải số
        const filteredValue = currentValue.replace(/\D/g, '');

        // Giới hạn độ dài
        if (filteredValue.length > 10) {
            this.value = filteredValue.substring(0, 10);
        } else {
            this.value = filteredValue;
        }
    }, 0);
});

// Validate tên nhân viên chỉ cho phép nhập chữ, số và khoảng trắng
document.getElementById("employeeName").addEventListener("input", function(e) {
    const value = e.target.value;

    // Chỉ cho phép chữ cái, số và khoảng trắng
    if (!/^[A-Za-z0-9À-ỹ\s]*$/.test(value)) {
        e.target.value = value.replace(/[^A-Za-z0-9À-ỹ\s]/g, '');
    }

    // Giới hạn độ dài
    if (value.length > 50) {
        e.target.value = value.slice(0, 50);
    }
});

// Xử lý khi paste tên nhân viên
document.getElementById("employeeName").addEventListener('paste', function(e) {
    setTimeout(() => {
        const MAX_LENGTH = 50;
        const currentValue = this.value;

        // Lọc ký tự không hợp lệ
        const filteredValue = currentValue.replace(/[^A-Za-z0-9À-ỹ\s]/g, '');

        // Giới hạn độ dài
        if (filteredValue.length > MAX_LENGTH) {
            this.value = filteredValue.substring(0, MAX_LENGTH);
        } else {
            this.value = filteredValue;
        }
    }, 0);
});

// Validate ngày sinh để đảm bảo tuổi > 15
document.getElementById("employeeBirthday").addEventListener("change", function(e) {
    const birthDate = new Date(this.value);
    const today = new Date();
    const ageDate = new Date(today - birthDate);
    const age = Math.abs(ageDate.getUTCFullYear() - 1970);

    if (age <= 15) {
        document.getElementById("dobError").textContent = "Tuổi phải lớn hơn 15";
        document.getElementById("dobError").style.display = "block";
    } else {
        document.getElementById("dobError").style.display = "none";
    }
});