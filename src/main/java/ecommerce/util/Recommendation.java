package ecommerce.util;

import ecommerce.business.BillEntity;
import ecommerce.business.CustomerEntity;
import ecommerce.business.LineitemsEntity;
import ecommerce.business.ProductEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.*;

public class Recommendation {

    //Recommend
    List<String> words = Arrays.asList(
            "Vintage", "Leather", "Boots", "Handmade", "Wooden", "Coffee", "Table", "Stainless",
            "Steel", "Kitchen", "Knife", "Set", "Organic", "Cotton", "Men's", "T-Shirt", "Waterproof",
            "Hiking", "Backpack", "Wireless", "Bluetooth", "Headphones", "Yoga", "Mat", "Carrying",
            "Strap", "Compact", "Portable", "Charger", "Scented", "Soy", "Wax", "Candle", "Insulated",
            "Stainless", "Steel", "Water", "Bottle", "Adjustable", "Dumbbell", "Set", "Ergonomic",
            "Office", "Chair", "Silk", "Pillowcase", "Natural", "Skin", "Care", "Kit", "Gourmet",
            "Coffee", "Beans", "Indoor", "Herb", "Garden", "Kit", "Non-Stick", "Baking", "Mat", "Set",
            "Digital", "SLR", "Camera", "Microfiber", "Quick-Dry", "Towel", "Cold", "Press", "Juicer",
            "Lightweight", "Running", "Shoes", "Ceramic", "Dinnerware", "Set", "Plush", "Throw", "Blanket",
            "Decorative", "Throw", "Pillows", "LED", "Desk", "Lamp", "Essential", "Oil", "Diffuser",
            "Velvet", "Sofa", "Cover", "Wall", "Mounted", "Shelves", "Faux", "Fur", "Rug", "Glass",
            "Food", "Storage", "Containers", "Electric", "Toothbrush", "Bamboo", "Bath", "Mat", "Stainless",
            "Steel", "Trash", "Can", "Fitness", "Tracker", "Watch", "Noise", "Cancelling", "Earbuds",
            "High", "Waist", "Leggings", "Insulated", "Lunch", "Bag", "Reusable", "Shopping", "Bags",
            "Solar", "Powered", "Garden", "Lights", "Outdoor", "Patio", "Furniture", "Set", "Charcoal",
            "Grill", "Inflatable", "Swimming", "Pool", "Hammock", "with", "Stand", "Bicycle", "Helmet",
            "Yoga", "Blocks", "Resistance", "Bands", "Kettlebell", "Set", "Electric", "Kettle", "Toaster",
            "Oven", "Blender", "Rice", "Cooker", "Slow", "Cooker", "Air", "Fryer", "Vacuum", "Cleaner",
            "Steam", "Mop", "Iron", "Board", "Laundry", "Basket", "Clothes", "Hangers", "Bed", "Sheets",
            "Comforter", "Set", "Bath", "Towels", "Shower", "Curtain", "Bathroom", "Rug", "Set",
            "Toothbrush", "Holder", "Soap", "Dispenser", "Picture", "Frames", "Wall", "Clock", "Table",
            "Lamp", "Floor", "Lamp", "Bookshelf", "TV", "Stand", "Coffee", "Table", "Dining", "Table", "Set"
    );
    private final W2V w2v = new W2V(words);

    public List<ProductEntity> recommend(List<ProductEntity> boughtProductIds, int numRecommendations) {

        double[]mainVec = new double[w2v.size()];
        for (ProductEntity product : boughtProductIds) {



            String titleWords = product.getProducttittle();
            String categoryWords = product.getCategoryName();


            double[] titleVector = w2v.getWordVector(titleWords);
            double[] categoryVector = w2v.getWordVector(categoryWords);

            for (int i = 0; i < w2v.size(); i++) {
                mainVec[i] += 0.5*titleVector[i] +  categoryVector[i];
            }

        }


        PriorityQueue<Map.Entry<ProductEntity, Double>> nearestProductsQueue = new PriorityQueue<>(
                Comparator.comparingDouble(Map.Entry::getValue)
        );


        EntityManager em =  DBUtil.getEntityManager();
        String qString = "SELECT p FROM ProductEntity p ";
        TypedQuery<ProductEntity> p = em.createQuery(qString, ProductEntity.class);
        try {
            List<ProductEntity> products = p.getResultList();
            for (ProductEntity product : products) {
                double[] title = w2v.getWordVector(product.getProducttittle());
                double[] category = w2v.getWordVector(product.getCategoryName());
                double[] productVector = new double[w2v.size()];

                for (int i=0;i<w2v.size();i++) {
                    productVector[i] = 0.5*title[i]+category[i];
                }


                double cosineSimilarity = cosineSimilarity(mainVec, productVector);

                if (nearestProductsQueue.size() < numRecommendations) {
                    nearestProductsQueue.add(new AbstractMap.SimpleEntry<>(product, cosineSimilarity));
                } else if (cosineSimilarity > nearestProductsQueue.peek().getValue()) {
                    nearestProductsQueue.poll();
                    nearestProductsQueue.add(new AbstractMap.SimpleEntry<>(product, cosineSimilarity));
                }
            }


        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }


        List<ProductEntity> recommendedProducts = new ArrayList<>();
        while (!nearestProductsQueue.isEmpty()) {
            recommendedProducts.add(0,nearestProductsQueue.poll().getKey());
        }


        return recommendedProducts;
    }

    private double cosineSimilarity(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    //W2V and recommendation run
    public List<ProductEntity> getRecommendation(CustomerEntity customer){
        EntityManager em = DBUtil.getEntityManager();
        String qString = "SELECT b FROM BillEntity b ,CustomerEntity c " +
                         "WHERE :customerId = b.customerId";
        TypedQuery<BillEntity> b = em.createQuery(qString, BillEntity.class);
        b.setParameter("customerId", customer.getCustomerid());
        try {
            List<BillEntity> bill = b.getResultList();

            List<ProductEntity> boughtProducts = new ArrayList<>();
            for (BillEntity bi : bill){
                List<LineitemsEntity> productsList = bi.getLineItems();
                for(LineitemsEntity products : productsList){
                    boughtProducts.add(products.getProduct());
                }
            }
            return recommend(boughtProducts,10);

        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
