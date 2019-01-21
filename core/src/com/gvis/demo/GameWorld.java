package com.gvis.demo;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.gvis.demo.helpers.MapFileParser;
import com.gvis.demo.systems.PhysicsSystem;
import com.gvis.demo.systems.RenderSystem;

import java.util.List;


public class GameWorld {

    private static final float FOV = 67f;
    private ModelBatch batch;
    private Environment environment;
    private PerspectiveCamera cam;
    private Engine engine;
    private PhysicsSystem physicsSystem;
    private AssetManager assets;

    private Model wall, wholeFloor, zero;
    private Model marioBox;


    public GameWorld() {
        Bullet.init();
        initCamera();
        initEnvironment();
        initModelBatch();
        initEngine();
        initInput();
        addSystems();
        loadMap();
    }


    public GameWorld(String map, int size) {
        this();
    }

    private void loadMap() {

        FileHandle handle = Gdx.files.local("map.txt");
        MapFileParser parser = new MapFileParser(handle);
        addEntities(parser.getEntities());
    }

    private void addEntities(List<Entity> entities) {
        for (Entity e : entities) {
            engine.addEntity(e);
        }
    }

    private void addSystems() {
        engine.addSystem(new RenderSystem(batch, environment));
        engine.addSystem(physicsSystem = new PhysicsSystem());
    }

    private void initEngine() {
        engine = new Engine();
    }

    private void initModelBatch() {
        batch = new ModelBatch();
    }

    private void initInput() {
        CameraInputController cameraInputController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(cameraInputController);
    }

    private void initEnvironment() {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight,
                0.3f, 0.3f, 0.3f, 1f));
        environment.add(new DirectionalLight().set(
                0.8f, 0.8f, 0.8f,
                1f, -0.8f, -0.2f));
    }

    private void initCamera() {
        cam = new PerspectiveCamera(FOV, Core.V_WIDTH, Core.V_HEIGHT);
        cam.position.set(30f, 40f, 30f);
        cam.lookAt(0f, 0f, 0f);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();
    }


    public void render(float delta) {
        renderWorld(delta);
    }

    private void renderWorld(float delta) {
        batch.begin(cam);
        engine.update(delta);
        batch.end();
    }

    public void resize(int width, int height) {
        cam.viewportHeight = height;
        cam.viewportWidth = width;
    }

    public void dispose() {
        physicsSystem.dispose();
        batch.dispose();
        wall.dispose();
        wholeFloor.dispose();
        zero.dispose();
        marioBox.dispose();
    }
}
