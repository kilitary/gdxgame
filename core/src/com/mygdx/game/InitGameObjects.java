package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by cepp on 07.03.2015.
 */
public class InitGameObjects implements Runnable
{
	private GdxGame game = null;
	private static boolean running = false;

	public InitGameObjects(GdxGame gdxGame)
	{
		game = gdxGame;
		game.log("set game=" + game);
	}

	@Override
	public void run()
	{

		if(game == null || game.texturePlane == null || running == true)
		{
			game.attachingObjectsNum++;
			try
			{
				Thread.sleep(30 + MathUtils.random(100));
                return;
			}
			catch(InterruptedException exp)
			{
			}
		}

        game.log("[preloaderThread run()]");

        game.font.setScale(1.1f);
        game.centerLabel.setText("[placing planes]");
		game.attachingObjects = true;

		running = true;
		game.javaHeap = Gdx.app.getJavaHeap();
		game.nativeHeap = Gdx.app.getNativeHeap();

		game.log("[javaH(mb): " + game.javaHeap / 1024.0f / 1024.0f);
		game.log("[nativeH(mb): " + game.nativeHeap / 1024.0f / 1024.0f);

		int numPlanes = MathUtils.random(12) + 16;
		game.log("[numPlanes=" + numPlanes + "]");

		game.modePlanePath = true;

		for(int i = 0; i < numPlanes; i++)
		{
			game.hudfont.setScale(1.1f);
			game.centerLabel.setText(
				"[placing planes " + String.format(
					"%.0f", (float) i / (float) numPlanes * 100.0f) +
					"%] square=" + game.squareValue);
			game.attachingObjectsNum++;

			game.plane = new Plane();

			game.plane.stringCode = String.format("E%02d", i);

			game.plane.texturePlane = new Texture(game.texturePlane.getTextureData());
			game.plane.texturePlane
				.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			game.plane.spritePlane = new Sprite(
				new Texture(
					game.plane.texturePlane.getTextureData()));

			game.plane.startX = game.plane.curX = (float) MathUtils.random(game.screenWidth);
			game.plane.startY = game.plane.curY = (float) MathUtils.random(game.screenHeight);

			game.plane.destX = (float) game.plane.startX - 60f + MathUtils.random(120f);
			game.plane.destY = (float) game.plane.startY - 60f + MathUtils.random(120f);

			game.plane.planePath.add(
				new Vector2(game.plane.destX, game.plane.destY));

			game.planesLock.lock();
			game.planes.add(game.plane);
			game.planesLock.unlock();

			game.log(
				"[plane #" + game.planes.size() + " hash: " + String.format(
					"%X", game.plane.hashCode()) + "]");
			if(game.buttonSound != null) { game.buttonSound.play(0.1f); }

			try
			{
				Thread.sleep(50 + MathUtils.random(55));
			}
			catch(InterruptedException exp)
			{
			}
		}

		numPlanes = 4 + MathUtils.random(5);
		game.font.setScale(1.1f);
		game.centerLabel.setText("[placing sams]");

		for(int i = 0; i < numPlanes; i++)
		{
			game.attachingObjectsNum++;

			Sam sam;
			sam = new Sam(game);

			sam.stringCode = String.format("SA%02d", i);

			game.samsLock.lock();
			game.sams.add(sam);
			game.samsLock.unlock();

			if(game.buttonSound != null) { game.buttonSound.play(0.1f); }
			try
			{
				Thread.sleep(50 + MathUtils.random(110));
			}
			catch(InterruptedException exp)
			{
			}
		}

		game.modePlanePath = false;

		game.startSound.play(0.35f, 0.8f, 0.4f);

		running = false;

		game.hudfont.setScale(1.1f);
		game.centerLabel
			.setText("[playing " + game.planes.size() + " planes, " + game.sams.size() + " sams]");

		game.attachingObjects = false;
		return;
	}

	public void log(String str)
	{
		if(game == null) { return; }
		game.log(str);
	}
}
