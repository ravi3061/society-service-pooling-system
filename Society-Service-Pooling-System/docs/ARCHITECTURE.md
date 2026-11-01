# SSPS Architecture and Syllabus Mapping

## Files

| File | Responsibility |
| --- | --- |
| `Main.java` | Configure Swing and launch SSPS |
| `model/Visit.java` | Provider visit information |
| `model/Booking.java` | Resident job, bill and payment information |
| `model/AppData.java` | All persisted records |
| `storage/FileStorage.java` | File storage and in-memory rollback snapshots |
| `service/PoolingService.java` | Joining, consent, locking, cancellation, completion and payment rules |
| `service/BillingService.java` | Decimal values, discount and commission |
| `service/ReportService.java` | Provider settlement report |
| `ui/MainFrame.java` | Application window and tabs |
| `ui/ProfilePanel.java` | Acting resident, flat and zone |
| `ui/VisitPanel.java` | Visit and provider workflow controls |
| `ui/BookingPanel.java` | Individual completion and payment controls |
| `ui/LedgerPanel.java` | Read-only settlement screen |
| `ui/UiUtils.java` | Common dialogs, buttons and forms |

## Flow

UI panels collect input and call service methods. Service methods validate business rules, update models and save through FileStorage. Failed saves restore a snapshot. Tables refresh after actions.

## Concepts used

Classes, constructors, methods, conditionals, loops, string handling, packages, interfaces, exception handling, Swing components, layout managers, event listeners and inheritance through Swing subclasses.

Supporting additions include ArrayList, streams, BigDecimal, serialization and file I/O. Models use public fields for beginner readability.

JDBC, multi-device networking, explicit background worker tasks and advanced inheritance examples are not implemented. They may be future milestones; do not claim full syllabus coverage.
