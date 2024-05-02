import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RefreshableCanvas extends JPanel {

    private ArrayList<Boid> boids;
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        for(Boid b : boids) {
            g.fillRect((int) b.x, (int) b.y, 2, 2);
        }
    }

    public void setBoids(ArrayList<Boid> boids) {
        this.boids = boids;
    }
}
