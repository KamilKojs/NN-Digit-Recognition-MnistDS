import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class GUI {
    NeuralNetwork neuralNetwork;
    final int WINDOW_WIDTH = 400;
    final int WINDOW_HEIGHT = 310;

    //GUI elements
    JButton clear;
    JButton guess;

    JLabel guessInfo;
    Painter painter;

    public GUI(NeuralNetwork neuralNetwork){
        this.neuralNetwork = neuralNetwork;
        createGui();
    }

    public void createGui(){

        clear = new JButton("Clear");
        clear.setMinimumSize(new Dimension(100,30));
        guess = new JButton("Guess");
        guess.setMinimumSize(new Dimension(100,30));

        guessInfo = new JLabel();
        guessInfo.setText("My guess is: ");
        painter = new Painter();

        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                painter.clear();
                System.out.println();
            }
        });

        guess.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Scaling the image to 28x28 and getting int greyscale values 0-255 row wise
                double[] imageData =  prepareImageData();
                double[] result = neuralNetwork.feedforward(imageData);
                //for(int i=0; i<result.length ;i++) System.out.println(i + ": " + result[i]);
                int index = 0;
                for(int i=1; i<result.length ;i++){
                    if(result[i] > result[index]) index = i;
                }
                guessInfo.setText("My guess is: " + index);
            }
        });

        JFrame jFrame = new JFrame();
        jFrame.setTitle("Digit Recognition");
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
        jFrame.setLocation(50,50);
        jFrame.setResizable(false);

        //creating jPanel
        JPanel jPanel = new JPanel();
        jPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        jPanel.setLayout(new BorderLayout());

        //creating jPanel in which the buttons are stored, its the one to the left
        JPanel jPanelLeft = new JPanel();
        jPanelLeft.setMinimumSize(new Dimension(100, WINDOW_HEIGHT));
        jPanelLeft.setLayout(new BoxLayout(jPanelLeft, BoxLayout.PAGE_AXIS));

        jPanelLeft.add(clear);
        jPanelLeft.add(guess);

        //creating jPanel in which the messages are stored, its the one at the bottom
        JPanel jPanelBottom = new JPanel();
        jPanelBottom.setMinimumSize(new Dimension(WINDOW_WIDTH, 80));
        jPanelBottom.setLayout(new BoxLayout(jPanelBottom, BoxLayout.PAGE_AXIS));

        jPanelBottom.add(guessInfo);

        //adding elements to main jPanel
        jPanel.add(jPanelLeft, BorderLayout.WEST);
        jPanel.add(jPanelBottom, BorderLayout.SOUTH);
        jPanel.add(painter, BorderLayout.CENTER);

        jPanel.setBackground(Color.LIGHT_GRAY);
        jPanelLeft.setBackground(Color.LIGHT_GRAY);
        jPanelBottom.setBackground(Color.LIGHT_GRAY);

        jFrame.setContentPane(jPanel);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public double[] prepareImageData(){
        Image image = painter.getImage();

        //Original Image
        //Convert Image to grayScale bufferedImage
        BufferedImage bufferedImageOriginal = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = bufferedImageOriginal.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        //Resizing Image to 28,28
        BufferedImage bufferedImageResized = new BufferedImage(28,28,bufferedImageOriginal.getType());
        Graphics2D g2 = bufferedImageResized.createGraphics();
        g2.drawImage(bufferedImageOriginal, 0,0,28,28, null);
        g2.dispose();

        //Getting grayscale values 0-255 and putting them in 2xdim array
        int[][] imageData2xdim = new int[bufferedImageResized.getHeight()][bufferedImageResized.getWidth()];
        for(int i=0; i<bufferedImageResized.getHeight() ;i++){
            for(int j=0; j<bufferedImageResized.getWidth() ;j++){
                int greyscale = bufferedImageResized.getRGB(j, i)& 0xFF;
                if(greyscale == 255) imageData2xdim[i][j] = 0;
                else imageData2xdim[i][j] = 255;
            }
        }

        //getting 1xdim array from the 2xdim array rowWise
        int[] imageData = new int[imageData2xdim.length * imageData2xdim[0].length];
        int index = 0;
        for(int i=0; i<imageData2xdim.length ;i++){
            for(int j=0; j<imageData2xdim[i].length ;j++){
                imageData[index] = imageData2xdim[i][j];
                index = index + 1;
            }
        }

        double[] imageDataDouble = new double[imageData.length];
        for(int i=0; i<imageData.length ;i++){
            imageDataDouble[i] = imageData[i] / 255;
        }

        return imageDataDouble;
    }
}
