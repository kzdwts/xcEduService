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

