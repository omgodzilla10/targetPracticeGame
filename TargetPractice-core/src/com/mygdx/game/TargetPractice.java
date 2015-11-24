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
    //TODO - Add javadocs for instance variables.
    final static int START_MAX_TIME = 3;
    
    private int score;
    
    private int strikes;
    
    private int screenWidth;
    
    private int screenHeight;
    
    private float maxTime;
    
    private float timer;
    
	private SpriteBatch batch;
	
	private Texture targetTexture;
	
	private OrthographicCamera camera;
	
	private Rectangle target;
	
	private Vector3 mousePos;
	
	private BitmapFont bMap;
	
	@Override
	public void create () {
	    //Initialize all instance variables.
	    batch = new SpriteBatch();
        mousePos = new Vector3();
        bMap = new BitmapFont();
        camera = new OrthographicCamera();
        camera.setToOrtho(true, screenWidth, screenHeight);
        
        //Set screen dimensions.
	    screenWidth = 800;
	    screenHeight = 480;
	    
	    //Score and strike counters.
	    score = 0;
	    strikes = 0;
	    
	    //Initialize timer and maximum time.
	    maxTime = START_MAX_TIME;
	    timer = 0;
	    
		//Initialize target texture, and set a file path.
		targetTexture = new Texture(Gdx.files.internal("assets/target.png"));
		
		//Initialize target rectangle.
		target = new Rectangle();
		target.width = 32;
		target.height = 32;
		
		//Give the target a starting position.
		setNewCoords();
	}

	@Override
	public void render () {
	    //Clear background and set color.
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//Increment timer with every frame.
		timer += Gdx.graphics.getDeltaTime();
		
		//Check to see if time has ran out
		if (timer > maxTime) {
		    //Increment strikes and reset timer.
		    strikes++;
		    timer = 0;
		    
		    //Move the target to a new location.
		    setNewCoords();
		}
		
		//If mouse is clicked.
		checkMouseClick();
		
		//Lose condition
		if(strikes == 3) {
	        Gdx.app.exit();
	    }

		batch.begin();
		
		//Draw the score and strikes counters to the screen.
		bMap.draw(batch, "Score: " + score, 0, screenHeight);
		bMap.draw(batch, "Strikes: " + strikes, 0, screenHeight - 20);
		
		//Draw the target to the screen.
		batch.draw(targetTexture, target.x, target.y);
		batch.end();
	}
	
	

	private void setNewCoords() {
		int newXCoord = (int) (Math.random() * (screenWidth - target.width));
		int newYCoord = (int) (Math.random() * (screenHeight - target.height));

		target.x = newXCoord;
		target.y = newYCoord;
	}
	
	/**
	 * This method is called once per frame to check
	 * to see if the mouse has been clicked, and to see
	 * whether or not the mouse clicked the target.
	 */
	private void checkMouseClick() {
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
	}
}