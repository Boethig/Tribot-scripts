package scripts.boe_api.utilities;

import lombok.Getter;
import lombok.Setter;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.interfaces.Clickable;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.util.abc.ABCProperties;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSInterface;

/**
 * @author boe123
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Antiban {

    private static Antiban antiban;

    private Antiban() {
        General.useAntiBanCompliance(true);
        this.run_at = abc.generateRunActivation();
        this.eat_at = abc.generateEatAtHP();
        this.should_hover = abc.shouldHover();
        this.should_open_menu = abc.shouldOpenMenu();
    }

    public static synchronized Antiban get() {
        if (antiban == null) {
            antiban = new Antiban();
        }
        return antiban;
    }

    @Getter
    private final ABCUtil abc = new ABCUtil();

    private boolean print_debug = true;

    private boolean enableReactionSleep = true;

    @Getter @Setter
    private long last_under_attack_time = 0;

    @Getter @Setter
    private double abcMultiplier = 1.0;

    // Action Conditions
    @Getter
    private int run_at;

    @Getter
    private int eat_at;

    @Getter @Setter
    private int resources_won = 0;

    @Getter @Setter
    private int resources_lost = 0;

    @Getter
    private boolean should_hover;

    @Getter
    private boolean should_open_menu;


    private Positionable nextTarget;

    public void destroy() {
        abc.close();
    }

    public ABCProperties getProperties() {
        return abc.getProperties();
    }

    public int getWaitingTime() {
        return getProperties().getWaitingTime();
    }


    /**
     * Gets the reaction time that we should sleep for before performing our
     * next action. Examples:
     * <ul>
     * <li>Reacting to when our character stops fishing. The response time will
     * be used before we move on to the next fishing spot, or before we walk to
     * the bank.</li>
     * <li>Reacting to when our character stops mining. The response time will
     * be used before we move on to the next rock, or before we walk to the
     * bank.</li>
     * <li>Reacting to when our character kills our target NPC. The response
     * time will be used before we attack our next target, or before we walk to
     * the bank.</li>
     * </ul>
     *
     * @Return The reaction time.
     */
    public int getReactionTime() {
    	resetShouldHover();
		resetShouldOpenMenu();
		ABCProperties properties = getProperties();
		properties.setWaitingTime(getWaitingTime());
		properties.setHovering(should_hover);
		properties.setMenuOpen(should_open_menu);
		properties.setUnderAttack(Combat.isUnderAttack() || (Timing.currentTimeMillis() - last_under_attack_time < 2000));
		properties.setWaitingFixed(false);
        return abc.generateReactionTime();
    }

    public void setPrintDebug(boolean state) {
        print_debug = state;
    }

    public void incrementResourcesWon() {
        resources_won++;
    }

    public void incrementResourcesLost() {
        this.resources_lost++;
    }

    public void sleepReactionTime() {
        if (!enableReactionSleep) return;
        final int reaction_time = (int) (getReactionTime() * abcMultiplier);
        if (print_debug) {
            debug("Sleeping for: " + reaction_time + "ms.");
        }
        try {
            abc.sleep(reaction_time);
        } catch (InterruptedException e) {
            debug("Background thread interrupted sleep");
        }
    }
 
    /**
     * Generates the trackers for ABCUtil. Call this only after successfully
     * completing an action that has a dynamic wait time for the next action.
     *
     * @param estimated_wait
     *            The estimated wait time (in milliseconds) before the next
     *            action occurs.
     */
    public void generateTrackers(int estimated_wait) {
        final ABCProperties properties = getProperties();
        properties.setHovering(should_hover);
        properties.setMenuOpen(should_open_menu);
        properties.setWaitingFixed(false);
        properties.setUnderAttack(Combat.isUnderAttack());
        properties.setWaitingTime(estimated_wait);
        abc.generateTrackers();
    }
 
    /**
     * Resets the should_hover bool to match the ABCUtil value. This method
     * should be called after successfully clicking an entity.
     */
    public void resetShouldHover() {
        this.should_hover = abc.shouldHover();
    }

    public void resetEatAt() {
        this.eat_at = abc.generateEatAtHP();
    }
 
    /**
     * Resets the should_open_menu bool to match the ABCUtil value. This method
     * should be called after successfully clicking an entity.
     */
    public void resetShouldOpenMenu() {
        this.should_open_menu = abc.shouldOpenMenu() && abc.shouldHover();
    }

    public void moveCamera() {
        if (abc.shouldRotateCamera()) {
            if (print_debug) {
                debug("Rotated camera");
            }
            abc.rotateCamera();
        }
    }

    public void checkXp() {
        if (abc.shouldCheckXP()) {
            if (print_debug) {
                debug("Checked xp");
            }
            abc.checkXP();
        }
    }

    public void pickUpMouse() {
        if (abc.shouldPickupMouse()) {
            if (print_debug) {
                debug("Picked up mouse");
            }
            abc.pickupMouse();
        }
    }

    public void leaveGame() {
        if (abc.shouldLeaveGame()) {
            if (print_debug) {
                debug("Left game window");
            }
            abc.leaveGame();
        }
    }

    public void examineEntity() {
        if (abc.shouldExamineEntity()) {
            if (print_debug) {
                debug("Examined entity");
            }
            abc.examineEntity();
        }
    }

    public void rightClick() {
        if (abc.shouldRightClick()) {
            if (print_debug) {
                debug("Right clicked");
            }
            abc.rightClick();
        }
    }

    public void mouseMovement() {
        if (abc.shouldMoveMouse()) {
            if (print_debug) {
                debug("Mouse moved");
            }
            abc.moveMouse();
        }
    }

    public void checkTabs() {
        if (abc.shouldCheckTabs()) {
            if (print_debug) {
                debug("Tab checked");
            }
            abc.checkTabs();
        }
    }

    public void timedActions() {
        moveCamera();
        checkXp();
        pickUpMouse();
        rightClick();
        examineEntity();
        leaveGame();
        mouseMovement();
        checkTabs();
    }

    @SuppressWarnings("unchecked")
    public <T extends Positionable> T selectNextTarget(Positionable[] target) {
        return (T) (nextTarget = abc.selectNextTarget(target));
    }
 
    /**
     * Activates run. No action is taken if run is already enabled or the
     * current run energy is less than the value returned by ABCUtil.
     *
     * @Return True if run was enabled, false otherwise.
     */
    public boolean activateRun() {
        if (Game.getRunEnergy() >= run_at && !Game.isRunOn()) {
            Options.setRunEnabled(true);
            if (Options.setRunEnabled(true)) {
                if (print_debug) {
                    debug("Turned run on at " + run_at + "%");
                }
                run_at = abc.generateRunActivation();
                return true;
            }
        }
        return false;
    }
 
    /**
     * Eats/drinks an item in your inventory with the specified name if your
     * current HP percent is less than or equal to the value generated by
     * ABCUtil. Note that if there is any delay/lag that is longer than 3000
     * milliseconds between the time the food/drink was clicked and when your
     * players HP is changed the tracker will not be reset and you will have to
     * reset it manually.
     *
     * @param option
     *            The option to click the food/drink with (this is normally
     *            "Eat" or "Drink"). Input an empty string to have the method
     *            attempt to find the correct option automatically. Note that
     *            this is not guaranteed to execute properly if an empty string
     *            is inputted.
     * @param name
     *            The name of the food or drink.
     * @Return True if the food/drink was successfully eaten/drank, false
     *         otherwise.
     */
    public boolean eat(String option, final String name) {
        return eat(option, Inventory.getCount(name));
    }
 
    /**
     * Eats/drinks an item in your inventory with the specified ID if your
     * current HP percent is less than or equal to the value generated by
     * ABCUtil. Note that if there is any delay/lag that is longer than 3000
     * milliseconds between the time the food/drink was clicked and when your
     * players HP is changed the tracker will not be reset and you will have to
     * reset it manually.
     *
     * @param option
     *            The option to click the food/drink with (this is normally
     *            "Eat" or "Drink"). Input an empty string to have the method
     *            attempt to find the correct option automatically. Note that
     *            this is not guaranteed to execute properly if an empty string
     *            is inputted.
     * @param id
     *            The ID of the food or drink.
     * @Return True if the food/drink was successfully eaten/drank, false
     *         otherwise.
     */
    public boolean eat(String option, final int id) {
        return eat(option, Inventory.getCount(id));
    }
 
    /**
     * Checks to see if the player should switch resources. Note that this
     * method will only return correctly if you have been tracking the resources
     * you have won and lost. Note also that you must create the check time in
     * your script and reset it accordingly. e.g. to check if you should switch
     * resources, you should check the following condition:
     * <code>Timing.currentTimeMillis() >= check_time && AntiBan.shouldSwitchResources()</code>
     *
     * @param player_count
     *            The amount of players gathering resources near you.
     * @Return True if your player should switch resources, false otherwise.
     */
    public boolean shouldSwitchResources(int player_count) {
        double win_percent = ((double) (resources_won + resources_lost) / (double) resources_won);
        return win_percent < 50.0 && abc.shouldSwitchResources(player_count);
    }

    /**
     * Sleeps the current thread for the item interaction delay time. This
     * method should be called directly after interacting with an item in your
     * players inventory.
     */
    public void waitItemInteractionDelay() {
        General.sleep(General.randomSD(25,75, 15));
    }
 
    /**
     * Sleeps the current thread for the item interaction delay time multiplied
     * by the specified number of iterations. This method can be used to sleep
     * between certain actions that do not have a designated method already
     * assigned to them such as casting spells or clicking interfaces.
     * <p/>
     * This method does not guarantee a  sleep time each iteration.
     *
     * @param iterations
     *            How many times to sleep the item interaction delay time.
     * @see #waitItemInteractionDelay()
     */
    public final void waitItemInteractionDelay(int iterations) {
        for (int i = 0; i < iterations; i++) {
            waitItemInteractionDelay();
        }
    }
 
    /**
     * Hovers the entity if applicable.
     * Note that you <i>must</i> reset the tracker yourself after the current
     * Object interaction is finished.
     */
    public void hoverEntity(Clickable entity) {
        if (should_hover && Mouse.isInBounds()) {
            if (print_debug) {
                debug("Hovering entity");
            }
            entity.hover();
        }
    }
 
    /**
     * Enable or disable reaction sleeps
     * @param enabled
     */
    public void setEnableReactionSleep(boolean enabled) {
        enableReactionSleep = enabled;
    }
    /**
     * Sends the specified message to the system print stream with the [ABC2]
     * tag.
     * @param message The message to print.
     */
    private void debug(Object message) {
    	General.println("[ABC2] " + message);
    }
 
}