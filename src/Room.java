import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Room
{
    private boolean[][] grid;
    private final JPanel[][] cellPanels;
    private final JFrame frame = new JFrame();
    public Room(boolean[][] grid, int roomSize) {
        this.cellPanels = new JPanel[roomSize][roomSize];
        this.grid = grid;
        createGUI(roomSize);

    }

    //creates the GUI of the room
    public void createGUI(int roomSize) {
        JFrame frame = new JFrame("Room");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(roomSize, roomSize));
        panel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

//        int numSubGrids = roomSize/5;
//        int cellSize = 5;

        float[] hsb = new float[3];
        int j = 0;
        int rank = 0;

//        List<Color> colors = generateUniqueColors(((roomSize/5) * (roomSize/5))+1);
//        System.out.println("size: " + colors.size());


        for (int i = 0; i < roomSize; i++) {
            for (int x = 0; x < roomSize; x++) {
                JPanel cell = new JPanel();
                cell.setPreferredSize(new Dimension(50, 50));
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                int l = x/5;
                int m = (roomSize-i-1)/5;
                Color cellColor = getDistinctColor(l, m);
                cell.setBackground(cellColor);

                int y = roomSize - i - 1;

                if (j % 5 == 0 & i % 5 == 0)
                {
                    int[] topLeftCell = new int[] {x, y};
                    section Section = new section(topLeftCell, rank);
                    rank++;
                }

                JLabel label = new JLabel(x + ", " + y);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("Arial", Font.PLAIN, 10));
                cell.add(label);

                panel.add(cell);

                cellPanels[x][y] = cell; // Store reference to cell panel in the 2D array
                j++;
            }
        }

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }


    private Color getDistinctColor(int x, int y) {
        // Calculate distinct colors based on x and y coordinates
        int red = (x * 51) % 206 + 50;
        int green = (y * 61) % 206 + 50;
        int blue = ((x + y) * 71) % 206 + 50;

        return new Color(red, green, blue);
    }

    public void updateGrid(boolean[][] newGrid, int[] robotPosition, int roomSize, ArrayList<Robot> robots) {
        this.grid = newGrid;

        for (int y = 0; y < newGrid.length; y++) {
            for (int x = 0; x < newGrid.length; x++) {
                if (newGrid[y][x])
                {
                    cellPanels[x][y].setBackground(Color.WHITE);
                }
            }
        }

        for (Robot robot: robots)
        {
            cellPanels[robot.getRobotCoordinates()[0]][roomSize - 1 - robot.getRobotCoordinates()[1]].setBackground(Color.RED);
        }

        frame.revalidate();
        frame.repaint();
    }
}
