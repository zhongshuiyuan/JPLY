package com.otitan.gylyeq.drawTool;

import com.esri.core.map.Graphic;

import java.util.EventObject;

/**
 * 自定义通用事件
 */
public class DrawEvent extends EventObject {

	private static final long serialVersionUID = 1L;

	public static final int DRAW_END = 1;

	private Graphic drawGrahic;
	private int type;

	public DrawEvent(Object source, int type, Graphic drawGrahic) {
		super(source);
		this.drawGrahic = drawGrahic;
		this.type = type;
	}

	public Graphic getDrawGraphic() {
		return drawGrahic;
	}

	public int getType() {
		return type;
	}

}
