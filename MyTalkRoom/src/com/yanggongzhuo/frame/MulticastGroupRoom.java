package com.yanggongzhuo.frame;

import javax.swing.*;
import java.awt.*;

/**
 * 多人聊天室對話框UI
 * @date 2020-05-05 13:12:45
 * @author ygz
 */
public class MulticastGroupRoom extends JFrame{

    /**对话框的小组件，按钮等*/

    /**    多人聊天室在线好友列表框及滚动面板*/
    private JList<String> membersList = null;
    private JScrollPane memberListPane = null;
    /**    聊天室聊天信息展示文本域框及滚动面板*/
    private JTextArea msgPrintArea = null;
    private JScrollPane msgPrintPane = null;
    private JTextArea msgSendArea = null;
    /**    发送文本域框及滚动面板*/
    private JScrollPane msgSendPane = null;
    private JButton send2AllMembers = null;

    /**对话框分为上下两个面板*/
    private JPanel northPanel = null;
    private JPanel southPanel = null;
    private Container container = null;

    public MulticastGroupRoom(String title) {
        super(title);
        container = getContentPane();
        setLayout(new BorderLayout());

        msgPrintArea = new JTextArea(10,40);
        msgPrintArea.setDisabledTextColor(Color.BLACK);
        msgPrintArea.setEnabled(false);
//        msgPrintArea.setText("你好呀\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n我是");
        msgPrintPane = new JScrollPane(msgPrintArea);
        msgSendArea = new JTextArea(3,42);
        msgSendPane =new JScrollPane(msgSendArea);
        send2AllMembers = new JButton("发送");
        membersList = new JList<String>();
        memberListPane = new JScrollPane(membersList);
        DefaultListModel<String> model = new DefaultListModel<>();
//        String s[] = {"在线好友列表","小红","小明","小刚","小雅","晓晓","小磊","小狗","小辉","小李","小威","登哥","里皮"};
        String s[] = {"在綫好友列表"};
        for(String d:s){
            model.addElement(d);
        }
        membersList.setModel(model);
        northPanel = new JPanel(new FlowLayout());
        southPanel = new JPanel(new FlowLayout());
        southPanel.setPreferredSize(new Dimension(500,60));
        northPanel.add(msgPrintPane);
        northPanel.add(memberListPane);
        southPanel.add(msgSendPane);
        southPanel.add(send2AllMembers);
        container.add(northPanel,BorderLayout.NORTH);
        container.add(southPanel,BorderLayout.SOUTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);            //    注意，这里的默认关闭方式和JFrame不一样   DO_NOTHING_ON_CLOSE, HIDE_ON_CLOSE, or DISPOSE_ON_CLOSE
        setBounds(500,160,530,280);
        setVisible(true);
    }

    public JScrollPane getMsgSendPane() {
        return msgSendPane;
    }

    public JButton getSend2AllMembers() {
        return send2AllMembers;
    }

    public JList<String> getMembersList() {
        return membersList;
    }

    public JScrollPane getMsgPrintPane() {
        return msgPrintPane;
    }

    public JTextArea getMsgPrintArea() {
        return msgPrintArea;
    }

    public JTextArea getMsgSendArea() {
        return msgSendArea;
    }

    public static void main(String[] args) {
        MulticastGroupRoom mu = new MulticastGroupRoom("多人聊天室");
    }
}
