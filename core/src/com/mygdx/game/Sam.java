package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class Sam extends Object
{
    public float positionX = 0f, positionY = 0f;
    public static float airRadarRange = 72f, surfaceToAirRange = 43f;
    public static GdxGame game;
    public Texture textureSam = null;
    public Sprite spriteSam = null;
    public Boolean goodPlace = false;
    public float prevX = 0, prevY = 0, scaleX = 0, scaleY = 0;
    public Pixmap surfacePixMap = null, pixTemp = null;
    public int numtryes = 0;
    public int i = 0;
    public Boolean xFlipped = false;
    public long millisAtFire = 0;
    public String stringCode = "";

    public int squareAmount(float x, float y, int neededSquare)
    {
        game.squareValue = 0;
        for (int i = (int) x; i < x + neededSquare; i++)
        {
            for (int u = (int) y; u < y + neededSquare; u++)
            {
                Color color = new Color();
                Color.rgba8888ToColor(color, surfacePixMap.getPixel((int) i, (int) u));
                if (color == null)
                {
                    break;
                }

                if (color.g == 0.0f && color.r == 0.0f && color.b == 0.0f)
                {
                    game.squareValue++;
                }
                /*surfacePixMap.setColor(Color.RED);

				surfacePixMap.drawPixel(
					(int) i, (int) u, 111);
				//surfacePixMap.

				Texture text;
				Sprite spr;
				//	text = new Texture(surfacePixMap);
				Gdx.app.postRunnable(
					new Runnable()
					{
						@Override
						public void run()
						{
							game.spriteMap.set(new Sprite(new Texture(surfacePixMap, false)));
						}
					});
*/
				/*game.spriteMap = new Sprite(new Texture(surfacePixMap.getTextureData()));
				game.textureMapSource = new Texture(game.textureMap.getTextureData());*/
            }
        }
        //	log("square("+x+","+y+","+neededSquare+"): "+game.squareValue);
        return game.squareValue;
    }

    public Sam(GdxGame gm)
    {
        int sv = 0;
        //log("[Sam)("+gm+")]");
        game = gm;

        goodPlace = false;
        numtryes = 0;
        surfacePixMap = new Pixmap(game.screenWidth, game.screenHeight, Pixmap.Format.RGBA8888);
        surfacePixMap.drawPixmap(
                game.surfacePixMap, 0, 0, game.surfacePixMap.getWidth(), game.surfacePixMap
                        .getHeight(), 0, 0, game.screenWidth, game.screenHeight);
        game.centerLabel.setText(
                "[placing sams ]");
        //log("[Sam surfacePixMap done]");
        game.font.setScale(1.1f);
        do
        {
            i++;
            game.attachingObjectsNum++;
            positionX = 100f + MathUtils.random(game.screenWidth - 100f);
            positionY = 50f + MathUtils.random(game.screenHeight - 50f);
            Color color = new Color();
            Color.rgb888ToColor(color, surfacePixMap.getPixel((int) positionX, (int) positionY));
            if (color == null)
            {
                break;
            }
            if (color.g == 0.0f && color.r == 0.0f && color.b == 0.0f)
            {
                goodPlace = true;
            } else
            {
                goodPlace = false;
            }
            //log("g:"+color.g+" r:"+color.r+" b:"+color.b);

            sv = squareAmount(positionX, positionY, (int) airRadarRange);
            game.squareValue = sv;
            if (goodPlace && sv <= airRadarRange)
            {
                goodPlace = false;
                continue;
            }

            game.samsLock.lock();
            for (Sam sam : game.sams)
            {
                if ((Math.abs(sam.positionX - positionX) <= airRadarRange || Math
                        .abs(sam.positionY - positionY) <= airRadarRange))
                {
                    goodPlace = false;
                }
            }
            game.samsLock.unlock();

            if (MathUtils.random(11) == 1)
            {
                game.font.setScale(1.1f);
                game.centerLabel.setText(
                        "[placing sam #" + String.format("%02d", game.sams.size() + 1) + " " + String
                                .format(

                                        "%.0f", (((float) i / 1000f) * 100.0f)) + "% square=" + game.squareValue + " " +
                                "gp" +
                                ":" +
                                "" + goodPlace + "]");
            }

		/*	try
			{

				Thread.sleep(MathUtils.random(1));
			}
			catch(InterruptedException exp)
			{
			}*/
        } while (goodPlace != true && numtryes++ < 100);

	/*	prevX = positionX;
		prevY = positionY;*/
        xFlipped = MathUtils.randomBoolean();

        log(
                "new sam at #" + numtryes + " " + positionX + "@" + positionY + " surface:" + (goodPlace
                        ? "success" : "failed") + " square:" + sv);
    }


    public void checkPlaneInAAZone()
    {
        if (textureSam == null)
        {
            return;
        }

        game.planesLock.lock();
        for (Plane plane : game.planes)
        {
            if (System.currentTimeMillis() - millisAtFire >= 1000 * 8 && game.isPointInCircle(
                    positionX, positionY, surfaceToAirRange, plane.curX, plane.curY))
            {
                log(stringCode + " targets " + plane.stringCode);

                game.rocketLaunchSound.play(0.1f, 0.6f+MathUtils.random(0.5f),
                        0.6f+MathUtils.random(0.5f));

                Rocket rocket = new Rocket(
                        game, positionX, positionY, plane.curX, plane.curY);

                rocket.targetHashCode = plane.hashCode();
                game.rocketsLock.lock();
                game.rockets.add(rocket);
                game.rocketsLock.unlock();

                millisAtFire = System.currentTimeMillis();
            }
        }
        game.planesLock.unlock();
    }

    public void render(int idx, SpriteBatch batch, ShapeRenderer shapeRenderer)
    {
        if (game == null)
        {
            return;
        }

        Gdx.gl20.glLineWidth(0.05f);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.circle(positionX, positionY, airRadarRange);

        shapeRenderer.setColor(Color.RED);
        Gdx.gl20.glLineWidth(0.05f);
        shapeRenderer.circle(positionX, positionY, surfaceToAirRange);

        if (textureSam == null)
        {
            textureSam = new Texture("sam.png");
            textureSam.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        batch.setColor(Color.WHITE);

        batch.draw(
                textureSam, positionX - 15.0f / 2, positionY - 15.0f / 2, 15.0f, 15.0f, 0, 0, textureSam
                        .getWidth(), textureSam.getHeight(), xFlipped, false);

        game.font.setColor(Color.WHITE);
        CharSequence str = String.format("AA%02d", idx);
        game.font.setScale(0.6f);
        game.font.draw(batch, str, positionX - 13f, positionY - 8f);
    }

    public void log(String str)
    {
        if (game == null)
        {
            return;
        }
        game.log(str);
    }
}
