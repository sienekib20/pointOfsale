package com.buesimples.posfx.animations;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeInDown;
import animatefx.animation.FadeInLeft;
import animatefx.animation.FadeInRight;
import animatefx.animation.FadeInUp;
import animatefx.animation.FadeOut;
import animatefx.animation.Shake;
import javafx.scene.Node;

public class Animations {

   public static void fadeInUp(Node node, double duration) {
      new FadeInUp(node).setSpeed(duration).play();
   }

   public static void fadeInDown(Node node, double duration) {
      new FadeInDown(node).setSpeed(duration).play();
   }

   public static void fadeInRight(Node node, double duration) {
      new FadeInRight(node).setSpeed(duration).play();
   }

   public static void fadeInLeft(Node node, double duration) {
      new FadeInLeft(node).setSpeed(duration).play();
   }

   public static void fadeIn(Node node, double duration) {
      new FadeIn(node).setSpeed(duration).play();
   }

   public static void fadeOut(Node node, double duration) {
      new FadeOut(node).setSpeed(duration).play();
   }

   public static void shake(Node node, double duration) {
      new Shake(node).setSpeed(duration).play();
   }
}