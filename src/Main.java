import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Main
{
    static String fileName = "robots.txt"; // Specify the path and name of your text file
    static File robotsFile = new File(fileName);
    private static final ArrayList<Robot> robots = new ArrayList<>();
    private static final ArrayList<Thread> threads = new ArrayList<>();
    public static int roomSize = 0;
    private static boolean roomIsClean = true;
    private static boolean collision = false;

//    private static Room room = new Room();

    public static void main(String[] args) throws InterruptedException
    {
        roomSize = getRoomSize(roomSize);
        boolean[][] grid = new boolean[roomSize][roomSize];
        Room room = new Room(grid, roomSize);

        //add robots in the file and the center robot to the arraylist of robots
        robotFactory(robots);
        addCenterRobot();

        // Create and start threads for each robot
        for (int i = 0; i < robots.size(); i++) {

            int finalI = i;
            Thread robotThread = new Thread(() -> {
                // Code for the robot's movement logic
                try
                {
                    moveRobot(robots.get(finalI), roomSize, grid, room);
                    for (int robotA = 0; robotA < robots.size(); robotA++)
                    {
                        for (int robotB = robotA + 1; robotB < robots.size(); robotB++)
                        {
                            if (robots.get(robotA).getRobotCoordinates() == robots.get(robotB).getRobotCoordinates())
                            {
                                collision = true;
                                System.out.println("COLLISION AT CELL (" + robots.get(robotA).getRobotCoordinates()[0] + "," + robots.get(robotA).getRobotCoordinates()[1] + ")");
                                break;
                            }
                        }
                        if (collision){break;}
                    }
                }
                catch (InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
            });

            threads.add(robotThread);
            robotThread.start();
        }
    }

    // spiral movement of a robot with updates to the grid and the GUI
    public static void moveRobot(Robot robot, int roomSize, boolean[][] grid, Room room) throws InterruptedException
    {
            // move robots spirally
            int moveCount = 1; // Number of moves in each direction
            String initialDirection = robot.getInitialDirection();
            int direction;
            int robotX = robot.getRobotCoordinates()[0];
            int robotY = robot.getRobotCoordinates()[1];
//            System.out.println(Arrays.toString(robot.getRobotCoordinates()));

            // map directions:- 0: down, 1: left, 2: up, 3: right
            if (Objects.equals(initialDirection, "D")) {direction = 0;}
            else if (Objects.equals(initialDirection, "L")) {direction = 1;}
            else if (Objects.equals(initialDirection, "U")) {direction = 2;}
            else {direction = 3;}

            while (true) //(robotX >= 0 && robotX < roomSize-1 && robotY >= 0 && robotY < roomSize-1)
            {
                for (int i = 0; i < moveCount; i++)
                {
                    roomIsClean = true;

                    // iterate over robots list and change corresponding cell positions from dirty(False) to clean(True)
                    int[] robot1Position = robot.getRobotCoordinates();
                    grid[roomSize - robot1Position[1] - 1][robot1Position[0]] = true;

                    // check if room is clean
                    for (boolean[] booleans : grid)
                    {
                        for (boolean j : booleans) {if (!j){roomIsClean = false; break;}}
                        if (!roomIsClean){break;}
                    }

                    // check if room is clean yet
                    if (roomIsClean) {break;}

                    // Move the robot in the current direction
                    if (direction == 0) {robotY--;} // Down
                    else if (direction == 1) {robotX--;} // Left
                    else if (direction == 2) {robotY++;} // Up
                    else if (direction == 3) {robotX++;} // Right

                    // right wall trying to go right > go up instead
                    if (robotX >= roomSize && direction == 3 && robotY < roomSize - 1)
                    {
//                        System.out.println("ex 1");
                        robotX--;
                        robotY++;
                        direction = 2;
                    }

                    // upper wall trying to go up > go left
                    else if (robotY >= roomSize && direction == 2 && robotX > 0)
                    {
//                        System.out.println("ex 2");
//                        System.out.println("robotX: "+robotX);
//                        System.out.println("robotY: "+robotY);
                        robotY--;
                        robotX--;
                        direction = 1;
                    }

                    // left wall trying to go left > go down
                    else if (robotX < 0 && direction == 1 && robotY > 0)
                    {
//                        System.out.println("ex 3");
                        robotX++;
                        robotY--;
                        direction = 0;
                    }

                    // bottom wall trying to go down > go right
                    else if (robotY < 0 && direction == 0 && robotX < roomSize - 1)
                    {
//                        System.out.println("ex 4");
                        robotY++;
                        robotX++;
                        direction = 3;
                    }

                    // update robot position
                    int[] newCoordinates = {robotX, robotY};
                    robot.setRobotCoordinates(newCoordinates);

                    // Print the robot's current position
//                    System.out.println("RobotCoordinates is: " + Arrays.toString(robot.getRobotCoordinates()));

                    room.updateGrid(grid, robot.getRobotCoordinates(), roomSize);

                    printGrid(grid, roomSize);
                    System.out.println(robots);

//                    System.out.println(robot.getInitialDirection());

                    // thread delay
                    Thread.sleep(200);
                }

                //check if room is clean to break from while loop
                if (roomIsClean){System.out.println("ROOM IS CLEAN"); break;}

                // Increment moveCount every second move in the same direction
                if (direction % 2 == 1) {moveCount++;}

                // Update the direction (cycle counter-clockwise: 0, 3, 2, 1)
                direction = (direction + 3) % 4;

                if (direction == 0) {robot.setInitialDirection("D");}
                else if (direction == 1) {robot.setInitialDirection("L");}
                else if (direction == 2) {robot.setInitialDirection("U");}
                else {robot.setInitialDirection("R");}
            }
    }

    //create a checkForWall method

    // prints the grid to the console
    public static void printGrid(boolean[][] grid, int roomSize)
        {
            for (int i = 0; i < grid.length; i++)
            {
//                System.out.println(Arrays.toString(grid[i]));
            }
        }

    // Creates robot objects corresponding to provided robots.txt and adds them to robots list
    public static void robotFactory(ArrayList<Robot> robots)
    {
        int robotCount;
        int[] initialCoordinates = new int[2];
        String initialDirection;

        try
        {
            Scanner scanner = new Scanner(robotsFile);
            robotCount = Integer.parseInt(scanner.nextLine());

            while (scanner.hasNextLine())
            {
                // read robot line
                String line = scanner.nextLine();
                Scanner lineScanner = new Scanner(line);

                // get initial coordinates and initial direction of each robot
                initialCoordinates[0] = lineScanner.nextInt();
                initialCoordinates[1] = lineScanner.nextInt();
                initialDirection = lineScanner.next();

                // create new robot with initial coordinates and initial direction
                Robot robot = new Robot();
                robot.setRobotCoordinates(initialCoordinates);
                robot.setInitialDirection(initialDirection);

                // add robot object to robots list
                robots.add(robot);

                lineScanner.close();
            }
            scanner.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("INPUT ERROR");
        }
    }

    // create and add robot at center
    public static void addCenterRobot()
    {
        int center = (int) Math.floor(roomSize / 2);
        Robot centerRobot = new Robot();
        int[] Center = new int[2];
        Center[0] = center;
        Center[1] = center;
        centerRobot.setRobotCoordinates(Center);
        centerRobot.setInitialDirection("U");
        robots.add(centerRobot);
    }

    // gets room size from room.txt file
    public static int getRoomSize(int roomSize){
        //Reads room dimensions from room.txt file and throws an error if file is not found or if size is 0
        String fileName = "room.txt";
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                roomSize = Integer.parseInt(line);
            }
            scanner.close();
            if (roomSize == 0) {throw new FileNotFoundException();}
        }
        catch (FileNotFoundException e) {
            System.out.println("INPUT ERROR");
        }
        return roomSize;
    }
}