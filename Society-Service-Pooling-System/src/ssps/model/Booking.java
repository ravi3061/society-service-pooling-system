package ssps.model;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;
public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;
    public final String id = UUID.randomUUID().toString().substring(0, 8);
    public String visitId, resident, flat, work;
    public String status = "JOINED", payment = "UNPAID";
    public BigDecimal base, payable = BigDecimal.ZERO, fee = BigDecimal.ZERO;
}
