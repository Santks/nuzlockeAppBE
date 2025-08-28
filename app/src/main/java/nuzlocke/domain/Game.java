package nuzlocke.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameId;

    @NotBlank
    private String title;

    @NotNull
    private String developer;

    @NotNull
    private String description;

    @NotNull
    private int gameGeneration;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    @Size(max = 2)
    private List<Region> regions = new ArrayList<>();

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGameGeneration() {
        return gameGeneration;
    }

    public void setGameGeneration(int gameGeneration) {
        this.gameGeneration = gameGeneration;
    }

    protected Game() {
    }

    public Game(String title, String developer, String description, int gameGeneration) {
        this.title = title;
        this.developer = developer;
        this.description = description;
        this.gameGeneration = gameGeneration;
    }

    @Override
    public String toString() {
        return "Game [gameId=" + gameId + ", title=" + title + ", developer=" + developer + ", description="
                + description + ", gameGeneration=" + gameGeneration + "]";
    }

}
