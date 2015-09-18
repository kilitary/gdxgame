package com.mygdx.game.android;

import android.os.Bundle;

//import com.example.cepp.app2.GameView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.GdxGame;
//import com.mygdx.game.;

public class AndroidLauncher extends AndroidApplication
{
	GdxGame gdxGame;
	long logMillis = System.currentTimeMillis();
	int generatedThreadIdx;
	Boolean generatedThreadSign = false;
	AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		gdxGame = new GdxGame();
		log(
			"[AndroidLauncher.onCreate  gdxGame:" + gdxGame + " " + config + " " +
				"savedInstanceState:" + savedInstanceState + "]");
		config.numSamples = 4;
		config.hideStatusBar = true;
		config.r = 64;
		initialize(gdxGame, config);

		log("gdxGame.pubint=" + gdxGame.pubint);

		hideStatusBar(true);

		log("[AndroidLauncher.onCreate done]");
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		log("AndroidLauncher onDestroy()");
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		log("AndroidLauncher onPause()");
	}

	public void log(String str)
	{
		gdxGame.log(str);
	}
}