// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorPdfViewer",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "CapacitorPdfViewer",
            targets: ["PdfViewerPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "PdfViewerPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/PdfViewerPlugin"),
        .testTarget(
            name: "PdfViewerPluginTests",
            dependencies: ["PdfViewerPlugin"],
            path: "ios/Tests/PdfViewerPluginTests")
    ]
)