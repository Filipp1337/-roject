
import data.User;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import sql.QueryUser;

public class MainWindow extends JFrame {

    public static User user;
    private QueryUser queryUser;
    private GameField gameField;

    private JMenuItem menuNewGame;
    private JMenuItem menuItemEntry;
    private JMenuItem menuItemReg;
    private JMenuItem menuItemStat;
    private JMenuItem menuItemExit;

    public MainWindow() {

        queryUser = new QueryUser();

        setTitle("Змейка");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(470, 500);
        setLocation(400, 400);

        gameField = new GameField();

        // Создание строки главного меню
        JMenuBar menuBar = new JMenuBar();

        // Создание выпадающего меню
        JMenu file = new JMenu("Файл");
        menuNewGame = new JMenuItem("Новая игра");
        menuNewGame.setEnabled(false);
        menuItemEntry = new JMenuItem("Авторизация");
        menuItemReg = new JMenuItem("Регистрация");
        menuItemStat = new JMenuItem("Статистика");
        menuItemExit = new JMenuItem("Выход");

        file.add(menuNewGame);
        file.add(menuItemEntry);
        file.add(menuItemReg);
        file.add(menuItemStat);
        file.add(menuItemExit);

        menuBar.add(file);

        setJMenuBar(menuBar);

        menuNewGame.addActionListener((ActionEvent e) -> {
            gameField.initGame();
            setVisible(true);
        });

        menuItemEntry.addActionListener((ActionEvent e) -> {
            showDialogInitUser("Авторизация", true);
        });

        menuItemReg.addActionListener((ActionEvent e) -> {
            showDialogInitUser("Регистрация", false);
        });

        menuItemStat.addActionListener((ActionEvent e) -> {
            showDialogStatistic();
        });

        menuItemExit.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });

        add(gameField);
        setResizable(false);
        setVisible(true);
    }

    /**
     * Диалоговое окно входа и регистрации пользователя
     *
     * @param title название окна
     * @param entry true - вход, false - регистрация
     */
    private void showDialogInitUser(String title, boolean entry) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel panelBorder = new JPanel();
        panelBorder.setBackground(Color.lightGray);
        JPanel panelGrid = new JPanel();
        panelGrid.setBackground(Color.lightGray);

        GridLayout layout = new GridLayout(2, 2, 0, 10);

        panelGrid.setLayout(layout);
        panelGrid.add(new JLabel("Логин"));

        JTextField jTextFieldLogin = new JTextField();
        jTextFieldLogin.setPreferredSize(new Dimension(200, 25));
        JPasswordField jPasswordFieldPassword = new JPasswordField();
        jPasswordFieldPassword.setPreferredSize(new Dimension(200, 25));

        panelGrid.add(jTextFieldLogin);
        panelGrid.add(new JLabel("Пароль"));
        panelGrid.add(jPasswordFieldPassword);

        BorderLayout border = new BorderLayout();
        panelBorder.setLayout(border);
        panelBorder.add(panelGrid, BorderLayout.NORTH);

        JPanel panelButton = new JPanel();
        panelButton.setLayout(new GridLayout(2, 1));

        JButton jbuttonOk = new JButton("Ок");
        JButton jButtonCancel = new JButton("Отмена");

        jbuttonOk.addActionListener((ActionEvent e) -> {
            //получение данных с текстовых полей
            String login = jTextFieldLogin.getText();
            String password = new String(jPasswordFieldPassword.getPassword());
            //проверка введенных данных на пустоту
            if (login.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Данные введены не корректно", "Ввод данных", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //если выполняется вход, то
            if (entry) {
                user = queryUser.getUser(login, password);
                if (user != null) {
                    dialog.setVisible(false);
                    JOptionPane.showMessageDialog(this, "Вход выполнен успешно", "Ввод данных", JOptionPane.INFORMATION_MESSAGE);
                    menuItemEntry.setEnabled(false);
                    menuItemReg.setEnabled(false);
                    menuNewGame.setEnabled(true);
                    gameField.updateUI();
                } else {
                    dialog.setVisible(false);
                    JOptionPane.showMessageDialog(this, "Данный пользователь не найден", "Ввод данных", JOptionPane.WARNING_MESSAGE);
                }
                //если выполняется регистрация
            } else {
                boolean add = queryUser.add(login, password);
                if (add) {
                    dialog.setVisible(false);
                    JOptionPane.showMessageDialog(this, "Регистрация прошла успешно", "Регистрация", JOptionPane.INFORMATION_MESSAGE);
                    menuItemReg.setEnabled(false);
                } else {
                    dialog.setVisible(false);
                    JOptionPane.showMessageDialog(this, "Регистрация не выполнена", "Регистрация", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        jButtonCancel.addActionListener((ActionEvent e) -> {
            dialog.setVisible(false);
        });

        panelButton.add(jbuttonOk);
        panelButton.add(jButtonCancel);

        panelBorder.add(panelButton, BorderLayout.SOUTH);

        dialog.add(panelBorder);
        dialog.setSize(300, 150);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showDialogStatistic() {
        JDialog dialog = new JDialog(this, "Статистика", true);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setSize(400, 400);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(this);

        List<User> allByScore = queryUser.getAllByScore();
        Vector dataVector = new Vector();
        Vector rowVector;
        for (User u : allByScore) {
            rowVector = new Vector<>();
            rowVector.add(u.getLogin());
            rowVector.add(u.getScore() + "");
            dataVector.add(rowVector);
        }
        Vector columnName = new Vector();
        columnName.add("Логин");
        columnName.add("Очки");
        JTable jTable = new JTable(dataVector, columnName);
        jTable.setBackground(Color.lightGray);
        jTable.setEnabled(false);
        dialog.add(jTable);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        new MainWindow();
    }
}
