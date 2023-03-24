# project_springboot_security
SpringBoot中Security相关Demo。





# 1、JWT

## 1.1、

**JWT由三部分组成，中间使用句点"."连接，即 header.payload.signature，这三个部分都是由base64编码的，这么做的目的是为了保证url中安全的传输。**

第一部分header中是由两部分信息组成，即声明类型jwt和声明加密的算法（例如SHA256），所以说header在base64URL编码之前是如下的JSON。

```
{
    'typ':'jwt',
    'alg':'SHA256'
}
```

base64URL编码之后为：

```
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9
```

第二部分payload是存放想要传递的信息，例如经常用到的用户ID和过时时间exp，所以说payload在base64URL编码之前是如下的JSON

```json
{
	'id':'10',
	'exp':'2301597433'
}
复制代码
```

base64URL编码之后为：

```makefile
ewoJJ2lkJzonMTAnLAoJJ2V4cCc6JzIzMDE1OTc0MzMnCn0=
复制代码
```

第三部分signature最是关键，它的组成原理是：1、将header和payload分别base64URL编码之后组合到一起（通过"."连接）；2、添加一个只有服务器知道的签名字符串；3、再使用header中的签名算法SHA256加密步骤1、2。可以看成下面的公式：

```ini
signature = SHA256(base64encode(header) + '.' + base64encode(payload), 'SEVER_SECRET_KEY')
复制代码
```

最终可以得到签名信息为

```
05dd35b4d20c95430cd1b63406f861de7e4c1476f9dbffa25f30fe08baf8f530
复制代码
```

为啥第三部分是关键呢？因为第三部分只能由服务器生成，而只能由服务器生成的根本原因就是没有人知道签名字符串--SEVER_SECRET_KEY，如果有人只篡改了第一和第二部分，服务器能够正常解析里面的内容，但是作为验证的第三部分显然是不匹配的；如果有人篡改了所有部分，服务器是没法解析第三部分的，因为SEVER_SECRET_KEY一定不一样。

以上三个部分组合在一起就构成了完成的JWT了，如下所示：

```ini
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.ewoJJ2lkJzonMTAnLAoJJ2V4cCc6JzIzMDE1OTc0MzMnCn0=.05dd35b4d20c95430cd1b63406f861de7e4c1476f9dbffa25f30fe08baf8f530
```



## 1.2、JWT优点缺点：

优点：

1、安全性：从以上的描述中我们可以看到，payload中的信息只是进行了base64URL编码，并没有加密，所以不能存放敏感信息；同时，JWT如果泄露会被人冒用身份，为防止盗用，JWT应尽量使用https协议传输。

2、性能：JWT为了做到安全性，导致其本身很长，即http请求开销增加。



优点：

1、扩展性：应用程序分布式部署的情况下，客户端的一个令牌可以访问所有的服务器的同时，也避免了服务器之间做session id的多机数据共享。

2、性能：JWT中信息的存储，可以有效的减少服务器查询数据库的次数。

















