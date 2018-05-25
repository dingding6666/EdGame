package com.edplan.framework.ui.drawable.sprite;
import android.graphics.drawable.shapes.RectShape;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.buffer.BufferUtil;
import java.nio.FloatBuffer;
import com.edplan.framework.math.IQuad;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.graphics.opengl.GLWrapped;
import com.edplan.framework.graphics.opengl.objs.Color4;

public abstract class RectSprite<S extends SpriteShader> extends ObjectSprite<S>
{
	private FloatBuffer positionBuffer;
	
	private FloatBuffer colorBuffer;
	
	private IQuad area;
	
	public RectSprite(MContext c){
		super(c);
		positionBuffer=BufferUtil.createFloatBuffer(3*4);
	}
	
	public void setColor(Color4 tl,Color4 tr,Color4 bl,Color4 br){
		if(colorBuffer==null)colorBuffer=BufferUtil.createFloatBuffer(4*4);
		colorBuffer.position(0);
		bl.put2buffer(colorBuffer);
		br.put2buffer(colorBuffer);
		tr.put2buffer(colorBuffer);
		tl.put2buffer(colorBuffer);
		colorBuffer.position(0);
	}

	public void setArea(IQuad area){
		this.area=area;
		positionBuffer.position(0);
		area.getBottomLeft().put2bufferAsVec3(positionBuffer);
		area.getBottomRight().put2bufferAsVec3(positionBuffer);
		area.getTopRight().put2bufferAsVec3(positionBuffer);
		area.getTopLeft().put2bufferAsVec3(positionBuffer);
		positionBuffer.position(0);
	}

	public IQuad getArea(){
		return area;
	}

	@Override
	protected void loadVertexs(BaseCanvas canvas){
		// TODO: Implement this method
		shader.loadColor((colorBuffer!=null)?colorBuffer:BufferUtil.STD_RECT_COLOR_ONE_BUFFER);
		shader.loadSpritePositionBuffer(BufferUtil.STD_1X1_POSITION_BUFFER);
		shader.loadPositionBuffer(positionBuffer);
	}

	@Override
	protected void postDraw(BaseCanvas canvas){
		// TODO: Implement this method
		GLWrapped.drawElements(GLWrapped.GL_TRIANGLES,6,GLWrapped.GL_UNSIGNED_SHORT,BufferUtil.STD_RECT_INDICES_BUFFER);
	}
}
