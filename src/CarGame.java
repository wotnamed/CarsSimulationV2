import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class CarGame extends JPanel implements ActionListener {


    private boolean upPressed2 = false;
    private boolean downPressed2 = false;
    private boolean leftPressed2 = false;
    private boolean rightPressed2 = false;

    // Game Loop Timer
    private Timer timer;

    //Buffered image
    private BufferedImage image;

    Physics physics = new Physics();
    Racecar[] racecars = new Racecar[]{
        new Racecar(new Color(0, 255, 4), new Color(255,255,200), 3.14, new double[]{400,200}, new int[]{50,20}, new Tire(0.8, "Sigma", 9000), 0.05, 0.7, 50, 200, 909, 3.0)
        ,new Racecar(new Color(255, 0, 255), new Color(255,255,200), 3.14, new double[]{400,200}, new int[]{50,20}, new Tire(0.8, "Sigma", 9000), 0.05, 0.7, 100, 200, 909, 3)
        ,new Racecar(new Color(255, 185, 0), new Color(255,255,200), 3.14, new double[]{400,200}, new int[]{50,20}, new Tire(0.8, "Sigma", 9000), 0.05, 0.7, 150, 200, 909, 3)
    };
    SafetyCar safetyCar = new SafetyCar(new double[]{400, 300}, Color.YELLOW, Color.BLACK, 1.0);
    Checkpoint target = new Checkpoint(new double[]{200,200});
    // crew cars
    Team[] teamList = new Team[]{new Team(new Color(255,255,255), new Color(0,0,0),"beta", "git gud", 0, 909, new int[]{0,0})};
    Judge judge = Judge.getJudge();
    // TODO: change crew to other system?

    Pitstop[] pitstops;
    Tanker[] tankers;
    TireChanger[] tireChangers;
    Checkpoint[] pitBoxes;
    TeamLeader[] teamleaders;
    // create map
    Color[] groundColourMap = new Color[]{new Color(85,85,85), new Color(30,120,30), new Color(70, 70, 70)};
    double[] groundTractionMap = new double[]{0.99, 0.8, 0.99};
    double[] groundDragMap = new double[]{0, 0.05, 0.5};
    Checkpoint[] checkpointMap = new Checkpoint[]{new Checkpoint(new double[]{400,150}), new Checkpoint(new double[]{200,200}), new Checkpoint(new double[]{150,300}), new Checkpoint(new double[]{200,400}), new Checkpoint(new double[]{400,450}), new Checkpoint(new double[]{600,400}), new Checkpoint(new double[]{650,300}), new Checkpoint(new double[]{600,200})};

    int[] mainOval = new int[]{100, 100, 600, 400, 1, 200};
    int[] pitArea = new int[]{250, 200, 300, 50, 2};
    Map map = new Map(groundColourMap, groundDragMap, groundTractionMap, checkpointMap, mainOval, pitArea);

    private void preRenderTrack(int w, int h, Map map) {
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background
        g2d.setColor(map.getGroundColourMap()[1]);
        g2d.fillRect(0, 0, w, h);
        // Draw oval
        g2d.setColor(map.getGroundColourMap()[0]);
        g2d.fillOval(map.getMainOval()[0], map.getMainOval()[1], map.getMainOval()[2], map.getMainOval()[3]);
        g2d.setColor(map.getGroundColourMap()[1]);
        g2d.fillOval(map.getMainOval()[0]+map.getMainOval()[5]/2, map.getMainOval()[1]+map.getMainOval()[5]/2, map.getMainOval()[2]-map.getMainOval()[5], map.getMainOval()[3]-map.getMainOval()[5]);
        // Draw pit
        g2d.setColor(map.getGroundColourMap()[2]);
        g2d.fillRect(map.getPitArea()[0], map.getPitArea()[1], map.getPitArea()[2], map.getPitArea()[3]);

        g2d.dispose();
    }

    private void initializePitCrews(int numberOfCars) {
        pitstops = new Pitstop[numberOfCars];
        tankers = new Tanker[numberOfCars];
        tireChangers = new TireChanger[numberOfCars];
        pitBoxes = new Checkpoint[numberOfCars];
        teamleaders = new TeamLeader[numberOfCars];

        for(int i = 0; i < numberOfCars; i++) {
            double pitX = 250 + (i * 30);
            double pitY = 225;

            pitstops[i] = new Pitstop();
            tankers[i] = new Tanker(new double[]{pitX + 5, pitY + 10}, new int[]{255, 50, 50}, new int[]{200, 0, 0}, 0.5);
            tireChangers[i] = new TireChanger(new double[]{pitX - 5, pitY - 10}, new int[]{50, 50, 255}, new int[]{0, 0, 200}, 1.5);
            pitBoxes[i] = new Checkpoint(new double[]{pitX, pitY});

            teamleaders[i] = new TeamLeader(racecars[i], pitBoxes[i], tankers[i], tireChangers[i], pitstops[i]);
            racecars[i].addObserver(teamleaders[i]);
        }
    }

    public CarGame() {
        // Set up the panel
        setBackground(Color.DARK_GRAY);
        setFocusable(true);
        setPreferredSize(new Dimension(800, 600));
        preRenderTrack(800, 600, map);
        initializePitCrews(racecars.length);
        judge.setTeamList(teamList);
        judge.setLapCount(10);

        for (Racecar car : racecars) {
            car.addObserver(judge);
        }

        // Add keyboard listener
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeys(e.getKeyCode(), true);
                if (e.getKeyCode() == KeyEvent.VK_Y) {
                    if (safetyCar.isDeployed()) {
                        safetyCar.recall();
                    } else {
                        safetyCar.deploy();
                    }
                }
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

        for (int i = 0; i < racecars.length; i++) {
            Racecar car = racecars[i];

            if (car.isPitStopping) {
                pitstops[i].PitStop(2, racecars[i], tireChangers[i], tankers[i]);
            } else {
                Color groundColor = getBackgroundColorAtCar(car);
                car.updateGroundParameters(groundColor, map);

                double[] targetCoordinates;
                if (((car.getFuel() < 0.6 * car.getMaxFuel()) || car.getTire().getDurability() < 0.6) && (car.getCheckpointIndex() == 0)) {
                    targetCoordinates = pitBoxes[i].getCoordinates();
                    double distanceToPit = physics.calculateDistance(car, pitBoxes[i]);
                    if (distanceToPit <= 15.0) {
                        car.isPitStopping = true;
                        car.nextTire = new Tire(0.8, "Sigma", 9000);
                        car.getCurrentCoordinates()[0] = targetCoordinates[0];
                        car.getCurrentCoordinates()[1] = targetCoordinates[1];
                    }
                } else {
                    Targeting.updateTargetCheckpoint(car, checkpointMap, 50);
                    targetCoordinates = checkpointMap[car.getCheckpointIndex()].getCoordinates();
                }

                if (!car.isPitStopping) {
                    double[] oldCoords = new double[]{car.getCurrentCoordinates()[0], car.getCurrentCoordinates()[1]};
                    double dt = 2.0;
                    car.updatePosition(physics, dt, targetCoordinates);
                    if (safetyCar != null) {
                        safetyCar.enforceSpeedLimit(car, oldCoords, dt);
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updatePhysics();
        repaint(); // Request a redraw
    }

    protected void paintUI(Graphics g2d){
        // UI testing
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 11));
        g2d.drawString("Lorem Ipsum", 40, 40);
        int maxLaps = judge.getLapCount();
        for (int i = 0; i<racecars.length; i++){
            String message = "Team:" + racecars[i].getTeamIdentifier() + ", Laps: " + racecars[i].getLapCount() + "/" + maxLaps;
            g2d.drawString(message, 80, 40+i*20);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }

        // Cast to Graphics2D for rotation and better rendering
        Graphics2D g2d = (Graphics2D) g;

        // Enable antialiasing for smooth edges
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 0; i < racecars.length; i++) {
            paintVehicle(g2d, racecars[i]);
        }
        for (int i = 0; i < racecars.length; i++) {
            paintVehicle(g2d, tankers[i]);
            paintVehicle(g2d, tireChangers[i]);
            paintCheckpoint(g2d, pitBoxes[i]);
        }
        paintVehicle(g2d, judge);
        paintCheckpoint(g2d, target);
        for (int i = 0; i < map.checkpointMap.length; i++){
            paintCheckpoint(g2d, map.checkpointMap[i]);
        }
        paintUI(g2d);

        if (safetyCar != null && safetyCar.isDeployed()) {
            paintVehicle(g2d, safetyCar);
        }
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

    private Color getBackgroundColorAtCar(Racecar car) {
        // Get the car's current coordinates
        double[] coords = car.getCurrentCoordinates();
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

