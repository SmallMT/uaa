# UAA
    本应用提供用户三亚市政务中心用户账号和授权（User Account and Authentication,简称UAA）的服务.
## 如何运行该项目

1. clone该项目`git clone https://gitee.com/liuyatao1992/SY-UAA.git`
2. 数据库配置信息位于`./src/main/resources/config/application-dev.yml`中的`spring.datasource`中
3. 实名认证文件保存位置位于`./src/main/resources/config/application-dev.yml`中的`realName.filePath`中,没有的话请创建文件路径
4. 邮件发送的base-url位于`./src/main/resources/config/application-dev.yml`中的`spring.mail.base-url`中,请根据实际地址配置
5. 登录错误跳转地址配置信息位于`./src/main/java/com/easted/sy/user/archieves/uaa/security/LoginAuthenticationFailureHandler.java`中，请根据实际情况修改
6. 安装相关maven依赖，在项目所在文件夹下执行`mvn clean install`
7. 微信开发相关设置：
 * 由于本项目微信开发使用到的是[weixin-java-tools](https://github.com/wechat-group/weixin-java-tools)项目，因为涉及加密工具，所以请根据[相关说明](https://github.com/Wechat-Group/weixin-java-tools/wiki/%E5%8A%A0%E8%A7%A3%E5%AF%86%E7%9A%84%E5%BC%82%E5%B8%B8%E5%A4%84%E7%90%86%E5%8A%9E%E6%B3%95)做相应处理。
 * 用于微信开发的配置必须使用`80`端口，Mac下非root用户不能使用1000以内的端口号，所有需要配置端口的转发，本项目是将`8080`转发到`80`端口的。
 * 测试中生成域名工具：ngrok。直接运行`ngrok http 8080`
 * 注册微信开发测试账号，并将配置信息填写在`./src/main/resources/config/application-dev.yml`中的`wechat.mp`中
8. 运行项目：直接运行`UaaApp`中的main函数，或者使用spring boot的maven插件`mvn spring-boot:run`


