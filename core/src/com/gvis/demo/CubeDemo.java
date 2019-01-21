package com.gvis.demo;

import com.badlogic.gdx.*;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;


public class CubeDemo extends ApplicationAdapter {

    private static final float FOV = 67;
    CameraInputController camController;
    Vector3 position = new Vector3();
    float scale = 1;
    boolean scaling = false;
    private InputMultiplexer multiplexer;
    private PerspectiveCamera camera;
    private Model boxModel;
    private Model tvModel;
    private ModelInstance boxInstance;
    private ModelBatch modelBatch;
    private Environment environment;
    private float speed = 5f;
    private float rotation;
    private boolean rotating;
    private boolean increment;

    private void movement() {

        boxInstance.transform.getTranslation(position);
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            position.x += Gdx.graphics.getDeltaTime() * speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            position.x -= Gdx.graphics.getDeltaTime() * speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            position.z -= Gdx.graphics.getDeltaTime() * speed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            position.z += Gdx.graphics.getDeltaTime() * speed;
        }
    }

    private void rotate() {
        rotation = (rotation + Gdx.graphics.getDeltaTime() * 100) %
                360;
    }

    void scale() {
        if (increment) {
            scale = (scale + Gdx.graphics.getDeltaTime() / 5);
            if (scale >= 1.5f) {
                increment = false;
            }
        } else {
            scale = (scale - Gdx.graphics.getDeltaTime() / 5);
            if (scale <= 0.5f) {
                increment = true;
            }
        }
    }


    private void updateTransformation() {
        boxInstance.transform.setFromEulerAngles(0, 0,
                rotation).trn(position.x, position.y,
                position.z).scale(scale, scale, scale);
    }

    @Override
    public void create() {
        for (Controller controller : Controllers.getControllers()) {
            Gdx.app.log("Controller: ", controller.getName());
        }

        Controllers.addListener(new ControllerAdapter() {
            private final int GC_A = 1;
            private final int GC_B = 2;
            private final int GC_X = 0;
            private final int GC_Y = 3;

            @Override
            public boolean buttonDown(Controller controller, int buttonCode) {
                Gdx.app.log("Controller: ", controller.getName() + " code :" + buttonCode);
                if (buttonCode == GC_A) {
                    rotating = !rotating;
                } else if (buttonCode == GC_B) {
                    scaling = !scaling;
                } else if (buttonCode == GC_X) {
                    boxInstance = new ModelInstance(tvModel);
                } else if (buttonCode == GC_Y) {
                    boxInstance = new ModelInstance(boxModel);
                }
                return false;
            }
        });

        camera = new PerspectiveCamera(FOV, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(10f, 10f, 10f);
        camera.lookAt(0, 0, 0);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();
        camController = new CameraInputController(camera);
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(camController);
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.S) {
                    scaling = !scaling;
                } else if (keycode == Input.Keys.R) {
                    rotating = !rotating;
                } else if (keycode == Input.Keys.T) {
                    boxInstance = new ModelInstance(tvModel);
                } else if (keycode == Input.Keys.B) {
                    boxInstance = new ModelInstance(boxModel);
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(multiplexer);


        ModelBuilder modelBuilder = new ModelBuilder();
        Material material = new Material(ColorAttribute.createDiffuse(Color.BLUE));
        boxModel = modelBuilder.createBox(5, 5, 5, material,
                VertexAttributes.Usage.Position |
                        VertexAttributes.Usage.Normal);
        ObjLoader loader = new ObjLoader();
        tvModel = loader.loadModel(Gdx.files.internal("data/tv/TV.obj"));
        boxInstance = new ModelInstance(boxModel);

        modelBatch = new ModelBatch();
        environment = new Environment();

        environment.set(new ColorAttribute(
                ColorAttribute.AmbientLight,
                0.4f, 0.4f, 0.4f, 1f));

        environment.add(new DirectionalLight().set(
                0.8f, 0.8f, 0.8f,
                1f, -0.8f, -0.2f));

    }

    @Override
    public void render() {
        if (rotating) {
            rotate();
        }
        movement();
        if (scaling) {
            scale();
        }

        updateTransformation();
        camController.update();
        Gdx.gl.glViewport(0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        modelBatch.begin(camera);
        modelBatch.render(boxInstance, environment);
        modelBatch.end();
    }

    @Override
    public void dispose() {
        boxModel.dispose();
    }
}
