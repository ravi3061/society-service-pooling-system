package ssps.model;
import java.io.Serializable;
import java.util.ArrayList;
public class AppData implements Serializable {
    private static final long serialVersionUID = 1L;
    public ArrayList<Visit> visits = new ArrayList<>();
    public ArrayList<Booking> bookings = new ArrayList<>();
}
