package ui;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import busi.GameManager;

/**
 * 游戏中的窗口
 */
public class GamingFrame extends JFrame {

	// 父窗口
	private EntryFrame parentFrame;

	private GameManager gameManager;

	private JLabel turnLabel;

	private JLabel remainLabel;
	
	private List<Stick> stickList;
	
	private JButton passBtn;
	
	private Random random;

	public GamingFrame(EntryFrame parentFrame) {
		random = new Random();
		gameManager = new GameManager();
		//监听AI的动作
		gameManager.setAIActionLisenter(isOutfire -> {
			refreshCurGameUIInfo();
			if(isOutfire) {
				randomOutfire();
			}else {
				//回合结束
				//是否胜利了
				if(gameManager.isWin()) {
					showMsg("你输了！");
					//回到主界面
					backToParentFrame();
				}
				
				passBtn.setEnabled(true);
			}
		});
		
		// 设置标题
		setTitle("游戏开始");
		// 设置大小
		setSize(650, 500);

		this.parentFrame = parentFrame;
		// 关闭窗口
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				gameManager.destory();
				backToParentFrame();
			}
		});

		initPanelCont();
		// 设置窗口屏幕居中
		setLocationRelativeTo(null);
		// 设置可见
		setVisible(true);
		// 不可改变大小
		setResizable(false);

	}

	/**
	 * 随机灭一根火柴
	 */
	private void randomOutfire() {
		int index = random.nextInt(stickList.size());
		stickList.get(index).outfire();
		stickList.remove(index);
	}

	private void initPanelCont() {
		JPanel contPanel = new JPanel();
		setContentPane(contPanel);
		// 清空默认布局
		contPanel.setLayout(null);

		turnLabel = new JLabel("轮到：我方");
		turnLabel.setBounds(20, 10, 100, 50);
		turnLabel.setFont(new Font("宋体", Font.BOLD, 15));
		contPanel.add(turnLabel);

		remainLabel = new JLabel("剩余行动数：3");
		remainLabel.setBounds(20, 40, 200, 50);
		remainLabel.setFont(new Font("宋体", Font.BOLD, 15));
		contPanel.add(remainLabel);

		passBtn = new JButton("回合结束");
		passBtn.setBounds(450, 20, 100, 30);
		contPanel.add(passBtn);
		passBtn.addActionListener(e -> {
			// 结束回合
			gameManager.endTurn();
			refreshCurGameUIInfo();
			passBtn.setEnabled(false);
		});

		// 获取火柴数目
		int stickCount = gameManager.getStickCount();

		int baseX = 20;
		int baseY = 100;
		int curX = baseX;
		int curY = baseY;
		int spanX = 30;
		int spanY = 110;
		int colCount = 0;
		// 每行多少个
		int maxColCount = 20;

		// 火柴被点击事件
		MouseAdapter stickMouseClickListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!gameManager.isPlayerTurn()) {
					showErrMsg("不是你的回合，请稍后");
					return;
				}
				
				
				Stick stick = (Stick) e.getSource();
				// 灭点火柴
				if (!stick.outfire()) {
					showErrMsg("该火柴已经灭掉了，请重新选择");
					return;
				}

				// 减少火柴
				boolean countDownSuccss = gameManager.countDown();
				if (!countDownSuccss) {
					//恢复火柴的状态
					stick.fire();
					showErrMsg("你已不能再行动，请结束回合。");
					return;
				}
				
				stickList.remove(stick);
				//更新游戏界面信息
				refreshCurGameUIInfo();
				//是否胜利了
				if(gameManager.isWin()) {
					showMsg("你赢了！");
					//回到主界面
					backToParentFrame();
				}
			}
		};

		stickList = new ArrayList<Stick>(stickCount);
		for (int i = 0; i < stickCount; ++i) {
			Stick st = new Stick();
			contPanel.add(st);
			st.setBounds(curX, curY);

			curX += spanX;
			++colCount;
			if (colCount == maxColCount) {
				colCount = 0;
				curX = baseX;
				curY += spanY;
			}

			stickList.add(st);
			st.addMouseListener(stickMouseClickListener);
		}

		// 刷新当前游戏的UI信息
		refreshCurGameUIInfo();
	}

	/**
	 * 刷新当前游戏的UI信息
	 */
	private void refreshCurGameUIInfo() {
		turnLabel.setText(gameManager.isPlayerTurn() ? "轮到：我方" : "轮到：对方");
		remainLabel.setText("剩余行动数：" + gameManager.getRemainActionCount());
	}

	private void showErrMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "错误", JOptionPane.ERROR_MESSAGE);
	}
	
	private void showMsg(String msg){
		JOptionPane.showMessageDialog(null, msg, "", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * 关闭自己，显示父窗口
	 */
	private void backToParentFrame() {
		parentFrame.setVisible(true);
		// 关闭自己
		dispose();
	}

}
