import Foundation
import StoreKit

final class StoreKit2Bridge {
    func currentEntitlements(
        onTransaction: @escaping (String, String, Error?) -> Void,
        onComplete: @escaping () -> Void
    ) -> () -> Void {
        let task = Task { @MainActor in
            for await result in StoreKit.Transaction.currentEntitlements {
                guard !Task.isCancelled else { return }

                switch result {
                case .verified(let transaction):
                    onTransaction(transaction.productID, String(transaction.id), nil)
                case .unverified(let transaction, let error):
                    onTransaction(transaction.productID, String(transaction.id), error)
                }
            }

            guard !Task.isCancelled else { return }
            onComplete()
        }

        return {
            task.cancel()
        }
    }
}
