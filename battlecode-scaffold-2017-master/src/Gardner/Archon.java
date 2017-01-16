package Gardner;

import battlecode.common.*;

public strictfp class Archon extends Unit {
    static int gardnerCount = 0;
    static Direction direction = Util.randomDirection();

    public void loop() throws GameActionException {
        if (rc.hasRobotBuildRequirements(RobotType.GARDENER) && Unit.rc.readBroadcast(Constants.gardnerCount) < 3 || (rc.getTeamBullets() > 300 && Util.getUpdatingChannelCount(Constants.currentUnitGardnerCount1, Constants.getCurrentUnitGardnerCount2) < 2)) {
            for (int i = 0; i < 8; i++) {
                Direction dir = Util.getDirectionForInt(i);

                if (rc.canHireGardener(dir)) {
                    gardnerCount++;
                    rc.hireGardener(dir);
                    Util.updateChannel(Constants.gardnerCount);
                }
            }
        }

        if (Math.random() < 0.1) {
            direction = Util.randomDirection();
        }

        Util.tryMove(direction);
    }
}
