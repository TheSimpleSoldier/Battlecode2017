package NavTest;

import battlecode.common.*;

public strictfp class Util {

    // 3 points 1 and 2 on a line, 3 point off line
    public static int turnBulletWillHitLocation(MapLocation bulletSpot, MapLocation target, float speed, Direction direction, int radius) {
        MapLocation next = bulletSpot.add(direction, 5);
        float y1 = bulletSpot.y;
        float y2 = next.y;
        float y3 = target.y;
        float x1 = bulletSpot.x;
        float x2 = next.x;
        float x3 = target.x;

        float dist = Math.abs((y2 - y1) * x3 - (x2 - x1) * y3 + x2 * y1 - y2 * x1);
        dist /= Math.sqrt(Math.pow((y2 - y1), 2) + Math.pow((x2 - x1), 2));

        if (dist > radius) {
            return -1;
        }

        return (int) (dist / speed);
    }

    /**
     * Causes a unit to water any trees around it that need it
     *
     * @throws GameActionException
     */
    public static void waterTrees() throws GameActionException{
        TreeInfo[] trees = Unit.rc.senseNearbyTrees();
        for (int i = trees.length; --i >=0; ) {
            TreeInfo tree = trees[i];


            if (tree.getTeam() == Unit.rc.getTeam()) {
                if (tree.getHealth() < 40) {
                    if (Unit.rc.canWater(tree.getID())) {
                        Unit.rc.water(tree.getID());
                    } else {
                        System.out.println("cant water tree");
                    }
                }
            }
        }
    }

    public static void moveToWaterTrees() throws GameActionException {
        TreeInfo[] trees = Unit.rc.senseNearbyTrees();

        float weakestTreeHealth = 40;
        TreeInfo weakestTree = null;

        for (int i = trees.length; --i >=0; ) {
            TreeInfo tree = trees[i];

            if (tree.getTeam() == Unit.rc.getTeam()) {// && Unit.rc.getLocation().distanceSquaredTo(tree.location) < 5) {
                if (tree.getHealth() < weakestTreeHealth) {
                    weakestTree = tree;
                    weakestTreeHealth = tree.getHealth();
                }
            }
        }

        if (weakestTree != null) {
            System.out.println("moving towards weakest tree");
            if (Unit.rc.canWater(weakestTree.getID())) {
                Unit.rc.water(weakestTree.getID());
            } else {
                tryMove(Unit.rc.getLocation().directionTo(weakestTree.location));
            }
        }
    }

    /**
     * Returns a random Direction
     * @return a random Direction
     */
    static Direction randomDirection() {
        return new Direction((float)Math.random() * 2 * (float)Math.PI);
    }

    /**
     * Returns a direction
     */
    static Direction getDirectionForInt(int dir) {
        return new Direction((float)dir / 8 * 2 * (float)Math.PI);
    }

    /**
     * Attempts to move in a given direction, while avoiding small obstacles directly in the path.
     *
     * @param dir The intended direction of movement
     * @return true if a move was performed
     * @throws GameActionException
     */
    static boolean tryMove(Direction dir) throws GameActionException {
        return tryMove(dir,20,3);
    }

    /**
     * Attempts to move in a given direction, while avoiding small obstacles direction in the path.
     *
     * @param dir The intended direction of movement
     * @param degreeOffset Spacing between checked directions (degrees)
     * @param checksPerSide Number of extra directions checked on each side, if intended direction was unavailable
     * @return true if a move was performed
     * @throws GameActionException
     */
    static boolean tryMove(Direction dir, float degreeOffset, int checksPerSide) throws GameActionException {

        // First, try intended direction
        if (Unit.rc.canMove(dir)) {
            Unit.rc.move(dir);
            return true;
        }

        // Now try a bunch of similar angles
        boolean moved = false;
        int currentCheck = 1;

        while(currentCheck<=checksPerSide) {
            // Try the offset of the left side
            if(Unit.rc.canMove(dir.rotateLeftDegrees(degreeOffset*currentCheck))) {
                Unit.rc.move(dir.rotateLeftDegrees(degreeOffset*currentCheck));
                return true;
            }
            // Try the offset on the right side
            if(Unit.rc.canMove(dir.rotateRightDegrees(degreeOffset*currentCheck))) {
                Unit.rc.move(dir.rotateRightDegrees(degreeOffset*currentCheck));
                return true;
            }
            // No move performed, try slightly further
            currentCheck++;
        }

        // A move never happened, so return false.
        return false;
    }

    /**
     * A slightly more complicated example function, this returns true if the given bullet is on a collision
     * course with the current robot. Doesn't take into account objects between the bullet and this robot.
     *
     * @param bullet The bullet in question
     * @return True if the line of the bullet's path intersects with this robot's current position.
     */
    static boolean willCollideWithMe(BulletInfo bullet) {
        MapLocation myLocation = Unit.rc.getLocation();

        // Get relevant bullet information
        Direction propagationDirection = bullet.dir;
        MapLocation bulletLocation = bullet.location;

        // Calculate bullet relations to this robot
        Direction directionToRobot = bulletLocation.directionTo(myLocation);
        float distToRobot = bulletLocation.distanceTo(myLocation);
        float theta = propagationDirection.radiansBetween(directionToRobot);

        // If theta > 90 degrees, then the bullet is traveling away from us and we can break early
        if (Math.abs(theta) > Math.PI/2) {
            return false;
        }

        // distToRobot is our hypotenuse, theta is our angle, and we want to know this length of the opposite leg.
        // This is the distance of a line that goes from myLocation and intersects perpendicularly with propagationDirection.
        // This corresponds to the smallest radius circle centered at our location that would intersect with the
        // line that is the path of the bullet.
        float perpendicularDist = (float)Math.abs(distToRobot * Math.sin(theta)); // soh cah toa :)

        return (perpendicularDist <= Unit.rc.getType().bodyRadius);
    }
}
