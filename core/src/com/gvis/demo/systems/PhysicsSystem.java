package com.gvis.demo.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.gvis.demo.components.PhysicsComponent;

public class PhysicsSystem extends EntitySystem implements EntityListener {

    private final btCollisionConfiguration collisionConfiguration;
    private final btCollisionDispatcher dispatcher;
    private final btBroadphaseInterface broadPhase;
    private final btConstraintSolver solver;
    private final btDiscreteDynamicsWorld collisionWorld;


    private btGhostPairCallback ghostPairCallback;
    private int maxSubSteps = 5;
    private float fixedTimeStep = 1f / 60f;

    public PhysicsSystem() {
        collisionConfiguration = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfiguration);
        broadPhase = new btAxisSweep3(
                new Vector3(-1000, -1000, -1000),
                new Vector3(1000, 1000, 1000));
        solver = new btSequentialImpulseConstraintSolver();
        collisionWorld = new btDiscreteDynamicsWorld(
                dispatcher, broadPhase, solver, collisionConfiguration);
        ghostPairCallback = new btGhostPairCallback();
        broadPhase.getOverlappingPairCache().setInternalGhostPairCallback(
                ghostPairCallback);
        this.collisionWorld.setGravity(new Vector3(0, -0.5f, 0));

    }

    @Override
    public void update(float deltaTime) {
        collisionWorld.stepSimulation(deltaTime, maxSubSteps, fixedTimeStep);
    }

    public void dispose() {
        collisionWorld.dispose();
        if (solver != null) {
            solver.dispose();
        }
        if (broadPhase != null) {
            broadPhase.dispose();
        }
        if (dispatcher != null) {
            dispatcher.dispose();
        }
        if (collisionConfiguration != null) {
            collisionConfiguration.dispose();
            ghostPairCallback.dispose();
        }
    }


    @Override
    public void addedToEngine(Engine engine) {
        engine.addEntityListener(Family.all(PhysicsComponent.class).get(), this);
    }

    @Override
    public void entityAdded(Entity entity) {
        PhysicsComponent physicsComponent =
                entity.getComponent(PhysicsComponent.class);
        if (physicsComponent.getBody() != null) {
            collisionWorld.addRigidBody((btRigidBody) physicsComponent.getBody());
        }
    }

    @Override
    public void entityRemoved(Entity entity) {

    }

    public btDiscreteDynamicsWorld getCollisionWorld() {
        return collisionWorld;
    }


}
