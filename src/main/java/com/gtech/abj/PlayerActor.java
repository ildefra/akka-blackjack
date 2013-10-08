package com.gtech.abj;

import java.util.Random;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import static com.gtech.abj.BJProtocol.*;


public class PlayerActor extends UntypedActor {

private final LoggingAdapter log =
        Logging.getLogger(getContext().system(), this);

public static Props props(final ActorRef board) {
    return Props.create(PlayerActor.class, board);
}


private final ActorRef board;
private final BJHand hand;

public PlayerActor(final ActorRef board) {
    this.board = board;
    hand = new BJHand();
    log.info("created player {}", this);
}


/** Registers itself to the provided board on construction. */
@Override
public void preStart() {
    board.tell(new RegisterPlayer(), self());
}


/**
 * Defines actor's reactions to messages.
 * <ol>
 * <li>{@link Reset} -> empties hand</li>
 * <li>
 * {@link AskBet} -> replies with a {@link Bet} with a random amount
 * </li>
 * <li>{@link CardDealt} -> saves the card</li>
 * <li>
 * {@link HitOrStand} -> replies with {@link PlayerHit} or {@link PlayerStand}
 * in a random fashion
 * </li>
 * <li>{@link YouWon} -> log message</li>
 * </ol>
 */
@Override
public void onReceive(final Object message) throws Exception {
    log.debug("received message {} from {}", message, sender());
    if (message instanceof Reset) {
        hand.reset();
    } else if (message instanceof AskBet) {
        sender().tell(new Bet(new Random().nextInt(10) + 1), self());
    } else if (message instanceof CardDealt) {
        hand.addCard(((CardDealt) message).card);
        log.info("I received a card, my hand is now: {}", hand);
    } else if (message instanceof HitOrStand) {
        sender().tell(
                shouldHit() ? new PlayerStand() : new PlayerHit(), self());
    } else if (message instanceof YouWon) {
        log.info("Hurray! I won {} dollars!", ((YouWon) message).amount);
    } else unhandled(message);
}
private boolean shouldHit() {
    log.debug("Should I hit?");
    return new Random().nextInt(2) > 0;
}
}
