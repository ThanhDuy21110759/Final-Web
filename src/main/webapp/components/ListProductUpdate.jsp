<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="ecommerce.data.ProductDB" %>
<%@ page import="ecommerce.business.ShopEntity" %>
<%@ page import="ecommerce.data.ShopDB" %>
<%@ page import="ecommerce.business.CustomerEntity" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="mx-auto my-4 flex flex-col justify-center">
    <div class="flex max-w-[1400px] flex-wrap">
        <c:choose>
            <c:when test="${empty listProduct}"> Không có sản phẩm nào </c:when>
            <c:otherwise>
                <c:forEach items="${listProduct}" var="item">
                    <a class="relative m-5 h-auto w-72 rounded-lg bg-gray-300 p-5 text-white"
<%--                       href=" ${pageContext.request.contextPath}/productDetailServlet?Id=${item.getProductid()}"--%>
                    >
                        <div>
                            <img src="data:image/jpg;base64,${item.getProductimgBase64()}" alt="product-img" class="h-48 w-full rounded-md bg-black object-cover" />

                            <div class="overflow-hidden py-3 text-xl text-white">
                                <h3>${item.getProducttittle()}</h3>
                            </div>
                            <div class="mb-3 text-base text-blue-400">$${item.getProductprice()}</div>

                            <div class="grid grid-cols-2 gap-4">
                                <form action="editProductServlet?Id=${item.getProductid()}&action=EDIT" method="post"
                                      class="rounded-lg bg-blue-400 p-2 text-center text-white">
                                    <button type="submit">Update</button>
                                </form>

                                <form action="applyProductDataServlet?Id=${item.getProductid()}&action=DELETE" method="post"
                                      class="rounded-lg bg-blue-400 p-2 text-center text-white">
                                    <button type="submit">Delete</button>
                                </form>
                            </div>
                        </div>
                    </a>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>
