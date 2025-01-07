import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GroupMessagingSystem extends JFrame {

    final int FRAME_WIDTH = 600;
    final int FRAME_HEIGHT = 500;

    JPanel chatBoxPanel;
    JTextArea textBoxArea;
    JLabel goBackLabel;
    JButton sendButton;
    JLabel connected;
    JTextArea messagesArea;
    JScrollPane scrollPane;

    GroupMessagingSystem() {

        this.setTitle("DIALOUGE CHAT BOX");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setFocusable(false);

        chatBoxPanel = new JPanel();
        chatBoxPanel.setBackground(new Color(254, 216, 177));
        chatBoxPanel.setLayout(null);
        this.add(chatBoxPanel);

        connected = new JLabel();
        connected.setForeground(new Color(10,10,10));
        connected.setFont(new Font("Monoscaped", Font.BOLD, 12));
        connected.setBounds(
                75,
                (FRAME_HEIGHT / 100),
                80,
                20);
        chatBoxPanel.add(connected);
        
        sendButton = new JButton();
        sendButton.setText("SEND");
        sendButton.setBounds(FRAME_WIDTH - (65 * 2), FRAME_HEIGHT - (52 * 2), 100, 40);
        sendButton.setFont(new Font("Monoscaped", Font.BOLD, 16));
        sendButton.setForeground(new Color(254, 216, 177));
        sendButton.setBackground(new Color(111, 78, 55));
        sendButton.setFocusable(false);
        chatBoxPanel.add(sendButton);

        textBoxArea = new JTextArea();
        textBoxArea.setLineWrap(true);
        textBoxArea.setWrapStyleWord(true);
        textBoxArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textBoxArea.setForeground(new Color(25, 25, 25));
        textBoxArea.setBackground(new Color(240, 240, 240));
        textBoxArea.setMargin(new Insets(2, 2, 2, 2));
        textBoxArea.setBorder(BorderFactory.createRaisedBevelBorder());
        textBoxArea.setBounds(
                (FRAME_WIDTH / 5) - 115,
                (FRAME_HEIGHT / 4) * 3,
                (FRAME_WIDTH / 4) * 3,
                (FRAME_HEIGHT / 6));
        chatBoxPanel.add(textBoxArea);

        messagesArea = new JTextArea(50, 50);
        messagesArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        messagesArea.setMargin(new Insets(0, 0, 0, 0));
        messagesArea.setEditable(false);

        scrollPane = new JScrollPane(messagesArea);
        scrollPane.setBorder(BorderFactory.createEtchedBorder());
        scrollPane.setSize(FRAME_WIDTH - 25, FRAME_HEIGHT - 180);
        scrollPane.setLocation(
                (FRAME_WIDTH / 5) - 115, 
                (FRAME_HEIGHT / 100) + 30);
        //scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        chatBoxPanel.add(scrollPane);

        goBackLabel = new JLabel();
        goBackLabel.setText("Go Back");
        goBackLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        goBackLabel.setForeground(new Color(25, 25, 25));
        goBackLabel.setBounds(5, 5, 62, 20);
        goBackLabel.setBackground(Color.WHITE);
        goBackLabel.setOpaque(true);
        goBackLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        chatBoxPanel.add(goBackLabel);


        this.setVisible(true);
    }
}
