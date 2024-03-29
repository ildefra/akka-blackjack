package com.gtech.abj;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import static com.gtech.abj.BJProtocol.*;


public class DealerActor extends UntypedActor {

private final LoggingAdapter log =
        Logging.getLogger(getContext().system(), this);

public static Props props() {return Props.create(DealerActor.class); }


private final BJHand hand;

public DealerActor() {
    hand = new BJHand();
    log.info("created dealer {}", this);
}

/**
 * Defines actor's reactions to messages.
 * <ol>
 * <li>{@link Reset} -> empties hand</li>
 * <li>{@link CardDealt} -> saves the card</li>
 * <li>
 * {@link HitOrStand} -> replies with {@link DealerHit} or {@link DealerStand}
 * following the standard blackjack dealer logic
 * </li>
 * </ol>
 */
@Override
public void onReceive(final Object message) throws Exception {
    log.debug("received message {} from {}", message, sender());
    if (message instanceof Reset) {
        hand.reset();
    } else if (message instanceof CardDealt) {
        hand.addCard(((CardDealt) message).card);
        log.info("I received a card, my hand is now: {}", hand);
    } else if (message instanceof HitOrStand) {
        sender().tell(
                shouldHit() ? new DealerHit() : new DealerStand(), null);
    } else unhandled(message);
}
private boolean shouldHit() {
    log.debug("Should I hit?");
    return hand.score() < 17;
}
}
