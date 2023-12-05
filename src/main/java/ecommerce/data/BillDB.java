package ecommerce.data;

import ecommerce.business.BillEntity;
import ecommerce.business.LineitemsEntity;
import ecommerce.business.ProductEntity;
import ecommerce.util.DBUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BillDB {
    public static void insert(BillEntity bill) {
        EntityManager em = DBUtil.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        try {
            em.persist(bill);
            trans.commit();
        } catch (Exception e) {
            System.out.println(e);
            trans.rollback();
        } finally {
            em.close();
        }
    }

    public static void update(BillEntity bill) {
        EntityManager em = DBUtil.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        try {
            em.merge(bill);
            trans.commit();
        } catch (Exception e) {
            System.out.println(e);
            trans.rollback();
        } finally {
            em.close();
        }
    }

    public static void delete(BillEntity bill) {
        EntityManager em = DBUtil.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        try {
            em.remove(em.merge(bill));
            trans.commit();
        } catch (Exception e) {
            System.out.println(e);
            trans.rollback();
        } finally {
            em.close();
        }
    }
    public static BillEntity getBillById(Long billId){
        EntityManager em = DBUtil.getEntityManager();
        try {
            return em.find(BillEntity.class, billId);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            em.close();
        }
        return null;
    }
    public static boolean checkBill(Long productId){
        EntityManager em = DBUtil.getEntityManager();
        try {
            // Tìm tất cả các LineitemEntity chứa product
            String q = "SELECT l FROM LineitemsEntity l WHERE l.product.productid = :productId";
            TypedQuery<LineitemsEntity> query = em.createQuery(q, LineitemsEntity.class);
            query.setParameter("productId", productId);
            List<LineitemsEntity> lineItems = query.getResultList();

            // Kiểm tra từng LineitemsEntity
            for (LineitemsEntity lineItem : lineItems) {
                BillEntity bill = BillDB.getBillById(lineItem.getBillId());
                if (bill != null) {
                    String status = bill.getStatus();
                    if ("INQUEUE".equals(status)) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            em.close();
        }
        return false;
    }
    public static Long lastId() {
        EntityManager em = DBUtil.getEntityManager();
        try {
            Query query = em.createQuery("SELECT MAX(b.billid) FROM BillEntity b");
            Long lastId = (Long) query.getSingleResult();
            return lastId + 1;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            em.close();
        }
    }

}
