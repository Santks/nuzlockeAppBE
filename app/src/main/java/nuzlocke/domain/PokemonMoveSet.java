package nuzlocke.domain;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class PokemonMoveSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moveSetId;

    @NotBlank
    @Min(1)
    @Max(100)
    private int pokemonLevel;

    @NotEmpty
    @Size(min = 1, max = 4)
    private List<String> moves;

    @NotEmpty
    private String item;

    @ManyToOne
    @JoinColumn(name = "pokemonId")
    private Pokemon pokemon;

    public Long getMoveSetId() {
        return moveSetId;
    }

    public void setMoveSetId(Long moveSetId) {
        this.moveSetId = moveSetId;
    }

    public int getPokemonLevel() {
        return pokemonLevel;
    }

    public void setPokemonLevel(int pokemonLevel) {
        this.pokemonLevel = pokemonLevel;
    }

    public List<String> getMoves() {
        return moves;
    }

    public void setMoves(List<String> moves) {
        this.moves = moves;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    protected PokemonMoveSet() {
    }

    public PokemonMoveSet(int pokemonLevel, List<String> moves, String item) {
        this.pokemonLevel = pokemonLevel;
        this.moves = moves;
        this.item = item;
    }

    @Override
    public String toString() {
        return "PokemonMoveSet [moveSetId=" + moveSetId + ", pokemonLevel=" + pokemonLevel + ", moves=" + moves
                + ", item=" + item + ", pokemon=" + pokemon + "]";
    }

}
