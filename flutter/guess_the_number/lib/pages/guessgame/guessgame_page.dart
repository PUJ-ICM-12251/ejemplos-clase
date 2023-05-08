import 'dart:io';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'dart:math';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:guess_the_number/pages/home/home_page.dart';

class GuessGame extends StatefulWidget {
  const GuessGame({super.key});

  @override
  State<GuessGame> createState() => _GuessGameState();
}

class _GuessGameState extends State<GuessGame> {
  int _counter = 0;
  int _random = Random().nextInt(101);
  String _message = "";
  bool _done = false;
  TextEditingController tec = TextEditingController();

  void play() {
    setState(() {
      print(_random);

      var tryGuess = int.tryParse(tec.text);
      if (tryGuess != null) {
        int guess = tryGuess;
        if (guess >= 0 && guess < 101) {
          _counter++;
          if (guess > _random) {
            _message = "$guess is bigger than the number";
          } else if (guess < _random) {
            _message = "$guess is smaller than the number";
          } else {
            _message = "$guess is the winning number";
            _done = true;

            if (Platform.isIOS) {
              showCupertinoDialog<void>(
                context: context,
                builder: (BuildContext context) => CupertinoAlertDialog(
                  title: const Text('Success'),
                  content: Text(_message),
                  actions: <CupertinoDialogAction>[
                    CupertinoDialogAction(
                      child: const Text('Cancel'),
                      onPressed: () => Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) =>
                            HomePage(title: 'Flutter Demo Home Page'),
                        ),
                      ),
                    ),
                    CupertinoDialogAction(
                      isDestructiveAction: true,
                      onPressed: () {
                        // Do something destructive.
                      },
                      child: const Text('Ok'),
                    ),
                  ],
                ),
              );
            } else {
              showDialog(
                context: context,
                builder: (BuildContext context) => AlertDialog(
                  title: const Text('Success'),
                  content: Text(_message),
                  actions: <Widget>[
                    TextButton(
                      onPressed: () => Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) =>
                            HomePage(title: 'Flutter Demo Home Page'),
                        ),
                      ),
                      child: const Text('Cancel'),
                    ),
                    TextButton(
                      onPressed: () => Navigator.pop(context, 'Continue'),
                      child: const Text('Ok'),
                      ),
                  ],
                ),
              );
            }

          }
        } else {
          launchToast("$guess is out of bounds");
        }
      } else {
        launchToast("invalid input");
      }
      tec.text = "";
    });
  }


  void newGame() {
    setState(() {
      _counter = 0;
      _random = Random().nextInt(101);
      _message = "";
      _done = false;
      tec.text = "";
    });
  }

  void launchToast(String message) {
    Fluttertoast.showToast(
        msg: message,
        toastLength: Toast.LENGTH_LONG,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIosWeb: 1,
        backgroundColor: Colors.red,
        textColor: Colors.white,
        fontSize: 16.0);
  }



  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Guess Game"),
        backgroundColor: Colors.deepOrange,
      ),
      body: Container(
          margin: const EdgeInsets.fromLTRB(20, 10, 20, 10),
          width: double.infinity,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const SizedBox(
                width: double.infinity,
                child: Text(
                  "Enter a number 1-100",
                  textAlign: TextAlign.center,
                  style: TextStyle(
                      color: Colors.blue,
                      fontSize: 20,
                      fontWeight: FontWeight.bold),
                ),
              ),
              const SizedBox(
                height: 20,
              ),
              TextField(
                enabled: !_done,
                controller: tec,
                keyboardType: TextInputType.number,
              ),
              const SizedBox(
                height: 20,
              ),
              SizedBox(
                  width: double.infinity,
                  child: ElevatedButton(
                      onPressed: play,
                      child: const Text("Play"))),
              const SizedBox(
                height: 20,
              ),
              Text("Message: $_message"),
              const SizedBox(
                height: 10,
              ),
              Text("Counter: $_counter")
            ],
          )),
      floatingActionButton: FloatingActionButton(
        onPressed: _done ? newGame : null,
        backgroundColor: _done ? Colors.blue : Colors.grey,
        child: const Icon(Icons.refresh),
      ),
    );
  }
}