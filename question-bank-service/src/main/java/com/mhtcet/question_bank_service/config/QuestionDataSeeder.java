package com.mhtcet.question_bank_service.config;

import com.mhtcet.question_bank_service.model.OptionItem;
import com.mhtcet.question_bank_service.model.Question;
import com.mhtcet.question_bank_service.model.Standard; // Remove if standard is a String
import com.mhtcet.question_bank_service.model.Subject;
import com.mhtcet.question_bank_service.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionDataSeeder implements CommandLineRunner {

    private final QuestionRepository questionRepository;

    @Override
    public void run(String... args) {
        if (questionRepository.count() > 0) {
            return;
        }

        List<Question> sampleQuestions = List.of(
                // --- PHYSICS (+1 Mark) ---
                Question.builder()
                        .subject(Subject.PHYSICS)
                        .standard(Standard.XII)
                        .chapter("Rotational Dynamics")
                        .questionText("What is the SI unit of moment of inertia?")
                        .options(List.of(
                                new OptionItem("A", "kg·m²"),
                                new OptionItem("B", "kg·m"),
                                new OptionItem("C", "N·m"),
                                new OptionItem("D", "kg/m²")
                        ))
                        .correctOptionId("A")
                        .marks(1)
                        .explanation("Moment of inertia I = mr², so its SI unit is kg·m².")
                        .build(),

                Question.builder()
                        .subject(Subject.PHYSICS)
                        .standard(Standard.XI)
                        .chapter("Gravitation")
                        .questionText("If the radius of Earth shrinks by 1% with mass remaining constant, acceleration due to gravity on the surface will:")
                        .options(List.of(
                                new OptionItem("A", "Decrease by 2%"),
                                new OptionItem("B", "Increase by 2%"),
                                new OptionItem("C", "Increase by 1%"),
                                new OptionItem("D", "Remain unchanged")
                        ))
                        .correctOptionId("B")
                        .marks(1)
                        .explanation("g = GM/R², so Δg/g = -2(ΔR/R) = -2(-1%) = +2%.")
                        .build(),

                // --- CHEMISTRY (+1 Mark) ---
                Question.builder()
                        .subject(Subject.CHEMISTRY)
                        .standard(Standard.XII)
                        .chapter("Chemical Kinetics")
                        .questionText("The half-life period of a first-order reaction is independent of:")
                        .options(List.of(
                                new OptionItem("A", "Rate constant"),
                                new OptionItem("B", "Temperature"),
                                new OptionItem("C", "Initial concentration of reactant"),
                                new OptionItem("D", "Catalyst")
                        ))
                        .correctOptionId("C")
                        .marks(1)
                        .explanation("For a first-order reaction, t₁/₂ = 0.693 / k, which has no initial concentration term [A]₀.")
                        .build(),

                Question.builder()
                        .subject(Subject.CHEMISTRY)
                        .standard(Standard.XI)
                        .chapter("States of Matter")
                        .questionText("At constant temperature, the pressure of a fixed amount of gas varies inversely with its volume. This is:")
                        .options(List.of(
                                new OptionItem("A", "Charles' Law"),
                                new OptionItem("B", "Boyle's Law"),
                                new OptionItem("C", "Avogadro's Law"),
                                new OptionItem("D", "Dalton's Law")
                        ))
                        .correctOptionId("B")
                        .marks(1)
                        .explanation("Boyle's law states P ∝ 1/V at constant temperature and n.")
                        .build(),

                // --- MATHEMATICS (+2 Marks) ---
                Question.builder()
                        .subject(Subject.MATHEMATICS)
                        .standard(Standard.XII)
                        .chapter("Matrices")
                        .questionText("If A is a non-singular square matrix of order 3 such that |A| = 4, then |2A| equals:")
                        .options(List.of(
                                new OptionItem("A", "8"),
                                new OptionItem("B", "16"),
                                new OptionItem("C", "32"),
                                new OptionItem("D", "64")
                        ))
                        .correctOptionId("C")
                        .marks(2)
                        .explanation("|kA| = kⁿ|A| = 2³ × 4 = 8 × 4 = 32.")
                        .build(),

                Question.builder()
                        .subject(Subject.MATHEMATICS)
                        .standard(Standard.XI)
                        .chapter("Trigonometry")
                        .questionText("What is the principal value of sin⁻¹(-1/2)?")
                        .options(List.of(
                                new OptionItem("A", "π/6"),
                                new OptionItem("B", "-π/6"),
                                new OptionItem("C", "π/3"),
                                new OptionItem("D", "-π/3")
                        ))
                        .correctOptionId("B")
                        .marks(2)
                        .explanation("Principal branch of sin⁻¹ is [-π/2, π/2], and sin(-π/6) = -1/2.")
                        .build(),

                // --- BIOLOGY (+1 Mark) ---
                Question.builder()
                        .subject(Subject.BIOLOGY)
                        .standard(Standard.XII)
                        .chapter("Molecular Basis of Inheritance")
                        .questionText("Which enzyme joins Okazaki fragments on the lagging strand during DNA replication?")
                        .options(List.of(
                                new OptionItem("A", "DNA Helicase"),
                                new OptionItem("B", "RNA Primase"),
                                new OptionItem("C", "DNA Ligase"),
                                new OptionItem("D", "Topoisomerase")
                        ))
                        .correctOptionId("C")
                        .marks(1)
                        .explanation("DNA ligase catalyzes phosphodiester bond formation to seal nicks between Okazaki fragments.")
                        .build(),

                Question.builder()
                        .subject(Subject.BIOLOGY)
                        .standard(Standard.XI)
                        .chapter("Cell Structure and Organization")
                        .questionText("Which cell organelle is responsible for packaging and modification of proteins into vesicles?")
                        .options(List.of(
                                new OptionItem("A", "Golgi apparatus"),
                                new OptionItem("B", "Peroxisome"),
                                new OptionItem("C", "Centrosome"),
                                new OptionItem("D", "Nucleolus")
                        ))
                        .correctOptionId("A")
                        .marks(1)
                        .explanation("The Golgi complex modifies, sorts, and packages proteins arriving from the rough ER.")
                        .build()
        );

        questionRepository.saveAll(sampleQuestions);
        System.out.println("✅ Seeded " + sampleQuestions.size() + " sample MHT-CET questions into Question Bank.");
    }
}
