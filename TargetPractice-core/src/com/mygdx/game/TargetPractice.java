package com.mygdx.game;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class TargetPractice extends ApplicationAdapter {
    /** The starting maximum amount of time that the player
     * has to click the target. */
    static final int START_MAX_TIME = 3;
    
    /** The maximum number of strikes the user can have until
     * the game is over. */
    static final int MAX_STRIKES = 5;
    
    /** The default screen width. */
    static final int SCREEN_X = 400;
    
    /** The default screen height. */
    static final int SCREEN_Y = 400;
    
    /** The factor at which the difficulty is increased 
     * throughout the game. 1 = No change, lower is harder.*/
    static final double DIFF_FACTOR = 0.92;
    
    /** The user's current score. */
    private int score;
    
    /** The user's current number of strikes. */
    private int strikes;
    
    /** The amount of time the user has to click the target
     * before gaining a strike. */
    private float maxTime;
    
    /** A variable that holds time. Used to check to see
     * if the target hasn't been clicked in time. */
    private float timer;
    
    /** Used to format the timer and maxTime variables
     * when displaying onto the screen. */
    private NumberFormat decFormat;
    
    /** A LIBGDX SpriteBatch object, which handles all of the 
     * drawing of textures onto the screen. */
	private SpriteBatch batch;
	
	/** A Texture object, which is used to store the
	 * texture for the target. */
	private Texture targetTexture;
	
	/** A camera object, projects objects on the screen, and
	 * provides an easy way to transform and rotate objects on screen, 
	 * without having to manually manipulate matrices. */
	private OrthographicCamera camera;
	
	/** A rectangle object that contains the width, height, 
	 * and x and y coordinates. */
	private Rectangle target;
	
	/** An object that contains two values, 
	 * an X and a Y coordinate. */
	private Vector2 mousePos;
	
	/** A LIBGDX Object that is responsible for drawing
	 * and rendering fonts onto the screen. */
	private BitmapFont bMap;
	
	/**
	 * This is a default method extended from the
	 * ApplicationAdapter class. The method is called
	 * immediately when the program executes. */
	@Override
	public void create () {
	    //Initialize all instance variables
	    decFormat = new DecimalFormat("0.#");
	    batch = new SpriteBatch();
        mousePos = new Vector2();
        bMap = new BitmapFont();
        camera = new OrthographicCamera();
        camera.setToOrtho(true, SCREEN_X, SCREEN_Y);
	    
	    //Score and strike counters
	    score = 0;
	    strikes = 0;
	    
	    //Initialize timer and maximum time
	    maxTime = START_MAX_TIME;
	    timer = 0;
	    
		//Initialize target texture, and set a file path
		targetTexture = new Texture(Gdx.files.internal("assets/target.png"));
		
		//Initialize target rectangle
		target = new Rectangle();
		target.width = 32;
		target.height = 32;
		
		//Give the target a starting position
		setNewCoords();
	}

	@Override
	public void render () {
	    //Clear background and set color
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//Increment timer with every frame
		timer += Gdx.graphics.getDeltaTime();
		
		//Check for mouse clicks.
        checkMouseClick();
		
		//Check to see if time has ran out
		if (timer > maxTime) {
		    //Increment strikes and reset timer
		    strikes++;
		    timer = 0;
		    
		    //Move the target to a new location
		    setNewCoords();
		}
		
		//Lose condition
		if (strikes == MAX_STRIKES) {
	        Gdx.app.exit();
	    }
		
		//Begin rendering
		batch.begin();
		
		//Draw the score and strikes counters to the screen
		bMap.setColor(Color.BLUE);
		bMap.draw(batch, "Score: " + score, 0, SCREEN_Y);
		bMap.draw(batch, "Strikes: " + strikes, 0, SCREEN_Y - 20);
		bMap.draw(batch, "Current Time: " + decFormat.format(timer)
		+ " / " + decFormat.format(maxTime), 0, SCREEN_Y - 40);
		
		//Draw the target to the screen
		batch.draw(targetTexture, target.x, target.y);
		batch.end();
	}
	
	/**
	 * This method sets a new random position 
	 * for the target rectangle.
	 * */
	private void setNewCoords() {
	    //Set new X and Y coordinates within the screen bounds.
		int newXCoord = (int) (Math.random() * (SCREEN_X - target.width));
		int newYCoord = (int) (Math.random() * (SCREEN_Y - target.height));

		target.x = newXCoord;
		target.y = newYCoord;
	}
	
	/**
	 * This method is called once per frame to check
	 * to see if the mouse has been clicked, and to see
	 * whether or not the mouse clicked the target.
	 * When the target is clicked, the score is incremented.
	 * When the target is missed, the strikes counter is 
	 * incremented.
	 */
	private void checkMouseClick() {
	    //Check to see if the mouse has been clicked
	    if (Gdx.input.justTouched()) {
	        
            //Store mouse position in a variable
            mousePos.set(Gdx.input.getX(), SCREEN_Y - Gdx.input.getY());
            
            //Check to see if mouse is within the bounds of the rectangle
            if (mousePos.x >= target.x && mousePos.x <= target.x 
                    + target.width) {
                
                if (mousePos.y >= target.y && mousePos.y <= target.y 
                        + target.height) {
                    //Player clicks the target
                    score++;
                    timer = 0;
                    setNewCoords();
                    
                    /*Decrease time to click the target after every three
                    points. */
                    if (score % 3 == 0) {
                        maxTime *= DIFF_FACTOR;
                    }
                }
            } else {
                //Player misses the target
                strikes++;
                timer = 0;
                setNewCoords();
            }
        }
	}
}