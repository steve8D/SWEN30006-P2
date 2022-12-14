package thrones.game.players.humanplayer;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.character.Character;

import java.util.Optional;

public interface IHumanInputAdapter {
    public int selectPile(Character[] characters);

    public void setUpClickListener(Hand hand);

    public Optional<Card> getSelectedCard();

    public void setSelectedCard(Optional<Card> selectedCard);
}
