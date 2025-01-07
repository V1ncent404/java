
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

class Frame extends JFrame {

    final int FRAME_WIDTH = 250;
    final int FRAME_HEIGHT = 350;

    JPanel formPanel;
    JLabel headerLabel;
    JLabel usernameLabel;
    JLabel passwordLabel;
    JTextField usernameTextField;
    JPasswordField passwordTextField;
    JLabel textLabel;
    JButton logButton;
    JButton signButton;
    JCheckBox showCheckBox;

    Frame() {
        // FRAME
        this.setTitle("LOG IN");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        // PANEL
        formPanel = new JPanel();
        formPanel.setBackground(new Color(254, 216, 177));
        formPanel.setLayout(null);
        this.add(formPanel);

        // HEADER LABEL
        headerLabel = new JLabel("LOG IN");
        headerLabel.setBounds((250 / 2) - (100 / 2), 15, 100, 50);
        headerLabel.setForeground(new Color(25, 25, 25));
        headerLabel.setFont(new Font("sanserif", Font.BOLD, 28));
        formPanel.add(headerLabel);

        // USERNAME LABEL
        usernameLabel = new JLabel();
        usernameLabel.setText("Username ");
        usernameLabel.setBounds(20, 80, 150, 20);
        usernameLabel.setForeground(new Color(25, 25, 25));
        usernameLabel.setFont(new Font("sanserif", Font.BOLD, 12));
        formPanel.add(usernameLabel);

        // USERNAME TEXTFIELD
        usernameTextField = new JTextField();
        usernameTextField.setBounds(20, 110, FRAME_WIDTH - (30 * 2), 25);
        usernameTextField.setFont(new Font("sanserif", Font.PLAIN, 14));
        usernameTextField.setForeground(new Color(25, 25, 25));
        usernameTextField.setBorder(null);
        formPanel.add(usernameTextField);

        // PASSWORD LABEL
        passwordLabel = new JLabel();
        passwordLabel.setText("Password ");
        passwordLabel.setBounds(20, 150, 150, 20);
        passwordLabel.setForeground(new Color(25, 25, 25));
        passwordLabel.setFont(new Font("sanserif", Font.BOLD, 12));
        formPanel.add(passwordLabel);

        // PASSWORD TEXTFIELD
        passwordTextField = new JPasswordField();
        passwordTextField.setBounds(20, 180, FRAME_WIDTH - (30 * 2), 25);
        passwordTextField.setFont(new Font("sanserif", Font.PLAIN, 14));
        passwordTextField.setForeground(new Color(25, 25, 25));
        passwordTextField.setBorder(null);
        passwordTextField.setMargin(new Insets(5, 10, 5, 10));
        formPanel.add(passwordTextField);

        // TEXT LABEL
        textLabel = new JLabel();
        textLabel.setBounds(20, 210, 200, 15);
        textLabel.setFont(new Font("sanserf", Font.PLAIN, 10));
        textLabel.setForeground(new Color(255, 25, 25));
        formPanel.add(textLabel);

        // SHOW AND HIDE CHECKBOX
        showCheckBox = new JCheckBox();
        showCheckBox.setFont(new Font("sanserf", Font.PLAIN, 8));
        showCheckBox.setBounds((FRAME_WIDTH / 4) * 3 + 5, 160, 20, 15);
        // showCheckBox.setBackground(Color.white);
        showCheckBox.setContentAreaFilled(false);
        showCheckBox.setOpaque(false);
        showCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (showCheckBox.isSelected())
                    passwordTextField.setEchoChar((char) 0);
                else
                    passwordTextField.setEchoChar('*');
            }

        });
        formPanel.add(showCheckBox);

        // LOG IN BUTTON
        logButton = new JButton();
        logButton.setText("LOG IN");
        logButton.setBounds(
                (FRAME_WIDTH / 2) - (110 / 2),
                (FRAME_HEIGHT / 3) * 2 - 10,
                100,
                30);
        logButton.setFont(new Font("sanserif", Font.BOLD, 14));
        logButton.setForeground(new Color(254, 216, 177));
        logButton.setBackground(new Color(111, 78, 55));
        logButton.setFocusable(false);

        logButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String query = "SELECT * FROM user_accounts WHERE user_name = ? AND user_pass = ?";
                String userQuery = "SELECT user_name FROM user_accounts WHERE user_name = ?";
                String passQuery = "SELECT user_pass FROM user_accounts WHERE user_pass = ?";

                String username = usernameTextField.getText();
                String password = new String(passwordTextField.getPassword());

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");

                    Connection conn = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/user_accounts",
                            "root",
                            "rootroot");

                    PreparedStatement userQueryStatement = conn.prepareStatement(userQuery);
                    userQueryStatement.setString(1, username);
                    ResultSet userResult = userQueryStatement.executeQuery();

                    if (!userResult.next()) {
                        JOptionPane.showMessageDialog(
                                null,
                                username + " doesn't exist",
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    PreparedStatement passQueryStatement = conn.prepareStatement(passQuery);
                    passQueryStatement.setString(1, password);
                    ResultSet passResult = passQueryStatement.executeQuery();

                    if (!passResult.next()) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Password is incorrect",
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);

                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        setVisible(false);
                        GroupChatServer gcs = new GroupChatServer();
                        gcs.startServer();

                    }

                    resultSet.close();
                    preparedStatement.close();
                    conn.close();
                } catch (ClassNotFoundException | SQLException | IOException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            ex.getMessage(),
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        formPanel.add(logButton);

        signButton = new JButton();
        signButton.setText("SIGN IN");
        signButton.setBounds(
                (FRAME_WIDTH / 2) - (110 / 2),
                (FRAME_HEIGHT / 3) * 2 + 30,
                100,
                30);
        signButton.setFont(new Font("sanserif", Font.BOLD, 14));
        signButton.setForeground(new Color(254, 216, 177));
        signButton.setBackground(new Color(111, 78, 55));
        signButton.setFocusable(false);
        signButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String query = "INSERT INTO user_accounts" +
                        "(user_id, user_name, user_pass)" +
                        "VALUES (?, ?, ?)";

                String queryResult = "SELECT * FROM user_accounts WHERE user_name = ?";

                Random rand = new Random();
                int random = rand.nextInt();
                String username = usernameTextField.getText();
                String password = new String(passwordTextField.getPassword());

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");

                    Connection conn = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/user_accounts",
                            "root",
                            "rootroot");

                    if (username.trim().isEmpty() || password.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(
                                null,
                                "TextField is Empty",
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (password.contains(" ") || username.contains(" ")) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Password and Username must not contain spaces",
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String symbol = ".*[^a-zA-Z0-9].*";

                    if (!password.matches(symbol)) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Password must contains " + symbol,
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (password.length() <= 7 || password.length() >= 17) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Password must be (8-16) characters long",
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    PreparedStatement preStatement = conn.prepareStatement(queryResult);
                    preStatement.setString(1, username);

                    ResultSet result = preStatement.executeQuery();

                    if (result.next()) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Username Existing",
                                "EXIST",
                                JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    PreparedStatement prepStatement = conn.prepareStatement(query);
                    prepStatement.setInt(1, random);
                    prepStatement.setString(2, username);
                    prepStatement.setString(3, password);
                    prepStatement.executeUpdate();

                    JOptionPane.showMessageDialog(
                            null,
                            "Account Created",
                            "CREATED",
                            JOptionPane.INFORMATION_MESSAGE);

                    prepStatement.close();
                    preStatement.close();
                    result.close();
                    conn.close();

                    return;

                } catch (ClassNotFoundException | SQLException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            ex.getMessage(),
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE);

                    System.out.println(ex.getMessage());
                }

            }

        });
        formPanel.add(signButton);

        this.setVisible(true);
    }

}

public class GroupChatSystem {
    public static void main(String[] args) throws IOException {
        new Frame();

        // new GroupMessagingSystem();
    }
}