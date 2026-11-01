package ssps.service;
import ssps.model.*;
import java.math.BigDecimal;
public class ReportService {
    public String providerLedger(PoolingService service) {
        StringBuilder out = new StringBuilder("SSPS DEMO LEDGER - INR | ALL ZONES | NO REAL MONEY\n\n");
        for (Visit v : service.visits()) {
            BigDecimal net = BigDecimal.ZERO, direct = BigDecimal.ZERO, feeDue = BigDecimal.ZERO, held = BigDecimal.ZERO;
            for (Booking b : service.members(v.id)) {
                if (b.payment.equals("PLATFORM DEMO")) { net = net.add(b.payable.subtract(b.fee)); held = held.add(b.fee); }
                if (b.payment.startsWith("DIRECT")) { direct = direct.add(b.payable); feeDue = feeDue.add(b.fee); }
            }
            out.append(v.id).append(" | ").append(v.provider).append(" | ").append(v.type).append(" | ").append(v.zone)
                .append("\n  Organiser: ").append(v.organiser).append(" | Contact: ").append(v.contact)
                .append("\n  Platform net payout ").append(v.payout ? "RECORDED: " : "pending: ").append(BillingService.display(net))
                .append(" | Platform fee withheld: ").append(BillingService.display(held))
                .append("\n  Direct payment received: ").append(BillingService.display(direct))
                .append(" | Direct-payment provider fee DUE (not collected): ").append(BillingService.display(feeDue)).append("\n\n");
        }
        return out.toString();
    }
}
