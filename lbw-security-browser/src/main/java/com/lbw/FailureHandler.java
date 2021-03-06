package com.lbw;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lbw.properties.LoginType;
import com.lbw.properties.SecurityProperties;
import com.lbw.support.SimpleResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * Author by lbw , Date on 2018/10/12.
 */
@Slf4j
@Component
public class FailureHandler extends SimpleUrlAuthenticationFailureHandler {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  SecurityProperties securityProperties;

  @Override
  public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, AuthenticationException e)
      throws IOException, ServletException {
    log.info("登陆失败");

    if(LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())){
      httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      httpServletResponse.setContentType("application/json;charset=UTF-8");
      httpServletResponse.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse(e.getMessage())));
    }else {
      super.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
    }
  }
}
