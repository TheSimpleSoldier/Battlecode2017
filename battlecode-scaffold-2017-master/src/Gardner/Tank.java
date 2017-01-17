package Gardner;

import battlecode.common.*;

public class Tank extends Unit {
    static MapLocation target;

    public void loop() throws GameActionException {
        RobotInfo[] enemies = rc.senseNearbyRobots(mySensorRadius, enemy);
        RobotInfo[] allies = rc.senseNearbyRobots(mySensorRadius, us);

        target = Util.nextTargetLocation(enemies);
        Util.tryMove(rc.getLocation().directionTo(target));
        if (enemies.length > 0) {
            Util.shootAtEnemies(enemies, allies);
        }
    }
}
