package thrones.game.gameSequence.turn;

import thrones.game.GameOfThrones;
import thrones.game.players.Player;
import thrones.game.utility.CardUI;
import thrones.game.character.Character;

public abstract class Turn {
    protected GameOfThrones game;
    protected CardUI cardUI;
    protected Character[] characters;

    protected final int ATTACK_RANK_INDEX = 0;
    protected final int DEFENCE_RANK_INDEX = 1;

    public Turn(GameOfThrones game, CardUI cardUI, Character[] characters) {
        this.game = game;
        this.cardUI = cardUI;
        this.characters = characters;
    }

    public abstract void runTurn(Player player);

    protected void updatePileRanks() {
        for (int j = 0; j < characters.length; j++) { //characters.length remove
            int[] ranks = characters[j].calculatePileRanks();
            cardUI.updatePileRankState(j, ranks[ATTACK_RANK_INDEX], ranks[DEFENCE_RANK_INDEX]);
        }
    }
}
