
import 'package:flutter/material.dart';
import 'post_item.dart';
import 'post_share.dart';

class HomePage extends StatefulWidget {
  static const String id ="HomePage";

  const HomePage({Key? key}) : super(key: key);
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {


  @override
  Widget build(BuildContext context) {
    return getBody();
  }

  Widget getBody() {
    onTap: () {
      Navigator.pushNamed(context, HomePage.id);
    };
    return SingleChildScrollView(
      child: Column(

        children: <Widget>[
          Column(
            children: List.generate(posts.length, (index){
              return PostItem(

                postImg: posts[index]['postImg'],
                profileImg: posts[index]['profileImg'],
                name: posts[index]['name'],
              );
            }),
          )

        ],
      ),
    );
  }
}


