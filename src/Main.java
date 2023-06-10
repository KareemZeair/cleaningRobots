import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main
{
    static String fileName = "robots.txt"; // Specify the path and name of your text file
    static File robotsFile = new File(fileName);
    private static ArrayList<Robot> robots = new ArrayList<>();
    private static final ArrayList<Thread> threads = new ArrayList<>();
    public static int roomSize;
    private static boolean roomIsClean = true;
    private static boolean collision = false;

    public static void main(String[] args) throws InterruptedException, FileNotFoundException
    {
        roomSize = getRoomSize();
        boolean[][] grid = new boolean[roomSize][roomSize];
        Room room = new Room(grid, roomSize);

        //add robots in the file and the center robot to the arraylist of robots
        robotFactory();
        addCenterRobot();


        // Create and start threads for each robot
        for (int i = 0; i < robots.size(); i++) {

            int finalI = i;
            Thread robotThread = new Thread(() -> {
                // Code for the robot's movement logic
                try
                {
                    Robot robot = robots.get(finalI);
                    if (robot.getMovementType() == 'S' && !collision)
                    {
                        robot.moveSpirally(robot, roomSize, grid, room, roomIsClean, robots, false, finalI);
                        robot.setMovementType(robot, roomSize);
                    }
                    if (robot.getMovementType() == 'C' && !collision)
                    {
                        robot.moveCircularly(robot, roomSize, grid, room, false, robots, false, finalI);
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

    // Creates robot objects corresponding to provided robots.txt and adds them to robots list
    public static void robotFactory() throws FileNotFoundException
    {
        int[] initialCoordinates = new int[2];
        String initialDirection;
        String filePath = "robots.txt";
//        ArrayList<Robot> robots = new ArrayList<Robot>();
         // Create a Scanner object to read from the file
            Scanner scanner = new Scanner(new File(filePath));
            String num = scanner.nextLine();

            // Read and store the data from each line
            while (scanner.hasNextLine())
            {
                Robot robot = new Robot();
                // Read the data as "int int string" format
                int num1 = scanner.nextInt();
                int num2 = scanner.nextInt();
                String str = scanner.next();

                initialCoordinates[0] = num1;
                initialCoordinates[1] = roomSize - num2 - 1;
                initialDirection = str;

                // update new robot with initial coordinates and initial direction and add them to robots list
                robot.setX(initialCoordinates[0]);//= initialCoordinates;
                robot.setY(initialCoordinates[1]);//= initialCoordinates;
                robot.setDirection(initialDirection);
                Main.robots.add(robot);
            }
            // Close the scanner
            scanner.close();
    }

    // create and add robot at center
    public static void addCenterRobot()
    {
        int center = (int) Math.floor(roomSize / 2);
        Robot centerRobot = new Robot();
        int[] Center = new int[2];
        Center[0] = center;
        Center[1] = center;
        centerRobot.setX(Center[0]);// = Center;
        centerRobot.setY(Center[1]);// = Center;
        centerRobot.setDirection("U");
        robots.add(centerRobot);
    }

    // gets room size from room.txt file
    public static int getRoomSize(){
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

//    public static void setRobots(ArrayList<Robot> robots)
//    {
//        Main.robots = robots;
//    }
}