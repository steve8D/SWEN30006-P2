package thrones.game.gameSequence.round;

import thrones.game.character.Character;
import thrones.game.gameSequence.turn.Turn;
import thrones.game.players.Player;
import thrones.game.utility.CardUI;

public abstract class Round {
    protected CardUI cardUI;
    protected Character[] characters;
    private Player[] players;
    private int startingPlayer;
    private Turn[] turns = null;

    public Round(CardUI cardUI, Character[] characters, Player[] players, int startingPlayer) {
        this.cardUI = cardUI;
        this.characters = characters;
        this.players = players;
        this.startingPlayer = startingPlayer;
        this.turns = createTurns();
    }

    public void runRound() {
        int currPlayer = startingPlayer;
        for (int i = 0; i < 4; i++) {
            currPlayer = getPlayerIndex(currPlayer);
            turns[i].runTurn(players[currPlayer]);
            currPlayer++;
        }
    }

    private int getPlayerIndex(int index) {
        return index % 4;
    }//magic for now

    protected abstract Turn[] createTurns();
}
