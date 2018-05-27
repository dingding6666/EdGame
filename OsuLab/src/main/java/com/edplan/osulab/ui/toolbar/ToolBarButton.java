package com.edplan.osulab.ui.toolbar;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.MContext;
import com.edplan.framework.ui.drawable.sprite.ColorRectSprite;
import com.edplan.framework.ui.drawable.sprite.TextureSprite;
import com.edplan.framework.graphics.opengl.BlendType;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.math.RectF;
import com.edplan.framework.ui.Anchor;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.ui.text.font.FontAwesome;
import com.edplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edplan.framework.ui.animation.FloatQueryAnimation;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.osulab.ui.UiConfig;
import com.edplan.framework.ui.animation.ComplexAnimation;

public class ToolBarButton extends EdView
{
	private ColorRectSprite backlight;
	
	private TextureSprite icon;
	
	private OnClickListener onClickListener;
	
	public ToolBarButton(MContext c){
		super(c);
		setClickable(true);
		backlight=new ColorRectSprite(c);
		//backlight.setBlendType(BlendType.Additive);
		float gr=0.7f;
		backlight.setColor(Color4.rgba(gr,gr,gr,0.2f),
						   Color4.rgba(gr,gr,gr,0.2f),
						   Color4.rgba(1,1,1,0.4f),
						   Color4.rgba(1,1,1,0.4f));
		backlight.setAlpha(0);
		icon=new TextureSprite(c);
	}

	public void setOnClickListener(OnClickListener onClickListener){
		this.onClickListener=onClickListener;
	}

	public OnClickListener getOnClickListener(){
		return onClickListener;
	}
	
	public void onPressAnim(){
		ComplexAnimationBuilder builder=ComplexAnimationBuilder.start(new FloatQueryAnimation<ColorRectSprite>(backlight,"alpha")
										 .transform(backlight.getAlpha(),0,Easing.None)
										 .transform(1,100,Easing.None));
		ComplexAnimation anim=builder.build();
		anim.start();
		setAnimation(anim);
	}
	
	public void offPressAnim(){
		ComplexAnimationBuilder builder=ComplexAnimationBuilder.start(new FloatQueryAnimation<ColorRectSprite>(backlight,"alpha")
																	  .transform(backlight.getAlpha(),0,Easing.None)
																	  .transform(0,250,Easing.None));
		ComplexAnimation anim=builder.build();
		anim.start();
		setAnimation(anim);
	}

	@Override
	public void onClickEvent(){
		// TODO: Implement this method
		super.onClickEvent();
		if(onClickListener!=null)onClickListener.onClick(this);
	}

	@Override
	public void setPressed(boolean pressed){
		// TODO: Implement this method
		super.setPressed(pressed);
		if(pressed){
			onPressAnim();
		}else{
			offPressAnim();
		}
	}
	
	public void setIcon(AbstractTexture t){
		icon.setTexture(t);
	}

	@Override
	public void onDraw(BaseCanvas canvas){
		// TODO: Implement this method
		super.onDraw(canvas);
		
		backlight.setArea(RectF.xywh(0,0,canvas.getWidth(),canvas.getHeight()));
		backlight.draw(canvas);
		
		icon.setPosition(canvas.getWidth()/2f,canvas.getHeight()/2f);
		icon.setAreaFitTexture(RectF.anchorOWH(Anchor.Center,0,0,canvas.getWidth()*0.5f,canvas.getHeight()*0.5f));
		icon.draw(canvas);
	}
	
}
