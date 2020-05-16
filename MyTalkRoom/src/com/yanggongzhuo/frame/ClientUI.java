//:
package com.yanggongzhuo.frame;

import javax.swing.*;
import java.awt.*;

/**
 * 客戶端UI
 * @date 2020-05-05 11:34:56
 * @author ygz
 */
public class ClientUI extends JFrame {

    /**客户端的一些小组件，按钮等*/

    /**    文本输入框中发送信息按钮*/
    private JButton send2Member = null;
    /**    当前在线用户下拉表*/
    private JComboBox membersList = null;
    /**    聊天信息文本域和滚动面板*/
    private JTextArea msgPrintArea = null;
    private JScrollPane msgPrintPane = null;
    /**    聊天编辑区文本域和滚动面板*/
    private JTextArea msgSendArea = null;
    private JScrollPane msgSendPane = null;
    /**    下拉表提示标签*/
    private JLabel membersListLabel = null;
    /**    进入多人聊天室按钮*/
    private JButton enterMulticastGroup = null;

    /**客户端UI的大体组件、面板等*/

    /**    上中下三个一级面板*/
    private JPanel northPanel = null;
    private JPanel centerPanel = null;
    private JPanel southPanel = null;
    private Container container = null;

    public JLabel getMembersListLabel() {
        return membersListLabel;
    }

    public JButton getEnterMulticastGroup() {
        return enterMulticastGroup;
    }

    public JButton getSend2Member() {
        return send2Member;
    }

    public JComboBox getMembersList() {
        return membersList;
    }

    public JTextArea getMsgPrintBox() {
        return msgPrintArea;
    }

    public JTextArea getMsgSendBox() {
        return msgSendArea;
    }

    public JScrollPane getMsgSendPane() {
        return msgSendPane;
    }

    public JScrollPane getMwgPrintPane() {
        return msgPrintPane;
    }

    public ClientUI(String title) {
        super(title);
        container = this.getContentPane();
        setLayout(new BorderLayout());
        send2Member = new JButton("发送");
        membersList = new JComboBox();
        membersList.addItem("请选择---");
        membersListLabel = new JLabel("选择在线好友");
        msgPrintArea = new JTextArea(10,30);
        msgPrintPane = new JScrollPane(msgPrintArea);
        msgPrintArea.setEnabled(false);
        msgPrintArea.setDisabledTextColor(Color.black);
        enterMulticastGroup = new JButton("进入多人聊天室");
        msgSendArea = new JTextArea(3,30);
        msgSendPane = new JScrollPane(msgSendArea);
        send2Member = new JButton("发送");

        northPanel = new JPanel(new FlowLayout());
        northPanel.setPreferredSize(new Dimension(400,30));
        centerPanel = new JPanel(new FlowLayout());
        southPanel = new JPanel(new FlowLayout());
        northPanel.add(membersListLabel);
        northPanel.add(membersList);
        northPanel.add(enterMulticastGroup);
        centerPanel.add(msgPrintPane);
        container.add(northPanel,BorderLayout.NORTH);
        container.add(centerPanel,BorderLayout.CENTER);
        container.add(southPanel,BorderLayout.SOUTH);
        southPanel.add(msgSendPane);
        southPanel.add(send2Member);
        setBounds(500,140,400,300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public ClientUI(){
        this("");
    }      //    无参构造

    public static void main(String[] args) {
        ClientUI ui = new ClientUI("TalkRoomClient");
    }
}///:~
