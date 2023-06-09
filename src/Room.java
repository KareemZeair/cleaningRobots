import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class Room
{

    private boolean[][] grid;
    private int roomSize;
    private JPanel[][] cellPanels;
    private JFrame frame = new JFrame();
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

        for (int i = 0; i < roomSize; i++) {
            for (int j = 0; j < roomSize; j++) {
                JPanel cell = new JPanel();
                cell.setPreferredSize(new Dimension(50, 50));
                cell.setBackground(Color.GRAY);
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                int x = j;
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


    //changes the color of one cell
    public void changeCellColor(int x, int y, Color color) {
        if (x >= 0 && x < roomSize && y >= 0 && y < roomSize) {
            cellPanels[x][y].setBackground(color);
        } else {
            System.out.println("Invalid cell coordinates: (" + x + ", " + y + ")");
        }
    }

    public void updateGrid(boolean[][] newGrid, int[] robotPosition, int roomSize) {
        this.grid = newGrid;

        for (int y = 0; y < newGrid.length; y++) {
            for (int x = 0; x < newGrid.length; x++) {
                if ((x == robotPosition[0]) && (y == roomSize - robotPosition[1] - 1))
                {
//                    System.out.println("robotPosition[1]: " + x +", robotPosition[0]: "+ y);
                    cellPanels[x][y].setBackground(Color.RED);
                }
                else if (newGrid[y][x])
                {
                    cellPanels[x][y].setBackground(Color.WHITE);
                }
                else
                {
                    cellPanels[x][y].setBackground(Color.GRAY);
                }
            }
        }

        frame.revalidate();
        frame.repaint();
    }

    //  getter for room size
    public int getRoomSize() {return roomSize;};

    public static void main(String[] args)
    {
    }
}
