package com.gvis.demo.screens;

import com.badlogic.gdx.Screen;
import com.gvis.demo.Core;
import com.gvis.demo.GameWorld;

public class GameScreen implements Screen {

    private Core game;
    private GameWorld gameWorld;

    public GameScreen(Core game) {
        this.game = game;
        gameWorld = new GameWorld();
        // hide cursor
        //Gdx.input.setCursorCatched(true);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        gameWorld.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        gameWorld.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        gameWorld.dispose();
    }
}
