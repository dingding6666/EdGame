package com.edplan.mygame;

import android.util.Log;
import com.edplan.framework.MContext;
import com.edplan.framework.graphics.layer.BufferedLayer;
import com.edplan.framework.graphics.opengl.GLCanvas2D;
import com.edplan.framework.graphics.opengl.GLPaint;
import com.edplan.framework.graphics.opengl.GLWrapped;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.math.RectF;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.media.bass.BassChannel;
import com.edplan.framework.resource.AResource;
import com.edplan.framework.resource.DirResource;
import com.edplan.framework.timing.AudioTimeline;
import com.edplan.framework.timing.PreciseTimeline;
import com.edplan.framework.ui.Anchor;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.text.font.bmfont.BMFont;
import com.edplan.framework.ui.text.font.drawing.TextPrinter;
import com.edplan.framework.utils.MLog;
import com.edplan.nso.NsoException;
import com.edplan.nso.parser.StdBeatmapDecoder;
import com.edplan.nso.parser.StoryboardDecoder;
import com.edplan.nso.resource.OsuSkin;
import com.edplan.nso.ruleset.amodel.playing.PlayField;
import com.edplan.nso.ruleset.std.StdBeatmap;
import com.edplan.nso.ruleset.std.playing.StdPlayField;
import com.edplan.nso.ruleset.std.playing.StdPlayingBeatmap;
import com.edplan.nso.ruleset.std.playing.drawable.DrawableStdHitCircle;
import com.edplan.nso.storyboard.PlayingStoryboard;
import com.edplan.nso.storyboard.Storyboard;
import java.io.File;
import java.io.IOException;
import org.json.JSONObject;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.test.Test;
import com.edplan.framework.graphics.opengl.CanvasUtil;
import com.edplan.nso.storyboard.TBVStoryboard;

public class TestView extends EdView
{
	private StdPlayField playField;

	private StdBeatmap beatmap;

	private StdPlayingBeatmap playingBeatmap;

	private Storyboard storyboard;

	private PlayingStoryboard playingStoryboard;

	private PreciseTimeline timeline;
	
	private PreciseTimeline renderLine;
	
	private OutputStoryboard outputSb;

	private BassChannel audio;

	private OsuSkin skin;

	private Vec2 point=new Vec2();

	//private DrawableStdSlider sld2;

	private BufferedLayer newLayer;

	private GLCanvas2D newCanvas;
	
	TBVStoryboard tbvp;


	private GLTexture testPng;
	private GLTexture cardPng;
	private GLTexture sliderTex;

	private DrawableStdHitCircle firstObj;

	float a=0;
	long lt=0;

	int delt=0;

	BMFont font;

	TestData test;

	GLTexture cursor;

	public TestView(MContext context){
		super(context);

	}

	@Override
	public void onCreate(){
		Test.get().setBoolean(Test.IS_RELEASE,false);
		test=new TestData();
		try {
			AResource res=getContext().getAssetResource().subResource("font");
			font=BMFont.loadFont(
				res,
				//			"Exo2.0-Regular.fnt");

				"Noto-CJK-Basic.fnt");
			//font.setErrCharacter(BMFont.CHAR_NOT_FOUND);
			font.addFont(res,"Noto-Basic.fnt");
			font.setErrCharacter('⊙');
			TextPrinter.addFont(font,"default");

			cursor=getContext().getAssetResource().subResource("osu/skins/default").loadTexture("cursor.png");

		} catch (IOException e) {
			e.printStackTrace();
		}


		System.out.println(test.getBeatmapPath());
		System.out.println(test.getSongPath());

		try {


			skin=new OsuSkin();
			skin.load(
				getContext()
				.getAssetResource()
				.subResource("osu")
				.subResource("skins")
				.subResource("default"));

			StdBeatmapDecoder bparser=new StdBeatmapDecoder(
				//getContext()
				//.getAssetResource()
				//.subResource("osu/test/beatmap")
				test.testFloder().openInput(
					//"t+pazolite with siromaru - Chambarising (Bloodmoon) [Stream Practice (160)].osu"
					//"Suzuki Konomi - Cyber Thunder Cider (Nattu) [Niat's Cider].osu"
					//"Petit Rabbit's - No Poi! (walaowey) [[ -Scarlet- ]'s Extra].osu"
					//"UNDEAD CORPORATION - The Empress (Plutia) [STARBOW BREAK!].osu"
					//"Mai Zang - Si Ye Cao De Huang Xiang (Axarious) [Despair].osu"
					//"Osu!Droid Tieba Challenge - Evolution Pack (Believer_zzr) [Future Cider].osu"
					//"Petit Rabbit's - No Poi! (walaowey) [[ -Scarlet- ]'s Extra].osu"
					//"Yueporu feat. Hatsune Miku - Kurikaeshi Hitotsubu (Zweib) [Hitotsubu].osu"
					//"1/Chino (CV.Minase Inori) - Okashina yume o Ohitotsu douzo (- Skanbis -) [Asuka_-'s insane].osu"
					//"2/Halozy - Kikoku Doukoku Jigokuraku (Hollow Wings) [Notch Hell].osu"
					//"3/IAHN - Transform (Original Mix) (Monstrata) [Aspire].osu"
					//"4/IOSYS - Marisa wa Taihen na Mono wo Nusunde Ikimashita (DJPop) [TAG4].osu"
					//"5/Luo Tianyi - Xiao Ji Bibi ~ Remix ~ (MoleAkarin) [Bi~bi~].osu"
					//"6/xi - FREEDOM DiVE (Nakagawa-Kanon) [FOUR DIMENSIONS].osu"
					//"7/Panda Eyes & Teminite - Highscore (Fort) [Game Over].osu"
					//"8/Helblinde - The Solace of Oblivion (ProfessionalBox) [Aspire].osu"
					//"9/Knife Party - Centipede (Sugoi-_-Desu) [This isn't a map, just a simple visualisation].osu"
					test.getBeatmapPath()
				),
				"test case beatmap: " 
			);

			audio=BassChannel.createStreamFromFile(
				test.dir+"/"+
				//.createStreamFromResource(getContext().getAssetResource(),"osu/test/beatmap/"+
				//.createStreamFromAsset(getContext(),"osu/test/beatmap/"+
				//"test.mp3"
				//"cloverfantasy.mp3"
				//"audio.mp3"
				//"hust.mp3"
				//"No Poi!.mp3"
				//"1/audio.mp3"
				//"2/Kikoku Doukoku Jigokuraku.mp3"
				//"3/audio.mp3"
				//"4/Marisa wa Taihen na Mono wo Nusunde Ikimashita.mp3"
				//"5/bibibi.mp3"
				//"6/Freedom Dive.mp3"
				//"7/Teminite & Panda Eyes - Highscore.mp3"
				//"8/audio.mp3"
				//"9/02-knife_party-centipede.mp3"
				test.getSongPath()
			);
			try
			{
				Log.v("parse-osu","start parse");
				bparser.parse();
				Log.v("parse-osu","end parse");
				beatmap=bparser.makeupBeatmap(StdBeatmap.class);
				beatmap.getDifficulty().setCircleSize(beatmap.getDifficulty().getCircleSize());
				timeline=new AudioTimeline(audio);
				//new PreciseTimeline();
				//new AdjustableTimeline(1/5f);
				playingBeatmap=new StdPlayingBeatmap(getContext(),beatmap,timeline,skin);
				Log.v("osu","objs: "+playingBeatmap.getHitObjects().size()+" first: "+playingBeatmap.getHitObjects().get(0).getStartTime());
				playField=new StdPlayField(getContext(),timeline);
				playField.applyBeatmap(playingBeatmap);

				
				if(test.playTBV){
					tbvp=new TBVStoryboard(getContext(),new File("/storage/emulated/0/MyDisk/bin/tbv/test1.tbv"),timeline);
				}else//# osb test
				if(test.enableStoryboard){
					File osb=new File(//"/storage/emulated/0/ADM/470977 Mili - world.execute(me)/Mili - world.execute(me); (Exile-).osb");
						//"/storage/emulated/0/ADM/186911 Function Phantom - Neuronecia2/Function Phantom - Neuronecia (Amamiya Yuko).osb");
						//"/storage/emulated/0/ADM/151720 ginkiha - EOS1/ginkiha - EOS (alacat).osb");
						//"/storage/emulated/0/ADM/389179 Jay Chou - Fa Ru Xue3/Jay Chou - Fa Ru Xue (KaedekaShizuru).osb");
						"/storage/emulated/0/osu!droid/Songs/470977 Mili - world.execute(me)/Mili - world.execute(me); (Exile-).osb");
					StoryboardDecoder decoder=new StoryboardDecoder(
						//osb);
						test.testFloder().openInput(test.getOsbPath()),"ahh");

					try {
						long s=System.currentTimeMillis();
						decoder.parse();
						storyboard=decoder.getStoryboard();
						if(beatmap!=null&&test.applyBeatmap){
							storyboard.applyBeatmap(beatmap);
						}
						System.out.println("end edcode osb: "+storyboard.getObjectCount());
						if(test.outputTBV){
							renderLine=new PreciseTimeline();
							playingStoryboard=new PlayingStoryboard(getContext(),renderLine,storyboard,
																	//new DirResource(osb.getParentFile()));
																	test.testFloder().subResource(test.testBeatmapFloder+""));
							outputSb=new OutputStoryboard(playingStoryboard);
						}else{
							playingStoryboard=new PlayingStoryboard(getContext(),timeline,storyboard,
																	//new DirResource(osb.getParentFile()));
																	test.testFloder().subResource(test.testBeatmapFloder+""));
						}
						//new DirResource(osb.getParentFile()));
						System.out.println("end transform to playing state");
						System.out.println("storyboard parse done in: "+(System.currentTimeMillis()-s)+"ms");

						JSONObject jobj=new JSONObject("{\"a\":100}");
						System.out.println(jobj.toString());


						/*
						 for(int i=0;i<5;i++){
						 BaseDrawableSprite spr=(BaseDrawableSprite) playingStoryboard.getLayer(Storyboard.Layer.Foreground.name()).getSprites().get(i);
						 System.out.println(spr.getAnimations());
						 }
						 */

					} catch (Exception e) {
						e.printStackTrace();
					}

					//# end osb test
				}
			}
			catch (NsoException e)
			{
				e.printStackTrace();
				throw new RuntimeException("err",e);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}


			testPng=skin.hitcircle.getRes();
			/*
			 GLTexture.decodeStream(
			 context
			 .getAssetResource()
			 .getTextureResource()
			 .openInput
			 ("ic_launcher.png"));*/
			cardPng=GLTexture.decodeResource(getContext().getAssetResource().getTextureResource(),
											 "card.jpg");
			//"default-bg.png");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	boolean ifend=false;
	double renderTime=0;
	@Override
	public void draw(BaseCanvas canvas) {
		// TODO: Implement this method
		super.draw(canvas);
		renderTime+=getContext().getFrameDeltaTime();
		int audioOffset=(int)(timeline.frameTime()-audio.currentPlayTimeMS());

		a+=0.005;
		float c=Math.abs(a%2-1);

		MLog.test.runOnce("test-play", new Runnable(){
				@Override
				public void run()
				{
					// TODO: Implement this method
					post(new Runnable(){
							@Override
							public void run()
							{
								// TODO: Implement this method
								getContext().getUiLooper().addLoopableBeforeDraw(timeline);
								audio.play();
								//audio.seekTo(42000);
								//timeline.onLoop(103000*5);
							}
						},2000);
				}
			});



		canvas.drawColor(Color4.gray(0.0f));
		//new Color4(c,c,c,1.0f));
		canvas.clearBuffer();

		float dh=50;
		GLPaint paint=new GLPaint();
		//paint.setColorMixRate(1f);
		paint.setFinalAlpha(1f);
		paint.setMixColor(Color4.rgba(1,1,1,1));
		paint.setPadding(10);
		paint.setVaryingColor(Color4.rgba(1,1,1,1));
		paint.setRoundedRadius(30);
		paint.setGlowFactor
		//(0.1f);
		(0.9f+c*0.3f*0);
		paint.setGlowColor(Color4.rgba(0.3f,0.3f,0.3f,0.9f));
		lt=System.currentTimeMillis();


		//c=0.3f;


		//转换到osu field
		if(test.playTBV){
			tbvp.draw(canvas);
		}else if(test.outputTBV){
			float startX=700;
			float startY=500;
			float width=500;
			float str=25;
			float padding=5;
			GLPaint tp=new GLPaint();
			tp.setVaryingColor(Color4.Red);
			TextPrinter tpt=new TextPrinter(font,startX,startY+str+padding+5,tp);
			tpt.setTextSize(70);
			tpt.printString((int)(outputSb.progress()*100)+"% "+outputSb.finished());
			GLPaint pbp=new GLPaint();
			pbp.setStrokeWidth(str+padding);
			pbp.setVaryingColor(Color4.White);
			canvas.drawLine(startX-padding,startY,startX+padding+width,startY,pbp);
			pbp.setStrokeWidth(str);
			pbp.setVaryingColor(Color4.rgba(0,1,1,1));
			canvas.drawLine(startX,startY,startX+width*(float)outputSb.progress(),startY,pbp);
			tpt.draw(canvas);
		}else{
			drawOsu(canvas);
		}

		float deltaTime=(float)getContext().getFrameDeltaTime();
		GLPaint ntp=new GLPaint();
		ntp.setStrokeWidth(1.2f);
		//ntp.setColorMixRate(1);
		ntp.setMixColor(Color4.White);
		canvas.save();
		float lengthScale=5;
		ntp.setMixColor(Color4.rgba(0,1,0,1));
		canvas.drawLine(10,50,10+18*6,50,ntp);
		ntp.setMixColor(Color4.rgba(1,1,0,1));
		canvas.drawLine(10,55,10+deltaTime*lengthScale,55,ntp);
		ntp.setMixColor(Color4.White);
		for(int i=timelist.length-1;i>0;i--){
			timelist[i]=timelist[i-1];
		}
		timelist[0]=deltaTime;
		for(int i=1;i<timelist.length;i++){
			canvas.drawLine(10,55+6*i,10+timelist[i]*lengthScale,55+6*i,ntp);
		}
		float avg=0;
		float max=0;
		float min=99999;
		float avg_nogc=0;
		int count_nogc=0;
		for(float t:timelist){
			avg+=t;
			if(t<min){
				min=t;
			}
			if(t>max){
				max=t;
			}
			if(t<400){
				avg_nogc+=t;
				count_nogc++;
			}
		}
		avg_nogc/=count_nogc;
		avg/=timelist.length;
		ntp.setStrokeWidth(3);
		ntp.setMixColor(Color4.rgba(1,0,0,1));
		canvas.drawLine(10+avg*lengthScale,40,10+avg*lengthScale,60+6*timelist.length,ntp);
		//ntp.setStrokeWidth(2);
		//ntp.setMixColor(Color4.rgba(0,1,0,1));
		//canvas.drawLine(10+max*lengthScale,40,10+max*lengthScale,60+6*timelist.length,ntp);
		ntp.setStrokeWidth(2);
		ntp.setMixColor(Color4.rgba(0,0,1,1));
		canvas.drawLine(10+min*lengthScale,40,10+min*lengthScale,60+6*timelist.length,ntp);
		ntp.setStrokeWidth(3);
		ntp.setMixColor(Color4.rgba(0.5f,0.5f,1,1));
		canvas.drawLine(10+avg_nogc*lengthScale,40,10+avg_nogc*lengthScale,60+6*timelist.length,ntp);
		canvas.restore();

		//canvas.drawTexture(cardPng,RectF.xywh(0,0,canvas.getWidth(),canvas.getHeight()),newPaint);


		canvas.setCanvasAlpha(1f);
		float baseLine=300;
		GLPaint textPaint=new GLPaint();
		textPaint.setMixColor(Color4.rgba(1,0,0,1));
		TextPrinter printer=new TextPrinter(font,100,baseLine,textPaint);
		printer.setTextSize(60);
		printer.printString("DEVELOPMENT BUILD\nosu!lab 2018.4.9");
		printer.toNextLine();
		printer.printString("fps: "+(int)(1000/avg)+"/"+(int)(1000/avg_nogc));
		printer.toNextLine();
		printer.printString(timeline.frameTime()+"");
		printer.toNextLine();
		printer.printString("draws:"+drawCalls+"/"+drawCalls2);
		printer.toNextLine();
		printer.printString("memory:"+Runtime.getRuntime().freeMemory()/1024/1024+"/"+Runtime.getRuntime().totalMemory()/1024/1024+"/"+Runtime.getRuntime().maxMemory()/1024/1024);
		printer.toNextLine();
		if(test.enableStoryboard){
			printer.printString(playingStoryboard.objectsInField()+"\n");
			printer.printString("new: "+playingStoryboard.newApply()+"");
		}
		printer.toNextLine();
		printer.printString(timeline.animationCount()+"");
		//printer.setTextSize(50);
		//printer.printString("osu!lab");
		//printer.toNextLine();
		//printer.toNextLine();
		//printer.printString("D̨Á̶̢T̛͝͡AÈ̶R͢͢Ŕ͡0R͟͟");
		printer.draw(canvas);
		GLPaint baseLinePaint=new GLPaint();
		baseLinePaint.setMixColor(Color4.rgba(1,0,0,0.6f));
		baseLinePaint.setStrokeWidth(2);
		//baseLinePaint.setColorMixRate(1);
		canvas.drawLine(0,baseLine,1000,baseLine,baseLinePaint);
		baseLinePaint.setMixColor(Color4.rgba(0,1,0,0.6f));
		canvas.drawLine(0,baseLine+font.getCommon().base*printer.getScale(),1000,baseLine+font.getCommon().base*printer.getScale(),baseLinePaint);

		/*
		Vec2 v=TestStaticData.touchPosition.copy();
		v.zoom(canvas.getWidth(),canvas.getHeight(),2,2);
		canvas.drawTexture(cursor,RectF.anchorOWH(Anchor.Center,v.x,v.y,100,100),new GLPaint());
		*/

	}
	
	public void drawOsu(BaseCanvas canvas){
		canvas.save();
		
		if(newLayer==null){
			newLayer=new BufferedLayer(getContext(),canvas.getDefWidth(),canvas.getDefHeight(),true);
			newCanvas=new GLCanvas2D(newLayer);
		}
		newCanvas.prepare();
		int state=newCanvas.save();
		newCanvas.drawColor(Color4.gray(0f));
		newCanvas.clearDepthBuffer();


		float osuScale=PlayField.BASE_Y/canvas.getHeight();
		newCanvas.translate(newCanvas.getWidth()/2-PlayField.BASE_X/2/osuScale,0);
		newCanvas.scaleContent(osuScale);
		newCanvas.translate(PlayField.PADDING_X,PlayField.PADDING_Y);
		newCanvas.clip(new Vec2(PlayField.CANVAS_SIZE_X,PlayField.CANVAS_SIZE_Y));

		GLPaint tp=new GLPaint();
		tp.setVaryingColor(Color4.rgb(1,1,1));
		//tp.setColorMixRate(1);
		tp.setStrokeWidth(5);
		tp.setFinalAlpha(1);
		//newCanvas.drawLines(new float[]{0,0,PlayField.CANVAS_SIZE_X,PlayField.CANVAS_SIZE_Y},tp);


		newCanvas.save();
		String toStoryboardLayer=
			CanvasUtil.buildOperation()
			.translate(-PlayField.PADDING_X,-PlayField.PADDING_Y).toString();
		CanvasUtil.operateCanvas(newCanvas,toStoryboardLayer);
		//newCanvas.translate(-PlayField.PADDING_X,-PlayField.PADDING_Y);
		//newCanvas.translate(-(PlayField.BASE_Y*16/9f-PlayField.BASE_X)/2,0);
		newCanvas.setCanvasAlpha(1f);
		if(test.enableStoryboard&&!test.watchPool)playingStoryboard.draw(newCanvas);
		newCanvas.setCanvasAlpha(1);
		//newCanvas.drawTexture(GLTexture.ErrorTexture,RectF.xywh(200,200,100,100),Color4.ONE,1);
		newCanvas.restore();

		drawCalls=GLWrapped.frameDrawCalls();
		drawCalls2=newCanvas.getDrawCalls();

		if(test.enablePlayField&&!test.watchPool)playField.draw(newCanvas);
		//firstObj.draw(newCanvas);
		//newCanvas.drawTexture(GLTexture.ErrorTexture,RectF.xywh(100,100,100,100),Color4.ONE,1);
		//newCanvas.drawTexture(GLTexture.ErrorTexture,RectF.xywh(200,200,100,100),Color4.ONE,0.5f);
		newCanvas.unprepare();
		newCanvas.restoreToCount(state);
		//paint.setColorMixRate(0);

		GLPaint newPaint=new GLPaint();
		//newPaint.setFinalAlpha(postAlpha);
		//newPaint.setMixColor(new Color4(1,0.5f,0.5f,1));
		AbstractTexture texture=newLayer.getTexture();
		//canvas.drawTexture(texture,new RectF(0,0,texture.getWidth(),texture.getHeight()),new RectF(0,0,canvas.getWidth(),canvas.getHeight()),newPaint);
		//canvas.drawTextureAnchorCenter(testPng,new Vec2(canvas.getWidth()/2,canvas.getHeight()/2),new Vec2(canvas.getWidth()/2,canvas.getHeight()/2),newPaint);


		//canvas.drawTexture(GLTexture.ErrorTexture,RectF.xywh(200,200,100,100),Color4.ONE,1);
		canvas.drawTextureAnchorCenter(texture,new Vec2(canvas.getWidth()/2,canvas.getHeight()/2),new Vec2(canvas.getWidth()/2,canvas.getHeight()/2),newPaint);
		//canvas.drawTexture(GLTexture.Black,RectF.xywh(0,0,canvas.getWidth(),canvas.getHeight()),newPaint);
		//newLayer.recycle();

		canvas.restore();
		
	}

	int drawCalls,drawCalls2;
	public float[] timelist=new float[40];


	public class TestData{
		public String testPath="osu/test/beatmap";

		public String dir="/storage/emulated/0/MyDisk/WorkBench/bin/testdata";

		public int testBeatmapFloder=11;

		public boolean enableStoryboard=true;

		public boolean enablePlayField=false;
		
		public boolean outputTBV=false;
		
		public boolean playTBV=false;

		public boolean watchPool=false;

		public boolean applyBeatmap=true;

		public AResource res=new DirResource("/storage/emulated/0/MyDisk/WorkBench/bin/testdata");

		public String getBeatmapPath(){
			try {
				String[] list=res.list(testBeatmapFloder + "");
				for(String s:list){
					System.out.println(s);
					if(s.endsWith(".osu")){
						return testBeatmapFloder+"/"+s;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		public String getSongPath(){
			try {
				String[] list=res.list(testBeatmapFloder + "");
				for(String s:list){
					if(s.endsWith(".mp3")){
						return testBeatmapFloder+"/"+s;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		public String getOsbPath(){
			try {
				String[] list=res.list(testBeatmapFloder + "");
				for(String s:list){
					if(s.endsWith(".osb")){
						return testBeatmapFloder+"/"+s;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		public AResource testFloder(){
			return res;
		}

	}


}
