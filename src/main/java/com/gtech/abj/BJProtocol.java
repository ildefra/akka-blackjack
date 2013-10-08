package com.gtech.abj;

import java.io.Serializable;


/** All messages between actors go here. */
public abstract class BJProtocol {


public static final class RegisterPlayer implements Serializable {
private static final long serialVersionUID = -8046027720412677414L;

@Override
public String toString() {return "RegisterPlayer"; }
}


public static final class AskBet implements Serializable {
private static final long serialVersionUID = 681653139963713221L;

@Override
public String toString() {return "AskBet"; }
}


public static final class Bet implements Serializable {
private static final long serialVersionUID = -8495160126588390035L;

public final int amount;

/**
 * Betting message.
 * 
 * @param amount        MUST be a positive number
 */
public Bet(final int amount) {
    if (amount < 1) {
        throw new IllegalArgumentException("amount must be a positive number");
    }
    this.amount = amount;
}

@Override
public String toString() {
    return "Bet{" + "amount='" + amount + '\'' + '}';
}
}


public static final class CardDealt implements Serializable {
private static final long serialVersionUID = 984747865374446469L;

public final FrenchCard card;

public CardDealt(final FrenchCard card) {this.card = card; }

@Override
public String toString() {
    return "CardDealt{" + "card='" + card + '\'' + '}';
}
}


public static final class HitOrStand implements Serializable {
private static final long serialVersionUID = 3114574104726567430L;

@Override
public String toString() {return "HitOrStand"; }
}


}
