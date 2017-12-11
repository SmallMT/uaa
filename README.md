<!-- TOC -->

- [1. 如何运行该项目](#1-%E5%A6%82%E4%BD%95%E8%BF%90%E8%A1%8C%E8%AF%A5%E9%A1%B9%E7%9B%AE%08)
    - [1.1. UAA](#11-uaa)
    - [1.2. OAuthRS](#12-oauthrs)
    - [1.3. OAuthClientSample](#13-oauthclientsample)
- [2. 数据库表结构](#2-%E6%95%B0%E6%8D%AE%E5%BA%93%E8%A1%A8%E7%BB%93%E6%9E%84)
    - [2.1. client_details(OAuth Client信息表)](#21-clientdetailsoauth-client%E4%BF%A1%E6%81%AF%E8%A1%A8)
    - [2.2. jhi_user(用户表)](#22-jhiuser%E7%94%A8%E6%88%B7%E8%A1%A8)
    - [2.3. jhi_authority(权限表)](#23-jhiauthority%E6%9D%83%E9%99%90%E8%A1%A8)
    - [2.4. jhi_user_authority(用户权限多对多中间表)](#24-jhiuserauthority%E7%94%A8%E6%88%B7%E6%9D%83%E9%99%90%E5%A4%9A%E5%AF%B9%E5%A4%9A%E4%B8%AD%E9%97%B4%E8%A1%A8)
    - [2.5. real_name(实名认证表)](#25-realname%E5%AE%9E%E5%90%8D%E8%AE%A4%E8%AF%81%E8%A1%A8)
    - [2.6. bind_enterprise(绑定企业)](#26-bindenterprise%E7%BB%91%E5%AE%9A%E4%BC%81%E4%B8%9A)
    - [2.7. bind_agent(绑定代办人)](#27-%08bindagent%E7%BB%91%E5%AE%9A%E4%BB%A3%E5%8A%9E%E4%BA%BA)

<!-- /TOC -->
# 1. 如何运行该项目

## 1.1. UAA

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

## 1.2. OAuthRS
> 资源服务器

## 1.3. OAuthClientSample
> Spring Security OAuth 2.0提供的客户端访问OAuth2.0 Server的实现


# 2. 数据库表结构

## 2.1. client_details(OAuth Client信息表)

| 字段 | 说明 |备注 |
| --- | --- | -- |
|id |id|主键、自增|
|app_id|客端户id|
| app_secret|客户端密码|
|access_token_validity|access_token有效期（ms）|
|refresh_token_validity|refresh_token有效期（ms）|
|authorities|客户端权限|多个权限使用逗号分开|
|grant_types|授权类型|多种类型使用逗号分开|
|redirect_url|回调地址|
|scope|请求域|
|resource_ids|资源id|
|auth_approve_scopes|自动授权请求域|

## 2.2. jhi_user(用户表)

|字段|说明|备注|
| --- | -- | -- |
|id|编号|主键自增|
|login|登录用户名|unique|
|password_hash|密码hash|
|email|邮箱|unique|
|image_url|头像地址链接|废弃|
|activated|是否已激活|
|lang_key|语言|
|activation_key|激活key|
|reset_key|密码重置key|
|created_by|创建者|
|create_data|创建时间|
|reset_date|重置时间|
|last_modified_by|最后修改者|
|last_modified_date|最后修改日期|
|tel|电话号码|
|verfied|是否已实名认证|
|wechat|微信 unique id|
|identity|身份证号码|
|name|真实姓名|
|enterprise_name|企业名称|作废|
|credit_code|同一信用代码|作废|
|is_legal_person|是否是法人|作废|

## 2.3. jhi_authority(权限表)

|字段|说明|备注|
| --- | -- | -- |
|name|权限名称|主键|

## 2.4. jhi_user_authority(用户权限多对多中间表)

|字段|说明|备注|
| --- | -- | -- |
|user_id|user表的主键|主键|
|authority_name|authority表的主键|主键|

## 2.5. real_name(实名认证表)

|字段|说明|备注|
| --- | -- | -- |
|id|编号|主键
|login|登录用户名|
|name|实名认证姓名|
|tel|绑定的手机号码|
|front_image|身份证正面照片地址|
|back_iamge|身份证反面照片地址|
|sefile_iamge|手持身份证照片地址|
|identity|身份证号码|
|state|状态|

## 2.6. bind_enterprise(绑定企业)

|字段|说明|备注|
| --- | -- | -- |
|ID|编号|主键、自增|
|enterprise_name|企业名称|
|credit_code|统一社会信用代码|
|user_id|user表主键|外键|

## 2.7. bind_agent(绑定代办人)

|字段|说明|备注|
| --- | -- | -- |
|ID|编号|主键、自增
|bind_enterprise_id|bind_enterprise表主键|外键
|user_id|user表主键|外键|
