import 'package:flutter/material.dart';
import 'package:untitled1/profile.dart';
import 'AddPost.dart';
import 'Home_Page.dart';
import 'ItemCard.dart';
import 'SearchPage.dart';
import 'Settings.dart';

class SettingsPage extends StatelessWidget {

  Widget _arrow() {
    return Icon(
      Icons.arrow_forward_ios,
      size: 20.0,
    );
  }

  @override
  Widget build(BuildContext context) {
    var brightness = MediaQuery
        .of(context)
        .platformBrightness;
    return Scaffold(
      appBar: AppBar(
        title: Text(
          'Settings',
          style: TextStyle(
              fontWeight: FontWeight.bold
          ),
        ),
      ),
      body: Container(
        color: (brightness == Brightness.light) ? Color(0xFFF7F7F7) : Color(
            0xFF000000),
        child: ListView(
          children: <Widget>[
            Padding(
                padding: const EdgeInsets.only(top: 20),
                child: Container(
                  color: (brightness == Brightness.light) ? const Color(
                      0xFFF7F7F7) : const Color(0xFF000000),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: <Widget>[

                  Container(
                  padding: EdgeInsets.all(50),
                  alignment: Alignment.center,
                  child:IconButton(
                        icon: Icon(Icons.account_box),
                        iconSize: 60,
                        onPressed: () {
                          Navigator.pushReplacement(
                            context,
                            MaterialPageRoute(
                              builder: (context) => const profile(),
                            ),
                          );
                        },
                      ),
                  ),
                      Container(
                        padding: EdgeInsets.all(50),
                        alignment: Alignment.center,
                        child:IconButton(
                          icon: Icon(Icons.add_circle),
                          iconSize: 60,
                          onPressed: () {
                            Navigator.pushReplacement(
                              context,
                              MaterialPageRoute(
                                builder: (context) => const HomePage(),
                              ),
                            );
                          },
                        ),
                      ),
                      Container(
                        padding: EdgeInsets.all(50),
                        alignment: Alignment.center,
                        child:IconButton(
                          icon: Icon(Icons.arrow_circle_down_outlined ),
                          iconSize: 60,
                          onPressed: () {
                            Navigator.pushReplacement(
                              context,
                              MaterialPageRoute(
                                builder: (context) => const HomePage(),
                              ),
                            );
                          },
                        ),
                      ),
                      Container(
                        padding: EdgeInsets.all(50),
                        alignment: Alignment.center,
                        child:IconButton(
                          icon: Icon(Icons.call),
                          iconSize: 60,
                          onPressed: () {
                            Navigator.pushReplacement(
                              context,
                              MaterialPageRoute(
                                builder: (context) => const HomePage(),
                              ),
                            );
                          },
                        ),
                      ),
                    ],
                  ),
                )
            ),
          ],
        ),
      ),
    );
  }}