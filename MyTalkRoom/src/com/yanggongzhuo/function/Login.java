//:
package com.yanggongzhuo.function;

import com.yanggongzhuo.frame.SignInUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;

/**
 * 将客户端和登录集成在一起，实现了完整的客户端功能
 * @date 2020-05-12 16:38:22
 * @author ygz
 */
public class Login {
    private SignInUI ui4Login = null;
    private User user = null;

    /**
     * 登录的构造方法
     * @param user 登录客户端所用的用户载体
     */
    public Login(User user) {
        ui4Login = new SignInUI("登录");
        this.user = user;
    }

    /**
     * 运行几个主要的方法来使登录运行
     */
    public void play() {
        setActionListener4TextField(this.ui4Login.getPortBox(), "输入框为空", "");
        setActionListener4TextField(this.ui4Login.getUserNameBox(), "昵称仅本次有效", "昵称仅本次有效");
        setActionListener4JButton(this.ui4Login);
    }

    /**
     *为昵称输入框设置焦点事件监听器，用来增加用户体验，输入框默认文本等
     * @param ui4Login 用来设置默认文本的昵称输入框
     * @param defaultText 默认文本
     * @param display 将要显示的文本
     */
    public void setActionListener4TextField(JTextField ui4Login, String defaultText, String display) {
        ui4Login.addFocusListener(new FocusAdapter() {
            /**
             * 设置焦点监听，如果输入框内为默认文本，当焦点集中在输入框时则清除输入框
             * @param e 无解释
             */
            @Override
            public void focusGained(FocusEvent e) {
//                super.focusGained(e);
                String text;
                JTextField nameBox = (JTextField) e.getSource();
                text = nameBox.getText();
                if (text.equals(defaultText) || "输入框为空".equals(text)) {
                    nameBox.setText("");
                    nameBox.setForeground(Color.BLACK);
                }
//                System.out.println(text);
            }

            /**
             * 如果输入框为空，则输入框显示默认文本
             * @param e 无解释
             */
            @Override
            public void focusLost(FocusEvent e) {
                String text;
                JTextField nameBox = (JTextField) e.getSource();
                text = nameBox.getText();
                if (text.equals("")) {
                    nameBox.setText(display);
                    nameBox.setForeground(Color.GRAY);
                }
            }
        });
    }

    /**
     *给SignUI类对象的按钮添加动作监听，用来实现登录功能
     * @param ui 将要进行添加动作监听的SignUI类对象
     * @see com.yanggongzhuo.frame.SignInUI
     */
    public void setActionListener4JButton(SignInUI ui) {
        SignInUI tempUI = ui;
        tempUI.getSignIn().addActionListener(new ActionListener() {

            /**
             * 增加了输入框为空，端口数字不合法（小于1024）以及昵称冲突和ip端口冲突的容错
             * @param e 无解释
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField nameBox;
                JTextField portBox;
                nameBox = tempUI.getUserNameBox();
                portBox = tempUI.getPortBox();
                if (portBox.getText().equals("")) {
                    portBox.setText("输入框为空");
                } else if (nameBox.getText().equals("")) {
                    nameBox.setText("输入框为空");
                } else if ("昵称仅本次有效".equals(nameBox.getText()) && !portBox.getText().equals("")) {
                    nameBox.setText("输入框为空");
                } else if (!isNumeric(portBox.getText()) || (Integer.parseInt(portBox.getText()) <= 1024)){
                    portBox.setText("数字不合法");
                }else {
                    user.setName(nameBox.getText());
                    user.setServerPort(Integer.parseInt(portBox.getText()));
//                    user.setAddress(new InetSocketAddress("127.0.0.1",Integer.parseInt(portBox.getText())));
//                    getUi4Login().hide();
                    Client client = new Client(user);
                    client.connect2Server();
                    System.out.println("这是小方法里的输出" + user);
                }
            }
        });
    }

    //    判断一个字符串是否能够转变为Integer类型

    /**
     *判断此字符串是否能够转换成一个数字类型
     * @param str 将要进行判断的字符串
     * @return 如果能够转换成数字则返回true，否则返回false
     * @see java.math.BigDecimal
     */
    public  boolean isNumeric(String str) {
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }
        return true;
    }

    public static void main(String[] args) {
        User user = new User();
        Login l = new Login(user);
        l.play();
    }
}///:~
