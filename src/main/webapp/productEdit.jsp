<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Product Edit</title>

    <script src="https://cdn.tailwindcss.com"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/flowbite/2.0.0/datepicker.min.js"></script>
</head>
<body>
<form action="applyProductDataServlet" method="post" enctype="multipart/form-data">
<%--    ?shopId=${shop.getShopid()}--%>
<%--    <input type="hidden" name="action" value="${action}"/>--%>
<%--    <input type="hidden" name="shop" value="${shop}"/>--%>
<%--    <input type="hidden" name="Id" value="${product.getProductid()}"/>--%>
<%--    <input type="hidden" name="managerId" value="${user.customerid}"/>--%>

    <input type="hidden" name="updatedate" value="${product.updatedate}" />

    <div class="flex min-h-screen items-center justify-center bg-gray-100 p-6" >
        <div class="container mx-auto max-w-screen-lg">
            <div>
                <h2 class="mb-6 text-xl font-semibold text-gray-600">Product Form</h2>

                <div class="mb-6 rounded bg-white p-4 px-4 shadow-lg md:p-8">
                    <div class="grid grid-cols-1 gap-4 gap-y-2 text-sm lg:grid-cols-3">
                        <div class="text-gray-600">
                            <p class="text-lg font-medium">Product Details</p>
                            <p>Please fill out all the fields.</p>
                        </div>

                        <div class="lg:col-span-2">
                            <div class="grid grid-cols-1 gap-4 gap-y-2 text-sm md:grid-cols-5">
                                <!-- Product Info -->
                                <div class="md:col-span-5">
                                    <label for="producttittle">Title</label>
                                    <input type="text" name="producttittle" id="producttittle"
                                           class="mt-1 h-10 w-full rounded border bg-gray-50 px-4"
                                           value="${product.producttittle}" required />
                                </div>

                                <div class="md:col-span-5">
                                    <label for="productprice">Price</label>
                                    <input type="text" name="productprice" id="productprice" class="mt-1 h-10 w-full rounded border bg-gray-50 px-4" value="${product.productprice}" required />
                                </div>

                                <div class="md:col-span-5">
                                    <label for="description">Description</label>
                                    <input type="text" name="description" id="description" class="mt-1 h-10 w-full rounded border bg-gray-50 px-4" value="${product.description}" required />
                                </div>

                                <div class="md:col-span-5">
                                    <label for="categoryName">Category</label>
                                    <input onclick="dropDownButton()"
                                           type="text"
                                           name="categoryName"
                                           id="categoryName"
                                           class="dropbtn mt-1 h-10 w-full rounded border bg-gray-50 px-4" value="${product.category.name}" required />

                                    <!-- drop down content -->
                                    <div id="myDropdown" class="dropdown-content rounded-none border bg-white p-0 hidden">
                                        <div class="px-4 py-2" onclick="getDropdownContent('Phone')">Phone</div>
                                        <div class="px-4 py-2" onclick="getDropdownContent('Laptop')">Laptop</div>
                                        <div class="px-4 py-2" onclick="getDropdownContent('Cloth')">Cloth</div>
                                    </div>
                                </div>

                                <div class="md:col-span-2">
                                    <label for="numberCounter">Number</label>
                                    <div class="mt-1 flex h-10 w-28 items-center rounded border border-gray-200 bg-gray-50">
                                        <span onclick="decrement()" tabindex="-1" class="cursor-pointer border-r border-gray-200 text-gray-500 outline-none transition-all hover:text-blue-600 focus:outline-none">
                                          <svg xmlns="http://www.w3.org/2000/svg" class="mx-2 h-4 w-4" viewBox="0 0 20 20" fill="currentColor">
                                            <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd" />
                                          </svg>
                                        </span>

                                        <input onchange="changeValue(value)" name="numberCounter" id="numberCounter" placeholder="0" class="w-full appearance-none bg-transparent px-2 text-center text-gray-800 outline-none" value="${inventory.getAmount()}" />

                                        <span onclick="increment()" tabindex="-1" class="cursor-pointer border-l border-gray-200 text-gray-500 outline-none transition-all hover:text-blue-600 focus:outline-none">
                                          <svg xmlns="http://www.w3.org/2000/svg" class="mx-2 h-4 w-4 fill-current" viewBox="0 0 20 20" fill="currentColor">
                                            <path fill-rule="evenodd" d="M14.707 12.707a1 1 0 01-1.414 0L10 9.414l-3.293 3.293a1 1 0 01-1.414-1.414l4-4a1 1 0 011.414 0l4 4a1 1 0 010 1.414z" clip-rule="evenodd" />
                                          </svg>
                                        </span>
                                    </div>
                                </div>

                                <!-- Upload img -->
                                <div class="md:col-span-5">
                                    <label class="mb-5 block">
                                        Upload Product Image
                                    </label>

                                    <div class="mb-8">
                                        <input type="file" name="image" value="${product.productimg}"/>
                                    </div>

                                    <c:if test="${product.productimg != null}">
                                        <img src="data:image/jpg;base64,${product.getProductimgBase64()}">
                                    </c:if>
                                </div>

                                <div class="text-right md:col-span-5">
                                    <div class="inline-flex items-end">
                                        <button type="submit" class="rounded bg-blue-500 px-4 py-2 font-bold text-white hover:bg-blue-700">Submit</button>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>

<script>
    let data = 0;

    if(document.getElementById("numberCounter").value != null && document.getElementById("numberCounter").value !=""){
        data = parseInt(document.getElementById("numberCounter").value);
    }else{
        data = 0;
        document.getElementById("numberCounter").value = data.toString();
    }

    // Change the index of number
    function increment() {
        data = data + 1;

        document.getElementById("numberCounter").value = data.toString();
    }

    function decrement() {
        if(data > 0) {
            data = data - 1;
            document.getElementById("numberCounter").value = data.toString();
        }
    }

    function changeValue(num){
        if(num >= 0){
            data = num;
            document.getElementById("numberCounter").value = data.toString();
        }else{
            document.getElementById("numberCounter").value = 0;

            alert('Invalid input')
        }
    }

    /* When the user clicks on the button,
        toggle between hiding and showing the dropdown content */
    function dropDownButton() {
        document.getElementById("myDropdown").classList.toggle("hidden");
    }

    // Close the dropdown if the user clicks outside of it
    window.onclick = function(event) {
        if (!event.target.matches('.dropbtn')) {
            var dropdowns = document.getElementsByClassName("dropdown-content");
            var i;
            for (i = 0; i < dropdowns.length; i++) {
                var openDropdown = dropdowns[i];
                if (!openDropdown.classList.contains('hidden')) {
                    openDropdown.classList.add('hidden');
                }
            }
        }
    }

    function getDropdownContent(name){
        document.getElementById("categoryName").value = name;
    }
</script>

</body>
</html>
