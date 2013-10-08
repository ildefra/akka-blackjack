package com.gtech.abj;

import java.util.ArrayList;
import java.util.List;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import static com.gtech.abj.BJProtocol.*;


public class DealerActor extends UntypedActor {

private final LoggingAdapter log =
        Logging.getLogger(getContext().system(), this);

public static Props props() {return Props.create(DealerActor.class); }


private List<FrenchCard> cards = new ArrayList<FrenchCard>();

public DealerActor() {
    log.info("created dealer {}", this);
}

/**
 * Defines actor's reactions to messages.
 * <ol>
 * </ol>
 */
@Override
public void onReceive(final Object message) throws Exception {
    log.debug("received message {}", message);
    if (message instanceof CardDealt) {
        cards.add(((CardDealt) message).card);
    } else if (message instanceof HitOrStand) {
        sender().tell(
                shouldHit() ? new DealerStand() : new DealerHit(), self());
    } else unhandled(message);
}
private boolean shouldHit() {
    
    //TODO: dealer logic
    
    return false;
}
}
