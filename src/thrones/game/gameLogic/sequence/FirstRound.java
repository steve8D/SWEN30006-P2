package thrones.game.gameLogic.sequence;

import thrones.game.GameOfThrones;
import thrones.game.character.Character;
import thrones.game.players.Player;
import thrones.game.utility.CardUI;

public class FirstRound extends Round{

    public FirstRound(GameOfThrones game, CardUI cardUI, Character[] characters, Player[] players, int startingPlayer) {
        super(game, cardUI, characters, players, startingPlayer);
    }

    @Override
    protected Turn[] createTurns() {
        Turn[] turns = {
                new HeartTurn(game,cardUI,characters),
                new HeartTurn(game,cardUI,characters),
                new EffectTurn(game,cardUI,characters),
                new EffectTurn(game,cardUI,characters),
        };

        return turns;
    }
}
