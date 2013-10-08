package com.gtech.abj;

import java.util.ArrayList;
import java.util.List;


public class BJHand {

private List<FrenchCard> cards = new ArrayList<FrenchCard>();

public void addCard(final FrenchCard card) {cards.add(card); }
public void reset() {cards.clear(); }

public int score() {
    int score = 0;
    for (FrenchCard card : cards) score += card.rank.value;
    return score;
}
}
