package nuzlocke.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
public class TrainerTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainerTeamId;

    @NotEmpty
    @Size(min = 1, max = 6)
    @OneToMany(mappedBy = "trainerTeam")
    private List<Pokemon> pokemons = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "trainer_id")
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
