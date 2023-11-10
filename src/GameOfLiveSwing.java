/**
 * Lizenz: BSD-3-Clause
 * Autor: Muhsin Sahin
 * Version 3.0
 * Datum: 9.11.2023
 * E-Mail: sahinmuhsin2018@gmail.com
 * https://opensource.org/license/bsd-3-clause/
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOfLiveSwing extends JFrame {
    private final byte[][] pool;
    private final byte[][] savedPattern;
    private final JButton[][] cellButtons;
    private static final int numRows = 30;
    private static final int numCols = 40;
    private static final int buttonSize = 20;
    private Timer timer;

    public GameOfLiveSwing() {
        // initialise the arrays for game logic
        pool = new byte[numRows][numCols];
        savedPattern = new byte[numRows][numCols];

        // Create cells and add controls to each cell
        cellButtons = new JButton[numRows][numCols];
        JPanel gridPanel = new JPanel(new GridLayout(numRows, numCols));

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                JButton cellButton = new JButton();
                cellButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
                cellButton.setBackground(Color.GRAY);
                cellButton.addActionListener(new CellClickListener(row, col));
                cellButtons[row][col] = cellButton;
                gridPanel.add(cellButton);
            }
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> startSimulation());
        buttonPanel.add(startButton);

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(e -> stopSimulation());
        buttonPanel.add(stopButton);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetSimulation());
        buttonPanel.add(resetButton);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> savePattern());
        buttonPanel.add(saveButton);

        add(gridPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void startSimulation() {
        // Implement start simulation logic
        stopSimulation();
        // padding
        int pad = 12;// pad must oven number
        byte[][] padded_pool = addPadding(pool, pad);
        timer = new Timer(300, e -> {
            moveLife(padded_pool);
            // get back relevant part into pool
            for (int i = 0; i < pool.length; i++) {
                System.arraycopy(padded_pool[i + pad / 2], pad / 2, pool[i], 0, pool[0].length);
            }
            visualisePool();
        });
        timer.start();
        System.out.println("Simulation started");
    }

    private static void moveLife(byte[][] pool) {
        int pad = 2;// pad must oven number
        byte[][] padded_pool = addPadding(pool, pad);
        byte[][] dummy_pool = new byte[padded_pool.length][padded_pool[0].length];
        for (int i = 1; i < padded_pool.length - 1; i++) {
            for (int j = 1; j < padded_pool[i].length - 1; j++) {
                int sum = 0;
                sum += padded_pool[i - 1][j - 1];
                sum += padded_pool[i - 1][j];
                sum += padded_pool[i - 1][j + 1];
                sum += padded_pool[i][j - 1];
                sum += padded_pool[i][j + 1];
                sum += padded_pool[i + 1][j - 1];
                sum += padded_pool[i + 1][j];
                sum += padded_pool[i + 1][j + 1];
                if (padded_pool[i][j] == 1) {
                    if (sum < 2 || 3 < sum) {
                        dummy_pool[i][j] = 0;
                    } else {
                        dummy_pool[i][j] = 1;
                    }
                } else {
                    if (sum == 3) {
                        dummy_pool[i][j] = 1;
                    } else {
                        dummy_pool[i][j] = 0;
                    }
                }
            }
        }
        // get back relevant part into dummy_pool
        for (int i = 0; i < pool.length; i++) {
            System.arraycopy(dummy_pool[i + pad / 2], pad / 2, pool[i], 0, pool[0].length);
        }
    }

    private static byte[][] addPadding(byte[][] pool, int pad) {
        byte[][] padded_pool = new byte[pool.length + pad][pool[0].length + pad];
        for (int i = 0; i < pool.length; i++) {
            System.arraycopy(pool[i], 0, padded_pool[i + pad / 2], pad / 2, pool[0].length);
        }
        return padded_pool;
    }

    private void visualisePool() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (pool[i][j] == 1) {
                    cellButtons[i][j].setBackground(Color.YELLOW);
                } else {
                    cellButtons[i][j].setBackground(Color.GRAY);
                }
            }
        }
    }

    private void stopSimulation() {
        // Implement stop simulation logic
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        System.out.println("Simulation stopped");
    }

    private void resetSimulation() {
        // Implement reset simulation logic
        stopSimulation();
        for (int i = 0; i < numRows; i++) {
            System.arraycopy(savedPattern[i], 0, pool[i], 0, numCols);
        }
        visualisePool();
        System.out.println("Simulation reset");
    }

    private void savePattern() {
        // Implement save pattern logic
        stopSimulation();
        for (int i = 0; i < numRows; i++) {
            System.arraycopy(pool[i], 0, savedPattern[i], 0, numCols);
        }
        System.out.println("Pattern saved");
    }

    private class CellClickListener implements ActionListener {
        private final int row;
        private final int col;

        public CellClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Implement cell click logic
            System.out.println("Cell clicked: Row " + row + ", Column " + col);
            if (pool[row][col] == 1) {
                cellButtons[row][col].setBackground(Color.GRAY);
                pool[row][col] = 0;
            } else {
                cellButtons[row][col].setBackground(Color.YELLOW);
                pool[row][col] = 1;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameOfLiveSwing gameOfLifeSwing = new GameOfLiveSwing();
            gameOfLifeSwing.setTitle("Game of Life");
            gameOfLifeSwing.setSize(numCols * buttonSize, numRows * buttonSize + 50);
            gameOfLifeSwing.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameOfLifeSwing.setVisible(true);
        });
    }
}
