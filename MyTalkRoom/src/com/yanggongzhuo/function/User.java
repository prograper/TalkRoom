package com.yanggongzhuo.function;

import java.net.*;

/**
 * 登录用户类
 * @date 2020-05-06 19:29:36
 * @author ygz
 */
public class User {
    private String name = null;
    private InetSocketAddress address = null;
    private int serverPort;

    public User(){
        this.address = new InetSocketAddress("127.0.0.1",2333);
        this.name = "沉默王二";
    }

    public User(String name){
        this.name = name;
    }

    public void setServerPort(int port){
        serverPort = port;
    }

    public String getName() {
        return name;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return this.address.getHostName() + " " + this.address.getPort() + " " + this.name;
    }
    public static void main(String[] args) {
            InetSocketAddress address = new InetSocketAddress("192.168.43.1",8888);
            User user = new User("小红");
            System.out.println(user);
    }
}
