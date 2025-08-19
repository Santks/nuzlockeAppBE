package nuzlocke.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class TrainerTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainerTeamId;

    @NotBlank
    private List<Pokemon> pokemons = new ArrayList<>();

    @ManyToOne
    @Column(name = "trainerId")
    private Trainer trainer;

    public Long getTrainerTeamId() {
        return trainerTeamId;
    }

    public void setTrainerTeamId(Long trainerTeamId) {
        this.trainerTeamId = trainerTeamId;
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }

    public void setPokemons(List<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public TrainerTeam(Trainer trainer, List<Pokemon> pokemons) {
        this.trainer = trainer;
        this.pokemons = pokemons;
    }

    @Override
    public String toString() {
        return "TrainerTeam [trainerTeamId=" + trainerTeamId + ", pokemons=" + pokemons + ", trainer=" + trainer + "]";
    }

}
