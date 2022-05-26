import java.awt.*;
import java.util.ArrayList;

public class Steganography {
    public static void clearLow(Pixel p){
        p.setRed((p.getRed()/4)*4);
        p.setGreen((p.getGreen()/4)*4);
        p.setBlue((p.getBlue()/4)*4);
    }

    public static void setLow(Pixel p, Color c){
        p.setRed( (p.getRed()/4)*4 + (c.getRed()/64) );
        p.setGreen( (p.getGreen()/4)*4 + (c.getGreen()/64) );
        p.setBlue( (p.getBlue()/4)*4 + (c.getBlue()/64) );
    }

    public static Picture testClearLow(Picture p){
        Pixel[][] p2= p.getPixels2D();
        for(int r=0; r< p2.length; r++){
            for(int c=0; c<p2[0].length; c++){
                clearLow(p2[r][c]);
            }
        }
        return p;
    }

    public static Picture testSetLow(Picture p, Color co){
        Pixel[][] p2= p.getPixels2D();
        for(int r=0; r< p2.length; r++){
            for(int c=0; c<p2[0].length; c++){
                setLow(p2[r][c], co);
            }
        }
        return p;
    }

    public static Picture revealPicture(Picture hidden){
        Picture copy= new Picture(hidden);
        Pixel[][]pixels= copy.getPixels2D();
        Pixel[][]source= hidden.getPixels2D();
        for(int r=0; r<pixels.length; r++){
            for(int c=0; c<pixels[0].length; c++){
                Color col = source[r][c].getColor();
                int red= col.getRed()%4*64;
                int blue= col.getBlue()%4*64;
                int green=col.getGreen()%4*64;
                pixels[r][c].setRed(red);
                pixels[r][c].setBlue(blue);
                pixels[r][c].setGreen(green);
            }

        }
        return copy;
    }

    public static boolean canHide(Picture source, Picture secret){
        return (source.getHeight()>=secret.getHeight() && source.getWidth()>= secret.getWidth());
    }


    public static Picture hidePicture(Picture source, Picture secret) {
        if (!canHide(source, secret)) return source;

        Pixel[][] sourceA = source.getPixels2D();
        Pixel[][] secretA = secret.getPixels2D();

        for (int r = 0; r < secretA.length; r++) {
            for (int c = 0; c < secretA[0].length; c++) {
                setLow(sourceA[r][c], secretA[r][c].getColor());
            }
        }
        return source;
    }

    public static Picture hidePicture(Picture source, Picture secret, int startRow, int startColumn) {
        if (!canHide(source, secret)) return source;
        Picture copy=new Picture(source);

        Pixel[][] sourceA = copy.getPixels2D();
        Pixel[][] secretA = secret.getPixels2D();

        for (int r = startRow; r < secretA.length+startRow; r++) {
            for (int c = startColumn; c < secretA[0].length+startColumn; c++) {
                setLow(sourceA[r][c], secretA[r-startRow][c-startColumn].getColor());
            }
        }
        return copy;
    }

    public static boolean isSame(Picture p1, Picture p2) {
        Pixel[][] sourceA = p1.getPixels2D();
        Pixel[][] sourceB = p2.getPixels2D();

        for (int r = 0; r < sourceA.length; r++) {
            for (int c = 0; c < sourceA[0].length; c++) {
                if (!sourceA[r][c].getColor().equals(sourceB[r][c].getColor())) return false;
            }

        }
        return true;
    }

    public static ArrayList<Point> findDifferences(Picture p1, Picture p2) {
        ArrayList<Point> s = new ArrayList<Point>();
        if (isSame(p1, p2)) return s;

        if(!(p1.getHeight()== p2.getHeight()&&p1.getWidth()== p2.getWidth())) return s;

        Pixel[][] sourceA = p1.getPixels2D();
        Pixel[][] sourceB = p2.getPixels2D();

        for (int r = 0; r < sourceA.length; r++) {
            for (int c = 0; c < sourceA[0].length; c++) {
                if (sourceA[r][c].getRed()!=sourceB[r][c].getRed() || sourceA[r][c].getBlue()!=sourceB[r][c].getBlue() ||sourceA[r][c].getGreen()!=sourceB[r][c].getGreen()) {
                    s.add(new Point(r, c));

                }

            }

        }
        return s;
    }




    public static void main(String[] args) {
//        Picture beach= new Picture("beach.jpg");
//        Picture robot= new Picture("robot.jpg");
//        Picture flower1= new Picture("flower1.jpg");
//        beach.explore();
//        Picture hidden1= hidePicture(beach,robot,65,208);
//        Picture hidden2= hidePicture(hidden1, flower1,280,110);
//        hidden2.explore();
//        Picture unhidden= revealPicture(hidden2);
//        unhidden.explore();

            Picture arch = new Picture ("arch.jpg");
            Picture koala = new Picture ("koala.jpg");
            Picture robot1 = new Picture ("robot.jpg") ;
            ArrayList<Point> pointList = findDifferences(arch,arch);
            System.out.println ("PointList after comparing two identical pictures "+ "has a size "+pointList.size());
            pointList = findDifferences (arch, koala) ;
            System.out.println ("PointList after comparing two different sized pictures "+"has a size of " + pointList.size());
            Picture arch2 = hidePicture (arch, robot1, 65, 102) ;
            pointList = findDifferences (arch, arch2);
            System.out.println ("Pointlist after hiding a picture has a size of "+ pointList.size());
            arch.show();
            arch2.show();

//        Picture beach = new Picture ("beach.jpg");
//        beach.explore();
//        Picture copy= testClearLow(beach);
//        copy.explore();
//        Picture beach2 = new Picture ("beach.jpg");
//        beach2.explore();
//        Picture copy2= testSetLow(beach,Color.PINK);
//        copy2.explore();
//        Picture copy3= revealPicture(copy2);
//        copy3.explore();
    }

}
