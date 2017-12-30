package com.edplan.framework.graphics.opengl.shader.uniforms;
import android.opengl.GLES20;
import com.edplan.framework.graphics.opengl.GLException;
import com.edplan.framework.graphics.opengl.shader.DataUniform;
import com.edplan.framework.graphics.opengl.shader.GLProgram;

public class UniformFloat implements DataUniform<Float>
{
	private String name;

	private int handle;

	private GLProgram program;

	public UniformFloat(){
		
	}

	@Override
	public void loadData(Float t){
		GLES20.glUniform1f(getHandle(),t);
	}

	@Override
	public int getHandle() {
		// TODO: Implement this method
		return handle;
	}

	@Override
	public GLProgram getProgram() {
		// TODO: Implement this method
		return program;
	}
	
	public static UniformFloat findUniform(GLProgram program,String name){
		UniformFloat um=new UniformFloat();
		um.handle=GLES20.glGetUniformLocation(program.getProgramId(),name);
		um.program=program;
		um.name=name;
		//if(um.handle==-1)throw new GLException("handle "+name+" NOT found");
		return um;
	}
}
