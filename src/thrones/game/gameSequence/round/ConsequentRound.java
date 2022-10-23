package thrones.game.gameSequence.round;

import thrones.game.GameOfThrones;
import thrones.game.character.Character;
import thrones.game.gameSequence.turn.EffectTurn;
import thrones.game.gameSequence.turn.Turn;
import thrones.game.players.Player;
import thrones.game.utility.CardUI;

public class ConsequentRound extends Round {
    public ConsequentRound(CardUI cardUI, Character[] characters, Player[] players, int startingPlayer) {
        super(cardUI, characters, players, startingPlayer);
    }

    @Override
    protected Turn[] createTurns() {
        Turn[] turns = {
                new EffectTurn(cardUI, characters),
                new EffectTurn(cardUI, characters),
                new EffectTurn(cardUI, characters),
                new EffectTurn(cardUI, characters),
        };
        return turns;
    }
}
