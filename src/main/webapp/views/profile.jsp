
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<%@ include file="/views/common.jsp" %>

<body class="bg-gray-100 p-6">
<div class="max-w-md mx-auto bg-white shadow-lg rounded-xl p-8">
    <h2 class="text-2xl font-semibold mb-4 text-gray-800">User Profile</h2>
    <div class="flex justify-center mb-6">
        <img src="https://via.placeholder.com/100" alt="Avatar" class="w-24 h-24 rounded-full shadow-md">
    </div>
    <form action="#" method="POST" class="space-y-4">
        <div>
            <label for="name" class="block text-gray-600">Full Name</label>
            <input type="text" id="name" name="name" placeholder="John Doe" class="w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
        </div>
        <div>
            <label for="email" class="block text-gray-600">Email</label>
            <input type="email" id="email" name="email" placeholder="john.doe@example.com" class="w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
        </div>
        <div>
            <label for="phone" class="block text-gray-600">Phone Number</label>
            <input type="tel" id="phone" name="phone" placeholder="(123) 456-7890" class="w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
        </div>
        <div>
            <label for="address" class="block text-gray-600">Address</label>
            <input type="text" id="address" name="address" placeholder="123 Main St" class="w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
        </div>
        <button type="submit" class="w-full bg-blue-500 text-white py-2 rounded-lg hover:bg-blue-600">Save</button>
    </form>
</div>
</body>
</html>
