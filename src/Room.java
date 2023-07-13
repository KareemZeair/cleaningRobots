import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static java.awt.Color.RGBtoHSB;

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
        float[] hsb = new float[3];

        for (int i = 0; i < roomSize; i++) {
            for (int x = 0; x < roomSize; x++) {
                JPanel cell = new JPanel();
                cell.setPreferredSize(new Dimension(50, 50));
                cell.setBackground(Color.getHSBColor(Color.RGBtoHSB(112, 75, 35, hsb)[0], Color.RGBtoHSB(112, 75, 35, hsb)[1], Color.RGBtoHSB(112, 75, 35, hsb)[2]));
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                int y = roomSize - i - 1;

                JLabel label = new JLabel(x + ", " + y);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("Arial", Font.PLAIN, 10));
                cell.add(label);

                panel.add(cell);

                cellPanels[x][y] = cell; // Store reference to cell panel in the 2D array
            }
        }

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
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
