package ecommerce.controller;

import ecommerce.business.*;
import ecommerce.util.DBUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.*;



import ecommerce.business.*;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class API {
    private final W2V w2v = new W2V();

    public API(){
        List<String> word_to_train = new ArrayList<String>();

        try{
            File myObj = new File("META-INF/corpus.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String word = myReader.nextLine();
                word_to_train.add(word);
            }
            myReader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        w2v.train(word_to_train);
    }


    //Recomendation --> homepage list after login
    public List<ProductEntity> getRecommendation(CustomerEntity customer){
        EntityManager em = DBUtil.getEntityManager();
        String qString = "SELECT b FROM BillEntity b ,CustomerEntity c WHERE :customer = BillEntity.customerId";
        TypedQuery<BillEntity> b = em.createQuery(qString, BillEntity.class);
        try {
            List<BillEntity> bill = b.getResultList();

            List<ProductEntity> boughtProducts = new ArrayList<>();
            for (BillEntity bi : bill){
                List<LineitemsEntity> productsList = bi.getLineItems();
                for(LineitemsEntity products : productsList){
                    boughtProducts.add(products.getProduct());
                }

            }

            recommedation recommender = new recommedation(w2v);


            return recommender.recommend(boughtProducts,32); //4*8

        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }


    //SearchEngine --> servlet product (action listSearch)

    public List<ProductEntity> getSearchProducts(String queryString) {
        EntityManager em = DBUtil.getEntityManager();
        String qString = "SELECT p FROM ProductEntity p WHERE p.producttittle LIKE :queryString";
        TypedQuery<ProductEntity> query = em.createQuery(qString, ProductEntity.class);
        query.setParameter("queryString", "%" + queryString + "%");

        List<ProductEntity> searchResults = new ArrayList<>();
        try {
            searchResults = query.getResultList();
        } catch (NoResultException e) {
            System.out.println(e);
        } finally {
            em.close();
        }

        return searchResults;
    }



    public Map<Integer, Float> getStatisticMonthly(ShopEntity shop, Date date) {
        EntityManager em = DBUtil.getEntityManager();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String qString = "SELECT FUNCTION('MONTH', b.billdate), SUM(li.product.productprice * li.amount) FROM BillEntity b JOIN b.lineItems li WHERE li.product.shop.id= :ShopID AND FUNCTION('YEAR', b.billdate) = :year AND FUNCTION('MONTH', b.billdate) = :month GROUP BY FUNCTION('MONTH', b.billdate)";
        TypedQuery<Object[]> query = em.createQuery(qString, Object[].class);
        query.setParameter("ShopID", shop.getShopid());
        query.setParameter("year", year);
        query.setParameter("month", month);

        List<Object[]> results = query.getResultList();
        Map<Integer, Float> statistics = new HashMap<>();
        for (Object[] result : results) {
            statistics.put((Integer) result[0], ((Number) result[1]).floatValue());
        }

        em.close();
        return statistics;
    }




    public Map<Date, Float> getStatisticByCat(ShopEntity shop, Date date, String catName) {
        EntityManager em = DBUtil.getEntityManager();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        String qString = "SELECT FUNCTION('MONTH', b.billdate), SUM(li.product.productprice * li.amount) FROM BillEntity b JOIN b.lineItems li WHERE li.product.shop.id = :ShopID AND li.product.category.name = :catName AND FUNCTION('YEAR', b.billdate) = :year AND FUNCTION('MONTH', b.billdate) = :month GROUP BY FUNCTION('MONTH', b.billdate)";
        TypedQuery<Object[]> query = em.createQuery(qString, Object[].class);
        query.setParameter("ShopID", shop.getShopid());
        query.setParameter("catName", catName);
        query.setParameter("year", year);
        query.setParameter("month", month);

        List<Object[]> results = query.getResultList();
        Map<Date, Float> statistics = new HashMap<>();
        for (Object[] result : results) {
            statistics.put((Date) result[0], ((Number) result[1]).floatValue());
        }

        em.close();
        return statistics;
    }
}