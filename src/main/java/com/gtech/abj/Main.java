package com.gtech.abj;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;


public final class Main {

private Main() { }
public static void main(final String[] args) {
    final ActorSystem system = ActorSystem.create("table");
    
    final ActorRef board = system.actorOf(BoardActor.props(), "board");
    
    system.actorOf(PlayerActor.props(board), "player1");
    system.actorOf(PlayerActor.props(board), "player2");
    system.actorOf(PlayerActor.props(board), "player3");
}
}
