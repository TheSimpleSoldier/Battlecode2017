package Gardner;

import battlecode.common.*;

public class TreeGardner extends Unit {
    static boolean establishedPosition = false;


    public void loop() throws GameActionException {
        RobotInfo[] enemies = rc.senseNearbyRobots(mySensorRadius, enemy);
        RobotInfo[] allies = rc.senseNearbyRobots(mySensorRadius, us);

        // send out distress signal if needed
        Util.GardnerDistressSignal(enemies, allies);

        TreeInfo[] trees = rc.senseNearbyTrees();
        Direction dir = null;
        boolean needUnit = false;

        if (!establishedPosition) {
            dir = Util.getClosestLocForTreeFarm(allies);
        }

        // create lumberjack to clear area
        if (Util.createLumberJackIfNeeded(trees, allies)) {
            Util.createUnit(RobotType.LUMBERJACK);
            needUnit = true;
        } else if (Util.needScout()) {
            Util.createUnit(RobotType.SCOUT);
            needUnit = true;
        }

        if (dir != null) {
            Util.tryMove(dir);
        } else {
            // grow and water trees
            Util.waterTrees();
            if (!needUnit) {
                Util.plantTree();
            }

            establishedPosition = true;
        }
    }
}
