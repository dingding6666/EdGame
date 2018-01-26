package com.edplan.nso.ruleset.std.playing;
import com.edplan.nso.ruleset.amodel.playing.PlayingBeatmap;
import com.edplan.nso.filepart.PartDifficulty;
import com.edplan.framework.MContext;
import com.edplan.nso.ruleset.std.StdBeatmap;
import com.edplan.nso.ruleset.std.playing.drawable.DrawableStdHitObject;
import com.edplan.nso.ruleset.std.objects.StdHitObject;
import com.edplan.nso.ruleset.std.playing.drawable.DrawableStdHitCircle;
import java.util.List;
import com.edplan.nso.resource.OsuSkin;
import com.edplan.framework.timing.PreciseTimeline;

public class StdPlayingBeatmap extends PlayingBeatmap
{
	private StdBeatmap beatmap;
	
	private MContext context;
	
	public StdPlayingBeatmap(MContext context,StdBeatmap beatmap,PreciseTimeline timeline,OsuSkin skin){
		super(skin,timeline);
		this.context=context;
		this.beatmap=beatmap;
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
	
	public DrawableStdHitObject createDrawableHitObject(StdHitObject obj){
		DrawableStdHitObject dobj=new DrawableStdHitCircle(getContext(),obj);
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
}
