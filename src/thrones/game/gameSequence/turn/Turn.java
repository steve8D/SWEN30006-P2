package thrones.game.gameSequence.turn;

import thrones.game.character.Character;
import thrones.game.character.CharacterFactory;
import thrones.game.players.Player;
import thrones.game.utility.CardUI;

public abstract class Turn {
    protected final int ATTACK_RANK_INDEX = 0;
    protected final int DEFENCE_RANK_INDEX = 1;
    protected CardUI cardUI;
    protected Character[] characters;
    protected CharacterFactory characterFactory;

    public Turn(CardUI cardUI, Character[] characters) {
        this.cardUI = cardUI;
        this.characters = characters;
    }

    public abstract void runTurn(Player player);

    protected void updatePileRanks() {
        for (int j = 0; j < characters.length; j++) {
            int[] ranks = characters[j].calculatePileRanks();
            cardUI.updatePileRankState(j, ranks[ATTACK_RANK_INDEX], ranks[DEFENCE_RANK_INDEX]);
        }
    }
}
