let uploadedFile = null;

// Xử lý upload ảnh
document.getElementById('imageUpload').addEventListener('change', function(event) {
    const file = event.target.files[0];
    if (!file) return;

    const imagePreview = document.getElementById('imagePreview');
    uploadedFile = file;

    const reader = new FileReader();
    reader.onload = function(e) {
        // Xóa nội dung cũ
        imagePreview.innerHTML = '';

        // Tạo container ảnh
        const imageContainer = document.createElement('div');
        imageContainer.className = 'image-container';

        // Tạo ảnh preview
        const img = document.createElement('img');
        img.src = e.target.result;
        img.style.maxWidth = '100%';
        img.style.height = 'auto';

        // Tạo nút xóa
        const deleteBtn = document.createElement('button');
        deleteBtn.className = 'delete-btn';
        deleteBtn.innerHTML = '<i class="bi bi-x"></i>';
        deleteBtn.type = 'button';
        deleteBtn.onclick = function() {
            uploadedFile = null;
            imagePreview.innerHTML = createUploadLabel();
            updateHiddenInput();
        };

        imageContainer.appendChild(img);
        imageContainer.appendChild(deleteBtn);
        imagePreview.appendChild(imageContainer);

        updateHiddenInput();
    };

    reader.readAsDataURL(file);
    // Reset input
    this.value = '';
});

// Tạo label upload
function createUploadLabel() {
    return `
        <label class="image-upload" for="imageUpload">
            <i class="bi bi-camera"></i>
            <span>Thêm ảnh sản phẩm</span>
        </label>
    `;
}

// Cập nhật input ẩn
function updateHiddenInput() {
    const hasImage = uploadedFile ? 1 : 0;
    document.getElementById('selectedImages').value = hasImage;
}

// Validate form khi submit
document.querySelector('form').addEventListener('submit', function(event) {
    if (!uploadedFile) {
        event.preventDefault();
        Swal.fire({
            icon: 'error',
            title: 'Thiếu ảnh sản phẩm',
            text: 'Vui lòng tải lên ảnh sản phẩm',
            confirmButtonColor: '#ee4d2d'
        });
        return false;
    }
});
