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

//dealcard

//hitorstand

}
