import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

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
                    Point p= new Point(r,c);
                    s.add(p);
                }
            }
        }
        return s;
    }

    public static Picture showDifferentArea(Picture p1, ArrayList<Point> d){
        int xmin=p1.getWidth();
        int xmax=0;
        int ymin=p1.getHeight();
        int ymax=0;

        for(Point a:d){
            if(a.getX()<xmin) xmin=a.getX();
            if(a.getX()>xmax) xmax=a.getX();
            if(a.getY()<ymin) ymin=a.getY();
            if(a.getY()>ymax) ymax=a.getY();
        }


        Picture copy= new Picture(p1);
        Pixel[][] pix1= copy.getPixels2D();
        for(int r=xmin; r<=xmax; r++){
            for(int c=ymin; c<=ymax; c++){

                if((r==xmin||r==xmax)||(c==ymin||c==ymax)){
                    pix1[r][c].setColor(Color.black);
                }
            }
        }
        return copy;
    }

    public static ArrayList<Integer> encodeString(String s){
        s = s.toUpperCase();
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ";
        ArrayList<Integer> result=new ArrayList<Integer>();
        for(int i=0; i<s.length(); i++){
            result.add(alpha.indexOf(s.charAt(i))+1);
        }
        result.add(0);
        return result;
    }

    public static String decodeString(ArrayList<Integer> codes){
        String result="";
        String alpha="ABCDEFGHIJKLMNOPQRSTUVWXYZ ";
        for(int i=0; i<codes.size(); i++){
            if(codes.get(i)==0)return result;
            result+= alpha.charAt(codes.get(i)-1);
        }
        return result;
    }

    private static int[] getBitPairs(int num){
        int[] arr= new int[3];
        arr[2]=num/16;
        arr[1]=num/4%4;
        arr[0]=num%4;
        return arr;
    }

    public static Picture hideText(Picture source, String s) {
        Picture copy= new Picture(source);
        Pixel[][] pixels= copy.getPixels2D();
        ArrayList<Integer> arrayList= encodeString(s);
        int[][]arr= new int[encodeString(s).size()][3];
        int x=0;


        for(int i=0; i<encodeString(s).size(); i++){
                arr[i] = getBitPairs(arrayList.get(i));
        }

        for (int row=0; row< source.getHeight(); row++){
            for (int col=0; col<source.getWidth(); col++){
                pixels[row][col].setRed(arr[x][0]);
                pixels[row][col].setGreen(arr[x][1]);
                pixels[row][col].setBlue(arr[x][2]);
                x++;


                if(x>s.length())return copy;
            }
        }
        return copy;

    }
//
    public static String revealText(Picture source){
        Picture copy= new Picture(source);
        Pixel[][] pixels= copy.getPixels2D();
        ArrayList<Integer>encoded= new ArrayList<Integer>();



        for (int row=0; row< pixels.length; row++){
            for (int col=0; col<pixels[0].length; col++){
                Pixel p=pixels[row][col];
                int x=p.getRed()%4+p.getGreen()%4*4+p.getBlue()%4*16;
                encoded.add(x);
                System.out.println("ping");
                if(x==0){return decodeString(encoded);}

            }
        }
        return decodeString(encoded);
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

//            Picture arch = new Picture ("arch.jpg");
//            Picture koala = new Picture ("koala.jpg");
//            Picture robot1 = new Picture ("robot.jpg") ;
//            ArrayList<Point> pointList = findDifferences(arch,arch);
//            System.out.println ("PointList after comparing two identical pictures "+ "has a size "+pointList.size());
//            pointList = findDifferences (arch, koala) ;
//            System.out.println ("PointList after comparing two different sized pictures "+"has a size of " + pointList.size());
//            Picture arch2 = hidePicture (arch, robot1, 65, 102) ;
//            pointList = findDifferences (arch, arch2);
//            System.out.println ("Pointlist after hiding a picture has a size of "+ pointList.size());
//            arch.show();
//            arch2.show();
//        Picture hall = new Picture ("femaleLionAndHall.jpg") ;
//        Picture robot2 =  new Picture ("robot.jpg") ;
//        Picture flower2 = new Picture ("flower1.jpg") ;
//        Picture hall2 = hidePicture (hall, robot2, 50, 300) ;
//        Picture hall3= hidePicture(hall2, flower2, 115, 275);
//        hall3.explore();
//        if(!isSame(hall, hall3)) {
//            Picture hall4 = showDifferentArea(hall, findDifferences(hall, hall3));
//            hall4.show();
//            Picture unhiddenHall3 = revealPicture(hall3);
//            unhiddenHall3.show();
//        }
        String preamble = "We the People of the United States in Order to form a more perfect Union establish Justice insure domestic Tranquility provide for the common defence promote the general Welfare and secure the Blessings of Liberty to ourselves and our Posterity do ordain and establish this Constitution for the United States of America";

        Picture beach = new Picture ("beach.jpg");
        beach.explore();

        Picture hide= hideText(beach, preamble);
        hide.explore();

        System.out.println(revealText(hide));
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
