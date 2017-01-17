package Gardner;

/**
 * Created by fredkneeland on 1/13/17.
 */
public class Constants {

    // point at which we ignor position messages
    public static final int roundCutOff = 25;
    public static final int farthestDistToRespondToAttack = 400;

    ///////////////////////////////////////////////
    ///
    /// Everything messaging!
    ///
    ///////////////////////////////////////////////

    // counter to make handling messages easy
    public static int count = 0;

    // channels
    public static final int gardnerCount = count++;
    public static final int soldierCount = count++;
    public static final int lumberJackCount = count++;
    public static final int scoutCount = count++;
    public static final int tankCount = count++;

    public static final int lowestMapX = count++;
    public static final int highestMapX = count++;
    public static final int lowestMapY = count++;
    public static final int highestMapY = count++;

    public static final int foundLowestMapX = count++;
    public static final int foundLowestMapY = count++;
    public static final int foundHighestMapX = count++;
    public static final int foundHighestMaxY = count++;

    // used to keep track of # of gardners building units
    public static final int currentUnitGardnerCount1 = count++;
    public static final int getCurrentUnitGardnerCount2 = count++;


    // used for keeping track of enemy Archons (save ID as we know there is a max of 3)
    public static final int enemyArchon1ID = count++;
    public static final int enemyArchon2ID = count++;
    public static final int enemyArchon3ID = count++;
    public static final int enemyArchon1Loc = count++;
    public static final int enemyArchon2Loc = count++;
    public static final int enemyArchon3Loc = count++;

    // keep track of up to ten enemy gardner locations
    public static final int enemyGardner1Loc = count++;
    public static final int enemyGardner2Loc = count++;
    public static final int enemyGardner3Loc = count++;
    public static final int enemyGardner4Loc = count++;
    public static final int enemyGardner5Loc = count++;
    public static final int enemyGardner6Loc = count++;
    public static final int enemyGardner7Loc = count++;
    public static final int enemyGardner8Loc = count++;
    public static final int enemyGardner9Loc = count++;
    public static final int enemyGardner10Loc = count++;

    public static final int[] enemyGardnerPositions = {
        enemyGardner1Loc,
        enemyGardner2Loc,
        enemyGardner3Loc,
        enemyGardner4Loc,
        enemyGardner5Loc,
        enemyGardner6Loc,
        enemyGardner7Loc,
        enemyGardner8Loc,
        enemyGardner9Loc,
        enemyGardner10Loc
    };

    // keep track of enemy trees
    public static final int enemyTree1Loc = count++;
    public static final int enemyTree2Loc = count++;
    public static final int enemyTree3Loc = count++;
    public static final int enemyTree4Loc = count++;
    public static final int enemyTree5Loc = count++;
    public static final int enemyTree6Loc = count++;
    public static final int enemyTree7Loc = count++;
    public static final int enemyTree8Loc = count++;
    public static final int enemyTree9Loc = count++;
    public static final int enemyTree10Loc = count++;

    public static final int[] enemyTreePositions = {
            enemyTree1Loc,
            enemyTree2Loc,
            enemyTree3Loc,
            enemyTree4Loc,
            enemyTree5Loc,
            enemyTree6Loc,
            enemyTree7Loc,
            enemyTree8Loc,
            enemyTree9Loc,
            enemyTree10Loc
    };


    // Distress Calls
    public static final int ArchonInDestressTimeStamp = count++;
    public static final int ArchonInDestress = count++;

    public static final int gardner1InDestressTimeStamp = count++;
    public static final int gardner2InDestressTimeStamp = count++;
    public static final int gardner3InDestressTimeStamp = count++;
    public static final int gardner4InDestressTimeStamp = count++;
    public static final int gardner5InDestressTimeStamp = count++;

    public static final int[] gardnerInDestressTimeStamps = {
            gardner1InDestressTimeStamp,
            gardner2InDestressTimeStamp,
            gardner3InDestressTimeStamp,
            gardner4InDestressTimeStamp,
            gardner5InDestressTimeStamp,
    };

    public static final int gardner1InDistress = count++;
    public static final int gardner2InDistress = count++;
    public static final int gardner3InDistress = count++;
    public static final int gardner4InDistress = count++;
    public static final int gardner5InDistress = count++;

    public static final int[] gardnerInDistressLocs = {
            gardner1InDistress,
            gardner2InDistress,
            gardner3InDistress,
            gardner4InDistress,
            gardner5InDistress,
    };
}
