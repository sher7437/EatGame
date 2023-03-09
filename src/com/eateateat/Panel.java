package com.eateateat;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {
    private final Ball[] balls;

    public Panel(Ball[] balls) {
        this.balls = balls;
    }

    public void paste_img(Graphics g, Ball the_ball, String pic, int size) {
        // 渲染给定大小的球
        Image image = Toolkit.getDefaultToolkit().getImage("res/" + pic + ".png");
        the_ball.r = size;
        g.drawImage(image,
                the_ball.get_x() - the_ball.r,
                the_ball.get_y() - the_ball.r,
                the_ball.get_r() * 2,
                the_ball.get_r() * 2,
                this);
    }

    public void paste_img(Graphics g, Ball the_ball, String pic) {
        // 渲染球
        Image image = Toolkit.getDefaultToolkit().getImage("res/" + pic + ".png");
        g.drawImage(image,
                the_ball.get_x() - the_ball.r,
                the_ball.get_y() - the_ball.r,
                the_ball.get_r() * 2,
                the_ball.get_r() * 2,
                this);
    }

    @Override
    public void paint(Graphics g) {
        // 渲染不同种类的球
        super.paint(g);
        for (Ball the_ball : balls) {
            if (the_ball != null) {
                switch (the_ball.type) {
                    // 如果是玩家
                    case 0 -> {
                        paste_img(g, the_ball, "player");
                    }
                    // 如果是青蛙
                    case 1 -> {
                        paste_img(g, the_ball, "gua");
                    }
                    // 如果是便便
                    case 2 -> {
                        paste_img(g, the_ball, "shit", 16);
                    }
                    // 如果是药
                    case 3 -> {
                        paste_img(g, the_ball, "pill", 16);
                    }
                    // 其他
                    default -> {
                        paste_img(g, the_ball, Integer.toString(the_ball.type));
                    }
                }
            }
        }
        super.paintChildren(g);
    }

    @Override
    public void paintComponent(Graphics g) {
        // 绘制背景
        super.paintComponent(g);
        ImageIcon img = new ImageIcon("res/back_ground.jpg");
        g.drawImage(img.getImage(), 0, 0, getWidth(), getHeight(), img.getImageObserver());
    }

}