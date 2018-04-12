package com.edplan.framework.view;

import com.edplan.framework.math.Vec2;
import android.view.MotionEvent;
import android.graphics.drawable.Drawable;
import android.graphics.ColorFilter;
import android.graphics.Canvas;
import com.edplan.framework.inputs.Pointer;
import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.GLCanvas2D;

public class BaseView implements MDrawable
{
	private MViewGroup parent;
	
	private Vec2 basePoint;
	private float height;
	private float width;
	
	private boolean visible=true;
	
	private boolean clipCanvas=true;
	private boolean clipTouch=false;
	
	private MOnSizedListener sizedListener;
	
	private MContext context;
	
	
	
	public BaseView(MContext _context){
		basePoint=Vec2.instance(0,0);
		setContext(_context);
	}
	
	/*
	public BaseWidget(float w,float h,String dw){
		this();
		switch(dw){
			case "dp":
				w=BaseDatas.dpToPixel(w);
				h=BaseDatas.dpToPixel(h);
				break;
		}
		width=w;
		height=h;
	}*/

	public void setContext(MContext context){
		this.context=context;
	}

	public MContext getContext(){
		return context;
	}

	public BaseView setParent(MViewGroup parent){
		this.parent=parent;
		return this;
	}

	public MViewGroup getParent(){
		return parent;
	}

	public BaseView setSizedListener(MOnSizedListener sizedListener){
		this.sizedListener=sizedListener;
		return this;
	}

	public MOnSizedListener getSizedListener(){
		return sizedListener;
	}
	
	public boolean catchPointer(Pointer p){
		return false;
	}

	public BaseView setClipTouch(boolean clipTouch)
	{
		this.clipTouch = clipTouch;
		return this;
	}

	public boolean isClipTouch()
	{
		return clipTouch;
	}

	public BaseView setClipCanvas(boolean clipCanvas)
	{
		this.clipCanvas = clipCanvas;
		return this;
	}

	public boolean isClipCanvas()
	{
		return clipCanvas;
	}

	public BaseView setVisible(boolean visible)
	{
		this.visible = visible;
		return this;
	}

	public boolean ifVisible()
	{
		return visible;
	}
	
	public BaseView setBasePoint(float x,float y){
		basePoint.set(x,y);
		return this;
	}
	
	public Vec2 getBasePoint(){
		return basePoint;
	}
	
	public BaseView setCenter(float x,float y){
		setBasePoint(x-getWidth()/2,y-getHeight()/2);
		return this;
	}
	
	public BaseView setCenterX(float x){
		setBasePoint(x-getWidth()/2,basePoint.y);
		return this;
	}
	
	public BaseView setCenterY(float y){
		setBasePoint(basePoint.x,y-getHeight()/2);
		return this;
	}
	
	public BaseView setBottom(float y){
		setBasePoint(basePoint.x,y-getHeight());
		return this;
	}
	
	public BaseView setHeight(float height)
	{
		this.height = height;
		return this;
	}

	public float getHeight()
	{
		return height;
	}

	public BaseView setWidth(float width)
	{
		this.width = width;
		return this;
	}

	public float getWidth()
	{
		return width;
	}
	
	public void sized(float w,float h){
		setWidth(w);
		setHeight(h);
		if(sizedListener!=null)sizedListener.onSized(this,w,h);
	}
	
	public float getTop(){
		return basePoint.y;
	}
	
	public float getBottom(){
		return basePoint.y+getHeight();
	}
	
	public float getLeft(){
		return basePoint.x;
	}
	
	public float getRight(){
		return basePoint.x+getWidth();
	}
	
	public float getCenterY(){
		return getTop()+getHeight()/2;
	}
	
	public float getCenterX(){
		return getLeft()+getWidth()/2;
	}
	
	public boolean onTouch(MotionEvent event){
		return false;
	}
	
	public boolean inWidget(float x,float y){
		//if(getWidth()==0&&getHeight()==0)return true;
		return (x>=basePoint.x)&&(x<=basePoint.x+getWidth())&&(y>=basePoint.y)&&(y<=basePoint.y+getHeight());
	}
	
	public Canvas clipCanvasToThis(Canvas c){
		if(c.getWidth()!=0&&c.getHeight()!=0)c.clipRect(getLeft(),getTop(),getRight(),getBottom());
		c.translate(basePoint.x,basePoint.y);
		return c;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		// TODO: Implement this method
	}
	
	
	public void drawGL(GLCanvas2D canvas){
		
	}

	
	public BaseView setAlpha(int a)
	{
		// TODO: Implement this method
		return this;
	}

	
	public BaseView setColorFilter(ColorFilter p1)
	{
		// TODO: Implement this method
		return this;
	}

	
	public int getOpacity()
	{
		// TODO: Implement this method
		return 0;
	}
	
	public interface MOnSizedListener{
		public void onSized(BaseView widget,float w,float h);
	}
	
	public interface MOnClickListener{
		public void onClick(BaseView view);
	}
	
}
