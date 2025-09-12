package nuzlocke.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;

@Entity
public class TrainerTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainerTeamId;

    @Size(max = 6)
    @OneToMany(mappedBy = "trainerTeam", fetch = FetchType.EAGER)
    private List<Pokemon> pokemons = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    @JsonIgnore
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

    protected TrainerTeam() {
    }

    public TrainerTeam(Trainer trainer, List<Pokemon> pokemons) {
        this.trainer = trainer;
        this.pokemons = pokemons;
    }

    public TrainerTeam(Trainer trainer) {
        this.trainer = trainer;
    }

    @Override
    public String toString() {
        return "TrainerTeam [trainerTeamId=" + trainerTeamId + ", pokemons=" + pokemons + ", trainer=" + trainer + "]";
    }

}
