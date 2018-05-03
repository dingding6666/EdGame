package com.edplan.nso.ruleset.std.playing;
import com.edplan.framework.MContext;
import com.edplan.framework.timing.PreciseTimeline;
import com.edplan.nso.filepart.PartDifficulty;
import com.edplan.nso.resource.OsuSkin;
import com.edplan.nso.ruleset.amodel.playing.PlayingBeatmap;
import com.edplan.nso.ruleset.std.StdBeatmap;
import com.edplan.nso.ruleset.std.objects.StdHitObject;
import com.edplan.nso.ruleset.std.playing.drawable.DrawableStdHitCircle;
import com.edplan.nso.ruleset.std.playing.drawable.DrawableStdHitObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.edplan.nso.ruleset.std.playing.drawable.DrawableStdFollowpoint;
import android.util.Log;
import com.edplan.nso.ruleset.std.objects.StdSlider;
import com.edplan.nso.ruleset.std.playing.drawable.DrawableStdSlider;
import com.edplan.nso.ruleset.std.playing.controlpoint.ControlPoints;

public class StdPlayingBeatmap extends PlayingBeatmap
{
	private StdBeatmap beatmap;
	
	private MContext context;
	
	private List<DrawableStdHitObject> drawableHitObjects;
	
	private List<DrawableStdHitObject> connectionObjs;
	
	private ControlPoints controlPoints;
	
	public StdPlayingBeatmap(MContext context,StdBeatmap beatmap,PreciseTimeline timeline,OsuSkin skin){
		super(skin,timeline);
		this.context=context;
		this.beatmap=beatmap;
		loadControlPoints();
		calDrawables();
	}
	
	public void loadControlPoints(){
		controlPoints=new ControlPoints();
		controlPoints.load(getBeatmap().getTimingPoints().getTimingPoints());
	}

	public void calDrawables(){
		int objCount=getHitObjects().size();
		drawableHitObjects=new ArrayList<DrawableStdHitObject>(objCount);
		DrawableStdHitObject dobj=null;
		int count=0;
		
		if(!getHitObjects().get(0).isNewCombo()){
			Log.w("err-beatmap","Beatmap's first HitObject must be a new combo");
			getHitObjects().get(0).setIsNewCombo(true);
		}
		
		for(StdHitObject obj:getHitObjects()){
			count++;
			dobj=createDrawableHitObject(obj);
			//Log.v("load-bmp",count+"/"+getHitObjects().size());
			drawableHitObjects.add(dobj);
		}
		//Log.v("Slider-size",maxSliderArray+"");
		
		//此时依然保持原顺序，计算followpoints
		connectionObjs=new ArrayList<DrawableStdHitObject>(objCount);
		DrawableStdHitObject pre=(objCount!=0)?drawableHitObjects.get(0):null;
		DrawableStdHitObject now;
		DrawableStdFollowpoint followpoint;
		for(int i=1;i<objCount;i++){
			now=drawableHitObjects.get(i);
			if(!now.getHitObject().isNewCombo()){
				followpoint=new DrawableStdFollowpoint(getContext(),pre,now);
				connectionObjs.add(followpoint);
			}
			pre=now;
		}
		Collections.sort(connectionObjs, new Comparator<DrawableStdHitObject>(){
				@Override
				public int compare(DrawableStdHitObject p1,DrawableStdHitObject p2) {
					// TODO: Implement this method
					return (int)Math.signum(p1.getShowTime()-p2.getShowTime());
				}
			});
		Collections.sort(drawableHitObjects, new Comparator<DrawableStdHitObject>(){
				@Override
				public int compare(DrawableStdHitObject p1,DrawableStdHitObject p2) {
					// TODO: Implement this method
					return (int)Math.signum(p1.getShowTime()-p2.getShowTime());
				}
			});
		int comboIndex=1;
		for(DrawableStdHitObject obj:drawableHitObjects){
			if(obj.getHitObject().isNewCombo()){
				comboIndex=1;
			}else{
				comboIndex++;
			}
			obj.setComboIndex(comboIndex);
			obj.applyDefault(this);
		}
		for(DrawableStdHitObject obj:connectionObjs){
			obj.applyDefault(this);
		}
	}
	
	public StdBeatmap getBeatmap() {
		return beatmap;
	}

	public MContext getContext() {
		return context;
	}
	
	public List<StdHitObject> getHitObjects(){
		return getBeatmap().getHitObjects().getHitObjectList();
	}
	
	public List<DrawableStdHitObject> getDrawableHitObjects(){
		return drawableHitObjects;
	}
	
	public List<DrawableStdHitObject> getDrawableConnections(){
		return connectionObjs;
	}
	
	public DrawableStdHitObject createDrawableHitObject(StdHitObject obj){
		DrawableStdHitObject dobj=null;
		if(obj instanceof StdSlider){
			dobj=new DrawableStdSlider(getContext(),(StdSlider)obj);
			//Log.v("sld","create sld");
		}else{
			dobj=new DrawableStdHitCircle(getContext(),obj);
		}
		if(obj.isNewCombo()){
			getSkin().comboColorGenerater.skipColors(obj.getComboColorsSkip());
			dobj.setAccentColor(getSkin().comboColorGenerater.nextColor());
		}else{
			dobj.setAccentColor(getSkin().comboColorGenerater.currentColor());
		}
		return dobj;
	}

	@Override
	public PartDifficulty getDifficulty() {
		// TODO: Implement this method
		return getBeatmap().getDifficulty();
	}

	@Override
	public ControlPoints getControlPoints() {
		// TODO: Implement this method
		return controlPoints;
	}
}