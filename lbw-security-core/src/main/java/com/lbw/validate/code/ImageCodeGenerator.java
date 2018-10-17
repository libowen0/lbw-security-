package com.lbw.validate.code;

import com.lbw.properties.SecurityProperties;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * Author by lbw , Date on 2018/10/17.
 */
public class ImageCodeGenerator implements ValidateCodeGenerator {

  public SecurityProperties getSecurityProperties() {
    return securityProperties;
  }

  public void setSecurityProperties(SecurityProperties securityProperties) {
    this.securityProperties = securityProperties;
  }

  @Autowired
  private SecurityProperties securityProperties;

  @Override
  public ImageCode generate(ServletWebRequest request) {
    int width = ServletRequestUtils
        .getIntParameter(request.getRequest(),"width",securityProperties.getCode().getImage().getWidth());

    int height = ServletRequestUtils.getIntParameter(request.getRequest(),"height",securityProperties.getCode().getImage().getHeight());

    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    Graphics g = image.getGraphics();

    Random random = new Random();
    g.setColor(getRandColor(200, 250));
    g.fillRect(0, 0, width, height);
    g.setFont(new Font("Times new Roman", Font.ITALIC, 20));
    for (int i = 0; i < 155; i++) {
      int x = random.nextInt(width);
      int y = random.nextInt(height);
      int xl = random.nextInt(12);
      int yl = random.nextInt(12);
      g.drawLine(x, y, x + xl, +y + yl);
    }

    String sRand = "";

    for (int i = 0; i < securityProperties.getCode().getImage().getLength(); i++) {
      String rand = String.valueOf(random.nextInt(10));
      sRand += rand;
      g.setColor(
          new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
      g.drawString(rand, 13 * i + 6, 16);
    }
    g.dispose();
    return new ImageCode(image, sRand, securityProperties.getCode().getImage().getExpireIn());
  }

  // 删除随机背景条纹
  private Color getRandColor(int fc, int bc) {
    Random random = new Random();
    if (fc> 255) {
      fc = 255;
    }
    if (bc > 255) {
      bc = 255;
    }
    int r = fc + random.nextInt(bc - fc);
    int g = fc + random.nextInt(bc - fc);
    int b = fc + random.nextInt(bc - fc);
    return new Color(r,g,b);
  }
}
