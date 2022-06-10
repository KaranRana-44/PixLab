import java.util.Collections;
///**
// * @author Mihir Patankar, Karan Rana, Kirtan Patel
// */
public class Grayscale {

    public static Picture gs(Picture input){
        Picture copy= new Picture(input);

        Pixel[][] pixels = copy.getPixels2D();
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[0].length; col++) {
                double avg= pixels[row][col].getAverage();
                pixels[row][col].setRed((int)avg);
                pixels[row][col].setBlue((int)avg);
                pixels[row][col].setGreen((int)avg);
            }
        }
        return copy;
    }

    public static void main(String[] args) {
        Picture beach= new Picture("beach.jpg");
        Picture greyscale= gs(beach);

        beach.explore();
        greyscale.explore();
    }
}
