package ssps.service;
import ssps.model.Booking;
import java.math.*;
public class BillingService {
    public static final BigDecimal COMMISSION = new BigDecimal("0.05");
    public static BigDecimal money(BigDecimal value) { return value.setScale(2, RoundingMode.HALF_UP); }
    public static String display(BigDecimal value) { return money(value).toPlainString(); }
    public static BigDecimal parse(String text) {
        BigDecimal value;
        try { value = new BigDecimal(text.trim()); }
        catch (NumberFormatException ex) { throw new IllegalArgumentException("Enter a valid amount, such as 500.00."); }
        if (value.signum() <= 0 || value.compareTo(new BigDecimal("1000000")) > 0 || value.scale() > 2)
            throw new IllegalArgumentException("Amount must be 0.01 to 1000000, with at most two decimal places.");
        return money(value);
    }
    public void calculate(Booking booking, BigDecimal base, int discountPercent) {
        booking.base = base;
        booking.payable = money(base.multiply(BigDecimal.valueOf(100 - discountPercent)).divide(new BigDecimal("100")));
        booking.fee = money(booking.payable.multiply(COMMISSION));
    }
}
