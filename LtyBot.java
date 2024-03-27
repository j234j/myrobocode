package Lty;

import robocode.*;
import java.awt.Color;

public class LtyBot extends AdvancedRobot {

    private double enemyDistance;
    private double enemyBearing;
    private double enemyHeading;
    private double lastEnemyHeading;

    public void run() {
        setColors(Color.blue, Color.blue, Color.blue, Color.blue, Color.blue);
        setMaxVelocity(8);

        while (true) {
            setTurnRadarRight(Double.POSITIVE_INFINITY);
            execute();
        }
    }

    private void move() {
        if (enemyDistance < 300) {
            if (enemyBearing > -90 && enemyBearing <= 90) {
                setAhead(100);
            } else {
                setBack(100);
            }
        } else {
            randomMove();
        }
        execute();
    }

    private void randomMove() {
        if (Math.random() > 0.5) {
            setAhead(100);
        } else {
            setBack(100);
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        updateEnemyData(e);
        double gunTurnAmt = calculateGunTurnAngle();
        double desiredFirePower = calculateFirePower();

        setTurnGunRight(gunTurnAmt);
        if (Math.abs(gunTurnAmt) < 10) {
            setFire(desiredFirePower);
        }
        execute();
    }

    private void updateEnemyData(ScannedRobotEvent e) {
        enemyDistance = e.getDistance();
        enemyBearing = e.getBearing();
        lastEnemyHeading = enemyHeading;
        enemyHeading = e.getHeading();
    }

    private double calculateGunTurnAngle() {
        return normalRelativeAngleDegrees(enemyBearing + getHeading() - getGunHeading());
    }

    private double calculateFirePower() {
        return Math.min(400 / enemyDistance, 3);
    }

    public void onHitByBullet(HitByBulletEvent e) {
        if (lastEnemyHeading != enemyHeading) {
            setTurnLeft(90 - e.getBearing());
            setAhead(150);
            execute();
        }
    }

    public void onHitWall(HitWallEvent e) {
        setTurnLeft(90);
        setAhead(150);
        execute();
    }

    public double normalRelativeAngleDegrees(double angle) {
        if (angle > 180)
            angle -= 360;
        if (angle < -180)
            angle += 360;
        return angle;
    }
}