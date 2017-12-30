package com.edplan.framework.graphics.opengl;
import com.edplan.framework.graphics.opengl.shader.advance.Texture3DShader;
import com.edplan.framework.main.GameContext;
import com.edplan.framework.MContext;
import java.io.IOException;
import com.edplan.framework.resource.IResource;
import com.edplan.framework.graphics.opengl.shader.advance.RectShader;
import com.edplan.framework.graphics.opengl.shader.advance.RoundedRectShader;

public class ShaderManager
{

	public void setRectShader(RectShader rectShader) {
		this.rectShader=rectShader;
	}

	public RectShader getRectShader() {
		return rectShader;
	}

	public void setRoundedRectShader(RoundedRectShader roundedRectShader) {
		this.roundedRectShader=roundedRectShader;
	}

	public RoundedRectShader getRoundedRectShader() {
		return roundedRectShader;
	}
	public static class PATH{
		private static final String PATH_Texture3DShader="StdTexture3DShader";
		private static final String PATH_RectShader_VS="StdRectShader.vs";
		private static final String PATH_RectShader_FS="StdRectShader.fs";
		private static final String PATH_RoundedRectShader_FS="StdRoundedRectShader.fs";
	}
	
	private static ShaderManager shaderManager;
	
	private Texture3DShader texture3DShader;
	
	private RectShader rectShader;
	
	private RoundedRectShader roundedRectShader;
	
	private IResource res;
	
	public ShaderManager(IResource _res){
		init(_res);
	}
	
	public void init(IResource _res){
		res=_res;
		loadShader();
	}
	
	private void loadShader(){
		try {
			texture3DShader=
				Texture3DShader.create(
					res.loadText(PATH.PATH_Texture3DShader + ".vs"),
					res.loadText(PATH.PATH_Texture3DShader + ".fs"));
			rectShader=
				RectShader.create(
					res.loadText(PATH.PATH_RectShader_VS),
					res.loadText(PATH.PATH_RectShader_FS));
			roundedRectShader=
				RoundedRectShader.create(
					res.loadText(PATH.PATH_RectShader_VS),
					res.loadText(PATH.PATH_RoundedRectShader_FS));
		}catch(IOException e){
			e.printStackTrace();
			throw new GLException("err load shader from asset! msg: "+e.getMessage(),e);
		}
	}
	
	public Texture3DShader getStdTexture3DShader(){
		return texture3DShader;
	}
	
	public static void initStatic(MContext context){
		shaderManager=new ShaderManager(context.getAssetResource().subResource("shaders"));
	}
	
	public static ShaderManager get(){
		return shaderManager;
	}
}
