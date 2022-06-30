import 'package:flutter/material.dart';

class PostItem extends StatelessWidget {
  final  profileImg;
  final  name;
  final  postImg;
  final  caption;
  final isLoved;
  final  likedBy;
  final  viewCount;
  final  dayAgo;
  const PostItem({
     key, this.profileImg, this.name, this.postImg, this.isLoved, this.likedBy, this.viewCount, this.dayAgo, this.caption,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 10),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 10,horizontal: 15),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: <Widget>[
                Row(children: <Widget>[
                  Container(
                    width: 40,
                    height: 40,
                    decoration: BoxDecoration(
                        shape: BoxShape.circle,
                        image: DecorationImage(image: NetworkImage(profileImg),fit: BoxFit.cover)
                    ),
                  ),
                  const SizedBox(width: 15,),
                  Text(name,style: const TextStyle(

                      fontSize: 15,
                      fontWeight: FontWeight.w500
                  ),)
                ],),
              ],
            ),
          ),
          const SizedBox(height: 12,),
          Container(
            height: 400,
            decoration: BoxDecoration(
                image: DecorationImage(image: NetworkImage(postImg),fit: BoxFit.cover)
            ),
          ),
          const SizedBox(height: 10,),
          Padding(
            padding: const EdgeInsets.only(left: 15,right: 15,top: 3),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: <Widget>[
                Row(
                  children: <Widget>[
                    IconButton(
                      icon: const Icon(Icons.upload),
                      onPressed: () {
                      },
                    ),
                  ],
                ),
              ],
            ),
          ),
          const SizedBox(height: 12,),
          const SizedBox(height: 12,),
          Padding(padding: const EdgeInsets.only(left: 15,right: 15),
              child: RichText(text: TextSpan(
                  children: [
                    TextSpan(
                        text: "$name ",
                        style: const TextStyle(
                            fontSize: 15,
                            fontWeight: FontWeight.w700
                        )
                    ),
                    TextSpan(
                        text: "$caption",
                        style: const TextStyle(
                            fontSize: 15,
                            fontWeight: FontWeight.w500
                        )
                    ),

                  ]
              ))),
          const SizedBox(height: 12,),
          const Padding(padding: EdgeInsets.only(left: 15,right: 15),
          ),
          SizedBox(height: 12,),
          Padding(padding: const EdgeInsets.only(left: 15,right: 15),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: <Widget>[
                  Row(
                    children: <Widget>[
                      Container(
                        width: 30,
                        height: 30,
                        decoration: BoxDecoration(
                            shape: BoxShape.circle,
                            image: DecorationImage(image: NetworkImage(profileImg),fit: BoxFit.cover)
                        ),
                      ),
                      const SizedBox(width: 15,),

                    ],
                  ),
                ],
              )
          ),
          const SizedBox(height: 12,),
        ],
      ),
    );
  }
}