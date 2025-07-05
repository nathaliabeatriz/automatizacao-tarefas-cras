package com.projeto_scfv_cras.ProjetoCRAS.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios_scfv")
public class UsuarioScfv {
    @Id
    @GeneratedValue
    @Column(name = "id_usuario")
    private Integer id;

    @NotBlank(message = "Nome do usuário é um campo obrigatório")
    @Column(name = "nome")
    private String nome;

    @NotNull(message = "Data de nascimento é um campo obrigatório")
    @Past(message = "A data de nascimento deve ser anterior a atual")
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @NotBlank(message = "NIS é um campo obrigatório")
    @Column(name = "nis", length = 15)
    private String nis;

    @NotBlank(message = "Nome do responsável é um campo obrigatório")
    @Column(name = "nome_responsavel")
    private String nomeResponsavel;

    @NotBlank(message = "Telefone é um campo obrigatório")
    @Column(name = "telefone")
    private String telefone;

    @NotNull(message = "Campo obrigatório")
    @Column(name = "bolsa_familia")
    private Boolean bolsaFamilia;

    @NotNull(message = "Campo obrigatório")
    @Column(name = "prioridade")
    private Boolean prioridade;

    public int getIdade(){
        return Period.between(this.dataNascimento, LocalDate.now()).getYears();
    }

    public String getDataFormatada(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.dataNascimento.format(formatter);   
    }
}
