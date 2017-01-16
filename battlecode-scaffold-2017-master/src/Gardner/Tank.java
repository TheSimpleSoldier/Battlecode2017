package Gardner;

import battlecode.common.*;

public class Tank extends Unit {
    static MapLocation target;
    static int enemyArchon = 0;

    public void loop() throws GameActionException {
        if (target == null || rc.getLocation().distanceSquaredTo(target) < 10) {
            target = enemyArchons[enemyArchon];

            enemyArchon = (enemyArchon + 1) % enemyArchons.length;
        }

        RobotInfo[] enemies = rc.senseNearbyRobots(mySensorRadius, enemy);
        RobotInfo[] allies = rc.senseNearbyRobots(mySensorRadius, us);

        Util.tryMove(rc.getLocation().directionTo(target));
        if (enemies.length > 0) {
            Util.shootAtEnemies(enemies, allies);
        }
    }
}
