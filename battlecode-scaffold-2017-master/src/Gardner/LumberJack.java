package Gardner;

import battlecode.common.*;

public strictfp class LumberJack extends Unit {
    static int currentEnemyArchon = 0;

    public void loop() throws GameActionException {
        Util.LumberJackAttack();

        TreeInfo[] trees = rc.senseNearbyTrees();

            for (int i = trees.length; --i >= 0; ) {
                TreeInfo tree = trees[i];

                if ((tree.getTeam() == Team.NEUTRAL || tree.getTeam() == rc.getTeam().opponent()) && rc.canChop(tree.getID())) {
                    rc.chop(tree.getID());
                }
            }

            // No close robots, so search for robots within sight radius
            RobotInfo[] robots = rc.senseNearbyRobots(-1, enemy);

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
