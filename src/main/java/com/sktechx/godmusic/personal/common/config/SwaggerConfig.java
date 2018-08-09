package com.sktechx.godmusic.personal.common.config;

import com.sktechx.godmusic.lib.domain.CommonConstant;
import com.sktechx.godmusic.personal.common.interceptor.TransactionIdInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

//import org.apache.maven.model.Model;
//import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

/**
 * 설명 : http://springfox.github.io/springfox/docs/current/#springfox-swagger2-with-spring-mvc-and-spring-boot
 *
 * @author 안영현(Younghyun Ahn)/(younghyun.ahn@sk.com)
 * @date 2018.07.03
 */


@Profile({"local", "dev"})
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() throws IOException {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sktechx.godmusic.personal.rest.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .globalOperationParameters(operationParameters())
                ;
    }

    private ApiInfo apiInfo() throws IOException{
//        MavenXpp3Reader reader = new MavenXpp3Reader();
//        Model model = reader.read(new FileInputStream("pom.xml"));

        return new ApiInfoBuilder()
                .title("Personal Api Documentation")
                .description("Documentation automatically generated")
//                .version(model.getVersion())
                .version("V1")
                .contact(new Contact("안영현", "http://172.21.112.100/swagger-ui.html", "younghyun.ahn@sk.com"))
                .build();
    }

    private List<Parameter> operationParameters()   {
        List<Parameter> operationParameters = new LinkedList<>();
//        operationParameters.add(
//                new ParameterBuilder()
//                        .name(CommonConstant.X_GM_MEMBER_NO)
//                        .required(false)
//                        .description("회원번호")
//                        .parameterType("header")
//                        .defaultValue("1")
//                        .modelRef(new ModelRef("string"))
//                        .build()
//        );
//
//        operationParameters.add(
//                new ParameterBuilder()
//                .name(CommonConstant.X_GM_CHARACTER_NO)
//                .required(false)
//                .description("캐릭터번호")
//                .parameterType("header")
//                .defaultValue("12")
//                .modelRef(new ModelRef("string"))
//                .build()
//        );

        operationParameters.add(
                new ParameterBuilder()
                .name(CommonConstant.X_GM_ACCESS_TOKEN)
                .required(false)
                .description("Access Token")
                .parameterType("header")
                .defaultValue("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiZ2RtIjp7ImNubyI6MTIsIm1ubyI6MX0sImlzcyI6Imh0dHBzOlwvXC9kZXYtYXBpLm11c2ljbWF0ZXMuY28ua3IiLCJleHAiOjE1NDg4MjY0MDQsImp0aSI6IjQ3OGY1NWRmODAyYjRhZjRhMTJjZGUzYzRmMDUzODMzIn0.Z-YvrKIZFPwCg8uhVHE_QrnaZh-ro130Ib0CwxJ4uSM")
                .modelRef(new ModelRef("string"))
                .build()
        );
        return operationParameters;
    }
}
