# BIGO ET — Build the test APK using only your Android phone

This project includes a GitHub Actions workflow that builds a **debug/test APK** in the cloud.
You do not need to install Android Studio on your phone.

## What this build is
- A test APK for installing on your own Android device.
- Demo registration, demo live-room screens and simulated transaction checking.
- No real live video streaming, Telebirr payment, CBE payment, or real money movement.
- Not signed for Google Play production release.

## Steps on your Samsung phone

1. Open Chrome and sign in to GitHub, or create a GitHub account at https://github.com.
2. Create a **private** repository named `BigoET`.
3. Extract this ZIP using Samsung My Files (tap the ZIP, then Extract).
4. In Chrome, open your new repository and choose **Add file → Upload files**.
5. Upload the extracted project contents, keeping the folder structure. The `.github/workflows/android.yml` file is important. If the phone's file picker cannot upload folders, use GitHub's browser desktop-site option or ask a trusted adult to help upload the files.
6. Commit the files to the `main` branch.
7. Open the repository's **Actions** tab. Select **Build BIGO ET Android APK**, then tap **Run workflow** if it has not started automatically.
8. Wait for the workflow to finish. If it succeeds, open the completed run and download the `BIGO-ET-debug-apk` artifact.
9. The downloaded artifact is another ZIP. Extract it in My Files to find `app-debug.apk`.
10. Tap `app-debug.apk` to install. Android may ask you to allow Chrome or My Files to install apps from that source. Only allow this for the APK you built from your own private repository, then turn the permission off again.

## If the build fails
Open the failed workflow run and review the red error step. The source may need a small compatibility fix; share the error text to get help.

## Security
- Keep the GitHub repository private.
- Never upload passwords, API keys, Telebirr PINs, CBE credentials, OTPs, signing keys or private customer information.
- Do not add real payment credentials to this demo.
- A transaction reference typed into this demo is not proof of payment.
- Keep live hosting, gifting and real payments disabled until a qualified adult/business owner has arranged provider authorization, age assurance, moderation, privacy and security reviews.

## Troubleshooting & Important Notes
- **Do NOT add a Node.js (`npm`) workflow** in GitHub Actions: this project is a native Kotlin/Android app and does not use `package.json`. If you created a Node.js workflow file in `.github/workflows/`, delete it so only `.github/workflows/android.yml` runs.
- **Keep `build.gradle.kts` in the repository root:** When uploading files to GitHub, make sure `build.gradle.kts` (with the `plugins { ... version "8.7.3" ... }` block) and `settings.gradle.kts` are uploaded at the root alongside `app/` and `.github/workflows/android.yml`.
