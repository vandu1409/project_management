
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html>
<%@ include file="/views/common.jsp" %>

<body>
<section class="bg-gray-900">

    <div class="min-h-screen flex flex-col items-center justify-center py-6 px-4 gap-2">
        <div class="flex justify-center">
            <%@ include file="/views/icon.html" %>
        </div>
        <div class="group-button">
            <%--            <div class="button-item active btn-login">Login</div>--%>
            <%--            <div class="button-item btn-register">Register</div>--%>
        </div>

        <div class="form-wrapper">
            <div class="form-container login-form active">
                <!----Login Form---->
                <div class="mx-auto  items-center gap-10 max-w-6xl max-md:max-w-md w-full flex justify-center">

                    <form action="${pageContext.request.contextPath}/register"  method="POST"
                    class="rounded-lg shadow-sm form-send max-w-md md:mx-auto w-full p-6" >
                    <h3 class="text-white lg:text-3xl text-2xl font-bold mb-5">
                        Register to our platform
                    </h3>

                    <p class="bg-red-400 rounded text-white text-center mb-2">${errorMessage}</p>
                    <div class="space-y-6">
                        <div>
                            <label class='text-sm text-white font-medium mb-2 block'>Email</label>
                            <input name="email" type="email" required class="bg-slate-100 w-full text-sm text-white px-4 py-3 rounded-md outline-none border focus:border-blue-600 focus:bg-transparent" placeholder="Enter Email" />
                        </div>
                        <div>
                            <label class='text-sm text-white font-medium mb-2 block'>Password</label>
                            <input name="password" type="password" required class="bg-slate-100 w-full text-sm text-white px-4 py-3 rounded-md outline-none border focus:border-blue-600 focus:bg-transparent" placeholder="Enter Password" />
                        </div>
<%--                        <div class="flex flex-wrap items-center justify-between gap-4">--%>
<%--                            <div class="flex items-center">--%>
<%--                                <input id="remember-me" name="remember-me" type="checkbox" class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-slate-300 rounded" />--%>
<%--                                <label for="remember-me" class="ml-3 block text-sm text-slate-500">--%>
<%--                                    Remember me--%>
<%--                                </label>--%>
<%--                            </div>--%>
<%--                            <div class="text-sm">--%>
<%--                                <a href="jajvascript:void(0);" class="text-blue-600 hover:text-blue-500 font-medium">--%>
<%--                                    Forgot your password?--%>
<%--                                </a>--%>
<%--                            </div>--%>
<%--                        </div>--%>
                    </div>

                    <div class="!mt-12">
                        <button type="submit"
                                class="flex items-center gap-2 justify-center w-full shadow-xl py-2.5 px-4 text-sm font-semibold rounded text-white bg-blue-600 hover:bg-blue-700 focus:outline-none">
                            Register
                        </button>
                    </div>
                    <p class="text-sm mt-12 text-slate-500">Don't have an account <a href="" class="text-blue-600 font-medium hover:underline ml-1">Register here</a></p>

                    </form>
                </div>
            </div>

        </div>
</section>

</body>
</html>
