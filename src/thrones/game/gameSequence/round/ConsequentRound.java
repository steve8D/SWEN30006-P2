package thrones.game.gameSequence.round;

import thrones.game.GameOfThrones;
import thrones.game.character.Character;
import thrones.game.gameSequence.turn.EffectTurn;
import thrones.game.gameSequence.turn.Turn;
import thrones.game.players.Player;
import thrones.game.utility.CardUI;

public class ConsequentRound extends Round {
    public ConsequentRound(GameOfThrones game, CardUI cardUI, Character[] characters, Player[] players, int startingPlayer) {
        super(game, cardUI, characters, players, startingPlayer);
    }

    @Override
    protected Turn[] createTurns() {
        Turn[] turns = {
                new EffectTurn(game, cardUI, characters),
                new EffectTurn(game, cardUI, characters),
                new EffectTurn(game, cardUI, characters),
                new EffectTurn(game, cardUI, characters),
        };
        return turns;
    }
}
