package thrones.game.gameLogic.sequence;

import ch.aplu.jcardgame.Card;
import thrones.game.GameOfThrones;
import thrones.game.character.BaseCharacter;
import thrones.game.players.Player;
import thrones.game.players.PlayerType;
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

        selected = player.pickCard(true);


//        if (playerType == PlayerType.HUMAN) {
//            //waitForCorrectSuit(playerIndex, true);
//            selected = player.pickCard(true);
//        } else {
//            //pickACorrectSuit(playerIndex, true);
//            selected = player.pickCard(true);
//        }

        int playerIndex=player.getPlayerIndex();

        int pileIndex = playerIndex % 2;
        assert selected.isPresent() : " Pass returned on selection of character.";

        LoggingSystem.logMove(playerIndex,selected.get(),pileIndex);

        cardUI.moveToPile(selected.get(),characters[pileIndex].getPile());

        // i am forced to initialise a character before a heart card is chosen because i need the pile for clicking purposes
        // will fix later
        BaseCharacter base = (BaseCharacter) characters[pileIndex];
        base.addBaseCard(selected.get());


        updatePileRanks();

    }


}
