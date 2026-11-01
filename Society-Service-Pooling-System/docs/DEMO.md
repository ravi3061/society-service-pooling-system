# SSPS Demonstration Walkthrough

Use fictional data. No action transfers real money.

## Create a worker visit

1. Set the resident to Alex, flat to Green Society / 101 and zone to Zone A.
2. Create a visit for Taylor, type Assisted / Offline, skill Electrician.
3. Use a future date, slot 11 AM - 2 PM, capacity 5 and proposed discount 10.
4. Select the visit and choose Confirm Provider [Admin] only after recording provider agreement to scope, capacity, discount and 5% commission.

An organiser may record offline consent. No phone or worker login is required.

## Join two jobs

1. Select the OPEN visit and join with Fan repair and estimated labour 500.
2. Change resident to Jordan and flat to Lake Apartments / 204, keeping Zone A.
3. Join the same visit with Switch repair and estimated labour 700.

Creating a visit does not automatically book the organiser's personal job.

## Lock agreed prices

Select the visit and choose Lock Rates [Admin]. Confirm only after every resident and the provider agrees to the job scope and charges.

| Resident | Base | Discount | Payable | Fee 5% | Worker net |
| --- | ---: | ---: | ---: | ---: | ---: |
| Alex | 500.00 | 50.00 | 450.00 | 22.50 | 427.50 |
| Jordan | 700.00 | 70.00 | 630.00 | 31.50 | 598.50 |

Amounts are labour-only. Locked prices cannot be edited in this version.

## Complete and record payments

In Bills and Payments, select Alex's booking, confirm completion and record PLATFORM DEMO payment. Select Jordan's booking, confirm completion and record DIRECT CASH.

Direct payment records assert that the provider received the full payable amount. DIRECT UPI is not verified against a bank.

## Settle the visit

In Nearby Visits, select the visit and choose Record Payout [Admin]. All jobs must be complete and payments recorded.

Provider Ledger should show:

- Simulated platform net payout: 427.50
- Platform commission withheld: 22.50
- Direct payment already received: 630.00
- Direct-payment fee due: 31.50, not collected

Direct payments are not included in the platform payout.

## Additional checks

- One active booking receives zero group discount.
- Duplicate joining for the same resident and flat is blocked.
- Full, pending and locked visits cannot be joined.
- Cancellation works only before rate locking.
- Duplicate payment and settlement records are blocked.
- Changing zones filters visible visits and bills.
- Closing and reopening from the same project root preserves records.
