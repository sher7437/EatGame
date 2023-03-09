package com.eateateat;

import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Screen {
    public Ball[] balls = new Ball[Game.BALL_NUM + 1];  // 球数组
    public JFrame frame;  // 窗口
    public Panel panel = new Panel(balls);  // 绘制版面

    public int mouse_x;  // 鼠标x
    public int mouse_y;  // 鼠标y

    public final int WIDTH;  // 画面宽
    public final int HEIGHT;  // 画面高

    public static final int P_BAR_HEIGHT = 30;  // 时间条高
    public static final int BOTTOM_HEIGHT = 70;  // 底部高

    public JButton s_button;  // 开始按钮
    public JButton m_button;  // 规则按钮
    public JButton e_button;  // 退出按钮

    public JLabel score_label;  // 当前得分
    public JLabel best_score_label;  // 最高分
    public JLabel level_label;  // 难度等级

    public JLabel slogan_label;  // 欢迎文字
    public JLabel fail_label;  // 失败文字
    public JLabel message_label1;  // 规则文字
    public JLabel message_label2;
    public JLabel message_label3;
    public JLabel message_label4;
    public JLabel message_label5;

    public PBar time_bar;  // 时间条

    public Screen() {
        // 获取字体
        Font font1 = get_font("res/my_font.ttf", 30);
        Font font2 = get_font("res/my_font.ttf", 80);
        Font font3 = get_font("res/my_font.ttf", 200);

        // 窗体外观简单初始化
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        frame = new JFrame("我是大胃王");
        Image ic = toolkit.getImage("res/20.png");
        frame.setIconImage(ic);

        WIDTH = toolkit.getScreenSize().width;
        HEIGHT = toolkit.getScreenSize().height - BOTTOM_HEIGHT;
        // 全屏
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        frame.setLayout(null);  // java默认为flowLayout布局的，设置为null即为清空布局管理器
        panel.setLayout(null);

        // 初始化开始按钮
        s_button = new JButton();
        s_button.setBounds(560, 600, 80, 80);
        ImageIcon ico = new ImageIcon("res/s_button.png");
        Image temp = ico.getImage().getScaledInstance(80, 80, ico.getImage().SCALE_DEFAULT);
        ico = new ImageIcon(temp);
        s_button.setIcon(ico);
        s_button.setBorder(null);  // 除去边框
        s_button.setFocusPainted(false);  // 除去焦点的框
        s_button.setContentAreaFilled(false);  // 除去默认的背景填充
        s_button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));  // 箭头变小手

        // 初始化规则按钮
        m_button = new JButton();
        m_button.setBounds(680, 600, 80, 80);
        ImageIcon icom = new ImageIcon("res/m_button.png");
        Image tempm = icom.getImage().getScaledInstance(80, 80, icom.getImage().SCALE_DEFAULT);
        icom = new ImageIcon(tempm);
        m_button.setIcon(icom);
        m_button.setBorder(null);  // 除去边框
        m_button.setFocusPainted(false);  // 除去焦点的框
        m_button.setContentAreaFilled(false);  // 除去默认的背景填充
        m_button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));  // 箭头变小手

        // 初始化退出按钮
        e_button = new JButton();
        e_button.setBounds(800, 600, 80, 80);
        ImageIcon icoe = new ImageIcon("res/e_button.png");
        Image tempe = icoe.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT);
        icoe = new ImageIcon(tempe);
        e_button.setIcon(icoe);
        e_button.setBorder(null);
        e_button.setFocusPainted(false);
        e_button.setContentAreaFilled(false);
        e_button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // 初始化分数板
        score_label = new JLabel();
        score_label.setFont(font1);
        score_label.setForeground(Color.pink);
        score_label.setBounds(10, 40, 200, 30);
        score_label.setVisible(false);

        best_score_label = new JLabel();
        best_score_label.setFont(font1);
        best_score_label.setForeground(Color.pink);
        best_score_label.setBounds(10, 70, 200, 30);
        best_score_label.setVisible(false);

        level_label = new JLabel();
        level_label.setFont(font1);
        level_label.setForeground(Color.pink);
        level_label.setBounds(10, 100, 200, 30);
        level_label.setVisible(false);

        // 初始化欢迎文字
        slogan_label = new JLabel("吃掉食物，变成大胃王吧！");
        slogan_label.setFont(font2);
        slogan_label.setForeground(Color.decode("#DC99CC"));
        slogan_label.setBounds(250, 300, 1500, 100);
        slogan_label.setVisible(true);

        // 初始化失败文字
        fail_label = new JLabel("失败");
        fail_label.setBounds(520, 300, 400, 200);
        fail_label.setFont(font3);
        fail_label.setForeground(Color.red);
        fail_label.setVisible(false);

        // 初始化规则文字
        message_label1 = new JLabel("1.吃掉比自己小的食物，同时能恢复少量时间");
        message_label1.setBounds(400, 240, 700, 30);
        message_label1.setFont(font1);
        message_label1.setForeground(Color.blue);
        message_label1.setVisible(false);

        message_label2 = new JLabel("2.不要吃比自己大的食物和便便·v·");
        message_label2.setBounds(400, 280, 700, 30);
        message_label2.setFont(font1);
        message_label2.setForeground(Color.blue);
        message_label2.setVisible(false);

        message_label3 = new JLabel("3.上方时间耗尽会失败");
        message_label3.setBounds(400, 320, 700, 30);
        message_label3.setFont(font1);
        message_label3.setForeground(Color.blue);
        message_label3.setVisible(false);

        message_label4 = new JLabel("4.吃药可以恢复更多时间");
        message_label4.setBounds(400, 360, 700, 30);
        message_label4.setFont(font1);
        message_label4.setForeground(Color.blue);
        message_label4.setVisible(false);

        message_label5 = new JLabel("5.吃青蛙会消化不良变回原型");
        message_label5.setBounds(400, 400, 700, 30);
        message_label5.setFont(font1);
        message_label5.setForeground(Color.blue);
        message_label5.setVisible(false);

        // 初始化时间条
        time_bar = new PBar();
        time_bar.get_pbar().setSize(WIDTH, P_BAR_HEIGHT);
        time_bar.get_pbar().setLocation(0, 0);
        time_bar.get_pbar().setVisible(false);  // 在主页不可见

        // 将组件添加到绘制面板
        panel.add(s_button);
        panel.add(e_button);
        panel.add(m_button);
        panel.add(score_label);
        panel.add(best_score_label);
        panel.add(level_label);
        panel.add(slogan_label);
        panel.add(fail_label);
        panel.add(message_label1);
        panel.add(message_label2);
        panel.add(message_label3);
        panel.add(message_label4);
        panel.add(message_label5);
        panel.add(time_bar.get_pbar());

        // 鼠标动作监听
        frame.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouse_x = e.getX() - 5;
                mouse_y = e.getY() - 20;
            }
        });

        // 设置一下窗体
        frame.setContentPane(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);  // 隐藏窗口
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 退出程序

    }

    //获取字体
    public Font get_font(String filepath, int size) {
        Font font;
        File file = new File(filepath);
        try{
            font = Font.createFont(Font.TRUETYPE_FONT, file);
            font = font.deriveFont(Font.PLAIN, size);
        } catch (FontFormatException | IOException e){
            return null;
        }
        return font;
    }

    // 显示失败
    public void show_fail() {
        fail_label.setVisible(true);
        frame.getContentPane().repaint();
    }

    // 开始前准备
    public void prepare_screen(int score, int best_score, int ball_sleep_time) {
        // 隐藏button
        s_button.setVisible(false);
        e_button.setVisible(false);
        m_button.setVisible(false);
        // 隐藏label
        slogan_label.setVisible(false);
        message_label1.setVisible(false);
        message_label2.setVisible(false);
        message_label3.setVisible(false);
        message_label4.setVisible(false);
        message_label5.setVisible(false);
        // 显示计分板
        score_label.setVisible(true);
        best_score_label.setVisible(true);
        level_label.setVisible(true);

        // 初始化分数、速度、时间条、清空球
        score_label.setText("你吃掉了：" + score);
        best_score_label.setText("历史最多：" + best_score);
        level_label.setText("食物速度：" + (100 - ball_sleep_time));
        clear_balls();
        time_bar.get_pbar().setValue(100);
        time_bar.get_pbar().setVisible(true);
    }

    // 球重绘
    public void repaint_ball(Ball c) {
        if (c != null) {
            // 更新球数组
            balls[c.get_id()] = c;
            // 是玩家，重绘
            if (c.get_id() == 0) {
                frame.getContentPane().repaint();
            }
        }
    }

    // 清空球
    public void clear_balls() {
        Arrays.fill(balls, null);
        frame.getContentPane().repaint();
    }
}
