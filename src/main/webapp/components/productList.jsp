<ul class="grid gap-4 md:gap-6 md:grid-cols-4 lg:grid-cols-6 py-4">
    <c:forEach var = "i" items="${listProduct}" >
        <li class="relative flex flex-col justify-between border min-h-[60px] rounded-lg bg-white">
            <form id="ProductDetail" type="hidden" action="productDetailServlet" method="post">
                <%-- parram value --%>
                <input type="hidden" name="productTittle" value="${i.productTittle}" />
                <input type="hidden" name="productPrice" value="${i.productPrice}" />
                <input type="hidden" name="productImg" value="${i.productImg}" />

                <button type="submit">
                    <%-- Display --%>
                    <img src="${i.productImg}" alt="Product Image"
                         class="w-full h-full min-h-[60px] object-cover mb-2 rounded-md">
                    <h2 class="text-xl px-5">${i.productTittle}</h2>
                    <p class="text-cyan-500 mb-2 px-5 ">$ ${i.productPrice}</p>
                </button>
            </form>
        </li>
    </c:forEach>
</ul>