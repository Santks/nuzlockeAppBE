package nuzlocke.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pokemonId;

    @NotBlank
    private String pokemonName;

    @NotEmpty
    private List<String> abilities;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Type type1;

    @Enumerated(EnumType.STRING)
    private Type type2;

    @ManyToOne
    @JoinColumn(name = "trainerTeam_id")
    private TrainerTeam trainerTeam;

    @OneToMany(mappedBy = "pokemon")
    private Set<PokemonMoveSet> moveSets = new HashSet<>();

    public Long getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(Long pokemonId) {
        this.pokemonId = pokemonId;
    }

    public String getPokemonName() {
        return pokemonName;
    }

    public void setPokemonName(String pokemonName) {
        this.pokemonName = pokemonName;
    }

    public List<String> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<String> abilities) {
        this.abilities = abilities;
    }

    public Type getType1() {
        return type1;
    }

    public void setType1(Type type1) {
        this.type1 = type1;
    }

    public Type getType2() {
        return type2;
    }

    public void setType2(Type type2) {
        this.type2 = type2;
    }

    public TrainerTeam getTrainerTeam() {
        return trainerTeam;
    }

    public void setTrainerTeam(TrainerTeam trainerTeam) {
        this.trainerTeam = trainerTeam;
    }

    public Set<PokemonMoveSet> getMoveSets() {
        return moveSets;
    }

    public void setMoveSets(Set<PokemonMoveSet> moveSets) {
        this.moveSets = moveSets;
    }

    protected Pokemon() {
    }

    public Pokemon(String pokemonName) {
        this.pokemonName = pokemonName;
    }

    public Pokemon(String pokemonName, List<String> abilities, TrainerTeam trainerTeam, Set<PokemonMoveSet> moveSets) {
        this.pokemonName = pokemonName;
        this.abilities = abilities;
        this.trainerTeam = trainerTeam;
        this.moveSets = moveSets;
    }

    public Pokemon(String pokemonName, List<String> abilities, Set<PokemonMoveSet> moveSets) {
        this.pokemonName = pokemonName;
        this.abilities = abilities;
        this.moveSets = moveSets;
    }

    public Pokemon(String pokemonName, List<String> abilities, Type type1, Type type2) {
        this.pokemonName = pokemonName;
        this.abilities = abilities;
        this.type1 = type1;
        this.type2 = type2;
    }

    @Override
    public String toString() {
        return "Pokemon [pokemonId=" + pokemonId + ", pokemonName=" + pokemonName + ", abilities=" + abilities
                + ", type1=" + type1 + ", type2=" + type2 + ", trainerTeam=" + trainerTeam + "]";
    }

}
