package com.mygdx.game;

//import com.example.cepp.app2.GameView;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by cepp on 14.02.2015.
 */
public class GameAI extends Thread
{
	public long lastUpdateMillis = 0;
	public GdxGame game;
	public long logMillis = System.currentTimeMillis();
	public Boolean runAI = true;
	public int generatedThreadIdx;
	public int putMaxPlanePathes = 8;
	public Boolean generatedThreadSign = false;
	public Boolean suspended = false;

	public void pause()
	{
		suspended = true;
	}

	public void restore()
	{
		suspended = false;
	}

	public GameAI(GdxGame gdxGame)
	{
		game = gdxGame;
		log("gameAI game = [" + game.toString() + "]");
	}

	public void run()
	{
		log(
			"[gameAI.run " + game.hashCode() + " thidx:" +
				"" + generatedThreadIdx + "]");

		while(runAI)
		{
			if(System.currentTimeMillis() - lastUpdateMillis < 15 || suspended || game.planes
				.size() <= 0 || game.screenWidth == 0 || game.screenHeight == 0)
			{
				try
				{
					sleep(10 + MathUtils.random(25));
				}
				catch(InterruptedException e)
				{
					e.printStackTrace();
				}
				continue;
			}

			//for(int i = 0; i < game.planes.size(); i++)
			game.planesLock.lock();
			for(Plane pln : game.planes)
			{
				game.curX = pln.curX;
				game.curY = pln.curY;
				game.destX = pln.destX;
				game.destY = pln.destY;
				game.curAngle = pln.curAngle;
				game.destAngle = pln.destAngle;
				game.stepMove = pln.stepMove;
				game.doneY = pln.doneY;
				game.doneX = pln.doneX;
				game.stepX = pln.stepX;
				game.stepY = pln.stepY;
				game.spritePlane = pln.spritePlane;
				game.planePath = pln.planePath;
				game.startY = pln.startY;
				game.startX = pln.startX;
				game.stepMoves = pln.stepMoves;
				game.distanceTotal = pln.distanceTotal;

				game.movePlane();
				game.checkArrival();

				pln.curX = game.curX;
				pln.curY = game.curY;
				pln.destX = game.destX;
				pln.destY = game.destY;
				pln.curAngle = game.curAngle;
				pln.destAngle = game.destAngle;
				pln.stepMove = game.stepMove;
				pln.doneY = game.doneY;
				pln.doneX = game.doneX;
				pln.stepX = game.stepX;
				pln.stepY = game.stepY;
				pln.startX = game.startX;
				pln.startY = game.startY;
				pln.spritePlane = game.spritePlane;
				pln.stepMoves = game.stepMoves;
				pln.distanceTotal = game.distanceTotal;

				if(game.planePath.size() <= 1)
				{
					if(game.tipSound != null) { game.tipSound.play(0.03f, 1.5f, 0.5f); }

					putMaxPlanePathes = 3 + MathUtils.random(3);
					/*..log("#"+pln+" putMaxPlanePathes="+putMaxPlanePathes);*/
					for(int i = 0; i < putMaxPlanePathes; i++)
					{
						//if(MathUtils.randomBoolean(0.6f)) { continue; }

						float x, y;
						/*if(game.touchPos.positionX != 0f || game.touchPos.positionY != 0f)
						{
							log(
								"#" + pln.spritePlane
									.hashCode() + " game.touchPos.positionX=" + game.touchPos.positionX + "" +
									" game" +
									".touchPos.positionY=" + game.touchPos.positionY);
						}
						else{
							log("#" + pln.spritePlane
									.hashCode() +" startX "+game.startX+" startY "+game.startY);
						}*/
						x = game.startX - 35f + MathUtils.random(75f);
						y = game.startY - 35f + MathUtils.random(75f);

					/*	log("#" + pln.spritePlane
								.hashCode() +" " + i + " new positionX=" + positionX + " positionY=" + positionY);*/
						game.planePath.add(new Vector2(x, y));
					}
				}
				//game.moveRocket();
				pln.planePath = game.planePath;
			}
			game.planesLock.unlock();



			//	log("renderPlane " + (System.currentTimeMillis() - lastUpdateMillis));
			lastUpdateMillis = System.currentTimeMillis();
		}

		log("gameAI: finish");
	}

	public String getCodeThreadSign()
	{

		if(generatedThreadSign) { return game.code[generatedThreadIdx]; }
		else
		{
			generatedThreadIdx = MathUtils.random(game.code.length - 1);
			generatedThreadSign = true;
		}
		return game.code[generatedThreadIdx];
	}

	public void log(String str)

	{
		game.log(str);
		/*if(logMillis > 0)
		{
			String chr;

			chr = getCodeThreadSign();
			Gdx.app.log(
				"info", String
					.format("%-5d", System.currentTimeMillis() - logMillis) + " " + Thread.currentThread().getId() + " " + str);
		}
		logMillis = System.currentTimeMillis();*/
	}
}

