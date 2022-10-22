package thrones.game.gameSequence.plays;

import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.character.BaseCharacter;
import thrones.game.character.Character;
import thrones.game.gameSequence.Battle;
import thrones.game.gameSequence.round.ConsequentRound;
import thrones.game.gameSequence.round.FirstRound;
import thrones.game.gameSequence.round.Round;
import thrones.game.gameSequence.strategy.IStartingPlayerStrategy;
import thrones.game.players.Player;
import thrones.game.utility.CardUI;
import thrones.game.utility.PropertiesLoader;

public class Play {
    private final int ATTACK_RANK_INDEX = 0;
    private final int DEFENCE_RANK_INDEX = 1;
    protected GameOfThrones game;
    protected CardUI cardUI;
    protected Character[] characters;
    protected Player[] players;
    protected Round[] rounds;
    protected IStartingPlayerStrategy startingPlayerStrategy;

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
                new FirstRound(game, cardUI, characters, players, startingPlayer),
                new ConsequentRound(game, cardUI, characters, players, startingPlayer),
                new ConsequentRound(game, cardUI, characters, players, startingPlayer)
        };
        return rounds;
    }

    private void updatePileRanks() {
        for (int j = 0; j < characters.length; j++) { //
            int[] ranks = characters[j].calculatePileRanks();
            cardUI.updatePileRankState(j, ranks[ATTACK_RANK_INDEX], ranks[DEFENCE_RANK_INDEX]);
        }
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
        updatePileRanks();
    }

    public void runPlay() {
        for (int i = 0; i < 3; i++) {
            rounds[i].runRound();
        }
        updatePileRanks();
        Battle battle = new Battle(game, characters, cardUI, players);
        battle.doBattle();
        game.delay(Long.parseLong(PropertiesLoader.getProperties().getProperty("WatchingTime", PropertiesLoader.getDefaultWatchingTime())));
        removeOldPiles();
    }
}
