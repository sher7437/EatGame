package com.eateateat;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Game {

    public static final int PLAYER_ORG_R = 15; // 玩家初始半径
    public static final int BALL_NUM = 40; // 球的数量
    public static final int MAX_R = 120; // 最大半径
    public static final int MIN_R = 10; // 最小半径

    public static int ball_sleep_time = 100; // 球移动速度
    public static Random rand; // 随机生成器

    public static int best_score = get_best_score("res/record.txt"); // 历史最高分

    public static volatile int score = 0; // 当前分数
    public static volatile boolean game_state = false; // 游戏状态

    public static final Screen screen = new Screen();

    // 碰撞检测
    public boolean collision(Ball a, Ball b) {
        if (b == null) {
            return false;
        }
        return Math.sqrt(Math.pow(a.get_x() - b.get_x(), 2) + Math.pow(a.get_y() - b.get_y(), 2)) <= a.get_r() + b.get_r();
    }

    // 返回随机种类
    public int get_type(Random r) {
        return r.nextInt(18) + 2;
    }

    // 读取最高分
    public static int get_best_score(String filename) {
        int record = 0;
        try {
            Scanner in = new Scanner(new FileReader(filename));
            if (in.hasNext()) {
                record = in.nextInt();
            } else {
                System.out.println("No record!");
            }
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
        return record;
    }

    // 写入最高分
    public static void update_best_score(String filename, int record) {
        File file = new File(filename);
        Writer writer;
        StringBuilder s_builder = new StringBuilder();
        try {
            s_builder.append(record);
            writer = new FileWriter(file, false);
            writer.write(s_builder.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 返回主页
    public void to_home_page() {
        // 进度条
        screen.time_bar.get_pbar().setVisible(false);
        // 文本
        screen.slogan_label.setText("加油变成更厉害的大胃王吧！");
        screen.slogan_label.setVisible(true);
        screen.fail_label.setVisible(false);
        screen.best_score_label.setText("历史最多：" + best_score);
        // 按钮
        screen.e_button.setVisible(true);
        screen.s_button.setVisible(true);
        screen.m_button.setVisible(true);
        // 球
        screen.clear_balls();

        screen.frame.getContentPane().repaint();
    }

    // 产生球
    public void create_balls(int i, Player[] player, Ball[] balls) {
        int ball_r;
        // 前3/4的小球，R ∈ [MIN_R, me + MIN_R)，快
        if (i < BALL_NUM * 3 / 4) {
            ball_r = rand.nextInt(player[0].get_r()) + MIN_R;
            // 中间那1个球设置为青蛙
            if (i == BALL_NUM / 2) {
                do {
                    // x ∈ [R, width - R), y ∈ [R + bar_width, height - R)
                    // dx, dy ∈ [1, 10]
                    // type:gua
                    balls[i] = new Ball(rand.nextInt(screen.WIDTH - ball_r * 2) + ball_r,
                                            rand.nextInt(screen.HEIGHT - ball_r * 2 - Screen.BOTTOM_HEIGHT) + ball_r + Screen.P_BAR_HEIGHT,
                                            ball_r, i, 1, screen,
                                            rand.nextInt(10) + 1,
                                            rand.nextInt(10) + 1);
                } while (collision(balls[i], player[0]));  // 如果被碰掉了就要生成新的
            }
            // 前3/4的球
            else {
                do {
                    balls[i] = new Ball(rand.nextInt(screen.WIDTH - ball_r * 2) + ball_r,
                                            rand.nextInt(screen.HEIGHT - ball_r * 2 - Screen.BOTTOM_HEIGHT) + ball_r + Screen.P_BAR_HEIGHT,
                                            ball_r, i, get_type(rand), screen,
                                            rand.nextInt(10) + 1,
                                            rand.nextInt(10) + 1);
                } while (collision(balls[i], player[0]));
            }
        }
        // 后1/4的大球，R ∈ [me, MAX_R)，慢
        else {
            ball_r = rand.nextInt(MAX_R - player[0].get_r()) + player[0].get_r();
            do {
                balls[i] = new Ball(rand.nextInt(screen.WIDTH - ball_r * 2) + ball_r,
                                        rand.nextInt(screen.HEIGHT - ball_r * 2 - Screen.BOTTOM_HEIGHT) + ball_r + Screen.P_BAR_HEIGHT,
                                        ball_r, i, get_type(rand), screen,
                                        rand.nextInt(3) + 1,
                                        rand.nextInt(3) + 1);
            } while (collision(balls[i], player[0]));
        }
    }

    // 更新被吃掉的球
    public void update_dead_balls(int i, Player[] player, Ball[] balls) {
        int ball_r, backup_r;
        if (player[0] == null) {
            backup_r = 11;
        }
        else {
            backup_r = player[0].get_r();
        }
        if (i < BALL_NUM * 3 / 4) {
            ball_r = rand.nextInt(backup_r - MIN_R) + MIN_R;
        } else {
            ball_r = rand.nextInt(MAX_R - backup_r) + backup_r;
        }
        if (i == BALL_NUM / 2) {
            do {
                balls[i] = new Ball(rand.nextInt(screen.WIDTH - ball_r * 2) + ball_r,
                                        rand.nextInt(screen.HEIGHT - ball_r * 2 - Screen.BOTTOM_HEIGHT) + ball_r + Screen.P_BAR_HEIGHT,
                                        ball_r, i, 1, screen,
                                        rand.nextInt(10) + 1,
                                        rand.nextInt(10) + 1);
            } while (collision(balls[i], player[0]));
        }
        else {
            do {
                balls[i] = new Ball(rand.nextInt(screen.WIDTH - ball_r * 2) + ball_r,
                        rand.nextInt(screen.HEIGHT - ball_r * 2 - Screen.BOTTOM_HEIGHT) + ball_r + Screen.P_BAR_HEIGHT,
                        ball_r, i, get_type(rand), screen,
                        rand.nextInt(3) + 1,
                        rand.nextInt(3) + 1);
            } while (collision(balls[i], player[0]));
        }
    }

    // 多线程开始游戏
    public synchronized void start_game() throws InterruptedException {
        /*
         * 创建一个玩家球
         * （为什么一个变量要final和[]：因为下面的“方法内部类”使用它，jdk1.8之前只能用final修饰，但是下面还要改值所以[]）
         */
        final Player[] player = {new Player(screen.mouse_x, screen.mouse_y, PLAYER_ORG_R, BALL_NUM, 0, screen, MAX_R)};
        final Ball[] balls = new Ball[BALL_NUM];  // 创建球数组
        score = 0;  // 分数初始化为0
        game_state = true;  // 游戏状态设为true
        rand = new Random();  // 创建随机生成器
        screen.time_bar.get_pbar().setForeground(Color.blue);

        for (int i = 0; i < BALL_NUM; i++) {
            create_balls(i, player, balls);
        }
        screen.frame.getContentPane().repaint();

        // 重载多线程，玩家移动
        class MT_Player implements Runnable {
            @Override
            public synchronized void run() {
                // 游戏开始前停1s
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 在游戏状态且玩家不为null
                while (game_state && player[0] != null) {
                    player[0].update_loca();
                }
            }
        }

        // 重载多线程，球移动和新生成
        class MT_Balls implements Runnable {
            public synchronized void run() {
                // 游戏开始前停1s
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                // 在游戏状态且玩家不为null
                while (game_state && player[0] != null) {
                    for (int i = 0; i < balls.length; i++) {
                        // 如果被碰要创建新的球
                        if (balls[i] == null) {
                            update_dead_balls(i, player, balls);
                        }
                            balls[i].update_loca();

                    }
                    // 重绘所有球
                    screen.frame.getContentPane().repaint();
                    // 让球的线程睡眠一定时间，这个时间会不断变短，制造球越来越快的难度效果
                    try {
                        Thread.sleep(ball_sleep_time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // 重载多线程，时间条
        class MT_Time_Bar implements Runnable {
            public synchronized void run() {
                // 游戏开始前停1s
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 匀速减小2
                while (screen.time_bar.get_value() > 0 && game_state) {
                    screen.time_bar.update_value(-2);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 为0游戏状态false
                game_state = false;
            }
        }

        // 重载多线程，更新分数
        class MT_Check_Update implements Runnable {
            public synchronized void run() {
                // 在游戏状态
                while (game_state) {
                    for (int i = 0; i < balls.length; i++) {
                        if (balls[i] != null && player[0] != null) {
                            // 如果碰撞了
                            if (collision(balls[i], player[0])) {
                                // 是便便
                                if (balls[i].type == 2) {
                                    game_state = false;
                                    break;
                                }
                                // 是药丸
                                if (balls[i].type == 3) {
                                    screen.time_bar.update_value(7);
                                    balls[i] = null;
                                    continue;
                                }
                                // 是小球
                                if (player[0].get_r() > balls[i].get_r()) {
                                    // 不是还原球
                                    if (i != BALL_NUM / 2) {
                                        // 大小+1
                                        player[0].update_size(1);
                                        // 分数+1
                                        score++;
                                        screen.score_label.setText("你吃掉了：" + score);
                                        // 时间条加3
                                        screen.time_bar.update_value(3);
                                        // 每10分增一次速
                                        if (score != 0 && score % 10 == 0) {
                                            if (ball_sleep_time > 40) {
                                                ball_sleep_time -= 20;
                                            } else if (ball_sleep_time > 20) {
                                                ball_sleep_time -= 5;
                                            } else if (ball_sleep_time > 1) {
                                                ball_sleep_time -= 1;
                                            }
                                            screen.level_label.setText("食物速度：" + (100 - ball_sleep_time));
                                        }
                                    }
                                    // 是青蛙
                                    else {
                                        // 变成原始大小
                                        player[0].update_size(-1 * (player[0].get_r()
                                                - PLAYER_ORG_R));
                                    }
                                    // 吃掉要消除
                                    balls[i] = null;
                                }
                                // 是大球
                                else {
                                    game_state = false;
                                    break;
                                }
                            }
                        }
                    }
                }
                // 不在游戏状态了，球全部null
                Arrays.fill(balls, null);
                // 玩家球置null
                player[0] = null;
                // 更新最高分
                if (score > best_score) {
                    best_score = score;
                }
                // 失败画面
                screen.show_fail();
                // 停1s
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 回到主页
                to_home_page();
            }
        }

        // 新建玩家线程
        Thread player_thread = new Thread(new MT_Player());
        // 新建球线程
        Thread balls_thread = new Thread(new MT_Balls());
        // 新建时间条线程
        Thread timebar_thread = new Thread(new MT_Time_Bar());
        // 新建计分线程
        Thread cu_thread = new Thread(new MT_Check_Update());

        // 开始运行多线程
        player_thread.start();
        balls_thread.start();
        cu_thread.start();
        timebar_thread.start();
    }

    // 程序入口
    public static void main(String[] args) {
        // 初始化游戏
        final Game the_game = new Game();
        screen.frame.getContentPane().repaint();

        // 监听开始按钮
        screen.s_button.addActionListener(e -> {
            // 初始化分数、速度、时间条、清空球
            score = 0;
            ball_sleep_time = 100;
            screen.prepare_screen(score, best_score, ball_sleep_time);
            // 开始
            try {
                the_game.start_game();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        });

        // 监听规则按钮
        screen.m_button.addActionListener(e -> {
            // 显示/隐藏规则
            screen.slogan_label.setVisible(!screen.slogan_label.isVisible());
            screen.message_label1.setVisible(!screen.message_label1.isVisible());
            screen.message_label2.setVisible(!screen.message_label2.isVisible());
            screen.message_label3.setVisible(!screen.message_label3.isVisible());
            screen.message_label4.setVisible(!screen.message_label4.isVisible());
            screen.message_label5.setVisible(!screen.message_label5.isVisible());
        });

        // 监听关闭按钮
        screen.e_button.addActionListener(e -> {
            // 保存最高分
            update_best_score("res/record.txt", best_score);
            System.exit(0);
        });

        // 监听窗口关闭
        screen.frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                // 保存最高分
                update_best_score("res/record.txt", best_score);
            }

        });
    }

}
