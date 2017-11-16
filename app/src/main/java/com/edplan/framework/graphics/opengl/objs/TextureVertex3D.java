package com.edplan.framework.graphics.opengl.objs;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.math.Vec3;

public class TextureVertex3D extends Vertex3D
{
	public Vec2 texturePoint;
	
	public TextureVertex3D(Vec3 p,Vec2 tp){
		super(p);
		this.texturePoint=tp;
	}

	@Override
	public TextureVertex3D setColor(Color4 color) {
		this.color=color;
		return this;
	}

	public Color4 getColor() {
		return color;
	}

	public TextureVertex3D setTexturePoint(Vec2 texturePoint) {
		this.texturePoint=texturePoint;
		return this;
	}

	public Vec2 getTexturePoint() {
		return texturePoint;
	}

	@Override
	public Vertex3D setPosition(Vec3 position) {
		// TODO: Implement this method
		this.position=position;
		return this;
	}
}
