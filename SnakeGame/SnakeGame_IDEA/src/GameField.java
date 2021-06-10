
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Random;

import sql.QueryUser;

public class GameField extends JPanel implements ActionListener {

    private final int SCREEN_WIDTH = 440;
    private final int SCREEN_HEIGHT = 420;
    private final int UNIT_SIZE = 16;
    private final int ALL_DOTS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE); //ед помещаемые на поле
    private Image Snake;
    private Image apple;
    private Image Cherry;
    private int appleX;
    private int appleY;
    private int CherryX;
    private int CherryY;
    private int[] x = new int[ALL_DOTS]; //сколько всего может быть занято ячеек
    private int[] y = new int[ALL_DOTS];
    private int BodyParts; //длина змейки
    private int totalScore;
    private Timer timer;
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;
    private boolean isPause = false;
    private QueryUser queryUser;

    private int level = 1; //стартовый уровень игры
    private int countApple = 0; //счетчик яблок
    private int countCherry = 0;//счетчик вишен
    private int countTime = 60;//счетчик времени
    private Timer timerLevel;//таймер уровней
    private boolean win = false;//маркер победы

    //*******************параметры инициализации для уровней********************
    private int countAppleL1 = 5;//количество яблок на 1 уровне
    private int countCherryL1 = 5;//количество вишен на 1 уровне
    private int countAppleL2 = 5;//количество яблок на 2 уровне
    private int countCherryL2 = 5;//количество вишен на 2 уровне
    private int countAppleL3 = 10;//количество яблок на 3 уровне
    private int countCherryL3 = 10;//количество вишен на 3 уровне
    private int timeInLevel = 0;//время на уровне (секунд)
    private int fontSize = 18;//размер шрифта в верхней строке

    public GameField() {
        queryUser = new QueryUser();
        setBackground(Color.gray);
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    public void initGame() { //иницилизация игры
        if (timer != null) {
            inGame = true;//возвращаем все значения в исходное состояние
            left = false;
            right = true;
            up = false;
            down = false;
            timer.stop();//останавливаем таймер

            win = false;
            level = 1;
            countApple = 0;
            countCherry = 0;
        }

        if (timerLevel != null) {
            timerLevel.stop();
        }

        initTimerLevel();
        loadImages();

        BodyParts = 3; //начальное количество клеток змеи
        totalScore = BodyParts;
        for (int i = 0; i < BodyParts; i++) {
            x[i] = 48 - i * UNIT_SIZE; //начальная позиция по X,Y
            y[i] = 48;
        }

        timer = new Timer(150, this);
        timer.setRepeats(true);
        timer.start();
        createApple();
        createCherry();
    }

    public void createApple() {
        appleX = new Random().nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE; //позиция яблока
        appleY = new Random().nextInt((SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void createCherry() {
        CherryX = new Random().nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE; //позиция вишни
        CherryY = new Random().nextInt((SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void loadImages() {
        ImageIcon iia = new ImageIcon("image/apple.png");
        apple = iia.getImage();
        ImageIcon iic = new ImageIcon("image/Cherry.png");
        Cherry = iic.getImage();
        ImageIcon iis = new ImageIcon("image/Snake.png");
        Snake = iis.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            Font f = new Font("VERDANA", Font.BOLD, fontSize);
            g.setColor(Color.LIGHT_GRAY);
            g.setFont(f);
            g.drawString("Score: " + totalScore, 0, 20);



            Date date = new Date(1000 * countTime);
            SimpleDateFormat sdf = new SimpleDateFormat("mm : ss");
            String textTime = sdf.format(date);
            String text = "";
            switch (level) {
                case 1:
                    text = "Level: " + level + " | a:" + countApple + "/" + countAppleL1 + ", c:" + countCherry + "/" + countCherryL1;
                    break;
                case 2:
                    text = "Level: " + level + " | a:" + countApple + "/" + countAppleL2 + ", c:" + countCherry + "/" + countCherryL2 + " #" + textTime;
                    break;
                case 3:
                    text = "Level: " + level + " | a:" + countApple + "/" + countAppleL3 + ", c:" + countCherry + "/" + countCherryL3 + " #" + textTime;
                    break;
                default:
                    text = "";
                    break;
            }

            g.drawString(text, 115, 20);

            f = new Font("VERDANA", Font.BOLD, 12);
            g.setFont(f);
            if (MainWindow.user != null) {
                g.drawString(MainWindow.user.getLogin(), 0, 435);
            }

            g.drawImage(apple, appleX, appleY, this);
            g.drawImage(Cherry, CherryX, CherryY, this);
            for (int i = 0; i < BodyParts; i++) {
                g.drawImage(Snake, x[i], y[i], this);
            }

        } else {
            Font f = new Font("VERDANA", Font.BOLD, 20);
            g.setColor(Color.WHITE);
            g.setFont(f);
            if (win) {
                g.drawString("Победа!", (SCREEN_HEIGHT / 2) - 0, (SCREEN_WIDTH / 2 - 20));
            } else {
                g.drawString("Game Over", (SCREEN_HEIGHT / 2) - 40, (SCREEN_WIDTH / 2 - 20));
            }

            g.setColor(Color.RED);
            g.drawString("Your score:" + totalScore, (SCREEN_HEIGHT / 2) - 50, (SCREEN_WIDTH / 2) + 20);
            g.setColor(Color.WHITE);
            g.drawString("Нажмите Пробел для перезапуска", (SCREEN_HEIGHT / 2) - 170, (SCREEN_WIDTH / 2) + 60);
            timer.stop();

            if (MainWindow.user != null && MainWindow.user.getScore() < totalScore) {
                MainWindow.user.setScore(totalScore);
                boolean addScore = queryUser.addScore(MainWindow.user);
                if (!addScore) {
                    JOptionPane.showMessageDialog(this, "Сохранение данных", "Ошибка сохранения результатов игры", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }

    /**
     * Инициализация таймера уровней 2 и 3
     */
    private void initTimerLevel() {
        timerLevel = new Timer(1000, (ActionEvent e) -> {
            countTime--;

            //если время уровня истекло, то
            if (countTime <= timeInLevel) {
                inGame = false;
                timerLevel.stop();
                return;
            }

            if (level == 2) {
                if (countApple >= countAppleL2 && countCherry >= countCherryL2) {
                    level = 3;
                    countApple = 0;
                    countCherry = 0;
                    countTime = 60;
                    timerLevel.stop();
                    timer.stop();
                    int showConfirmDialog = JOptionPane.showConfirmDialog(this, "Уровень 2 пройден. Перейти на уровень 3?", "Уровень пройден", JOptionPane.YES_NO_OPTION);
                    if (showConfirmDialog == JOptionPane.NO_OPTION) {
                        inGame = false;
                        timerLevel.stop();
                    } else {
                        timerLevel.start();
                        timer.start();
                        BodyParts = 3;
                    }
                        
                }
            }

            if (level == 3) {
                if (countApple >= countAppleL3 && countCherry >= countCherryL3) {
                    level = 1;
                    countApple = 0;
                    countCherry = 0;
                    countTime = 60;
                    inGame = false;
                    win = true;
                    timerLevel.stop();
                }
            }
        });
        timerLevel.setRepeats(true);
    }

    public void move() { //сдвиг точек
        for (int i = BodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (left) {
            x[0] -= UNIT_SIZE;
        }
        if (right) {
            x[0] += UNIT_SIZE;
        }
        if (up) {
            y[0] -= UNIT_SIZE;
        }
        if (down) {
            y[0] += UNIT_SIZE;
        }
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            BodyParts++;
            totalScore++;
            countApple++;
            createApple();
        }
        if (x[0] == CherryX && y[0] == CherryY) {
            BodyParts += 2;
            totalScore += 2;
            countCherry++;
            createCherry();
        }

        //если первый уровень и собраны все фрукты, то
        if (level == 1 && countApple >= countAppleL1 && countCherry >= countCherryL1) {
            //переход неа второй уровень
            level = 2;
            //обнуление счетчиков фруктов
            countApple = 0;
            countCherry = 0;
            BodyParts = 3;
            int showConfirmDialog = JOptionPane.showConfirmDialog(this, "Уровень 1 пройден. Перейти на уровень 2?", "Уровень пройден", JOptionPane.YES_NO_OPTION);
            if (showConfirmDialog == JOptionPane.YES_OPTION) {
                //запуск таймера
                timerLevel.start();
            } else {
                inGame = false;
            }
        }
    }

    public void checkCollisions() {
        for (int i = BodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                inGame = false;
            }
        }

        if (x[0] > SCREEN_WIDTH) {
            inGame = false;
        }
        if (x[0] < 0) {
            inGame = false;
        }
        if (y[0] > SCREEN_HEIGHT) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollisions();
            move();
        }
        repaint();
    }

    class FieldKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            float L, R, U, D;
            L = KeyEvent.VK_LEFT;
            R = KeyEvent.VK_RIGHT;
            U = KeyEvent.VK_UP;
            D = KeyEvent.VK_DOWN;

            if (key == L) {
                left = true;
                up = false;
                down = false;
                right = false;
            }
            if (key == R) {
                right = true;
                up = false;
                down = false;
                left = false;
            }
            if (key == U) {
                up = true;
                left = false;
                right = false;
                down = false;
            }
            if (key == D) {
                down = true;
                left = false;
                right = false;
                up = false;
            }
            if (!inGame && key == KeyEvent.VK_SPACE) {//рестарт игры по нажатию пробел
                initGame();//вызываем метод старта новой игры
            }
            if (inGame && key == KeyEvent.VK_R) {//рестарт игры по нажатию пробел
                initGame();//вызываем метод старта новой игры
            }
            if (!isPause && key == KeyEvent.VK_P) {
                timer.stop();
                isPause = true;
            } else if (isPause && key == KeyEvent.VK_P) {
                timer.start();
                isPause = false;
            }
        }
    }
}
