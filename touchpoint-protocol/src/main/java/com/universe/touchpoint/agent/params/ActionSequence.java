package com.universe.touchpoint.agent.params;

import com.universe.touchpoint.context.TouchPoint;

public class ActionSequence extends TouchPoint {

    private Position deltaPosition;
    private Orientation deltaOrientation;
    private double deltaGrip;

    public ActionSequence(Position deltaPosition, Orientation deltaOrientation, double deltaGrip) {
        this.deltaPosition = deltaPosition;
        this.deltaOrientation = deltaOrientation;
        this.deltaGrip = deltaGrip;
    }

    public Position getDeltaPosition() {
        return deltaPosition;
    }

    public void setDeltaPosition(Position deltaPosition) {
        this.deltaPosition = deltaPosition;
    }

    public Orientation getDeltaOrientation() {
        return deltaOrientation;
    }

    public void setDeltaOrientation(Orientation deltaOrientation) {
        this.deltaOrientation = deltaOrientation;
    }

    public double getDeltaGrip() {
        return deltaGrip;
    }

    public void setDeltaGrip(double deltaGrip) {
        this.deltaGrip = deltaGrip;
    }

    // 内部类：表示位置变化
    public static class Position {
        private double x, y, z;

        public Position(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getZ() {
            return z;
        }

        public void setZ(double z) {
            this.z = z;
        }
    }

    // 内部类：表示角度变化
    public static class Orientation {
        private double thetaX, thetaY, thetaZ;

        public Orientation(double thetaX, double thetaY, double thetaZ) {
            this.thetaX = thetaX;
            this.thetaY = thetaY;
            this.thetaZ = thetaZ;
        }

        public double getThetaX() {
            return thetaX;
        }

        public void setThetaX(double thetaX) {
            this.thetaX = thetaX;
        }

        public double getThetaY() {
            return thetaY;
        }

        public void setThetaY(double thetaY) {
            this.thetaY = thetaY;
        }

        public double getThetaZ() {
            return thetaZ;
        }

        public void setThetaZ(double thetaZ) {
            this.thetaZ = thetaZ;
        }
    }

}
