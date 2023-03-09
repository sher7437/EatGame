package com.eateateat;

import javax.swing.*;
import java.awt.*;

public class PBar {
    // 时间条类
    private final JProgressBar p_bar;

    PBar() {
        this.p_bar = new JProgressBar();
    }

    public int get_value() {
        return p_bar.getValue();
    }

    public JProgressBar get_pbar() {
        return p_bar;
    }

    public void update_value(int increment) {
        // 获取新值
        int new_value = this.p_bar.getValue() + increment;
        // 设置颜色
        if (new_value < 20) {
            this.p_bar.setForeground(Color.red);
        } else if (new_value < 40) {
            this.p_bar.setForeground(Color.orange);
        } else if (new_value < 60) {
            this.p_bar.setForeground(Color.yellow);
        } else if (new_value < 80) {
            this.p_bar.setForeground(Color.green);
        } else {
            this.p_bar.setForeground(Color.blue);
        }
        // 设置新值
        this.p_bar.setValue(new_value);
    }
}