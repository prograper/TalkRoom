//:
package com.yanggongzhuo.demo;

import com.yanggongzhuo.function.Login;
import com.yanggongzhuo.function.User;

/**
 * 客户端demo0
 * @author ygz
 */
public class DemoClient {
    public static void main(String[] args) {
        //    新建用户
        User user = new User();
        //    给登录设置用户载体
        Login login = new Login(user);
        //    运行登录界面
        login.play();
    }
}///:~
