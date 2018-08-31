package com.ygy.shiro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


/**
* project_name: shiro-redis-json
* package: com.ygy.shiro.config
* describe: swagger配置
* @author : ygy
* creat_time: 2018年8月31日 下午2:41:53
* 
**/
@Configuration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig extends WebMvcConfigurationSupport {

    /**
     * Docket配置，集成在springfox中，可以进行扫描过滤,个性化设置
     * @author 严光远
     * @return
     * 2018年8月31日 下午2:48:50
     */
    @Bean
    public Docket swaggerSpringMvcPlugin() {
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        //token参数设置；
        ticketPar.name("Token").description("token")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build();
        pars.add(ticketPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("api_1")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ygy.shiro.controller"))
                .build().globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Test API Docs")
                .description("对外接口文档说明")
                .license("License Version 1.0")
                .version("1.0").build();
    }
}