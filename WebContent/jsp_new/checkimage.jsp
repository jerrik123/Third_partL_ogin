<%@ page contentType="image/png"
	import="java.awt.*,java.awt.image.*,java.util.*,javax.imageio.*"%>
<%!Color getRandColor(int fc, int bc) {//给定范围获得随机颜色
        Random random = new Random();
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    private void contortImage(BufferedImage img) {
        Random rand = new Random();
        int[] newRGBs = new int[img.getWidth()];
        int offset = 0;
        int[] RGBs = new int[img.getWidth()];
        for (int y = 0; y < img.getHeight(); y++) {
            img.getRGB(0, y, img.getWidth(), 1, RGBs, 0, RGBs.length);
            if (y == img.getHeight() / 2) {
                for (int i = 0; i < newRGBs.length; i++)
                    newRGBs[i] = RGBs[0];
            } else {
                offset += rand.nextInt(3) - 1;
                for (int i = 0; i < RGBs.length; i++) {
                    int tmp = i + offset;
                    if (tmp < 0)
                        tmp = newRGBs.length - 1;
                    else if (tmp >= newRGBs.length)
                        tmp = 0;
                    newRGBs[tmp] = RGBs[i];
                }
            }
            img.setRGB(0, y, img.getWidth(), 1, newRGBs, 0, newRGBs.length);
        }
    }%>
<%
            //设置页面不缓存
            response.reset();
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);

            // 在内存中创建图象
            int width = 80, height = 25;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            // 获取图形上下文
            Graphics g = image.getGraphics();

            //生成随机类
            Random random = new Random();

            // 设定背景色
            g.setColor(getRandColor(200, 250));
            g.fillRect(0, 0, width, height);

            //设定字体

            //画边框
            //g.setColor(new Color());
            //g.drawRect(0,0,width-1,height-1);

            // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
            g.setColor(getRandColor(160, 200));
            for (int i = 0; i < 120; i++) {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                int xl = random.nextInt(12);
                int yl = random.nextInt(12);
                g.drawLine(x, y, x + xl, y + yl);
            }

            // 取随机产生的认证码(4位数字)
            String sRand = "";
            for (int i = 0; i < 4; i++) {
                String rand = String.valueOf(random.nextInt(10));
                sRand += rand;
                // 将认证码显示到图象中
                g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110),
                    20 + random.nextInt(110))); //调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
                g.setFont(new Font("Arial", Font.BOLD, 20 + random.nextInt(10)));
                g.drawString(rand, 18 * i + random.nextInt(10), 20);
            }

            // 将认证码存入SESSION
            session.setAttribute("sysrand", sRand);
            contortImage(image);
            // 图象生效
            g.dispose();
            ServletOutputStream os = response.getOutputStream();
            // 输出图象到页面
            ImageIO.write(image, "PNG", os);
            os.flush();
            os.close();
            os = null;
            response.flushBuffer();
            out.clear();
            out = pageContext.pushBody();
%>
