package thrones.game.gameLogic.sequence;

import thrones.game.GameOfThrones;
import thrones.game.character.Character;
import thrones.game.players.Player;
import thrones.game.utility.CardUI;

public class ConsequentRound extends Round{
    public ConsequentRound(GameOfThrones game, CardUI cardUI, Character[] characters, Player[] players, int startingPlayer, boolean[] humanPlayers) {
        super(game, cardUI, characters, players, startingPlayer, humanPlayers);
    }

    @Override
    protected Turn[] createTurns() {
        Turn[] turns = {
                new EffectTurn(game,cardUI,characters),
                new EffectTurn(game,cardUI,characters),
                new EffectTurn(game,cardUI,characters),
                new EffectTurn(game,cardUI,characters),
        };

        return turns;
    }
}
