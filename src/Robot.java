import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


//!error: Need to add throw statement for when robots.txt is empty but ask Dr. Reza to be sure..


public class Robot
{
    static String fileName = "robots.txt"; // Specify the path and name of your text file
    static File file = new File(fileName);
    private static int robotCount;
    private int[] robotCoordinates = new int[2]; // {Y-axis, X-axis}
    private String initialDirection;

    public Robot()
    {
    }

    public static void main(String[] args)
    {
        try
        {
            Scanner scanner = new Scanner(file);
            robotCount = Integer.parseInt(scanner.nextLine());
            scanner.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("INPUT ERROR");
        }

    }

    // Setters
    public void setRobotCoordinates(int[] coordinates) {
        this.robotCoordinates = coordinates;
    }
    public void setInitialDirection(String initialDirection) {this.initialDirection = initialDirection;}

    //getters
    public int getRobotCount()
    {
        return robotCount;
    }
    public int[] getRobotCoordinates()
    {
        return robotCoordinates;
    }
    public String getInitialDirection() {return initialDirection;}
}
