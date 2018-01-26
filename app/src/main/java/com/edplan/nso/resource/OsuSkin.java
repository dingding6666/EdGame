package com.edplan.nso.resource;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.resource.IResource;
import com.edplan.nso.resource.annotation.AResPath;
import com.edplan.nso.resource.annotation.AResType;
import com.edplan.nso.ruleset.std.playing.IComboColorGenerater;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import com.edplan.nso.ruleset.std.playing.BaseComboColorGenerater;
import com.edplan.framework.graphics.opengl.objs.Color4;

public class OsuSkin
{
	private HashMap<String,ResourceInfo> resourceDictionary=new HashMap<String,ResourceInfo>();
	
	@AResType(ResType.TEXTURE)
	@AResPath("hitcircle.png")
	public TextureInfo hitcircle;
	
	@AResType(ResType.TEXTURE)
	@AResPath("hitcircleoverlay.png")
	public TextureInfo hitcircleOverlay;
	
	@AResType(ResType.TEXTURE)
	@AResPath("approachcircle.png")
	public TextureInfo approachCircle;
	
	
	public IComboColorGenerater comboColorGenerater;
	
	//public TextureDrawable hitcircleDrawable;
	
	public OsuSkin(){
		initial();
	}
	
	public void initial(){
		comboColorGenerater=
			BaseComboColorGenerater
				 .createBy(
				  Color4.rgb(1,0.8f,0.8f),
				  Color4.rgb(0.8f,1,0.8f),
				  Color4.rgb(0.8f,0.8f,1),
				  Color4.rgb(1,1,1));
		try{
			Class klass=OsuSkin.class;
			Field[] fields=klass.getFields();
			for(Field f:fields){
				//Log.v("load","check "+f);
				if(f.isAnnotationPresent(AResType.class)){
					//Log.v("load","check ok. "+f);
					//Log.v("load","parse annotation. "+f);
					AResType type=(AResType)f.getAnnotation(AResType.class);
					AResPath path=(AResPath)f.getAnnotation(AResPath.class);
					f.setAccessible(true);
					switch(type.value()){
						case TEXTURE:
							f.set(this,new TextureInfo(path.value()));
							//Log.v("load","create info {"+path+"}. "+f);
							break;
						default:break;
					}
					resourceDictionary.put(f.getName(),(ResourceInfo)f.get(this));
				}
			}
		}catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new OsuResException("reflect err illaccs: "+e.getMessage(),e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new OsuResException("reflect err illagr: "+e.getMessage(),e);
		}
	}
	
	public void load(IResource res){
		try{
			Class klass=OsuSkin.class;
			Field[] fields=klass.getFields();
			for(Field f:fields){
				if(f.isAnnotationPresent(AResType.class)){
					AResType type=(AResType)f.getAnnotation(AResType.class);
					switch(type.value()){
						case TEXTURE:
							f.setAccessible(true);
							TextureInfo info=(TextureInfo)f.get(this);
							try {
								loadTextureRes(info, res);
							} catch (IOException e) {
								e.printStackTrace();
								throw new OsuResException("res io err : "+e.getMessage(),e);
							}
							break;
						default:break;
					}
				}
			}
		}catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new OsuResException("reflect err illaccs: "+e.getMessage(),e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new OsuResException("reflect err illagr: "+e.getMessage(),e);
		}
	}
	
	public void loadTextureRes(TextureInfo info,IResource res) throws IOException{
		info.setTexture(GLTexture.decodeResource(res,info.getPath()));
	}
}
