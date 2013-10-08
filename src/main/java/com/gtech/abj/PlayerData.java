package com.gtech.abj;

import java.util.ArrayList;
import java.util.List;
import akka.actor.ActorRef;


/**
 * Game data about playing players, dealer included. For the dealer {@link #bet}
 * is always 0.
 */
public class PlayerData {

public int bet;
public List<FrenchCard> cards = new ArrayList<FrenchCard>();

public final ActorRef ref;

public PlayerData(final ActorRef ref) {this.ref = ref; }

public void reset() {
    bet = 0;
    cards = new ArrayList<FrenchCard>();
}
}
