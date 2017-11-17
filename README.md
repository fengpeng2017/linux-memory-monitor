# Linux集群内存球监控系统

### 页面效果

![Image text](http://image.jthinking.com/image/github-images/linux-memory-monitor/20171117130721.png)

![Image text](http://image.jthinking.com/image/github-images/linux-memory-monitor/20171117130004.png)

### 1. 需要解决的问题

a) 由于Linux服务器通常是没有图形界面的，导致很难直观的了解服务器当前的内存占用率。

### 2.想法

a) 我们通常都很了解自己电脑的内存使用情况，原因是在Windows系统上有很多软件会在桌面显示一个加速球，这个加速球可以非常直观的显示当前内存的使用情况。所以我想可不可以模拟Windows系统上常见的加速球，也给Linux服务器配置上一个类似的内存球来显示内存的使用情况呢？

### 3.设计

a) 首先清楚的是，我们不可能在Linux服务器上安上图形界面，然后安上一个软件来显示内存使用率，这样太耗费服务器宝贵的内存。而且我们一般用远程连接的方式登录Linux，这个图形界面也很难看到。

b) 所以采用另一种方式，通过Web应用来显示内存球。我们打开一个网页，就可以实时查看Linux服务器的内存使用率，这样是最方便的。

### 4.实现

a) 基于这样的思路，实现这样一个功能感觉是非常简单的，我们只要部署一个Web应用，然后在Web应用中获取系统内存，然后展示在页面就可以了，不过我们有更多的考虑，比如，我们有多台服务器，现在的系统逐渐都升级成了分布式的系统，那我们就得在每台服务器上都得部署一个相同的项目，而且这样不便于对分布式系统的集中监控管理，所以我决定使用Zookeeper，一个分布式系统管理框架，来存储我们每台服务器的内存使用情况，然后我们会实时更新Zookeeper中的数据，然后我们在一个项目中获取所有服务器数据，实时展示到页面。

### 5.实时性的考虑

a) 在内存使用率计算阶段。由于是内存使用率的监控，所以实时性是很重要的。首先，在内存使用率计算阶段，这个实时性我们通过降低计算的时间间隔来提高实时性，不过这样会消耗更多的资源，不过结合实际考虑，Linux服务器的内存变动并不是那么频繁，所以我们根据实际情况规定一个时间间隔，5秒或者是10秒就已经很准确了。

b) 内存使用率获取阶段。使用Zookeeper还有一个好处就是，我们可以借助它的事件监听机制，当各个服务器上的监控程序向Zookeeper更新数据后，我们的内存球展示程序已经监听在Zookeeper数据节点的监听器会立即响应。

c) 内存使用率展示阶段。当展示程序获知数据变动后，我们使用WebSocket实时将后台数据传到前台，让管理员最快的发现内存异常情况。

### 6.框架

a) Zookeeper

b) WebSocket

c) Spring MVC

### 7.代码

a) 安装Zookeeper。

b) 写监控程序。

c) 将监控程序运行到需要监控的服务器上。

d) 通过Zookeeper命令行查看数据。

e) 写内存球展示程序。

### 8.源码运行

#### 展示程序：monitor-web

a) 运行源码需要jdk和Maven，以及Zookeeper服务器

b) 修改配置。monitor-web是Web展示程序，做了简单的动态密码登录。

    i. 到com.jthinking.monitor.util.JavaMail中修改smtp邮件服务器的各项参数。
    
    ii. resources下的applicationVariable.properties中修改WebSocket地址，本地运行把端口换成自己的就可以。
    
    iii. resources下的users.properties中添加一个登录用户，填自己的邮箱和名字就可以。
    
    iv. 到com.jthinking.monitor.service.ServerMonitor中修改Zookeeper的连接信息，以及WebSocket地址，本地的话也是只改一下端口。pom.xml中已经配置了Tomcat7插件，可以直接用Maven 命令tomcat7:run来运行，端口也在pom.xml中配置了，默认8087。
    
#### 监控程序：monitor-sentinel

a) 	monitor-sentinel是一个普通的maven quickstart项目，只需要所依赖jar包，然后		在com.jthinking.monitor.MemoryListener中修改Zookeeper服务器地址即可运行。
