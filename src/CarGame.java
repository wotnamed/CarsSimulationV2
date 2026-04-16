import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CarGame extends JPanel implements ActionListener {

    // Car Physics Variables
    private double x = 400;             // Initial X position
    private double y = 300;             // Initial Y position
    private double speed = 0;           // Current speed
    private double angle = 0;           // Current heading (in radians)

    // Tuning Constants
    private final double ACCELERATION = 0.2;
    private final double FRICTION = 0.96;     // Multiplier applied every frame (1.0 = no friction)
    private final double MAX_SPEED = 8.0;
    private final double MAX_REVERSE = -4.0;
    private final double TURN_SPEED = 0.07;   // Radians per frame

    // Car Dimensions
    private final int CAR_LENGTH = 40;
    private final int CAR_WIDTH = 20;

    // Input States
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private boolean upPressed2 = false;
    private boolean downPressed2 = false;
    private boolean leftPressed2 = false;
    private boolean rightPressed2 = false;

    // Game Loop Timer
    private Timer timer;

    Physics physics = new Physics();
    Racecar racecar = new Racecar(new int[]{255, 185, 0}, new int[]{255,255,200}, 3.14, new double[]{400,200}, new int[]{50,20});
    Checkpoint target = new Checkpoint(new double[]{200,200});

    public CarGame() {
        // Set up the panel
        setBackground(Color.DARK_GRAY);
        setFocusable(true);
        setPreferredSize(new Dimension(800, 600));


        // Add keyboard listener
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeys(e.getKeyCode(), true);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                handleKeys(e.getKeyCode(), false);
            }
        });

        // 60 FPS Game Loop (approx 16ms per frame)
        timer = new Timer(16, this);
        timer.start();
    }

    private void handleKeys(int keyCode, boolean pressed) {
        if (keyCode == KeyEvent.VK_UP) upPressed = pressed;
        if (keyCode == KeyEvent.VK_DOWN) downPressed = pressed;
        if (keyCode == KeyEvent.VK_LEFT) leftPressed = pressed;
        if (keyCode == KeyEvent.VK_RIGHT) rightPressed = pressed;
        if (keyCode == KeyEvent.VK_W) upPressed2 = pressed;
        if (keyCode == KeyEvent.VK_S) downPressed2 = pressed;
        if (keyCode == KeyEvent.VK_A) leftPressed2 = pressed;
        if (keyCode == KeyEvent.VK_D) rightPressed2 = pressed;
    }

    private void updatePhysics() {
        // move checkpoint
        if (upPressed2){
            target.setCoordinates(new double[]{target.getCoordinates()[0], target.getCoordinates()[1]-10});
        } if (downPressed2){
            target.setCoordinates(new double[]{target.getCoordinates()[0], target.getCoordinates()[1]+10});
        } if (leftPressed2){
            target.setCoordinates(new double[]{target.getCoordinates()[0]-10, target.getCoordinates()[1]});
        } if (rightPressed2) {
            target.setCoordinates(new double[]{target.getCoordinates()[0]+10, target.getCoordinates()[1]});
        }
        racecar.updatePosition(physics, 1, target.getCoordinates());
        // 1. Apply Acceleration / Braking
        if (upPressed) {
            speed += ACCELERATION;
        } else if (downPressed) {
            speed -= ACCELERATION;
        } else {
            // Apply friction if no gas/brake is pressed
            speed *= FRICTION;
        }

        // Limit speeds
        if (speed > MAX_SPEED) speed = MAX_SPEED;
        if (speed < MAX_REVERSE) speed = MAX_REVERSE;

        // Stop completely if the speed is negligible (prevents infinite micro-sliding)
        if (Math.abs(speed) < 0.1 && !upPressed && !downPressed) {
            speed = 0;
        }

        // 2. Apply Steering (only if moving)
        if (Math.abs(speed) > 0) {
            // The direction of the turn flips if we are reversing
            double direction = (speed > 0) ? 1 : -1;

            if (leftPressed) angle -= TURN_SPEED * direction;
            if (rightPressed) angle += TURN_SPEED * direction;
        }

        // 3. Update Position based on velocity vector
        x += speed * Math.cos(angle);
        y += speed * Math.sin(angle);

        // Optional: Screen wrapping (so the car doesn't get lost off-screen)
        if (x > getWidth()) x = 0;
        if (x < 0) x = getWidth();
        if (y > getHeight()) y = 0;
        if (y < 0) y = getHeight();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updatePhysics();
        repaint(); // Request a redraw
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Cast to Graphics2D for rotation and better rendering
        Graphics2D g2d = (Graphics2D) g;

        // Enable anti-aliasing for smooth edges
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Save the current transform state
        var oldTransform = g2d.getTransform();

        // Move the canvas to the car's coordinates, then rotate it
        g2d.translate(x, y);
        g2d.rotate(angle);

        // Draw the car (centered on the translation point)
        g2d.setColor(new Color(50, 150, 250)); // Light blue car

        // Note: x represents length (forward direction), y represents width
        g2d.fillRoundRect(-CAR_LENGTH / 2, -CAR_WIDTH / 2, CAR_LENGTH, CAR_WIDTH, 5, 5);

        // Draw a "windshield" to indicate the front of the car
        g2d.setColor(Color.WHITE);
        g2d.fillRect((CAR_LENGTH / 2) - 10, (-CAR_WIDTH / 2) + 2, 6, CAR_WIDTH - 4);

        // Restore the original transform state so other drawings aren't affected
        g2d.setTransform(oldTransform);
        Racecar testVehicle = new Racecar(new int[]{185, 185, 0}, new int[]{255,255,255}, 0, new double[]{100,100}, new int[]{50,50});
        paintVehicle(g2d, testVehicle);
        paintVehicle(g2d, racecar);
        paintCheckpoint(g2d, target);
    }
    protected void paintVehicle(Graphics2D g2d, Vehicle vehicle){
        // works as of 20260416
        var oldTransform = g2d.getTransform();
        g2d.translate(vehicle.getCurrentCoordinates()[0], vehicle.getCurrentCoordinates()[1]);
        g2d.rotate(vehicle.getFacingAngleRad());

        // Draw the car (centered on the translation point)
        g2d.setColor(vehicle.getPrimaryColour()); // Light blue car

        // Note: x represents length (forward direction), y represents width
        g2d.fillRoundRect(-vehicle.getDimensions()[0] / 2, -vehicle.getDimensions()[1] / 2, vehicle.getDimensions()[0], vehicle.getDimensions()[1], 5, 5);

        // Draw a "windshield" to indicate the front of the car
        g2d.setColor(vehicle.getSecondaryColour());
        g2d.fillRect((vehicle.getDimensions()[0] / 2) - 10, (-vehicle.getDimensions()[1] / 2) + 2, 6, vehicle.getDimensions()[1] - 4);

        // Restore the original transform state so other drawings aren't affected
        g2d.setTransform(oldTransform);
    }
    protected void paintCheckpoint(Graphics2D g2d, Checkpoint checkpoint){
        // works as of 20260416
        var oldTransform = g2d.getTransform();
        g2d.translate(checkpoint.getCoordinates()[0], checkpoint.getCoordinates()[1]);

        // Draw the car (centered on the translation point)
        g2d.setColor(new Color(255,0,0)); // Light blue car

        // Note: x represents length (forward direction), y represents width
        g2d.fillRoundRect(-10 / 2, -10 / 2, 10, 10, 5, 5);

        // Restore the original transform state so other drawings aren't affected
        g2d.setTransform(oldTransform);
    }
    public static void main(String[] args) {
        // Ensure Swing UI updates are on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Top-Down Car Physics");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(new CarGame());
            frame.pack();
            frame.setLocationRelativeTo(null); // Center on screen
            frame.setVisible(true);
        });
    }
}

