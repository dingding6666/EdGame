package com.edplan.framework.ui.text.font.bmfont;
import com.edplan.superutils.U;

/**
 *文档：
 *http://www.angelcode.com/products/bmfont/doc/file_format.html#bin
 */
public class FNTChar
{
	public static final String HEAD="char";
	public static final String ID="id";
	public static final String X="x";
	public static final String Y="y";
	public static final String WIDTH="width";
	public static final String HEIGHT="height";
	public static final String XOFFSET="xoffset";
	public static final String YOFFSET="yoffset";
	public static final String XADVANCE="xadvance";
	public static final String PAGE="page";
	public static final String CHNL="chnl";
	
	public final char id;
	public final int x;
	public final int y;
	public final int width;
	public final int height;
	public final int xoffset;
	public final int yoffset;
	public final int xadvance;
	public final int page;
	public final int chnl;
	
	public FNTChar(
		char id,
		int x,
		int y,
		int width,
		int height,
		int xoffset,
		int yoffset,
		int xadvance,
		int page,
		int chnl
	){
		this.id=id;
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.xoffset=xoffset;
		this.yoffset=yoffset;
		this.xadvance=xadvance;
		this.page=page;
		this.chnl=chnl;
	}

	@Override
	public String toString() {
		// TODO: Implement this method
		
		return super.toString();
	}
	
	public static FNTChar parse(String fntline){
		String[] split=fntline.split(" ");
		if(split.length!=11){
			throw new IllegalArgumentException("a char line must has 11 parts : "+fntline);
		}
		char id=(char)FNTHelper.parseInt(ID,1,split[1]);
		int x=FNTHelper.parseInt(X,1,split[2]);
		int y=FNTHelper.parseInt(Y,1,split[3]);
		int width=FNTHelper.parseInt(WIDTH,1,split[4]);
		int height=FNTHelper.parseInt(HEIGHT,1,split[5]);
		int xoffset=FNTHelper.parseInt(XOFFSET,1,split[6]);
		int yoffset=FNTHelper.parseInt(YOFFSET,1,split[7]);
		int xadvance=FNTHelper.parseInt(XADVANCE,1,split[8]);
		int page=FNTHelper.parseInt(PAGE,1,split[9]);
		int chnl=FNTHelper.parseInt(CHNL,1,split[10]);
		return new FNTChar(
			id,x,y,width,height,xoffset,yoffset,xadvance,page,chnl
		);
	}
}