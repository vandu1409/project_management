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

/*
* Ajax Function
* */
const sendAjaxRequest = ({url, method = 'POST', data = {}}) => {
    $('body').addClass('page-loading');
    return $.ajax({
        url,
        method,
        data
    }).always(() => {
        $('body').removeClass('page-loading');
    });
};

/*
* Ajax
* Get Board
* */

const getBoards = async (boardId = null) => {

    const dataUrl = $('#board-item-template').data('url')
    $('body').addClass('page-loading');

    await sendAjaxRequest({
        url: dataUrl,
        data: ''
    })
        .done((response) => {
            $('.board-list-container').empty();

            response.boards.forEach((item, index) => {
                const template = $('#board-item-template').html();
                const $board = $(template);

                $board.find('.board-title').text(item.title);
                $board.attr('data-board-id', item.id);
                if (item.id === boardId || (boardId === null && index === 0)) {
                    $board.addClass('active');
                }

                $('.board-list-container').append($board);
            });

            if (response.boards.length > 0) {
                $('.board-name').text(response.boards[0].title);
                getTaskByBoardId(response.boards[0].id);
            }
            $('.board-name').text(response.boards[0].title)
            $('body').removeClass('page-loading');
            deleteBoard()
            $('.board-item').on('click', function () {
                $('.board-item.active').removeClass('active');
                $(this).addClass('active');
                getTaskByBoardId($(this).data('board-id'))
            })
        })
        .fail((err) => {
            $('body').removeClass('page-loading');
            console.error('Lỗi khi lấy board:', err);
        });

}

/**
 * AJax
 * Add Board
 */
const handleAddBoar = async () => {

    $('.form-board').on('submit', async function (e) {
        e.preventDefault();
        const form = $(this);
        const formData = form.serialize();
        await sendAjaxRequest({
            url: form.attr('action'),
            data: formData
        })
            .done((res) => {
                getBoards(res.boardId)
                $('#collapseBoard input').val('');
                getTaskByBoardId(res.boardId)

                var opened = document.querySelectorAll('#collapseBoard.collapse.show');
                opened.forEach(el => {
                    bootstrap.Collapse.getInstance(el).hide();
                });
            })
            .fail((err) => {
                $('body').removeClass('page-loading');
                console.error('Lỗi khi lấy Add board:', err);
            });


    })
}

/**
 * AJax
 * Delete Board
 */
const deleteBoard = () => {
    $('.delete-board').on('click', function (e) {
        e.stopPropagation();
        const boardId = $(this).closest('.board-item').data('board-id')
        e.preventDefault()
        return Swal.fire({
            title: "Bạn Chắn Chắn Muốn Xóa?",
            showCancelButton: true,
            confirmButtonText: "Đồng ý",
        }).then((result) => {
            if (result.isConfirmed) {
                sendAjaxRequest({
                    url: $(this).data('url'),
                    data: {
                        board_id: boardId
                    }
                })
                    .done((response) => {
                        Swal.fire({
                            icon: 'success',
                            title: response,
                            showConfirmButton: false,
                            timer: 1500
                        }).then(() => {
                            $(`[data-board-id="${boardId}"]`).closest('.board-item').remove();
                        });
                        getBoards()
                    })
                    .fail((err) => {
                        console.error('Lỗi khi lấy task theo board:', err);
                    });
            }
        });
    })
}

/**
 * AJax
 * Get Task By Board Id
 */

let taskList = ''

const getTaskByBoardId = (boardId) => {

    boardId = $('.board-item.active').data('board-id');

    const dataUrl = $('.task-list-container').data('task-url');
    sendAjaxRequest({
        url: dataUrl,
        data: {
            board_id: boardId
        }
    })
        .done((response) => {

            $('.board-name').text(response.selected_board.title);
            $('.task-list-container').empty();
            taskList = response.task_list
            response.task_list.forEach(item => {
                const template = $('#task-list-template').html();
                const $task = $(template);
                $task.attr('data-task-id', item.id);
                $task.find('.child-task-gr').attr('data-task-list-id', item.id);
                $task.attr('data-board-id', item.boardId);
                $task.find('.task-title').text(item.title);
                $task.find('.dropdown-menu').attr('aria-labelledby', `dropdownTask${item.id}`);
                $task.find('.dropdown-toggle').attr('id', `dropdownTask${item.id}`);
                $task.find('.collapse-task-child').attr('id', `collapseTaskChild-${item.id}`);
                $task.find('.btn-task-child').attr('href', `collapseTaskChild-${item.id}`);
                $task.find('.task-child-container').attr('data-task-list-id', item.id);


                $('.task-list-container').append($task);

                const $childContainer = $task.find('.task-child-container');

                item.tasks.forEach(taskChild => {
                    const childTemplate = $('#task-list-child-template').html();
                    const $taskChild = $(childTemplate);
                    $taskChild.find('.task-child-title').text(taskChild.title);
                    $taskChild.find('.task-child-group').attr('data-task-child-id', taskChild.id);
                    $childContainer.append($taskChild);
                });
            });
            getTaskChildModal()
            removeTaskList();
            addTask()
            createTaskChild()
            createDescription()
            const dataChildUrl = $('.task-child-container').data('url')
            document.querySelectorAll(".task-child-container").forEach(list => {
                new Sortable(list, {
                    group: "shared",
                    animation: 150,
                    draggable: ".task-child-list",

                    onAdd: function (evt) {
                        const taskEl = evt.item;
                        const newListEl = evt.to;
                        $('body').addClass('page-loading')
                        const taskId = $(taskEl).find('.task-child-group').attr('data-task-child-id');
                        const newTaskListId = newListEl.getAttribute('data-task-list-id');
                        $.ajax({
                            url: dataChildUrl,
                            method: 'POST',
                            data: {
                                task_child_id: taskId,
                                tas_list_id: newTaskListId,
                            },
                            success: function (response) {
                                $('body').removeClass('page-loading')
                            },
                            error: function (e) {
                                console.log(e)
                                $('body').removeClass('page-loading')
                            }
                        });
                    }
                });
            });


        })
        .fail((err) => {
            console.error('Lỗi khi lấy task theo board:', err);
        });

};

const createDescription = () => {
    const dataUrl = $('.btn-button-group').data('url')
    $('.task-area-description').on('focus', function () {
        $('.btn-button-group').addClass('!flex');
    });

    $('.btn-cancel').on('click', function () {
        $('.btn-button-group').removeClass('!flex');
    })
    $('.btn-save').on('click', function () {
        $('body').addClass('page-loading')

        $.ajax({
            url: dataUrl,
            method: 'POST',
            data: {
                task_id: $('#taskChildModal').data('task-id'),
                description: $('.task-area-description').val(),
            },
            success: function (response) {
                    $('.btn-button-group').removeClass('!flex');
                    $('.description-title').text($('.task-area-description').val())
                    $('.task-area-description').addClass('hidden').removeClass('block')
                    $('.btn-edit').addClass('block').removeClass('hidden')
                    $('.description-title').addClass('block').removeClass('hidden')
                const dataUrlChdil = $('#taskChildModal').data('url');
                const taskId = parseInt($('#taskChildModal').attr('data-task-id'), 10);


                sendAjaxRequest({
                    url: dataUrlChdil,
                    data: {
                        task_id: taskId
                    }
                })
                    .done((response) => {
                        $('.modal-dropdown').empty();
                        $('.task-area-description').val('');
                        $('.description-title').text('')
                        $('.btn-edit').removeClass('block');

                        $('.task-modal-title').text(response.task_detail.title)
                        if (response.task_detail.description){
                            $('.description-title').text(response.task_detail.description)
                            $('.description-title').addClass('block').removeClass('hidden')
                            $('.task-area-description').addClass('hidden')
                            $('.btn-edit').addClass('block').removeClass('hidden')

                        }else{
                            $('.description-title').addClass('hidden')
                            $('.btn-edit').addClass('hidden')
                            $('.task-area-description').addClass('block').removeClass('hidden')
                        }
                        taskList.forEach(item => {
                            if (response.task_detail.taskListId === item.id) {
                                $('.dropdown-active').text(item.title);
                            }
                            const template = `<li><a class="dropdown-item" href="#" data-task_id = ${item.id}>${item.title}</a></li>`;
                            $('.modal-dropdown').append(template)
                        })
                        $('.btn-edit').off('click').on('click',function (){
                            $(this).addClass('hidden')
                            $('.description-title').removeClass('block').addClass('hidden')
                            $('.btn-button-group').addClass('!flex');
                            $('.task-area-description').addClass('block').removeClass('hidden')
                            $('.task-area-description').val(response.task_detail.description)
                        })
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


/*
* Ajax
* Add Task List
* */
const addTaskList = () => {
    $('.btn-add-task-list').on('click', function (e) {
        const dataUrl = $(this).data('url')
        const boardId = $('.board-item.active').data('board-id')
        e.preventDefault();

        $('body').addClass('page-loading')
        $.ajax({
            url: dataUrl,
            method: 'POST',
            data: {
                board_id: boardId,
                title: $('.title-add-list-task').val(),
            },
            success: function (response) {
                $('.title-add-list-task').val('');
                var opened = document.querySelectorAll('#collapseAddTaskList.collapse.show');
                opened.forEach(el => {
                    bootstrap.Collapse.getInstance(el).hide();
                });
                getTaskByBoardId($('.board-item.active').data('board-id'))
                $('body').removeClass('page-loading')

            },
            error: function (e) {
                console.log(e)
                $('body').removeClass('page-loading')

            }
        });
    })
}

/*
* Ajax
* Remove Task List
* */

const removeTaskList = () => {
    $('.remove-task-list-item').on('click', function (e) {
        const taskListId = $(this).closest('.task-list-item').data('task-id')
        e.preventDefault()
        return Swal.fire({
            title: "Bạn Chắn Chắn Muốn Xóa?",
            showCancelButton: true,
            confirmButtonText: "Đồng ý",
        }).then((result) => {
            if (result.isConfirmed) {
                sendAjaxRequest({
                    url: $('#task-list-template').data('url'),
                    data: {task_list_id: taskListId}
                })
                    .done((response) => {
                        Swal.fire({
                            icon: 'success',
                            title: response,
                            showConfirmButton: false,
                            timer: 1500
                        }).then(() => {
                            $(`[data-task-id="${taskListId}"]`).closest('.task-list-item').remove();
                        });
                    })
            }
        });


    })
}

const addTask = () => {
    $('.task-child.add-task').on('click', function () {
        let _this = $(this);
        _this.hide();
        const parentItem = _this.closest('.task-list-item');
        parentItem.find('.add-child-task').show();
        parentItem.find('.collapse-task-child').show();
    });
    $('.cancel').on('click', function () {
        const parentItem = $(this).closest('.task-list-item');
        parentItem.find('.task-child.add-task').show();
        parentItem.find('.add-child-task').hide();
        parentItem.find('.collapse-task-child').hide();
    });

}


const createTaskChild = () => {
    $('.create-task').on('click', function (e) {
        const dataUrl = $(this).data('url')
        const taskListId = $(this).closest('.task-list-item').data('task-id')
        e.preventDefault();

        $('body').addClass('page-loading')
        $.ajax({
            url: dataUrl,
            method: 'POST',
            data: {
                task_list_id: taskListId,
                title: $('.task-child-val').val(),
            },
            success: function (response) {
                console.log(response)
                $('.task-child-val').val('');
                $('body').removeClass('page-loading')
                getTaskByBoardId()

            },
            error: function (e) {
                console.log(e)
                $('body').removeClass('page-loading')

            }
        });
    })
}

const getTaskChildModal = () => {
    $('.task-child-group').on('click', function () {
        $('.btn-button-group').removeClass('!flex');
        $('.task-description').val('');

        $('body').addClass('page-loading')
        const taskId = $(this).data('task-child-id')
        $('#taskChildModal').attr('data-task-id',taskId)

        $('#taskChildModal').modal('show');

            const dataUrl = $('#taskChildModal').data('url');
            sendAjaxRequest({
                url: dataUrl,
                data: {
                    task_id:taskId
                }
            })
                .done((response) => {
                    $('.modal-dropdown').empty();
                    $('.task-area-description').val('');
                    $('.description-title').text('')
                    $('.btn-edit').removeClass('block');

                    $('.task-modal-title').text(response.task_detail.title)
                    if (response.task_detail.description){
                        $('.description-title').text(response.task_detail.description)
                        $('.description-title').addClass('block').removeClass('hidden')
                        $('.task-area-description').addClass('hidden')
                        $('.btn-edit').addClass('block').removeClass('hidden')

                    }else{
                        $('.description-title').addClass('hidden')
                        $('.btn-edit').addClass('hidden')
                        $('.task-area-description').addClass('block').removeClass('hidden')
                    }
                    taskList.forEach(item => {
                        if (response.task_detail.taskListId === item.id) {
                            $('.dropdown-active').text(item.title);
                        }
                        const template = `<li><a class="dropdown-item" href="#" data-task_id = ${item.id}>${item.title}</a></li>`;
                        $('.modal-dropdown').append(template)
                    })
                    $('.btn-edit').off('click').on('click',function (){
                        $(this).addClass('hidden')
                        $('.description-title').removeClass('block').addClass('hidden')
                        $('.btn-button-group').addClass('!flex');
                        $('.task-area-description').addClass('block').removeClass('hidden')
                        $('.task-area-description').val(response.task_detail.description)
                    })
                })
        $('#taskChildModal').on('hidden.bs.modal', function () {
            $('.task-area-description').val('').addClass('hidden');
            $('.description-title').text('').addClass('block');
            $('.btn-edit').addClass('hidden');
        });
    });


}
$(document).ready(function () {
    getBoards()
    addTaskList()
    handleAddBoar()
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
})
//
// const getTasks = async (taskListId) => {
//     const dataUrl = "http://localhost:8080/project_managament_war/task/details?taskListId=" + taskListId;
//
//     $('body').addClass('page-loading');
//
//     await sendAjaxRequest({
//         url: dataUrl,
//         method: 'GET',
//
//     })
//         .done((response) => {
//             $('.task-list-container').empty();  // Xóa các task hiện có
//
//             response.tasks.forEach((task) => {
//                 const template = $('#task-list-template').html();  // Lấy template HTML của task
//                 const $task = $(template);
//
//                 $task.find('.task-title').text(task.title);  // Gán title của task
//                 $task.attr('data-task-id', task.id);  // Gán ID task
//
//                 $('.task-list-container').append($task);  // Thêm task vào container
//             });
//
//             if (response.tasks.length > 0) {
//                 $('.task-name').text(response.tasks[0].title);  // Hiển thị tên của task đầu tiên
//             }
//
//             $('body').removeClass('page-loading');
//             deleteTask();  // Thêm chức năng xóa task
//
//             $('.task-item').on('click', function () {
//                 $('.task-item.active').removeClass('active');
//                 $(this).addClass('active');
//             });
//         })
//         .fail((err) => {
//             $('body').removeClass('page-loading');
//             console.error('Lỗi khi lấy task:', err);
//         });
// }

//
// const addTask = async (taskListId, taskTitle) => {
//     const dataUrl = $('#create-child-task-form').attr('action');  // URL để gửi yêu cầu tạo task mới
//     $('body').addClass('page-loading');
//
//     await sendAjaxRequest({
//         url: dataUrl,
//         method: 'POST',  // Phương thức POST để tạo task mới
//         data: { taskListId, title: taskTitle }  // Dữ liệu task cần thêm
//     })
//         .done((response) => {
//             // Nếu tạo task thành công, gọi lại hàm getTasks để reload danh sách task
//             getTasks(taskListId);
//             $('body').removeClass('page-loading');
//         })
//         .fail((err) => {
//             $('body').removeClass('page-loading');
//             console.error('Lỗi khi tạo task:', err);
//         });
// }
//
// $('.create-task-btn').on('click', function () {
//     const taskListId = $(this).closest('.task-list-item').data('task-id')
//     const taskTitle = $('#task-title').val();
//
//     if (taskTitle.trim() !== '') {
//
//         addTask(boardId, taskTitle);
//
//         $('#task-title').val('');
//     } else {
//         alert('Vui lòng nhập tiêu đề task');
//     }
// });

