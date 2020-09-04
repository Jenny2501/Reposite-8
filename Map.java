package ru.geekbrains.j_one.lesson_a.online.TicTocToe;

import javafx.scene.paint.Color;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Map extends JPanel {
    public static final int MODE_HVH=0;
    public static final int MODE_HVA=1;

    private static final int DOT_EMPTY = 0;
    private static final int DOT_HUMAN = 1;
    private static final int DOT_AI = 2;

    private static final String  MSG_WIN_HUMAN = " ПОБЕДИЛ ИГРОК!";
    private static final String  MSG_WIN_AI    = "ПОБЕДИЛ КОМПЬЮТЕР!";
    private static final String  MSG_DRAW      =  "НИЧЬЯ!";

    private static final Random RANDOM  = new Random ();


    private int [][] field;
    private int fieldSizeX;
    private int fieldSizeY;
    private int winLength;
    private int cellWidth;
    private int cellHeight;

    Map(){
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                update(e);

                int cellX = e.getX()/cellWidth;
                int cellY = e.getY()/cellHeight;
                System.out.println ("x="+cellX+"y="+cellY);
            }
        });
    }



    void StartNewGame(int gameMod, int fieldSizeX, int fieldSizeY, int winLength){
        this.fieldSizeX = fieldSizeX;
        this.fieldSizeY = fieldSizeY;
        this.winLength = winLength;
        field = new int [fieldSizeX][fieldSizeY];
        repaint();
}
    @Override
    protected void printComponent(Graphics g) {
        super.printComponent(g);

        int width = getWidth();
        int height = getHeight();
        cellWidth = width/fieldSizeX;
        cellHeight = height/fieldSizeY;
        g.setColor(Color.BLACK);
        for (int i = 0 ; i<fieldSizeY;i++);{
            int y = i * cellHeight;
            g.drawLine(0,y,width,y);

        }
        for (int i = 0; i<fieldSizeX;i++);{
            int x = i * cellWidth;
            g.drawLine(x,0,x,height);
        }
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (isEmptyCell(x, y)) continue;
                if (field[y][x] == DOT_HUMAN) {
                    g.setColor(new Color(1, 1, 255));
                    g.fillOval(x * cellWidth + DOT_PADDING,
                            y * cellHeight + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2,
                            cellHeight - DOT_PADDING * 2);
                } else if (field[y][x] == DOT_AI) {
                    g.setColor(Color.RED);
                    g.fillRect(x * cellWidth + DOT_PADDING,
                            y * cellHeight + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2,
                            cellHeight - DOT_PADDING * 2);
                } else {
                    throw new RuntimeException(
                            String.format("Can't recognize cell field[%d][%d]: %d", y, x, field[y][x]));
                }
            }
        }
        if (isGameOver) {
            showMessageGameOver(g);
        }
    }
    }
    private void showMessageGameOver(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 200, getWidth(), 70);
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Times new roman", Font.BOLD, 48));
        switch (stateGameOver) {
            case STATE_DRAW:
                g.drawString(MSG_DRAW, 180, getHeight() / 2);
                break;
            case STATE_WIN_AI:
                g.drawString(MSG_WIN_AI, 20, getHeight() / 2);
                break;
            case STATE_WIN_HUMAN:
                g.drawString(MSG_WIN_HUMAN, 70, getHeight() / 2);
                break;
            default:
                throw new RuntimeException("Unexpected gameOver state: " + stateGameOver);
        }
    }


    //
    private void aiTurn() {
        if(turnAIWinCell()) return;
        if(turnHumanWinCell()) return;
        int x, y;
        do {
            x = RANDOM.nextInt(fieldSizeX);
            y = RANDOM.nextInt(fieldSizeY);
        } while (!isEmptyCell(x, y));
        field[y][x] = DOT_AI;
    }

    private boolean turnAIWinCell() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (isEmptyCell(j, i)) {
                    field[i][j] = DOT_AI;
                    if (checkWin(DOT_AI)) return true;
                    field[i][j] = DOT_EMPTY;
                }
            }
        }
        return false;
        private boolean checkWin(int c) {
            for (int i = 0; i < fieldSizeX; i++) {
                for (int j = 0; j < fieldSizeY; j++) {
                    if (checkLine(i, j, 1, 0, winLength, c)) return true;
                    if (checkLine(i, j, 1, 1, winLength, c)) return true;
                    if (checkLine(i, j, 0, 1, winLength, c)) return true;
                    if (checkLine(i, j, 1, -1, winLength, c)) return true;
                }
            }
            return false;
        }

        private boolean checkLine(int x, int y, int vx, int vy, int len, int c) {
            final int far_x = x + (len - 1) * vx;
            final int far_y = y + (len - 1) * vy;
            if (!isValidCell(far_x, far_y)) return false;
            for (int i = 0; i < len; i++) {
                if (field[y + i * vy][x + i * vx] != c) return false;
            }
            return true;
        }

        private boolean isMapFull() {
            for (int i = 0; i < fieldSizeY; i++) {
                for (int j = 0; j < fieldSizeX; j++) {
                    if (field[i][j] == DOT_EMPTY) return false;
                }
            }
            return true;
        }

        private boolean isValidCell(int x, int y) {
            return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
        }

        private boolean isEmptyCell(int x, int y) {
            return field[y][x] == DOT_EMPTY;
        }

    }
    }
