package com.gvis.demo.helpers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.gvis.demo.components.ModelComponent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MapFileParser {

    private static int SQUARE_SIZE = 6;
    private static float FLOOR_HEIGHT = 1f;

    private int width;
    private int height;
    private String map;
    private FileHandle handle;
    private AssetManager assets;
    private List<Entity> entities;

    private Model wall, wholeFloor, zero, marioBox;

    public MapFileParser(FileHandle handle) {
        this.handle = handle;
        entities = new ArrayList<>();
        readFile();
        loadAssets();
        buildModels();
        createEntities();
        Gdx.app.log(MapFileParser.class.getCanonicalName(), toString());
    }

    public List<Entity> getEntities() {
        return entities;
    }

    private void loadAssets() {
        assets = new AssetManager();
        assets.load("data/block.g3db", Model.class);
        assets.finishLoading();
    }

    private void buildModels() {

        ModelBuilder builder = new ModelBuilder();
        wall = builder.createBox(SQUARE_SIZE, SQUARE_SIZE * 2, SQUARE_SIZE,
                new Material(ColorAttribute.createDiffuse(Color.BLUE),
                        ColorAttribute.createSpecular(Color.RED), FloatAttribute
                        .createShininess(16f)), VertexAttributes.Usage.Position
                        | VertexAttributes.Usage.Normal);

        wholeFloor = builder.createBox(width * SQUARE_SIZE, FLOOR_HEIGHT, height * SQUARE_SIZE,
                new Material(ColorAttribute.createDiffuse(Color.WHITE),
                        ColorAttribute.createSpecular(Color.RED), FloatAttribute
                        .createShininess(16f)), VertexAttributes.Usage.Position
                        | VertexAttributes.Usage.Normal);
        zero = builder.createBox(1, 1, 1,
                new Material(ColorAttribute.createDiffuse(Color.RED),
                        ColorAttribute.createSpecular(Color.RED), FloatAttribute
                        .createShininess(16f)), VertexAttributes.Usage.Position
                        | VertexAttributes.Usage.Normal);

        marioBox = assets.get("data/block.g3db", Model.class);

    }

    private void createEntities() {
        int elements = width * height;
        for (int i = 0; i < elements; i++) {

            switch (map.charAt(i)) {
                case 'w':
                    entities.add(EntityFactory.createStaticEntity(
                            wall, i % width * SQUARE_SIZE, SQUARE_SIZE + FLOOR_HEIGHT, (i / width) * SQUARE_SIZE));
                    break;
                case 'b':
                    Entity box = EntityFactory.createStaticEntity(
                            marioBox, i % width * SQUARE_SIZE, +FLOOR_HEIGHT, (i / width) * SQUARE_SIZE);

                    ModelInstance boxInstance = box.getComponent(ModelComponent.class).getInstance();
                    boxInstance.transform.scl(0.002f);
                    Array<Material> materials = boxInstance.materials;
                    for (Material m : materials) {
                        m.set(new IntAttribute(IntAttribute.CullFace, 0));
                    }

                    entities.add(box);
                    break;
            }
        }

        entities.add(EntityFactory.createStaticEntity(
                wholeFloor,
                width * SQUARE_SIZE * 0.5f - SQUARE_SIZE * 0.5f,
                FLOOR_HEIGHT * 0.5f, ((float) height * SQUARE_SIZE * 0.5f - SQUARE_SIZE * 0.5f)));
        entities.add(EntityFactory.createStaticEntity(zero, 0, 0, 0));
    }


    private void readFile() {

        BufferedReader br = new BufferedReader(new InputStreamReader(handle.read()));
        StringBuilder builder = new StringBuilder();

        try {
            String line = br.readLine();
            width = line.length();
            while (line != null) {
                builder.append(line);
                height += 1;
                line = br.readLine();

                if (line != null && line.length() != width) {
                    throw new MapFormatException("Each line has to be of the same length");
                }
            }
            map = builder.toString();
        } catch (IOException e) {

        }

    }

    @Override
    public String toString() {
        return "MapFileParser{" +
                "width=" + width +
                ", map='" + map + '\'' +
                '}';
    }

    private class MapFormatException extends RuntimeException {

        public MapFormatException(String message) {
            super(message);
        }
    }
}
