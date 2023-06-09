import java.awt.*;
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
    private static ArrayList<Robot> robots = new ArrayList<Robot>();
    public static int roomSize = 0;

//    private static Room room = new Room();

    public static void main(String[] args) throws InterruptedException
    {
        roomSize = getRoomSize(roomSize);
        boolean[][] grid = new boolean[roomSize][roomSize];
        Room room = new Room(grid, roomSize);
        Robot robot = new Robot();
        int[] robotPosition = robot.getRobotCoordinates();
        boolean running = true;

        //add robots in the file to the arraylist of robots
        robotFactory(robots);

        //add a robot in the center of the room
        int center = (int) Math.floor(roomSize / 2);
        Robot centerRobot = new Robot();
        int[] Center = new int[2];
        Center[0] = center;
        Center[1] = center;
        centerRobot.setRobotCoordinates(Center);
        centerRobot.setInitialDirection("U");
        robots.add(centerRobot);

        //assign a thread to each robot
        for (int i = 0; i < robots.size(); i++)
        {
            /////////
        }

        while (running)
        {
            spiralMovement(centerRobot, roomSize, grid, room);
        }

    }

    // spiral movement of a robot with updates to the grid and the GUI
    public static void spiralMovement(Robot robot, int roomSize, boolean[][] grid, Room room) throws InterruptedException
    {
            // move robots spirally
            int moveCount = 1; // Number of moves in each direction
            String initialDirection = robot.getInitialDirection();
            int direction;
            int robotX = robot.getRobotCoordinates()[0];
            int robotY = robot.getRobotCoordinates()[1];
            System.out.println(Arrays.toString(robot.getRobotCoordinates()));

            // map directions:- 0: down, 1: left, 2: up, 3: right
            if (Objects.equals(initialDirection, "D")) {direction = 0;}
            else if (Objects.equals(initialDirection, "L")) {direction = 1;}
            else if (Objects.equals(initialDirection, "U")) {direction = 2;}
            else {direction = 3;}

            while (robotX >= 0 && robotX < roomSize && robotY >= 0 && robotY < roomSize)
            {
                for (int i = 0; i < moveCount; i++)
                {
                    // iterate over robots list and change corresponding cell positions from dirty(False) to clean(True)
                    for (Robot robot1 : robots)
                    {
                        int[] robot1Position = robot1.getRobotCoordinates();
                        grid[robot1Position[1]][robot1Position[0]] = true;
//                        room.updateGrid(grid, robot1Position);
                    }

                    // Move the robot in the current direction
                    if (direction == 0) {robotY--;} // Down
                    else if (direction == 1) {robotX--;} // Left
                    else if (direction == 2) {robotY++;} // Up
                    else if (direction == 3) {robotX++;} // Right

                    // update robot position
                    int[] newCoordinates = {robotY, robotX};
                    robot.setRobotCoordinates(newCoordinates);

                    // Print the robot's current position
                    System.out.println("Robot position is: " + Arrays.toString(robot.getRobotCoordinates()));

                    room.updateGrid(grid, robot.getRobotCoordinates());

                    printGrid(grid, roomSize);
                    System.out.println(robots);

                System.out.println(robot.getInitialDirection());
                Thread.sleep(2000);
                }

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
                System.out.println(Arrays.toString(grid[i]));
//                System.out.println("\n");
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