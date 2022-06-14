import java.awt.Color;

import static java.awt.Color.BLUE;

public class Overlay {

    private final Picture m_overlayPic, m_backgroundPic;
    private final Pixel[][] m_overlayPixels, m_backgroundPixels;
    private final int m_posX, m_posY;

    public Overlay(Picture input, Picture background, int posX, int posY) {
        m_overlayPic = new Picture(input);
        m_overlayPixels = m_overlayPic.getPixels2D();
        m_backgroundPic = new Picture(background);
        m_backgroundPixels = m_backgroundPic.getPixels2D();
        m_posX = posX;
        m_posY = posY;
    }

    public Overlay(Picture input, Picture background) {
        this(input,
             background,
             (background.getWidth() / 2) - (input.getWidth() / 2),
             (background.getHeight() / 2) - (input.getHeight() / 2));

    }

    private boolean doesFit(Picture input, Picture background) {
        return (input.getHeight() <= background.getHeight()) && (input.getWidth() <= background.getWidth());
    }

    private boolean isWhite(Pixel pixel) {
        return (new Color(pixel.getRed(), pixel.getGreen(), pixel.getBlue()).equals(Color.WHITE));
    }

    public Picture putPicture() {
        if (!doesFit(m_overlayPic, m_backgroundPic)) return null;

        Pixel[][] top = new Pixel[m_overlayPixels.length][m_overlayPixels[0].length];

       // Subtract white pixels
       for (int row = 0; row < m_overlayPixels.length; row++) {
            for (int col = 0; col < m_overlayPixels[0].length; col++) {

                if(!isWhite(m_overlayPixels[row][col]))
                    top[row][col] = m_overlayPixels[row][col];
                else
                    top[row][col] = null;

            }
        }

        for (int row = m_posY, topRow = 0; row < m_backgroundPixels.length; row++, topRow++) {
            for (int col = m_posX, topCol = 0; col < m_backgroundPixels[0].length; col++, topCol++) {
                if (!(row > top[0].length- 1 || col > top.length - 1)) {
                    System.out.println("RETAIL ROW");
//                    if(top[topRow][topCol] != null) {
//                        m_backgroundPixels[row][col].setColor(top[row-m_posX][col-m_posY].getColor());

//                    }
                    System.out.println("row: " + row + " " + "col: " + col);
                    System.out.println("topRow: " + topRow + " " + "topCol: " + topCol);
                    m_backgroundPixels[row][col].setColor(BLUE);
                }
        }
    }

        return m_backgroundPic;
    }

    public int getPosX() {
        return m_posX;
    }

    public int getPosY() {
        return m_posY;
    }

    public static void main(String[] args) {
        Picture caterpillar = new Picture("caterpillar.jpg");
        Picture koala = new Picture("koala.jpg");
//        Overlay overlay = new Overlay(caterpillar, koala);
        Overlay overlay = new Overlay(caterpillar, koala, 50,50);
        overlay.putPicture().explore();

        System.out.println(overlay.getPosX() + " " + overlay.getPosY());
    }
}
