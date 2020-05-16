//:
package com.yanggongzhuo.function;

import com.yanggongzhuo.frame.ServerUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 聊天室服务器端，集成了TCP和UCP组播组服务器
 * @date 2020-05-12 16:33:21
 * @author ygz
 * @version 1.0
 */
public class Server {
    private final int port = 5678;
    /**服务器UI*/
    private ServerUI serverUI;
    /**    服务器TCP服务器*/
    private ServerSocket server;
    /**    服务器多播数据报套接字实现服务器*/
    private MulticastSocket multicastServer;
    /**    多人聊天室服务器组播组地址*/
    private InetAddress talkGroup;
    private boolean bStart = false;
    private int index = 0;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private HashMap<String, Socket> clientCollections = new HashMap<>();
    private DateFormat df3 = new SimpleDateFormat("HH:mm:ss");
    private ComboBoxModel<String> model;
    private Hashtable<String, String> UDPOnlineMembersList;

    public Server() {
        serverUI = new ServerUI("服务器");
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.runServer();
    }

    /**
     * 运行两个服务器，为他们开辟两个线程
     */
    public void runServer() {
        new Thread(new RunTCPServer()).start();
        new Thread(new RunUDPServer()).start();
    }

    /**
     * 一个实现Runnable接口的内部类，将为TCP服务器单独开辟一个线程
     */
    private class RunTCPServer implements Runnable {
        @Override
        public void run() {
            try {
                server = new ServerSocket(5678);
                receiveClient();
            } catch (IOException e) {
                System.out.println("创建TCP服务器套接字失败");
                e.printStackTrace();
            }

        }
    }

    /**
     * 一个实现了Runnable接口的内部类，用来为UDP服务器开辟新的线程
     */
    private class RunUDPServer implements Runnable {
        /**
         * 创建UDP多播数据报套接字用来当作组播组服务器，并初始话组播地址，将套接字添加到组中，
         * 给服务器frame添加一些时间监听器，增加用户体验
         * @param
         * @retyrn
         */
        @Override
        public void run() {
            try {
                multicastServer = new MulticastSocket(5678);
                multicastServer.setLoopbackMode(false);
                talkGroup = InetAddress.getByName("230.0.0.1");
                multicastServer.joinGroup(talkGroup);
                UDPOnlineMembersList = new Hashtable<>();

                //    给发送按钮设置监听,归纳错误情景，增加容错率
                serverUI.getSendAllMember().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (! (serverUI.getUdpSend2AllMembersArea().getText().equals("") ||
                                serverUI.getUdpSend2AllMembersArea().getText().equals("发送至组播组全体人员") ||
                                    serverUI.getUdpSend2AllMembersArea().getText().equals("输入框为空"))) {
                            String manager = "管理员:";
                            Date date = new Date();
                            String msg = manager + " " + df3.format(date) + "\n" +
                                    serverUI.getUdpSend2AllMembersArea().getText();
                            byte[] buf1 = msg.getBytes();
                            DatagramPacket packet = new DatagramPacket(buf1, buf1.length, talkGroup, 5678);
                            try {
                                multicastServer.send(packet);
                                serverUI.getUdpServerArea().append(msg + "\n");
                                serverUI.getUdpSend2AllMembersArea().setText("");
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        } else {
                            serverUI.getUdpSend2AllMembersArea().setText("输入框为空");
                        }
                    }
                });

                //    给UDP服务器输入框设置焦点监听，使用户使用更加合理，体验更好
                serverUI.getUdpSend2AllMembersArea().addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
//                        super.focusGained(e);
                        if("输入框为空".equals(serverUI.getUdpSend2AllMembersArea().getText()) ||
                            serverUI.getUdpSend2AllMembersArea().getText().equals("发送至组播组全体人员")){
                            serverUI.getUdpSend2AllMembersArea().setText("");
                        }
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
//                        super.focusLost(e);
                        if("输入框为空".equals(serverUI.getUdpSend2AllMembersArea().getText()) ||
                            serverUI.getUdpSend2AllMembersArea().getText().equals("")){
                            serverUI.getUdpSend2AllMembersArea().setText("发送至组播组全体人员");
                        }
                    }
                });
                //    开启组播组服务器的接收信息的线程
                new Thread(new Rec2Group()).start();
            } catch (IOException e) {
                System.out.println("创建多人聊天室服务器失败");
                e.printStackTrace();
            }

        }
    }

    /**
     * 一个实现了Runnable接口的内部类，用来接收组播组中的客户端的信息和指令，
     * 并进行反馈和处理
     */
    private class Rec2Group implements Runnable {
        /**
         * 根据用户的消息进行收发反馈，根据指令来进行连接、退出和发送全员消息等
         */
        @Override
        public void run() {
            while (true) {
                try {
                    byte buf2[] = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buf2, buf2.length);
                    multicastServer.receive(packet);
                    String msg = new String(buf2);
                    String cmd[] = msg.split(" ");
                    System.out.println(msg);
                    if (cmd.length == 3) {
                        if (cmd[1].equals("上线了")) {                 //这里用的原本是ArrayList类，但是报错数组下标越界，查了一下资料才知道是线程不安全的集合类
                                                                        //    可以用Vector来代替，Vector就比list类多了个同步机制
                                                                    //后来发现Vector自己也弄出bug就还是改用了Hashtable
                            UDPOnlineMembersList.put(cmd[0], "");
//                        UDPOnlineMembersList.addElement(cmd[0]);   //todo:这里用add来代替
                            System.out.println("多人聊天加入成员" + cmd[0]);   //todo:这里是检查是否添加进去成员
                            sendAllMemberList();
                        } else if (cmd[1].equals("下线了")) {
                            UDPOnlineMembersList.remove(cmd[0]);
                            System.out.println("移除UDP成员变量" + cmd[0]);   //todo:这里是检查是否添加成员
                            sendAllMemberList();
                        }
                    }
                    if(! (cmd[0].equals("管理员:") || cmd[0].equals("Collection"))){
                        serverUI.getUdpServerArea().append(msg + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //    将UDP服务器连接的用户变动发送给每一个聊天室成员
        private void sendAllMemberList() throws IOException {
            String collection = "Collection ";
            Iterator<String> it;
            it = UDPOnlineMembersList.keySet().iterator();
            while (it.hasNext()) {
                String s1 = it.next();
                collection += (s1 + " ");
            }
            byte buf1[] = collection.getBytes();
            DatagramPacket packet = new DatagramPacket(buf1, buf1.length, talkGroup, 5678);
            multicastServer.send(packet);
            printMembersList();
        }

        //    将UDP服务器连接的用户变动改动到人员列表
        private void printMembersList() {
            serverUI.getUdpOnlineMembersArea().setText("在线用户\n");
            Iterator<String> it;
            it = UDPOnlineMembersList.keySet().iterator();
            while (it.hasNext()) {
                String s = it.next();
                serverUI.getUdpOnlineMembersArea().append(s + "\n");
            }
            System.out.println("UDP: " + UDPOnlineMembersList);
        }
    }

    /**
     * 用来不停地接收来自客户端的请求访问连接，手动实现TCP协议二次握手，
     * 将每一个连接的客户端都开辟新的线程进行处理，并添加进Hashtable里
     */
    public void receiveClient() {                                              //    为TCP服务器接收客户端消息开辟新的线程
        while (true) {
            /**
             * 读取用户的连接请求使用的ip和端口以及本次登录昵称，遍历Hashtable来判断是否存在用户冲突
             * 若冲突则返回Refuse，不冲突则添加进Hashtable，并返回Hashtable里的成员set
             */
            try {
                Socket socket = server.accept();
                String clientCmd;
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                clientCmd = dataInputStream.readUTF();
                String cmd[] = clientCmd.split(" ");
                //TODO:    从容器中查找是否存在该用户，从而做出判断
                if (add4Collection(cmd[3], socket)) {
                    dataOutputStream.writeUTF("Refuse " + cmd[1] + " " +
                            cmd[2] + " " + cmd[3]);
                    //TODO:这一块曾经用的是hashtable的toArray方法，但是hashtable是默认的线程同步的
                } else {
                    // TODO：在这里我开辟了多个线程来处理关于用户上/下线，所以只能用toArray的迭代器来得到容器内的元素，从而得到监听
                    String temp = "Collection ";
                    String userList = "在线用户\n";
                    Iterator<String> it = clientCollections.keySet().iterator();
                    while (it.hasNext()) {
                        String s = it.next();
                        temp += (s + " ");
                        userList += (s + "\n");
                    }
                    model = new DefaultComboBoxModel<>(temp.split(" "));
                    serverUI.getTcpOnlineMembersArea().setText(userList);
                    for (Socket s : clientCollections.values()) {
                        DataOutputStream out = new DataOutputStream(s.getOutputStream());
                        out.writeUTF(temp);
                    }
                    //    打印用户登录信息，并更新右侧用户在线成员列表
                    serverUI.getTcpServerArea().append(clientCmd + " " + df3.format(new Date()) + "\n");
                    User user = new User(cmd[3]);
                    user.setAddress(new InetSocketAddress(cmd[1], Integer.parseInt(cmd[2])));
                    ClientMonitor(user, socket);
                    System.out.println("成功连接" + socket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断Hashtable中是否存在此用户，不存在则添加进Hashtable
     * @param name 登录用户的临时昵称
     * @param socket  登录时所用套接字
     * @return 如果存在用户冲突则返回true，否则返回false
     */
    public boolean add4Collection(String name, Socket socket) {
        boolean isExit;
        isExit = clientCollections.containsKey(name);
        if (!isExit) {
            clientCollections.put(name, socket);
        }
        System.out.println(clientCollections.keySet());
        return isExit;
    }

    /**
     * 给每一个连上服务器的客户端开启新的线程，进行处理
     * @param user User类的实例对象
     * @param client 客户端的套接字
     */
    private void ClientMonitor(User user, Socket client) {

        new Thread(new ClientReceiver(user, client)).start();
    }

    /**
     * 新建实现了Runnable接口的内部类，用于处理每个连接上的客户端的各种指令和消息
     */
    private class ClientReceiver implements Runnable {
        Socket client;
        User user;
        DataOutputStream dataOut;
        DataInputStream dataIn;

        /**
         * 构造函数
         * @param user
         * @param client
         */
        public ClientReceiver(User user, Socket client) {
            this.client = client;
            this.user = user;
            try {
                dataIn = new DataInputStream(client.getInputStream());
                dataOut = new DataOutputStream(client.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         *接收客户端发送消息，获取消息中的指令，从而进行发送指定好友信息、退出和发送至全体客户端在线成员更新set
         */
        @Override
        public void run() {                        //TODO:实现对客户端的每个命令的回馈，包括“退出”、“发送给指定好友信息（小红）,从容其中查找”
            String clientCmd;
            String cmd[];
            boolean stop = true;
            while (stop) {
                try {
                    clientCmd = dataIn.readUTF();
                    cmd = clientCmd.split(" ");
                    if (cmd[0].equals("Exit")) {
                        clientCollections.remove(cmd[3], client);
                        serverUI.getTcpServerArea().append(clientCmd + " " + df3.format(new Date()) + "\n");
                        String temp1 = "Collection ";
                        String userList = "在线用户\n";
                        Iterator<String> it = clientCollections.keySet().iterator();
                        while (it.hasNext()) {
                            String s = it.next();
                            temp1 += (s + " ");
                            userList += (s + "\n");
                        }
                        for (Socket s1 : clientCollections.values()) {
                            DataOutputStream out1 = new DataOutputStream(s1.getOutputStream());
                            out1.writeUTF(temp1);
                        }
                        serverUI.getTcpOnlineMembersArea().setText(userList);
                        System.out.println("移除" + user + user.getAddress());
                        stop = false;

                    } else if (cmd[0].equals("Login")) {

                    } else {
                        Socket targetClient = clientCollections.get(cmd[0]);
                        DataOutputStream tempOut = new DataOutputStream(targetClient.getOutputStream());
                        tempOut.writeUTF(user.getName() + df3.format(new Date()) + "\n" +
                                cmd[1]);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}///:~
