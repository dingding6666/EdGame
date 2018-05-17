package com.edplan.framework.graphics.opengl.buffer.direct;

import com.edplan.framework.graphics.opengl.buffer.BufferUtil;
import com.edplan.framework.graphics.opengl.shader.VertexAttrib;
import java.nio.IntBuffer;
import java.util.Arrays;

public class DirectIntAttributeBuffer
{
	public int[] ary;
	public IntBuffer buffer;
	public IntPointer[] pointers;

	public VertexAttrib attributePointer;

	public DirectIntAttributeBuffer(int size){
		ensureSize(size);
	}

	public void ensureSize(int size){
		if(ary==null){
			ary=new int[size];
			buffer=BufferUtil.createIntBuffer(size);
			pointers=new IntPointer[size];
			for(int i=0;i<pointers.length;i++){
				pointers[i]=new ThisPointer(i);
			}
		}else if(pointers.length<size){
			ary=Arrays.copyOf(ary,size);
			buffer=BufferUtil.createIntBuffer(size);
			int i=pointers.length;
			pointers=Arrays.copyOf(pointers,size);
			for(;i<pointers.length;i++){
				pointers[i]=new ThisPointer(i);
			}
		}
	}

	public class ThisPointer extends IntPointer
	{
		private final int x;

		public ThisPointer(int idx){
			x=idx;
		}

		@Override
		public void set(int v){
			// TODO: Implement this method
			ary[x]=v;
		}
	}
}
