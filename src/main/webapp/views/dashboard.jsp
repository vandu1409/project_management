<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<%@ include file="/views/common.jsp" %>

<body>

<header class="bg-gray-800  text-white px-4 py-3 flex justify-between items-center">
    <div>
        <div class="text-2xl font-bold text-white text-nowrap">TaskHub</div>
    </div>
    <div class="cursor-pointer dropdown">
        <span data-bs-toggle="dropdown" aria-expanded="false">
    <svg width="25px" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512">
            <path fill="white"
                  d="M406.5 399.6C387.4 352.9 341.5 320 288 320l-64 0c-53.5 0-99.4 32.9-118.5 79.6C69.9 362.2 48 311.7 48 256C48 141.1 141.1 48 256 48s208 93.1 208 208c0 55.7-21.9 106.2-57.5 143.6zm-40.1 32.7C334.4 452.4 296.6 464 256 464s-78.4-11.6-110.5-31.7c7.3-36.7 39.7-64.3 78.5-64.3l64 0c38.8 0 71.2 27.6 78.5 64.3zM256 512A256 256 0 1 0 256 0a256 256 0 1 0 0 512zm0-272a40 40 0 1 1 0-80 40 40 0 1 1 0 80zm-88-40a88 88 0 1 0 176 0 88 88 0 1 0 -176 0z"/>
        </svg>
        </span>

        <ul class="dropdown-menu dropdown-menu-dark">
            <li><a class="dropdown-item" href="../New%20folder/profile.html">Profile</a></li>
            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">Logout</a></li>
        </ul>
    </div>
</header>

<section class="bg-gray-900   flex text-gray-900 overflow-auto " style="height: calc(100% - 56px);">
    <!-- Sidebar -->
    <aside class="z-1 bg-gray-900  w-48 p-3 border-r border-dotted border-gray-600 sticky left-0">
        <div class="flex justify-between gap-3 items-center mb-3 ">
            <h2 class="text-xl font-bold text-white text-nowrap">Task Board</h2>
        </div>
        <nav>
            <ul>
                <li class="mb-3 flex justify-between items-center">
                    <span href="#"
                          class="block p-2 font-normal  rounded-lg text-white whitespace-nowrap">Dự án của bạn</span>
                    <a class="hover:bg-[#6a6a6a80] rounded flex p-1 cursor-pointer w-[25px] h-[25px]"
                       data-bs-toggle="collapse" href="#collapseBoard" aria-expanded="false"
                       aria-controls="collapseBoard">
                        <svg width="100%" height="100%" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512">
                            <path
                                    fill="white"
                                    d="M256 80c0-17.7-14.3-32-32-32s-32 14.3-32 32l0 144L48 224c-17.7 0-32 14.3-32 32s14.3 32 32 32l144 0 0 144c0 17.7 14.3 32 32 32s32-14.3 32-32l0-144 144 0c17.7 0 32-14.3 32-32s-14.3-32-32-32l-144 0 0-144z"/>
                        </svg>

                    </a>

                </li>
            </ul>

            <div class="collapse !visible ${status == 'success' ? 'successed' : (status == 'fail' ? 'failed' : '')}"
                 id="collapseBoard">
                <form action="${pageContext.request.contextPath}/dashboard/add"
                      class="form-board card card-body !bg-black p-2" method="POST">
                    <input required class="border-2 rounded mb-3 px-2 text-dark" type="text" name="title"
                           placeholder="Tạo Board">
                    <button type="submit" class="btn btn-primary text-center">Tạo</button>
                </form>

            </div>

            <section class="flex space-x-6 overflow-x-auto flex-column board-list-container">

            </section>

        </nav>
    </aside>

    <!-- Main Content -->
    <main class=" grow flex-col main-section relative z-0 ">
        <!-- Header -->
        <header class="px-4 py-2 flex justify-between items-center bg-gray-700 z-0">

            <h1 class="text-2xl font-semibold text-white board-name"> </h1>

        </header>

        <!--  Task List -->

        <div class="flex p-6 space-x-6 ">
            <section class="flex space-x-6 overflow-x-auto task-list-container"
                     data-task-url="${pageContext.request.contextPath}/dashboard/board_id">

            </section>
            <div class="">
                <button data-bs-toggle="collapse" href="#collapseAddTaskList"
                        class="w-[125px] bg-[#000000] text-white p-3 rounded-md font-semibold add-task"
                >+ Add Task List
                </button>
                <div class="collapse !visible w-[125px] max-w-[125px]" id="collapseAddTaskList"
                >
                    <div class=" card card-body !bg-black p-2">
                        <input required class="border-2 rounded mb-3 px-2 text-dark title-add-list-task" type="text"
                               name="title"
                               placeholder="Task List Name">
                        <button type="submit" class="btn-add-task-list btn btn-primary text-center"
                                data-url="${pageContext.request.contextPath}/dashboard/add_task_list">Tạo
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </main>
    <!-- Modal -->
    <div class="modal fade" id="taskChildModal" tabindex="-1" data-url="${pageContext.request.contextPath}/dashboard/get_detail_task" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content " style="border-radius: 20px">
                <div class="modal-body p-0 ">
                    <section class="">
                        <div class=" bg-gray-900 rounded-xl  p-4  flex gap-4">
                            <div class="flex-grow">
                                <div class=" mb-4">
                                    <div class="text-xl font-bold text-white flex justify-between">
                                        <div class="task-modal-title">


                                        </div>
                                        <div class="cursor-pointer hover:bg-gray-800 p-2 rounded"
                                             data-bs-dismiss="modal">
                                            <svg width="20px" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 384 512">
                                                <path fill="white"
                                                      d="M342.6 150.6c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0L192 210.7 86.6 105.4c-12.5-12.5-32.8-12.5-45.3 0s-12.5 32.8 0 45.3L146.7 256 41.4 361.4c-12.5 12.5-12.5 32.8 0 45.3s32.8 12.5 45.3 0L192 301.3 297.4 406.6c12.5 12.5 32.8 12.5 45.3 0s12.5-32.8 0-45.3L237.3 256 342.6 150.6z"/>
                                            </svg>
                                        </div>
                                    </div>
                                    <div class="text-gray-400 flex items-center gap-2 mt-2">
                                        <div>
                                            In list
                                        </div>

                                        <div class="dropdown">
                                            <div class="dropdown-active dropdown-toggle bg-gray-800 p-2 rounded-xl cursor-pointer fw-medium"
                                                 data-bs-toggle="dropdown" aria-expanded="false">

                                            </div>
                                            <ul class="dropdown-menu modal-dropdown">
                                            </ul>
                                        </div>

                                    </div>
                                </div>
                                <div class="flex gap-2 mb-4">
                                    <span class="bg-green-600 text-white px-2 py-1 rounded">xong roi nha</span>
                                    <span class="bg-yellow-600 text-white px-2 py-1 rounded"></span>
                                    <button class="bg-gray-700 text-white px-2 py-1 rounded">Watch</button>
                                </div>
                                <div class="mb-4">
                                    <h3 class="text-gray-400 font-medium mb-2 flex item-center gap-2">
                                        <svg width="20px" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512">
                                            <path fill="#B6C2CF"
                                                  d="M0 96C0 78.3 14.3 64 32 64l384 0c17.7 0 32 14.3 32 32s-14.3 32-32 32L32 128C14.3 128 0 113.7 0 96zM64 256c0-17.7 14.3-32 32-32l384 0c17.7 0 32 14.3 32 32s-14.3 32-32 32L96 288c-17.7 0-32-14.3-32-32zM448 416c0 17.7-14.3 32-32 32L32 448c-17.7 0-32-14.3-32-32s14.3-32 32-32l384 0c17.7 0 32 14.3 32 32z"/>
                                        </svg>
                                        Description
                                    </h3>
                                    <div class="btn-edit bg-blue-500 p-2 rounded-md text-white hidden w-fit ms-auto" style="cursor: pointer">Edit</div>

                                    <div class="description-title text-white bg-gray-800 text-white p-2 rounded mt-3 hidden"></div>
                                    <textarea class="w-full bg-gray-700 text-white p-2 rounded mt-1 task-area-description"
                                              placeholder="Add a more detailed description..."></textarea>

                                    <div class="hidden btn-button-group  gap-3 justify-content-end mt-4" data-url="${pageContext.request.contextPath}/dashboard/update_detail">
                                        <button class="btn-save bg-blue-500 p-2 rounded-md text-white">Lưu</button>
                                        <button class="btn-cancel bg-[#22272B] border p-2 text-white rounded-md">X</button>
                                    </div>
                                </div>
                                <div class="flex justify-between items-center mb-4">
                                    <h3 class="text-gray-400 flex item-center gap-2 fw-medium">
                                        <svg width="20px" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512">
                                            <path fill="#B6C2CF"
                                                  d="M64 144a48 48 0 1 0 0-96 48 48 0 1 0 0 96zM192 64c-17.7 0-32 14.3-32 32s14.3 32 32 32l288 0c17.7 0 32-14.3 32-32s-14.3-32-32-32L192 64zm0 160c-17.7 0-32 14.3-32 32s14.3 32 32 32l288 0c17.7 0 32-14.3 32-32s-14.3-32-32-32l-288 0zm0 160c-17.7 0-32 14.3-32 32s14.3 32 32 32l288 0c17.7 0 32-14.3 32-32s-14.3-32-32-32l-288 0zM64 464a48 48 0 1 0 0-96 48 48 0 1 0 0 96zm48-208a48 48 0 1 0 -96 0 48 48 0 1 0 96 0z"/>
                                        </svg>
                                        Activity
                                    </h3>
                                </div>
                                <div class="mb-3">
                                    <input class="w-full bg-gray-800 text-white p-2 rounded"
                                           placeholder="Write a comment...">
                                </div>
                            </div>
                            <div class="text-white pt-4 min-w-[150px]">
                                <div class="bg-gray-800 rounded p-2 px-3 flex item-center gap-2 font-medium cursor-pointer hover:bg-gray-700 ">
                                    <svg width="15px" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 512">
                                        <path fill="#B6C2CF"
                                              d="M96 128a128 128 0 1 1 256 0A128 128 0 1 1 96 128zM0 482.3C0 383.8 79.8 304 178.3 304l91.4 0C368.2 304 448 383.8 448 482.3c0 16.4-13.3 29.7-29.7 29.7L29.7 512C13.3 512 0 498.7 0 482.3zM504 312l0-64-64 0c-13.3 0-24-10.7-24-24s10.7-24 24-24l64 0 0-64c0-13.3 10.7-24 24-24s24 10.7 24 24l0 64 64 0c13.3 0 24 10.7 24 24s-10.7 24-24 24l-64 0 0 64c0 13.3-10.7 24-24 24s-24-10.7-24-24z"/>
                                    </svg>
                                    Join
                                </div>
                                <div class="bg-gray-800 rounded p-2 flex px-3 item-center gap-2 font-medium my-3 cursor-pointer hover:bg-gray-700">
                                    <svg width="15px" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512">
                                        <path fill="#B6C2CF"
                                              d="M345 39.1L472.8 168.4c52.4 53 52.4 138.2 0 191.2L360.8 472.9c-9.3 9.4-24.5 9.5-33.9 .2s-9.5-24.5-.2-33.9L438.6 325.9c33.9-34.3 33.9-89.4 0-123.7L310.9 72.9c-9.3-9.4-9.2-24.6 .2-33.9s24.6-9.2 33.9 .2zM0 229.5L0 80C0 53.5 21.5 32 48 32l149.5 0c17 0 33.3 6.7 45.3 18.7l168 168c25 25 25 65.5 0 90.5L277.3 442.7c-25 25-65.5 25-90.5 0l-168-168C6.7 262.7 0 246.5 0 229.5zM144 144a32 32 0 1 0 -64 0 32 32 0 1 0 64 0z"/>
                                    </svg>
                                    Labels
                                </div>
                                <div class="bg-gray-800 rounded p-2 px-3 flex item-center gap-2 font-medium mb-3 cursor-pointer hover:bg-gray-700">
                                    <svg width="15px" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 576 512">
                                        <path fill="#B6C2CF"
                                              d="M0 64C0 28.7 28.7 0 64 0L224 0l0 128c0 17.7 14.3 32 32 32l128 0 0 125.7-86.8 86.8c-10.3 10.3-17.5 23.1-21 37.2l-18.7 74.9c-2.3 9.2-1.8 18.8 1.3 27.5L64 512c-35.3 0-64-28.7-64-64L0 64zm384 64l-128 0L256 0 384 128zM549.8 235.7l14.4 14.4c15.6 15.6 15.6 40.9 0 56.6l-29.4 29.4-71-71 29.4-29.4c15.6-15.6 40.9-15.6 56.6 0zM311.9 417L441.1 287.8l71 71L382.9 487.9c-4.1 4.1-9.2 7-14.9 8.4l-60.1 15c-5.5 1.4-11.2-.2-15.2-4.2s-5.6-9.7-4.2-15.2l15-60.1c1.4-5.6 4.3-10.8 8.4-14.9z"/>
                                    </svg>
                                    Fields

                                </div>
                                <div class="bg-gray-800 rounded p-2 px-3 flex item-center gap-2 font-medium cursor-pointer hover:bg-gray-700">
                                    <svg width="15px" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512">
                                        <path fill="#B6C2CF"
                                              d="M128 0c17.7 0 32 14.3 32 32l0 32 128 0 0-32c0-17.7 14.3-32 32-32s32 14.3 32 32l0 32 48 0c26.5 0 48 21.5 48 48l0 48L0 160l0-48C0 85.5 21.5 64 48 64l48 0 0-32c0-17.7 14.3-32 32-32zM0 192l448 0 0 272c0 26.5-21.5 48-48 48L48 512c-26.5 0-48-21.5-48-48L0 192zm64 80l0 32c0 8.8 7.2 16 16 16l32 0c8.8 0 16-7.2 16-16l0-32c0-8.8-7.2-16-16-16l-32 0c-8.8 0-16 7.2-16 16zm128 0l0 32c0 8.8 7.2 16 16 16l32 0c8.8 0 16-7.2 16-16l0-32c0-8.8-7.2-16-16-16l-32 0c-8.8 0-16 7.2-16 16zm144-16c-8.8 0-16 7.2-16 16l0 32c0 8.8 7.2 16 16 16l32 0c8.8 0 16-7.2 16-16l0-32c0-8.8-7.2-16-16-16l-32 0zM64 400l0 32c0 8.8 7.2 16 16 16l32 0c8.8 0 16-7.2 16-16l0-32c0-8.8-7.2-16-16-16l-32 0c-8.8 0-16 7.2-16 16zm144-16c-8.8 0-16 7.2-16 16l0 32c0 8.8 7.2 16 16 16l32 0c8.8 0 16-7.2 16-16l0-32c0-8.8-7.2-16-16-16l-32 0zm112 16l0 32c0 8.8 7.2 16 16 16l32 0c8.8 0 16-7.2 16-16l0-32c0-8.8-7.2-16-16-16l-32 0c-8.8 0-16 7.2-16 16z"/>
                                    </svg>
                                    Dates
                                </div>
                            </div>


                        </div>

                    </section>
                </div>
            </div>
        </div>
    </div>


    <!----Templte Board---->
    <template id="board-item-template" data-url="${pageContext.request.contextPath}/dashboard/get_boards">
        <div class="board-item ms-2 mt-3 p-2 justify-between flex items-center gap-2 rounded" data-board-id="">
            <div class="flex items-center gap-2">
                <svg width="10px" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 384 512">
                    <path fill="white" d="M192 0c-41.8 0-77.4 26.7-90.5 64L64 64C28.7 64 0 92.7 0 128L0 448c0 35.3 28.7 64 64 64l256 0c35.3 0 64-28.7 64-64l0-320c0-35.3-28.7-64-64-64l-37.5 0C269.4 26.7 233.8 0 192 0zm0 64a32 32 0 1 1 0 64 32 32 0 1 1 0-64zM72 272a24 24 0 1 1 48 0 24 24 0 1 1 -48 0zm104-16l128 0c8.8 0 16 7.2 16 16s-7.2 16-16 16l-128 0c-8.8 0-16-7.2-16-16s7.2-16 16-16zM72 368a24 24 0 1 1 48 0 24 24 0 1 1 -48 0zm88 0c0-8.8 7.2-16 16-16l128 0c8.8 0 16 7.2 16 16s-7.2 16-16 16l-128 0c-8.8 0-16-7.2-16-16z"/>
                </svg>
                <span class="text-white board-title"></span>
            </div>
            <a data-url="${pageContext.request.contextPath}/dashboard/delete" class="delete-board">
                <svg width="15px" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512">
                    <path fill="white" d="M170.5 51.6L151.5 80l145 0-19-28.4c-1.5-2.2-4-3.6-6.7-3.6l-93.7 0c-2.7 0-5.2 1.3-6.7 3.6zm147-26.6L354.2 80 368 80l48 0 8 0c13.3 0 24 10.7 24 24s-10.7 24-24 24l-8 0 0 304c0 44.2-35.8 80-80 80l-224 0c-44.2 0-80-35.8-80-80l0-304-8 0c-13.3 0-24-10.7-24-24S10.7 80 24 80l8 0 48 0 13.8 0 36.7-55.1C140.9 9.4 158.4 0 177.1 0l93.7 0c18.7 0 36.2 9.4 46.6 24.9zM80 128l0 304c0 17.7 14.3 32 32 32l224 0c17.7 0 32-14.3 32-32l0-304L80 128zm80 64l0 208c0 8.8-7.2 16-16 16s-16-7.2-16-16l0-208c0-8.8 7.2-16 16-16s16 7.2 16 16zm80 0l0 208c0 8.8-7.2 16-16 16s-16-7.2-16-16l0-208c0-8.8 7.2-16 16-16s16 7.2 16 16zm80 0l0 208c0 8.8-7.2 16-16 16s-16-7.2-16-16l0-208c0-8.8 7.2-16 16-16s16 7.2 16 16z"/>
                </svg>
            </a>
        </div>
    </template>


    <!----Templte Task---->

    <template id="task-list-template" data-url="${pageContext.request.contextPath}/dashboard/remove_task_list">
        <div class="w-72 bg-[#000000] rounded-lg shadow-md h-fit task-list-item" data-task-id=""
             data-board-id="">
            <div class="font-bold text-xl p-3 text-white flex justify-between items-center">
                <span class="task-title ">
                </span>
                <div class="dropdown btn-option" >
                     <span class="dropdown-toggle" id="dropdownTask1"  data-bs-toggle="dropdown">
                            <svg width="24" height="24" role="presentation" focusable="false" viewBox="0 0 24 24"
                                 xmlns="http://www.w3.org/2000/svg">
                                <path fill-rule="evenodd" clip-rule="evenodd"
                                      d="M5 14C6.10457 14 7 13.1046 7 12C7 10.8954 6.10457 10 5 10C3.89543 10 3 10.8954 3 12C3 13.1046 3.89543 14 5 14ZM12 14C13.1046 14 14 13.1046 14 12C14 10.8954 13.1046 10 12 10C10.8954 10 10 10.8954 10 12C10 13.1046 10.8954 14 12 14ZM21 12C21 13.1046 20.1046 14 19 14C17.8954 14 17 13.1046 17 12C17 10.8954 17.8954 10 19 10C20.1046 10 21 10.8954 21 12Z"
                                      fill="currentColor"></path></svg>
                        </span>
                    <ul class="dropdown-menu" aria-labelledby="dropdownTask1">
                        <li><a class="dropdown-item cursor-pointer edit-task-list-item" >Edit</a></li>
                        <li><a class="dropdown-item cursor-pointer remove-task-list-item" >Remove</a></li>

                    </ul>
                </div>

            </div>

            <div class="task-child-container" data-url="${pageContext.request.contextPath}/dashboard/update_task"></div>

            <!--------Template Task CHild------->
            <template id="task-list-child-template">
            <div class="task-child-list space-y-4 p-2 " id=""  data-url="${pageContext.request.contextPath}/dashboard/get_task_child">
                <div class="task-child-group  p-2 rounded-lg bg-[#22272B] border-solid cursor-pointer flex gap-2 border-2 border-transparent hover:border-white"
                    >
                    <div>
                        <div class="text-white text-sm ">
                            <div class="bg-success text-xs fw-medium px-2 rounded w-fit task-child-label">label</div>
                        </div>
                        <h3 class="font-medium  text-white my-2 task-child-title"></h3>
                        <div class=" flex gap-1 text-white">
                            <svg width="20px" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 512">
                                <path fill="white"
                                      d="M88.2 309.1c9.8-18.3 6.8-40.8-7.5-55.8C59.4 230.9 48 204 48 176c0-63.5 63.8-128 160-128s160 64.5 160 128s-63.8 128-160 128c-13.1 0-25.8-1.3-37.8-3.6c-10.4-2-21.2-.6-30.7 4.2c-4.1 2.1-8.3 4.1-12.6 6c-16 7.2-32.9 13.5-49.9 18c2.8-4.6 5.4-9.1 7.9-13.6c1.1-1.9 2.2-3.9 3.2-5.9zM208 352c114.9 0 208-78.8 208-176S322.9 0 208 0S0 78.8 0 176c0 41.8 17.2 80.1 45.9 110.3c-.9 1.7-1.9 3.5-2.8 5.1c-10.3 18.4-22.3 36.5-36.6 52.1c-6.6 7-8.3 17.2-4.6 25.9C5.8 378.3 14.4 384 24 384c43 0 86.5-13.3 122.7-29.7c4.8-2.2 9.6-4.5 14.2-6.8c15.1 3 30.9 4.5 47.1 4.5zM432 480c16.2 0 31.9-1.6 47.1-4.5c4.6 2.3 9.4 4.6 14.2 6.8C529.5 498.7 573 512 616 512c9.6 0 18.2-5.7 22-14.5c3.8-8.8 2-19-4.6-25.9c-14.2-15.6-26.2-33.7-36.6-52.1c-.9-1.7-1.9-3.4-2.8-5.1C622.8 384.1 640 345.8 640 304c0-94.4-87.9-171.5-198.2-175.8c4.1 15.2 6.2 31.2 6.2 47.8l0 .6c87.2 6.7 144 67.5 144 127.4c0 28-11.4 54.9-32.7 77.2c-14.3 15-17.3 37.6-7.5 55.8c1.1 2 2.2 4 3.2 5.9c2.5 4.5 5.2 9 7.9 13.6c-17-4.5-33.9-10.7-49.9-18c-4.3-1.9-8.5-3.9-12.6-6c-9.5-4.8-20.3-6.2-30.7-4.2c-12.1 2.4-24.8 3.6-37.8 3.6c-61.7 0-110-26.5-136.8-62.3c-16 5.4-32.8 9.4-50 11.8C279 439.8 350 480 432 480z"/>
                            </svg>
                            <span class="text-sm"></span>
                        </div>
                    </div>
                </div>
            </div>
            </template>
            <!--------------->

            <div class="collapse !visible collapse-task-child"
                 >
                <form action="${pageContext.request.contextPath}/dashboard/task-child"
                      class="form-board card card-body !bg-black p-2 m-0" method="POST">
                    <label>
                        <input required class="border-2 w-full rounded mb-3 p-2 text-dark task-child-val" type="text" name="title"
                               placeholder="Tao Card">
                    </label>
                </form>

            </div>
            <div class="child-task-gr">
            <div class="add-child-task hidden  justify-center gap-3 items-center ms-2">
                <button type="submit" class="btn btn-primary text-center px-4 rounded-3 create-task" data-url="${pageContext.request.contextPath}/dashboard/create_task_child">Tạo</button>
                <button  class="btn text-center cancel text-white hover:bg-[#6a6a6a80]">X</button>
            </div>
            <a class="block px-3 pb-3 btn-task-child" data-bs-toggle="collapse"  >
                <button class="mt-4 w-full bg-blue-500 text-white py-2 rounded-md font-semibold task-child add-task"
                        data-list="done" data-task-id="">+ Add Task
                </button>
            </a>
            </div>
        </div>
    </template>

    <script src="${pageContext.request.contextPath}/js/index.js"></script>

</section>

</html>
