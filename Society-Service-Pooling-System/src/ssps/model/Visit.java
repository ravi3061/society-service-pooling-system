package ssps.model;
import java.io.Serializable;
import java.util.UUID;
public class Visit implements Serializable {
    private static final long serialVersionUID = 1L;
    public final String id = UUID.randomUUID().toString().substring(0, 8);
    public String provider, type, skill, zone, date, slot, contact, organiser;
    public int capacity, discount;
    public String status = "PENDING";
    public boolean payout;
}
