import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;


//! Need to add throw statement for when robots.txt is empty but ask Dr. Reza to be sure..
//! Need to add throw statement for when robots.txt is empty but ask Dr. Reza to be sure..
//! Need to add throw statement for when robots.txt is empty but ask Dr. Reza to be sure..
//! Need to add throw statement for when robots.txt is empty but ask Dr. Reza to be sure..


public class Robot
{
    String fileName = "robots.txt"; // Specify the path and name of your text file
    File file = new File(fileName);
    private int x;
    private int y;
//    int[] robotCoordinates = new int[2]; // {Y-axis, X-axis}
    private String direction;
    private Thread thread;
    private char movementType = 'S';

    public void main(String[] args)
    {
        try
        {
            Scanner scanner = new Scanner(file);
            scanner.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("INPUT ERROR");
        }
    }

    // spiral movement of a robot with updates to the grid and the GUI
    public void moveSpirally(Robot robot, int roomSize, boolean[][] grid, Room room, boolean roomIsClean, ArrayList<Robot> robots, boolean collision, int index) throws InterruptedException
    {
        int moveCount = 1; // Number of moves in each direction
        String initialDirection = robot.getDirection();
        int direction;
        int robotX = robot.getRobotCoordinates()[0];
        int robotY = roomSize - 1 - robot.getRobotCoordinates()[1];

        // map directions:- 0: down, 1: left, 2: up, 3: right
        if (Objects.equals(initialDirection, "D")) {direction = 0;}
        else if (Objects.equals(initialDirection, "R")) {direction = 1;}
        else if (Objects.equals(initialDirection, "U")) {direction = 2;}
        else {direction = 3;}

        while (true)
        {
            robot.setMovementType(robot, roomSize);

            if (robot.movementType == 'S')
            {
                for (int i = 0; i < moveCount; i++)
                {
                    robot.setMovementType(robot, roomSize);

                    if (robot.movementType == 'S')
                    {
                        collision = checkForCollision(robots, collision);
                        if (collision){break;}

                        roomIsClean = true;

                        // iterate over robots list and change corresponding cell positions from dirty(False) to clean(True)
                        int[] robot1Position = robot.getRobotCoordinates();
                        grid[roomSize - robot1Position[1] - 1][robot1Position[0]] = true;

                        // if room is dirty set roomIsClean to false
                        for (boolean[] booleans : grid)
                        {
                            for (boolean j : booleans) {if (!j){roomIsClean = false;break;}}
                            if (!roomIsClean){break;}
                        }
                        //check if room is clean to break from while loop
                        if (roomIsClean){System.out.println("ROOM IS CLEAN"); break;}

                        // Move the robot in the current direction
                        if (direction == 0) {robotY--;} // Down
                        else if (direction == 1) {robotX--;} // Left
                        else if (direction == 2) {robotY++;} // Up
                        else if (direction == 3) {robotX++;} // Right

                        // update robot position
                        robot.setX(robotX);//robotCoordinates = new int[]{robotX, (roomSize-1-robotY)};
                        robot.setY(roomSize-1-robotY);//robotCoordinates = new int[]{robotX, (roomSize-1-robotY)};

                        room.updateGrid(grid, robot.getRobotCoordinates(), roomSize, robots);

                        // thread delay
                        Thread.sleep(50);
                    }
                }
                if (collision){;break;}
            }
            else {break;}

            // Increment moveCount every second move in the same direction
            if (direction % 2 == 1) {moveCount++;}

            // Update the direction (cycle counter-clockwise: 0, 3, 2, 1)
            direction = (direction + 3) % 4;

            if (direction == 0) {robot.setDirection("D");}
            else if (direction == 3) {robot.setDirection("L");}
            else if (direction == 2) {robot.setDirection("U");}
            else {robot.setDirection("R");}

            if (roomIsClean){System.out.println("ROOM IS CLEAN"); break;}
        }
    }

    // circular movement of a robot with updates to the grid and the GUI
    public void moveCircularly (Robot robot, int roomSize, boolean[][] grid, Room room, boolean roomIsClean, ArrayList<Robot> robots, boolean collision, int index) throws InterruptedException
    {
        int robotX = robot.getRobotCoordinates()[0];
        int robotY = roomSize - 1 - robot.getRobotCoordinates()[1];

        while (!roomIsClean)
        {
            collision = checkForCollision(robots, collision);
            if (collision){break;}

            // iterate over robots list and change corresponding cell positions from dirty(False) to clean(True)
            int[] robot1Position = robot.getRobotCoordinates();
            grid[roomSize - robot1Position[1] - 1][robot1Position[0]] = true;

            roomIsClean = true;

            if (robotX == roomSize - 1 && robotY < roomSize - 1){robotY++;} //right edge move up
            else if (robotY == roomSize - 1 && robotX > 0){robotX--;} //top edge move left
            else if (robotX == 0 && robotY > 0){robotY--;} //left edge move down
            else if (robotY == 0 && robotX < roomSize - 1){robotX++;} //bottom edge move right

            // update robot position
            robot.setX(robotX);// = new int[]{robotX, (roomSize-1-robotY)};
            robot.setY(roomSize-1-robotY);// = new int[]{robotX, (roomSize-1-robotY)};

            room.updateGrid(grid, robot.getRobotCoordinates(), roomSize, robots);

            // check if room is clean
            for (boolean[] booleans : grid)
            {
                for (boolean j : booleans) {if (!j){roomIsClean = false;break;}}
                if (!roomIsClean){break;}
            }

            // thread delay
            Thread.sleep(50);

            //check if room is clean to break from while loop
            if (roomIsClean){System.out.println("ROOM IS CLEAN");} //break;}
        }
    }

    public void setMovementType(Robot robot, int roomSize)
    {
        int robotX = robot.getRobotCoordinates()[0];
        int robotY = roomSize - 1 - robot.getRobotCoordinates()[1];

        if ((robotY == roomSize - 1 && Objects.equals(robot.direction, "R")) || (robotY == 0 && Objects.equals(robot.direction, "L")) ||
                (robotX == roomSize - 1 && Objects.equals(robot.direction, "U")) || (robotX ==0 && Objects.equals(robot.direction, "D")))
        {
            robot.movementType = 'C';
        }

        else
        {
            robot.movementType = 'S';
        }
    }

    public boolean checkForCollision(ArrayList<Robot> robots, boolean collision)
    {
        collision = false;
        // collision detection
        for (int i = 0; i < robots.size(); i++)
        {
            Robot robotA = robots.get(i);
            for (int j = i + 1; j < robots.size(); j++)
            {
                Robot robotB = robots.get(j);
                if (robotA.getRobotCoordinates()[0] == robotB.getRobotCoordinates()[0] && robotA.getRobotCoordinates()[1] == robotB.getRobotCoordinates()[1])
                {
                    collision = true;
                    System.out.println("COLLISION AT CELL (" + robotA.getRobotCoordinates()[0] + "," + robotB.getRobotCoordinates()[1] + ")");
                    return true;
                }
            }
        }
        return false;
    }


    // Setters
    public void setDirection(String direction) {this.direction = direction;}

    //getters

    public int[] getRobotCoordinates()
    {
        return new int[]{x, y};
    }
    public String getDirection()
    {
        return direction;
    }
    public char getMovementType()
    {
        return movementType;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }
}
