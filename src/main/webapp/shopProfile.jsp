<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${shop.getShopname()} Profile</title>

    <script src="https://cdn.tailwindcss.com"></script>
    <script src="https://kit.fontawesome.com/07e390e8c9.js" crossorigin="anonymous"></script>
</head>
<body>
<%@include file="components/navbar.jsp" %>

<div class="bg-light/30 draggable relative mb-6 flex w-full min-w-0 flex-col break-words rounded-2xl border border-dashed border-stone-200 bg-clip-border">
    <!-- card body -->
    <div class="min-h-[70px] flex-auto bg-transparent px-9 pb-0 pt-9">
        <div class="mb-6 flex flex-wrap xl:flex-nowrap">
            <div class="mb-5 mr-5">
                <div class="relative inline-block shrink-0 rounded-2xl">
                    <img class="inline-block h-[80px] w-[80px] shrink-0 rounded-2xl lg:h-[160px] lg:w-[160px]" src="https://raw.githubusercontent.com/Loopple/loopple-public-assets/main/riva-dashboard-tailwind/img/avatars/avatar1.jpg" alt="image" />

                    <div class="group/tooltip relative">
                        <span class="absolute bottom-0 end-0 -mb-1 -mr-2 h-[15px] w-[15px] rounded-full border border-white bg-green-500"></span>
                        <span class="text-secondary-inverse absolute bottom-0 start-full z-10 -mb-2 ml-4 block transform whitespace-nowrap rounded-2xl bg-white px-3 py-2 text-center text-xs font-medium opacity-0 shadow-sm transition-opacity duration-300 ease-in-out group-hover/tooltip:opacity-100"> Status: Active </span>
                    </div>
                </div>
            </div>
            <div class="grow">
                <div class="mb-2 flex flex-wrap items-start justify-between">
                    <div class="flex flex-col">
                        <div class="mb-2 flex items-center">
                            <a class="text-secondary-inverse hover:text-primary mr-1 text-[1.5rem] font-semibold transition-colors duration-200 ease-in-out">${shop.getShopname()} </a>
                        </div>
                        <div class="mb-4 inline pr-2 font-medium">
                            <a class="text-secondary-dark hover:text-primary mb-2 mr-5 flex items-center">
                                <span class="mr-1"> <i class="fa-solid fa-location-dot"></i> </span>
                                Address: ${shop.getShoplocation()}
                            </a>
                        </div>
                    </div>

                    <%--Go to Edit product with action INSERT--%>
                    <form action="editProductServlet?action=INSERT" method="post" class="my-auto flex flex-wrap">
<%--                        <input type="hidden" name="action" value=""/>--%>
                        <button type="submit"
                                class="hover:bg-light-dark active:bg-light-dark focus:bg-light-dark mr-3 inline-block cursor-pointer rounded-2xl border bg-blue-200 px-6 py-3 text-center align-middle text-base font-medium leading-normal shadow-none transition-colors duration-150 ease-in-out"
                        >Add new Product</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
