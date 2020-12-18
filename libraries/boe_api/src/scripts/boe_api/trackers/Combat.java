package scripts.boe_api.trackers;

import com.allatori.annotations.DoNotRename;
import org.tribot.api2007.Skills;

import java.util.ArrayList;
import java.util.Collections;

@DoNotRename
public class Combat {

    private ArrayList<Experience> trackers = new ArrayList<>();

    public Combat() {
        Collections.addAll(this.trackers,
                new Experience(Skills.SKILLS.ATTACK),
                new Experience(Skills.SKILLS.MAGIC),
                new Experience(Skills.SKILLS.DEFENCE),
                new Experience(Skills.SKILLS.HITPOINTS),
                new Experience(Skills.SKILLS.STRENGTH),
                new Experience(Skills.SKILLS.RANGED));
    }

    public Combat(Experience ...skills) {
        Collections.addAll(trackers, skills);
    }

    public Experience getTracker(Skills.SKILLS skill) {
        return this.trackers.stream().filter(experience -> experience.getSkill().equals(skill)).findFirst().get();
    }

    public void update() {
        for (Experience tracker : this.trackers) {
            tracker.update();
        }
    }
}
