package com.leng.project.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Knife4j接口文档配置
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
public class Knife4jConfig {

    @Bean
    public Docket defaultApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("default")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.leng.project.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API接口开放平台接口文档")
                .version("1.0")
                .description("API 接口服务平台是一个为用户和开发者提供全面 API 接口调用服务的平台")
                .termsOfServiceUrl("http://localhost:8101/api/doc.html")
                .contact(new Contact("leng",
                        "https://github.com/xiaoleng-ros/lengapi-backend ",
                        "1873048956@qq.com"))
                .build();
    }
}
