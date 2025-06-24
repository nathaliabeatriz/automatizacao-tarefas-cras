package com.projeto_scfv_cras.ProjetoCRAS.model;

import java.time.LocalTime;

import com.projeto_scfv_cras.ProjetoCRAS.validations.HorarioValido;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "oficinas")
@HorarioValido
public class Oficina {
    @Id
    @GeneratedValue
    @Column(name = "id_oficina")
    private Integer id;

    @NotBlank(message = "Nome da oficina é um campo obrigatório")
    @Column(name = "nome")
    private String nome;

    @NotNull(message = "Quantidade de vagas é um campo obrigatório")
    @Min(0)
    @Column(name = "qtd_vagas")
    private int qtdVagas;

    @NotNull(message = "Horário de início é um campo obrigatório")
    @Column(name = "horario_inicio")
    private LocalTime horarioInicio;

    @NotNull(message = "Horário de término é um campo obrigatório")
    @Column(name = "horario_termino")
    private LocalTime horarioTermino;

    @NotBlank(message = "Dia da semana é um campo obrigatório")
    @Column(name = "dia_semana")
    private String diaSemana;
}
