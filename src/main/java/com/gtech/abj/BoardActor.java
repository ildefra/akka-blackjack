package com.gtech.abj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import static com.gtech.abj.BJProtocol.*;


//XXX: on its way to God Object -> extract class!
public class BoardActor extends UntypedActor {

private final LoggingAdapter log =
        Logging.getLogger(getContext().system(), this);

public static Props props() {return Props.create(BoardActor.class); }


private final PlayerData dealer;

/** Players in current game. Added in registration order. */
private List<PlayerData>        playingPlayers = new ArrayList<PlayerData>();

private List<ActorRef>          waitingPlayers = new ArrayList<ActorRef>();

private BJDeck                  deck;
private boolean                 gameStarted;
private Stack<Integer>          playingOrder = new Stack<Integer>();

public BoardActor() {
    dealer =
            new PlayerData(getContext().actorOf(DealerActor.props(), "dealer"));
    log.info("created board {}", this);
}


//XXX: toolong
/**
 * Defines actor's reactions to messages.
 * <ol>
 * <li>
 * {@link RegisterPlayer} -> adds sender to list, starts the game if not already
 * started
 * </li>
 * <li>{@link Bet} -> registers bet, deal cards if last one</li>
 * </ol>
 */
@Override
public void onReceive(final Object message) throws Exception {
    log.debug("received message {}", message);
    if (message instanceof RegisterPlayer) {
        waitingPlayers.add(sender());
        if (!gameStarted) startGame();  //TODO: timer to wait for other players
    } if (message instanceof Bet) {
        handleBet((Bet) message);
    } if (message instanceof PlayerHit) {
        PlayerData currentPlayer = playingPlayers.get(playingOrder.peek());
        
        //TODO: assert currentPlayer.ref = sender()
        
        dealCardTo(currentPlayer);
        if (currentPlayer.score() > 21) {
            //pop and log
        }
        //if stack nn vuota peek ! hitorstand
        //passalapallaaldealer
    } if (message instanceof PlayerStand) {
        //pop
        //if stack nn vuota peek ! hitorstand
        //passalapallaaldealer
    } if (message instanceof DealerHit) {
        //?
    } if (message instanceof DealerStand) {
        //?
    } else unhandled(message);
}


/** Two french decks = 104 cards. */
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
    
    dealCardTo(dealer); //dealer is dealt a (covered) card immediately
    letsBet();
}
private void letsBet() {
    for (PlayerData player : playingPlayers) {
        player.ref.tell(new AskBet(), self());
    }
}


private void handleBet(final Bet bet) {
    
    //TODO: timer to kick non-betting players out
    
    registerBet(bet);
    
    boolean everyoneHasBet = true;
    for (PlayerData player : playingPlayers) {
        if (player.bet == 0) everyoneHasBet = false;
    }
    if (!everyoneHasBet) return;
    
    dealCards();
    sortPlayersByScore();
    playingPlayers.get(playingOrder.peek()).ref.tell(new HitOrStand(), self());
}

private void registerBet(final Bet bet) {
    for (PlayerData player : playingPlayers) {
        if (player.ref.equals(sender())) player.bet = bet.amount;
        break;
    }
}

private void dealCards() {
    for (PlayerData player : playingPlayers) dealCardTo(player);
    dealCardTo(dealer);
    for (PlayerData player : playingPlayers) dealCardTo(player);
}

//XXX: toolong
private void sortPlayersByScore() {
    Integer[] playingOrderArray = new Integer[playingPlayers.size()];
    for (int i = 0; i < playingOrderArray.length; i++) {
        playingOrderArray[i] = Integer.valueOf(i);
    }
    
    Arrays.sort(playingOrderArray, new Comparator<Integer>() {
        
        @Override
        public int compare(final Integer i1, final Integer i2) {
            int score1 = playingPlayers.get(i1).score();
            int score2 = playingPlayers.get(i2).score();
            return Integer.compare(score1, score2);
        }
    });
    
    for (Integer pos : playingOrderArray) playingOrder.push(pos);
}


private void dealCardTo(final PlayerData player) {
    FrenchCard drawnCard = deck.draw();
    player.cards.add(drawnCard);
    player.ref.tell(new CardDealt(drawnCard), self());    
}
}
