import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main
{
    private static ArrayList<Robot> robots = new ArrayList<>();
    private static final ArrayList<Thread> threads = new ArrayList<>();
    public static int roomSize;
    private static boolean roomIsClean = false;
    private static boolean collision = false;

    public static void main(String[] args) throws FileNotFoundException
    {
        roomSize = getRoomSize();
        boolean[][] grid = new boolean[roomSize][roomSize];
        Room room = new Room(grid, roomSize);

        //add robots in the file and the center robot to the arraylist of robots
        try
        {
            if (robotFactory() != null)
            {
                throw Objects.requireNonNull(robotFactory());
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("INPUT ERROR");
            throw new FileNotFoundException();
        }
        addCenterRobot();


        // Create and start threads for each robot
        while (!roomIsClean)
        {
            for (int i = 0; i < robots.size(); i++)
            {

                int finalI = i;
//            Thread robotThread = new Thread(() -> {
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
            }

//            threads.add(robotThread);
//            robotThread.start();
        }
    }
//    }

    // Creates robot objects from robots.txt and adds them to robots list
    public static FileNotFoundException robotFactory()
    {
        try
        {
            int[] initialCoordinates = new int[2];
            String initialDirection;
            String filePath = "robots.txt";

            // Create a Scanner object to read from the file
            Scanner scanner = new Scanner(new File(filePath));
            if (!scanner.hasNext())
            {throw new FileNotFoundException();}

            int num = scanner.nextInt();
            int robotCount = 0;

            // Read and store the data from each line
            while (scanner.hasNextLine())
            {
                robotCount++;
                Robot robot = new Robot();
                // Read the data as "int int string" format
                int num1 = scanner.nextInt();
                int num2 = scanner.nextInt();
                String str = scanner.next();

                initialCoordinates[0] = num1;
                initialCoordinates[1] = roomSize - num2 - 1;
                initialDirection = str;

                // update new robot with initial coordinates and initial direction and add them to robots list
                robot.setX(initialCoordinates[0]);
                robot.setY(initialCoordinates[1]);
                robot.setDirection(initialDirection);
                Main.robots.add(robot);
            }
            // Close the scanner
            scanner.close();
            if (robotCount != num-1){throw new FileNotFoundException();}
        }
        catch(FileNotFoundException e) {return e;}
        return null;
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
            if (roomSize <= 0 || roomSize % 10 == 5) {throw new FileNotFoundException();}
        }
        catch (FileNotFoundException e)
        {
            System.out.println("INPUT ERROR");
        }//return 0;}
        return roomSize;
    }

    // create and add robot at center
    public static void addCenterRobot()
    {
        int center = (int) Math.floor(roomSize / 2);
        Robot centerRobot = new Robot();
        int[] Center = new int[2];
        Center[0] = center;
        Center[1] = center;
        centerRobot.setX(Center[0]);
        centerRobot.setY(Center[1]);
        centerRobot.setDirection("U");
        robots.add(centerRobot);
    }
}