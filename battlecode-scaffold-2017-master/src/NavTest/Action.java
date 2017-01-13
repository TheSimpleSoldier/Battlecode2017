package NavTest;

import battlecode.common.Direction;
import battlecode.common.MapLocation;

/**
 * Created by joshua on 1/12/17.
 */
public class Action {

    public boolean shouldMove;
    public boolean shouldAttack;
    public boolean shouldShake;

    public MapLocation moveLocation;
    public Direction attackDirection;
    public int shakeID;

    public Action()
    {
        shouldMove = false;
        shouldAttack = false;
        shouldShake = false;
        moveLocation = null;
        attackDirection = null;
        shakeID = -1;
    }
}
