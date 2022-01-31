# xcEduService

学成在线

## 项目结构
- xcEduService
    - xc-framework-parent（学成在线父工程：这里没有以文件夹的形式存放，所有工程均在同一个目录）
    - test-fastdfs（fastdfs文件上传测试工程）
    - test-freemarker（freemarker模板测试工程）
    - test-rabbitmq-consumer（rabbitmq消费者测试工程）
    - test-rabbitmq-producer（rabbitmq生产者测试工程）
    - xc-framework-common（学成在线通用工程）
        - client
        - exception
        - model
        - web
    - xc-framework-model（学成在线model工程，主要存放数据库实体，请求入参出参实体）
    - xc-framework-utils（学成在线工具类工程，存放所有的通用工具类） 
    - xc-govern-center（学成在线注册管理中心，eureka注册中心，所有的工程都要在注册中心进行注册）
    - xc-server-api（学成在线api工程，所有的接口都在这里定义，接口标注swagger注解，业务工程继承本工程实现业务接口）
        - cms（CMS系统接口）
        - config（swagger配置）
        - course（课程管理系统接口）
        - filesystem（文件管理工程接口）
        - media（媒资管理工程接口）
        - search（ES搜索工程接口）
        - sys（字典配置相关接口）
    - xc-service-base-filesystem（学成在线文件管理系统，主要实现调用fastdfs存储文件）
    - xc-service-manage-cms（学成在线cms管理系统，主要有cms页面、模板、站点、配置相关功能）
    - xc-service-manage-cms-client（学成在线客户端系统，主要实现接受mq消息生成cms文件到相关nginx映射目录）
    - xc-service-manage-course（学成在线课程管理系统，课程管理相关接口：课程发布、预览、新增）
    - xc-service-manage-media（学成在线媒资管理系统，主要实现文件断点续传，前端配合百度web uploader实现，后端将文件存储在MongoDB GridFS）
    - xc-service-search（学成在线ES搜索系统）
    - xc-service-manage-media-processor（学成在线媒资管理子系统，接受文件上传mq消息，处理视频）
    - xc-service-learning（学成在线学习工程。主要查询媒资文件访问路径）
    - xc-service-ucenter-auth（学成在线用户认证工程）
    
    
## 参考文档

* [百度web uploader文件上传](http://fex.baidu.com/webuploader/) 分片（断点续传）、分发 
* [FFmpeg](https://ffmpeg.org/) FFmpeg是一套可以用来记录、转换数字音频、视频，并能将其转化为流的开源计算机程序
* [Nuxt](https://nuxtjs.org/) 服务器端渲染,生成静态站点；动态生成静态页面技术


