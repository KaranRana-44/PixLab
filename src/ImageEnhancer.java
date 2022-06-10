//import java.awt.*;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//
///**
// * @author Mihir Patankar, Karan Rana, Kirtan Patel
// */
//public class ImageEnhancer {
//
//    private ArrayList<Integer> alphas;
//
//    public Picture enhance(Picture input){
//        Pixel[][] pixels = input.getPixels2D();
//        for (int row = 0; row < pixels.length; row++) {
//            for (int col = 0; col < pixels[0].length; col++) {
//                alphas.add(pixels[row][col].getAlpha());
//            }
//            Collections.sort(alphas);
//        }
//        int medianAlpha = getMedianValue(alphas);
//    }
//
//    private int getMedianValue(ArrayList<Integer> a) {
//        if (a.size() % 2 == 0)
//            return (a.get(a.size() / 2) + a.get((a.size() / 2) - 1)) / 2;
//        else
//            return a.get(a.size() / 2);
//    }
//
//
//    public static void main(String[] args) {
//        ImageEnhancer imageEnhancer = new ImageEnhancer();
//
//    }
//
//}
