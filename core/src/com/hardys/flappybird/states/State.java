package com.hardys.flappybird.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public abstract class State {

    protected OrthographicCamera camera;
    protected OrthographicCamera cameraScore;

    protected Vector3 mouse;
    protected GameStateManager gsm;

    protected State(GameStateManager gameStateManager){
        this.gsm = gameStateManager;
        camera = new OrthographicCamera();
        cameraScore = new OrthographicCamera();
        mouse = new Vector3();
    }

    protected abstract void handleInput();
    public abstract void update (float dt);
    public abstract void render (SpriteBatch spriteBatch);
    public abstract void dispose();

}
