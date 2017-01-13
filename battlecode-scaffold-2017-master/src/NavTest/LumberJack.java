package NavTest;

import battlecode.common.*;


public strictfp class LumberJack extends Unit {
    static int currentEnemyArchon = 0;

    public void loop() throws GameActionException {
        // See if there are any enemy robots within striking range (distance 1 from lumberjack's radius)
        RobotInfo[] robots = rc.senseNearbyRobots(RobotType.LUMBERJACK.bodyRadius+ GameConstants.LUMBERJACK_STRIKE_RADIUS, enemy);

        if (robots.length > 0 && !rc.hasAttacked()) {
            // Use strike() to hit all nearby robots!
            rc.strike();
        } else {

            TreeInfo[] trees = rc.senseNearbyTrees();

            for (int i = trees.length; --i >= 0; ) {
                TreeInfo tree = trees[i];

                if ((tree.getTeam() == Team.NEUTRAL || tree.getTeam() == rc.getTeam().opponent()) && rc.canChop(tree.getID())) {
                    rc.chop(tree.getID());
                }
            }

            // No close robots, so search for robots within sight radius
            robots = rc.senseNearbyRobots(-1, enemy);

            // If there is a robot, move towards it
            if(robots.length > 0) {
                MapLocation myLocation = rc.getLocation();
                MapLocation enemyLocation = robots[0].getLocation();
                Direction toEnemy = myLocation.directionTo(enemyLocation);

                Util.tryMove(toEnemy);
            } else {
                MapLocation[] enemyArchons = rc.getInitialArchonLocations(enemy);

                MapLocation enemyArchon = enemyArchons[currentEnemyArchon];

                if (rc.getLocation().distanceSquaredTo(enemyArchon) < 10) {
                    currentEnemyArchon = (currentEnemyArchon + 1) % rc.getInitialArchonLocations(enemy).length;
                }
                Direction dir = rc.getLocation().directionTo(enemyArchon);

                // Move Randomly
                Util.tryMove(dir);
            }
        }
    }

}