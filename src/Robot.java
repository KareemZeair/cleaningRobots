import mpi.MPI;
import mpi.MPIException;

import java.util.ArrayList;
import java.util.Objects;

public class Robot
{
    private int x;
    private int y;
    private String direction;
    private Thread thread;
    private int robotSection;
    private char movementType = 'S';
    private int robotId;
    private int prevSection;
    private int currentSection;

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
                        if (roomIsClean){System.out.println("ROOM IS CLEAN"); System.exit(0);}

                        prevSection = getSection(robotX, robotY, roomSize);

                        // Move the robot in the current direction
                        if (direction == 0) {robotY--;} // Down
                        else if (direction == 1) {robotX--;} // Left
                        else if (direction == 2) {robotY++;} // Up
                        else if (direction == 3) {robotX++;} // Right

                        currentSection = getSection(robotX, robotY, roomSize);

                        if (currentSection != prevSection)
                        {
                            try {
                                MPI.COMM_WORLD.Send(new int[]{this.robotId}, 0, 1, MPI.INT, 7, 0);
                            } catch (MPIException e) {
                                e.printStackTrace();
//                                MPI.Finalize();
//                                System.exit(1);
                            }
                            System.out.println("Process " + this.robotSection + ": Sent robotID = " + this.robotId + " to process 1");

                            try {
                                MPI.COMM_WORLD.Probe(0, 0);
                                int[] buffer = new int[1];
                                MPI.COMM_WORLD.Recv(buffer, 0, 1, MPI.INT, 0, 0);
                                this.robotId = buffer[0];
                            } catch (MPIException e) {
                                e.printStackTrace();
//                                MPI.Finalize();
//                                System.exit(1);
                            }
                            System.out.println("moved from: " + prevSection + " to " + currentSection);
                        }


                        // update robot position
                        robot.setX(robotX);
                        robot.setY(roomSize-1-robotY);

                        room.updateGrid(grid, robot.getRobotCoordinates(), roomSize, robots);

                        // thread delay
                        Thread.sleep(600);
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

            if (roomIsClean){System.out.println("ROOM IS CLEAN"); System.exit(0);}
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
            Thread.sleep(600);

            //check if room is clean to break from while loop
            if (roomIsClean){System.out.println("ROOM IS CLEAN"); System.exit(0);}
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


    // Setters & Getters
    public void setDirection(String direction) {this.direction = direction;}
    public int[] getRobotCoordinates() {return new int[]{x, y};}
    public String getDirection() {return direction;}
    public int getPrevSection() {return this.prevSection;}
    public int getCurrentSection() {return this.currentSection;}
    public char getMovementType() {return movementType;}
    public void setX(int x) {this.x = x;}
    public int getX() {return x;}
    public void setY(int y) {this.y = y;}
    public void setRobotId(int id) {this.robotId = id;}
    public int getRobotId() {return this.robotId;}
    public int getY() {return y;}

    public static int getSection(int x, int y, int roomSize) {
        int sectionSize = 5; // Size of each section (5x5)
        int sectionsPerRow =  roomSize/5; // Number of sections per row (15/5)

        int sectionX = x / sectionSize; // Calculate the section's x-coordinate
        int sectionY = (roomSize - 1 - y) / sectionSize; // Calculate the section's y-coordinate with y flipped

        int sectionIndex = (sectionY % sectionsPerRow) * sectionsPerRow + (sectionX % sectionsPerRow);

        return sectionIndex;
    }
}
