import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Painter extends JComponent {
    private Image image;
    private Graphics2D g2;
    private int currentX, currentY, oldX, oldY;

    public Painter() {
        setFocusable(true);
        requestFocus();
        setBackground(Color.WHITE);
        setMinimumSize(new Dimension(28,28));
        setPreferredSize(new Dimension(28,28));

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                oldX = event.getX();
                oldY = event.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent event) {
                currentX = event.getX();
                currentY = event.getY();

                if(g2 != null){
                    g2.drawLine(oldX, oldY, currentX, currentY);
                    repaint();
                    oldX = currentX;
                    oldY = currentY;
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(image == null){
            image = createImage(getWidth(), getHeight());
            g2 = (Graphics2D) image.getGraphics();
            g2.setStroke(new BasicStroke(20));
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            clear();
        }
        g.drawImage(image, 0,0,null);
    }

    public void clear(){
        g2.setPaint(Color.WHITE);
        g2.fillRect(0,0,getWidth(), getHeight());
        g2.setPaint(Color.BLACK);
        repaint();
    }

    public Image getImage(){
        return image;
    }
}
