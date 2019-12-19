package ui;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * 火柴
 */
public class Stick extends JLabel {
	// 火柴宽度
	private static final int STICK_WIDHT = 25;
	// 火柴高度
	private static final int STICK_HEIGHT = 100;
	// 点着的火柴图片
	private static final ImageIcon FIRE_IMAGE = new ImageIcon(Stick.class.getResource("/res/fire.png"));
	// 灭了的火柴图片
	private static final ImageIcon NO_FIRE_IMAGE = new ImageIcon(Stick.class.getResource("/res/no_fire.png"));
	
	{
		// 设置图片大小
		FIRE_IMAGE.setImage(FIRE_IMAGE.getImage().getScaledInstance(STICK_WIDHT, STICK_HEIGHT, Image.SCALE_DEFAULT));
		// 设置图片大小
		NO_FIRE_IMAGE.setImage(NO_FIRE_IMAGE.getImage().getScaledInstance(STICK_WIDHT, STICK_HEIGHT, Image.SCALE_DEFAULT));
	}
	
	//是否点着状态
	private boolean isFire;

	public Stick() {
		super(FIRE_IMAGE);
		isFire = true;
	}

	public void setBounds(int x, int y) {
		setBounds(x, y, STICK_WIDHT, STICK_HEIGHT);
	}
	
	
	/**
	 * 灭掉火柴
	 * @return
	 */
	public boolean outfire() {
		if(isFire) {
			setIcon(NO_FIRE_IMAGE);
			isFire = false;
			return true;
		}
		
		return false;
	}
	
	/**
	 * 点着火柴
	 */
	public void fire() {
		isFire = true;
		setIcon(FIRE_IMAGE);
	}
	
}
