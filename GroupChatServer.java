
import java.io.IOException;
import java.net.*;

import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GroupChatServer {

    volatile static boolean isFinished = false;
    SocketAddress sAdd;
    public InetAddress inetAdd;
    NetworkInterface netIf;
    int port;
    public static String name = "Dialogue";

    String message;

    public static GroupMessagingSystem gms = new GroupMessagingSystem();

    void startServer() throws IOException {

        inetAdd = InetAddress.getByName("239.0.0.0");
        port = 1234;
        sAdd = new InetSocketAddress(inetAdd, port);
        netIf = NetworkInterface.getByInetAddress(inetAdd);

        gms.connected.setText(" • ONLINE");

        try {
            MulticastSocket mcSocket = new MulticastSocket(port);
            mcSocket.setTimeToLive(0);
            mcSocket.joinGroup(sAdd, netIf);

            Thread thread = new Thread(new ReadThread(mcSocket, inetAdd, port));
            thread.start();

            gms.textBoxArea.setText("Send a message...");

            gms.goBackLabel.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    gms.setVisible(false);
                    isFinished = true;

                    try {

                        mcSocket.leaveGroup(sAdd, netIf);

                    } catch (IOException e1) {

                        JOptionPane.showMessageDialog(
                                null,
                                e1.getMessage());

                    }
                    mcSocket.close();
                    Frame f = new Frame();
                    f.setVisible(true);
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }

            });
            sendMessage(mcSocket, inetAdd, port, message);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage());
        }

    }

    private void sendMessage(MulticastSocket mcSocket, InetAddress inetAdd, int port, String message)
            throws IOException {

        gms.sendButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String message = gms.textBoxArea.getText();

                byte[] buffer = message.getBytes();
                DatagramPacket dgPacket = new DatagramPacket(buffer, buffer.length, inetAdd, port);

                try {
                
                    mcSocket.send(dgPacket);
                    gms.textBoxArea.setText(" ");

                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(
                            null,
                            e1.getMessage());
                }
            }

        });

    }

    public boolean isEmpty(String message) {
        if (message.trim().isEmpty() || message.length() == 0) {
            return true;
        }
        return false;
    }
}

class ReadThread implements Runnable {

    MulticastSocket mSocket;
    InetAddress inetAdd;
    int port;

    ReadThread(MulticastSocket mSocket, InetAddress inetAdd, int port) {
        this.mSocket = mSocket;
        this.inetAdd = inetAdd;
        this.port = port;
    }

    @Override
    public void run() {

        while (!GroupChatServer.isFinished) {

            byte[] buffer = new byte[1000];
            DatagramPacket dgPacket = new DatagramPacket(buffer, buffer.length, inetAdd, port);

            try {

                mSocket.receive(dgPacket);
                String message = new String(buffer, 0, dgPacket.getLength(), "UTF-8");

                GroupChatServer.gms.messagesArea.append(message + "\n");

                if (isEmpty(message)) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Text Area is Empty");
                    return;
                }

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        null,
                        e.getMessage());
            }
        }
    }

    public boolean isEmpty(String message) {
        if (message.trim().isEmpty() || message.length() == 0) {
            return true;
        }
        return false;
    }

}