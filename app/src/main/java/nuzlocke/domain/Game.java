package nuzlocke.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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

    public Game() {

    }

    public Game(Long gameId, @NotBlank String title, @NotNull String developer, @NotNull String description) {
        this.gameId = gameId;
        this.title = title;
        this.developer = developer;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Game [gameId=" + gameId + ", title=" + title + ", developer=" + developer + ", description="
                + description + "]";
    }

}
