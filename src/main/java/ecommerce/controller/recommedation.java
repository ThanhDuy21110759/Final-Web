package ecommerce.controller;

import java.util.*;

import ecommerce.business.*;
import ecommerce.util.DBUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class recommedation {
    private W2V w2v;
//    private Map<Long, Product> products;

    public recommedation(W2V w2v) {
        this.w2v = w2v;
//        this.products = new HashMap<>();
//        for (Product product : productList) {
//            this.products.put(product.getProductID(), product);
//        }
    }

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
}

