package com.edplan.framework.ui.layout;

public class Param{
	public static final int SHIFT_SIZE=32;
	public static final long SIZE_MASK=(1L<<SHIFT_SIZE)-1;
	public static final long MODE_MASK=3L<<SHIFT_SIZE;

	public static final int NONE=0;
	public static final int MATCH_PARENT=1;
	public static final int WRAP_CONTENT=2;

	public static long intToLongMode(int mode){
		return ((long)mode)<<SHIFT_SIZE;
	}

	public static long makeupParam(float size,int mode){
		return intToLongMode(mode)|Float.floatToRawIntBits(size);
	}

	public static float getSize(long param){
		return Float.intBitsToFloat((int)(param&SIZE_MASK));
	}

	public static int getMode(long param){
		return (int)((param&MODE_MASK)>>>SHIFT_SIZE);
	}

	public static long adjustSize(long raw,float add){
		return makeupParam(getSize(raw)+add,getMode(raw));
	}

	public static long setSize(long raw,float size){
		return makeupParam(size,getMode(raw));
	}
}