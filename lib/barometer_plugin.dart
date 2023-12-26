import 'package:flutter/services.dart';

class BarometerPlugin {
  Stream<double>? get dataStream => _dataStream;
  Future<bool> isPresent() async {
    final result = await _methodChannel.invokeMethod<bool>(_cmdIsPresent);
    return result ?? false;
  }

  Future<bool> start() async {
    final result = await _methodChannel.invokeMethod<bool>(_cmdStart);
    _dataStream ??= createStream();

    return result ?? false;
  }

  Future<bool> stop() async {
    final result = await _methodChannel.invokeMethod<bool>(_cmdStop);
    _dataStream = null;
    return result ?? false;
  }

  static const _prefix = 'org.xbasoft.barometer';

  static const _cmdIsPresent = 'isPresent';
  static const _cmdStart = 'start';
  static const _cmdStop = 'stop';
  static const MethodChannel _methodChannel = MethodChannel('$_prefix/command');
  static const EventChannel _dataChannel = EventChannel('$_prefix/data');

  Stream<double>? _dataStream;

  Stream<double> createStream() {
    return _dataChannel
        .receiveBroadcastStream()
        .where((event) => event is double)
        .cast<double>();
  }
}
