import Foundation
import StoreKit

final class StoreKit2Bridge {
    func finishTransaction(
        transactionId: String,
        onComplete: @escaping (Error?) -> Void
    ) -> () -> Void {
        let task = Task { @MainActor in
            for await result in StoreKit.Transaction.unfinished {
                guard !Task.isCancelled else { return }

                switch result {
                case .verified(let transaction):
                    if String(transaction.id) == transactionId || String(transaction.originalID) == transactionId {
                        await transaction.finish()
                    }
                case .unverified(let transaction, let error):
                    if String(transaction.id) == transactionId || String(transaction.originalID) == transactionId {
                        onComplete(error)
                        return
                    }
                }
            }

            guard !Task.isCancelled else { return }
            onComplete(nil)
        }

        return { task.cancel() }
    }

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
