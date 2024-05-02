import java.awt.*;
import java.util.ArrayList;

public class RefreshableCanvas extends Canvas {

    private ArrayList<Boid> boids;
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // Clear the canvas (optional - see note below)
        g.setColor(getBackground());
        g.fillRect(0,0, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        for(Boid b : boids) {
            g.fillRect((int) b.x, (int) b.y, 5, 5);
        }
    }

    public void setBoids(ArrayList<Boid> boids) {
        this.boids = boids;
    }
}
