package com.gtech.abj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.gtech.abj.FrenchCard.FrenchRank;
import com.gtech.abj.FrenchCard.FrenchSuit;


public class BJDeck {

private List<FrenchCard> cards = new ArrayList<FrenchCard>();

public BJDeck(final int noOfDecks) {
    for (int i = 0; i < noOfDecks; i++) addDeck();
    Collections.shuffle(cards);
}
private void addDeck() {
    for (FrenchSuit suit : FrenchSuit.values()) {
        for (FrenchRank rank : FrenchRank.values()) {
            cards.add(new FrenchCard(rank, suit));
        }
    }
}

//carta
}
