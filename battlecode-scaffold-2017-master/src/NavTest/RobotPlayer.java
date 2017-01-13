package NavTest;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public strictfp class RobotPlayer {

    static RobotController rc;
    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * If this method returns, the robot dies!
     **/
    @SuppressWarnings("unused")
    public static void run(RobotController robotController) throws GameActionException {

        // This is the RobotController object. You use it to perform actions from this robot,
        // and to get information on its current status.
        rc = robotController;
        Unit unit;


        // Here, we've separated the controls into a different method for each RobotType.
        // You can add the missing ones or rewrite this into your own control structure.
        switch (rc.getType()) {
            case ARCHON:
                unit = new Archon();
                break;
            case GARDENER:
                unit = new Gardner();
                break;
            case LUMBERJACK:
                unit = new LocalLumberJack();
                break;
            case SCOUT:
                unit = new Scout();
                break;
            default:
                unit = new Unit();
        }

        while(true) {
            try {
                if (rc.getTeamBullets() >= 10000) {
                    rc.donate(rc.getTeamBullets());
                }
                unit.loop();
            } catch (Exception e) {
                System.out.println("Exception");
                e.printStackTrace();
            }

            Clock.yield();
        }
    }
}
