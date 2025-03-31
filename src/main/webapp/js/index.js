
/*
* Method
* Toggle Show Hide Password ( Text <=> Password ) 
* */
const togglePassword = (eventElement) => {
    $(eventElement).on('click', function () {
        if ($(this).closest('.group-password').find('input').attr('type') == 'password') {
            $(this).closest('.group-password').find('input').attr('type', 'text');
            $(this).html(' <svg width="20px" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512"><path\n' +
                '                                                d="M288 80c-65.2 0-118.8 29.6-159.9 67.7C89.6 183.5 63 226 49.4 256c13.6 30 40.2 72.5 78.6 108.3C169.2 402.4 222.8 432 288 432s118.8-29.6 159.9-67.7C486.4 328.5 513 286 526.6 256c-13.6-30-40.2-72.5-78.6-108.3C406.8 109.6 353.2 80 288 80zM95.4 112.6C142.5 68.8 207.2 32 288 32s145.5 36.8 192.6 80.6c46.8 43.5 78.1 95.4\n' +
                '                                                93 131.1c3.3 7.9 3.3 16.7 0 24.6c-14.9 35.7-46.2 87.7-93 131.1C433.5 443.2 368.8\n' +
                '                                                480 288 480s-145.5-36.8-192.6-80.6C48.6 356 17.3 304 2.5 268.3c-3.3-7.9-3.3-16.7\n' +
                '                                                 0-24.6C17.3 208 48.6 156 95.4 112.6zM288 336c44.2 0 80-35.8 80-80s-35.8-80-80-80c-.7 0-1.3 0-2 0c1.3 5.1 2 10.5 2 16c0 35.3-28.7 64-64 64c-5.5 0-10.9-.7-16-2c0 .7 0 1.3 0 2c0 44.2 35.8 80 80 80zm0-208a128 128 0 1 1 0 256 128 128 0 1 1 0-256z"/></svg>\n')

        } else {
            $(this).closest('.group-password').find('input').attr('type', 'password');
            $(this).html('<svg width="20px" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 512"><path d="M38.8 5.1C28.4-3.1 13.3-1.2 5.1 9.2S-1.2 34.7 9.2 42.9l592 464c10.4 8.2 25.5 6.3 33.7-4.1s6.3-25.5-4.1-33.7L525.6 386.7c39.6-40.6 66.4-86.1 79.9-118.4c3.3-7.9 3.3-16.7 0-24.6c-14.9-35.7-46.2-87.7-93-131.1C465.5 68.8 400.8 32 320 32c-68.2 0-125 26.3-169.3 60.8L38.8 5.1zm151 118.3C226 97.7 269.5 80 320 80c65.2 0 118.8 29.6 159.9 67.7C518.4 183.5 545 226 558.6 256c-12.6 28-36.6 66.8-70.9 100.9l-53.8-42.2c9.1-17.6 14.2-37.5 14.2-58.7c0-70.7-57.3-128-128-128c-32.2 0-61.7 11.9-84.2 31.5l-46.1-36.1zM394.9 284.2l-81.5-63.9c4.2-8.5 6.6-18.2 6.6-28.3c0-5.5-.7-10.9-2-16c.7 0 1.3 0 2 0c44.2 0 80 35.8 80 80c0 9.9-1.8 19.4-5.1 28.2zm9.4 130.3C378.8 425.4 350.7 432 320 432c-65.2 0-118.8-29.6-159.9-67.7C121.6 328.5 95 286 81.4 256c8.3-18.4 21.5-41.5 39.4-64.8L83.1 161.5C60.3 191.2 44 220.8 34.5 243.7c-3.3 7.9-3.3 16.7 0 24.6c14.9 35.7 46.2 87.7 93 131.1C174.5 443.2 239.2 480 320 480c47.8 0 89.9-12.9 126.2-32.5l-41.9-33zM192 256c0 70.7 57.3 128 128 128c13.3 0 26.1-2 38.2-5.8L302 334c-23.5-5.4-43.1-21.2-53.7-42.3l-56.1-44.2c-.2 2.8-.3 5.6-.3 8.5z"/></svg>')
        }
    })
}

/*
* Method
* Swal Alert Error
* */
const swalError = () => {
    return Swal.fire({
        icon: "error",
        title: "Lỗi...",
        text: "Có lỗi trong quá trình tạo, vui lòng thử lại!",
        showConfirmButton: true,

    }).then(() => {
        $('body').removeClass('page-loading')
        $('.form-board')[0].reset();
    });
}
/*
* Method
* Swal Alert Success
* */
const swalSuccess = () => {
    return Swal.fire({
        title: "Tạo thành công",
        icon: "success",
        draggable: true,
        showConfirmButton: true,

    }).then(() => {

        $('body').removeClass('page-loading')
        $('.form-board')[0].reset();

    });
};
/**
 * AJax
 * Add Board
 */
const handleAddBoar = () => {

    $('.form-board').on('submit', async function (e) {
        e.preventDefault(); // Ngăn reload

        const form = $(this);
        const formData = form.serialize();

        $('body').addClass('page-loading');

        $.ajax({
            url: form.attr('action'),
            method: 'POST',
            data: formData,
            success: function (response) {
                console.log(response)
                $('body').removeClass('page-loading');
                if (response.status === 'success') {
                    swalSuccess().then(() => location.reload());
                } else {
                    swalError();
                }
            },
            error: function () {
                $('body').removeClass('page-loading');
                swalError();
            }
        });
    })
}
/**
 * AJax
 * Delete Board
 */
const deleteBoard = () => {
    $('.delete-board').on('click', function (e) {
        const boardId = $(this).data('board-id')
        e.preventDefault()
        return Swal.fire({
            title: "Bạn Chắn Chắn Muốn Xóa?",
            showCancelButton: true,
            confirmButtonText: "Đồng ý",
        }).then((result) => {
            if (result.isConfirmed) {

                $.ajax({
                    url: $(this).data('url'),
                    method: 'POST',
                    data: {
                        id: boardId
                    },
                    success: function (response) {
                        Swal.fire({
                            icon: 'success',
                            title: response,
                            showConfirmButton: false,
                            timer: 1500
                        }).then(() => {
                            $(`[data-board-id="${boardId}"]`).closest('.board-item').remove();
                        });
                    },
                    error: function (e) {
                        console.log(e)
                    }
                });

            }
        });
    })
}

/**
 * AJax
 * Get Task By Board Id
 */
const getTaskByBoardId = () => {
    $('.board-item').on('click', function () {
        const dataUrl = $('.task-list-container').data('task-url')
        const boardId = $(this).data('board-id')

        $('.board-item.active').removeClass('active')
        $(this).addClass('active')
        $('body').addClass('page-loading')
        $.ajax({
            url: dataUrl,
            method: 'POST',
            data: {
                board_id: boardId
            },
            success: function (response) {

                $('.board-name').text(response.selected_board.title)
                $('.task-list-container').empty();

                if (Array.isArray(response.task_list) && response.task_list.length > 0) {
                    response.task_list.forEach(item => {
                        const taskHtml = `<div class="task-list-item">${item.title}</div>`;
                        $('.task-list-container').append(taskHtml);
                    });
                } else {
                    $('.task-list-container').empty();
                }
                $('body').removeClass('page-loading')

            },
            error: function (e) {
                console.log(e)
                $('body').removeClass('page-loading')

            }
        });
    })
}

const addTaskList = ()=>{
    $('.btn-add-task-list').on('click',function (){
        const dataUrl = $(this).data('url')
        const boardId = $('.board-item.active').data('board-id')

        $('body').addClass('page-loading')
        $.ajax({
            url: dataUrl,
            method: 'POST',
            data: {
                board_id: boardId,
                title: $('.title-add-list-task').val(),
            },
            success: function (response) {
                response.task_list.forEach(item =>{
                    const taskHtml = `
                            <div 
                              class="task-list-item" 
                              data-task-id="${item.id}" 
                              data-board-id="${item.boardId}" 
                              ${item.title}
                            </div>
                        `;
                    $('.task-list-container').append(taskHtml);

                })
                $('body').removeClass('page-loading')
            },
            error: function (e) {
                console.log(e)
                $('body').removeClass('page-loading')

            }
        });
    })
}
$(document).ready(function () {

    $('.button-item').on('click', function () {
        $('.button-item.active').removeClass('active');
        $(this).addClass('active');

        if ($(this).hasClass('btn-register')) {
            $('.login-form').removeClass('active');
            $('.register-form').addClass('active');
        } else {
            $('.register-form').removeClass('active');
            $('.login-form').addClass('active');
        }
    });

    togglePassword('.active-password');
    togglePassword('.active-confirm');
    document.querySelectorAll(".task-list").forEach(list => {
        new Sortable(list, {
            group: "shared",
            animation: 150
        });
    });

    $('.form-send').on('submit', function () {
        $('.submit-login').find('span').addClass('loader')
        $('.submit-login').prop('disabled', true)

    })

    handleAddBoar()
    deleteBoard()
    getTaskByBoardId()
    addTaskList()
})