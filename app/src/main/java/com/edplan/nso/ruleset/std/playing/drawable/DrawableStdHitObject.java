package com.edplan.nso.ruleset.std.playing.drawable;
import com.edplan.nso.ruleset.amodel.playing.drawable.DrawableHitObject;
import com.edplan.nso.ruleset.amodel.playing.Judgment;
import com.edplan.framework.timing.PreciseTimeline;
import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.math.Vec2;
import com.edplan.nso.ruleset.std.objects.StdHitObject;
import com.edplan.nso.ruleset.std.StdBeatmap;
import com.edplan.nso.difficulty.DifficultyUtil;
import com.edplan.nso.ruleset.amodel.playing.PlayingBeatmap;

public class DrawableStdHitObject extends DrawableHitObject
{
	private float baseSize=64;
	
	private Vec2 origin=new Vec2();
	
	private StdHitObject hitObject;
	
	private float showTime;
	
	private float timePreempt;
	
	private float timeFadein;
	
	private float alpha=1;
	
	private PreciseTimeline timeLine;

	public DrawableStdHitObject(StdHitObject obj){
		hitObject=obj;
		setOrigin(new Vec2(obj.getStartX(),obj.getStartY()));
	}

	public void setBaseSize(float baseSize) {
		this.baseSize=baseSize;
	}

	public float getBaseSize() {
		return baseSize;
	}

	public void setOrigin(Vec2 origin) {
		this.origin.set(origin);
	}

	public Vec2 getOrigin() {
		return origin;
	}

	/**
	 *通过HitObject无法确定的属性在这里设置（如和难度相关的）
	 */
	public void applyDefault(PlayingBeatmap beatmap){
		timeLine=beatmap.getTimeLine();
		timePreempt=DifficultyUtil.stdHitObjectTimePreempt(beatmap.getDifficulty().getApproachRate());
		timeFadein=DifficultyUtil.stdHitObjectTimeFadein(beatmap.getDifficulty().getApproachRate());
		showTime=hitObject.getStartTime()-timePreempt;
	}
	
	@Override
	public void draw(GLCanvas2D canvas) {
		// TODO: Implement this method
	}

	@Override
	public void setAlpha(float a) {
		// TODO: Implement this method
		this.alpha=a;
	}

	@Override
	public float getAlpha() {
		// TODO: Implement this method
		return alpha;
	}

	@Override
	public float getShowTime() {
		// TODO: Implement this method
		return showTime;
	}

	@Override
	public void onShow() {
		// TODO: Implement this method
	}

	@Override
	public void onJudgment(Judgment judgment) {
		// TODO: Implement this method
	}

	@Override
	public PreciseTimeline getTimeLine() {
		// TODO: Implement this method
		return timeLine;
	}

}
