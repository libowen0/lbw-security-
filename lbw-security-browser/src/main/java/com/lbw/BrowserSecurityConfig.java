package com.lbw;

import com.lbw.properties.SecurityProperties;
import com.lbw.validate.code.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Author by lbw , Date on 2018/10/11.
 */

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  SecurityProperties securityProperties;

  @Autowired
  private SuccessHandler successHandler;

  @Autowired
  private FailureHandler failureHandler;

  //  加盐加密 防止破解密码相同的账户
  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // 配置验证码 和 错误处理器
    ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
    validateCodeFilter.setFailureHandler(failureHandler);
    validateCodeFilter.setSecurityProperties(securityProperties);
    validateCodeFilter.afterPropertiesSet();
    // http.httpBasic()
    http
        .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
        .formLogin()
//        自定义登陆页面
//        .loginPage("/signIn.html")
//        由请求来确定返回自定义页面
        .loginPage("/authentication/require")
//        默认是/login
        .loginProcessingUrl("/authentication/form")
//        自定义登陆成功处理 实现AuthenticationSuccessHandler
        .successHandler(successHandler)
//        自定义登陆失败处理 实现AUthenticationFailureHandler
        .failureHandler(failureHandler)
        .and()
        .authorizeRequests()
        .antMatchers("/signIn.html", "/authentication/require", "/authentication/signln.html",
            "/code/image", securityProperties.getBrowser().getLoginPage()).permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .csrf().disable();


  }
}
