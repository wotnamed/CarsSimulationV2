import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class CarGame extends JPanel implements ActionListener {

    // Car Physics Variables
    private double x = 400;             // Initial X position
    private double y = 300;             // Initial Y position
    private double speed = 0;           // Current speed
    private double angle = 0;           // Current heading (in radians)

    // Tuning Constants
    private final double ACCELERATION = 0.5;
    private final double FRICTION = 0.96;     // Multiplier applied every frame (1.0 = no friction)
    private final double MAX_SPEED = 16.0;
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

    //Buffered image
    private BufferedImage image;

    Physics physics = new Physics();
    Racecar racecar = new Racecar(new Color(255, 185, 0), new Color(255,255,200), 3.14, new double[]{400,200}, new int[]{50,20}, new Tire(1, "Sigma", 900000000), 0.2, 0.2, 1, 1);
    Checkpoint target = new Checkpoint(new double[]{200,200});

    private void preRenderTrack(int w, int h) {
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background
        g2d.setColor(new Color(30, 120, 30));
        g2d.fillRect(0, 0, w, h);
        g2d.setColor(Color.GRAY);
        g2d.fillOval(100, 100, 600, 400);
        g2d.setColor(new Color(30, 120, 30));
        g2d.fillOval(200, 200, 400, 200);

        g2d.dispose();
    }

    public CarGame() {
        // Set up the panel
        setBackground(Color.DARK_GRAY);
        setFocusable(true);
        setPreferredSize(new Dimension(800, 600));
        preRenderTrack(800, 600);


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

        Color groundColor = getBackgroundColorAtCar();
        if (groundColor.equals(new Color(30, 120, 30))) {
            racecar.groundDrag = 0.05; // default value
        } else {
            racecar.groundDrag = 0.0; // off track
        }

        racecar.updatePosition(physics, 2, target.getCoordinates());
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

        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }

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

    private Color getBackgroundColorAtCar() {
        // Get the car's current coordinates
        double[] coords = racecar.getCurrentCoordinates();
        int carX = (int) Math.round(coords[0]);
        int carY = (int) Math.round(coords[1]);

        // Check if coords is within boundaries to prevent crash then get color and covert to color object
        if (carX >= 0 && carX < image.getWidth() && carY >= 0 && carY < image.getHeight()) {
            int rgbInt = image.getRGB(carX, carY);
            return new Color(rgbInt);
        }
        // standard value if outside boundaries
        return Color.GRAY;
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

