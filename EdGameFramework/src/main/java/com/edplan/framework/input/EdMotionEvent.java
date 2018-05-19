package com.edplan.framework.input;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.interfaces.Copyable;
import android.view.MotionEvent;

/**
 *对于鼠标，触摸等事件的封装，包含事件发生位置，事件种类等信息
 */
public class EdMotionEvent
{
	/**
	 *记录事件对应原始输入的类型
	 */
	private RawType rawType;
	
	private Vec2 eventPosition=new Vec2();
	
	private EventType eventType;
	
	private int pointerId;
	
	/**
	 *相关联的按键，比如鼠标点击的时候用这个判定左右键
	 */
	private int keyCode;
	
	private int pointCount;
	
	public EdMotionEvent(){
		
	}
	
	public EdMotionEvent(EdMotionEvent event){
		set(event);
	}
	
	public void setPointCount(int pointCount) {
		this.pointCount=pointCount;
	}

	public int getPointCount() {
		return pointCount;
	}

	public void set(EdMotionEvent e){
		this.rawType=e.rawType;
		this.eventPosition.set(e.eventPosition);
		this.eventType=e.eventType;
		this.pointerId=e.pointerId;
		this.keyCode=e.keyCode;
		this.pointCount=e.pointCount;
	}
	
	public void setRawType(RawType rawType) {
		this.rawType=rawType;
	}

	public RawType getRawType() {
		return rawType;
	}

	public void setEventPosition(Vec2 eventPosition) {
		this.eventPosition.set(eventPosition);
	}

	public Vec2 getEventPosition() {
		return eventPosition;
	}

	public void setEventType(EventType eventType) {
		this.eventType=eventType;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setPointerId(int pointerId) {
		this.pointerId=pointerId;
	}

	public int getPointerId() {
		return pointerId;
	}

	public void setKeyCode(int keyCode) {
		this.keyCode=keyCode;
	}

	public int getKeyCode() {
		return keyCode;
	}
	
	public EdMotionEvent copy(){
		return new EdMotionEvent(this);
	}
	
	public static EdMotionEvent load(MotionEvent raw){
		EdMotionEvent event=new EdMotionEvent();
		event.eventPosition.set(raw.getX(),raw.getY());
		event.eventType=parseType(raw.getActionMasked());
		return event;
	}
	
	public static EventType parseType(int actionMasked){
		switch(actionMasked){
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				return EventType.Down;
			case MotionEvent.ACTION_MOVE:
				return EventType.Move;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				return EventType.Up;
			case MotionEvent.ACTION_HOVER_ENTER:
				return EventType.HoverEnter;
			case MotionEvent.ACTION_HOVER_MOVE:
				return EventType.Hover;
			case MotionEvent.ACTION_HOVER_EXIT:
				return EventType.HoverExit;
			default:return EventType.Cancel;
		}
	}
	
	/**
	 *一个事件组应该是以下类型：
	 *1：Down - Move x n - Cancel 或者 Up
	 *2：静止 - Swipe x n - Cancel 或者 Stop
	 *一般不建议在Swipe事件组处理有实质交互的行为，
	 *Swipe事件组一般应该用来进行组件动画效果
	 */
	public enum EventType{
		//Down：对应触摸的点击还有鼠标的左右键
		Down,
		//Move：对应在点击状态下的移动
		Move,
		//Up：对应点击事件抬起
		Up,
		//HoverEnter：非点击状态下进入区域
		HoverEnter,
		//Hover：对应在非点击状态下的移动
		Hover,
		//HoverExit：非点击状态下离开区域
		HoverExit,
		//Cancel：对应事件组被取消
		Cancel
	}
	
	public enum RawType{
		TouchScreen,Tablet,Mouse
	}
}
