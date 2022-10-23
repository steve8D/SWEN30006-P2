package thrones.game.gameSequence.plays;

import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.character.BaseCharacter;
import thrones.game.character.Character;
import thrones.game.gameSequence.round.ConsequentRound;
import thrones.game.gameSequence.round.FirstRound;
import thrones.game.gameSequence.round.Round;
import thrones.game.gameSequence.strategy.IStartingPlayerStrategy;
import thrones.game.players.Player;
import thrones.game.utility.CardUI;
import thrones.game.utility.PropertiesLoader;

public class Play {
    private GameOfThrones game;
    private CardUI cardUI;
    private Character[] characters;
    private Player[] players;
    private Round[] rounds;
    private IStartingPlayerStrategy startingPlayerStrategy;

    public Play(GameOfThrones game, CardUI cardUI, Player[] players, IStartingPlayerStrategy startingPlayerStrategy) {
        this.game = game;
        this.cardUI = cardUI;
        this.players = players;
        this.startingPlayerStrategy = startingPlayerStrategy;
        createNewPiles();
        this.rounds = createRounds();
    }

    private Round[] createRounds() {
        int startingPlayer = startingPlayerStrategy.getStartingPlayer();
        Round[] rounds = {
                new FirstRound(cardUI, characters, players, startingPlayer),
                new ConsequentRound(cardUI, characters, players, startingPlayer),
                new ConsequentRound(cardUI, characters, players, startingPlayer)
        };
        return rounds;
    }

    private void removeOldPiles() {
        Hand[] piles = getPilesFromCharacters(characters);
        cardUI.removeAll(piles);
    }

    private Hand[] getPilesFromCharacters(Character[] characters) { // move this function into card UI and let cardUI accept character[] params
        Hand[] piles = null;
        if (characters != null) { //if there are characters, get their piles
            piles = new Hand[2];
            piles[0] = characters[0].getPile();
            piles[1] = characters[1].getPile();
        }
        return piles;
    }

    private void createNewPiles() {
        characters = new Character[2];
        for (int i = 0; i < 2; i++) {
            characters[i] = new BaseCharacter();
            // get the pile for setting up the listener (this will happen when the heart card is played later)
            // for now the code requires i set up the listener now
            Hand characterPile = characters[i].getPile();
            cardUI.drawPile(characterPile, i);
        }
        cardUI.updatePileRanks(characters);
    }

    public void runPlay() {
        for (int i = 0; i < 3; i++) {
            rounds[i].runRound();
        }
        cardUI.updatePileRanks(characters);
        Battle battle = new Battle(characters, cardUI, players);
        battle.doBattle();
        game.delay(Long.parseLong(PropertiesLoader.getProperties().getProperty("WatchingTime", PropertiesLoader.getDefaultWatchingTime())));
        removeOldPiles();
    }
}
