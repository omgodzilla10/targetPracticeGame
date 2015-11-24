package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class TargetPractice extends ApplicationAdapter {
    int score = 0;
    int strikes = 0;
    int screenWidth = 800;
    int screenHeight = 480;
    float maxTime;
    float timer = 0;
    
	SpriteBatch batch;
	Texture targetTexture;
	Rectangle target;
	OrthographicCamera camera;
	Vector3 mousePos;
	BitmapFont bMap;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(true, screenWidth, screenHeight);
		
		targetTexture = new Texture(Gdx.files.internal("assets/target.png"));

		target = new Rectangle();
		target.width = 32;
		target.height = 32;
		
		maxTime = 3;
		
		mousePos = new Vector3();
		setNewCoords();
		
		bMap = new BitmapFont();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//Increment timer with every frame.
		timer += Gdx.graphics.getDeltaTime();
		
		//Check to see if time has ran out
		if (timer > maxTime) {
		    System.out.println("Time ran out!");
		    strikes++;
		    timer = 0;
		    setNewCoords();
		}
		
		//If mouse is clicked.
		if(Gdx.input.justTouched()) {
		    //Store mouse position in a variable.
			mousePos.set(Gdx.input.getX(), screenHeight - Gdx.input.getY(), 0);
			
			//Check to see if mouse is within the bounds of the rectangle.
			if (mousePos.x >= target.x && mousePos.x <= target.x + target.width) {
			    if (mousePos.y >= target.y && mousePos.y <= target.y + target.height) {
			        //When the player clicks the target.
			        System.out.println("Good job!");
			        score++;
			        timer = 0;
			        setNewCoords();
			        
			        if(score % 3 == 0) {
			            maxTime *= 0.95;
			        }
			    }
			} else {
			    //When the player misses the target.
			    System.out.println("You missed!");
			    strikes++;
			    timer = 0;
			    setNewCoords();
			}
		}
		
		if(strikes == 3) {
	        Gdx.app.exit();
	    }

		batch.begin();
		bMap.draw(batch, "Score: " + score, 0, screenHeight);
		bMap.draw(batch, "Strikes: " + strikes, 0, screenHeight - 20);
		batch.draw(targetTexture, target.x, target.y);
		batch.end();
	}
	
	

	private void setNewCoords() {
		int newXCoord = (int) (Math.random() * (screenWidth - target.width));
		int newYCoord = (int) (Math.random() * (screenHeight - target.height));

		target.x = newXCoord;
		target.y = newYCoord;
	}
}