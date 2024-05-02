import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Frame extends JFrame {
    private final RefreshableCanvas canvas;


    int x = 1200;
    int y = 600;

    int leftMargin = 50;
    int rightMargin = x - 50;

    int topMargin = 50;

    int bottomMargin = y - 50;

    private final ArrayList<Boid> boids = new ArrayList<>();

    public Frame(int numBoids) {
        super("canvas");
        for(int i = 0; i < numBoids; i++) {
            boids.add(new Boid(ThreadLocalRandom.current().nextInt(0,x), ThreadLocalRandom.current().nextInt(0,y)));
        }
        this.canvas = new RefreshableCanvas();
        canvas.setBoids(boids);
        canvas.setSize(new Dimension(x,y));
        canvas.setDoubleBuffered(true);
        add(canvas);
        setSize(x, y);
        setVisible(true);
        javax.swing.Timer timer = new javax.swing.Timer(1, e -> update());
        timer.start();
    }

    public void update() {
        for(Boid boid : boids) {
            double close_dx = 0;
            double close_dy = 0;

            double avg_xVel = 0;
            double avg_yVel = 0;
            double neighbouring_boids = 0;

            double xPos_average = 0;
            double yPos_average = 0;

            for(Boid otherBoid : boids) {
                if(boid == otherBoid) {
                    continue;
                }
                double dx = boid.x - otherBoid.x;
                double dy = boid.y - otherBoid.y;
                double visibleDistance = 60;
                if(Math.abs(dx) > visibleDistance || Math.abs(dy) > visibleDistance) {
                    continue;
                }
                double distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));


                double protectedRange = 10;
                if(distance < protectedRange) {
                    close_dx += boid.x - otherBoid.x;
                    close_dy += boid.y - otherBoid.y;
                }
                else if(distance < visibleDistance) {

                    xPos_average += otherBoid.x;
                    yPos_average += otherBoid.y;
                    avg_xVel += otherBoid.vx;
                    avg_yVel += otherBoid.vy;
                    neighbouring_boids += 1;
                }

            }
            if(neighbouring_boids > 0) {
                xPos_average = xPos_average/neighbouring_boids;
                yPos_average  = yPos_average/neighbouring_boids;
                avg_xVel = avg_xVel/neighbouring_boids;
                avg_yVel = avg_xVel/neighbouring_boids;

                double matchingFactor = 0.05;
                double centringFactor = 0.009;
                boid.vx += (xPos_average - boid.x) * centringFactor + (avg_xVel - boid.vx) * matchingFactor;
                boid.vy += (yPos_average - boid.y) * centringFactor + (avg_yVel - boid.vy) * matchingFactor;
            }
            double avoidFactor = 0.1;
            boid.vx += close_dx * avoidFactor;
            boid.vy += close_dy * avoidFactor;

            double turnFactor = 0.5;
            if(boid.x < leftMargin) {
                boid.vx += turnFactor;
            }
            if(boid.x > rightMargin) {
                boid.vx -= turnFactor;
            }
            if(boid.y < topMargin) {
                boid.vy += turnFactor;
            }
            if(boid.y > bottomMargin) {
                boid.vy -= turnFactor;
            }
            double speed = Math.sqrt(Math.pow(boid.vx, 2) + Math.pow(boid.vy, 2));
            double minSpeed = 3;
            if(speed < minSpeed) {
                boid.vx = (boid.vx/speed)* minSpeed;
                boid.vy = (boid.vy/speed)* minSpeed;
            }
            double maxSpeed = 7;
            if(speed > maxSpeed) {
                boid.vx = (boid.vx/speed)* maxSpeed;
                boid.vy = (boid.vy/speed)* maxSpeed;
            }
            boid.x += boid.vx;
            boid.y += boid.vy;
        }
        canvas.setBoids(boids);
        canvas.removeAll();
        canvas.repaint();
    }
}
