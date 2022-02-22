#import "FlutterNativePedometerPlugin.h"
#if __has_include(<flutter_native_pedometer/flutter_native_pedometer-Swift.h>)
#import <flutter_native_pedometer/flutter_native_pedometer-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_native_pedometer-Swift.h"
#endif

@implementation FlutterNativePedometerPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterNativePedometerPlugin registerWithRegistrar:registrar];
}
@end
