package com.yanggongzhuo.frame;

import javax.swing.*;
import java.awt.*;

/**
 * 登录之前用的UI
 * @date 2020-05-05 23:26:13
 * @author ygz
 */
public class SignInUI extends JFrame {
    private JButton signIn = null;
    private JLabel userNameLabel = null;
    private JTextField userNameBox = null;
    private JTextField portBox = null;
    private JLabel portLabel = null;

    private JPanel northPanel = null;
    private JPanel southPanel = null;
    private Container container = null;

    public JTextField getPortBox() {
        return portBox;
    }

    public JTextField getUserNameBox() {
        return userNameBox;
    }

    public JButton getSignIn() {
        return signIn;
    }

    public JPanel getNorthPanel() {
        return northPanel;
    }

    public JPanel getSouthPanel() {
        return southPanel;
    }

    public JLabel getPortLabel() {
        return portLabel;
    }

    public JLabel getUserNameLabel() {
        return userNameLabel;
    }

    public Container getContainer() {
        return container;
    }

    public SignInUI(String title){
        super(title);
        container = getContentPane();
        setLayout(new BorderLayout());

        signIn = new JButton("登录");
        userNameBox = new JTextField(15);
        userNameBox.setText("昵称仅本次有效");
        portBox = new JTextField(6);
        userNameLabel = new JLabel("设置昵称");
        portLabel = new JLabel("选择服务器端口");

        northPanel = new JPanel(new FlowLayout());
        southPanel = new JPanel(new FlowLayout());
        northPanel.add(portLabel);
        northPanel.add(portBox);
        southPanel.add(userNameLabel);
        southPanel.add(userNameBox);
        southPanel.add(signIn);
        container.add(northPanel,BorderLayout.NORTH);
        container.add(southPanel,BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(500,180,300,100);
        setVisible(true);
    }

    public SignInUI(){
        this("");
    }

    public static void main(String[] args) {
        SignInUI sign = new SignInUI("登录界面");
    }
}
