//GUI Imports
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//Other Imports
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class GUIClient {

    JFrame frame;
    GridLayout gridLayout = new GridLayout(1,2);

    JPanel userList;
    private JTextArea messages;

    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList list = new JList<>(listModel);
    private JScrollPane listScrollPane = new JScrollPane(list);

    private void createAndShowGUI(String user, SocketChannel socket){

        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());  
        }catch(Exception e) {e.printStackTrace();}

        //Window decorations
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create window.
        frame = new JFrame(user);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //Output JPanel and JTextArea inside JScrollPane.
        JPanel output = new JPanel(gridLayout);
        

        messages = new JTextArea(20, 30);
        messages.setBackground(new Color(192,192,192));
        messages.setEditable(false);

        output.add(new JScrollPane(messages));

        //Input Jpanel and JTextField + JButton.
        JPanel input = new JPanel(new FlowLayout());
        JTextField text = new JTextField(65);
        JButton send = new JButton("Send");

        send.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                    String msg = text.getText();
                    if(!(msg.equals(""))){

                        messages.append("Me: " + msg + "\n");
                        text.setText("");

                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        buffer.put(msg.getBytes());
                        buffer.flip();
                        socket.write(buffer);
                    }
                    
                }catch(IOException ex){}

            }
        });

        JButton exit = new JButton("Exit");

        exit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                    String msg = "exit";

                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    buffer.put(msg.getBytes());
                    buffer.flip();
                    socket.write(buffer);
                    frame.dispose();

                }catch(IOException ex){}

            }
        });

        input.setBackground(Color.black);
        send.setBackground(Color.green);
        exit.setBackground(Color.red);

        input.add(text);
        input.add(send);
        input.add(exit);

        //User List
        userList = new JPanel(gridLayout);

        Dimension dimension = list.getPreferredScrollableViewportSize();
        double width = dimension.getWidth() + 5;
        double height = dimension.getHeight() + 5;
        listScrollPane.setMinimumSize(new Dimension((int) width,(int) height));

        list.setBackground(Color.black);
        list.setForeground(Color.white);

        list.setVisibleRowCount(-1);
        
        

        userList.add(listScrollPane, BorderLayout.CENTER);

        //Add panels to main frame.
        frame.add(output, BorderLayout.CENTER);
        frame.add(input, BorderLayout.PAGE_END);
        frame.add(userList, BorderLayout.EAST);
        
        //Set enter to activate send button
        frame.getRootPane().setDefaultButton(send);
       
        //Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }


    public void threadRead(SocketChannel client) {
        while (true) {
            try {

                ByteBuffer buffer = ByteBuffer.allocate(1024);
                client.read(buffer);
                
                String msg = new String(buffer.array()).trim();

                if (msg.length() > 0) {
                    if(msg.charAt(0) == '#'){

                        listModel.clear();
                        String[] users = msg.split(",");
                        for(int i = 1; i < (users.length - 1) ; i++){
                            listModel.addElement(users[i]);
                        }

                        //Update list Size
                        updateSize();

                    }else{
                        messages.append(msg + "\n");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateSize(){
        Dimension dimension = list.getPreferredScrollableViewportSize();
        double width = dimension.getWidth() + 5;
        double height = dimension.getHeight() + 5;
        listScrollPane.setMinimumSize(new Dimension((int) width,(int) height));
        SwingUtilities.updateComponentTreeUI(frame);
    }


        public static void main(String [] args){

            try {


                GUIClient client = new GUIClient();
                Scanner s = new Scanner(new InputStreamReader(System.in));
    
                System.out.println("Put your nickname: ");
                String nick = s.nextLine();
                System.out.println("Welcome " + nick);
    
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                buffer.put(nick.getBytes());
                buffer.flip();
    
                SocketChannel socketClient = SocketChannel.open(new InetSocketAddress("localhost", 8089));
                socketClient.write(buffer);
                
                //Thread lectura.
                Thread t = new Thread() {
                    public void run() {
                        client.threadRead(socketClient);
                    }
                };
                //Tanquem el thread al tencar la GUI
                t.setDaemon(true);
                t.start();

    

                //GUI Loop.
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run(){
                    client.createAndShowGUI(nick, socketClient);
                }
           });
                
    
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }


