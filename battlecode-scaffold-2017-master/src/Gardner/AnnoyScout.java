package Gardner;

import battlecode.common.*;

public class AnnoyScout extends Unit {
    public static MapLocation target;
    public static int currentArchonTarget = 0;
    public static boolean scouted = false;

    @Override
    public void loop() throws GameActionException {
        // scout out the map
        if (!scouted && SearchScout.loop()) {
            return;
        } else {
            scouted = true;
        }

        if (target == null || rc.getLocation().distanceSquaredTo(target) < 10) {
            if (currentArchonTarget < enemyArchons.length) {
                target = enemyArchons[currentArchonTarget];
            }

            currentArchonTarget = (currentArchonTarget + 1) % (enemyArchons.length);
        }

        float dist = 4 + myType.strideRadius + myType.bodyRadius;
        BulletInfo[] bullets = rc.senseNearbyBullets(dist * dist);
        RobotInfo[] enemyUnits = rc.senseNearbyRobots(mySensorRadius, enemy);

        Direction dir = Util.closestDefenselessEnemy(enemyUnits);

        if (dir == null) {
            dir = rc.getLocation().directionTo(target);
        }

        Util.scoutMove(dir, bullets, enemyUnits);

        RobotInfo[] allies = rc.senseNearbyRobots(mySensorRadius, us);
    }
}
