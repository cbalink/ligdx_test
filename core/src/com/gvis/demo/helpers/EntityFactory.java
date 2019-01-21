package com.gvis.demo.helpers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.gvis.demo.components.ModelComponent;
import com.gvis.demo.components.PhysicsComponent;
import com.gvis.demo.physics.MotionState;

public class EntityFactory {

    private static Model playerModel;
    private static Texture playerTexture;
    private static ModelBuilder modelBuilder;


    public static Entity createStaticEntity(Model model, float x, float y, float z) {
        final BoundingBox boundingBox = new BoundingBox();
        model.calculateBoundingBox(boundingBox);
        Vector3 tmpV = new Vector3();
        btCollisionShape collisionShape = new btBoxShape(
                tmpV.set(boundingBox.getWidth() * 0.5f, boundingBox.getHeight() * 0.5f, boundingBox.getDepth() * 0.5f
                ));

        Entity entity = new Entity();
        ModelComponent modelComponent = new ModelComponent(model, x, y, z);
        entity.add(modelComponent);

        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.setBodyInfo(
                new btRigidBody.btRigidBodyConstructionInfo(
                        0, null, collisionShape, Vector3.Zero));
        physicsComponent.setBody(new btRigidBody(physicsComponent.getBodyInfo()));
        physicsComponent.getBody().userData = entity;
        physicsComponent.setMotionState(new MotionState(modelComponent.getInstance().transform));
        ((btRigidBody) physicsComponent.getBody()).setMotionState(physicsComponent.getMotionState());
        entity.add(physicsComponent);

        return entity;
    }

}
