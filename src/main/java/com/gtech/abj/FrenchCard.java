package com.gtech.abj;

public class FrenchCard {

public enum FrenchRank {
TWO('2', 2),    THREE('3', 3),  FOUR('4', 4),   FIVE('5', 5),
SIX('6', 6),    SEVEN('7', 7),  EIGHT('8', 8),  NINE('9', 9),
TEN('T', 10),   JACK('J', 10),  QUEEN('Q', 10), KING('K', 10),  ACE('A', 1);

/** User-friendly representation. For logging purposes mainly. */
private final char      code;

/** Blackjack value. */
public final int        value;

private FrenchRank(final char code, final int value) {
    this.code   = code;
    this.value  = value;
}

@Override
public String toString() { return String.valueOf(code); }
}


/* 
 * Has no value in blackjack, I am modeling it anyway. Could be nice to players
 * for UI and/or card counting.
 */
public enum FrenchSuit {
HEART('h'), DIAMOND('d'), CLUB('c'), SPADE('s');

private final char code;

private FrenchSuit(final char code) {this.code = code; }


@Override
public String toString() { return String.valueOf(code); }
}


public final FrenchRank rank;
public final FrenchSuit suit;

public FrenchCard(final FrenchRank rank, final FrenchSuit suit) {
    this.rank = rank;
    this.suit = suit;
}

@Override
public String toString() { return "" + rank + suit; }
}
