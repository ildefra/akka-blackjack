package com.gtech.abj.impl;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.gtech.abj.BoardActor;
import com.gtech.abj.PlayerActor;
import com.gtech.abj.api.InstantiateDealer;


public class InstantiateDealerImpl implements InstantiateDealer {

//XXX: dealerID is assigned to board, not dealer
@Override
public boolean instantiate(
        final String actorSystemName, final String dealerID) {
    final ActorSystem system = ActorSystem.create(actorSystemName);
    
    final ActorRef board = system.actorOf(BoardActor.props(), dealerID);
    
    system.actorOf(PlayerActor.props(board), "player1");
    system.actorOf(PlayerActor.props(board), "player2");
    system.actorOf(PlayerActor.props(board), "player3");

    return true;
}

}
