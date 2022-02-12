package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;


public class Main {
    public static int letterCount = 5;
    public static ArrayList<String> sols;
    public static ArrayList<String> possibleGuesses;
    public static void main(String[] args) throws Exception{

        BufferedReader bf = new BufferedReader (new FileReader("src/com/company/ofsolutions.txt"));
        String solsLine = bf.readLine();
        sols = new ArrayList<>(Arrays.asList(solsLine.split(",")));

        bf = new BufferedReader(new FileReader("src/com/company/ofguesses.txt"));
        String guessLine = bf.readLine();
        possibleGuesses = new ArrayList<>(Arrays.asList(guessLine.split(",")));

        //first();
        //4: aero
        //5: serai
        //8: lactones

        Scanner sc = new Scanner(System.in);
        for(int i = 6; i > 0; i--) {

            String myGuess = sc.nextLine().toLowerCase();
            String myClue = sc.nextLine().toUpperCase();


            removeBadWords(myGuess, myClue);

            int leastBadScore = 0;
            String leastBadWord = "";
            for (String w : possibleGuesses) {
                int bs = calculateBadnessScore(w);
                if (leastBadScore != 0) {
                    if (bs < leastBadScore) {
                        leastBadScore = bs;
                        leastBadWord = w;
                    }
                } else {
                    leastBadScore = bs;
                    leastBadWord = w;
                }
            }
            System.out.println(leastBadWord + " " + leastBadScore);
            System.out.println(sols);
        }
    }

    public static void first() {
        int leastBadScore = 0;
        String leastBadWord = "";
        for (String w : possibleGuesses) {
            int bs = calculateBadnessScore(w);
            if (leastBadScore != 0) {
                if (bs < leastBadScore) {
                    leastBadScore = bs;
                    leastBadWord = w;
                }
            } else {
                leastBadScore = bs;
                leastBadWord = w;
            }
        }
        System.out.println(leastBadWord + " " + leastBadScore);
    }



    public static String convertToClue(String input, String answer) {
        StringBuilder resultBuilder = new StringBuilder();
        String result = "";
        for(int i = 0; i < letterCount; i++) {
            char il = input.charAt(i);
            char al = answer.charAt(i);
            if(il == al) resultBuilder.append('G');
            else if(answer.indexOf(il)!=-1 && result.indexOf('Y')==-1) resultBuilder.append('Y');
            else resultBuilder.append('B');
            result = resultBuilder.toString();

        }
        return result;
    }

    public static HashMap<String,Integer> groupCounts(String input) {
        HashMap<String,Integer> gCounts = new HashMap<>();
        for(String w : sols) {
            String clue = convertToClue(input, w);
            if(gCounts.containsKey(clue)) gCounts.put(clue, gCounts.get(clue)+1);
            else gCounts.put(clue, 1);
        }
        return gCounts;
    }

    public static int calculateBadnessScore(String input) {
        int score = 0;
        HashMap<String,Integer> clueBadnesses = groupCounts(input);
        Map.Entry<String, Integer> maxEntry = null;
        for(Map.Entry<String, Integer> entry : clueBadnesses.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }
        if (maxEntry != null) {
            score = maxEntry.getValue();
        }

        return score;
    }

    public static void removeBadWords(String input, String gClue) {
        ArrayList<String> guesses = sols;
        for(int i = 0; i < letterCount; i++) {
            char letter = input.charAt(i);
            switch (gClue.charAt(i)) {
                case 'G' -> {
                    for (int j = 0; j < sols.size(); j++) {
                        if (sols.get(j).charAt(i) != letter) { //if letter at index is not correct
                            guesses.remove(sols.get(j));
                            j--;
                        }
                    }
                    sols = guesses;
                }
                case 'Y' -> {
                    for (int j = 0; j < sols.size(); j++) {
                        if (sols.get(j).indexOf(letter) == -1 || sols.get(j).charAt(i) == letter) { //if letter isn't in the word OR letter is same at index
                            guesses.remove(sols.get(j));
                            j--;
                        }
                    }
                    sols = guesses;
                }
                case 'B' -> {
                    for (int j = 0; j < sols.size(); j++) {
                        int inputNum = 0, solNum = 0;
                        for (int k = 0; k < letterCount; k++) {
                            if(input.charAt(k) == letter && (gClue.charAt(k) == 'Y' || gClue.charAt(k) == 'G')) inputNum++;
                            if(sols.get(j).charAt(k) == letter) solNum++;
                        }
                        if (sols.get(j).indexOf(letter) != -1 && inputNum!=solNum) { //if word contains letter
                            guesses.remove(sols.get(j));
                            j--;
                        }
                    }
                    sols = guesses;
                }

            }
        }

    }
    public static HashMap<Character,Integer> sort(HashMap<Character,Integer> map) {
        List<Map.Entry<Character,Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);

        HashMap<Character, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<Character, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public static void randomWord() {
        Random r = new Random();
        int randomNumber = r.nextInt(sols.size());
        System.out.println(sols.get(randomNumber));
    }
}
