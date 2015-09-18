package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.List;

public class Rocket extends Object
{
	public static final float ROCKET_SIZE = 6.0f;
	public float curX = 0f, curY = 0f, diffX = 0f, diffY = 0f, doneY = 0f, doneX = 0f,
		stepMoves = 0f, distanceTotal = 0f;
	public int stepX, stepY;
	public float destX = 0f, destY = 0f, curAngle = 0f, destAngle = 0f;
	public float speed = 0f, fuelCurrentValue = 3.5f, stepMove = 0f;
	public GdxGame game = null;
	public Texture textureRocket = null;
	public Sprite spriteRocket = null;
	public int targetHashCode = 0;

	public Rocket(GdxGame _game, float _curX, float _curY, float x, float y)
	{
		destX = x;
		destY = y;
		curX = _curX;
		curY = _curY;

		game = _game;

		curAngle = (float) calcRotationAngleInDegrees(
			new Vector2((int) curX, (int) curY), new Vector2((int) destX, (int) destY));
	}

	public void render(ShapeRenderer shaperender, SpriteBatch batch)
	{
		if(textureRocket == null)
		{
			textureRocket = new Texture("rocket.png");
			textureRocket.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		}

		batch.setColor(Color.RED);
		batch.draw(
			textureRocket, curX + ROCKET_SIZE / 2, curY + ROCKET_SIZE / 2, 7.5f, 7.5f, 7f, 12f, 0.6f,
			0.6f, curAngle, 0, 0, textureRocket
				.getWidth(), textureRocket.getHeight(), false, false);
		/*batch.draw(
			textureRocket, curX + ROCKET_SIZE / 2, curY + ROCKET_SIZE / 2, ROCKET_SIZE, ROCKET_SIZE,
			0, 0, textureRocket
				.getWidth(), textureRocket.getHeight(), false, false);*/
	}

	public void calculatePathStep()
	{
		float prevX = diffX, prevY = diffY;
		diffX = (Math.max(curX, destX) - Math.min(curX, destX));
		diffY = (Math.max(curY, destY) - Math.min(curY, destY));

		stepX = (int) Math.round((diffX / diffY));

		stepY = (int) Math.round((diffY / diffX));

		distanceTotal = Math.abs(destX - curX) + Math.abs(destY - curY);

		/*if(prevX != diffX || prevY != diffY)
		{
			log("new stepx=" + stepX + " stepy=" + stepY);
		}*/
	}

	public boolean moveRocket()
	{
		game.planesLock.lock();
		for(Plane plane : game.planes)
		{
			if(targetHashCode == plane.hashCode())
			{
				destX = plane.curX;
				destY = plane.curY;
			}
		}
		game.planesLock.unlock();

		fuelCurrentValue -= 0.028;

		if(fuelCurrentValue <= 0f)
		{
			log("rocket crashed fuel low");
			return false;
		}

		calculatePathStep();
		/*else
		{
			log("not arived: " + Math.abs(curX - destX) + " " + Math.abs(curY - destY) +
							" curX:" + curX + " curY:" + curY + " destX:" + destX + " destY:" + destY);
		}*/

		float diffX2 = (Math.max(curX, destX) - Math.min(curX, destX));
		float diffY2 = (Math.max(curY, destY) - Math.min(curY, destY));

		if(diffX2 == diffY2)
		{
			log(
				" [path cost] new stepX " + stepX + " stepY " + stepY + " " +
					"diff sync at " +
					diffX2);
		}

		stepMove = (float) 0.05;
		if(Math.abs(curAngle - destAngle) < 300)
		{
			stepMove = (float) -0.07;
		}
		if(Math.abs(curAngle - destAngle) < 150)
		{
			stepMove = (float) -0.13;
		}
		if(Math.abs(curAngle - destAngle) < 89)
		{
			stepMove = (float) 0.25;
		}
		if(Math.abs(curAngle - destAngle) < 25)
		{
			stepMove = (float) 0.27;
		}
		if(Math.abs(curAngle - destAngle) < 5)
		{
			stepMove = (float) 0.29;
		}
		if(Math.abs(curAngle - destAngle) == 0)
		{
			stepMove = (float) 0.3;
		}

		if(curX < destX && ((doneY <= 0 || diffY2 == 0) || ((doneY > 0) && doneY > stepY)))
		{
			curX = ((curX + stepMove));
			doneX++;
			doneY = 0;
		}
		if(curX > destX && ((doneY <= 0 || diffY2 == 0) || ((doneY > 0) && doneY > stepY)))
		{

			curX = ((curX - stepMove));
			doneX++;
			doneY = 0;
		}

		if(curY < destY && ((doneX <= 0 || diffX2 == 0) || ((doneX > 0) && doneX > stepX)))
		{

			curY = ((curY + stepMove));
			doneY++;
			doneX = 0;
		}
		if(curY > destY && ((doneX <= 0 || diffX2 == 0) || ((doneX > 0) && doneX > stepX)))
		{

			curY = ((curY - stepMove));
			doneY++;
			doneX = 0;
		}

		stepMoves += stepMove;

		// rotate
		destAngle = (float) calcRotationAngleInDegrees(
			new Vector2((int) curX, (int) curY), new Vector2((int) destX, (int) destY));

		if(destAngle > curAngle)
		{
			if(Math.abs(destAngle - curAngle) >= 2)
			{
				curAngle += 1.55f;
			}
			else
			{
				curAngle += Math.abs(destAngle - curAngle);
			}
		}
		else
		{
			if(Math.abs(destAngle - curAngle) >= 2)
			{
				curAngle -= 1.55f;
			}
			else
			{
				curAngle -= Math.abs(destAngle - curAngle);
			}
		}
		//log("destAngle: " + destAngle + " curAngle: " + curAngle);
		return true;
	}

	public static double calcRotationAngleInDegrees(Vector2 centerPt, Vector2 targetPt)
	{
		// calculate the destAngle theta from the deltaY and deltaX values
		// (atan2 returns radians values from [-PI,PI])
		// 0 currently points EAST.
		// NOTE: By preserving Y and X param order to atan2,  we are expecting
		// a CLOCKWISE destAngle direction.
		double theta = Math.atan2(targetPt.y - centerPt.y, targetPt.x - centerPt.x);

		// rotate the theta destAngle clockwise by 90 degrees
		// (this makes 0 point NORTH)
		// NOTE: adding to an destAngle rotates it clockwise.
		// subtracting would rotate it counter-clockwise
		//theta = MathUtils.random(50) > 5 ? theta + Math.PI / 2.0 : theta - Math.PI / 2.0;
		theta += Math.PI / 2.0;

		// convert from radians to degrees
		// this will give you an destAngle from [0->270],[-180,0]
		double angle = Math.toDegrees(theta);

		// convert to positive range [0-360)
		// since we want to prevent negative angles, adjust them now.
		// we can assume that atan2 will not return a negative value
		// greater than one partial rotation
		if(angle < 0)
		{
			angle += 360;
		}

		return angle;
	}

	public void log(String str)
	{
		game.log(str);
	}
}
