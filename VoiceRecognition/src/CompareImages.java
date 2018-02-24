import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class CompareImages {  //This will compare plotted amplitudes if the two audio signals by basic image processing techniques

    public static void main(String[] args) throws IOException
    { }

    public float comparePattern(int i,String decypt)
    {


        File f = null;
        File d = null;
        BufferedImage image = null;

        try {
            FileEncryptor df1 = new FileEncryptor();
            //EncryptFile df2=new EncryptFile();
            try {

                FileEncryptor.decryptFile("Original" + i + ".vrf", decypt);
                FileEncryptor.decryptFile("Data.vrf", decypt);
            } catch (IOException e) {


                return 0;
            } catch (GeneralSecurityException e) {

               return 0;
            }
            f = new File("Original" + i + ".vrf.decrypted.txt");
            d = new File("Data.vrf.decrypted.txt");
            BufferedImage img1 = ImageIO.read(f);
            BufferedImage img2 = ImageIO.read(d);

            Image thumbnail1 = img1.getScaledInstance(8, 8, Image.SCALE_AREA_AVERAGING);
            Image thumbnail2 = img2.getScaledInstance(8, 8, Image.SCALE_AREA_AVERAGING);

            BufferedImage f1 = toBufferedImage(thumbnail1);
            BufferedImage f2 = toBufferedImage(thumbnail2);

            compare(f1, f2);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
        float percent = compareImage(f, d);
        f.delete();
        d.delete();
        //System.out.println(percent);
        return percent;
    }


    public static float compareImage(File fileA, File fileB) {

        float percentage = 0;
        try {
            // take buffer data from both image files //
            BufferedImage biA = ImageIO.read(fileA);
            DataBuffer dbA = biA.getData().getDataBuffer();
            int sizeA = dbA.getSize();
            BufferedImage biB = ImageIO.read(fileB);
            DataBuffer dbB = biB.getData().getDataBuffer();
            int sizeB = dbB.getSize();
            int count = 0;
            // compare data-buffer objects //
            if (sizeA == sizeB) {

                for (int i = 0; i < sizeA; i++) {

                    if (dbA.getElem(i) == dbB.getElem(i)) {
                        count = count + 1;
                    }

                }
                percentage = (count * 100) / sizeA;
            } else {
                System.out.println("Both the images are not of same size");
            }

        } catch (Exception e) {
            System.out.println("Failed to compare image files ...");
        }
        return percentage;
    }

    public static double compare(BufferedImage img1, BufferedImage img2) {
        int width1 = img1.getWidth();
        int width2 = img2.getWidth();
        int height1 = img1.getHeight();
        int height2 = img2.getHeight();

        if ((width1 != width2) || (height1 != height2)) {
            throw new IllegalArgumentException("Error: Images dimensions mismatch");
        }

        int diff2 = 0;

        for (int i = 0; i < height1; i++) {
            for (int j = 0; j < width1; j++) {
                int rgb1 = img1.getRGB(j, i);
                int rgb2 = img2.getRGB(j, i);
                int r1 = (rgb1 >> 16) & 0xff;
                //int g1 = (rgb1 >> 8) & 0xff;
               //int b1 = (rgb1) & 0xff;
                int r2 = (rgb2 >> 16) & 0xff;
                //int g2 = (rgb2 >> 8) & 0xff;
               // int b2 = (rgb2) & 0xff;

                diff2 += Math.pow(r1 - r2, 2);//+ Math.pow(g1 - g2, 2) + Math.pow(b1 - b2, 2);
            }
        }
        return diff2 * 1.0 / (height1*width1);
    }

    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
}