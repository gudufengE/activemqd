package com.example.demo.config;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger2 api文档的配置
 * 访问：http://<ip>:<port>/doc.html
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Value("${swagger.host}")
    private String host;

    @Value("${swagger.title}")
    private String title;

    @Value("${swagger.description}")
    private String description;

    @Value("${swagger.author}")
    private String author;

    @Value("${project.version}")
    private String version;

    @Value("${server.port}")
    private String port;

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .termsOfServiceUrl(port + ":/*")
                .version(version)
                .contact(new Contact(author, "www.lqkj.com", "87902675@qq.com"))
                .build();
    }

    // Swagger2默认将所有的Controller中的RequestMapping方法都会暴露（前提：能扫描到的包下的Controller类），
    // 然而在实际开发中，我们并不一定需要把所有API都提现在文档中查看，这种情况下，使用注解
    // @ApiIgnore来解决，如果应用在Controller范围上，则当前Controller中的所有方法都会被忽略，
    // 如果应用在方法上，则忽略方法
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
//                扫描方式（使用其中的一种）
//                第一种、单基包方式
//                .apis(RequestHandlerSelectors.basePackage("com.lqkj.redisdemo.front.controller"))
//                第二种、多基包方式:以splitor定义的字符分割
//                .apis(basePackage("com.lqkj.redisdemo.front.controller" + splitor + "com.lqkj.redisdemo.front.model"))
//                第三种、注解的类方式：这里设置为添加了@Api注解的类
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }

//  扫描多基包的实现代码start
    private static final String splitor = ";";  //定义包之间的分隔符
    public static Predicate<RequestHandler> basePackage(final String basePackage) {
        return input -> declaringClass(input).transform(handlerPackage(basePackage)).or(true);
    }
    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage)     {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackage.split(splitor)) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }
    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }
//  扫描多基包的实现代码end
}
