package mms.zhangzhichao;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;

import org.apache.commons.lang3.RandomUtils;

//彩色验证码
public class ColorfulCAPTCHALabel extends JLabel {
	private static final long serialVersionUID = -963570191302793615L;
	private String text;// 用于保存生成验证图片的字符串
	private Color[] colors = { Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,
	Color.PINK, Color.RED};// 定义画笔颜色数组
	public ColorfulCAPTCHALabel(String text) {
		this.text = text;
		setPreferredSize(new Dimension(60, 36));// 设置标签的大小
		setBackground(Color.WHITE);
		setOpaque(true);
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);// 调用父类的构造方法
		g.setFont(new Font("微软雅黑", Font.PLAIN, 15));// 设置字体
		for (int i = 0; i < text.length(); i++) {
			g.setColor(colors[RandomUtils.nextInt(0, colors.length)]);
			g.drawString("" + text.charAt(i), 5 + i * 13, 25);// 绘制字符串
		}
	}
}
