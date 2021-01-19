package scripts.boe_api.paint;

import lombok.Getter;
import lombok.Setter;
import org.tribot.api.General;
import org.tribot.api.Timing;
import scripts.boe_api.scripting.BoeScript;

import java.awt.*;

public class BoePaint {

    private final int PAINT_X = 4; //The x coordinate for the paint text
    private final int PAINT_BOT_Y = 336; //The y coordinate for the paint string on the bottom
    private final int PAINT_SPACE = 15; //The space between paint fields
    private final long ONE_HOUR_MS = 3600000;
    private final Font PAINT_FONT = new Font("Open Sans", Font.PLAIN, 10); //The text font we will use on our paint

    protected Paintable paintable; //Paintable object we're painting (we get our paint info from this)
    @Getter
    protected BoeScript script;	//Script we're painting for --> This is so we can call getRunningTime() from this class
    @Setter
    protected Color color;	//The color of the paint
    protected long startTime;

    public BoePaint(Paintable paintable, Color color) {
        this.script = (BoeScript) paintable;
        this.paintable = paintable;
        this.color = color;
        startTime = System.currentTimeMillis();
    }

    public void paint(Graphics g) {
        //set paint text color
        g.setColor(color);
        g.setFont(PAINT_FONT);

        String[] info = paintable.getPaintInfo();

        //FOR(each paint information field in paintInfo)
        for (int index = 0; index < info.length; index++) {
            //draw paint field at the appropriate position on the screen, as defined by constants
            g.drawString(info[index], PAINT_X, PAINT_BOT_Y - (PAINT_SPACE * (info.length - (index + 1))));

        } //END FOR
    }

    public long getTimeRanMs() {
        return System.currentTimeMillis() - startTime;
    }

    public long getTimeRanS() { return getTimeRanMs() / 1000; }

    public String getTimeRan() {
        //return the properly formatted string
        return Timing.msToString(getTimeRanMs());
    } //END getTimeRan()

    public long getPerHour(int amount) {
        //return the projected amount per hour
        return amount > 0 ? Math.round( (double)(amount) / (getTimeRanMs() / ONE_HOUR_MS)) : 0;
    }

    public int getEstimatedPerHour(int amount) {
        return (int)(amount * ONE_HOUR_MS / getTimeRanMs());
    }
}
