import 'package:flutter/material.dart';
import 'package:guess_the_number/pages/home/home_page.dart';
import 'package:guess_the_number/pages/guessgame/guessgame_page.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      // home: const HomePage(title: 'Flutter Demo Home Page'),
      initialRoute: '/home',
      routes: {
        '/home': (context) => const HomePage(title: 'Flutter Demo Home Page'),
        '/guess': (context) => const GuessGame(),
      },
      debugShowCheckedModeBanner: true,
    );
  }
}
