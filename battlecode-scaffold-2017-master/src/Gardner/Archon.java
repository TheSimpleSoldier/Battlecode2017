package Gardner;

import battlecode.common.*;

public strictfp class Archon extends Unit {
    public static Direction dir;
    public static MapLocation target;

    public void loop() throws GameActionException {

        if (target == null) {
            // take the center of the map!
            target = Util.combinedCOM(ourArchons, enemyArchons);
        }

        if (rc.hasRobotBuildRequirements(RobotType.GARDENER) && Unit.rc.readBroadcast(Constants.gardnerCount) < 3 || (rc.getTeamBullets() > 300 && Util.getUpdatingChannelCount(Constants.currentUnitGardnerCount1, Constants.getCurrentUnitGardnerCount2) < 2)) {
            for (int i = 0; i < 8; i++) {
                Direction dir = Util.getDirectionForInt(i);

                if (rc.canHireGardener(dir)) {
                    rc.hireGardener(dir);
                    Util.updateChannel(Constants.gardnerCount);
                }
            }
        }

        RobotInfo[] enemies = rc.senseNearbyRobots(mySensorRadius, enemy);
        RobotInfo[] allies = rc.senseNearbyRobots(mySensorRadius, us);

        // send out distress code
        Util.ArchonDistressSignal(enemies, allies);

        // if there are enemies run away from them
        dir = null;

        if (enemies.length > 0) {
            dir = rc.getLocation().directionTo(Util.unitCOM(enemies)).opposite();
        } else {
            dir = rc.getLocation().directionTo(target);
        }

        Util.tryMove(dir);
    }
}
