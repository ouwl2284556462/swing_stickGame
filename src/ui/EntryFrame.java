package ui;

import java.awt.Image;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 进入游戏窗口
 */
public class EntryFrame extends JFrame {

	public EntryFrame() {
		// 设置标题
		setTitle("取火柴");
		// 设置大小
		setSize(400, 430);
		// 关闭窗口
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initPanelCont();
		// 设置窗口屏幕居中
		setLocationRelativeTo(null);
		// 设置可见
		setVisible(true);
		// 不可改变大小
		setResizable(false);
	}

	private void initPanelCont() {
		JPanel contPanel = new JPanel();
		setContentPane(contPanel);
		// 清空默认布局
		contPanel.setLayout(null);

		int imgLabelSize = 300;
		ImageIcon image = new ImageIcon(this.getClass().getResource("/res/myIcon.png"));
		// 设置图片大小
		image.setImage(image.getImage().getScaledInstance(imgLabelSize, -1, Image.SCALE_DEFAULT));
		JLabel gameImageLabel = new JLabel(image);
		gameImageLabel.setBounds(50, 0, imgLabelSize, imgLabelSize);
		contPanel.add(gameImageLabel);

		JButton startBtn = new JButton("开始游戏");
		startBtn.setBounds(140, 290, 100, 40);
		contPanel.add(startBtn);
		startBtn.addActionListener(e -> {
			//开始游戏
			new GamingFrame(this);
			//隐藏自己
			setVisible(false);
		});

		
		JButton exitBtn = new JButton("退出游戏");
		exitBtn.setBounds(140, 340, 100, 40);
		contPanel.add(exitBtn);
		exitBtn.addActionListener(e -> {
			//发出关闭窗口事件
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		});
	}

	public static void main(String[] args) {
		new EntryFrame();
	}
}
