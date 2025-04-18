$(document).ready(function () {
    const $modal = $("#share-modal");
    const $sendBtn = $modal.find("button.bg-blue-500");
    const $emailInput = $modal.find("input[type='email']");
    const $closeBtn = $("#close-share-modal");
    const $avatarGroup = $('#avatar-group-list');
    const $dropdown = $('#member-dropdown');
    const $memberList = $('#member-list');
    let memberList = [];

    // Gửi lời mời thành viên
    $sendBtn.on("click", function () {
        const email = $emailInput.val().trim();
        const boardId = $('.board-item.active').data('board-id');

        if (!email) {
            Swal.fire({
                title: "Thông báo!",
                text: "Vui lòng nhập email!",
                icon: "error"
            });
            return;
        }

        $('body').addClass('page-loading');

        $.ajax({
            url: "http://localhost:8080/project_managament_war/board-members/invite",
            method: "POST",
            data: {email, boardId},
            success: function () {
                Swal.fire({
                    title: "Thông báo!",
                    text: "Đã mời thành viên thành công!",
                    icon: "success"
                });
                $emailInput.val("");
                fetchMembersFromAPI(); // Cập nhật lại danh sách thành viên
            },
            error: function (xhr) {
                let errorMessage = "Mời thất bại!";
                try {
                    const resJson = JSON.parse(xhr.responseText);
                    errorMessage = resJson.message || xhr.responseText;
                } catch (e) {
                    if (xhr.responseText) errorMessage = xhr.responseText;
                }

                Swal.fire({
                    title: "Thông báo!",
                    text: errorMessage,
                    icon: "error"
                });
            },
            complete: function () {
                $('body').removeClass('page-loading');
            }
        });
    });

    // Mở & đóng modal
    $('#share-btn').on('click', () => $modal.removeClass('hidden'));
    $closeBtn.on('click', () => $modal.addClass('hidden'));
    $modal.on('click', function (e) {
        if ($(e.target).is('#share-modal')) {
            $(this).addClass('hidden');
        }
    });

    // Render avatar
    function renderAvatars(members) {
        $avatarGroup.empty();
        const maxVisible = 6;

        members.slice(0, maxVisible).forEach(member => {
            const avatarUrl = member.avatar || null;
            const initial = member.fullName?.charAt(0).toUpperCase() || '?';

            // Random Tailwind color classes
            const colorClasses = [
                'bg-red-500', 'bg-green-500', 'bg-blue-500',
                'bg-yellow-500', 'bg-indigo-500', 'bg-purple-500',
                'bg-pink-500', 'bg-orange-500', 'bg-teal-500'
            ];
            const randomColor = colorClasses[Math.floor(Math.random() * colorClasses.length)];

            const $avatar = $(`
            <div class="member-avatar w-8 h-8 rounded-full overflow-hidden cursor-pointer border-2 border-white text-sm" title="${member.fullName}">
                ${
                avatarUrl
                    ? `<img src="${avatarUrl}" alt="${initial}" class="w-full h-full object-cover" />`
                    : `<div class="w-full h-full ${randomColor} text-white flex items-center justify-center">${initial}</div>`
            }
            </div>
        `);

            $avatarGroup.append($avatar);
        });

        if (members.length > maxVisible) {
            const $more = $(`
            <div class="member-avatar more-members w-8 h-8 rounded-full bg-gray-500 text-white flex items-center justify-center cursor-pointer border-2 border-white text-sm">
                +
            </div>
        `);
            $avatarGroup.append($more);
        }
    }

    function renderMemberList(members) {
        const $memberList = $('#member-list');
        $memberList.empty();

        const currentBoardId = $('.board-item.active').data('board-id');
        const currentUserEmail = window.currentUserEmail;

        let currentUserRole = 'MEMBER';
        members.forEach(member => {
            if (member.email === currentUserEmail && member.boardId === currentBoardId) {
                currentUserRole = member.role;
            }
        });

        members.forEach(member => {
            const isPending = member.status === 'PENDING';
            let actionHTML = '';

            if (isPending) {
                if (currentUserRole === 'OWNER') {
                    actionHTML = `
                    <button class="approve-btn text-xs bg-blue-500 text-white px-2 py-1 rounded whitespace-nowrap" data-id="${member.id}">
                        Phê duyệt
                    </button>`;
                } else {
                    actionHTML = `
                    <span class="text-xs px-2 py-1 rounded bg-yellow-100 text-yellow-800 whitespace-nowrap">
                        Đang chờ phê duyệt
                    </span>`;
                }
            } else {
                actionHTML = `
                <span class="text-xs px-2 py-1 rounded bg-green-100 text-green-800 whitespace-nowrap">
                    Hoạt độnng
                </span>`;
            }

            const $item = $(`
              <div class="grid grid-cols-[auto_1fr_auto] items-center gap-3">
                <div class="w-8 h-8 rounded-full overflow-hidden bg-gray-200 flex-shrink-0">
                 <img src="${member.avatar || `${contextPath}/views/img.png`}" class="w-full h-full object-cover" alt="avatar">
                </div>
                <div>
                  <p class="font-medium leading-tight">${member.fullName}</p>
                  <p class="text-xs text-gray-500">${member.email}</p>
                </div>
                <div class="self-start mt-1">${actionHTML}</div>
              </div>
            `);
            $memberList.append($item);
        });
    }



    // Phê duyệt thành viên
    function approveMember(id) {
        $('body').addClass('page-loading');

        $.ajax({
            url: 'http://localhost:8080/project_managament_war/board-members/approve',
            method: 'POST',
            data: {boardMemberId: id},
            success: function () {
                const member = memberList.find(m => m.id === id);
                if (member) {
                    member.status = 'ACTIVE';
                    renderMemberList(memberList);
                }

                Swal.fire({
                    title: "Thông báo!",
                    text: "Phê duyệt thành công",
                    icon: "success"
                });
            },
            error: function (error) {
                console.error('Error approving member:', error);
            },
            complete: function () {
                $('body').removeClass('page-loading');
            }
        });
    }

    // Lấy dữ liệu từ API
    window.fetchMembersFromAPI = function() {
        const boardId = $('.board-item.active').data('board-id');

        console.log(boardId)

        if (!boardId) return;

        $.ajax({
            url: 'http://localhost:8080/project_managament_war/board-members/details?boardId=' + boardId,
            method: 'GET',
            success: function (data) {
                memberList = data;
                renderMemberList(memberList);
                renderAvatars(memberList);
            },
            error: function (error) {
                console.error('Error fetching members:', error);
            }
        });
    }

    // Sự kiện click vào avatar -> toggle dropdown
    $(document).on('click', '.member-avatar', function (e) {
        $dropdown.toggleClass('hidden');
        if (!$dropdown.hasClass('hidden')) {
            fetchMembersFromAPI(); // Refresh danh sách
        }
        e.stopPropagation();
    });

    // Đóng dropdown khi click ra ngoài
    $(document).on('click', function (e) {
        if (!$(e.target).closest('#avatar-group').length) {
            $dropdown.addClass('hidden');
        }
    });

    // Sự kiện phê duyệt thành viên
    $(document).on('click', '.approve-btn', function () {
        const id = $(this).data('id');
        approveMember(id);
    });

    function waitForBoardIdThenFetch() {
        const interval = setInterval(() => {
            const boardId = $('.board-item.active').data('board-id');
            if (boardId) {
                clearInterval(interval);
                fetchMembersFromAPI();
            }
        }, 200); // kiểm tra mỗi 200ms
    }

    waitForBoardIdThenFetch();
});
