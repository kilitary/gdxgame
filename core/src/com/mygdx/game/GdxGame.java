package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.CpuSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

//import java.awt.Point;
//import sun.rmi.runtime.Log;
///import android.util.Log;
public class GdxGame extends ApplicationAdapter
{
    public boolean stormMode = false;
    public static final int MAX_PATH_NODES = 36;
    public static final int FRAME_DELAY_MILLIS = 15;
    public static final float fileLoadingSize = 19.0f;
    public String code[] = {"!", "@", "#", "$", "%", "*", "+", "=", "r", "s", "d", "o", "l", "i",
            "k", "m", "n", "a", "b", "c", "e", "d", "t"};
    public CpuSpriteBatch batch = null;
    public static Texture texturePlane = null, textureLine = null, texturePlaneSource = null;
    public static Texture textureMap = null, textureFileLoading = null, textureSam = null,
            textureMapSource = null;
    public static Texture textureStorm = null, textureMark = null, textureSiegeline = null, textureRocket = null;
    public static Sprite spriteSiegeline = null, spriteStorm = null, spritePlane = null, spriteRocket = null, spriteFileLoading = null,
            spriteSam = null, spriteLine = null, spriteMap = null, spriteMark = null;
    public OrthographicCamera camera = null;
    public float x, y, destY = 0, destX = 0, curX, curY, stepMoves = 0;
    public float rotation, stepMove = 0, startX = 250f, startY = 500f, curAngle = 0, destAngle = 0;
    public float stepX = 0, stepY = 0, diffX = 0, diffY = 0;
    public float doneX = 0, doneY = 0, fuelCurrentValue = 30f, fuelAtCourseStart = 0;
    public Rectangle bucket = new Rectangle();
    public long nanosAtMove = 0, millisAtMoveShapeRenderer = 0;
    public ShapeRenderer shapeRenderer = null;
    public BitmapFont font = null, hudfont = null;
    public ReentrantLock planePathExhaustLock = new ReentrantLock();
    public ReentrantLock planesLock = new ReentrantLock();
    public ReentrantLock samsLock = new ReentrantLock();
    public ReentrantLock rocketsLock = new ReentrantLock();
    public ReentrantLock objectsLock = new ReentrantLock();
    public List<Vector2> planePathExhaust = new ArrayList<Vector2>();
    public List<Vector2> planePath = new ArrayList<Vector2>();
    public static GdxGame instance = null;
    public int pubint = 13;
    public float currentAlphaValue = 0f;
    public int generatedThreadIdx;
    public Boolean generatedThreadSign = false;
    public Boolean alphaDirectionForward = true;
    public Stage stage = null;
    public TextureAtlas atlas = null;
    public Table table = null;
    public Boolean modePlanePath = false;
    public Skin skin = null;
    public Button radarButton = null, stormButton = null, exitButton = null, debugButton = null,
            restartButton = null, modeButton4 = null;
    public float distanceTotal = 0f;
    public TextButton.TextButtonStyle textButtonStyle = null;
    public Vector2 nextTargetPos = new Vector2();
    public int skippedRenders = 0;
    public int screenHeight, screenWidth;
    public long logMillis = System.currentTimeMillis();
    public Vector3 touchPos = null;
    public Pixmap pixmap = null;
    public long millisAtTouch = 0;
    public Sound tipSound = null, rocketLaunchSound = null, buttonSound = null, startSound = null;
    public static ArrayList<Plane> planes = new ArrayList<Plane>();
    public static ArrayList<Sam> sams = new ArrayList<Sam>();
    public static ArrayList<Rocket> rockets = new ArrayList<Rocket>();
    public static GameAI gameAI = null;
    public static Plane plane = null, planeRadared = null;
    public long javaHeap, nativeHeap;
    public static GdxGame game;
    public Thread preloaderThread = null;
    public Sam testSam;
    public int idx = 0;
    public Pixmap pixMap = null, surfacePixMap = null;
    public Boolean attachingObjects = false;
    public int attachingObjectsNum = 0;
    public Label centerLabel = null;
    public int squareValue = 0;
    public static ArrayList<StormPlace> objects = new ArrayList<StormPlace>();
    public StormPlace stormPlace = null;

    @Override
    public void create()
    {
        super.create();
        log("GdxG4m3.create(): Gdx.app.getVersion[" + Gdx.app.getVersion() + "]");

        if (MathUtils.randomBoolean() == true)
        {
            log("く-------[boundary]----------------------------------------------------------");
            log(" ,---- fck l4n8 0r d13 ！！！ ッッ ---,");
            log("[ 中実に す ッシュ ー にェる 存在  ブブク うかど ジ ハのオ 'ルが' を確 認 際 ト, ルか ル.] - chg TX d[ns]");
            log("   ^く-- " + Thread.currentThread() + "-------^");
            log("くくくくくく っっっっっっだらね！！！！ ッッジ");
            log("--------[boundary]---------------------------------------------------------っっ>");
        }

        // unsorted init
        game = this;

        initializeCore();
        initializeSounds();
        initializeTextures();
        initializeButtons();
        initializeObjects();

        // start background shit
        preloaderThread = new Thread(new InitGameObjects(this));
        preloaderThread.start();

        gameAI = new GameAI(this);
        gameAI.start();
    }

    public void initializeCore()
    {
        pubint = MathUtils.random(20);

        screenWidth = Gdx.app.getGraphics().getWidth();
        screenHeight = Gdx.app.getGraphics().getHeight();

        log(
                "scr33n width=" + screenWidth +
                        " height=" + screenHeight);

        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        batch = new CpuSpriteBatch(5460);

        log("loading ShapeRenderer");
        shapeRenderer = new ShapeRenderer(11400);
        shapeRenderer.setAutoShapeType(true);

        camera = new OrthographicCamera();

        //atlas = new TextureAtlas("ui/hgj.pack");
        log("loading atlas");
        atlas = new TextureAtlas("ui/new.atlas");
        log("loading skin");
        skin = new Skin(atlas);
    }

    public void initializeObjects()
    {
        return;
    }

    public void initializeSounds()
    {
        tipSound = Gdx.audio.newSound(Gdx.files.internal("snd/c4_click.wav"));
        buttonSound = Gdx.audio.newSound(Gdx.files.internal("snd/button.wav"));
        startSound = Gdx.audio.newSound(Gdx.files.internal("snd/tutor_msg.wav"));
        rocketLaunchSound = Gdx.audio.newSound(Gdx.files.internal("snd/critical.wav"));
        //log("[ " + tipSound + " " + buttonSound + " " + startSound + "]");
    }

    public void initializeTextures()
    {
        pixMap = new Pixmap(Gdx.files.internal("worldmap3.jpg"));
        surfacePixMap = new Pixmap(Gdx.files.internal("surfacemap.png"));
        //	pixMap = new Pixmap(surfacePixMap);

        textureStorm = new Texture("line.png");
        textureStorm.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        spriteStorm = new Sprite(textureStorm);
        spriteStorm.setSize(6.4f, 4f);
        // spriteStorm.setColor(Color.CYAN);
        spriteStorm.setOriginCenter();

        textureSiegeline = new Texture("arbitrary_waveform.png");
        textureSiegeline.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        spriteSiegeline = new Sprite(textureSiegeline);
        spriteSiegeline.setSize(6.4f, 4f);
        spriteSiegeline.setColor(Color.CYAN);
        spriteSiegeline.setOriginCenter();

        textureRocket = new Texture("rocket_sign.png");
        textureRocket.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        spriteRocket = new Sprite(textureRocket);
        spriteRocket.setSize(9f, 9f);
        spriteRocket.setColor(Color.RED);
        spriteRocket.setOriginCenter();

        textureFileLoading = new Texture("file.png");
        textureFileLoading.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        spriteFileLoading = new Sprite(textureFileLoading);
        spriteFileLoading.setSize(35f, 35f);
        spriteFileLoading.setColor(Color.GREEN);
        spriteFileLoading.setOriginCenter();

        texturePlaneSource = new Texture("e3.png");
        texturePlaneSource.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        //texturePlaneSource.bind();
        texturePlane = new Texture(texturePlaneSource.getTextureData());
        //texturePlane.bind();
        texturePlane.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        textureMap = new Texture("worldmap3.jpg");//worldmap3.jpg
        textureMap.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textureMapSource = new Texture(textureMap.getTextureData());
        spriteMap = new Sprite(new Texture(textureMap.getTextureData()));
        spriteMap.setSize(screenWidth, screenHeight);
        spriteMap.setBounds(0f, 0f, screenWidth, screenHeight);
        spriteMap.setOriginCenter();

        //log("[initializeTextures spriteMap=" + spriteMap.toString() + "]");

        textureMark = new Texture("tgt.png");
        textureMark.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        //	log("[initializeTextures textureMark=" + textureMark.toString() + "]");
        spriteMark = new Sprite(textureMark);
        spriteMark.setSize(2.0f, 2.0f);
        spriteMark.scale(0.4f);
        //spriteMark.setScale(2.0f,2.0f);
        spriteMark.setOriginCenter();
        //	log("[initializeTextures spriteMark=" + spriteMark.toString() + "]");

        textureLine = new Texture("markdest.png");
        textureLine.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        spriteLine = new Sprite(textureLine);
        //spriteLine.setColor(Color.WHITE);
        spriteLine.setSize(10f, 10f);
        spriteLine.setOriginCenter();
        //log("[initializeTextures spriteLine=" + spriteLine + "]");
    }

    public void initializeButtons()
    {
        font = new BitmapFont();
        font.setColor(Color.RED);
        font.getRegion().getTexture().setFilter(
                Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        hudfont = new BitmapFont();
        hudfont.setColor(Color.RED);
        hudfont.getRegion().getTexture().setFilter(
                Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        textButtonStyle = new TextButton.TextButtonStyle();

        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("light_green");
        textButtonStyle.down = skin.getDrawable("light_red");

        stormButton = new TextButton("storm", textButtonStyle);
        stormButton.addListener(
                new InputListener()
                {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                    {
                        log("stormButton");
                        //camera.lookAt(1000f,100f,200f);
                        //camera.project(new Vector3(500f, 500f, 1f));
                        //camera.setToOrtho(false, 500f, 500f);
                        //camera.translate(200f,200f);
                        stormMode = !stormMode;
                        return false;
                    }
                });

        // exit
        exitButton = new TextButton("exit", textButtonStyle);
        exitButton.addListener(
                new InputListener()
                {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                    {
                        log("exitbutton");

                        Gdx.app.exit();
                        return false;
                    }
                });

        radarButton = new TextButton("mode", textButtonStyle);

        radarButton.pad(11);

        radarButton.addListener(
                new InputListener()
                {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                    {
                        modePlanePath = !modePlanePath;
                        log("radarButton Pressed, modePlanePath=" + modePlanePath);
                        radarButton.setChecked(modePlanePath);
                        buttonSound.play(1.1f);

                        if (modePlanePath)
                        {
                            radarButton.getStyle().up = skin.getDrawable("light_red");
                            radarButton.getStyle().down = skin.getDrawable("light_green");
                        } else
                        {
                            radarButton.getStyle().up = skin.getDrawable("light_green");
                            radarButton.getStyle().down = skin.getDrawable("light_red");
                        }
                        return false;
                    }

                    public void touchUp(InputEvent event, float x, float y, int pointer, int button)
                    {
                        log("radarButton Released");
                    }
                });

        //	log("[ radarButton=" + radarButton + "]");

        // restart button
        textButtonStyle = new TextButton.TextButtonStyle();

        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("light_green");
        textButtonStyle.down = skin.getDrawable("light_red");

        restartButton = new TextButton("restart", textButtonStyle);

        restartButton.pad(1);

        restartButton.addListener(
                new InputListener()
                {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                    {
                        log("restart");

                        planesLock.lock();
                        planes.clear();
                        planesLock.unlock();

                        samsLock.lock();
                        sams.clear();
                        samsLock.unlock();

                        preloaderThread = new Thread(new InitGameObjects(game));
                        new Thread(preloaderThread).start();

                        buttonSound.play(1.1f);

                        return false;
                    }

                    public void touchUp(InputEvent event, float x, float y, int pointer, int button)
                    {
                        log("restartButton Released");
                    }
                });


        // debug button
        textButtonStyle = new TextButton.TextButtonStyle();
        //log("[ TextButtonStyle=" + textButtonStyle + "]");
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("light_green");
        textButtonStyle.down = skin.getDrawable("light_red");

        debugButton = new TextButton("debug", textButtonStyle);

        debugButton.pad(1);

        debugButton.addListener(
                new InputListener()
                {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                    {

                        log("debugButton Pressed");

                        buttonSound.play(1.1f);

                        javaHeap = Gdx.app.getJavaHeap();
                        nativeHeap = Gdx.app.getNativeHeap();
                        log("javaHeap(mb): " + javaHeap / 1024.0f / 1024.0f);
                        log("nativeHeap(mb): " + nativeHeap / 1024.0f / 1024.0f);
                        log("planes: " + planes.size());
                        return false;
                    }

                    public void touchUp(InputEvent event, float x, float y, int pointer, int button)
                    {
                        log("debugButton Released");
                    }
                });

        table = new Table(skin);

        table.defaults().width(50f).height(50f);
        table.setFillParent(true);

        Label.LabelStyle lblstyle;//= new Label.LabelStyle();
        font = new BitmapFont();
        font.setScale(1.1f);
        lblstyle = new Label.LabelStyle(font, Color.WHITE);
        centerLabel = new Label("[init]", lblstyle);
        table.left().bottom();

        table.add(radarButton);
        table.add().width(20f);
        table.add(restartButton);
        table.add().width(20f);
        table.add(debugButton);
        table.add().width(20f);
        table.add(stormButton);
        table.add().width(20f);
        table.add(exitButton);
        table.add().width(20f);
        table.add(centerLabel);
        //table.add().width(90f);

        //table.setBounds(0f, 0f, 100f, 100f);

        //table.debugAll();
        //		table.padLeft(12f);
        stage.addActor(table);
    }

    @Override
    public void render()
    {
        if (System.currentTimeMillis() - millisAtMoveShapeRenderer >= FRAME_DELAY_MILLIS && planes
                .size() > 0 && camera != null && batch != null && shapeRenderer != null)
        {
            millisAtMoveShapeRenderer = System.currentTimeMillis();
        } else
        {
            skippedRenders++;
            try
            {
                Thread.sleep(10 + MathUtils.random(50));
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return;
        }

        if (Gdx.input.isTouched() && Gdx.input.justTouched() && System
                .currentTimeMillis() - millisAtTouch >= 100 && Gdx.input.getX() > 80f && Gdx.input
                .getY() > 80f)
        {
            millisAtTouch = System.currentTimeMillis();
            touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (modePlanePath)
            {
                touchPos.y = touchPos.y;

                if (planePath.size() <= 0)
                {
                    planePath.add(new Vector2(destX, destY));
                }

                planePath.add(new Vector2(touchPos.x + 7f, touchPos.y + 7f));
            } else if (stormMode == true)
            {

                stormPlace = new StormPlace(this, touchPos.x, touchPos.y);
                stormPlace.startMillis = System.currentTimeMillis();
                objectsLock.lock();
                objects.add(stormPlace);
                objectsLock.unlock();
            } else
            {

                Color color = new Color();
                ByteBuffer pixels = pixMap.getPixels();
                /*Gdx.gl.glReadPixels(
                    (int) positionX, (int) positionY, surfacePixMap.getWidth(), surfacePixMap
						.getHeight(), GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, pixels);*/
                //surfacePixMap.getPixel()
                Color.rgb565ToColor(
                        color, pixMap.getPixel(
                                (int) Gdx.input.getX(), (int) Gdx.input.getY()));
                //pixelColor = (int) (color.r+color.g+color+color.b);
                int pixelColor = Color.toIntBits(
                        (int) color.r, (int) color.g, (int) color.b, (int) color.a);
                log("color: r=" + color.r + " g=" + color.g + " b=" + color.b);
                log("pixelColor=" + pixelColor);
                //log("buffer=" + (float)pixels[(float)((float)positionX *(float) positionY + 3f)]);

			/*	setActiveCourse(touchPos.positionX, touchPos.positionY);
                planePath.clear();
				Gdx.input.vibrate(150);
				planePath.add(new Vector2(touchPos.positionX, touchPos.positionY));*/
            }
        }

        Gdx.gl.glClearColor(1, 1, 1, 1);
        //Gdx.gl.glClear(GL20.GL_STENCIL_BUFFER_BIT);
        Gdx.gl.glClear(
                GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics
                        .getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

		/*Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		*/
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.disableBlending();
        spriteMap.draw(batch);
        batch.end();

        stage.act();
        stage.draw();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.identity();

        //for(int i = 0; i < planes.size(); i++)
        idx = 0;
        if (planes.size() > 0)
        {
            planesLock.lock();
            for (Plane pln : planes)
            {
                renderPlane(pln);
                idx++;
            }
            planesLock.unlock();
        }

        if (sams.size() > 0)
        {
            int idx = 0;
            samsLock.lock();
            batch.begin();
            batch.enableBlending();
            for (Sam sam : sams)
            {
                sam.checkPlaneInAAZone();
                sam.render(idx++, batch, shapeRenderer);
            }
            batch.end();
            samsLock.unlock();
        }

        if (rockets.size() > 0)
        {
            rocketsLock.lock();
            int idx = -1;
            for (Rocket rocket : rockets)
            {
                if (rocket.moveRocket() == false)
                {
                    idx = rockets.indexOf(rocket);

                    continue;
                }
                batch.begin();
                batch.enableBlending();
                rocket.render(shapeRenderer, batch);
                batch.end();
            }
            if (idx != -1)
            {
                rockets.remove(idx);
            }
            rocketsLock.unlock();
        }

        if (attachingObjects)
        {
            if (attachingObjectsNum % 2 == 1)
            {
                batch.begin();
                batch.enableBlending();
                spriteFileLoading.setX(screenWidth - spriteFileLoading.getWidth() - 10f);
                spriteFileLoading.setY(10f);
                spriteFileLoading.setSize(fileLoadingSize, fileLoadingSize);
                spriteFileLoading.setColor(Color.WHITE);
                spriteFileLoading.draw(batch, 1.0f);
                batch.end();
            }
        }

        shapeRenderer.end();
        //

        //drawPlaneSmoke();

        //	Gdx.gl.glDisable(GL20.GL_BLEND);

    }

    public boolean checkPlaneInOtherRadarZone(Plane plane)
    {

        planesLock.lock();
        for (Plane plns : planes)
        {
            if (plane.hashCode() == plns.hashCode())
                continue;

            if (isPointInCircle(
                    plns.curX + 7.5f, plns.curY + 7.5f, Sam.airRadarRange / 2,
                    curX + 7.5f, curY + 7.5f))
            {
                planeRadared = plns;
                planesLock.unlock();
                return true;
            }
        }
        planesLock.unlock();
        return false;
    }

    public void renderPlane(Plane plane)
    {

        curX = plane.curX;
        curY = plane.curY;
        destX = plane.destX;
        destY = plane.destY;
        stepX = plane.stepX;
        stepY = plane.stepY;
        doneY = plane.doneY;
        doneX = plane.doneX;
        curAngle = plane.curAngle;
        destAngle = plane.destAngle;
        //texturePlane = pln.texturePlane;
        spritePlane = plane.spritePlane;
        planePath = plane.planePath;
        stepMoves = plane.stepMoves;
        distanceTotal = plane.distanceTotal;


        batch.begin();
        batch.enableBlending();

        StormPlace toRemovePlace = null;
        objectsLock.lock();
        for (StormPlace place : objects)
        {
            place.render(batch, shapeRenderer);
            if (System.currentTimeMillis() - place.startMillis >= 95000)
            {
                //log("removing " + place);
                toRemovePlace = place;
            }/* else
            {
                log("ostalos " + (System.currentTimeMillis() - place.startMillis));
            }*/
        }
        objects.remove(toRemovePlace);
        objectsLock.unlock();

        int planePathSize = planePath.size();
        for (int i = 0; planePathSize > 0 && i < planePathSize - 1; i++)
        {
            Vector2 nextVector, currentVector;
            try
            {
                currentVector = planePath.get(i).cpy();
                nextVector = planePath.get(i + 1).cpy();
            } catch (IndexOutOfBoundsException exc)
            {
                log("planePathSize index out of bounds at " + i);
                continue;
            }
            currentVector.x += 5f;
            currentVector.y += 5f;
            nextVector.x += 5f;
            nextVector.y += 5f;

            float angle = (float) calcRotationAngleInDegrees(
                    new Vector2(currentVector.x, currentVector.y), new Vector2(
                            nextVector.x, nextVector.y));
            spriteLine.setPosition(nextVector.x, nextVector.y);
            spriteLine.setSize(12f, 12f);
            spriteLine.setRotation(angle);

            float alpha = 0.5f;//((1f - (curX / nextVector.x) + (curY / nextVector.y) * (distanceTotal) /
            //100f)) % 1.0f;
/*

			if(alpha < 0.1f) { alpha = 0.1f; }
			if(alpha > 1.0f) { alpha %= 1.0f; }
			//log(i + " alpha: " + alpha);
			if((distanceTotal <= 20f || modePlanePath == true) && MathUtils.random(175) <= 1)
			{
				alpha += 0.9f - alpha;//0.8f;
			}
*/

            spriteLine.setColor(Color.WHITE);
            if (isPointInCircle(
                    plane.curX, plane.curY, 23f, currentVector.x, currentVector.y) && (modePlanePath ||
                    plane.surfaceRadarOn == true))
            {
                //spriteLine.setColor(Color.YELLOW);
                alpha = 0.8f;

                if (modePlanePath)
                {
                    shapeRenderer.setColor(Color.CYAN);
                    shapeRenderer.line(
                            new Vector2(plane.curX + 5f, plane.curY + 5f), new Vector2(
                                    destX, destY));
                }
            }

            if (i == 0)
            {
                alpha = 1.0f;
            }

            spriteLine.setAlpha(
                    alpha);
            spriteLine.draw(batch);
        }
       /* if (modePlanePath)
        {
            shapeRenderer.setColor(Color.ORANGE);
            shapeRenderer.line(
                    new Vector2(plane.curX + 5f, plane.curY + 5f), new Vector2(
                            plane.destX, plane.destY));
        }*/

        // RADARS
        if (modePlanePath || distanceTotal <= 20f ||
                checkPlaneInOtherRadarZone(plane) == true)
        {
            if (checkPlaneInOtherRadarZone(plane) == true &&
                    textureSiegeline != null && planeRadared != null
                    && planeRadared.hashCode() !=
                    plane.hashCode())
            {
                float rotate = (float) calcRotationAngleInDegrees(new Vector2(curX, curY),
                        new Vector2(planeRadared.curX, planeRadared.curY));
                spriteSiegeline.setRotation(rotate);
                spriteSiegeline.setPosition(curX + 4.5f, curY + 7.5f);
                spriteSiegeline.setCenter(curX,
                        curY);

                spriteSiegeline.setSize(9.0f, 9f);
                //spriteSiegeline.flip(true, false);
                spriteSiegeline.setColor(Color.WHITE);
                spriteSiegeline.draw(batch);
                /*batch.draw(textureSiegeline, curX, curY, 2.4f, 2.4f,
                        2f,
                        2f,
                        1f, 1f, rotate, (int)curX, (int)curY,  textureSiegeline.getWidth(),
                        textureSiegeline.getHeight(),
                        false, false
                );*/
            }
            shapeRenderer.setColor(Color.LIGHT_GRAY);
            Gdx.gl20.glLineWidth(0.41f);
            //shapeRenderer.rect(curX, curY, 20f, 20f);
            shapeRenderer.circle(curX + 7.5f, curY + 7.5f, 32f);
            plane.airRadarOn = true;
        }
        if (distanceTotal >= 20f && checkPlaneInOtherRadarZone(plane) != true)
        {
            plane.airRadarOn = false;
        }

        if (modePlanePath || distanceTotal <= 15f)
        {
            shapeRenderer.setColor(Color.YELLOW);
            Gdx.gl20.glLineWidth(0.01f);
            shapeRenderer.circle(curX + 7.5f, curY + 7.5f, 23f);
            plane.surfaceRadarOn = true;
        }
        if (distanceTotal >= 15f && checkPlaneInOtherRadarZone(plane) != true)
        {
            plane.surfaceRadarOn = false;
        }

        font.setColor(Color.WHITE);

        //log("draw "+texturePlaneSource);
        if (samsLock.tryLock())
        {
            for (Sam sam : sams)
            {
                if (sam.textureSam == null)
                {
                    break;
                }

                if (isPointInCircle(
                        sam.positionX, sam.positionY, Sam.airRadarRange, plane.curX, plane.curY))
                {
                    font.setColor(
                            Color.YELLOW);
                }
                if (isPointInCircle(
                        sam.positionX, sam.positionY, Sam.surfaceToAirRange, plane.curX, plane.curY))
                {
                    font.setColor(
                            Color.RED);
                }
            }
            samsLock.unlock();
        }

        // shadow
        batch.setColor(0f, 0f, 0f, 0.6f);
        batch.draw(
                texturePlaneSource, plane.curX + 2, plane.curY - 2, 7.5f, 7.5f, 15f, 15f, 1f, 1f,
                plane.curAngle,
                0, 0, 123, 132, false, false);

        // airplane
        batch.setColor(Color.WHITE);
        batch.draw(
                texturePlaneSource, plane.curX, plane.curY, 7.5f, 7.5f, 15f, 15f, 1f, 1f, plane.curAngle,
                0, 0, 123, 132, false, false);

        // airplane label
        CharSequence str = String.format("E%02d", idx);
        font.setScale(0.6f);
        font.draw(batch, str, curX, curY);

        //	if(MathUtils.random(2) == 1) { batch.draw(textureFileLoading, screenWidth - 100f, 150f); }
        batch.end();
        skippedRenders = 0;
    }

    public boolean isInRectangle(double centerX, double centerY, double radius, double x, double y)
    {
        return x >= centerX - radius && x <= centerX + radius &&
                y >= centerY - radius && y <= centerY + radius;
    }

    public boolean isPointInCircle(double centerX, double centerY, double radius, double x,
                                   double y)
    {
        if (isInRectangle(centerX, centerY, radius, x, y))
        {
            double dx = centerX - x;
            double dy = centerY - y;
            dx *= dx;
            dy *= dy;
            double distanceSquared = dx + dy;
            double radiusSquared = radius * radius;
            return distanceSquared <= radiusSquared;
        }
        return false;
    }

    public void checkArrival()
    {

        if (Math.abs(curX - destX) <= 6 && Math.abs(curY - destY) <= 6)
        {
            setNextCourse();
        }
    }

    public void setNextCourse()
    {
		/*log(
			"#" + spritePlane.hashCode() + " arrived at " + destX + ":" + destY + " stepMoves: " +
				"" + stepMoves + " " +
				"fuelWasted: " +
				String.format("%.2f" + " lbs", (Float) (fuelAtCourseStart - fuelCurrentValue)));*/

        stepMoves = 0f;
        if (planePath.size() > 0)
        {
            nextTargetPos = planePath.get(0);
            int where;
            where = planePath.indexOf(nextTargetPos);
            if (where != -1)
            {
                //	log("remove path "+where);
                planePath.remove(where);
            } else
            {
                log("remove path " + where + "@" + nextTargetPos + " failed: not found");
            }
		/*	log(
				"#" + spritePlane.hashCode() + " nextpos " + nextTargetPos.positionX + " positionX " +
				 nextTargetPos
					.positionY);*/
        } else
        {
            nextTargetPos.x = startX - 60f + MathUtils.random(122f);
            nextTargetPos.y = startY - 60f + MathUtils.random(122f);
		/*	log(
				"#" + spritePlane.hashCode() + " nextpos2 " + nextTargetPos.positionX + " positionX "
				+ nextTargetPos
					.positionY);*/
        }
	/*	if(nextTargetPos.positionX < 10 || nextTargetPos.positionY < 10)
		{
			log("#" + spritePlane.hashCode() + " bad positionX/positionY");
		}*/
        setActiveCourse(nextTargetPos.x, nextTargetPos.y);
        fuelCurrentValue += 0.5;
        //
    }

    public void setActiveCourse(float x, float y)
    {
        destX = x;
        destY = y;


		/*log(
			"#" + spritePlane.hashCode() + " set course " + positionX + " " + positionY + " stepx=" +
			 stepX + " " +
				"stepy=" + stepY);*/
        stepX = stepY = doneX = doneY = 0;

        fuelAtCourseStart = fuelCurrentValue;
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

    public void movePlane()
    {

		/*else
		{
			log("not arived: " + Math.abs(curX - destX) + " " + Math.abs(curY - destY) +
							" curX:" + curX + " curY:" + curY + " destX:" + destX + " destY:" + destY);
		}*/
        fuelCurrentValue -= 0.018;
        float diffX2 = (Math.max(curX, destX) - Math.min(curX, destX));
        float diffY2 = (Math.max(curY, destY) - Math.min(curY, destY));

        if (diffX2 == diffY2)
        {
            log(
                    "#" + spritePlane
                            .hashCode() + " [path cost] new stepX " + stepX + " stepY " + stepY + " " +
                            "diff sync at " +
                            diffX2);
        }

        stepMove = (float) 0.01;
        if (Math.abs(curAngle - destAngle) < 300)
        {
            stepMove = (float) 0.02;
        }
        if (Math.abs(curAngle - destAngle) < 150)
        {
            stepMove = (float) 0.03;
        }
        if (Math.abs(curAngle - destAngle) < 89)
        {
            stepMove = (float) 0.04;
        }
        if (Math.abs(curAngle - destAngle) < 25)
        {
            stepMove = (float) 0.05;
        }
        if (Math.abs(curAngle - destAngle) < 5)
        {
            stepMove = (float) 0.10;
        }
        if (Math.abs(curAngle - destAngle) == 0)
        {
            stepMove = (float) 0.15;
        }

        if (curX < destX && ((doneY <= 0 || diffY2 == 0) || ((doneY > 0) && doneY > stepY)))
        {
            curX = ((curX + stepMove));
            doneX++;
            doneY = 0;
        }
        if (curX > destX && ((doneY <= 0 || diffY2 == 0) || ((doneY > 0) && doneY > stepY)))
        {

            curX = ((curX - stepMove));
            doneX++;
            doneY = 0;
        }

        if (curY < destY && ((doneX <= 0 || diffX2 == 0) || ((doneX > 0) && doneX > stepX)))
        {

            curY = ((curY + stepMove));
            doneY++;
            doneX = 0;
        }
        if (curY > destY && ((doneX <= 0 || diffX2 == 0) || ((doneX > 0) && doneX > stepX)))
        {

            curY = ((curY - stepMove));
            doneY++;
            doneX = 0;
        }

        stepMoves += stepMove;

        // rotate
        destAngle = (float) calcRotationAngleInDegrees(
                new Vector2((int) curX, (int) curY), new Vector2((int) destX, (int) destY));

        if (destAngle > curAngle)
        {
            if (Math.abs(destAngle - curAngle) >= 2)
            {
                curAngle += 1.55f;
            } else
            {
                curAngle += Math.abs(destAngle - curAngle);
            }
        } else
        {
            if (Math.abs(destAngle - curAngle) >= 2)
            {
                curAngle -= 1.55f;
            } else
            {
                curAngle -= Math.abs(destAngle - curAngle);
            }
        }
        //log("destAngle: " + destAngle + " curAngle: " + curAngle);

        calculatePathStep();
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
        if (angle < 0)
        {
            angle += 360;
        }

        return angle;
    }

    public void resize(int width, int height)
    {
        //super.resize(width, height);
        log("[GdxGame.resize " + width + " positionX " + height + "]");
        if (camera != null)
        {
            camera.setToOrtho(false, (float) width, (float) height);
        }
    }

    public void pause()
    {
        //super.pause();
        log("[GdxGame.pause]");
        gameAI.pause();
    }

    public void resume()
    {
        log("[GdxGame.resume]");
        //super.resume();
        long javaHeap = Gdx.app.getJavaHeap();
        long nativeHeap = Gdx.app.getNativeHeap();
        log("javaHeap(mb): " + javaHeap / 1024.0f / 1024.0f);
        log("nativeHeap(mb): " + nativeHeap / 1024.0f / 1024.0f);
        gameAI.restore();
    }

    public void dispose()
    {
        super.dispose();
        log("[GdxGame.dispose]");
    }

    public String getThreadCodeSign()
    {

        if (generatedThreadSign)
        {
            return code[generatedThreadIdx];
        } else
        {
            generatedThreadIdx = MathUtils.random(code.length - 1);
            generatedThreadSign = true;
        }
        return code[generatedThreadIdx];
    }

    public static GdxGame getInstance()
    {
        if (instance == null)
        {
            instance = new GdxGame();
        }
        return instance;
    }

    public void log(String str)
    {
        if (Gdx.app == null)
        {
            return;
        }

        if (logMillis > 0)
        {
            String chr;

            chr = getThreadCodeSign();
            Gdx.app.log(
                    "info", String.format("%-5d", System.currentTimeMillis() - logMillis) + " " + Thread
                            .currentThread().getId() + " " + str);
        }
        logMillis = System.currentTimeMillis();
    }
}
