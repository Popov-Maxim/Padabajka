import SwiftUI
import FirebaseCore
import FirebaseMessaging
import shared
import GoogleSignIn

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        InitKoinIosKt.doInitKoinIos()

        FirebaseApp.configure()
        Messaging.messaging().delegate = self
        UNUserNotificationCenter.current().delegate = self

        let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
        // UNUserNotificationCenter.current().requestAuthorization(
        //   options: authOptions,
        //   completionHandler: { _, _ in }
        // )

        application.registerForRemoteNotifications()

        return true
    }

    func application(
        _ application: UIApplication,
        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
    ) {
//        print("new APN Token: \(deviceToken.map { String(format: "%02.2hhx", $0) }.joined())")
        Messaging.messaging().apnsToken = deviceToken
    }
    
    func application(_ application: UIApplication,
                     didFailToRegisterForRemoteNotificationsWithError
                     error: Error) {
//        print("new APN Token: ")
    }
    
    func application(_ app: UIApplication,
                     open url: URL,
                     options: [UIApplication.OpenURLOptionsKey: Any] = [:]) -> Bool {
      return GIDSignIn.sharedInstance.handle(url)
    }
}

extension AppDelegate: UNUserNotificationCenterDelegate {
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                willPresent notification: UNNotification,
                                withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        // get push
//        print("LOG: AppDelefate get push")
//        let userInfo = notification.request.content.userInfo as! [String: String]
//        let platformMessagePush = PushUtilsKt.platformMessagePush(notification: notification)
        
        SharedPushHandler.shared.handlePush(notification: notification)
        let userInfo = notification.request.content.userInfo
        let disableNotification = userInfo["disable_notification"] as? Bool ?? false
    
        if disableNotification {
            completionHandler([])
        } else {
            completionHandler([.banner, .sound])
        }
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                didReceive response: UNNotificationResponse,
                                withCompletionHandler completionHandler: @escaping () -> Void) {
        // click on notification
//        print("LOG: AppDelefate click on notification")
        completionHandler()
    }
}

extension AppDelegate: MessagingDelegate {
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
//        let apn = Messaging.messaging().apnsToken
//        print("FCM Token: \(fcmToken ?? "nil")")
//        print("APN Token: \(apn?.base64EncodedString() ?? "nil")")
    }
}

@main
struct iOSApp: App {
    // register app delegate for Firebase setup
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
