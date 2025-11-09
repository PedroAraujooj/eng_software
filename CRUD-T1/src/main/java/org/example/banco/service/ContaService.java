package org.example.banco.service;

import lombok.RequiredArgsConstructor;
import org.example.banco.repository.ContaRepository;
import org.springframework.stereotype.Service;
import org.example.banco.entity.Conta;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContaService {
    private final ContaRepository contaRepository;

    @Transactional
    public void excluirContaDb(Long id) {
        contaRepository.deleteById(id);
    }

    public List<Conta> consultarContasDb() {
        return contaRepository.findAll();
    }

    public Conta consultarContaDb(Long id) throws Exception {
        return contaRepository.findById(id).orElseThrow(() -> new Exception("Conta não existe"));
    }
    @Transactional
    public void incluirContaDb(String nome, BigDecimal saldo) {
        Conta conta = new Conta(null, nome, (saldo));
        contaRepository.save(conta);
    }

    @Transactional
    public void depositar(long l, BigDecimal v) throws Exception {
        Conta c = consultarContaDb(l);
        c.depositar((v));
    }
    @Transactional
    public void debitar(long l, BigDecimal v) throws Exception {
        Conta c = consultarContaDb(l);
        c.debitar((v));
        contaRepository.save(c);
    }

    @Transactional
    public void transferir(long origem, long destino, BigDecimal v) throws Exception {
        debitar(origem, v);
        depositar(destino, v);

    }
}
