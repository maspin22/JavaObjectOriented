package game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import gui.GUI;

import student.DiverMax;

public class GameState implements FindState, GetOutState {

    private enum Stage {
        FIND, GETOUT;
    }

    @SuppressWarnings("serial")
    private static class OutOfTimeException extends RuntimeException {
    }

    static boolean shouldPrint= true;

    public static final int MIN_ROWS= 8;
    public static final int MAX_ROWS= 25;
    public static final int MIN_COLS= 12;
    public static final int MAX_COLS= 40;

    public static final long FIND_TIMEOUT= 10;  // time out time for find phase
    public static final long GETOUT_TIMEOUT= 15;  // time out of time for get-out phas

    public static final double MIN_BONUS= 1.0;
    public static final double MAX_BONUS= 1.3;

    private static final double EXTRA_TIME_FACTOR= 0.3; //bigger is nicer - addition to total multiplier
    private static final double NO_BONUS_LENGTH= 3;

    private final Sewers findSewer;
    private final Sewers getOutSewer;

    private final SewerDiver sewerDiver;
    private final Optional<GUI> gui;

    private final long seed;

    private Node position;
    private int stepsTaken;
    private int stepsRemaining;
    private int coinsCollected;

    private Stage stage;
    private boolean findSucceeded= false;
    private boolean getOutSucceeded= false;
    private boolean findErred= false;
    private boolean getOutErred= false;
    private boolean findTimedOut= false;
    private boolean getOutTimedOut= false;

    private int minFindDistance;
    private int minGetOutDistance;

    private int findDistanceLeft= 0;
    private int getOutDistanceLeft= 0;

    private int minTimeToFind;

    /** = "get out succeeded" */
    public boolean getOutSucceeded() {
        return getOutSucceeded;
    }

    /** Constructor: a new GameState object for sewerDiver sd.
     * This constructor takes a path to files
     *  storing serialized sewers and simply loads these sewers.
     */
    /* package */ GameState(Path findSewerPath, Path getOutSewerPath, SewerDiver sd) throws IOException {
        findSewer= Sewers.deserialize(Files.readAllLines(findSewerPath));
        minTimeToFind= findSewer.minPathLengthToTarget(findSewer.getEntrance());
        getOutSewer= Sewers.deserialize(Files.readAllLines(getOutSewerPath));

        sewerDiver= sd;

        position= findSewer.getEntrance();
        stepsTaken= 0;
        stepsRemaining= Integer.MAX_VALUE;
        coinsCollected= 0;

        seed= -1;

        stage= Stage.FIND;
        gui= Optional.of(new GUI(findSewer, position.getTile().getRow(), 
                position.getTile().getColumn(), 0, this));
    }

    /** Constructor: a new random game instance with or without a GUI. */
    private GameState(boolean useGui, SewerDiver sd) {
        this((new Random()).nextLong(), useGui, sd);
    }

    /** Constructor: a new game instance using seed seed with or without a GUI,
     *  and with sewerDiver sd used to solve the game. */
    /* package */ GameState(long seed, boolean useGui, SewerDiver sd) {
        Random rand = new Random(seed);
        int ROWS = rand.nextInt(MAX_ROWS - MIN_ROWS + 1) + MIN_ROWS; 
        int COLS = rand.nextInt(MAX_COLS - MIN_COLS + 1) + MIN_COLS;
        findSewer= Sewers.digExploreSewer(ROWS, COLS, rand);
        minTimeToFind= findSewer.minPathLengthToTarget(findSewer.getEntrance());
        Tile ringTile = findSewer.getTarget().getTile();
        getOutSewer= Sewers.digGetOutSewer(ROWS, COLS,  ringTile.getRow(), ringTile.getColumn(), rand);

        position= findSewer.getEntrance();
        stepsTaken= 0;
        stepsRemaining= Integer.MAX_VALUE;
        coinsCollected= 0;

        sewerDiver= sd;
        stage= Stage.FIND;

        this.seed = seed;

        if (useGui) {
            gui= Optional.of(new GUI(findSewer, position.getTile().getRow(),
                    position.getTile().getColumn(), seed, this));
        } else {
            gui= Optional.empty();
        }
    }

    /** Run through the game, one step at a time.
     *  Will  run getOut() only if find() succeeds.
     *  Will fail in case of timeout.  */
    void runWithTimeLimit() {
        findWithTimeLimit();
        if (!findSucceeded) {
            findDistanceLeft= findSewer.minPathLengthToTarget(position);
            getOutDistanceLeft= getOutSewer.minPathLengthToTarget(getOutSewer.getEntrance());
        }
        else {
            getOutWithTimeLimit();
            if (!getOutSucceeded) {
                getOutDistanceLeft= getOutSewer.minPathLengthToTarget(position);
            }
        }
    }

    /** Run through the game, one step at a time.
     *  Will run getOut() only if find() succeeds.
     *  Does not use a timeout and will wait as long as necessary.
     */
    void run(){
        find();
        if (!findSucceeded){
            findDistanceLeft= findSewer.minPathLengthToTarget(position);
            getOutDistanceLeft= getOutSewer.minPathLengthToTarget(getOutSewer.getEntrance());
        }
        else {
            getOut();
            if (!getOutSucceeded) {
                getOutDistanceLeft= getOutSewer.minPathLengthToTarget(position);
            }
        }
    }

    /** Run only the Find mode. Uses timeout. */
    void runFindWithTimeout() {
        findWithTimeLimit();
        if (!findSucceeded) {
            findDistanceLeft= findSewer.minPathLengthToTarget(position);
        }
    }

    /** Run only the get-out mode. Uses timeout. */
    void runGetOutWithTimeout() {
        getOutWithTimeLimit();
        if (!getOutSucceeded) {
            getOutDistanceLeft= getOutSewer.minPathLengthToTarget(position);
        }
    }

    @SuppressWarnings("deprecation")
    /** Wrap a call to find() with the timeout functionality. */
    private void findWithTimeLimit() {
        FutureTask<Void> ft = new FutureTask<Void>(new Callable<Void>() {
            @Override
            public Void call() {
                find();
                return null;
            }
        });

        Thread t= new Thread(ft);
        t.start();
        try {
            ft.get(FIND_TIMEOUT, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            t.stop();
            findTimedOut= true;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("ERROR");
            // Shouldn't happen
        }
    }

    /** Run the sewerDiver's find() function with no timeout. */
    /* package */ void find() {
        stage= Stage.FIND;
        stepsTaken= 0;
        findSucceeded= false;
        position= findSewer.getEntrance();
        minFindDistance= findSewer.minPathLengthToTarget(position);
        gui.ifPresent((g) -> g.setLighting(false));
        gui.ifPresent((g) -> g.updateSewer(findSewer, 0));
        gui.ifPresent((g) -> g.moveTo(position));

        try {
            sewerDiver.findRing(this);
            //Verify that we returned at the correct location
            if (position.equals(findSewer.getTarget())) {
                findSucceeded= true;
            } else {
                errPrintln("Your solution to find returned at the wrong location.");
                gui.ifPresent((g) -> g.displayError("Your solution to find returned at the wrong location."));
            }
        } catch (Throwable t) {
            if (t instanceof ThreadDeath) return;
            errPrintln("Your code errored during the find phase.");
            gui.ifPresent((g) -> g.displayError("Your code errored during the find phase. Please see console output."));
            errPrintln("Here is the error that occurred.");
            t.printStackTrace();
            findErred= true;
        }
    }

    @SuppressWarnings("deprecation")
    /** Wraps a call to escape() with the timeout functionality. */
    private void getOutWithTimeLimit() {
        FutureTask<Void> ft= new FutureTask<Void>(new Callable<Void>() {
            @Override
            public Void call() {
                getOut();
                return null;
            }
        });

        Thread t= new Thread(ft);
        t.start();
        try {
            ft.get(GETOUT_TIMEOUT, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            t.stop();
            getOutTimedOut= true;
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("ERROR"); // Shouldn't happen
        }
    }

    /** Handle the logic for running the sewerDiver's getOut() procedure with no timeout. */
    /* package */ void getOut() {
        stage= Stage.GETOUT;
        Tile ringTile= findSewer.getTarget().getTile();
        position= getOutSewer.getNodeAt(ringTile.getRow(), ringTile.getColumn());
        minGetOutDistance= getOutSewer.minPathLengthToTarget(position);
        stepsRemaining= computeTimeToGetOut();
        gui.ifPresent((g) -> g.getOptionsPanel().changePhaseLabel("get-out phase"));
        gui.ifPresent((g) -> g.setLighting(true));
        gui.ifPresent((g) -> g.updateSewer(getOutSewer, stepsRemaining));

        try {
            sewerDiver.getOut(this);
            //Verify that the diver returned at the correct location
            if (!position.equals(getOutSewer.getTarget())) {
                errPrintln("Your solution to get-out returned at the wrong location.");
                gui.ifPresent((g) -> g.displayError("Your solution to get-out returned at the wrong location."));
                return;
            }

            getOutSucceeded= true;
            gui.ifPresent((g) -> g.getOptionsPanel().changePhaseLabel("Got out!"));
            System.out.println("Get Out Succeeded!");
            // Since the exit has been reached, turn off painting the
            GUI g= gui.isPresent() ? gui.get() : null;
            gui.MazePanel mp= g == null ? null : g.getMazePanel();
            if (mp != null) mp.repaint();


        } catch (OutOfTimeException e) {
            errPrintln("Your solution to get-out ran out of steps before returning!");
            gui.ifPresent((g) -> g.displayError("Your solution to get-out ran out of steps before returning!"));
        } catch (Throwable t) {
            if (t instanceof ThreadDeath) return;
            errPrintln("Your code errored during the get-out phase.");
            gui.ifPresent((g) -> g.displayError("Your code errored during the get-out phase. Please see console output."));
            t.printStackTrace();
            getOutErred= true;
        }

        outPrintln("Coins collected   : " + getCoinsCollected());
        DecimalFormat df= new DecimalFormat("#.##");
        outPrintln("Bonus multiplier : " + df.format(computeBonusFactor()));
        outPrintln("Score            : " + getScore());
    }

    /** Making sure the sewerDiver always has the minimum time needed to get out, add a 
     *  factor of extra time proportional to the size of the sewer.
     */
    private int computeTimeToGetOut() {
        int minTimeToGetOut= getOutSewer.minPathLengthToTarget(position);
        return (int)(minTimeToGetOut + EXTRA_TIME_FACTOR * 
                (Sewers.MAX_EDGE_WEIGHT + 1) * getOutSewer.numOpenTiles() / 2);

    }

    /** Compare the sewerDiver's performance on the getOut() stage to the theoretical minimum,
     *  compute their bonus factor on a call from MIN_BONUS to MAX_BONUS.
     *  Bonus should be minimum if take longer than NO_BONUS_LENGTH times optimal.
     */
    private double computeBonusFactor(){
        double findDiff= (stepsTaken - minTimeToFind) / (double) minTimeToFind;
        if (findDiff <= 0) return MAX_BONUS;
        double multDiff= MAX_BONUS - MIN_BONUS;
        return Math.max(MIN_BONUS, MAX_BONUS - findDiff / NO_BONUS_LENGTH * multDiff);
    }

    /** See moveTo(Node&lt;TileData&gt; n)
     * @param id The Id of the neighboring Node to move to */
    @Override public void moveTo(long id) {
        if (stage != Stage.FIND) {
            throw new IllegalStateException("moveTo(ID) can only be called while getting out!");
        }

        for (Node n : position.getNeighbors()) {
            if (n.getId() == id) {
                position= n;
                stepsTaken++;
                gui.ifPresent((g) -> g.updateBonus(computeBonusFactor()));
                gui.ifPresent((g) -> g.moveTo(n));
                return;
            }
        }
        throw new IllegalArgumentException("moveTo: Node must be adjacent to position");
    }

    /**  Return the unique id of the current location. */
    @Override public long currentLocation() {
        if (stage != Stage.FIND) {
            throw new IllegalStateException("getLocation() can only be called while getting out!");
        }

        return position.getId();
    }

    /**  Return a collection of NodeStatus objects that contain the unique ID of the node
     * and the distance from that node to the target. */
    @Override public Collection<NodeStatus> neighbors() {
        if (stage != Stage.FIND) {
            throw new IllegalStateException("getNeighbors() can only be called while getting out!");
        }

        Collection<NodeStatus> options= new ArrayList<>();
        for (Node n : position.getNeighbors()) {
            int distance= computeDistanceToTarget(n.getTile().getRow(), n.getTile().getColumn());
            options.add(new NodeStatus(n.getId(), distance));
        }
        return options;
    }

    /** Return the Manhattan distance from (row, col) to the target */
    private int computeDistanceToTarget(int row, int col) {
        return Math.abs(row - findSewer.getTarget().getTile().getRow())
                + Math.abs(col - findSewer.getTarget().getTile().getColumn());
    }

    /** Return the Manhattan distance from the current location to the target location on the map. */
    @Override public int distanceToRing() {
        if (stage != Stage.FIND) {
            throw new IllegalStateException("getDistanceToTarget() can only be called while getting out!");
        }

        return computeDistanceToTarget(position.getTile().getRow(), position.getTile().getColumn());
    }

    @Override
    public Node currentNode() {
        if (stage != Stage.GETOUT) {
            throw new IllegalStateException("getCurrentNode: Error, " +
                    "current Node may not be accessed unless in GETOUT");
        }
        return position;
    }

    @Override public Node getExit() {
        if (stage != Stage.GETOUT) {
            throw new IllegalStateException("getEntrance: Error, "+
                    "current Node may not be accessed unless in GETOUT");
        }
        return getOutSewer.getTarget();
    }

    @Override public Collection<Node> allNodes() {
        if (stage != Stage.GETOUT) {
            throw new IllegalStateException("getVertices: Error, "+
                    "Vertices may not be accessed unless in GETOUT");
        }
        return Collections.unmodifiableSet(getOutSewer.getGraph());
    }

    /** Attempt to move the sewerDiver from the current position to
     * the <tt>Node</tt> <tt>n</tt>. Throws an <tt>IllegalArgumentException</tt>
     * if <tt>n</tt> is not neighboring. Increment the steps taken
     * if successful. */
    @Override public void moveTo(Node n) {
        if (stage != Stage.GETOUT) {
            throw new IllegalStateException("Call moveTo(Node) only when getting out!");
        } 
        int distance= position.getEdge(n).length;
        if (stepsRemaining - distance < 0) throw new OutOfTimeException();

        if (!position.getNeighbors().contains(n)) 
            throw new IllegalArgumentException("moveTo: Node must be adjacent to position");
        position= n;
        stepsRemaining -= distance;
        gui.ifPresent((g) -> g.updateTimeLeft(stepsRemaining));
        gui.ifPresent((g) -> {g.moveTo(n);});
        grabCoins();
    }

    /** Do not call method grabCoins in DiverMax. Coins on a Node n are picked
     * up automatically when a call moveTo(n) is executed. */
    @Override public void grabCoins() {
        if (stage != Stage.GETOUT) {
            throw new IllegalStateException("Call grabCoins() only when getting out!");
        }
        coinsCollected += position.getTile().takeCoins();
        gui.ifPresent((g) -> g.updateCoins(coinsCollected, getScore()));
    }

    @Override public int stepsLeft() {
        if (stage != Stage.GETOUT) {
            throw new IllegalStateException("getTimeRemaining() can only be called while getting out!");
        }
        return stepsRemaining;
    }

    /* package */ int getCoinsCollected() {
        return coinsCollected;
    }

    /** Return the player's current score.  */
    /* package */ int getScore() {
        return (int)(computeBonusFactor() * coinsCollected);
    }

    /* package */ boolean getFindSucceeded() {
        return findSucceeded;
    }

    /* package */ boolean getGetOutSucceeded() {
        return getOutSucceeded;
    }

    /* package */ boolean getFindErrored() {
        return findErred;
    }

    /* package */ boolean getGetOutErrored() {
        return getOutErred;
    }

    /* package */ boolean getFindTimeout() {
        return findTimedOut;
    }

    /* package */ boolean getGetOutTimeout() {
        return getOutTimedOut;
    }

    /* package */ int getMinFindDistance() {
        return minFindDistance;
    }

    /* package */ int getMinGetOutDistance() {
        return minGetOutDistance;
    }

    /* package */ int getFindDistanceLeft() {
        return findDistanceLeft;
    }

    /* package */ int getGetOutDistanceLeft() {
        return getOutDistanceLeft;
    }

    /** Given seed, whether or not to use the GUI, and an instance of a solution to use,
     *  run the game.
     */
    public static int runNewGame(long seed, boolean useGui, SewerDiver solution) {
        GameState state;
        if (seed != 0) {
            state= new GameState(seed, useGui, solution);
        } else {
            state= new GameState(useGui, solution);
        }
        outPrintln("Seed : " + state.seed);
        state.run();
        return state.getScore();
    }
    /** Execute find-ring and get-out on a random seed, except that:
     *  (1) If there is a parameter -s <seed>, run on that seed  OR
     *  (2) If there is a parameter -n <count>, run count times on random seeds.
     */
    public static void main(String[] args) throws IOException {
        List<String> argList= new ArrayList<String>(Arrays.asList(args));
        int repeatNumberIndex= argList.indexOf("-n");
        int numTimesToRun= 1;
        if (repeatNumberIndex >= 0) {
            try {
                numTimesToRun= Math.max(Integer.parseInt(argList.get(repeatNumberIndex + 1)), 1);
            }
            catch (Exception e) {
                // numTimesToRun = 1
            }
        }
        int seedIndex = argList.indexOf("-s");
        long seed= 0;
        if (seedIndex >= 0) {
            try {
                seed= Long.parseLong(argList.get(seedIndex + 1));
            }
            catch (NumberFormatException e) {
                errPrintln("Error, -s must be followed by a numerical seed");
                return;
            }
            catch (ArrayIndexOutOfBoundsException e) {
                errPrintln("Error, -s must be followed by a seed");
                return;
            }
        }

        int totalScore= 0;
        for (int i= 0; i < numTimesToRun; i++) {
            totalScore += runNewGame(seed, false, new DiverMax());
            if (seed != 0) seed = new Random(seed).nextLong();
            outPrintln("");
        }

        outPrintln("Average score : " + totalScore / numTimesToRun);
    }

    static void outPrintln(String s) {
        if (shouldPrint) System.out.println(s);
    }

    static void errPrintln(String s) {
        if (shouldPrint) System.err.println(s);
    }
}
