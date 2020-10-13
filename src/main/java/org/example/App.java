package org.example;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Playing cards recognizer!
 * it takes an address as a parameter, processes the images
 * and prints the result to the console as "name - cards"
 * by BakeNezumi-01
 */
public class App {
    public static void main( String[] args ) throws IOException {
        Map digits = initDigits();
        Map signs = initSigns();

        Path path = Paths.get(args[0]);

        // * условие - "Тестирование программы будет осуществляться на аналогичных картинках"
        // * since the training sample has the same parameters,
        // * and the time is limited, it was decided to hardcode
        // * TODO: change harcode to dinamic serching of cards, if it needs

        for(File file : Objects.requireNonNull(path.toFile().listFiles())){
            BufferedImage img = ImageIO.read(file);
            StringBuilder answer = new StringBuilder();
            int[] coordinatesOfSigns = {170, 241, 313, 384, 456}; // because numbers non systematic
            int[] coordinatesOfDigits ={148, 220, 291, 364, 436}; // keeping the same method

            for(int card = 0; card < 5; card++){
                int color = img.getRGB(coordinatesOfDigits[card], 592);

                if(color == -1 || color == -8882056){ // make it parallel?
                    BufferedImage img1;

                    img1 =img.getSubimage(coordinatesOfDigits[card], 592, 28, 22);
                    monochrome(img1);
                    String digit = compareToSigns(img1, digits);

                    img1 = img.getSubimage( coordinatesOfSigns[card], 634, 30, 31);
                    monochrome(img1);
                    String sign = compareToSigns(img1, signs);

                    answer.append(digit).append(sign);
                }
            }
            System.out.println(file.getName() + " - " + answer);
        }
    }


    private static String compareToSigns(BufferedImage img1, Map signs) {
        int intMinDifference = Integer.MAX_VALUE;
        String strMinDifference = "";

        Iterator<Map.Entry<String, String>> iterator = signs.entrySet().iterator(); //  make it shorter
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            int diff = compareStrings(imgToBinaryString(img1), entry.getValue());
            if( intMinDifference > diff){
                intMinDifference = diff;
                strMinDifference = entry.getKey();
            }
        }
        return strMinDifference;
    }


    private static int compareStrings(String str1, String str2){
        int differences = 0;

        int len = str1.length();
        if( len != str2.length()) return -1;
        else{
            for(int ch = 0; ch < str1.length(); ch++){
                if(str1.charAt(ch) != str2.charAt(ch))
                    differences++;
            }
        }

        return differences;
    }

    private static String imgToBinaryString(BufferedImage img){
        StringBuilder answer = new StringBuilder();
        for (int j = 0; j < img.getHeight(); j++){
            for (int i = 0; i < img.getWidth(); i++){
                int color = img.getRGB(i, j);
                if(color == -1 || color == -8882056){
                    answer.append("0");
                }else{
                    answer.append("1");
                }
            }
        }
        return answer.toString();
    }

    private static void monochrome(BufferedImage img) {
        for (int j = 0; j < img.getHeight(); j++){
            for (int i = 0; i < img.getWidth(); i++){
                int color = img.getRGB(i, j);
                if(color == -1 || color == -8882056){
                    img.setRGB(i, j, -1);
                }else{
                    img.setRGB(i, j, -16777216);
                }
            }
        }
    }


    // if you know how to do it better, write me to ChernobrovkinEM@gmail.com, I didn’t come up with
    private static Map initDigits()  {
        Map<String, String> map = new HashMap<>();
        map.put("2", "0000000111111111000000000000000000111111111111000000000000000111111111111110000000000000111111111111111000000000000011111100011111100000000000001111100000111111000000000000001100000001111100000000000000000000001111110000000000000000000000111110000000000000000000000111111000000000000000000000011111100000000000000000000111111100000000000000000000111111100000000000000000000111111100000000000000000000111111100000000000000000000111111100000000000000000000111111000000000000000000001111111000000000000000000001111111111111111100000000000111111111111111110000000000011111111111111111000000000001111111111111111100000000");
        map.put("3", "0000111111111111111100000000000011111111111111110000000000001111111111111111000000000000111111111111111100000000000000000000111111100000000000000000000111111100000000000000000000011111100000000000000000000011111100000000000000000000011111100000000000000000000011111111000000000000000000001111111111000000000000000000111111111110000000000000000011111111111000000000000000000000001111110000000000000000000000011111000000000000001000000001111100000000000001110000000111110000000000001111100000111111000000000001111111111111111000000000000011111111111111100000000000000111111111111100000000000000001111111111100000000000");
        map.put("4", "0000000000000011111000000000000000000000011111100000000000000000000001111110000000000000000000001111111000000000000000000001111111100000000000000000001111111110000000000000000001111111111000000000000000001111111111100000000000000000111110111110000000000000000111110011111000000000000000111111001111100000000000000111111000111110000000000000111111000011111000000000000111111111111111111100000000011111111111111111110000000001111111111111111111000000000011111111111111111100000000000000000000111110000000000000000000000011111000000000000000000000001111100000000000000000000000111110000000000000000000000011111000000000");
        map.put("5", "0000011111111111111000000000000001111111111111100000000000000111111111111110000000000000011111111111111000000000000001111100000000000000000000000111110000000000000000000000011111000000000000000000000001111000100000000000000000000111111111111000000000000000011111111111110000000000000001111111111111100000000000000111111111111111000000000000000110000011111100000000000000000000000111110000000000000000000000011111000000000000000000000001111100000000000001110000000111110000000000001111100000111111000000000001111111111111111000000000000011111111111111100000000000000111111111111100000000000000001111111111100000000000");
        map.put("6", "0000000001111111110000000000000000011111111111110000000000000011111111111111000000000000001111111111111100000000000001111110000111100000000000000111110000000110000000000000111111000000000000000000000011111000110000000000000000001111111111111100000000000000111111111111111000000000000011111111111111110000000000001111111111111111100000000000111111000001111110000000000011111000000011111000000000001111100000001111100000000000111110000000111110000000000011111000000011111000000000000111110000011111100000000000011111111111111100000000000000111111111111110000000000000011111111111110000000000000000111111111110000000000");
        map.put("7", "0000111111111111111100000000000011111111111111110000000000001111111111111111000000000000111111111111111100000000000000000000001111110000000000000000000000111110000000000000000000000111111000000000000000000000111111000000000000000000000011111000000000000000000000011111100000000000000000000001111100000000000000000000001111110000000000000000000000111110000000000000000000000111111000000000000000000000011111000000000000000000000011111100000000000000000000001111100000000000000000000001111110000000000000000000000111110000000000000000000000111111000000000000000000000011111000000000000000000000011111100000000000000000");
        map.put("8", "0000000111111111100000000000000000111111111111000000000000000111111111111110000000000000011111111111111100000000000011111100001111110000000000001111100000011111000000000000111110000001111100000000000011111000000111110000000000000111111001111110000000000000011111111111111000000000000000111111111111000000000000000111111111111110000000000000111111111111111100000000000011111100001111110000000000001111100000001111100000000001111100000000111110000000000111110000000011111000000000001111100000011111100000000000111111111111111100000000000001111111111111110000000000000111111111111110000000000000001111111111110000000000");
        map.put("9", "0000000011111111100000000000000000111111111111000000000000000111111111111110000000000000011111111111111100000000000011111100000111110000000000001111100000001111100000000000111110000000111110000000000011111000000011111000000000001111100000001111100000000000111111000001111110000000000011111111111111111000000000000111111111111111100000000000001111111111111110000000000000011111111111111000000000000000011111001111100000000000000000000001111110000000000000110000000111110000000000000111100000111111000000000000011111111111111000000000000011111111111111000000000000000111111111111100000000000000001111111111100000000000");
        map.put("10", "0001111110000001111111110000111111111000011111111111110011111111100011111111111111101111111110011111111111111110111111111001111110000111111110001111101111110000001111110000111110111110000000011111000011111011111000000000111100001111111111100000000011110000111111111100000000001111000011111111110000000000111100001111111111000000000011110000111111111100000000001111000011111111111000000000111100001111101111100000000011110000111110111110000000011111000011111011111100000001111100001111100111111000011111110000111110011111111111111110000011111000111111111111111000001111100001111111111111000000111110000011111111110000");
        map.put("J", "0000000000000111110000000000000000000000011111000000000000000000000001111100000000000000000000000111110000000000000000000000011111000000000000000000000001111100000000000000000000000111110000000000000000000000011111000000000000000000000001111100000000000000000000000111110000000000000000000000011111000000000000000000000001111100000000000000000000000111110000000000000000000000011111000000000000000000000001111100000000000000000000001111110000000000000011100000111110000000000000011111000111111000000000000001111111111111100000000000000111111111111100000000000000001111111111110000000000000000011111111110000000000000");
        map.put("Q", "0000000000111111111110000000000000001111111111111100000000000001111111111111111100000000001111111111111111111000000000111111100000111111110000000111111000000001111111000000111111000000000011111110000011111100000000000111111000001111100000000000001111100000111110000000000000111111000011111000000000000011111100001111100000000110001111110000111110000000111100111111000011111000000111111011111000001111110000011111111111100000111111000000111111111110000001111110000001111111110000000111111110000011111111000000001111111111111111111110000000011111111111111111111100000000111111111111111111100000000001111111111110011110");
        map.put("K", "0000011110000000001111110000000001111000000001111110000000000111100000001111110000000000011110000001111111000000000001111000001111111000000000000111100001111111000000000000011110001111111000000000000001111001111111000000000000000111101111111000000000000000011110111111000000000000000001111111111100000000000000000111111111111000000000000000011111111111110000000000000001111111011111100000000000000111111001111110000000000000011111000011111100000000000001111000000111111000000000000111100000001111110000000000011110000000111111000000000001111000000001111110000000000111100000000011111100000000011110000000001111111000");
        map.put("A", "0000000000111110000000000000000000000111111000000000000000000000011111100000000000000000000011111111000000000000000000001111111100000000000000000001111111111000000000000000000111111111100000000000000000111110011111000000000000000011111001111100000000000000001111100111111000000000000001111100001111100000000000000111110000111110000000000000111110000001111100000000000011111111111111110000000000011111111111111111100000000001111111111111111110000000000111111111111111111100000000111110000000000111110000000011111000000000011111100000011111000000000000111110000001111100000000000011111100000111110000000000001111110000");
        return map;
    }
    private static Map initSigns(){
        Map<String, String> map = new HashMap<>();
        map.put("c", "000000000000111110000000000000000000000011111111100000000000000000000111111111110000000000000000001111111111111000000000000000011111111111111000000000000000011111111111111100000000000000011111111111111100000000000000011111111111111100000000000000011111111111111100000000000000011111111111111100000000000000011111111111111000000000000000001111111111111000000000000001111111111111111111100000000111111111111111111111111000001111111111111111111111111100011111111111111111111111111110111111111111111111111111111110111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111110011111111111111111111111111110001111111111111111111111111100000111111111011101111111111000000011111100111110011111100000000000000001111111000000000000000000000011111111100000000000000000000111111111110000000000");
        map.put("d", "000000000000011100000000000000000000000000111110000000000000000000000001111111000000000000000000000001111111100000000000000000000011111111110000000000000000000111111111111000000000000000001111111111111100000000000000011111111111111110000000000000111111111111111110000000000001111111111111111111100000000011111111111111111111110000000111111111111111111111111000001111111111111111111111111100011111111111111111111111111110111111111111111111111111111110111111111111111111111111111111111111111111111111111111111111011111111111111111111111111110001111111111111111111111111100000111111111111111111111111000000011111111111111111111110000000001111111111111111111100000000001111111111111111111000000000000111111111111111110000000000000011111111111111100000000000000001111111111111000000000000000000111111111111000000000000000000011111111110000000000000000000011111111100000000000000000000001111111000000000000000000000000111110000000000000");
        map.put("h", "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001111110000000111111000000000111111111100011111111111000001111111111110111111111111100011111111111111111111111111110111111111111111111111111111110111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111110111111111111111111111111111110011111111111111111111111111100001111111111111111111111111100000111111111111111111111111000000111111111111111111111110000000011111111111111111111100000000000111111111111111111000000000000011111111111111110000000000000001111111111111100000000000000000111111111111000000000000000000011111111100000000000000000000001111111000000000000000000000000111110000000000000");
        map.put("s", "000000000000000000000000000000000000000000001100000000000000000000000000011110000000000000000000000000111111000000000000000000000001111111100000000000000000000011111111110000000000000000000111111111111000000000000000001111111111111100000000000000011111111111111110000000000000111111111111111111000000000011111111111111111111100000000111111111111111111111110000001111111111111111111111111000001111111111111111111111111100011111111111111111111111111110111111111111111111111111111110111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111110111111111111111111111111111110011111111111111101111111111100000111111111011100111111111000000001111100011110001111100000000000000000111110000000000000000000000001111111000000000000000000001111111111111000000000");
        return map;
    }

}
