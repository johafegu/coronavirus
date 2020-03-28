package com.hardys.flappybird.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.hardys.flappybird.FlappyBird;
import com.hardys.flappybird.sprites.Bird;
import com.hardys.flappybird.sprites.Tube;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.awt.TextArea;

public class PlayState extends State {

    private static  final int TUBE_SPACING = 125;
    private static  final int TUBE_COUNT = 4;
    private static  final int GROUND_Y_OFFSET = -30;
    private Bird bird;
    private Texture bg;
    private Texture ground;
    private Vector2 groundPos1;
    private Vector2 groundPos2;

    private Array<Tube> tubes;

    private float renderX = 100;
    private float renderY = 100;

    private int score;
    private String yourScoreName;
    private BitmapFont yourBitmapFontName;
    private boolean up = true;
    private boolean down = false;
    private Sound point;
    private Sound crash;
    private Preferences prefs;


    public PlayState(GameStateManager gsm) {
        super(gsm);
        bird = new Bird (50,320);
        camera.setToOrtho(false, FlappyBird.WIDTH/2, FlappyBird.HEIGHT/2);
        cameraScore.setToOrtho(false, FlappyBird.WIDTH/2, FlappyBird.HEIGHT/2);
        bg = new Texture("bg.png");
        tubes = new Array<Tube>();
        ground = new Texture("ground.png");
        groundPos1 = new Vector2(camera.position.x - camera.viewportWidth/2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2((camera.position.x - camera.viewportWidth/2) + ground.getWidth(), GROUND_Y_OFFSET);
        point = Gdx.audio.newSound(Gdx.files.internal("point.ogg"));
        crash = Gdx.audio.newSound(Gdx.files.internal("crash.ogg"));

        score = 0;
        yourScoreName = "score: 0";
        yourBitmapFontName = new BitmapFont();
        prefs = Gdx.app.getPreferences("My Preferences");

        for(int i = 1; i <= TUBE_COUNT;  i++){
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }

    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()){
            bird.jump();
        }

    }

    @Override
    public void update(float dt) {

        handleInput();
        updateGround();
        bird.update(dt);
        camera.position.x = bird.getPosition().x + 80;
        cameraScore.position.x = bird.getPosition().x + 80;

        for(int i=0; i < tubes.size; i++) {

                Tube tube = tubes.get(i);
                if (camera.position.x - (camera.viewportWidth / 2) > tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
                    tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
                }
            /*System.out.println("tube.getPosTopTube().y"+ tube.getPosTopTube().y);
            System.out.println("tube.getPosBotTube().y"+ tube.getPosBotTube().y);
            System.out.println("cont"+ cont);*/
            /*System.out.println("LA scoreeee "+ score);*/
            /*System.out.println("LA iiiiiiiii " + i);*/

                if (tube.getPosTopTube().y <= 360 && up) {
                    tube.getPosTopTube().y++;
                    tube.getPosBotTube().y++;
                    tube.repositionBounds(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
                    if (tube.getPosTopTube().y == 360) {
                        up = false;
                        down = true;
                    }
                } else if (tube.getPosBotTube().y >= -255 && down) {
                    tube.getPosTopTube().y--;
                    tube.getPosBotTube().y--;
                    tube.repositionBounds(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
                    if (tube.getPosBotTube().y == -255) {
                        up = true;
                        down = false;
                    }
                }
                if (tube.score(bird.getBounds())) {
                score++;
                yourScoreName = "score: " + score / 20;
                /*point.play(0.5f);*/
            }
                if (tube.collides(bird.getBounds())) {
                    crash.play(0.3f);
                    int highscore = prefs.getInteger("highscore");
                    if (score/20 > highscore) {
                        prefs.putInteger("highscore", score/20);
                        prefs.flush();
                    }

                    gsm.set(new MenuState(gsm));
                }
        }

        if(bird.getPosition().y <= ground.getHeight() +  GROUND_Y_OFFSET){
            crash.play(0.4f);
            int highscore = prefs.getInteger("highscore");
            if (score/20 > highscore) {
                prefs.putInteger("highscore", score/20);
                prefs.flush();
            }
            gsm.set(new MenuState(gsm));
        }

        camera.update();

    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(camera.combined);
        /*renderX += Gdx.input.getAccelerometerX();
        renderY += Gdx.input.getAccelerometerY();*/
        spriteBatch.begin();
        spriteBatch.draw(bg, camera.position.x - (camera.viewportWidth / 2),0);
        spriteBatch.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);
        for (Tube tube: tubes) {
            spriteBatch.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            spriteBatch.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }
        spriteBatch.draw(ground, groundPos1.x, groundPos1.y);
        spriteBatch.draw(ground, groundPos2.x, groundPos2.y);
        spriteBatch.setProjectionMatrix(cameraScore.combined);
        yourBitmapFontName.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        yourBitmapFontName.draw(spriteBatch, yourScoreName, 10, 350);
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        ground.dispose();
        bird.dispose();
        for(Tube tube: tubes){
            tube.dispose();
        }
        System.out.println("PLAY STATE DISPOSED");
    }

    private void updateGround(){
        if(camera.position.x - (camera.viewportWidth/2) > groundPos1.x + ground.getWidth()){
            groundPos1.add(ground.getWidth()*2, 0);
        }
        if(camera.position.x - (camera.viewportWidth/2) > groundPos2.x + ground.getWidth()){
            groundPos2.add(ground.getWidth()*2, 0);
        }
    }
}
