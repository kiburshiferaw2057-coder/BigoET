# Security and production architecture

## Recommended payment flow
1. Android app requests a payment intent from your authenticated backend.
2. Backend creates the intent using an authorized Telebirr/CBE merchant integration.
3. User completes payment in the provider's official flow.
4. Provider sends a signed server-to-server callback to the backend.
5. Backend validates signature, merchant identity, amount, currency, order ID, timestamp and idempotency key.
6. Backend queries provider status where supported; only then marks the order paid.
7. App reads the verified order status from the backend.

Never mark a transaction paid based only on a reference typed by a user, a screenshot, or a client-side callback.

## Required controls
- Keep merchant secrets only in server-side secret storage, never in the APK.
- Use TLS, short-lived access tokens, secure refresh-token rotation and rate limits.
- Enforce authorization on every wallet and transaction endpoint.
- Use an immutable payment ledger and idempotency keys to prevent duplicate credit.
- Log security events without storing PINs, OTPs or unnecessary personal data.
- Add abuse reporting, blocking, moderator tools, live-room controls and content review.
- Use robust age assurance and appropriate legal review before enabling public live features.
- Set live-hosting, gifting and wallet features to unavailable until policy, age and provider checks are complete.

## Suggested backend endpoints
- POST /v1/payment-intents
- GET /v1/transactions/{transactionId}
- POST /v1/provider-webhooks/telebirr
- POST /v1/provider-webhooks/cbe
- GET /v1/wallet
- POST /v1/reports
- POST /v1/users/{userId}/block

These are design examples, not working endpoints. Provider-specific API documentation and authorization are required.
