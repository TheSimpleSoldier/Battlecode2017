package Gardner;

import battlecode.common.*;

public class Scout extends Unit {
    public static MapLocation target;
    public static int currentArchonTarget = 0;

    public void loop() throws GameActionException {

        if (target == null || rc.getLocation().distanceSquaredTo(target) < 10) {
            MapLocation[] enemyArchons =  rc.getInitialArchonLocations(enemy);
            if (currentArchonTarget < enemyArchons.length) {
                target = enemyArchons[currentArchonTarget];
            } else if (currentArchonTarget == enemyArchons.length) {
                target = Util.furthestCorner(Util.com(rc.getInitialArchonLocations(rc.getTeam())), enemyArchons);
            } else {
                target = rc.getLocation().add(Util.randomDirection(), 10);
            }

            currentArchonTarget = (currentArchonTarget + 1) % (enemyArchons.length + 2);
        }

        BulletInfo[] bullets = rc.senseNearbyBullets();
        RobotInfo[] enemyUnits = rc.senseNearbyRobots(rc.getType().sensorRadius, enemy);

        float leastDamage = Float.MAX_VALUE;
        Direction bestDir = null;
        MapLocation bestLoc = null;

        if (bullets.length > 0 || enemyUnits.length > 0) {
            for (int i = 0; i < 16; i++) {
                Direction dir = new Direction(i / 8 * (float)Math.PI);

                MapLocation next = rc.getLocation().add(dir, rc.getType().strideRadius);

                float damage = Util.PotentialDamageFromLocation(next, bullets, enemyUnits, 1);

                if (damage < leastDamage) {
                    leastDamage = damage;
                    bestDir = dir;
                    bestLoc = next;
                } else if (damage == leastDamage && (next.distanceSquaredTo(target) < bestLoc.distanceSquaredTo(target))) {
                    bestDir = dir;
                    bestLoc = next;
                }
            }
        }

        if (bestDir == null) {
            bestDir = rc.getLocation().directionTo(target);
            leastDamage = -1;
        }

        // move
        if (leastDamage <= Util.PotentialDamageFromLocation(rc.getLocation(), bullets, enemyUnits, 1)) {
            Util.tryMove(bestDir);
        }


        // shoot
//        if ()
    }
}
