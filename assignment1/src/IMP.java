/*
 *Hunter Lloyd
 * Copyrite.......I wrote, ask permission if you want to use it outside of class.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.awt.image.PixelGrabber;
import java.awt.image.MemoryImageSource;
import java.util.prefs.Preferences;
import java.awt.Color;


class IMP implements MouseListener {
    JFrame frame;
    JPanel mp;
    JButton start;
    JScrollPane scroll;
    JMenuItem openItem, exitItem, resetItem;
    Toolkit toolkit;
    File pic;
    ImageIcon img;
    int colorX, colorY;
    int[] pixels;
    int[] results;
    //Instance Fields you will be using below

    //This will be your height and width of your 2d array
    int height = 0, width = 0;

    //your 2D array of pixels
    int picture[][];

    /*
     * In the Constructor I set up the GUI, the frame the menus. The open pulldown
     * menu is how you will open an image to manipulate.
     */
    IMP() {
        toolkit = Toolkit.getDefaultToolkit();
        frame = new JFrame("Image Processing Software by Hunter");
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu functions = getFunctions();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent ev) {
                quit();
            }
        });
        openItem = new JMenuItem("Open");
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                handleOpen();
            }
        });
        resetItem = new JMenuItem("Reset");
        resetItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                reset();
            }
        });
        exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                quit();
            }
        });
        file.add(openItem);
        file.add(resetItem);
        file.add(exitItem);
        bar.add(file);
        bar.add(functions);
        frame.setSize(600, 600);
        mp = new JPanel();
        mp.setBackground(new Color(0, 0, 0));
        scroll = new JScrollPane(mp);
        frame.getContentPane().add(scroll, BorderLayout.CENTER);
        JPanel butPanel = new JPanel();
        butPanel.setBackground(Color.black);
        start = new JButton("start");
        start.setEnabled(false);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                fun1();
            }
        });
        butPanel.add(start);
        frame.getContentPane().add(butPanel, BorderLayout.SOUTH);
        frame.setJMenuBar(bar);
        frame.setVisible(true);
    }

    /*
     * This method creates the pulldown menu and sets up listeners to selection of the menu choices. If the listeners are activated they call the methods
     * for handling the choice, fun1, fun2, fun3, fun4, etc. etc.
     */

    private JMenu getFunctions() {
        JMenu fun = new JMenu("Functions");

        JMenuItem firstItem = new JMenuItem("MyExample - fun1 method");
        JMenuItem secondItem = new JMenuItem("Rotate Image");
        JMenuItem thirdItem = new JMenuItem("Grayscale");
        JMenuItem fourthItem = new JMenuItem("Blur Grayscale");
        JMenuItem fifthItem = new JMenuItem("Histogram");
        JMenuItem sixthItem = new JMenuItem("Edge detection");
        JMenuItem seventhItem = new JMenuItem("Track Color orange");
        JMenuItem eighthItem = new JMenuItem("Quizone");
        JMenuItem ninthItem = new JMenuItem("Quiztwo");


        firstItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                fun1();
            }
        });

        secondItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                rotate_image();
            }
        });

        thirdItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                grayscale();
            }
        });

        fourthItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Blur();
            }
        });

        fifthItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                histogram();
            }
        });

        sixthItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                color_grayscale();
            }
        });

        seventhItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Track_color();
            }
        });

        eighthItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                firstQuiz();
            }
        });

        ninthItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                secondQuiz();
            }
        });

        fun.add(firstItem);
        fun.add(secondItem);
        fun.add(thirdItem);
        fun.add(fourthItem);
        fun.add(fifthItem);
        fun.add(sixthItem);
        fun.add(seventhItem);
        fun.add(eighthItem);
        fun.add(ninthItem);

        return fun;

    }

    /*
     * This method handles opening an image file, breaking down the picture to a one-dimensional array and then drawing the image on the frame.
     * You don't need to worry about this method.
     */
    private void handleOpen() {
        img = new ImageIcon();
        JFileChooser chooser = new JFileChooser();
        Preferences pref = Preferences.userNodeForPackage(IMP.class);
        String path = pref.get("DEFAULT_PATH", "");

        chooser.setCurrentDirectory(new File(path));
        int option = chooser.showOpenDialog(frame);

        if (option == JFileChooser.APPROVE_OPTION) {
            pic = chooser.getSelectedFile();
            pref.put("DEFAULT_PATH", pic.getAbsolutePath());
            img = new ImageIcon(pic.getPath());
        }
        width = img.getIconWidth();
        height = img.getIconHeight();

        JLabel label = new JLabel(img);
        label.addMouseListener(this);
        pixels = new int[width * height];

        results = new int[width * height];


        Image image = img.getImage();

        PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            System.err.println("Interrupted waiting for pixels");
            return;
        }
        for (int i = 0; i < width * height; i++)
            results[i] = pixels[i];
        turnTwoDimensional();
        mp.removeAll();
        mp.add(label);

        mp.revalidate();
    }

    /*
     * The libraries in Java give a one dimensional array of RGB values for an image, I thought a 2-Dimensional array would be more usefull to you
     * So this method changes the one dimensional array to a two-dimensional.
     */
    private void turnTwoDimensional() {
        picture = new int[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                picture[i][j] = pixels[i * width + j];


    }

    /*
     *  This method takes the picture back to the original picture
     */
    private void reset() {
        for (int i = 0; i < width * height; i++)
            pixels[i] = results[i];
        turnTwoDimensional();
        Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width));

        JLabel label2 = new JLabel(new ImageIcon(img2));
        mp.removeAll();
        mp.add(label2);

        mp.revalidate();
    }

    /*
     * This method is called to redraw the screen with the new image.
     */
    private void resetPicture() {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                pixels[i * width + j] = picture[i][j];
        Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width));

        JLabel label2 = new JLabel(new ImageIcon(img2));
        mp.removeAll();
        mp.add(label2);

        mp.revalidate();

    }

    /*
     * This method takes a single integer value and breaks it down doing bit manipulation to 4 individual int values for A, R, G, and B values
     */
    private int[] getPixelArray(int pixel) {
        int temp[] = new int[4];
        temp[0] = (pixel >> 24) & 0xff;
        temp[1] = (pixel >> 16) & 0xff;
        temp[2] = (pixel >> 8) & 0xff;
        temp[3] = (pixel) & 0xff;
        return temp;

    }

    /*
     * This method takes an array of size 4 and combines the first 8 bits of each to create one integer.
     */
    private int getPixels(int rgb[]) {
        int alpha = 0;
        int rgba = (rgb[0] << 24) | (rgb[1] << 16) | (rgb[2] << 8) | rgb[3];
        return rgba;
    }

    public void getValue() {
        int pix = picture[colorY][colorX];
        int temp[] = getPixelArray(pix);
        System.out.println("Color value " + temp[0] + " " + temp[1] + " " + temp[2] + " " + temp[3]);
    }

    /**************************************************************************************************
     * This is where you will put your methods. Every method below is called when the corresponding pulldown menu is
     * used. As long as you have a picture open first the when your fun1, fun2, fun....etc method is called you will
     * have a 2D array called picture that is holding each pixel from your picture.
     *************************************************************************************************/
    /*
     * Example function that just removes all red values from the picture.
     * Each pixel value in picture[i][j] holds an integer value. You need to send that pixel to getPixelArray the method which will return a 4 element array
     * that holds A,R,G,B values. Ignore [0], that's the Alpha channel which is transparency, we won't be using that, but you can on your own.
     * getPixelArray will breaks down your single int to 4 ints so you can manipulate the values for each level of R, G, B.
     * After you make changes and do your calculations to your pixel values the getPixels method will put the 4 values in your ARGB array back into a single
     * integer value so you can give it back to the program and display the new picture.
     */


    private void firstQuiz(){
        System.out.println("quiz one");


        for (int i = 0; i < height; i++)
            for (int k = 0; k< width; k++) {
                if (k % 4 == 0) {
                    int rgbArray[] = new int[4];

                    //get three ints for R, G and B
                    rgbArray = getPixelArray(picture[i][k]);

                    rgbArray[1] = 0;
                    rgbArray[2] = 0;
                    rgbArray[3] = 0;

                    //take three ints for R, G, B and put them back into a single int
                    picture[i][k] = getPixels(rgbArray);

                }
            }
        resetPicture();
    }

    private void secondQuiz(){
        System.out.println("second quiz");

        grayscale();


        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int rgbArray[] = new int[4];

                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);


                for (int a = 1; a < 4; a++) {
                    if (rgbArray[a] < 200) {
                        rgbArray[a] = 255;
                    } else {
                        rgbArray[a] = 0;
                    }
                }


                //take three ints for R, G, B and put them back into a single int
                picture[i][j] = getPixels(rgbArray);
            }
        grayscale();
        resetPicture();
    }




    private void fun1() {

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int rgbArray[] = new int[4];

                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);


                rgbArray[1] = 0;
                //take three ints for R, G, B and put them back into a single int
                picture[i][j] = getPixels(rgbArray);
            }
        resetPicture();
    }

    private void rotate_image() {


        int[][] rotatedPic = new int[width][height];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                rotatedPic[j][(height - 1) - i] = picture[i][j];
            }
        }
        picture = rotatedPic;

        int rotatedHeight = rotatedPic.length;
        int rotatedWidth = rotatedPic[0].length;
        height = rotatedHeight;
        width = rotatedWidth;

        resetPicture();
    }


    private void grayscale() {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int[] rgbArray = new int[4];

                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);


                rgbArray[1] = (int) ((.21 * rgbArray[1]) + (.72 * rgbArray[2]) + (.07 * rgbArray[3]));
                rgbArray[2] = (int) ((.21 * rgbArray[1]) + (.72 * rgbArray[2]) + (.07 * rgbArray[3]));
                rgbArray[3] = (int) ((.21 * rgbArray[1]) + (.72 * rgbArray[2]) + (.07 * rgbArray[3]));
                rgbArray[1] = (int) ((.21 * rgbArray[1]) + (.72 * rgbArray[2]) + (.07 * rgbArray[3]));
                rgbArray[2] = (int) ((.21 * rgbArray[1]) + (.72 * rgbArray[2]) + (.07 * rgbArray[3]));
                rgbArray[3] = (int) ((.21 * rgbArray[1]) + (.72 * rgbArray[2]) + (.07 * rgbArray[3]));


                //take three ints for R, G, B and put them back into a single int
                picture[i][j] = getPixels(rgbArray);
            }
        resetPicture();
    }

    private void Blur() {


        int[][] blurrpic = new int[height][width];

        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                int[] rgbArray = new int[4];
                int avg = Average(picture, i, j);
                rgbArray[0] = 255;
                rgbArray[1] = avg;
                rgbArray[2] = avg;
                rgbArray[3] = avg;

                blurrpic[i][j] = getPixels(rgbArray);
            }
        }
        picture = blurrpic;
        resetPicture();
    }

    public int Average(int[][] image, int row, int column) {

        int[] x = getPixelArray(image[row - 1][column - 1]);
        int[] x1 = getPixelArray(image[row - 1][column]);
        int[] x2 = getPixelArray(image[row - 1][column + 1]);
        int[] x3 = getPixelArray(image[row][column - 1]);
        int[] x4 = getPixelArray(image[row][column]);
        int[] x5 = getPixelArray(image[row][column + 1]);
        int[] x6 = getPixelArray(image[row + 1][column - 1]);
        int[] x7 = getPixelArray(image[row + 1][column]);
        int[] x8 = getPixelArray(image[row + 1][column + 1]);
        return (x[2] + x1[2] + x2[2] + x3[2] + x4[2] + x5[2] + x6[2] + x7[2] + x8[2]) / 9;
    }


    private void color_grayscale() {
        grayscale();


        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int rgbArray[] = new int[4];

                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);


                for (int a = 1; a < 4; a++) {
                    if (rgbArray[a] < 180) {
                        rgbArray[a] = 0;
                    } else {
                        rgbArray[a] = 255;
                    }
                }


                //take three ints for R, G, B and put them back into a single int
                picture[i][j] = getPixels(rgbArray);
            }
        grayscale();
        resetPicture();
    }


    private void histogram() {


        int[] x = new int[256];
        int[] y = new int[256];
        int[] z = new int[256];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int rgbArray[] = new int[4];

                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);


                x[rgbArray[1]] = x[rgbArray[1]] + 1;
                y[rgbArray[2]] = y[rgbArray[2]] + 1;
                z[rgbArray[3]] = z[rgbArray[3]] + 1;
            }

        JFrame redFrame = new JFrame("Red");
        redFrame.setSize(305, 600);
        redFrame.setLocation(800, 0);
        JFrame greenFrame = new JFrame("Green");
        greenFrame.setSize(305, 600);
        greenFrame.setLocation(1150, 0);
        JFrame blueFrame = new JFrame("blue");
        blueFrame.setSize(305, 600);
        blueFrame.setLocation(1450, 0);
        MyPanel redPanel = new MyPanel(x);
        MyPanel greenPanel = new MyPanel(y);
        MyPanel bluePanel = new MyPanel(z);
        redFrame.getContentPane().add(redPanel, BorderLayout.CENTER);
        redFrame.setVisible(true);
        greenFrame.getContentPane().add(greenPanel, BorderLayout.CENTER);
        greenFrame.setVisible(true);
        blueFrame.getContentPane().add(bluePanel, BorderLayout.CENTER);
        blueFrame.setVisible(true);
        start.setEnabled(true);


        //take three ints for R, G, B and put them back into a single int
        //picture[i][j] = getPixels(rgbArray);

}

    private void equalize() {

    }

    private void Track_color() {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int rgbArray[] = new int[4];

                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);


                if ((rgbArray[1] < 255 && rgbArray[1] > 190) && (rgbArray[2] < 130 && rgbArray[2] > 5) && rgbArray[3] == 0) {
                    rgbArray[1] = 255;
                    rgbArray[2] = 255;
                    rgbArray[3] = 255;
                } else {
                    rgbArray[1] = 0;
                    rgbArray[2] = 0;
                    rgbArray[3] = 0;
                }


                //take three ints for R, G, B and put them back into a single int
                picture[i][j] = getPixels(rgbArray);
            }
        resetPicture();
    }


    private void quit() {
        System.exit(0);
    }

    @Override
    public void mouseEntered(MouseEvent m) {
    }

    @Override
    public void mouseExited(MouseEvent m) {
    }

    @Override
    public void mouseClicked(MouseEvent m) {
        colorX = m.getX();
        colorY = m.getY();
        System.out.println(colorX + "  " + colorY);
        getValue();
        start.setEnabled(true);
    }

    @Override
    public void mousePressed(MouseEvent m) {
    }

    @Override
    public void mouseReleased(MouseEvent m) {
    }

    public static void main(String[] args) {
        IMP imp = new IMP();
    }
}
