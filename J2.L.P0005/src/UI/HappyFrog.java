/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author chiss
 */
public class HappyFrog implements KeyListener {

    public static HappyFrog happyFrog;
    public MainScreen display;

    //Logic game var
    public final int WIDTH = 800, HEIGHT = 800;
    public Renderer renderer;
    public Rectangle frog;
    public ArrayList<Rectangle> columns;
    public int ticks, yMotion, score;
    public boolean gameOver, started;
    public JPanel gameScreen;
    public Timer timer;
    public Random rand;
    public int rbH = 0;

    //Player var
    public int bestScore = 0;
    public int playerRank = 0;
    public int bronzeCt = 0;
    public int silverCt = 0;
    public int goldCt = 0;
    public int platinumCt = 0;

    //system var
    public boolean checkPause = false;

    public HappyFrog() {

        renderer = new Renderer();
        rand = new Random();
        
        display.getGameScreen().add(renderer);
        frog = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
        columns = new ArrayList<Rectangle>();
        addColumn(true);
        addColumn(true);

    }

    public void addColumn(boolean start) {
        int space = 300;
        int width = 100;
        int ground = 120;
        int height = 50 + rand.nextInt(300);
        columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - ground, width, height));
        columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
    }

    public void jump() {
        // auto set up new game when press space again
        if (gameOver) {
            frog = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
            columns.clear();
            yMotion = 0;
            score = 0;

            addColumn(true);
            addColumn(true);
            gameOver = false;
        }

        //while game run
        if (!started) {
            started = true;
        } else if (!gameOver) {
            if (yMotion > 0) {
                yMotion = 0;
            }
            // frog jump 10
            yMotion -= 10;
        }
    }

    public void pause() {
        if (checkPause == false) {
            timer.stop();
            checkPause = true;
            display.getBtnPause().setText("Resume");
        } else {
            //continues play game
            timer.restart();
            checkPause = false;
            display.getBtnPause().setText("Pause");
        }
    }

    public void reset() {
        bestScore = 0;
        bronzeCt = 0;
        silverCt = 0;
        goldCt = 0;
        platinumCt = 0;
        playerRank = 0;
    }

    public Rectangle resetColumn(Rectangle column) {
        int space = 300;
        int width = 100;
        int ground = 120;
        int height = 50 + rand.nextInt(300);
        if (column.y == 0) {
            if (rbH != 0) {
                height = rbH;
            }
            column.setRect(WIDTH + width + 300, 0, width, HEIGHT - (height + space));
            rbH = 0;
        } else {
            column.setRect(WIDTH + width + 300, HEIGHT - height - ground, width, height);
            rbH = height;
        }
        return column;
    }

    public void addMedal(int score) {
        switch (score) {
            case 10:
                bronzeCt++;
                break;
            case 20:
                silverCt++;
                break;
            case 30:
                goldCt++;
                break;
            case 40:
                platinumCt++;
                break;
            default:
                break;
        }
    }

    public void runGame() {
        timer = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int speed = 10 + (playerRank * 2);
                ticks++;

                // game start
                if (started) {
                    
                    playerRank = bestScore / 10;
                    
                    // pipe move to left
                    if (!gameOver) {
                        for (int i = 0; i < columns.size(); i++) {
                            Rectangle column = columns.get(i);
                            column.x -= speed;
                        }
                    }

                    // increase fall speed
                    if (ticks % 2 == 0 && yMotion < 15) {
                        yMotion += 2;
                    }

                    // remove column out 
                    for (int i = 0; i < columns.size(); i++) {
                        Rectangle column = columns.get(i);
                        if (column.x + column.width < 0) {
                            column = resetColumn(column);
                        }
                    }

                    //increase frog fall speed
                    frog.y += yMotion;
                    

                    for (Rectangle column : columns) {

                        //if the frog jump over the pipe => add score
                        if (column.y == 0 && frog.x + frog.width / 2 > column.x + column.width / 2 - 10 && frog.x + frog.width / 2 < column.x + column.width / 2 + 10) {
                            if (!gameOver) {
                                score++;
                                addMedal(score);
                                if (bestScore <= score) {
                                    bestScore = score;
                                }
                                playerRank = bestScore / 10;
                            }
                        }

                        // If the frog hit the pipe
                        if (column.intersects(frog)) {
                            gameOver = true;
                            //timer.stop();
                            if (bestScore <= score) {
                                bestScore = score;
                            }
                            if (frog.x <= column.x) {
                                frog.x = column.x = frog.width;
                            } else {
                                if (column.y != 0) {
                                    frog.y = column.y - frog.height;
                                } else if (frog.y < column.height) {
                                    frog.y = column.height;
                                }
                            }
                        }
                    }

                    //The frog touches a ground
                    if (frog.y > HEIGHT - 120 || frog.y < 0) {
                        gameOver = true;
                        //timer.stop();
                        if (bestScore <= score) {
                            bestScore = score;
                        }
                    }

                    //The frog jump over the sky
                    if (frog.y + yMotion >= HEIGHT - 120) {
                        frog.y = HEIGHT - 120 - frog.height;
                        gameOver = true;
                        //timer.stop();
                        if (bestScore <= score) {
                            bestScore = score;
                        }
                    }
                }
                renderer.repaint();
            }
        });
        timer.start();
    }

    public void repaint(Graphics g) {

        //g.fillRect(0, 800, 900, 100);
        //sky
        g.setColor(Color.cyan);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        //groud
        g.setColor(Color.BLACK);
        g.fillRect(0, HEIGHT - 120, WIDTH, 120);

        //grass
        g.setColor(Color.GREEN);
        g.fillRect(0, HEIGHT - 120, WIDTH, 20);

        //frog
        g.setColor(Color.RED);
        g.fillRect(frog.x, frog.y, frog.width, frog.height);

        //pipe
        for (Rectangle column : columns) {
            paintColumn(g, column);
        }

        //character
        g.setColor(Color.white);
        g.setFont(new Font("Arial", 1, 70));
        if (!started) {
            g.drawString("Press Space to Start!", 50, HEIGHT / 2 - 50);
        }

        if (gameOver) {
            g.drawString("Game Over!", WIDTH / 4, HEIGHT / 2 - 50);
            g.setFont(new Font("Arial", 1, 20));
            g.drawString("Press Space to Play Again!", 280, 400);
        }

        if (!gameOver && started) {
            g.drawString(String.valueOf(score), WIDTH / 2 - 25, 170);
        }

        if (!gameOver && started) {
            g.setFont(new Font("Arial", 1, 20));
            g.drawString("Bronze medal: " + bronzeCt, 5, 50);
            g.drawString("Silver medal: " + silverCt, 5, 80);
            g.drawString("Gold medal: " + goldCt, 5, 110);
            g.drawString("Platinum medal: " + platinumCt, 5, 140);
        }
    }

    public void paintColumn(Graphics g, Rectangle column) {
        g.setColor(Color.YELLOW.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            jump();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

}
