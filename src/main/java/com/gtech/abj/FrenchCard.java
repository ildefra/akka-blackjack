package com.gtech.abj;

public class FrenchCard {

public enum FrenchRank {
TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE

//TODO: int value
}
public enum FrenchSuit {HEART, DIAMOND, CLUB, SPADE }

public final FrenchRank rank;
public final FrenchSuit suit;

public FrenchCard(final FrenchRank rank, final FrenchSuit suit) {
    this.rank = rank;
    this.suit = suit;
}
}
