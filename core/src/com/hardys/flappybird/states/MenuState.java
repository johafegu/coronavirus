package com.hardys.flappybird.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hardys.flappybird.FlappyBird;

public class MenuState extends State {

    private Texture background;
    private Texture playButton;
    public Preferences prefs;
    private BitmapFont yourBitmapFontName;
    private String yourScoreName;

    public MenuState(GameStateManager gsm){
        super(gsm);
        camera.setToOrtho(false, FlappyBird.WIDTH / 2, FlappyBird.HEIGHT / 2);
        background = new Texture("bg.png");
        playButton = new Texture("playbtn.png");
        prefs = Gdx.app.getPreferences("My Preferences");
        yourBitmapFontName = new BitmapFont();
        int highscore = prefs.getInteger("highscore", 0);
        yourScoreName = "Max Score: "+ highscore;

    }


    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()) {
            gsm.set(new PlayState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0);
        spriteBatch.draw(playButton, camera.position.x - playButton.getWidth() / 2, camera.position.y);
        yourBitmapFontName.setColor(1.0f, 1.0f, 1.0f, 8.0f);
        yourBitmapFontName.draw(spriteBatch, yourScoreName, camera.position.x - playButton.getWidth() / 2 + 10, camera.position.y - 60 );
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playButton.dispose();
        System.out.println("MENU STATE DISPOSED");
    }
}
