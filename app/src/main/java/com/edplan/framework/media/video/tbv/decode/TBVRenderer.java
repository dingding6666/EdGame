package com.edplan.framework.media.video.tbv.decode;
import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.media.video.tbv.TBV;
import com.edplan.framework.media.video.tbv.TextureBasedVideo;
import java.io.IOException;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.media.video.tbv.element.DataDrawBaseTexture;
import com.edplan.framework.graphics.opengl.BlendType;
import com.edplan.framework.graphics.opengl.GLWrapped;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.graphics.layer.BufferedLayer;
import java.io.InputStream;
import java.io.DataInputStream;
import org.json.JSONException;
import com.edplan.framework.media.video.tbv.TBVException;
import com.edplan.framework.MContext;
import com.edplan.framework.media.video.tbv.TBVJson;
import com.edplan.framework.graphics.opengl.CanvasUtil;
import com.edplan.framework.resource.AResource;
import com.edplan.framework.interfaces.Reflection;
import com.edplan.framework.media.video.tbv.TextureNode;

public class TBVRenderer
{
	private boolean hasReacheEndFrame=false;
	
	private TBVInputStream in;
	
	private GLTexture[] textures;
	
	private TBV.Header header;
	
	private TBV.FrameHeader frameHeader;
	
	private TBV.EventHeader eventHeader;
	
	private float currentPlayTime=0;
	
	private BufferedLayer layer;
	
	private GLCanvas2D canvas;
	
	private boolean frameIsRead=false;
	
	private MContext context;
	
	public TBVRenderer(InputStream in){
		this.in=new TBVInputStream(new DataInputStream(in));
		
	}
	
	public void initial(MContext context,final AResource res) throws JSONException, TBVException, IOException{
		initial(context, new Reflection<String,GLTexture>(){
				@Override
				public GLTexture invoke(String t) {
					// TODO: Implement this method
					try {
						return res.loadTexture(t);
					} catch (IOException e) {
						e.printStackTrace();
						return GLTexture.ErrorTexture;
					}
				}
			});
	}
	
	public void initial(MContext context,Reflection<String,GLTexture> loader) throws JSONException, TBVException, IOException{
		this.context=context;
		header=TBV.Header.read(in,header);
		textures=new GLTexture[header.textures.length];
		for(TextureNode msg:header.textures){
			textures[msg.id]=loader.invoke(msg.texture);
		}
		layer=new BufferedLayer(context,header.width,header.height,true);
		canvas=new GLCanvas2D(layer);
		String canvasOperation=header.jsonData.optString(TBVJson.OperatCanvas);
		if(canvasOperation!=null&&!canvasOperation.isEmpty()){
			CanvasUtil.operateCanvas(canvas,canvasOperation);
		}
	}
	
	public AbstractTexture getResult(){
		return layer.getTexture();
	}
	
	public void postFrame(float currentPlayTime){
		if(hasReacheEndFrame)return;
		this.currentPlayTime=currentPlayTime;
		try {
			handlerFrame();
		} catch (IOException e) {
			e.printStackTrace();
			onErr(e);
		}
	}
	
	protected void handlerFrame() throws IOException{
		if(frameHeader!=null&&frameHeader.startTime>currentPlayTime){
			return;
		}
		handlerNewFrame(false);
	}
	
	protected void handlerNewFrame(boolean forceClear) throws IOException{
		frameHeader=frameIsRead?TBV.FrameHeader.read(in, frameHeader):frameHeader;
		frameIsRead=false;
		switch(frameHeader.flag){
			case TBV.Frame.START_FRAME:
			case TBV.Frame.NORMAL_FRAME:{
				if(currentPlayTime>frameHeader.endTime){
					skipCurrentFrame();
					handlerNewFrame(forceClear||frameHeader.clearCanvas);
				}
				renderNewFrame(forceClear);
			}break;
			case TBV.Frame.END_FRAME:{
					hasReacheEndFrame=true;
					return;
			}
			default:return;
		}
	}
	
	private void skipCurrentFrame() throws IOException{
		if(!frameIsRead){
			in.skip(frameHeader.blockSize);
			frameIsRead=true;
		}
	}
	
	protected void renderNewFrame(boolean forceClear) throws IOException{
		frameIsRead=true;
		if(frameHeader.clearCanvas||forceClear){
			canvas.clearBuffer();
		}
		if(frameHeader.blockSize==0)return;
		
		in.mark(frameHeader.blockSize);
		
		try{
			eventHeader=TBV.EventHeader.read(in,eventHeader);
			switch(eventHeader.eventType){
				case TBV.FrameEvent.CANVAS_OPERATION:{
					operateCanvas();
				}break;
				case TBV.FrameEvent.CHANGE_SETTING:{
					changeSetting();
				}break;
				case TBV.FrameEvent.DRAW_BASE_TEXTURE:{
					drawBaseTexture();
				}break;
				case TBV.FrameEvent.DRAW_COLOR_VERTEX:{
					drawColorVertex();
				}break;
			}
		}catch(IOException ie){
			ie.printStackTrace();
			throw ie;
		}catch(Exception e){
			e.printStackTrace();
			onErr(e);
			frameIsRead=false;
			in.reset();
		}
	}
	
	
	protected void operateCanvas(){
		
	}
	
	TBV.SettingEvent settingEvent;
	protected void changeSetting() throws IOException{
		settingEvent=TBV.SettingEvent.read(in,settingEvent);
		switch(settingEvent.type){
			case TBV.SettingEvent.CHANGE_BLEND_TYPE:{
				changeBlend(settingEvent.data);
			}break;
		}
	}
	
	public void changeBlend(byte[] data){
		boolean enable=data[0]!=0;
		BlendType type=BlendType.values()[data[1]];
		GLWrapped.blend.set(enable,type);
	}
	
	DataDrawBaseTexture dataDrawBaseTexture;
	protected void drawBaseTexture() throws IOException{
		dataDrawBaseTexture=DataDrawBaseTexture.read(in,dataDrawBaseTexture);
		canvas.drawTexture3DBatch(dataDrawBaseTexture,textures[dataDrawBaseTexture.textureId]);
	}
	
	protected void drawColorVertex(){
		
	}
	
	public void onErr(Throwable e){
		
	}
}
