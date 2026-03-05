package com.github.d00ktora.football_tournament.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "teamAID")
	private Team teamA;

	@ManyToOne
	@JoinColumn(name = "teamBID")
	private Team teamB;

	private LocalDateTime date;

	private String score;

	@OneToMany(mappedBy = "teamA", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Match> matchesAsTeamA = new ArrayList<>();

	@OneToMany(mappedBy = "teamB", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Match> matchesAsTeamB = new ArrayList<>();
}
