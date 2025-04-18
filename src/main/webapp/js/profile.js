$(function () {
    // Định nghĩa URL ảnh mặc định nếu avatar rỗng
    const DEFAULT_AVATAR = 'https://cdn-icons-png.flaticon.com/512/149/149071.png';

    // Các phần tử trong modal
    const $modal = $('#profileModal');
    const $avatarInput = $('#avatarInput');
    const $avatarPreview = $('#avatarPreview');
    const $avatarPreviewWrapper = $('#avatarPreviewWrapper'); // Để nếu cần ẩn đi

    const $fullname = $('#fullnameInput');
    const $phone = $('#phoneInput');

    // Mở modal khi bấm nút "Profile"
    $('#openModalBtn').on('click', function () {
        $.ajax({
            url: 'http://localhost:8080/project_managament_war/user/get', // Lấy thông tin người dùng từ server
            method: 'GET',
            success: function (res) {
                // Điền thông tin vào form
                $fullname.val(res?.fullname || '');
                $phone.val(res?.phone || '');

                // Kiểm tra xem avatar có không
                if (res?.avatar && res.avatar !== 'null') {
                    // Nếu có avatar, hiển thị ảnh đó
                    $avatarPreview.attr('src', res.avatar).removeClass('hidden');
                } else {
                    // Nếu không có avatar, hiển thị ảnh mặc định
                    $avatarPreview.attr('src', DEFAULT_AVATAR).removeClass('hidden');
                }

                // Mở modal
                $modal.removeClass('hidden');
            },
            error: function () {
                Swal.fire({
                    title: "Thông báo!",
                    text: "Lỗi khi tải hồ sơ người dùng",
                    icon: "error"
                });
            }
        });
    });

    // Đóng modal
    $('#closeModalBtn').on('click', function () {
        $modal.addClass('hidden');
    });

    // Xử lý khi người dùng chọn ảnh mới
    $avatarInput.on('change', function () {
        const file = this.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                // Hiển thị ảnh vừa chọn
                $avatarPreview.attr('src', e.target.result).removeClass('hidden');
            };
            reader.readAsDataURL(file);
        }
    });

    // Xử lý submit form
    $('#profileForm').on('submit', function (e) {
        e.preventDefault();

        const formData = new FormData(this);

        $.ajax({
            url: 'http://localhost:8080/project_managament_war/user/update',
            method: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function (res) {
                Swal.fire({
                    title: "Thông báo!",
                    text: "Cập nhật thành công!",
                    icon: "success"
                });
                $modal.addClass('hidden');
                fetchMembersFromAPI()
            },
            error: function () {
                Swal.fire({
                    title: "Thông báo!",
                    text: "Lỗi khi cập nhật!",
                    icon: "error"
                });
            }
        });

    });


});
