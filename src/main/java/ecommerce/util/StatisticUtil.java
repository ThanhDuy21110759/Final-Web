package ecommerce.util;

import ecommerce.business.ProductEntity;
import ecommerce.business.ShopEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.*;

public class StatisticUtil {
    public static List<ProductEntity> getSearchProducts(String queryString){
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
    public static Map<Integer, Float> getStatisticMonthly(ShopEntity shop, Date date) {
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
}
