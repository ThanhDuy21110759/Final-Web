package ecommerce.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import ecommerce.business.BillEntity;
import ecommerce.business.CustomerEntity;
import ecommerce.business.ProductEntity;
import ecommerce.business.ShopEntity;
import ecommerce.data.BillDB;
import ecommerce.data.CustomerDB;
import ecommerce.data.ProductDB;
import ecommerce.data.ShopDB;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "ProfileServlet")
public class ProfileServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String url = "/userProfile.jsp";
        HttpSession session = request.getSession();

        String action = request.getParameter("action");

        // data
        CustomerEntity customer = CustomerDB.loginedCustomer;
        ShopEntity shop = ShopDB.getShopByManagerID(customer.getCustomerid());

        if(action != null) {
            request.setAttribute("shop", shop);

            if (action.equals("userProfile")) {

                request.setAttribute("user", customer);

                //Danh sách sản phẩm
                List<ProductEntity> listProduct = ShopDB.getProductsByShop(shop.getShopid());
                if (shop != null) request.setAttribute("listProduct", listProduct);

                url = "/userProfile.jsp";
            }
        }
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }


    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
