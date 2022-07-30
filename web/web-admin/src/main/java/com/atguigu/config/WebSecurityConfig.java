package com.atguigu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 包名:com.atguigu.config
 *
 * @author Leevi
 * 日期2022-06-22  09:09
 * 配置类要添加@Configuration注解
 * 要开启Spring Security则需要添加@EnableWebSecurity
 *
 * 一、配置内存中的账号密码
 * 1. 重写WebSecurityConfig类中的configure(AuthenticationManagerBuilder auth)指定内存中的账号密码
 * 2. 在IOC容器中配置一个加密器:BCryptPasswordEncoder
 * 3. 重写WebSecurityConfig类中
 *
 * 二、进行自定义配置
 *
 * 三、权限校验
 *  添加 @EnableGlobalMethodSecurity(prePostEnabled = true)
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("lucy")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .roles("");
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //配置允许页面嵌套
        http.headers().frameOptions().disable();

        //Spring Security的一些自定义配置
        http.authorizeRequests()
            .antMatchers("/static/**","/login").permitAll()  //如果你没有登录，允许你访问什么资源
            .anyRequest().authenticated()    //除了上述内容之外，其它的资源全部都需要登录后才能访问
            .and()
            .formLogin()
            .loginPage("/login")    // 设置登录页面
            .defaultSuccessUrl("/") // 登录成功之后跳转到哪个资源
            .and()
            .logout()
            .logoutUrl("/logout")   // 退出登录访问的路径，Spring Security提供的
            .logoutSuccessUrl("/login");// 当用户成功退出登录之后，访问哪个资源

        //关闭跨域请求伪造
        http.csrf().disable();

        //指定自定义的访问拒绝处理器
        http.exceptionHandling().accessDeniedHandler(new AtguiguAccessDeniedHandler());
    }

    @Bean
    public BCryptPasswordEncoder createBCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
