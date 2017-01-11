package Gardner;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;

public class Archon extends Unit {
    static int gardnerCount = 0;
    static Direction direction = Util.randomDirection();

    public  void loop() throws GameActionException {
        if (gardnerCount < 2) {
            for (int i = 0; i < 8; i++) {
                Direction dir = Util.getDirectionForInt(i);

                if (rc.canHireGardener(dir)) {
                    gardnerCount++;
                    rc.hireGardener(dir);
                }
            }
        }

        if (Math.random() < 0.1) {
            direction = Util.randomDirection();
        }

        Util.tryMove(direction);
    }
}
