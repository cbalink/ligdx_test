package com.gvis.demo.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.gvis.demo.physics.MotionState;

public class PhysicsComponent implements Component {
    private MotionState motionState;
    private btRigidBody.btRigidBodyConstructionInfo bodyInfo;
    private btCollisionObject body;


    public MotionState getMotionState() {
        return motionState;
    }

    public void setMotionState(MotionState motionState) {
        this.motionState = motionState;
    }

    public btRigidBody.btRigidBodyConstructionInfo getBodyInfo() {
        return bodyInfo;
    }

    public void setBodyInfo(btRigidBody.btRigidBodyConstructionInfo bodyInfo) {
        this.bodyInfo = bodyInfo;
    }

    public btCollisionObject getBody() {
        return body;
    }

    public void setBody(btCollisionObject body) {
        this.body = body;
    }
}
