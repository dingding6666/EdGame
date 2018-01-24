package com.edplan.framework.ui.looper;

import com.edplan.framework.ui.animation.AnimationHandler;
import com.edplan.superutils.classes.advance.IRunnableHandler;
import com.edplan.superutils.classes.advance.RunnableHandler;
import com.edplan.superutils.interfaces.Loopable;

public class UILooper extends StepLooper implements IRunnableHandler 
{
	
	/**
	 *循环开始时最先执行，处理各种post的预处理
	 *所有会影响layout的设置应该都在这里进行，
	 *这个Loopable跑完了之后layout应该被确定了
	 */
	private RunnableHandler runnableHandler;
	
	/**
	 *处理其他循环请求
	 */
	private LooperLoopable<Loopable> otherLoopableHandler;
	
	
	//处理所有动画
	private AnimationHandler animaHandler;

	//绘制操作
	//private UIDrawer

	public UILooper(){
		runnableHandler=new RunnableHandler();
		animaHandler=new AnimationHandler();
		addLoopable(runnableHandler,UIStep.HANDLE_RUNNABLES);
		addLoopable(otherLoopableHandler,UIStep.HANDLE_OTHER_LOOPABLE);
		addLoopable(animaHandler,UIStep.HANDLE_ANIMATION);
	}
	
	public void addLoopableBeforeDraw(Loopable l){
		otherLoopableHandler.addLoopable(l);
	}

	public AnimationHandler getAnimaHandler() {
		return animaHandler;
	}

	@Override
	public void post(Runnable r){
		runnableHandler.post(r);
	}

	@Override
	public void post(Runnable r,int delayMS) {
		// TODO: Implement this method
		runnableHandler.post(r,delayMS);
	}
}
