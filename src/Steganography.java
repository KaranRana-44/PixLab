import java.awt.Color;

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
        return (source.getHeight()==secret.getHeight() && secret.getWidth()== secret.getWidth());
    }


    public static Picture hidePicture(Picture source, Picture secret){
        Picture merge= new Picture();

    }


    public static void main(String[] args) {
        Picture beach = new Picture ("beach.jpg");
        beach.explore();
        Picture copy= testClearLow(beach);
        copy.explore();
        Picture beach2 = new Picture ("beach.jpg");
        beach2.explore();
        Picture copy2= testSetLow(beach,Color.PINK);
        copy2.explore();
        Picture copy3= revealPicture(copy2);
        copy3.explore();
    }

}
