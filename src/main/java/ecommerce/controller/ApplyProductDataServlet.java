package ecommerce.controller;

import ecommerce.business.CustomerEntity;
import ecommerce.business.InventoryEntity;
import ecommerce.business.ProductEntity;
import ecommerce.business.ShopEntity;
import ecommerce.data.CustomerDB;
import ecommerce.data.InventoryDB;
import ecommerce.data.ProductDB;
import ecommerce.data.ShopDB;
import ecommerce.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@MultipartConfig(maxFileSize = 16177215)
@WebServlet(name = "ApplyProductDataServlet")
public class ApplyProductDataServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException{
        String url = "/home.jsp";
        HttpSession session = request.getSession();

        String action = request.getParameter("action");
        if(action == null) action = (String) session.getAttribute("action");
        String productId = (String) session.getAttribute("Id");

        CustomerEntity customer = CustomerDB.loginedCustomer;
        ShopEntity shop = ShopDB.getShopByManagerID(customer.getCustomerid());
//        request.setAttribute("shop", shop);
//        request.setAttribute("listProduct",
//                ProductDB.getProductsByShopID(shop.getShopid()));

        url = "/shopProfile.jsp";

        // data from jsp
        String producttittle = request.getParameter("producttittle");
        String productprice = request.getParameter("productprice");
        String description = request.getParameter("description");
        String categoryName = request.getParameter("categoryName");
        String inventory = request.getParameter("numberCounter");

        String managerId = (String) session.getAttribute("managerId");

        // the upload img
        String realPath = null;
        String fileName = null;
        try{
            Part filePart = request.getPart("image");
            realPath = request.getServletContext().getRealPath("/images");
            fileName = Path.of(filePart.getSubmittedFileName()).getFileName().toString();
            if(!Files.exists(Path.of(realPath))){
                Files.createDirectory(Path.of(realPath));
            }
            filePart.write(realPath + "/" + fileName);
        }catch (Exception e){
            // lỗi
        }

        // get curent date
        Timestamp currTimestamp = new Timestamp(System.currentTimeMillis());

        // action
        if(action.equals(Constants.ACTION_INSERT)){
            ProductEntity newPro = new ProductEntity();
            InventoryEntity newInvent = new InventoryEntity();

            newPro.setProducttittle(producttittle);
            newPro.setProductprice(Double.parseDouble(productprice));
            newPro.setDescription(description);
            newPro.setCategory(ProductDB.getCategoryByName(categoryName));
            newPro.setUpdatedate(currTimestamp);
            newPro.setShop(ShopDB.getShopByManagerID(Long.parseLong(managerId)));
            if(fileName != null) newPro.setProductimg(realPath + "/" + fileName);

            ProductDB.insert(newPro);

            newInvent.setProduct(newPro.getProductid());
            newInvent.setAmount(Integer.parseInt(inventory));

            InventoryDB.insert(newInvent);
        }

        if(action.equals(Constants.ACTION_EDIT)){
            ProductEntity existPro = ProductDB.getProductById(Long.parseLong(productId));
            InventoryEntity existInvent = InventoryDB.getInventoryById(Long.parseLong(productId));

            existPro.setProducttittle(producttittle);
            existPro.setProductprice(Double.parseDouble(productprice));
            existPro.setDescription(description);
            existPro.setCategory(ProductDB.getCategoryByName(categoryName));
            existPro.setUpdatedate(currTimestamp);
            existPro.setShop(ShopDB.getShopByManagerID(Long.parseLong(managerId)));
            if(fileName != null) existPro.setProductimg(realPath + "/" + fileName);

            //existInvent.setProduct(existPro);
            existInvent.setAmount(Integer.parseInt(inventory));

            InventoryDB.update(existInvent);
            ProductDB.update(existPro);
        }

        if(action.equals(Constants.ACTION_DELETE)){
            productId = request.getParameter("Id");

            ProductEntity existPro = ProductDB.getProductById(Long.parseLong(productId));
            InventoryEntity existInvent = InventoryDB.getInventoryById(Long.parseLong(productId));

            InventoryDB.delete(existInvent);
            ProductDB.delete(existPro);
        }

        session.setAttribute("action", null);
        session.setAttribute("listProduct",
                ProductDB.getProductsByShopID(shop.getShopid()));

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
