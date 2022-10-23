package thrones.game.gameSequence.round;

import thrones.game.character.Character;
import thrones.game.gameSequence.turn.EffectTurn;
import thrones.game.gameSequence.turn.HeartTurn;
import thrones.game.gameSequence.turn.Turn;
import thrones.game.players.Player;
import thrones.game.utility.CardUI;

public class FirstRound extends Round {
    public FirstRound(CardUI cardUI, Character[] characters, Player[] players, int startingPlayer) {
        super(cardUI, characters, players, startingPlayer);
    }

    @Override
    protected Turn[] createTurns() {
        Turn[] turns = {
                new HeartTurn(cardUI, characters),
                new HeartTurn(cardUI, characters),
                new EffectTurn(cardUI, characters),
                new EffectTurn(cardUI, characters),
        };
        return turns;
    }
}
