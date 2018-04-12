package com.edplan.framework.math;
import java.util.List;
import com.edplan.framework.ui.animation.interpolate.Interplateable;
import com.edplan.framework.ui.animation.interpolate.ValueInterpolator;
import com.edplan.framework.ui.animation.interpolate.Vec2Interpolator;
import java.util.Stack;

public class Vec2 implements Interplateable<Vec2>
{
	public static final Vec2 BASE_POINT=new Vec2(0,0);
	
	public static final Stack<Vec2> cache=new Stack<Vec2>();
	
	public static final int MAX_CACHE=2000;
	
	public float x,y;
	
	public Vec2(){
		
	}
	
	public Vec2(float x,float y){
		this.x=x;
		this.y=y;
	}
	
	public Vec2(Vec2 res){
		this.x=res.x;
		this.y=res.y;
	}
	
	public Vec2(float v){
		this(v,v);
	}
	
	public Vec2 set(float x,float y){
		this.x=x;
		this.y=y;
		return this;
	}
	
	public Vec2 set(Vec2 v){
		return set(v.x,v.y);
	}
	
	public Vec2 add(Vec2 v){
		return add(v.x,v.y);
	}

	public Vec2 add(float ax,float ay){
		this.x+=ax;
		this.y+=ay;
		return this;
	}

	public Vec2 minus(Vec2 v){
		return minus(v.x,v.y);
	}

	public Vec2 minus(float dx,float dy){
		this.x-=dx;
		this.y-=dy;
		return this;
	}
	
	public Vec2 move(float offsetX,float offsetY){
		x+=offsetX;
		y+=offsetY;
		
		return this;
	}
	
	public Vec2 divide(float dx,float dy){
		this.x/=dx;
		this.y/=dy;
		return this;
	}
	
	public Vec2 multiple(Vec2 v){
		x*=v.x;
		y*=v.y;
		return this;
	}
	
	public Vec2 divide(float d){
		return divide(d,d);
	}
	
	public Vec2 zoom(float zt){
		return zoom(0,0,zt,zt);
	}
	
	public Vec2 zoom(float ox,float oy,float zoomTimesX,float zoomTimesY){
		this.x=zoomTimesX*(x-ox)+ox;
		this.y=zoomTimesY*(y-oy)+oy;
		return this;
	}
	
	public Vec2 zoom(Vec2 o,float zoomTimesX,float zoomTimesY){
		return zoom(o.x,o.y,zoomTimesX,zoomTimesY);
	}
	
	public Vec2 rotate(float r){
		float c=(float)Math.cos(r);
		float s=(float)Math.sin(r);
		float tmpX=x;
		x=x*c-y*s;
		y=y*c+tmpX*s;
		return this;
	}
	
	//顺时针，弧度
	public Vec2 rotate(Vec2 o,float r){
		return rotate(o.x,o.y,r);
	}
	
	public Vec2 rotate(float ox,float oy,float r){
		float c=FMath.cos(r);
		float s=FMath.sin(r);
		float xr=x-ox;
		float yr=y-oy;
		x=ox+xr*c-yr*s;
		y=oy+yr*c+xr*s;
		return this;
	}
	
	
	public Vec2 postMatrix(Mat2 m){
		float tmpX=x;
		x=x*m.get(0,0)+y*m.get(1,0);
		y=tmpX*m.get(1,0)+y*m.get(1,1);
		return this;
	}
	
	public Vec2 toNormal(){
		return divide(length());
	}
	
	public Vec2 toOrthogonalDirectionNormal(){
		return toNormal().rotate(FMath.PiHalf);
	}
	
	public float dot(Vec2 v){
		return this.x*v.x+this.y*v.y;
	}
	
	public float length(){
		return length(x,y);
	}
	
	public float lengthSquared(){
		return lengthSquared(x,y);
	}
	
	public float theta(){
		return FMath.atan2(y,x);
	}
	
	//public float thetaX(){
	//	return -theta();
	//}
	
	public Vec2 copy(){
		return new Vec2().set(this);
	}

	@Override
	public ValueInterpolator<Vec2> getInterpolator() {
		// TODO: Implement this method
		return Vec2Interpolator.Instance;
	}
	
	public Vec3 toVec3(float z){
		return new Vec3(this,z);
	}
	
	/*
	public static Vec2 instance(){
		if(cache.isEmpty()){
			return new Vec2();
		}else{
			return cache.pop();
		}
	}
	
	public static Vec2 instance(float x,float y){
		return instance().set(x,y);
	}
	
	public static Vec2 instance(float v){
		return instance().set(v,v);
	}
	*/
	
	public static float sizeOfTriangle(Vec2 v1,Vec2 v2,Vec2 v3){
		return (v1.x*v2.y+v2.x*v3.y+v3.x*v1.y-v1.x*v3.y-v2.x*v1.y-v3.x*v2.y)/2;
	}
	
	public static boolean shareLine(Vec2 v1,Vec2 v2,Vec2 v3,float toleration){
		return Math.abs(sizeOfTriangle(v1,v2,v3))<toleration;
	}
	
	public static boolean near(Vec2 v1,Vec2 v2,float t){
		return lengthSquared(v1,v2)<t;
	}
	
	public static Vec2 onLine(Vec2 v1,Vec2 v2,float p){
		return v1.copy().zoom(1-p).add(v2.copy().zoom(p));
	}
	
	public static Vec2 onLineLength(Vec2 v1,Vec2 v2,float l){
		return onLine(v1,v2,l/length(v1,v2));
	}
	
	public static Vec2 lineOthNormal(Vec2 ps,Vec2 pe){
		Vec2 v=pe.copy().minus(ps).toOrthogonalDirectionNormal();
		return v;
	}
	
	public static float lengthSquared(float x,float y){
		return x*x+y*y;
	}
	
	public static float lengthSquared(Vec2 v1,Vec2 v2){
		return lengthSquared(v1.x-v2.x,v1.y-v2.y);
	}
	
	public static float length(float x,float y){
		return (float)Math.sqrt(lengthSquared(x,y));
	}
	
	public static float length(Vec2 p1,Vec2 p2){
		return length(p1.x-p2.x,p1.y-p2.y);
	}
	
	public static Vec2 atCircle(float ang){
		return new Vec2((float)Math.cos(ang),(float)Math.sin(ang));
	}
	
	public static float calTheta(Vec2 start,Vec2 end){
		return FMath.atan2(end.y-start.y,end.x-start.x);
	}

	@Override
	public boolean equals(Object obj) {
		// TODO: Implement this method
		if(obj instanceof Vec2){
			Vec2 v=(Vec2)obj;
			return v.x==x&&v.y==y;
		}else{
			return false;
		}
	}

	@Override
	public String toString() {
		// TODO: Implement this method
		return (new StringBuilder("(")).append(x).append(",").append(y).append(")").toString();
	}
}
