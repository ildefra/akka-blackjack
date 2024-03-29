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

private static final int BUST_THRESHOLD = 21;


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
 * <li>
 * {@link Bet} -> registers bet, if last one deal cards and then send
 * {@link HitOrStand} to the player with the highest score
 * </li>
 * <li>
 * {@link PlayerHit} -> deal card to player, then send {@link HitOrStand} to him
 * if he did not bust, or else to next player (could be the dealer)
 * </li>
 * <li>
 * {@link PlayerStand} -> send {@link HitOrStand} to next player (could be the
 * dealer)
 * </li>
 * <li>
 * {@link DealerHit} -> deal card to dealer, then send {@link HitOrStand} to him
 * if he did not bust, or else every player wins double of his initial bet. A
 * new game will be started soon
 * </li>
 * <li>
 * {@link DealerStand} -> players with the dealer's score take back their money,
 * players who have beaten him take back their money doubled. A new game will be
 * started soon
 * </li>
 * </ol>
 */
@Override
public void onReceive(final Object message) throws Exception {
    log.debug("received message {} from {}", message, sender());
    if (message instanceof RegisterPlayer) {
        waitingPlayers.add(sender());
        
        //TODO: timer to wait for other players
        if (!gameStarted) startNewGame();
    } if (message instanceof Bet) {
        handleBet((Bet) message);
    } if (message instanceof PlayerHit) {
        handlePlayerHit();
    } if (message instanceof PlayerStand) {
        playingOrder.pop();
        nextPlay();
    } if (message instanceof DealerHit) {
        handleDealerHit();
    } if (message instanceof DealerStand) {
        handleDealerStand();
    } else unhandled(message);
}


/** Two french decks = 104 cards. */
private static final int NO_OF_DECKS = 2;

//XXX: toolong
private void startNewGame() {
    log.info("A new game is starting!");
    gameStarted = true;
    deck = new BJDeck(NO_OF_DECKS);
    
    
    dealer.reset();
    dealer.ref.tell(new Reset(), null);
    
    for (PlayerData player : playingPlayers) {
        player.reset();
        player.ref.tell(new Reset(), null);
    }
    
    
    for (ActorRef player : waitingPlayers) {
        playingPlayers.add(new PlayerData(player));
    }
    waitingPlayers.clear();
    
    dealCardTo(dealer); //dealer is dealt a (covered) card immediately
    letsBet();
}
private void letsBet() {
    log.info("People, let's bet!");
    for (PlayerData player : playingPlayers) {
        player.ref.tell(new AskBet(), self());
    }
}


private void handleBet(final Bet bet) {
    log.info("A bet was placed: {}", bet);
    
    //TODO: timer to kick non-betting players out
    
    registerBet(bet);
    
    
    boolean everyoneHasBet = true;
    for (PlayerData player : playingPlayers) {
        if (player.bet == 0) everyoneHasBet = false;
    }
    if (!everyoneHasBet) return;
    
    dealCards();
    sortPlayersByScore();
    nextPlay();
}

private void registerBet(final Bet bet) {
    log.debug("registerBet loop");
    for (PlayerData player : playingPlayers) {
        if (player.ref.equals(sender())) {
            player.bet = bet.amount;
            break;
        }
    }
}

private void dealCards() {
    log.debug("Dealing cards");
    for (PlayerData player : playingPlayers) dealCardTo(player);
    dealCardTo(dealer);
    for (PlayerData player : playingPlayers) dealCardTo(player);
}

//XXX: toolong
private void sortPlayersByScore() {
    log.debug("Sorting players by score");
    Integer[] playingOrderArray = new Integer[playingPlayers.size()];
    for (int i = 0; i < playingOrderArray.length; i++) {
        playingOrderArray[i] = Integer.valueOf(i);
    }
    
    Arrays.sort(playingOrderArray, new Comparator<Integer>() {
        
        @Override
        public int compare(final Integer i1, final Integer i2) {
            int score1 = playingPlayers.get(i1).hand.score();
            int score2 = playingPlayers.get(i2).hand.score();
            return Integer.valueOf(score1).compareTo(Integer.valueOf(score2));
        }
    });
    
    for (Integer pos : playingOrderArray) playingOrder.push(pos);
}


private void handlePlayerHit() {
    PlayerData currentPlayer = playingPlayers.get(playingOrder.peek());
    
    //TODO: assert currentPlayer.ref = sender()
    
    dealCardTo(currentPlayer);
    if (currentPlayer.hand.score() > BUST_THRESHOLD) {
        log.info("Player {} busted!", currentPlayer);
        playingOrder.pop();
    }
    nextPlay();
}


private void handleDealerHit() {
    dealCardTo(dealer);
    if (dealer.hand.score() > BUST_THRESHOLD) {
        log.info("Dealer busted!");        
        for (PlayerData player : playingPlayers) {
            if (player.hand.score() <= BUST_THRESHOLD) {
                player.ref.tell(new YouWon(player.bet * 2), null);
            }
        }
        startNewGame();
    } else nextPlay();
}


private void handleDealerStand() {
    log.info("Game has ended!");
    int dealerScore = dealer.hand.score();
    
    for (PlayerData player : playingPlayers) {
        int playerScore = player.hand.score();
        if (playerScore <= BUST_THRESHOLD) {
            if (playerScore == dealerScore) {
                player.ref.tell(new YouWon(player.bet), null);
            } else if (playerScore > dealerScore) {
                player.ref.tell(new YouWon(player.bet * 2), null);
            }
        }
    }
    
    startNewGame();
}


private void dealCardTo(final PlayerData player) {
    log.debug("Dealing a card to {}", player);
    FrenchCard drawnCard = deck.draw();
    player.hand.addCard(drawnCard);
    player.ref.tell(new CardDealt(drawnCard), null);    
}


private void nextPlay() {
    log.debug("Next play");
    if (!playingOrder.isEmpty()) {
        playingPlayers.get(playingOrder.peek()).ref.tell(
                new HitOrStand(), self());
    } else {
        if (!areTherePlayersStillInTheGame()) startNewGame();
        else {
            dealer.ref.tell(new HitOrStand(), self());
        }
    }
}

private boolean areTherePlayersStillInTheGame() {
    log.debug("Are there players still in the game?");
    for (PlayerData player : playingPlayers) {
        if (player.hand.score() <= BUST_THRESHOLD) return true;
    }
    return false;
}
}
