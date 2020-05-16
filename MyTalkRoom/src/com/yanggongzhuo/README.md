#单、多人聊天室使用说明
##1. 服务器
所在包 com.yanggongzhuo.function.Server
###简介
1. 服务器是由两种协议的套接字完成的：
1. 好友之间的私人聊天业务是用TCP协议服务器依靠ServerSocket类处理的。
3. 多人聊天室服务器则是用UDP协议中的多播数据报套接字MulticastSocket来模拟的。
###功能介绍
* TCP和UDP服务器端都能够实时显示当前在线用户
* TCP服务器可以获取用户的IP和端口等基本信息，并获取用户上/下线时间
* UDP服务器可以向聊天室用户发送管理员公告，可以查看聊天室内的实时聊天动态
##2. 客户端
所在包 com.yanggongzhuo.function.Client
###简介
1. 客户端包含了单人聊天和多人聊天两种模式：
2. 单人聊天的实现使用了TCP协议的套接字Socket类
3. 多人聊天是使用UDP协议的多播数据报套接字MulticastSocket所实现
###功能介绍
* 客户端的下拉框可以实时的显示当前服务器所在线的成员
* 用户可以选取所在线用户进行单人聊天
* 本用户可以接收来自服务器其他在线好友的消息
* 可以点击进入多人聊天室进行多人聊天
##3.多人聊天室
所在包 com.yanggongzhuo.function.MulticastTalkRoom
###简介
1. 使用了多播数据报套接字
###功能介绍
* 可以实时的显示聊天室成员信息
* 有上/下线信息提示
* 可以向聊天室发送信息
##4.登录
所在包 com.yanggongzhuo.function.Login
###简介
1. 后台连接到Server，检查所登录用户是否信息合法，从而向服务器进行发送访问或退出
2. 使用User类实例对象作为登录用户的载体
###功能介绍
* 可以判断用户的昵称是否已经被正在线上的用户所使用
* 可以判断用户的端口（大于1024）和IP地址是否和正在线上的用户冲突
* 登录成功时，实例化Client类对象
##5.程序所用GUI
所在包 com.yanggongzhuo.frame.*
###介绍
* 客户端类：ClientUI
* 服务器类：ServerUI
* 多人聊天室类：MulticastGroupRoom
* 登录：SignInUI
##6.使用方法
####打开包 com.yanggongzhuo.demo下的DemoServer，然后打开DemoClient的任意一个即可
----------------------------------------
>@author ygz
#####[github主页]()