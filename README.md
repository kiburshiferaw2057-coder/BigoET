# BIGO ET — Android Prototype

A bilingual (Amharic / English) Android starter prototype for a live-streaming and social app.

## Included
- Kotlin + Jetpack Compose starter app
- Blue-and-white visual theme
- Amharic / English language toggle (prototype strings)
- Public registration screen with age-safety gate (18+ for live hosting and wallet features)
- Home, live-room mock, profile, wallet and transaction-check screens
- Simulated payment flow only
- Payment gateway interface placeholder; no real payment credentials or banking details

## Important prototype limits
- This project does **not** connect to Telebirr, CBE, a real wallet, or a real streaming service.
- The transaction checker only recognizes locally simulated reference numbers created by this demo.
- Never treat a user-entered transaction ID or screenshot as proof of payment.
- Do not collect PINs, OTPs, passwords for banking apps, or full card/bank credentials.
- Before public launch, obtain provider authorization and implement server-to-server payment verification, security review, moderation, reporting, blocking, and child-safety controls.

## Open in Android Studio
1. Open this folder in Android Studio.
2. Allow Gradle sync.
3. Run on an emulator or Android device.

The source is a starter template and may need Gradle/plugin version adjustments for your installed Android Studio.


## Phone-only cloud build
See `PHONE_BUILD_GUIDE.md`. A GitHub Actions workflow is included at `.github/workflows/android.yml` to create a debug APK in the cloud.
