package thrones.game.utility;

import ch.aplu.jcardgame.Card;
import thrones.game.GameOfThrones;

import java.util.ArrayList;

public class CardCounter {
    // the event bus for publish subscribe
    public static CardCounter instance;
    ArrayList<Subscriber> subscribersDiamond = new ArrayList<>();

    public static CardCounter getInstance() {
        if (instance == null) {
            instance = new CardCounter();
        }
        return instance;
    }

    public void subscribe(Subscriber subscriber, String eventType) {
        if (eventType.equals("Diamonds")) {
            subscribersDiamond.add(subscriber);
        }
    }

    private void notifySubscribers(Card c) {
        for (Subscriber s : subscribersDiamond) {
            s.notify(c);
        }
    }

    public void publish(Publisher p, Card event) {
        if (((GameOfThrones.Suit) event.getSuit()) == GameOfThrones.Suit.DIAMONDS) {
            notifySubscribers(event);
        }
    }
}
