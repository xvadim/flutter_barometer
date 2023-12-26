import 'package:barometer/barometer_plugin.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Barometer Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const MyHomePage(title: 'Barometer Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});
  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  bool? _isPresent;

  //just use it here (without any state management) for simplicity
  final _barometer = BarometerPlugin();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text('Is present: ${_isPresent ?? '?'}'),
            const SizedBox(height: 30),
            if (_barometer.dataStream != null)
              StreamBuilder<double>(
                stream: _barometer.dataStream,
                builder: (
                  BuildContext context,
                  AsyncSnapshot<double> snapshot,
                ) {
                  if (snapshot.hasData) {
                    return Text(snapshot.data.toString());
                  } else {
                    return const Text('');
                  }
                },
              ),
            if (_barometer.dataStream == null) const Text(''),
            const SizedBox(height: 30),
            Row(
              mainAxisSize: MainAxisSize.min,
              children: [
                FilledButton(
                  onPressed: () async => _checkBarometerPresence(),
                  child: const Text('Check'),
                ),
                const SizedBox(width: 16),
                FilledButton(
                  onPressed: _isPresent ?? false ? () async => _start() : null,
                  child: const Text('Start'),
                ),
                const SizedBox(width: 16),
                FilledButton(
                  onPressed: _isPresent ?? false ? () async => _stop() : null,
                  child: const Text('Stop'),
                ),
              ],
            )
          ],
        ),
      ),
    );
  }

  Future<void> _checkBarometerPresence() async {
    final isPresent = await _barometer.isPresent();
    setState(() {
      _isPresent = isPresent;
    });
  }

  Future<void> _start() async {
    await _barometer.start();
    setState(() {});
  }

  Future<void> _stop() async {
    await _barometer.stop();
    setState(() {});
  }
}
