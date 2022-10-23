package thrones.game.utility;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.players.Player;

import java.util.stream.Collectors;

public class LoggingSystem {
    private final static String[] PLAYER_TEAMS = {"[Players 0 & 2]", "[Players 1 & 3]"};
    private final static int ATTACK_RANK_INDEX = 0;
    private final static int DEFENCE_RANK_INDEX = 1;

    /*
    Canonical String representations of Suit, Rank, Card, and Hand
    */
    static String canonical(GameOfThrones.Suit s) {
        return s.toString().substring(0, 1);
    }

    static String canonical(GameOfThrones.Rank r) {
        switch (r) {
            case ACE:
            case KING:
            case QUEEN:
            case JACK:
            case TEN:
                return r.toString().substring(0, 1);
            default:
                return String.valueOf(r.getRankValue());
        }
    }

    static  String canonical(Card c) {
        return canonical((GameOfThrones.Rank) c.getRank()) + canonical((GameOfThrones.Suit) c.getSuit());
    }

    static String canonical(Hand h) {
        return "[" + h.getCardList().stream().map((Card c) -> canonical(c)).collect(Collectors.joining(",")) + "]";
    }

    public static void logScores(int[] scores) {
        System.out.println(PLAYER_TEAMS[0] + " score = " + scores[0] + "; " + PLAYER_TEAMS[1] + " score = " + scores[1]);
    }

    public static void logHand(Player p) {
        System.out.println("hands[" + p.getPlayerIndex() + "]: " + canonical(p.getHand()));
    }

    public static void logMove(int player, Card card, int pileIndex) {
        System.out.println("Player " + player + " plays " + canonical(card) + " on pile " + pileIndex);
    }

    public static void logPile(Hand hand, int[] pileRanks, int pilenum) {
        System.out.println("piles[" + pilenum + "]: " + canonical(hand));
        System.out.println("piles[" + pilenum + "] is " + "Attack: " + pileRanks[ATTACK_RANK_INDEX] + " - Defence: " + pileRanks[DEFENCE_RANK_INDEX]);
    }

    public static void logPiles(Hand[] piles, int[] pile0Ranks, int[] pile1Ranks) {
        logPile(piles[0], pile0Ranks, 0);
        logPile(piles[1], pile1Ranks, 1);
    }

    public static void logBattle(String text) {
        System.out.println(text);
    }

    public static void logResult(int score0, int score1) {
        String text;
        if (score0 > score1) {
            text = "Players 0 and 2 won.";
        } else if (score0 == score1) {
            text = "All players drew.";
        } else {
            text = "Players 1 and 3 won.";
        }
        System.out.println("Result: " + text);
    }

    public static void logSeed(int seed) {
        System.out.println("Seed = " + seed);
    }
}
