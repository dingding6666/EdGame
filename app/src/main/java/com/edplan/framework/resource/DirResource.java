package com.edplan.framework.resource;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;

public class DirResource extends IResource
{
	private File dir;
	
	public DirResource(File dir){
		this.dir=dir;
	}
	
	public DirResource(String absPath){
		this(new File(absPath));
	}
	
	@Override
	public InputStream openInput(String path) throws IOException {
		// TODO: Implement this method
		return new FileInputStream(new File(dir,path));
	}

	@Override
	public IResource subResource(String path) {
		// TODO: Implement this method
		return new DirResource(new File(dir,path));
	}

	@Override
	public String[] list(String rdir) throws IOException
	{
		// TODO: Implement this method
		if(rdir==null||rdir.length()==0){
			return dir.list();
		}else{
			return (new File(dir,rdir)).list();
		}
	}
}
