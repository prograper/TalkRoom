//:
package com.yanggongzhuo.function;

import com.yanggongzhuo.frame.ClientUI;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 集成TCP客户端和UDP多播数据报套接字，实现用户登录后的客户端功能
 * @date 2020-05-12 16:36:44
 * @author ygz
 */
public class Client {
    private User clientUser;
    private Socket client;
    DataOutputStream dataOut;
    DataInputStream dataIn;
    ClientUI clientUI;
    Thread receiveMsg = new Thread(new RecToServer());
    boolean isConnected = true;
    String[] members;
    ComboBoxModel<String> model;
    DateFormat df3 = new SimpleDateFormat("HH:mm:ss");

    /**
     * 客户端的构造
     * @param user 登录客户端时的用户对象
     */
    public Client(User user) {
        this.clientUser = user;
    }

    /**
     * 连接指定的TCP服务器，并对客户端进行一系列的初始化
     */
    public void connect2Server() {
        try {
            client = new Socket("127.0.0.1", 5678);
            InetSocketAddress tempAddress = new InetSocketAddress(client.getLocalAddress(),
                    client.getLocalPort());
            clientUser.setAddress(tempAddress);
            dataIn = new DataInputStream(client.getInputStream());
            dataOut = new DataOutputStream(client.getOutputStream());

            //    发送连接请求
            dataOut.writeUTF("Login " + clientUser);

            //    接收服务器指令，能否进行连接，如果指令是Refuse则退出，否则接收在线成员列表，进行更新
            String ServerReceive = dataIn.readUTF();
            String[] receiveCmd = ServerReceive.split(" ");
            if (receiveCmd[0].equals("Refuse")) {
                isConnected = false;
                JDialog jd = new JDialog(clientUI);
                JOptionPane.showMessageDialog(jd, "连接被拒绝，可能是端口或昵称冲突", "错误提示", 1);
                System.out.println("连接被拒绝，可能是端口或昵称冲突");
            } else {
                members = receiveCmd;
                for (String s : members) {
                    System.out.println(s);
                }
                model = new DefaultComboBoxModel<>(members);
                clientUI = new ClientUI(clientUser.getName() + "的客户端");
                clientUI.getMembersList().setModel(model);

                //    给输入框设置焦点事件监听
                clientUI.getMsgSendBox().addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
//                        super.focusGained(e);
                        if(clientUI.getMsgSendBox().getText().equals("输入框为空") ||
                            clientUI.getMsgSendBox().getText().equals("请先选择好友")){
                            clientUI.getMsgSendBox().setText("");
                        }
                    }
                });

        /** 给发送按钮设置监听器，获取所选择单人聊天的好友，并发送包含聊天信息和指定好友的指令 */
        clientUI
            .getSend2Member()
            .addActionListener(
                new ActionListener() {
                  @Override
                  public void actionPerformed(ActionEvent e) {
                    String msg;
                    String member = (String) clientUI.getMembersList().getSelectedItem();
                    if (!(
                    /*member.equals(clientUser.getName()) || */ member.equals("Collection"))) {
                      if (!(clientUI.getMsgSendBox().getText().equals("")
                          || clientUI.getMsgSendBox().getText().equals("输入框为空")
                          || clientUI.getMsgSendBox().getText().equals("请先选择好友"))) {
                        msg = member + " " + clientUI.getMsgSendBox().getText();
                        try {
                          dataOut.writeUTF(msg);
                          clientUI
                              .getMsgPrintBox()
                              .append(
                                  clientUser.getName()
                                      + df3.format(new Date())
                                      + "\n"
                                      + clientUI.getMsgSendBox().getText()
                                      + "\n");
                          clientUI.getMsgSendBox().setText("");
                        } catch (IOException ioException) {
                          ioException.printStackTrace();
                        }
                      } else {
                        clientUI.getMsgSendBox().setText("输入框为空");
                      }

                    } else {
                      clientUI.getMsgSendBox().setText("请先选择好友");
                    }
                  }
                });

                //    设置关闭窗口监听，向服务器发送退出指令
                clientUI.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
//                    super.windowClosing(e);
                        try {
                            dataOut.writeUTF("Exit " + clientUser);//  将用户数据通过toString()方法传递过去
                            disconnect();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        //System.exit(1);
                    }
                });

                /**    给进入多人聊天室按钮设置监听，加入聊天室*/
                clientUI.getEnterMulticastGroup().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        MulticastTalkRoom demo = new MulticastTalkRoom(clientUser);
                    }
                });
                receiveMsg.start();
                isConnected = true;
                System.out.println("创建客户端成功");
            }
//            todo:判断登陆不上的继续做法，而且将逻辑分开（登录和客户端，分别放在一个demo里面），然后再是客户端
        } catch (IOException e) {
            JDialog jd = new JDialog(clientUI);
            JOptionPane.showMessageDialog(jd, "创建客户端失败", "错误提示", 1);
            System.out.println("创建客户端失败");
            e.printStackTrace();
        }
    }

    /**
     * 断开与服务器的连接
     */
    public void disconnect() {
        try {
            if (dataIn != null) {
                dataIn.close();
            }
            if (dataOut != null) {
                dataOut.close();
            }
            if (client != null) {
                client.close();
            }
            System.out.println("关闭客户端成功");
        } catch (IOException e) {
            System.out.println("关闭客户端失败");
            e.printStackTrace();
        }
    }

    /**
     * 创建内部类实现Runnable接口，用来开辟单独的线程来接收来自服务器的指令或消息
     */
    private class RecToServer implements Runnable {
        @Override
        public void run() {
            try {
                while (isConnected) {
                    String str = dataIn.readUTF();
                    String cmd[];
                    cmd = str.split(" ");
                    if (cmd[0].equals("Collection")) {
                        members = cmd;
                        model = new DefaultComboBoxModel<>(members);
                        clientUI.getMembersList().setModel(model);
                    } else {
                        clientUI.getMsgPrintBox().append(str + "\n");
                    }
                }
            } catch (SocketException e) {
                System.out.println("服务器已关闭");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        User user = new User("杨功卓");
        Client client = new Client(user);
        client.connect2Server();
        System.out.println(user);
    }
}///:~
