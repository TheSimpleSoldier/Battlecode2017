package Gardner;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class UnitGardner extends Unit {
    static MapLocation target;

    @Override
    public void loop() throws GameActionException {
        Util.updateUnitCountChannel(Constants.currentUnitGardnerCount1, Constants.getCurrentUnitGardnerCount2);

        if (target == null) {
            // take the center of the map!
            target = Util.combinedCOM(ourArchons, enemyArchons);
        }

        RobotInfo[] enemies = rc.senseNearbyRobots(mySensorRadius, enemy);
        RobotInfo[] allies = rc.senseNearbyRobots(mySensorRadius, us);

        // send out distress signal
        Util.GardnerDistressSignal(enemies, allies);

        if (Util.runAwayFromEnemies(enemies));

        else {
            if (Util.needScout()) {
                Util.createUnit(RobotType.SCOUT);
            } else {
                Util.createUnit(RobotType.TANK);
                if (rc.getLocation().distanceSquaredTo(target) > 10) {
                    Util.tryMove(rc.getLocation().directionTo(target));
                }
            }
        }
    }
}
