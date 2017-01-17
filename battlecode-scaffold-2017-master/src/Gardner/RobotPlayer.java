package Gardner;
import battlecode.common.*;

public strictfp class RobotPlayer {

    static RobotController rc;
    static boolean searchingForEdges = true;
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
                if (rc.readBroadcast(Constants.gardnerCount) <= 3) {
                    unit = new TreeGardner();
                } else {
                    unit = new UnitGardner();
                }

                break;
            case LUMBERJACK:
                unit = new LocalLumberJack();
                break;
            case SCOUT:
                unit = new AnnoyScout();
                break;
            case TANK:
                unit = new Tank();
                break;
            case SOLDIER:
                unit = new Tank();
                break;
            default:
                unit = new Unit();
        }

        while(true) {
            try {
                if (rc.getTeamBullets() >= 10000) {
                    rc.donate(rc.getTeamBullets());
                } else if (rc.getRoundLimit() - rc.getRoundNum() < 5 ) {
                    // donate all our bullets at the end of the game
                    rc.donate((int)(rc.getTeamBullets() / 10) * 10);
                }

                // find map bounds
                if (searchingForEdges && Util.scanForMapEdges());
                else searchingForEdges = false;

                unit.loop();
            } catch (Exception e) {
                System.out.println("Exception");
                e.printStackTrace();
            }

            Clock.yield();
        }
    }
}
