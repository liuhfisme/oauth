# Spring Security OAuth 实现第三方授权快捷登录
## 一、GitHub 快捷登录
> 在GitHub官网上注册一个新的[OAuth应用](https://github.com/settings/applications/new)

### 1、注册

- Application name：应用名称，必填项
- Homepage URL：应用程序主页的完整URL（http://localhost:8080）
- Application description：应用描述，选填
- Authorization callback URL：认证的重定向地址，必填项（http://localhost:8080/login/oauth2/code/github）

​	当用户通过用户代理（浏览器）成功登录GitHub，并且用户在批准页（Approva Page）授权允许注册的客户端应用访问自己的用户数据后，GitHub会将授权码（Code）通过重定向的方式传递给客户端应用。

​	Spring Security OAuth 默认的重定向模板是{baseUrl}/login/oauth2/code/{registrationId},registrationId 是ClientRegistration的唯一ID，通常以接入的OAuth服务提供商的简称来命名即可，所以此处设置为 github。

### 2、配置application.yml

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          github:
            client-name: demo1
            client-id: db392611c29b1a4b87d2
            client-secret: 98021e50d849cde6a7a15826f23a99046e0ba725
```

说明：

（1）spring.security.oauth2.client.registration是OAuth客户端所有属性的基础前缀。

（2）registration下面的github是ClientRegistration的唯一ID。

## 二、QQ 快捷登录

​	前面使用最简配置实现了OAuth客户端接入GitHub登录的功能，得益于Spring Security对OAuth标 准认证流程的封装，最简配置客户端也可以很方便地接入Google和Facebook等实现相对标准的OAuth服 务提供商。而对于QQ登录等OAuth流程来说，则可以在此基础上进行额外的适配工作，Spring Security 良好的OAuth扩展性同样为适配提供了足够的支持。

​	不同OAuth服务提供商提供的授权流程在细节上略有不同，主要体现在与授权服务器交互过程中的传参、返回值解析，以及从资源服务器获取资源等几个方面。核心步骤大体相同，都是OAuth标准制定的形式。

- 获取code
- 使用code交换access_token
- 携带access_token请求被保护的用户信息和其它资源

