package com.yanggongzhuo.frame;

import javax.swing.*;
import java.awt.*;

/**
 *服务器的UI
 * @date 2020-05-05 00:53:13
 * @author ygz
 */
public class ServerUI extends JFrame{
    /*
    小的文本组件，输入框等
     */
    private JButton send2AllMember = null;                                  //    组播组服务器发送给全体成员按钮
    private JScrollPane tcpServerPane = null;                           //    TCP协议服务器的滚动面板及文本域
    private JTextArea tcpServerArea = null;
    private JScrollPane udpServerPane = null;                    //    UDP协议的组播组服务器的滚动面板及文本域
    private JTextArea udpServerArea = null;
    private JTextArea tcpOnlineMembersArea = null;                 //    TCP协议服务器在线人员显示列表文本域
    private JTextArea udpOnlineMembersArea = null;
    private JTextArea udpSend2AllMembersArea = null;               //    UDP协议组播组服务器文本框
    private JScrollPane udpSend2AllMembersPane = null;
    private JLabel tcpPortLabel = null;                            //    TCP协议服务器端口标签及输入框
    private JTextField tcpPortField = null;
    private JLabel udpPortLabel = null;                            //    UDP协议服务器端口标签及输入框
    private JTextField udpPortField = null;
    private Container container = null;                        //设置窗体组件的容器

    /*
    大的文本组件，窗体的面板等
     */
    private JPanel northPanel = null;                      //    窗体整体分为上下两个一级面板
    private JPanel southPanel = null;
    private JPanel secondLevelPanel1 = null;            //    上下两个一级面板又细分为5个二级面板，上部分2，下部分3个
    private JPanel secondLevelPanel2 = null;
    private JPanel secondLevelPanel3 = null;
    private JPanel secondLevelPanel4 = null;
    private JPanel secondLevelPanel5 = null;

    public ServerUI(String title){//实例化窗体
        super(title);
        container = getContentPane();
        setLayout(new BorderLayout());

        /**两个一级面板构造**/
        northPanel = new JPanel(new BorderLayout());
        northPanel.setPreferredSize(new Dimension(500,180));
        southPanel = new JPanel(new BorderLayout());
        southPanel.setBorder(BorderFactory.createTitledBorder("UDP组播组服务器"));
        northPanel.setBorder(BorderFactory.createTitledBorder("TCP服务器"));
        container.add(northPanel,BorderLayout.NORTH);
        container.add(southPanel,BorderLayout.CENTER);

        /**五个二级面板构造**/
        secondLevelPanel1 = new JPanel(new FlowLayout());
        secondLevelPanel1.setPreferredSize(new Dimension(500,30));
        secondLevelPanel2 = new JPanel(new BorderLayout()/*GridLayout(1,2)*/);
        secondLevelPanel2.setPreferredSize(new Dimension(500,130));
        secondLevelPanel3 = new JPanel(new FlowLayout());
        secondLevelPanel4 = new JPanel(new /*GridLayout(1,2)*/BorderLayout());
        secondLevelPanel5 = new JPanel(new FlowLayout());

        /**TCP服务器的零部件构造**/
        tcpPortLabel = new JLabel("TCP服务器端口");
        tcpPortField = new JTextField(6);
        tcpPortField.setText("5678");                              //端口号
        tcpPortField.setEnabled(false);                          //数字不可被设置
        tcpServerArea = new JTextArea(1,1);
        tcpServerArea.setEnabled(false);                    //    设置文本域不能被编辑
        tcpServerArea.setBackground(Color.DARK_GRAY);         //    文本域背景颜色
        tcpServerArea.setDisabledTextColor(Color.YELLOW);   //    文本域字体颜色
        tcpServerPane = new JScrollPane(tcpServerArea);
        tcpOnlineMembersArea = new JTextArea(10,15);
        tcpOnlineMembersArea.setEnabled(false);               //    设置窗体右上角在线ip地址文本域框背景颜色，字体，不可被编辑
        tcpOnlineMembersArea.setBackground(Color.DARK_GRAY);
        tcpOnlineMembersArea.setDisabledTextColor(Color.YELLOW);
//        tcpOnlineMembersArea.setText("你好");

        /**UDP协议服务器零部件构造**/
        udpPortLabel = new JLabel("组播组端口");
        udpPortField = new JTextField(6);
        udpPortField.setEnabled(false);
        udpPortField.setText("5678");
        udpServerArea = new JTextArea(10,10);
        udpServerArea.setBackground(Color.DARK_GRAY);              //    设置组播组群聊信息文本域框背景颜色，字体颜色，不可被编辑
        udpServerArea.setEnabled(false);
        udpServerArea.setDisabledTextColor(Color.YELLOW);
        udpServerPane = new JScrollPane(udpServerArea);
        udpOnlineMembersArea = new JTextArea(10,15);
        udpOnlineMembersArea.setDisabledTextColor(Color.YELLOW);
        udpOnlineMembersArea.setEnabled(false);
        udpOnlineMembersArea.setBackground(Color.DARK_GRAY);
//        udpOnlineMembersArea.setText("你好呀");
        udpSend2AllMembersArea = new JTextArea(3,40);
        udpSend2AllMembersArea.setText("发送至组播组全体人员");
        udpSend2AllMembersPane = new JScrollPane(udpSend2AllMembersArea);
        send2AllMember = new JButton("发送");

        /**给五个二级面板进行add组件**/
        secondLevelPanel1.add(tcpPortLabel);
        secondLevelPanel1.add(tcpPortField);
        secondLevelPanel2.add(tcpServerPane,BorderLayout.CENTER);
        secondLevelPanel2.add(tcpOnlineMembersArea,BorderLayout.EAST);
        secondLevelPanel3.add(udpPortLabel);
        secondLevelPanel3.add(udpPortField);
        secondLevelPanel4.add(udpServerPane,BorderLayout.CENTER);
        secondLevelPanel4.add(udpOnlineMembersArea,BorderLayout.EAST);
        secondLevelPanel5.add(udpSend2AllMembersPane);
        secondLevelPanel5.add(send2AllMember);

        /**给两个一级面板进行add五个二级面板**/
        northPanel.add(secondLevelPanel1,BorderLayout.NORTH);
        northPanel.add(secondLevelPanel2,BorderLayout.SOUTH);
        southPanel.add(secondLevelPanel3,BorderLayout.NORTH);
        southPanel.add(secondLevelPanel4,BorderLayout.CENTER);
        southPanel.add(secondLevelPanel5,BorderLayout.SOUTH);

        setBounds(500,160,500,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public ServerUI(){       //    无参构造器
        this("");
    }
    public JTextArea getTcpServerArea(){    //    获得左上侧TCP服务器显示用户登录/退出信息文本域
        return this.tcpServerArea;
    }

    public JTextArea getUdpServerArea(){    //    获得中间左侧UDP服务器用户聊天室信息文本域
        return this.udpServerArea;
    }

    public JTextArea getTcpOnlineMembersArea(){    //    获得右上TCP服务器用户地址信息文本域
        return this.tcpOnlineMembersArea;
    }

    public JTextArea getUdpOnlineMembersArea(){    //    获得中间右侧UDP服务器用户信息文本域
        return this.udpOnlineMembersArea;
    }

    public JTextArea getUdpSend2AllMembersArea(){    //    获得左下侧UDP协议服务器发送全体消息文本域
        return this.udpSend2AllMembersArea;
    }

    public JButton getSendAllMember(){    //    获得右下侧UDP服务器发送全体消息按钮
        return this.send2AllMember;
    }

    public static void main(String[] args) {
        ServerUI server = new ServerUI("TalkRoomServer");
//        server.getTcpOnlineMembersArea().setText("我是");
    }
}
