package com.padabajka.dating.feature.ads.yandex.ui

import cocoapods.YandexMobileAds.YMANativeAdView
import cocoapods.YandexMobileAds.YMANativeMediaView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.NSDirectionalEdgeInsetsMake
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.NSTextAlignmentCenter
import platform.UIKit.UIButton
import platform.UIKit.UIButtonTypeSystem
import platform.UIKit.UIColor
import platform.UIKit.UIControlStateNormal
import platform.UIKit.UIEdgeInsetsMake
import platform.UIKit.UIFont
import platform.UIKit.UIImageView
import platform.UIKit.UILabel
import platform.UIKit.UILayoutConstraintAxisHorizontal
import platform.UIKit.UILayoutConstraintAxisVertical
import platform.UIKit.UIStackView
import platform.UIKit.UIStackViewAlignmentCenter
import platform.UIKit.UIStackViewAlignmentFill
import platform.UIKit.UIStackViewDistributionFill
import platform.UIKit.UIView
import platform.UIKit.contentEdgeInsets

@OptIn(ExperimentalForeignApi::class)
@Suppress("ALL") // TODO(P2)
class CustomNativeAdView : YMANativeAdView(frame = CGRectZero.readValue()) {

    private val customTitleLabel = UILabel()
    private val customDomainLabel = UILabel()
    private val customWarningLabel = UILabel()
    private val customSponsoredLabel = UILabel()
    private val customFeedbackButton = UIButton() as UIButton
    private val customCallToActionButton = UIButton.buttonWithType(UIButtonTypeSystem) as UIButton
    private val customBodyLabel = UILabel()
    private val customIconImageView = UIImageView()
    private val customMediaView = YMANativeMediaView()

    init {
        setupUI()
        bindAssets()
    }

    private fun bindAssets() {
        titleLabel = customTitleLabel
        domainLabel = customDomainLabel
        warningLabel = customWarningLabel
        sponsoredLabel = customSponsoredLabel
        feedbackButton = customFeedbackButton
        callToActionButton = customCallToActionButton
        mediaView = customMediaView
        bodyLabel = customBodyLabel
        iconImageView = customIconImageView
        faviconImageView = customIconImageView
    }

    @Suppress("LongMethod", "CyclomaticComplexMethod")
    private fun setupUI() {

        // root background = #CCC
        backgroundColor = UIColor(
            red = 204.0 / 255.0,
            green = 204.0 / 255.0,
            blue = 204.0 / 255.0,
            alpha = 1.0
        )

        val root = UIStackView().apply {
            axis = UILayoutConstraintAxisVertical
            spacing = 30.0
            translatesAutoresizingMaskIntoConstraints = false
            layoutMarginsRelativeArrangement = true

            alignment = UIStackViewAlignmentFill
            distribution = UIStackViewDistributionFill

            directionalLayoutMargins = NSDirectionalEdgeInsetsMake(
                top = 33.0,
                leading = 0.0,
                bottom = 33.0,
                trailing = 0.0
            )
        }

        addSubview(root)

        NSLayoutConstraint.activateConstraints(
            listOf(
                root.topAnchor.constraintEqualToAnchor(topAnchor),
                root.leadingAnchor.constraintEqualToAnchor(leadingAnchor),
                root.trailingAnchor.constraintEqualToAnchor(trailingAnchor),
                root.bottomAnchor.constraintEqualToAnchor(bottomAnchor)
            )
        )

        // ---------------- HEADER ----------------

        val header = UIStackView().apply {
            axis = UILayoutConstraintAxisHorizontal
            alignment = UIStackViewAlignmentCenter
            spacing = 10.0
            layoutMarginsRelativeArrangement = true

            directionalLayoutMargins = NSDirectionalEdgeInsetsMake(
                top = 0.0,
                leading = 33.0,
                bottom = 0.0,
                trailing = 33.0
            )
        }

        customIconImageView.apply {
            translatesAutoresizingMaskIntoConstraints = false
            widthAnchor.constraintEqualToConstant(40.0).active = true
            heightAnchor.constraintEqualToConstant(40.0).active = true
        }

        customDomainLabel.apply {
            text = "My App"
            textColor = UIColor.blackColor
            font = UIFont.systemFontOfSize(16.0)
        }

        customSponsoredLabel.apply {
            text = "Ad, 18+"
            textColor = UIColor.blackColor
            font = UIFont.systemFontOfSize(12.0)
        }

        val textColumn = UIStackView().apply {
            axis = UILayoutConstraintAxisVertical
            spacing = 2.0
        }

        textColumn.addArrangedSubview(customDomainLabel)
        textColumn.addArrangedSubview(customSponsoredLabel)

        val spacer = UIView().apply {
            translatesAutoresizingMaskIntoConstraints = false
        }

        customFeedbackButton.apply {
            translatesAutoresizingMaskIntoConstraints = false
            widthAnchor.constraintEqualToConstant(30.0).active = true
            heightAnchor.constraintEqualToConstant(30.0).active = true
        }

        header.addArrangedSubview(customIconImageView)
        header.addArrangedSubview(textColumn)
        header.addArrangedSubview(spacer)
        header.addArrangedSubview(customFeedbackButton)

        // ---------------- TEXT ----------------

        val textBlock = UIStackView().apply {
            axis = UILayoutConstraintAxisVertical
            spacing = 8.0
            layoutMarginsRelativeArrangement = true

            directionalLayoutMargins = NSDirectionalEdgeInsetsMake(
                top = 0.0,
                leading = 20.0,
                bottom = 0.0,
                trailing = 20.0
            )
        }

        customTitleLabel.apply {
            text = "Приложение, где рисунки оживают как по волшебству"
            textColor = UIColor.blackColor
            font = UIFont.systemFontOfSize(26.0)
            numberOfLines = 0
        }

        customBodyLabel.apply {
            text = "Увлекательное рисование лучше прописей научит красивому почерку. ОР за 60 дней!"
            textColor = UIColor.blackColor
            font = UIFont.systemFontOfSize(16.0)
            numberOfLines = 0
        }

        textBlock.addArrangedSubview(customTitleLabel)
        textBlock.addArrangedSubview(customBodyLabel)

        // ---------------- MEDIA ----------------

        customMediaView.apply {
            translatesAutoresizingMaskIntoConstraints = false

            heightAnchor.constraintGreaterThanOrEqualToConstant(0.0).active = true
        }

        // ---------------- CTA (#6EA3CA + 16dp radius) ----------------

        customCallToActionButton.apply {
            setTitle("Call To Action", forState = UIControlStateNormal)

            backgroundColor = UIColor(
                red = 110.0 / 255.0,
                green = 163.0 / 255.0,
                blue = 202.0 / 255.0,
                alpha = 1.0
            )

            setTitleColor(UIColor.whiteColor, forState = UIControlStateNormal)

            layer.cornerRadius = 16.0
            layer.masksToBounds = true

            translatesAutoresizingMaskIntoConstraints = false

            contentEdgeInsets = UIEdgeInsetsMake(
                top = 14.0,
                left = 14.0,
                bottom = 14.0,
                right = 14.0
            )
        }

        val ctaContainer = UIView().apply {
            translatesAutoresizingMaskIntoConstraints = false
            layoutMargins = UIEdgeInsetsMake(0.0, 33.0, 0.0, 33.0)
        }

        ctaContainer.addSubview(customCallToActionButton)

        NSLayoutConstraint.activateConstraints(
            listOf(
                customCallToActionButton
                    .topAnchor.constraintEqualToAnchor(ctaContainer.topAnchor),
                customCallToActionButton
                    .leadingAnchor.constraintEqualToAnchor(ctaContainer.layoutMarginsGuide.leadingAnchor),
                customCallToActionButton
                    .trailingAnchor.constraintEqualToAnchor(ctaContainer.layoutMarginsGuide.trailingAnchor),
                customCallToActionButton
                    .bottomAnchor.constraintEqualToAnchor(ctaContainer.bottomAnchor)
            )
        )

        // ---------------- WARNING ----------------

        customWarningLabel.apply {
            text = "есть противопоказания. необходима консультация специалиста"
            textAlignment = NSTextAlignmentCenter
            textColor = UIColor.blackColor
            font = UIFont.systemFontOfSize(16.0)
            numberOfLines = 0
        }

        // ---------------- BUILD ----------------

        root.addArrangedSubview(header)
        root.addArrangedSubview(textBlock)
        root.addArrangedSubview(customMediaView)
        root.addArrangedSubview(ctaContainer)
        root.addArrangedSubview(customWarningLabel)
    }
}
