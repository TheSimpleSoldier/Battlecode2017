package NavTest;

import battlecode.common.*;

public class LocalLumberJack extends Unit {
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
                float closestTreeDist = Float.MAX_VALUE;
                MapLocation closestTree = null;

                for (int i = trees.length; --i>=0; ) {
                    if (trees[i].getTeam() != rc.getTeam() && trees[i].location.distanceSquaredTo(rc.getLocation()) < closestTreeDist) {
                        closestTree = trees[i].location;
                        closestTreeDist = trees[i].location.distanceSquaredTo(rc.getLocation());
                    }
                }

                if (closestTree != null) {
                    Util.tryMove(rc.getLocation().directionTo(closestTree));
                }
            }
        }
    }
}
