package com.edplan.framework.graphics.opengl.objs.texture;
import java.util.HashMap;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class TexturePool
{
	protected HashMap<String,AbstractTexture> pool=new HashMap<String,AbstractTexture>();
	
	private TextureLoader loader;
	
	public TexturePool(TextureLoader loader){
		this.loader=loader;
	}
	
	public List<MsgTexture> getAll(){
		List<MsgTexture> l=new ArrayList<MsgTexture>(pool.size());
		for(Map.Entry<String,AbstractTexture> t:pool.entrySet()){
			MsgTexture mt=new MsgTexture();
			mt.msg=t.getKey();
			mt.texture=t.getValue();
			l.add(mt);
			mt=null;
		}
		return l;
	}
	
	protected void directPut(String msg,AbstractTexture t){
		pool.put(new String(msg),t);
	}
	
	public void addAll(List<MsgTexture> list){
		for(MsgTexture t:list){
			directPut(t.msg,t.texture);
		}
	}
	
	public AbstractTexture getTexture(String msg){
		if(pool.containsKey(msg)){
			return pool.get(msg);
		}else{
			AbstractTexture t=loadTexture(msg);
			directPut(msg,t);
			return t;
		}
	}
	
	public AbstractTexture addTexture(String msg){
		AbstractTexture t=loadTexture(msg);
		directPut(msg,t);
		return t;
	}
	
	protected AbstractTexture loadTexture(String msg){
		AbstractTexture t=loader.load(msg);
		if(t==null)throw new RuntimeException("err load Texture: "+msg);
		return t;
	}
	
	//全部清除，但是不会直接释放纹理
	public void clear(){
		pool.clear();
	}
	
	public interface TextureLoader{
		public AbstractTexture load(String msg);
	}
	
	public class MsgTexture{
		public String msg;
		public AbstractTexture texture;
	}
}
