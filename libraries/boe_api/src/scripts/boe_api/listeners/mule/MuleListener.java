package scripts.boe_api.listeners.mule;

public interface MuleListener {

    void onMuleNearby(String muleName);

    void onMuleLeave(String muleName);
}