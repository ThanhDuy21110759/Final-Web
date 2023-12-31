package ecommerce.controller;

import java.util.*;

public class W2V {
    private Map<String, Integer> vocabulary = new LinkedHashMap<>();
    private List<double[]> sentenceVectors = new ArrayList<>();

    public void train(List<String> sentences) {
        for (String sentence : sentences) {
            for (String word : sentence.split(" ")) {
                word = word.toLowerCase();
                if (!vocabulary.containsKey(word)) {
                    vocabulary.put(word, vocabulary.size());
                }
            }
        }

        for (String sentence : sentences) {
            double[] vector = new double[vocabulary.size()];
            for (String word : sentence.split(" ")) {
                if (vocabulary.containsKey(word)) {
                    vector[vocabulary.get(word)]++;
                }
            }
            sentenceVectors.add(vector);
        }
    }

    public double[] getWordVector(String sentence) {

        double[] vector = new double[vocabulary.size()];
        for (String word : sentence.split(" ")) {
            word = word.toLowerCase();
            if (vocabulary.containsKey(word)) {
                vector[vocabulary.get(word)]++;
            }
        }
        return vector;
    }

    public boolean hasWord(String word) {
        return vocabulary.containsKey(word);
    }

    public int size(){
        return this.vocabulary.size();
    }
}
