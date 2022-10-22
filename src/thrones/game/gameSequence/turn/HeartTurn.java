package thrones.game.gameSequence.turn;

import ch.aplu.jcardgame.Card;
import thrones.game.GameOfThrones;
import thrones.game.character.BaseCharacter;
import thrones.game.players.Player;
import thrones.game.utility.BrokeRuleException;
import thrones.game.utility.CardUI;
import thrones.game.utility.LoggingSystem;
import thrones.game.character.Character;
import thrones.game.utility.rules.CompositeRule;
import thrones.game.utility.rules.HeartRule;
import thrones.game.utility.rules.LegalityChecker;

import java.util.Optional;

public class HeartTurn extends Turn {
    public HeartTurn(GameOfThrones game, CardUI cardUI, Character[] characters) {
        super(game, cardUI, characters);
    }
    @Override
    public void runTurn(Player player) {

        int playerIndex=player.getPlayerIndex();
        //move this to cardUI
        game.setStatusText("Player " + playerIndex + " select a Heart card to play");

        Optional<Card> selected;
        selected = player.pickCard(true, characters);

        int pileIndex = playerIndex % 2;

        try{
            CompositeRule legalityChecker = new CompositeRule();
            legalityChecker.addRule(new HeartRule());

            if(legalityChecker.isLegal(characters[pileIndex],selected.get() )==false){
                throw new BrokeRuleException("rule violated");
            }

        } catch (BrokeRuleException e){

        }

        assert selected.isPresent() : " Pass returned on selection of character.";

        LoggingSystem.logMove(playerIndex,selected.get(),pileIndex);

        cardUI.moveToPile(selected.get(),characters[pileIndex].getPile());

        // get existing pile and add heart card into the pile
        BaseCharacter base = (BaseCharacter) characters[pileIndex];
        base.addBaseCard(selected.get());

        updatePileRanks();
    }
}
