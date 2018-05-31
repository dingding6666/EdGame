package com.edplan.osulab;

import android.app.*;
import android.os.*;
import com.edplan.framework.main.MainApplication;
import com.edplan.framework.graphics.opengl.MainRenderer;
import com.edplan.framework.MContext;
import com.edplan.nso.OsuFilePart;
import com.edplan.framework.main.EdMainActivity;
import com.edplan.osulab.ui.BackQuery;
import com.edplan.framework.ui.text.font.bmfont.BMFont;
import java.io.IOException;
import com.edplan.framework.ui.text.font.FontAwesome;
import com.edplan.framework.database.DatabaseTable;
import com.edplan.framework.database.TestDBLine;

public class LabActivity extends EdMainActivity 
{
	private LabApplication app;

	@Override
	protected void createGame(){
		// TODO: Implement this method
		app=new LabApplication();
		app.setUpActivity(this);
		
		DatabaseTable table=new DatabaseTable();
		table.initial(TestDBLine.class);
		
	}
	
	public class LabApplication extends MainApplication
	{
		@Override
		public MainRenderer createRenderer(MContext context){
			// TODO: Implement this method
			return new LabMainRenderer(context,this);
		}

		@Override
		public boolean onBackPressNotHandled(){
			// TODO: Implement this method
			return BackQuery.get().back();
		}

		@Override
		public void onExit(){
			// TODO: Implement this method
			LabGame.get().exit();
		}

		@Override
		public void onGLCreate(){
			// TODO: Implement this method
			super.onGLCreate();
			try{
				{
					BMFont font=BMFont.loadFont(
						mContext.getAssetResource().subResource("font"),
						"osuFont.fnt");
					font.setErrCharacter(FontAwesome.fa_osu_heart1_break.charvalue);
					BMFont.addFont(font,font.getInfo().face);
				}
				{
					BMFont f=BMFont.getFont(BMFont.FontAwesome);
					f.addFont(
						mContext.getAssetResource().subResource("font"),
						"osuFont.fnt");
				}
			}catch(IOException e){
				e.printStackTrace();
				mContext.toast("读取字体osuFont失败："+e.getMessage());
			}
		}
	}
}
