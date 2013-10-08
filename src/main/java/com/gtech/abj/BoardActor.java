package com.gtech.abj;

import java.util.ArrayList;
import java.util.List;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import static com.gtech.abj.BJProtocol.*;


public class BoardActor extends UntypedActor {

private final LoggingAdapter log =
        Logging.getLogger(getContext().system(), this);

public static Props props() {return Props.create(BoardActor.class); }


private BJDeck                  deck;
private List<PlayerData>        playingPlayers = new ArrayList<PlayerData>();
private List<ActorRef>          waitingPlayers = new ArrayList<ActorRef>();
private boolean                 gameStarted;

private final PlayerData dealer;

public BoardActor() {
    dealer =
            new PlayerData(getContext().actorOf(DealerActor.props(), "dealer"));
    log.info("created board {}", this);
}


/**
 * Defines actor's reactions to messages.
 * <ol>
 * <li>
 * {@link HandStart} -> sends {@link AskBet} to all {@link PlayerActor}s
 * </li>
 * <li>{@link Bet} -> registers bet</li>
 * </ol>
 */
@Override
public void onReceive(final Object message) throws Exception {
    log.debug("received message {}", message);
    if (message instanceof RegisterPlayer) {
        waitingPlayers.add(getSender());
        if (!gameStarted) startGame();
    } if (message instanceof Bet) {
        handleBet((Bet) message);
    } else unhandled(message);
}

private static final int NO_OF_DECKS = 2;
private void startGame() {
    log.debug("Inside startGame");
    gameStarted = true;
    dealer.reset();
    deck = new BJDeck(NO_OF_DECKS);
    for (PlayerData player : playingPlayers) player.reset();
    for (ActorRef player : waitingPlayers) {
        playingPlayers.add(new PlayerData(player));
    }
    waitingPlayers.clear();
    
    //carta al dealer
    
    for (PlayerData player : playingPlayers) {
        player.ref.tell(new AskBet(), self());
    }
}

private void handleBet(final Bet bet) {
    
    //TODO: timer to kick non-betting players out
    
    for (PlayerData player : playingPlayers) {
        if (player.ref.equals(getSender())) {
            player.bet = bet.amount;
        }
        break;
    }
    
    boolean everyoneHasBet = true;
    for (PlayerData player : playingPlayers) {
        if (player.bet == 0) everyoneHasBet = false;
    }
    
    //if tutti bet dealcards
}

//dealcards 

//carta a tutti
//carta al dealer
//carta a tutti

}
