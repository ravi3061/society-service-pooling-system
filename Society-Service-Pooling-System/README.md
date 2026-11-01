# Society Service Pooling System (SSPS)

Shared Visits. Coordinated Services. Collective Savings.

A modular Java Swing educational prototype that enables residents from the same or nearby societies to share worker visits, coordinate service requests, and manage individual bills and payment records.

## Features

- Locality-based service zones covering multiple societies
- Agency, independent and assisted/offline workers
- Resident-created worker visits and manually recorded provider consent
- Visit capacity limits and individual work descriptions
- Group discounts for two or more active bookings
- Individual labour bills and a 5% platform commission
- Direct cash/UPI records and simulated platform payments
- Consolidated provider settlement ledger
- Local file persistence

## Requirements

- JDK 21 or newer; JDK 25 can also be used
- VS Code with Extension Pack for Java, or another Java IDE
- Git only for version control and GitHub publishing

No MySQL, Maven, Spring Boot, JavaFX or external JAR files are needed.

## Run on Windows

Extract the complete archive and open the project root in VS Code. In PowerShell:

```powershell
.\run.bat
```

Or compile and run manually:

```powershell
New-Item -ItemType Directory -Force bin
javac -encoding UTF-8 -d bin '@sources.txt'
java -cp bin ssps.Main
```

Verify `java -version` and `javac -version` first. The script does not install a JDK or repair PATH.

## Run on macOS or Linux

From the project root:

```bash
mkdir -p bin
javac -encoding UTF-8 -d bin @sources.txt
java -cp bin ssps.Main
```

## Structure

```text
src/ssps/
  Main.java
  model/       Visit, Booking, AppData
  storage/     FileStorage
  service/     PoolingService, BillingService, ReportService
  ui/          MainFrame, ProfilePanel, VisitPanel,
               BookingPanel, LedgerPanel, UiUtils
docs/          Setup, demonstration, architecture and GitHub guides
```

## Billing rules

Discount applies to labour only when two or more active bookings exist at rate-lock time. A single booking receives zero group discount. The provider and residents must agree before rates are locked.

Commission is 5% of discounted labour, rounded per booking to two decimal places. Worker net earnings equal the payable amount minus commission.

Direct payments are never paid again through the platform. Their commission remains a provider fee due; the prototype does not collect that fee.

## Storage and migration

Trusted local records are stored in `data/ssps.dat`, relative to the working directory. Always launch from the project root; `run.bat` sets that directory automatically.

The renamed packages use a new saved-data format identity. Records from earlier project versions are not imported. Do not rename an old data file to force loading it.

Do not edit serialized data manually, load downloaded `.dat` files, or run multiple app instances simultaneously. Back up records before changing model classes. The `.gitignore` excludes local data and compiled output.

## Limitations

This is a single-computer college prototype, not a production marketplace.

- No authentication or access control; admin actions and zone bills are accessible locally
- Resident fields switch demo identities rather than secure accounts
- No real payment gateway, bank payout, UPI verification or escrow
- Zones are predefined locality groups, not measured GPS distances
- Consent and completion are recorded manually
- No scheduling-conflict detection, chat, bidding, notifications or refunds
- No JDBC database, multi-device networking or background file operations
- File operations run on the GUI thread for small demonstration datasets
- Models expose public fields for beginner readability

Use fictional data only. Do not enter real personal or financial information.

## Documentation

- [Setup and troubleshooting](docs/SETUP.md)
- [Demonstration walkthrough](docs/DEMO.md)
- [Architecture and syllabus mapping](docs/ARCHITECTURE.md)
- [GitHub publishing checklist](docs/GITHUB.md)

## Verification status

Archive integrity and source/package references were checked. A Java compiler and GUI execution were not available for verification in the authoring environment. Compile locally and follow the demo before submission.
