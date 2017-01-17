package Gardner;

import battlecode.common.*;

public class SearchScout {
    static Direction direction;
    static int channel;

    // This scout moves toward an edge that has not been discovered yet
    public static boolean loop() throws GameActionException {
        if (Unit.rc.readBroadcast(Constants.foundHighestMapX) == 0) {
            channel = Constants.highestMapX;
            direction = new Direction(0);
        } else if (Unit.rc.readBroadcast(Constants.foundLowestMapX) == 0) {
            channel = Constants.lowestMapX;
            direction = new Direction((float)Math.PI);
        } else if (Unit.rc.readBroadcast(Constants.foundLowestMapY) == 0) {
            channel = Constants.lowestMapY;
            direction = new Direction((float) Math.PI * 3 / 2);
        } else if (Unit.rc.readBroadcast(Constants.foundHighestMaxY) == 0) {
            channel = Constants.highestMapY;
            direction = new Direction((float) Math.PI / 2);
        } else {
            return false;
        }

        BulletInfo[] bullets = Unit.rc.senseNearbyBullets();
        RobotInfo[] enemyUnits = Unit.rc.senseNearbyRobots(Unit.mySensorRadius, Unit.enemy);
        Util.scoutMove(direction, bullets, enemyUnits);

        return true;
    }
}
