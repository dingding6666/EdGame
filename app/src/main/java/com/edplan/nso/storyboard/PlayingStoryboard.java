package com.edplan.nso.storyboard;
import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.timing.PreciseTimeline;
import com.edplan.framework.ui.drawable.EdDrawable;
import com.edplan.nso.storyboard.StoryboardLayer;
import java.util.HashMap;
import java.util.Map;
import com.edplan.framework.resource.AResource;
import com.edplan.framework.graphics.opengl.objs.texture.TexturePool;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import java.io.IOException;
import com.edplan.framework.resource.BufferedListResource;

public class PlayingStoryboard extends EdDrawable
{
	private HashMap<String,PlayingStoryboardLayer> layers=new HashMap<String,PlayingStoryboardLayer>();
	
	private PreciseTimeline timeline;
	
	private BufferedListResource resource;
	
	private TexturePool pool;
	
	public PlayingStoryboard(MContext context,PreciseTimeline timeline,Storyboard storyboard,AResource resource){
		super(context);
		this.resource=new BufferedListResource(resource);
		this.resource.setIgnoreCase(true);
		//new BufferedListResource(resource.subResource("SB/Font")).printDetails();
		//this.resource.printDetails();
		this.timeline=timeline;
		pool=new TexturePool(new TexturePool.TextureLoader(){
				@Override
				public AbstractTexture load(String msg) {
					// TODO: Implement this method
					try {
						return PlayingStoryboard.this.resource.loadTexture(msg);
					} catch (IOException e) {
						e.printStackTrace();
						//return null;
						throw new RuntimeException(e.getMessage());
					}
				}
		});
		for(Map.Entry<String,StoryboardLayer> l:storyboard.getLayers().entrySet()){
			layers.put(l.getKey(),new PlayingStoryboardLayer(l.getValue(),this));
		}
	}
	
	public PlayingStoryboardLayer getLayer(String key){
		return layers.get(key);
	}
	
	public int objectsInField(){
		int c=0;
		for(PlayingStoryboardLayer l:layers.values()){
			c+=l.objectInField();
		}
		return c;
	}
	
	public TexturePool getTexturePool(){
		return pool;
	}

	public void setTimeline(PreciseTimeline timeline) {
		this.timeline=timeline;
	}

	public PreciseTimeline getTimeline() {
		return timeline;
	}

	@Override
	public void draw(GLCanvas2D canvas) {
		// TODO: Implement this method
		for(Map.Entry<String,PlayingStoryboardLayer> e:layers.entrySet()){
			e.getValue().draw(canvas);
		}
	}
}
