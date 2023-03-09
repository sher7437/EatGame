package com.eateateat;

import java.util.Random;

public class Ball {
    protected int r, x, y, dx, dy;
    private final int id;
    protected Screen screen;
    int type;  // 0玩家；1青蛙；2便便；3药丸；4...18其他

    public Ball(int x, int y, int r, int id, int type, Screen screen, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.id = id;
        this.type = type;
        this.screen = screen;
        Random random = new Random();
        this.dx = random.nextBoolean() ? dx : -dx;
        this.dy = random.nextBoolean() ? dy : -dy;

        screen.repaint_ball(this);
    }

    public Ball(int x, int y, int r, int id, int type, Screen screen) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.id = id;
        this.type = type;
        this.screen = screen;
    }

    public int get_x() {
        return x;
    }

    public int get_y() {
        return y;
    }

    public int get_r() {
        return r;
    }

    public int get_id() {
        return id;
    }

    public void update_loca() {
        x += dx;
        y += dy;
        // 碰到边换方向
        if (x + r > screen.WIDTH || x - r < 0) {
            dx = -dx;
        }
        if (y + r > screen.HEIGHT || y - r < Screen.P_BAR_HEIGHT) {
            dy = -dy;
        }
        // 该球拿去更新
        screen.repaint_ball(this);
    }
}