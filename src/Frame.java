import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Frame extends JFrame {
    private RefreshableCanvas canvas;

    private int avoidFactor;

    int x = 600;
    int y = 400;

    int leftMargin = 50;
    int rightMargin = x - 50;

    int topMargin = 50;

    int bottomMargin = y - 50;
    private double matchingFactor = 0.05;
    private double minSpeed = 2;
    private double maxSpeed = 3;

    private double turnFactor = 0.2;
    private double centringFactor = 0.0005;

    private double visibleDistance = 100;
    private ArrayList<Boid> boids = new ArrayList<Boid>();

    public Frame(int numBoids) {
        super("canvas");
        for(int i = 0; i < numBoids; i++) {
            boids.add(new Boid(ThreadLocalRandom.current().nextInt(0,x), ThreadLocalRandom.current().nextInt(0,y)));
        }
        this.canvas = new RefreshableCanvas();
        canvas.setBoids(boids);
        canvas.setSize(new Dimension(x,y));
        add(canvas);
        setSize(x, y);
        setVisible(true);
        while(true) {
            update();
        }
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


                close_dx += boid.x - otherBoid.x;
                close_dy += boid.y - otherBoid.y;

                double distance = Math.sqrt(Math.pow(Math.abs(boid.x - otherBoid.x), 2) + Math.pow(Math.abs(boid.y - otherBoid.y), 2));
                if(distance > visibleDistance) {
                    continue;
                }

                xPos_average += otherBoid.x;
                yPos_average += otherBoid.y;

                avg_xVel += otherBoid.vx;
                avg_yVel += otherBoid.vy;
                neighbouring_boids += 1;

            }
            if(neighbouring_boids > 0) {
                boid.vx += (avg_xVel - boid.vx) * matchingFactor;
                boid.vy += (avg_yVel - boid.vy) * matchingFactor;

                xPos_average = xPos_average/neighbouring_boids;
                yPos_average  = yPos_average/neighbouring_boids;

                boid.vx += xPos_average*centringFactor;
                boid.vy += yPos_average*centringFactor;
            }
            boid.vx += close_dx * avoidFactor;
            boid.vy += close_dy * avoidFactor;

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
            double speed = Math.sqrt(Math.pow(boid.x, 2) + Math.pow(boid.vy, 2));
            if(speed < minSpeed) {
                boid.vx = (boid.vx/speed)*minSpeed;
                boid.vy = (boid.vy/speed)*minSpeed;
            }
            if(speed > maxSpeed) {
                boid.vx = (boid.vx/speed)*maxSpeed;
                boid.vy = (boid.vy/speed)*maxSpeed;
            }
            boid.x += boid.vx;
            boid.y += boid.vy;
        }
        canvas.setBoids(boids);
        canvas.repaint();
    }
}
