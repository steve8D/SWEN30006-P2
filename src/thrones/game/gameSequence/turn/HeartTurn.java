package thrones.game.gameSequence.turn;

import ch.aplu.jcardgame.Card;
import thrones.game.GameOfThrones;
import thrones.game.character.BaseCharacter;
import thrones.game.players.Player;
import thrones.game.utility.CardUI;
import thrones.game.utility.LoggingSystem;
import thrones.game.character.Character;

import java.util.Optional;

public class HeartTurn extends Turn {
    public HeartTurn(GameOfThrones game, CardUI cardUI, Character[] characters) {
        super(game, cardUI, characters);
    }
    @Override
    public void runTurn(Player player) {
        Optional<Card> selected;

        selected = player.pickCard(true, characters);

        int playerIndex=player.getPlayerIndex();

        int pileIndex = playerIndex % 2;
        assert selected.isPresent() : " Pass returned on selection of character.";

        LoggingSystem.logMove(playerIndex,selected.get(),pileIndex);

        cardUI.moveToPile(selected.get(),characters[pileIndex].getPile());

        // get existing pile and add heart card into the pile
        BaseCharacter base = (BaseCharacter) characters[pileIndex];
        base.addBaseCard(selected.get());

        updatePileRanks();
    }
}
