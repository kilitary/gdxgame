package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.List;

public class Plane extends Object
{
	public float curX, curY, destX, destY, startX, startY, curAngle, destAngle;
	public float rotation, stepMove = 0, stepMoves = 0;
	public float stepX = 0, stepY = 0, diffX = 0, diffY = 0;
	public float doneX = 0, doneY = 0, fuelCurrentValue = 30f, fuelAtCourseStart = 0;
	public List<Vector2> planePathExhaust = new ArrayList<Vector2>();
	public List<Vector2> planePath = new ArrayList<Vector2>();
	public Vector2 nextTargetPos;
	public Texture texturePlane;
	public Sprite spritePlane;
	public GdxGame game;
	public float distanceTotal;
	public Boolean surfaceRadarOn = false, airRadarOn = false;
	public static int planesCount = 0;
	public String stringCode = "";

	public void Plane(GdxGame mygdxgame)
	{
		game = mygdxgame;
		planesCount++;
		log("Plane #"+ planesCount +" "+hashCode());
	}

	public void log(String str)
	{
		game.log(str);
	}
}
