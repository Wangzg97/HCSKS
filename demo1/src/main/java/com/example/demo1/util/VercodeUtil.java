package com.example.demo1.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class VercodeUtil {
    private static final char[] ops = new char[]{'+', '-', '*'};

    //生成验证码表达式
    public static String generateVerifyCode(Random random) {
        //生成4个随机整数
        int num1 = random.nextInt(10) + 1;
        int num2 = random.nextInt(10) + 1;
        int num3 = random.nextInt(10) + 1;
        int num4 = random.nextInt(10) + 1;
        //三个运算符
        var opsLen = ops.length;
        char op1 = ops[random.nextInt(opsLen)];
        char op2 = ops[random.nextInt(opsLen)];
        char op3 = ops[random.nextInt(opsLen)];

        return ""+num1+op1+num2+op2+num3+op3+num4;
    }

    //图形验证码表达式生成验证码图片
    public static BufferedImage createVerifyImage(String verifyCode, Random random) {
        var width = 120;
        var height = 32;
        //创建图形
        var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        //设置背景色
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        //绘制边框
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width-1, height-1);
        //生成一些干扰椭圆
        for(int i=0;i<50;i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        //设置颜色
        g.setColor(new Color(0, 100, 0));
        //设置字体
        g.setFont(new Font("Candara", Font.BOLD, 24));
        //绘制图形验证码
        g.drawString(verifyCode, 8, 24);
        g.dispose();

        return image;
    }

    //计算表达式的值
    public static int calc(String exp) {
        try{
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer) engine.eval(exp);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }
}
