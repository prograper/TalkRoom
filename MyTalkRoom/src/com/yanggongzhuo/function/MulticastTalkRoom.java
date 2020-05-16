//:
package com.yanggongzhuo.function;

import com.yanggongzhuo.frame.MulticastGroupRoom;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 使用多播套接字客户端，实现多人聊天室功能
 * @date 2020-05-12 16:34:55
 * @author ygz
 */
public class MulticastTalkRoom {
    private MulticastGroupRoom talkRoomUI;
    private DatagramPacket packet;
    private MulticastSocket sender;
    private MulticastSocket receiver;
    private InetAddress talkRoomGroup;
    User user;
    DateFormat dfs = new SimpleDateFormat("HH:mm:ss");
    ArrayList<String> membersList = new ArrayList<String>();

    /**
     * 多人聊天室客户端的构造方法：
     * 给窗体设置动作、焦点监听器，设置进入聊天室的用户
     * @param user 聊天室以此用户的名称登录
     */
    public MulticastTalkRoom(User user) {
        this.user = user;
        try {
            sender = new MulticastSocket();
            //    连接到组播组
            receiver = new MulticastSocket(5678);
            talkRoomGroup = InetAddress.getByName("230.0.0.1");
            sender.joinGroup(talkRoomGroup);
            receiver.joinGroup(talkRoomGroup);
            receiver.setLoopbackMode(false);
            String tempMsg = user.getName() + " 上线了 " + dfs.format(new Date());

            byte buf[] = tempMsg.getBytes();
            packet = new DatagramPacket(buf, buf.length, talkRoomGroup, 5678);
            receiver.send(packet);
            talkRoomUI = new MulticastGroupRoom(user.getName() + "的聊天室");
//            talkRoomUI.getMsgPrintArea().append(tempMsg);
            /**    给按钮设置发送到聊天室监听*/
            talkRoomUI.getSend2AllMembers().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (! (talkRoomUI.getMsgSendArea().getText().equals("") ||
                            talkRoomUI.getMsgSendArea().getText().equals("输入为空"))) {
                        Date date = new Date();
                        byte[] buf = (user.getName() + " " + dfs.format(date) + "\n" +
                                talkRoomUI.getMsgSendArea().getText()).getBytes();
                        packet = new DatagramPacket(buf, buf.length, talkRoomGroup, 5678);
                        try {
                            receiver.send(packet);
                            talkRoomUI.getMsgSendArea().setText("");
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    } else {
                        talkRoomUI.getMsgSendArea().setText("输入为空");
                    }
                }
            });
            new Thread(new UDPMonitor()).start();
        } catch (IOException e) {

            e.printStackTrace();
        }

        //    给输入框设置焦点事件监听器
        talkRoomUI.getMsgSendArea().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (talkRoomUI.getMsgSendArea().getText().equals("输入为空")) {
                    talkRoomUI.getMsgSendArea().setText("");
                }
            }
        });

        //    关闭窗口时向服务器发送退出指令
        talkRoomUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
//                super.windowClosing(e);
                byte buf11[] = (user.getName() + " 下线了 " + dfs.format(new Date())).getBytes();
                System.out.println("这是做实验用的检验是否发出下线" + new String(buf11));
                System.out.println(user + "下线了");
                DatagramPacket packet = new DatagramPacket(buf11, buf11.length, talkRoomGroup, 5678);
                try {
                    receiver.send(packet);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

    }

    /**
     * 创建实现Runnable接口的内部类，用于开辟新的线程来接收来自组播组的消息
     */
    private class UDPMonitor implements Runnable {
        boolean stop = true;

        @Override
        public void run() {
            while (stop) {
                DatagramPacket tempPacket;
                byte[] tempBuf = new byte[1024];
                tempPacket = new DatagramPacket(tempBuf, tempBuf.length);
                try {
                    receiver.receive(tempPacket);
                    String[] cmd = new String(tempBuf).split(" ");
                    if (cmd[0].equals("Collection")) {
//                        System.out.println("这里是检验是否能够收到成员列表" + new String(tempBuf));
                        DefaultListModel<String> model = new DefaultListModel<>();
                        model.addElement("在线好友列表");
                        for (int i = 1; i < cmd.length; i++) {
                            model.addElement(cmd[i]);
                        }
                        talkRoomUI.getMembersList().setModel(model);
                    } else {
                        talkRoomUI.getMsgPrintArea().append(new String(tempBuf) + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}///:~
