/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import UI.MainScreen;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author chiss
 */
public class Controller {
    
    private MainScreen hf;
    private JButton btn1 = new JButton();
    private JButton btn2 = new JButton();
    private JButton btn3 = new JButton();
    private JButton btn4 = new JButton();
    private JButton btnF = new JButton();
    private Timer timer;
    private KeyBoard key = new KeyBoard();
    private int point = 0;

    int x1 = 300;
    int h1 = 120;
    int x2 = 565;
    int h2 = 100;
    int h3 = 100;
    int h4 = 120;
    int yF = 50; //Khoảng cách của cóc so với mép trên
    
    //Tạo các biến tạm để lưu vị trí cóc các button và điểm lại
    int x1_1, x2_1, h1_1, h2_1, h3_1, h4_1, yF1, point1;
    boolean checkSave = false;
    boolean checkPause = false;

    public Controller(MainScreen hf) {
        this.hf = hf;
        //Để 2 button này có thể nghe được sự kiện khi mình nhấn phím Up
        hf.btnPause.addKeyListener(key);
        //hf.btnSave.addKeyListener(key);
    }

    //Hàm tạo và thêm các cột + con cóc vào Panel
    public void addButton() {
        //Set ảnh vào cho button cóc
        //không dùng đường dẫn tuyệt đối
        ImageIcon icon = new ImageIcon("icon.jpg");        btnF.setIcon(icon);
        //Set tọa độ x, y, width, height
        //điều chỉnh khoảng cách
        btn1.setBounds(x1, 0, 40, h1);
        btn2.setBounds(x2, 0, 40, h2);
        btn3.setBounds(x1, 320 - h3, 40, h3);
        btn4.setBounds(x2, 320 - h4, 40, h4);
        btnF.setBounds(50, yF, 40, 40);
        //Thêm các button vào panel để hiển thị
        hf.gameScreen.add(btn1);
        hf.gameScreen.add(btn2);
        hf.gameScreen.add(btn3);
        hf.gameScreen.add(btn4);
        hf.gameScreen.add(btnF);
    }

    //Hàm cho game chạy
    public void run() {
        //Timer nghỉ 5ms mỗi lần             
        timer = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {               
                if (key.isPress() == true) {
                    //Khi nhấn Up con cóc sẽ đi lên một khoảng 20
                    yF = yF - 20;
                    //Để con cóc không đi lên liên tục
                    key.setPress(false);
                }
                addButton();
                //Tọa độ x giảm thì các cột sẽ chạy dần từ phải sang trái
                x1--;
                x2--;
                //Cóc rơi từ trên xuống, trục tọa độ của mình ngược
                yF++;
                //Khi cột 1 và 3 đã chạy hết màn hình, sẽ quay ngược lại bên phải màn hình và chạy lại
                if (x1 == -40) {
                    x1 = 480;
                    Random rd = new Random();
                    //Phải cộng thêm một số vì có thể Random = 0 thì sẽ không hiển thị cột
                    h1 = rd.nextInt(40) + 80;
                    h3 = rd.nextInt(30) + 90;
                }
                //Tương tự với cột 2 và 4
                if (x2 == -40) {
                    x2 = 480;
                    Random rd = new Random();
                    h2 = rd.nextInt(30) + 90;
                    h4 = rd.nextInt(40) + 80;
                }
                //Nếu chạm thì dừng thread lại
                if (checkClash() == true) {
                    timer.stop();
                    endGame();
                }
                getPoint();
            }
        });
        timer.start();
    }

    //Hàm kiểm tra chạm giữa con cóc với 4 button và các mép trên dưới
    public boolean checkClash() {
        //Chạm trên và chạm dưới
        //Chạm dưới là 280 vì 320 - 40 để ngay khi chạm mép dưới của con cóc sẽ end game
        if (btnF.getY() <= 0 || btnF.getY() >= 280) {
            return true;
        }
        //Phải biến con cóc và 4 button thành HCN vì chỉ hỗ trợ hàm check chạm giữa các HCN
        Rectangle btF = new Rectangle(btnF.getX(), btnF.getY(), btnF.getWidth(), btnF.getHeight());
        Rectangle bt1 = new Rectangle(btn1.getX(), btn1.getY(), btn1.getWidth(), btn1.getHeight());
        Rectangle bt2 = new Rectangle(btn2.getX(), btn2.getY(), btn2.getWidth(), btn2.getHeight());
        Rectangle bt3 = new Rectangle(btn3.getX(), btn3.getY(), btn3.getWidth(), btn3.getHeight());
        Rectangle bt4 = new Rectangle(btn4.getX(), btn4.getY(), btn4.getWidth(), btn4.getHeight());
        if (btF.intersects(bt1) || btF.intersects(bt2) || btF.intersects(bt3) || btF.intersects(bt4)) {
            return true;
        }
        return false;
    }

    //Hàm tính điểm
    public void getPoint() {
        //Khi cóc và button 1 và 2 có cùng tọa độ x với nhau
        if (btn1.getX() == 50 || btn2.getX() == 50) {
            point++;
            hf.txtPoint.setText("Point: " + point);
            if (point >= 0 && point < 10) {
                hf.txtReward.setText("Try harder to get medal!");
            }
            if (point >= 10 && point < 20) {
                hf.txtReward.setText("You got The Bronze Medal!");
            }
            if (point >= 20 && point < 30) {
                hf.txtReward.setText("You got The Silver Medal!");
            }
            if (point >= 30 && point < 40) {
                hf.txtReward.setText("You got The Gold Medal!");
            }
            if (point >= 40) {
                hf.txtReward.setText("You got The Platinum Medal!");
            }
        }
    }

    public void endGame() {
        if (checkSave == false) {
            //Nếu chưa nhấn save mà end thì chỉ hiện ra 2 options
            Object msg[] = {"New Game", "Exit"};
            int choice = JOptionPane.showOptionDialog(null, "Do you want to play again?", hf.txtReward.getText(), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, msg, msg[0]);
            if (choice == 0) {
                //Khi tạo lại game mới thì tức là tất cả trở về lại ban đầu
                //Chọn "New Game"
                x1 = 300;
                h1 = 120;
                x2 = 550;
                h2 = 120;
                h3 = 100;
                h4 = 100;
                yF = 50;
                point = 0;
                hf.txtPoint.setText("Point: 0");
                timer.restart();
            }
            if (choice == 1) {
                //Chọn "Exit"
                System.exit(0);
            }
        } else {
            //Khi ấn save và end thì hiện ra 3 option
            Object msg[] = {"New Game", "Continue", "Exit"};
            int choice = JOptionPane.showOptionDialog(null, "Do you want to play again?", hf.txtReward.getText(), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, msg, msg[0]);
            if (choice == 0) {
                //Chọn "New Game"
                x1 = 300;
                h1 = 120;
                x2 = 520;
                h2 = 120;
                h3 = 100;
                h4 = 100;
                yF = 50;
                point = 0;
                hf.txtPoint.setText("Point: 0");
                timer.restart();
            }
            if (choice == 1) {
                //Chọn "Continue"
                x1 = x1_1;
                x2 = x2_1;
                h1 = h1_1;
                h2 = h2_1;
                h3 = h3_1;
                h4 = h4_1;
                yF = yF1;
                point = point1;
                hf.txtPoint.setText("Point: " + point);
                timer.restart();
            }
            if (choice == 2) {
                //Chọn "Exit"
                System.exit(0);
            }
        }
    }

    //Hàm xử lý pause button
    public void pause() {
        if (checkPause == false) {
            timer.stop();   
            checkPause = true;
            hf.btnPause.setText("Resume");
        } else {
            //Khi nhấn pause lần nữa phải chạy lại
            timer.restart();
            checkPause = false;
            hf.btnPause.setText("Pause");
        }
    }

    public void save() {
        //Khi nhấn save button thì lưu vị trí hiện hành của cóc vào các biến tạm
        checkSave = true;
        x1_1 = x1;
        x2_1 = x2;
        h1_1 = h1;
        h2_1 = h2;
        h3_1 = h3;
        h4_1 = h4;
        yF1 = yF;
        point1 = point;
    }
}
