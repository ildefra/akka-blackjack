package com.gtech.abj;

import java.io.Serializable;


/** All messages between actors go here. */
public abstract class BJProtocol {


public static final class RegisterPlayer implements Serializable {
private static final long serialVersionUID = -8046027720412677414L;

@Override
public String toString() {return "RegisterPlayer"; }
}


public static final class Reset implements Serializable {
private static final long serialVersionUID = 3637194318630895220L;

@Override
public String toString() {return "Reset"; }
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


/** Player asking for another card. */
public static final class PlayerHit implements Serializable {
private static final long serialVersionUID = -1893524193643930249L;

@Override
public String toString() {return "PlayerHit"; }
}


/** Player wants no more cards. */
public static final class PlayerStand implements Serializable {
private static final long serialVersionUID = -6173822909172912895L;

@Override
public String toString() {return "PlayerStand"; }
}


/** Dealer asking for another card. */
public static final class DealerHit implements Serializable {
private static final long serialVersionUID = -8934413265015413020L;

@Override
public String toString() {return "DealerHit"; }
}


/** Dealer wants no more cards. */
public static final class DealerStand implements Serializable {
private static final long serialVersionUID = 847810856690077721L;

@Override
public String toString() {return "DealerStand"; }
}


}
