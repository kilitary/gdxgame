package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.CpuSpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by cepp on 28.03.2015.
 */
public class StormPlace extends Object
{
    float x, y;
    public GdxGame game = null;
    float rotation = 0;
    public float sizeX, sizeY;
    public float alpha = 0;
    public long placeTime = System.currentTimeMillis();
    public long lastWeatherMillis = System.currentTimeMillis();
    public boolean rotationForward = true;
    public long startMillis = 0;

    public StormPlace(GdxGame _game, float _x, float _y)
    {
        x = _x;
        y = _y;
        game = _game;
        sizeX = 10 + MathUtils.random(28f);
        sizeY = 10 + MathUtils.random(28f);
        rotation = MathUtils.random(360f);
        alpha = 0.1f + MathUtils.random(0.2f);
        rotationForward = MathUtils.randomBoolean();
        startMillis = System.currentTimeMillis();//30000 + MathUtils.random(20000);
    }

    public void render(CpuSpriteBatch batch, ShapeRenderer shapeRenderer)
    {
        if (System.currentTimeMillis() - lastWeatherMillis >= 120)
        {
            lastWeatherMillis = System.currentTimeMillis();

            if (MathUtils.random(1200) == 5)
            {
                startMillis = System.currentTimeMillis();
               // game.log("propagated ");
                alpha = 0.6f;
            }
            if (MathUtils.random(30000.0f) < 52f)
            {
                rotationForward = !rotationForward;
               // game.log("chg rotation " + rotationForward);
            }
            if (rotationForward)//MathUtils.random(101.f) > 50f)
                rotation += 0.5f;
            else //if (MathUtils.random(101.0f) > 50f)
                rotation -= 0.5f;

            if (MathUtils.random(10110.f) > 5000f)
                alpha -= 0.01f;
            else if (MathUtils.random(11001.0f) < 5000f)
                alpha += 0.01f;

            /*if(MathUtils.random(25)==2)
            {
                sizeX = 10 + MathUtils.random(28f);
                sizeY = 10 + MathUtils.random(28f);
            }*/

            //rotation = MathUtils.random(360f);
        }

        if (alpha <= 0.01f || alpha >= 0.6f)
            alpha = 0.2f;
        if (rotation >= 360 || rotation <= 0)
            rotation = 180;


        game.spriteStorm.setPosition(x, y);
        game.spriteStorm.setRotation(rotation);
        game.spriteStorm.setSize(sizeX, sizeY);
        game.spriteStorm.setAlpha(alpha);
        game.spriteStorm.draw(batch, alpha);
        //batch.draw(game.spriteStorm, x, y);
    }
}
