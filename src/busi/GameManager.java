package busi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.function.Consumer;

import javax.swing.Timer;

/**
 * 游戏核心类 
 */
public class GameManager {
	//当前火柴数目
	private int stickCount;
	//是否到玩家回合
	private boolean isPlayerTurn;
	//当前回合剩余行动数
	private int remainActionCount;
	//一回合最大行动数
	private int maxActionCount = 3;
	
	private Random random;
	
	private AIActionTimeListener aiTimeListener;
	
	//AI动作的监听器
	private Consumer aiActionListener;
	
	private Timer aiTimer;
	
	public  GameManager() {
		random = new Random();
		//是否到玩家回合
		isPlayerTurn = true;
		//20 - 50
		stickCount = random.nextInt(31) + 20;
		//当前回合剩余行动数
		remainActionCount = maxActionCount;
		
		aiTimeListener = new AIActionTimeListener();
	}
	
	/**
	 * 获取火柴数目
	 * @return
	 */
	public int getStickCount() {
		return stickCount;
	}

	/**
	 * 是否到我的回合
	 * @return
	 */
	public boolean isPlayerTurn() {
		return isPlayerTurn;
	}

	/**
	 * 剩余行动数
	 * @return
	 */
	public int getRemainActionCount() {
		return remainActionCount;
	}

	/**
	 * 结束回合
	 */
	public void endTurn() {
		//重置剩余行动数
		remainActionCount = maxActionCount;
		//改变到谁操作
		isPlayerTurn = !isPlayerTurn;
		
		//如果不是玩家的回合，则模拟AI操作
		if(!isPlayerTurn) {
			startAiAction();
		}
	}

	/**
	 * 模拟AI操作
	 */
	private void startAiAction() {
		//使用Swing的Timer没有线程问题
		aiTimeListener.setActionCount(getAIActionCount());
		aiTimer = new Timer(getAIActionRandomSecond(), aiTimeListener);
		aiTimer.start();
	}
	
	/**
	 * 获取AI每次操作的执行事件（耗秒）
	 * @return
	 */
	private int getAIActionRandomSecond() {
		return random.nextInt(700) + 500;
	}
	
	/**
	 * 获取AI要执行的行动数
	 * @return
	 */
	private int getAIActionCount() {
		if(stickCount <= maxActionCount) {
			return stickCount;
		}
		
		return random.nextInt(maxActionCount + 1);
	}


	/**
	 * 减少火柴数目
	 * @return
	 */
	public boolean countDown() {
		//已没有行动数
		if(remainActionCount < 1) {
			return false;
		}
		
		--remainActionCount;
		--stickCount;
		return true;
	}

	/**
	 * 是否胜利了
	 * @return
	 */
	public boolean isWin() {
		return stickCount < 1;
	}
	
	private void stopAITimer() {
		if(aiTimer != null) {
			aiTimer.stop();
			aiTimer = null;
		}
	}
	
	/**
	 * 监听AI的行动
	 */
	public void setAIActionLisenter(Consumer<Boolean> lisenter) {
		aiActionListener = lisenter;
	}
	
	private void notifyAIAction(Boolean isOutFire) {
		if(aiActionListener != null) {
			aiActionListener.accept(isOutFire);
		}
	}
	
	
	/**
	 * AI的倒计时监听 
	 */
	private class AIActionTimeListener implements ActionListener{

		private int aiActionCount;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(aiActionCount < 1) {
				stopAITimer();
				//结束回合
				endTurn();
				notifyAIAction(false);
				return;
			}
			
			System.out.println("AI action");
			aiTimer.setDelay(getAIActionRandomSecond());
			//减少火柴数目
			countDown();
			notifyAIAction(true);
			--aiActionCount;
			if(aiActionCount == 0) {
				aiTimer.setDelay(500);
			}
		}
		
		/**
		 * 设置行动数
		 * @param aiActionCount
		 */
		public void setActionCount(int aiActionCount) {
			this.aiActionCount = aiActionCount;
		}
		
	}


	public void destory() {
		//停止AI计时器
		stopAITimer();
	}
}
