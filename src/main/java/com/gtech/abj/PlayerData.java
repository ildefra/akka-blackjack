package com.gtech.abj;

import akka.actor.ActorRef;


/**
 * Game data about playing players, dealer included. For the dealer {@link #bet}
 * is always 0.
 */
public class PlayerData {

public int bet;

public final BJHand hand;
public final ActorRef ref;

public PlayerData(final ActorRef ref) {
    this.ref = ref;
    hand = new BJHand();
}

public void reset() {
    bet = 0;
    hand.reset();
}

@Override
public String toString() {
    return String.format("PlayerData - bet = %d, hand = '%s', ref = '%s'",
            bet, hand, ref);
}
}
