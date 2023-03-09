package com.eateateat;

public class Player extends Ball {
    private final int MAX_SIZE;

    public Player(int x, int y, int r, int id, int type, Screen screen, int max_size) {
        super(x, y, r, id, type, screen);
        MAX_SIZE = max_size;
        screen.repaint_ball(this);
    }

    public void update_size(int increment) {
        if (r < MAX_SIZE) {
            r += increment;
            screen.repaint_ball(this);
        }
    }

    public void update_loca() {
        x = screen.mouse_x;
        y = screen.mouse_y;
        screen.repaint_ball(this);
    }

}
