package com.lbw.validate.code;

import com.lbw.properties.SecurityProperties;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * Author by lbw , Date on 2018/10/12.
 */

@RestController
public class ValidateCodeController {

  @Autowired
  SecurityProperties securityProperties;

  public static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

  private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

  @Autowired
  private ValidateCodeGenerator imageCodeGenerator;

  //  获取验证码
  @GetMapping("/code/image")
  public void createCode(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    ImageCode imageCode = imageCodeGenerator.generate(new ServletWebRequest(request));
//  存入session中
    sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY, imageCode);
//  将图片写入到输出流中
    ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
  }
}
