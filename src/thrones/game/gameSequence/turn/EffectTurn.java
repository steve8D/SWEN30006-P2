package thrones.game.gameSequence.turn;

import ch.aplu.jcardgame.Card;
import thrones.game.GameOfThrones;
import thrones.game.character.*;
import thrones.game.character.Character;
import thrones.game.players.Player;
import thrones.game.utility.CardUI;
import thrones.game.utility.LoggingSystem;

import java.util.Optional;

public class EffectTurn extends Turn {
    public EffectTurn(GameOfThrones game, CardUI cardUI, Character[] characters) {
        super(game, cardUI, characters);
    }

    @Override
    public void runTurn(Player player) {

        int selectedPileIndex;
        Optional<Card> selected;
        int playerIndex = player.getPlayerIndex();

        selected = player.pickCard(false);

        if (selected.isPresent()) {
            // fix this later
            game.setStatusText("Selected: " + LoggingSystem.canonical(selected.get()) + ". Player" + playerIndex + " select a pile to play the card.");

            selectedPileIndex = player.pickPile(characters);

            LoggingSystem.logMove(playerIndex,selected.get(),selectedPileIndex);

            cardUI.moveToPile(selected.get(), characters[selectedPileIndex].getPile());

            //move this to a factory later
            // just new CharacterFactory
            // characterFactory.getCharacter( selected.gt(), characters[selectedPileIndex]
            if(((GameOfThrones.Suit)selected.get().getSuit()).isAttack()) {
                characters[selectedPileIndex] = new AttackEffect(selected.get(), characters[selectedPileIndex]);
            }  else if(((GameOfThrones.Suit)selected.get().getSuit()).isDefence()) {
                characters[selectedPileIndex] = new DefenseEffect(selected.get(), characters[selectedPileIndex]);
            } else if(((GameOfThrones.Suit)selected.get().getSuit()).isMagic()){
                characters[selectedPileIndex] = new MagicEffect(selected.get(), characters[selectedPileIndex]);
            }
            updatePileRanks();
        } else {
            game.setStatusText("Pass.");
        }
    }
}
