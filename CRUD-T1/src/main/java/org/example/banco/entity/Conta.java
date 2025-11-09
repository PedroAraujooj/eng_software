package org.example.banco.entity;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class Conta {

    private final Long id;
    private final String titular;
    private BigDecimal saldo;

    public Conta(Long id, String titular, BigDecimal saldoInicial) {
        this.id = id;
        this.titular = titular;
        this.saldo = saldoInicial.setScale(2, RoundingMode.HALF_UP);
    }


    public void depositar(BigDecimal valor) {
        validarValor(valor);
        saldo = saldo.add(valor).setScale(2, RoundingMode.HALF_UP);
    }

    public void debitar(BigDecimal valor) {
        validarValor(valor);
        if (saldo.compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }
        saldo = saldo.subtract(valor).setScale(2, RoundingMode.HALF_UP);
    }

    private void validarValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor inválido");
        }
    }
}
