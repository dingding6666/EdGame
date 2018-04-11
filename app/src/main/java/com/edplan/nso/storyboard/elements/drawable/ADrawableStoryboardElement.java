package com.edplan.nso.storyboard.elements.drawable;
import com.edplan.framework.ui.drawable.EdDrawable;
import com.edplan.framework.MContext;
import com.edplan.nso.storyboard.PlayingStoryboard;
import com.edplan.framework.timing.PreciseTimeline;

public abstract class ADrawableStoryboardElement extends EdDrawable
{
	private PlayingStoryboard storyboard;
	
	public ADrawableStoryboardElement(PlayingStoryboard storyboard){
		super(storyboard.getContext());
		this.storyboard=storyboard;
	}
	
	public PreciseTimeline getTimeline(){
		return storyboard.getTimeline();
	}

	public void setStoryboard(PlayingStoryboard storyboard) {
		this.storyboard=storyboard;
	}

	public PlayingStoryboard getStoryboard() {
		return storyboard;
	}
	
	public abstract void onAdd();
	
	public abstract void onRemove();
	
	public abstract double getStartTime();
	
	public abstract double getEndTime();
	
	public abstract boolean hasAdd();
}