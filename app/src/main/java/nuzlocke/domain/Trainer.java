package nuzlocke.domain;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainerId;

    @NotBlank
    private String trainerName;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<TrainerTeam> trainerTeams = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "route_id")
    @JsonBackReference
    private Route route;

    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public Set<TrainerTeam> getTrainerTeams() {
        return trainerTeams;
    }

    public void setTrainerTeams(Set<TrainerTeam> trainerTeams) {
        this.trainerTeams = trainerTeams;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    protected Trainer() {

    }

    public Trainer(String trainerName, Route route) {
        this.trainerName = trainerName;
        this.route = route;
    }

    @Override
    public String toString() {
        return "Trainer [trainerId=" + trainerId + ", trainerName=" + trainerName + ", route=" + route + "]";
    }

}
