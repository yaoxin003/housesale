# housesale
房产销售项目
一.house为单体项目

二.house单体拆分
1.house-parent、house-common、house-biz、house-web是由house拆分而来，但本质上也是单体项目
2.单体版本端口说明
2.1.house-web:8080
2.2.actuator:8091 http://localhost:8091/health
3.3.spring-admin:9090  http://localhost:9090

三.微服务版本端口说明
api-gateway:8080
api-gateway对应的actural监控系统:8090 http://localhost:8090/health  
user-service:8083
house-service:8084
comment-service:8085
eureka-server:8666 http://localhost:8666/actuator/health
hystrix-dashboard:9097 http://localhost:9097/hystrix





