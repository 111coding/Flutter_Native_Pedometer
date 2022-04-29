import Flutter
import UIKit
import CoreMotion // 만보기

public class SwiftFlutterNativePedometerPlugin: NSObject, FlutterPlugin {

    private let pedometer = CMPedometer()

  public static func register(with registrar: FlutterPluginRegistrar) {

    /// ========== Method Channel ==========
    let channel = FlutterMethodChannel(name: "flutter_native_pedometer", binaryMessenger: registrar.messenger())
    let instance = SwiftFlutterNativePedometerPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)

    /// ========== Event Channel ==========
    let pedometerHandler = PedometerStreamHandler()
    let stepCountChannel = FlutterEventChannel.init(name: "flutter_native_pedometer_stream", binaryMessenger: registrar.messenger())
    stepCountChannel.setStreamHandler(pedometerHandler)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    guard call.method == "get_walk_data" else {
                return
    }
    self.getWalkData(call:call, result: result)
  }
  
    // ========================= 만보기 B =========================
    private func getWalkData(call: FlutterMethodCall, result: @escaping FlutterResult) {
        
        if CMPedometer.isStepCountingAvailable() {
            
            let beginTimeString = (call.arguments as? String) ?? ""
            
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "YYYY-MM-dd HH:mm:ss"
            
            let beginDate = dateFormatter.date(from: beginTimeString)
            let nowDate = Date()
            
            // form ~ to 데이터 (한번만)
            pedometer.queryPedometerData(from: beginDate!, to: nowDate) { (data, error) in
                result(data?.numberOfSteps ?? 0)
            }
            
            // form ~ 데이터 (계속)
            // pedometer.startUpdates(from: nowDate) { (data, error) in
            //     print("current")
            //     print(data ?? -1)
            // }
        }else{
            result(-1)
        }
    }
    // ========================= 만보기 E =========================

}




// ========================= 만보기 B =========================
// 이벤트 채널
public class PedometerStreamHandler: NSObject, FlutterStreamHandler {

    private var recentCnt = 0
    
    private let pedometer = CMPedometer()
    private var running = false
    private var eventSink: FlutterEventSink?

    private func handleEvent(count: Int) {
        // If no eventSink to emit events to, do nothing (wait)
        if (eventSink == nil) {
            return
        }
        // Emit step count event to Flutter

        self.recentCnt = count - self.recentCnt
        eventSink!(self.recentCnt)
    }

    public func onListen(withArguments arguments: Any?, eventSink: @escaping FlutterEventSink) -> FlutterError? {
        self.eventSink = eventSink
        if #available(iOS 10.0, *) {
            if (!CMPedometer.isStepCountingAvailable()) {
                eventSink(FlutterError(code: "3", message: "Step Count is not available", details: nil))
            }
            else if (!running) {
                running = true
                pedometer.startUpdates(from: Date()) {
                    pedometerData, error in
                    guard let pedometerData = pedometerData, error == nil else { return }

                    DispatchQueue.main.async {
                        self.handleEvent(count: pedometerData.numberOfSteps.intValue)
                    }
                }
            }
        } else {
            eventSink(FlutterError(code: "1", message: "Requires iOS 10.0 minimum", details: nil))
        }
        return nil
    }

    public func onCancel(withArguments arguments: Any?) -> FlutterError? {
        NotificationCenter.default.removeObserver(self)
        eventSink = nil

        if (running) {
            pedometer.stopUpdates()
            running = false
        }
        return nil
    }
}
// ========================= 만보기 E =========================
