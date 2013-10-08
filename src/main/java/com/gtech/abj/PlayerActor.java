package com.gtech.abj;

import java.util.ArrayList;
import java.util.List;
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


private List<FrenchCard> cards = new ArrayList<FrenchCard>();

private final ActorRef board;

public PlayerActor(final ActorRef board) {
    this.board = board;
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
 * {@link AskBet} -> replies with a {@link Bet} with a random amount
 * </ol>
 */
@Override
public void onReceive(final Object message) throws Exception {
    log.debug("received message {}", message);
    if (message instanceof AskBet) {
        getSender().tell(new Bet(new Random().nextInt(10) + 1), self());
    } else unhandled(message);
}
}
